package cn.payadd.majia.util;

import android.app.Activity;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import cn.payadd.majia.activity.BaseActivity;

/**
 * Created by zhengzhen.wang on 2017/10/12.
 */

public class MyWebChromeClient extends WebChromeClient {

    private WebCall webCall;

    private BaseActivity activity;

    public MyWebChromeClient(BaseActivity activity) {
        this.activity = activity;
    }

    public void setWebCall(WebCall webCall) {
        this.webCall = webCall;
    }

    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (webCall != null)
            webCall.fileChose(uploadMsg);
    }

    // For Android < 3.0
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    // For Android > 4.1.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    // For Android > 5.0
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {
        if (webCall != null)
            webCall.fileChose5(filePathCallback);
//        return super.onShowFileChooser(webView, filePathCallback,
//                fileChooserParams);
        return true;
    }

    public interface WebCall {
        void fileChose(ValueCallback<Uri> uploadMsg);

        void fileChose5(ValueCallback<Uri[]> uploadMsg);
    }
}
