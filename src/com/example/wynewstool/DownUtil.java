package com.example.wynewstool;

import android.os.Handler;

public class DownUtil {

	public static final int TYPE_JSON = 0;// JOSN 字符串
	public static final int TYPE_IAMGE = 1;// image图片
	private static Handler handler = new Handler();

	public static void down(final int type, final String url, final OnDownCompelet onDownCompelet) {
		HttpUtil.executor.execute(new Runnable(){
			public void run() {
				switch (type) {
				case DownUtil.TYPE_JSON:
					HttpUtil.getByteByURL(url, new DownCallBack() {
						@Override
						public void processData(byte[] b) {
							final Object result = HttpUtil.getJSON(b);
							handler.post(new Runnable() {
								@Override
								public void run() {
									// 在主线程中执行的方法
									// 下载完以后得到一个obj
									onDownCompelet.downCompelet(url, result);	
								}
							});
						}
					});
					break;
				case DownUtil.TYPE_IAMGE:
					HttpUtil.getByteByURL(url, new DownCallBack() {
						
						@Override
						public void processData(byte[] b) {
							final Object result = HttpUtil.getBitmap(b);
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									handler.post(new Runnable() {
										
										@Override
										public void run() {
											// 在主线程中执行的方法
											// 下载完以后得到一个obj
											onDownCompelet.downCompelet(url, result);
											
										}
									});
								}
							});
							
						}
					});
					break;

				default:
					break;
				}			
			}	
		});
			
	}

	public interface OnDownCompelet {
		void downCompelet(String url, Object obj);
	}
}
