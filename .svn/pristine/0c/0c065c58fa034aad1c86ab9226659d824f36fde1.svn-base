package cn.payadd.majia.activity;

import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fdsj.credittreasure.R;

import cn.payadd.majia.api.JSAPI;
import cn.payadd.majia.constant.ResourceConstants;

/**
 * Created by df on 2017/8/4.
 */

public class RegisterActivity extends BaseActivity {
    private WebView wvContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        setTitleCenterText("注册账号");
        setTitleBackButton();
        wvContent = (WebView) findViewById(R.id.wv_content);

        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置允许JS弹窗
        wvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return true;
            }
        });
        wvContent.addJavascriptInterface(new JSAPI(this), "mpos");
        wvContent.loadUrl(ResourceConstants.getRegisterURI());
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPermission() {

    }
}
