package com.example.wynewsview;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

public class PullRefreshContainer extends ViewGroup {

	/**
	 * 刷新事件通知接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface onRefreshListener {
		void onRefresh(PullRefreshContainer container);
	}

	private onRefreshListener mListener;

	/**
	 * 设置下拉刷新事件监听器的方法
	 * 
	 * @param listener
	 */
	public void setonRefreshListener(onRefreshListener listener) {
		mListener = listener;
	}

	// 用来判断用户操作类型的距离
	private static final int DISTANCE = 10;

	// 下拉刷新提示头,默认是不显示的
	private LinearLayout refreshHead;

	// 列表
	private AbsListView absListView;

	// 下拉刷新提示文案的TextView
	private TextView tipTv;

	public PullRefreshContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PullRefreshContainer(Context context) {
		super(context);
		init(context);
	}

	private Scroller mScroller;

	private void init(Context context) {

		mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());

		refreshHead = new LinearLayout(context);
		refreshHead.setBackgroundColor(0xff00ff00);

		ProgressBar bar = new ProgressBar(context);
		refreshHead.addView(bar);

		tipTv = new TextView(context);
		tipTv.setText("下拉即可刷新");
		tipTv.setTextSize(22);

		refreshHead.addView(tipTv);
		// 让提示内容居中
		refreshHead.setGravity(Gravity.CENTER);

		// 把下拉刷新的头放进容器中
		addView(refreshHead);
	}

	/**
	 * 设置列表控件
	 * 
	 * @param listView
	 */
	public void setAbsListView(AbsListView listView) {
		this.absListView = listView;
		// 请求重新测量以及布局
		requestLayout();
	}

	/**
	 * 还原UI的状态
	 */
	public void restoreUIState() {
		mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 创建头测量的参数
		int headHeightSpec = MeasureSpec.makeMeasureSpec(100, MeasureSpec.EXACTLY);
		// 测量头部
		refreshHead.measure(widthMeasureSpec, headHeightSpec);

		if (absListView == null) {// 如果为空则可能是在layout文件中配置的
			View child = getChildAt(1);
			if (child instanceof AbsListView) {
				absListView = (AbsListView) child;
			} else {
				throw new IllegalArgumentException("兄弟,下拉刷新容器的第一个孩子必须是AbsListView!!");
			}

			if (absListView == null) {
				throw new IllegalArgumentException(
						"兄弟,是不是忘记配置AbsListView了？也可以通过调用setAbsListView(AbsListView listView)来设置哦!");
			}
		}

		// 测量列表
		absListView.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		absListView.layout(l, t, r, b);
		refreshHead.layout(l, -refreshHead.getMeasuredHeight(), r, 0);
	}

	private Point point = new Point();

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			point.y = (int) ev.getY();
			super.dispatchTouchEvent(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			dealMove(ev);
			break;
		case MotionEvent.ACTION_UP:

			int scrollY = getScrollY();
			if (scrollY <= -refreshHead.getMeasuredHeight()) {// 说明头部完全被拉出，触发刷新事件

				mScroller.startScroll(0, scrollY, 0, -refreshHead.getMeasuredHeight() - scrollY);

				if (mListener != null) {// 这里也可以抛一个异常提醒使用者
					mListener.onRefresh(this);
				}

			} else {// 头部没有完全被拉出来，还原、不触发刷新事件
				mScroller.startScroll(0, scrollY, 0, -scrollY);
			}

			invalidate();

			operatType = TYPE_NONE;
			super.dispatchTouchEvent(ev);
			break;

		default:
			break;
		}
		// super.dispatchTouchEvent(ev);
		return true;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int cy = mScroller.getCurrY();
			scrollTo(0, cy);
			invalidate();
		}
	}

	private void dealMove(MotionEvent ev) {
		// 获取本次用户的操作是下拉还是上推
		getOperatType(ev);
		switch (operatType) {
		case TYPE_NONE:// 没有判断出操作模式，则不动

			break;
		case TYPE_PULL:// 下拉操作
			// 如果ListView的状态是初始状态（没有滚动过，那么这个时候下拉则可以让头部滚动出来）
			if (absListView.getFirstVisiblePosition() == 0
					&& absListView.getChildAt(0).getTop() == 0) {
				// int ty = (int) ev.getY();
				// // 计算偏移量
				// int dy = ty - point.y;
				//
				// scrollBy(0, -dy);
				//
				// point.y = ty;
				moveCanvas(ev);
			} else {
				super.dispatchTouchEvent(ev);
			}

			break;
		case TYPE_PUSH:// 上推操作
			if (getScrollY() != 0) {// 如果头部被拉出来了，则让头部滚动到0
				moveCanvas(ev);

			} else {
				// 事件统统分发给ListView
				super.dispatchTouchEvent(ev);
			}
			break;

		default:
			break;
		}

	}

	private void moveCanvas(MotionEvent ev) {
		int ty = (int) ev.getY();
		// 计算偏移量
		int dy = ty - point.y;

		int dst_y = getScrollY() - dy;

		// System.out.println("本次想要滚到: " + dst_y);
		if (dst_y > 0) {
			dy = getScrollY();
		}

		scrollBy(0, -dy);

		point.y = ty;
	}

	private static final int TYPE_PULL = 0;
	private static final int TYPE_PUSH = 1;
	private static final int TYPE_NONE = -1;

	private int operatType = TYPE_NONE;

	private void getOperatType(MotionEvent ev) {

		if (operatType != TYPE_NONE) {// 如果本次操作已经判断出了模式，不用再判断了
			return;
		}

		int y = (int) ev.getY();
		// 计算用户按下手指之后的总体偏移量
		int dis_y = y - point.y;
		if (Math.abs(dis_y) >= DISTANCE) {// 说明判断出了用户的操作类型
			if (dis_y > 0) {// 下拉
				operatType = TYPE_PULL;
			} else {
				operatType = TYPE_PUSH;
			}

			// 以计算出操作模式的那一点作为事件处理的起始点
			point.y = y;
		}

	}

}
