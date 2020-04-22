package cn.payadd.majia.jsInterface;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import cn.payadd.majia.activity.AcquireActivity;
import cn.payadd.majia.activity.BarCodeActivity;
import cn.payadd.majia.activity.GoodsManagerActivity;
import cn.payadd.majia.activity.SelectGoodsActivity;
import cn.payadd.majia.presenter.GoodsManagePresenter;
import cn.payadd.majia.presenter.ReadIdcardPresenter;

/**
 * Created by df on 2017/12/26.
 */
@SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
public class GoodDetailJSInterface {

    private Activity mContext;
    private WebView webView;
    /**
     * 扫描跳转Activity RequestCode
     */


    public GoodDetailJSInterface(Activity context, WebView webView) {
        this.mContext = context;
        this.webView = webView;
    }

    @JavascriptInterface
    public void goBack(){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (webView.canGoBack()) {
                    webView.goBack();
                    webView.reload();
                }else{
                    mContext.finish();
                }
            }
        });
    }

}
