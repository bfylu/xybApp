package cn.payadd.majia.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fdsj.credittreasure.R;

import cn.payadd.majia.api.JSAPI;
import cn.payadd.majia.constant.ResourceConstants;

/**
 * Created by df on 2017/8/4.
 */

public class ResetPasswordActivity extends BaseActivity{
    private WebView wvContent;
    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initView() {
        setTitleCenterText("重置密码");
        setTitleBackButton();
        wvContent = (WebView) findViewById(R.id.wv_content);

        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置允许JS弹窗
        wvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvContent.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wvContent.loadUrl(ResourceConstants.getResetByPhoneURI());
        wvContent.addJavascriptInterface(new JSAPI(this),"mpos");
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPermission() {

    }
}
