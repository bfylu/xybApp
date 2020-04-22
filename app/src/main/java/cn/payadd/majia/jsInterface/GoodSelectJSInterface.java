package cn.payadd.majia.jsInterface;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

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
public class GoodSelectJSInterface{

    private Activity mContext;
    private WebView webView;
    private ReadIdcardPresenter readIdcardPresenter;
    private GoodsManagePresenter presenter;
    /**
     * 扫描跳转Activity RequestCode
     */


    public GoodSelectJSInterface(Activity context,WebView webView) {
        this.mContext = context;
        this.webView = webView;
        readIdcardPresenter = new ReadIdcardPresenter(this.mContext);
    }

    @JavascriptInterface
    public void scanBarCode(){
        Intent intent = new Intent(mContext, BarCodeActivity.class);
        mContext.startActivityForResult(intent, SelectGoodsActivity.WEBVIEW_REQUEST_CODE);
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
    @JavascriptInterface
    public void openGoodsManager(){
        Intent intent = new Intent(mContext, GoodsManagerActivity.class);
//        intent.putExtra("callback", new GoodsManagerActivity.Callback() {
//            public void exec() {
//                webView.reload();
//            }
//        });

        mContext.startActivityForResult(intent,SelectGoodsActivity.GOODS_MANAGE_FINISH);
    }

    @JavascriptInterface
    public void installmentPay(String installmentType,String amount,String goodsList){
        readIdcardPresenter.applyInstallment(installmentType, amount,goodsList);


    }

    @JavascriptInterface
    public void pay(String amount,String goodsList){
        Intent intent = new Intent(mContext, AcquireActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("goodsList", goodsList);
        mContext.startActivity(intent); //收款界面
    }
    @JavascriptInterface
    public String getUserInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("merCode", Utilities.getMerCode(mContext));
            jsonObject.put("username", Utilities.getUsername(mContext));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
