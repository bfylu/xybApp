package cn.payadd.majia.api;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import cn.payadd.majia.activity.LoginActivity;

/**
 * Created by df on 2017/8/4.
 */

public class JSAPI {

    private static final String LOG_TAG = "JSApi";
    private Activity activity;

    public JSAPI(Activity activity) {
        this.activity = activity;
    }
    @JavascriptInterface
    public void returnLogin(){
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
