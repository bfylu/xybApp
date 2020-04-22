package cn.payadd.majia.exception;

import android.app.Application;
import android.content.res.Resources;

import com.fdsj.credittreasure.application.BaseApplication;

public class ExceptionUtil {

	/**
	 * 获得错误码描述
	 * @param code
	 * @return
	 */
	public static String getCodeDesc(String code) {

		String txt = null;

		try {
			Application appContext = BaseApplication.getAppContext();
			Resources res = appContext.getResources();
			int id = res.getIdentifier("exp_code_" + code, "string", appContext.getPackageName());
			txt = res.getString(id);
		} catch (Exception e) {
			e.printStackTrace();
			txt = "系统描述未定义，code=" + code;
		}

		return txt;
	}

}
