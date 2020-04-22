package cn.payadd.majia.activity;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fdsj.credittreasure.R;
import com.utils.Config;

import java.io.IOException;

import cn.payadd.majia.constant.ResourceConstants;
import cn.payadd.majia.jsInterface.GoodDetailJSInterface;
import cn.payadd.majia.util.MyWebChromeClient;

/**
 * Created by df on 2017/12/29.
 */

public class GoodsDetailActivity extends BaseActivity{
    private WebView wvContent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        findViewById(R.id.include_title).setVisibility(View.GONE);
        setTitleCenterText("选择商品");
        setTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wvContent.canGoBack()) {
                    wvContent.goBack();
                }else{
                    finish();
                }
            }
        });
//        findViewById(R.id.include_title).setVisibility(View.GONE);
        /*WebView 配置*/
        wvContent = (WebView) findViewById(R.id.wv_content);
        wvContent.getSettings().setJavaScriptEnabled(true);
        //设置 缓存模式
        wvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        wvContent.getSettings().setDomStorageEnabled(true);
        wvContent.getSettings().setTextZoom(100);
        // 设置允许JS弹窗
        wvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvContent.getSettings().setDomStorageEnabled(true);

        wvContent.addJavascriptInterface(new GoodDetailJSInterface(this,wvContent), "App");

        MyWebChromeClient myWebChromeClient = new MyWebChromeClient(this);
        wvContent.setWebChromeClient(myWebChromeClient);
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                setTitleCenterText(view.getTitle());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        if (url.contains("jquery.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", GoodsDetailActivity.this.getAssets().open("js/jquery.min.js"));
                        }
                        if (url.contains("iscroll.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", GoodsDetailActivity.this.getAssets().open("js/iscroll.js"));
                        }
                        if (url.contains("jquery-form.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", GoodsDetailActivity.this.getAssets().open("js/jquery-form.js"));
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("result", "加载本地js错误：" + e.toString());
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
        String id = getIntent().getStringExtra("id");

        wvContent.loadUrl(ResourceConstants.getGoodsDetailURI() + "?id=" + id);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPermission() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}