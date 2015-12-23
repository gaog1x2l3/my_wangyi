package com.example.wynewsview;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import com.example.wynewstool.DownUtil;
import com.example.wynewstool.DownUtil.OnDownCompelet;
import com.example.wynewstool.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * 自定义imageview控件
 * 
 * @author Administrator
 *
 */
public class MyImageView extends ImageView implements OnDownCompelet {

	public MyImageView(Context context) {
		super(context);
	}

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 一级缓存----根据最近最少使用的策略进行缓存移除
	private static LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(
			1024 * 1024) {
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {
			if (evicted) {
				// 缓存移除
				softCache.put(key, new SoftReference<Bitmap>(oldValue));
			}
		};

		protected int sizeOf(String key, Bitmap value) {
			return value.getHeight() * value.getRowBytes();
		};
	};
	// 二级缓存
	private static Map<String, SoftReference<Bitmap>> softCache = new HashMap<String, SoftReference<Bitmap>>();

	/**
	 * 调用时设置图片
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.setTag(url);
		Bitmap bitmap = getCacheBitmap(url);
		if (bitmap != null) {
			this.setImageBitmap(bitmap);
		} else {
			DownUtil.down(DownUtil.TYPE_IAMGE, url, this);
		}
	}

	// 接口回调
	@Override
	public void downCompelet(String url, Object obj) {
		if (this != null && obj != null & this.getTag() != null
				&& this.getTag().toString().equals(url)) {
			this.setImageBitmap((Bitmap) obj);
		}
		setCache(url, (Bitmap) obj);
	}

	private static void setCache(String key, Bitmap bitmap) {
		if(key!=null&&bitmap!=null){
			lruCache.put(key, bitmap);
			ImageUtil.saveImage(key, bitmap);
		}
	}

	/**
	 * 从缓存中获取
	 * 
	 * @param url
	 * @return
	 */
	private static Bitmap getCacheBitmap(String url) {
		Bitmap bitmap = null;
		bitmap = lruCache.get(url);
		if (bitmap == null) {
			if (softCache.containsKey(url)) {
				SoftReference<Bitmap> softBitmap = softCache.get(url);
				bitmap = softBitmap.get();
				if (bitmap != null) {
					lruCache.put(url, bitmap);
					softCache.remove(url);
				}
			}
		}
		if (bitmap == null) {
			bitmap = ImageUtil.getImage(url);
			if (bitmap != null) {
				lruCache.put(url, bitmap);
			}
		}
		return bitmap;
	}

}
