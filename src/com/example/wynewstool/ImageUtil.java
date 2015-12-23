package com.example.wynewstool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
//import android.util.Log;

/**
 * 图片缓存的方法
 * 
 * @author Administrator
 *
 */
public class ImageUtil {
	/**
	 * 图片路径
	 */
	private static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory() + "kanfan_image/images";
	/**
	 * 判断sdcard
	 * 
	 * @return
	 */
	public static boolean isScard() {
		//Log.d("print", "图片路径"+IMAGE_PATH);
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	public static void saveImage(String url, Bitmap bitmap) {
		if (!isScard()) {
			return;
		}
		File file = new File(IMAGE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					new FileOutputStream(new File(file, "" + url.hashCode())));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Bitmap getImage(String url) {

		if (!isScard()) {
			return null;
		}
		File file = new File(IMAGE_PATH, "" + url.hashCode());
		if (file.exists()) {

			return BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return null;

	}
}
