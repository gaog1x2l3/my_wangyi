package com.example.wynewsview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

public class MySlidingMenu extends ViewGroup {

	// 主面板最小的缩放倍数
	private static final float MIN_SCALE = 0.75f;

	// 用来判断用户操作类型的距离
	private static final int MIN_DIS = 100;

	// 还没有判断出用户意图的状态
	private static final int TYPE_NONE = 0;
	// 左右滑动状态
	private static final int TYPE_LEFT_RIGHT = 1;
	// 上下滑动状态
	private static final int TYPE_UP_DOWN = 2;

	// 当前用户的操作状态
	private int operate_type = TYPE_NONE;

	// 导航栏
	private ViewGroup navContainer;
	// 主面板
	private ViewGroup mainContainer;

	// 动画的计算工具
	private Scroller mScroller;

	public MySlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);

		mScroller = new Scroller(context,
				new AccelerateDecelerateInterpolator());
	}

	public MySlidingMenu(Context context) {
		super(context, null);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// 判断出用户的操作意图是左右滑动还是上下滑动
		getOperateType(ev);
		System.out.println("operate_type = " + operate_type);

		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (operate_type == TYPE_LEFT_RIGHT) {// 左右操作，则不向下分发事件

				// 计算出本次偏移量
				int dis_x = (int) (ev.getX() - pointF.x);

				int curLeft = dis_x + mainContainer.getLeft();
				if (curLeft < 0) {
					curLeft = 0;
				} else if (curLeft > navContainer.getWidth()) {
					curLeft = navContainer.getWidth();
				}

				mainContainer.layout(curLeft, mainContainer.getTop(), curLeft
						+ mainContainer.getMeasuredWidth(),
						mainContainer.getBottom());

				// 计算当前偏移量所占的比例
				float scale = curLeft / (float) navContainer.getWidth();

				animMain(scale);
				animNav(scale);

				// 让上一次计算的终点作为下一次计算的起点
				pointF.x = ev.getX();
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			operate_type = TYPE_NONE;
			// 根据当前主面板的坐标去看看是打开主面板，还是关闭主面板
			int left = mainContainer.getLeft();

			if (left > navContainer.getWidth() >> 1) {// 打开主面板
				open();
			} else {// 关闭主面板
				close();
			}

			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	// 对导航栏进行动画
	private void animNav(float scale) {
		// 计算主面板的缩放倍数，只能在0.75~1之间
		float naviScale = scale * (1 - MIN_SCALE) + MIN_SCALE;
		ViewCompat.setScaleX(navContainer, naviScale);
		ViewCompat.setScaleY(navContainer, naviScale);

		ViewCompat.setTranslationX(navContainer,
				-(navContainer.getWidth() >> 1) * (1 - scale));
	}

	// 对主面板进行缩放动画
	private void animMain(float scale) {
		// 这种方式只能在3.0以上的版本中使用
		// mainContainer.setScaleX(1 - scale);
		// mainContainer.setScaleY(1 - scale);
		// 计算主面板的缩放倍数，只能在1~0.75之间
		float mainScale = (1 - scale) * (1 - MIN_SCALE) + MIN_SCALE;
		ViewCompat.setScaleX(mainContainer, mainScale);
		ViewCompat.setScaleY(mainContainer, mainScale);

		// 计算因为缩小而产生空白
		int x = (int) (mainContainer.getWidth() * (1 - mainScale) / 2);
		ViewCompat.setTranslationX(mainContainer, -x);
	}

	/**
	 * 本方法在每次绘制的时候都会被调用
	 */
	@Override
	public void computeScroll() {
		// 判断动画是否已经执行完毕
		if (mScroller.computeScrollOffset()) {
			// 从计算工具中获取当前时刻我应该滚动到哪里
			int curX = mScroller.getCurrX();
			// 重新布局主面板
			mainContainer.layout(curX, mainContainer.getTop(), curX
					+ mainContainer.getMeasuredWidth(),
					mainContainer.getBottom());

			// 计算当前偏移量所占的比例
			float scale = curX / (float) navContainer.getWidth();
			animMain(scale);
			animNav(scale);
			// 要求重绘
			ViewCompat.postInvalidateOnAnimation(this);
		}

	}

	/**
	 * 打开主面板
	 */
	public void open() {
		int curX = mainContainer.getLeft();
		// 第三个参数是偏移量，不是目的地,这句话只是让动画开始计算，并没有真正的改变坐标
		mScroller.startScroll(curX, 0, navContainer.getWidth() - curX, 0);
		// 要求重绘
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/**
	 * 关闭主面板
	 */
	public void close() {
		int curX = mainContainer.getLeft();
		// 第三个参数是偏移量，不是目的地,这句话只是让动画开始计算，并没有真正的改变坐标
		mScroller.startScroll(curX, 0, -curX, 0);
		// 要求重绘
		ViewCompat.postInvalidateOnAnimation(this);
	}

	// 记录坐标点的对象
	private PointF pointF = new PointF();

	// 判断出用户的操作意图是左右滑动还是上下滑动
	private void getOperateType(MotionEvent ev) {
		if (operate_type != TYPE_NONE) {// 如果已经判断过了用户的操作意图，则不再判断
			return;
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:

			// 记录一个按下的坐标点
			pointF.x = ev.getX();
			pointF.y = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			// 获取本次移动到的坐标
			float x = ev.getX();
			float y = ev.getY();

			// 计算两点之间的距离
			double distance = Math.sqrt(Math.pow((pointF.x - x), 2)
					+ Math.pow((pointF.y - y), 2));

			if (distance < MIN_DIS) {// 还没有达到判定条件
				return;
			}

			float dis_x = Math.abs(x - pointF.x);
			float dis_y = Math.abs(y - pointF.y);

			if (dis_x > dis_y) {// 左右操作
				operate_type = TYPE_LEFT_RIGHT;
			} else {// 上下操作
				operate_type = TYPE_UP_DOWN;
			}

			// 更新计算移动的起始点
			pointF.x = x;
			break;

		default:
			break;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int w = MeasureSpec.getSize(widthMeasureSpec);
		// 让导航栏占本容器的3分之一
		int nw = w / 2;
		int navSpec = MeasureSpec.makeMeasureSpec(nw, MeasureSpec.EXACTLY);
		// 对导航栏进行测量
		navContainer.measure(navSpec, heightMeasureSpec);

		mainContainer.measure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		navContainer.layout(l, t, navContainer.getMeasuredWidth(), b);
		mainContainer.layout(l, t, r, b);
	}

	/**
	 * 在xml解析完成的时候被调用
	 */
	@Override
	protected void onFinishInflate() {
		int count = getChildCount();
		if (count < 2) {
			throw new IllegalArgumentException(
					"哥们，本容器必须有两个子容器，并且第一个是导航栏，第二个是主面板");
		}

		// 找到导航栏和主面板
		View naviga = getChildAt(0);
		View main = getChildAt(1);

		if (!(naviga instanceof ViewGroup) || !(main instanceof ViewGroup)) {
			throw new IllegalArgumentException(
					"哥们，本容器的直接子View必须是容器，并且第一个是导航栏，第二个是主面板");
		}

		navContainer = (ViewGroup) naviga;
		mainContainer = (ViewGroup) main;
	}

}
