package com.example.wynewstool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 网络连接工具类
 *
 */
public class HttpUtil {
	//创建线程池。
	public  static ExecutorService executor = Executors.newFixedThreadPool(2);
	/**
	 * 根据网址下载数据，并将数据交给回调方法。
	 * @param url   网址
	 * @param callback  处理下载的数据的对象。
	 */
	public static void getByteByURL(final String url,final DownCallBack callback) {
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					URL u = new URL(url);
					HttpURLConnection conn = (HttpURLConnection) u.openConnection();
					conn.setReadTimeout(5000);
					conn.setRequestMethod("GET");
					InputStream in = conn.getInputStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					int len = 0;
					byte[] buffer = new byte[1024 * 2];
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					byte[] b = out.toByteArray();
					callback.processData(b);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});	
	}

	/**
	 * 将字节数组转换为JSONObject对象。
	 * 
	 */
	public static JSONObject getJSON(byte[] b ) {
		if (b != null) {
			try {
				String data = new String(b, "utf-8");
				JSONObject json = new JSONObject(data);
				return json;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 将字节数组转换为图片对象
	 * 
	 */
	public static Bitmap getBitmap(byte[] b) {
		Bitmap bitmap = null;
		if (b != null) {
			bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return bitmap;
	}
}
