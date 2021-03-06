package cn.payadd.majia.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.constant.ResourceConstants;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.jsInterface.GoodSelectJSInterface;
import cn.payadd.majia.jsInterface.GoodsManangerJSInterface;
import cn.payadd.majia.presenter.GoodsManagePresenter;
import cn.payadd.majia.util.MyWebChromeClient;
import cn.payadd.majia.view.CommonDialog;

/**
 * Created by df on 2017/12/25.
 */

public class SelectGoodsActivity extends BaseActivity implements IActivity{
    private WebView wvContent;

    public static final String SCAN_CALLBACK = "AppCallback.setBarCode";

    public static final int GOODS_MANAGE_FINISH = 2;
    public static final int WEBVIEW_REQUEST_CODE = 1;



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
        wvContent.getSettings().setAllowFileAccessFromFileURLs(true);
        wvContent.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wvContent.addJavascriptInterface(new GoodSelectJSInterface(this,wvContent), "App");

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
                            return new WebResourceResponse("application/x-javascript", "utf-8", SelectGoodsActivity.this.getAssets().open("js/jquery.min.js"));
                        }
                        if (url.contains("iscroll.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", SelectGoodsActivity.this.getAssets().open("js/iscroll.js"));
                        }
                        if (url.contains("jquery-form.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", SelectGoodsActivity.this.getAssets().open("js/jquery-form.js"));
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("result", "加载本地js错误：" + e.toString());
                }
                return super.shouldInterceptRequest(view, url);
            }
        });

        wvContent.loadUrl(ResourceConstants.getChooseGoodsURI());
    }


    @Override
    public void initData() {
    }

    @Override
    protected void initPermission() {

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if (requestCode == WEBVIEW_REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle responseBundle = data.getExtras();
                if (responseBundle == null) {
                    return;
                }
                if (responseBundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = responseBundle.getString(CodeUtils.RESULT_STRING);
                    wvContent.loadUrl("javascript:" + SCAN_CALLBACK + "('" + result + "')");
                }
            }
        }else if(requestCode == GOODS_MANAGE_FINISH){
            wvContent.reload();
        }
    }
    // 设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvContent.canGoBack()) {
            wvContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void updateModel(String key, Object data) {
        if ("applyInstallment".equals(key)) {
            Map<String, String> respData = (Map<String, String>) data;
            String respCode = respData.get("respCode");
            if ("000000".equals(respCode)) {
                Map<String, String> map = (Map<String, String>) data;
                String installmentUrl = map.get("installmentUrl");
                String installmentAmt = map.get("installmentAmt");
                String orderAmount = map.get("orderAmount");
                String downPayment = map.get("downPayment");
                String linkUrl = map.get("linkUrl");
                Intent intent = new Intent(this, InstallmentPayActivity.class);
                intent.putExtra(InstallmentPayActivity.KEY_DOWN_PAYMENT, downPayment);
                intent.putExtra(InstallmentPayActivity.KEY_INSTALLMENT_AMOUNT, installmentAmt);
                intent.putExtra(InstallmentPayActivity.KEY_INSTALLMENT_URL, installmentUrl);
                intent.putExtra(InstallmentPayActivity.KEY_ORDER_AMOUNT, orderAmount);
                intent.putExtra(InstallmentPayActivity.KEY_LINK_URL, linkUrl);
                intent.putExtra(InstallmentPayActivity.KEY_SOURCE,"h5");
                startActivity(intent);
            }
        }
    }

    public WebView getWvContent() {
        return wvContent;
    }

    public void setWvContent(WebView wvContent) {
        this.wvContent = wvContent;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void refresh(){
        wvContent.reload();
    }

}
