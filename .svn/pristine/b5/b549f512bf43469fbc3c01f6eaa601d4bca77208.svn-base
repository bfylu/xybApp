package com.fdsj.credittreasure.fragment;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fdsj.credittreasure.R;
import com.utils.Config;
import com.utils.Utilities;

import butterknife.BindView;


/**
 *
 */
@Deprecated
public class StatisticsFragment2 extends BaseFragment {

    @BindView(R.id.webview)
    WebView mWebView;

    private WebViewClient webViewClient = new WebViewClient() {

    };

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_statistics2;
    }

    @Override
    protected void initView() {

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        // 设置支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 使用localStorage则必须打开
        settings.setDomStorageEnabled(true);

        settings.setGeolocationEnabled(true);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.setWebViewClient(webViewClient);
        String url = String.format(Config.getH5Statistics(),
                new Object[]{Utilities.getUsernameForLogin(getActivity()), Utilities.getSessionToKen(getActivity()), Utilities.getLoginToken(getActivity())});
        mWebView.loadUrl(url);
    }

    @Override
    protected void initData() {

    }

}
