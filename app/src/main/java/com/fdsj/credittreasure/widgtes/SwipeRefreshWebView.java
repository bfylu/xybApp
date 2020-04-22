package com.fdsj.credittreasure.widgtes;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.fdsj.credittreasure.R;
import com.utils.LogUtil;

/**
 * Created by BXND on 2016-10-22.
 */

public class SwipeRefreshWebView extends LinearLayout {

    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;


    private Context context;

    private View view;

    public SwipeRefreshWebView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SwipeRefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        initView();
    }


    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.view_swiperefresh_webview, this, true);
        webView = (WebView) view.findViewById(R.id.progress_webView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                LogUtil.info("隐藏进度条", newProgress + "");
                if (newProgress >= 100) {
                    //隐藏进度条
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    if (!swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        // 优先使用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        String cacheDirPath = context.getFilesDir().getAbsolutePath() + "/webcache";
        LogUtil.info("webcache", cacheDirPath);
        //String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        webView.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
                String Url = webView.getUrl();
                LogUtil.info("url", Url);
                webView.loadUrl(Url);
            }
        });
    }

    public void webDestroy() {
        webView.destroy();
    }

    public void setWebLoadUrl(final String StrUrl) {
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                webView.loadUrl(StrUrl);
//            }
//        });
        webView.loadUrl(StrUrl);

//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//            }
//        });
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
    }

    public void SetWebLoadData(String StrUrl) {
        webView.loadData(StrUrl, "text/html; charset=utf-8", "utf-8");
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
    }

    public void goBack() {
        webView.goBack();
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }


}