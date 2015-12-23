package com.example.wynewsview;

import java.util.ArrayList;
import java.util.List;

import com.example.wynews.R;
import com.example.wynews.WebViewActivity;
import com.example.wynewsobject.HeadAdObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint({ "DefaultLocale", "ViewConstructor" })
public class MyViewPager extends RelativeLayout {
	/**
	 * 数据
	 */
	private List<HeadAdObject> topViewData;// 头部数据集合
	private List<ImageView> imagesData;// 图片数据
	private TopViewPagerListener onViewPager;// 接口对象
	/**
	 * 控件
	 */
	private ViewPager viewPager;// viewpager 控件
	private LinearLayout layout;// 小点的布局
	private MyViewPAgerAdapter adapter;// viewpager适配器

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(msg.arg1);
		};
	};

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param onViewPager
	 */
	public MyViewPager(Context context, TopViewPagerListener onViewPager) {
		super(context);
		this.onViewPager = onViewPager;
		init();
	}

	/**
	 * 接口定义
	 * 
	 * @author Administrator
	 * 
	 */
	public interface TopViewPagerListener {
		void onTopViewPagerListener(MyViewPager vp);
	}

	public void setData(List<HeadAdObject> topViewData) {
		this.topViewData = topViewData;
		downCompelet();
		changePhoto();
	}

	/**
	 * 对控件进行初始化
	 */
	public void downCompelet() {
		if (topViewData != null && topViewData.size() > 0) {

			imagesData = getImageView();
			adapter = new MyViewPAgerAdapter(imagesData);
			viewPager.setAdapter(adapter);
			// 初始化小点指示
			for (int i = 0; i < topViewData.size(); i++) {
				ImageView iv = new ImageView(getContext());
				if (i == 0) {
					iv.setImageResource(R.drawable.nf_arrows_1);
					iv.setTag("checked");
				} else {
					iv.setImageResource(R.drawable.nf_arrows_2);
				}
				layout.addView(iv);
			}
		} else {
			// 调用接口回调
			onViewPager.onTopViewPagerListener(this);
		}
	}

	/**
	 * 得到viewpager图片
	 * 
	 * @return
	 */
	public List<ImageView> getImageView() {
		List<ImageView> image = null;
		if (topViewData != null && topViewData.size() > 0) {
			image = new ArrayList<ImageView>();
			for (HeadAdObject obj : topViewData) {
				MyImageView miv = new MyImageView(getContext());
				miv.setUrl(obj.getImgSrc());
				miv.setScaleType(ScaleType.CENTER_CROP);
				image.add(miv);
			}
		}
		return image;
	}

	/**
	 * 有数据开始改变
	 */
	public void changePhoto() {
		// 设置变量，表示下标
		new Thread(new Runnable() {
			int index = 0;

			@Override
			public void run() {
				while (true) {
					// System.out.println(flag);
					Message message = handler.obtainMessage();
					message.arg1 = index;
					handler.sendMessage(message);
					index++;
					if (index == imagesData.size()) {
						index = 0;
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 初始化控件
	 */
	private void init() {

		LayoutInflater.from(getContext()).inflate(R.layout.viewpager_hav, this, true);
		viewPager = (ViewPager) findViewById(R.id.vp_hadnav);
		layout = (LinearLayout) findViewById(R.id.layout_dot);
		/**
		 * 给viewpager设置监听器
		 */
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * 通过viewpager的改变来改变TextView和小点的图标
			 */
			@Override
			public void onPageSelected(int arg0) {
				ImageView iv = (ImageView) layout.findViewWithTag("checked");
				iv.setImageResource(R.drawable.nf_arrows_2);
				iv.setTag(null);
				ImageView iv2 = (ImageView) layout.getChildAt(arg0);
				iv2.setImageResource(R.drawable.nf_arrows_1);
				iv2.setTag("checked");
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		layout.removeAllViews();
	}

	/**
	 * 
	 * viewpager适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class MyViewPAgerAdapter extends PagerAdapter {
		private List<ImageView> datas;

		public MyViewPAgerAdapter(List<ImageView> datas) {
			this.datas = datas;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(datas.get(position));
		}

		private int index;

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = datas.get(position);
			index = position;
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String path = topViewData.get(index).getLink();
					Intent intent = new Intent(getContext(), WebViewActivity.class);
					intent.putExtra("path", path);
					getContext().startActivity(intent);
					System.out.println(topViewData.get(index).getLink());
				}
			});
			container.addView(datas.get(position));
			return datas.get(position);
		}
	}
}
