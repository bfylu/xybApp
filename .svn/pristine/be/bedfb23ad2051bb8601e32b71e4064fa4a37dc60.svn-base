package cn.payadd.majia.jsInterface;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebHistoryItem;
import android.webkit.WebView;
import android.widget.Toast;

import cn.payadd.majia.activity.BarCodeActivity;
import cn.payadd.majia.activity.GoodsManagerActivity;
import cn.payadd.majia.activity.SelectGoodsActivity;

/**
 * Created by df on 2017/12/13.
 */

@SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
public class GoodsManangerJSInterface {
    private Activity mContext;
    private WebView webView;
    /**
     * 扫描跳转Activity RequestCode
     */


    public GoodsManangerJSInterface(Activity context,WebView webView) {
        this.mContext = context;
        this.webView = webView;
    }

    @JavascriptInterface
    public void scanBarCode(){
        Intent intent = new Intent(mContext, BarCodeActivity.class);
        mContext.startActivityForResult(intent, GoodsManagerActivity.WEBVIEW_REQUEST_CODE);
    }
    @JavascriptInterface
    public void goBack(){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                WebBackForwardList webBackForwardList = webView.copyBackForwardList();
//                for (int i = 0;i<webBackForwardList.getSize();i++){
//                    WebHistoryItem webHistoryItem = webBackForwardList.getItemAtIndex(i);
//                    webHistoryItem.getUrl();
//                    Log.d("historyUrlList", webHistoryItem.getUrl());
//                }
                if (webView.canGoBack()) {
                    webView.goBack();
                }else{
                    mContext.setResult(SelectGoodsActivity.GOODS_MANAGE_FINISH);
                    mContext.finish();
                }
            }
        });
    }

}
