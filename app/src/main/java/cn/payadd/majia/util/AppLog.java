package cn.payadd.majia.util;

import android.util.Log;

import com.utils.Config;

import java.util.HashSet;
import java.util.Set;

public class AppLog {

	private static final String LOG_TAG = "AppLog";
	private static final Set<String> EXCLUDE_TAG = new HashSet<>();
	
	static {
		EXCLUDE_TAG.add(LOG_TAG);
//		EXCLUDE_TAG.add(MPOSSecurity.LOG_TAG);
//		EXCLUDE_TAG.add(HttpUtil.LOG_TAG);
	}
	
	public static void v(String tag, String msg) {
		
		if (isPrint() && ! EXCLUDE_TAG.contains(tag)) {
			Log.v(tag, msg);
		}
	}
	
	public static void v(String msg) {
		
		v(LOG_TAG, msg);
	}
	
	public static void d(String tag, String msg) {
		
		if (isPrint() && ! EXCLUDE_TAG.contains(tag)) {
			Log.d(tag, msg);
		}
	}
	
	public static void d(String msg) {

		if (isPrint() && ! EXCLUDE_TAG.contains(LOG_TAG)) {
			d(LOG_TAG, msg);
		}
	}
	
	public static void i(String tag, String msg) {
		
		if (isPrint() && ! EXCLUDE_TAG.contains(tag)) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String msg) {

		if (isPrint() && ! EXCLUDE_TAG.contains(LOG_TAG)) {
			i(LOG_TAG, msg);
		}
	}
	
	public static void w(String tag, String msg) {
		
		if (isPrint() && ! EXCLUDE_TAG.contains(tag)) {
			Log.w(tag, msg);
		}
	}
	
	public static void w(String msg) {

		if (isPrint() && ! EXCLUDE_TAG.contains(LOG_TAG)) {
			w(LOG_TAG, msg);
		}
	}
	
	public static void e(String tag, String msg) {
		
		if (isPrint() && ! EXCLUDE_TAG.contains(tag)) {
			Log.e(tag, msg);
		}
	}
	
	public static void e(String msg) {
		
		e(LOG_TAG, msg);
	}

	private static boolean isPrint() {

		boolean flag = false;

		if (Config.isDevel() || Config.isTest()) {
			flag = true;
		}

		return flag;
	}
}
