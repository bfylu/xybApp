package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by Administrator on 2017/07/19 0019.
 */

public class PaymentDetailPresenter extends BasePresenter {

    public static final String LOG_TAG = "InstallmentPresenter";

    private IActivity iActivity;

    public PaymentDetailPresenter(Activity activity) {
        super(activity);
        this.iActivity = (IActivity) activity;
    }
    public void orderRefund(String orderNo,String orderAmt){
        Map<String, String> data = new HashMap<>();
        data.put("orderNo",orderNo);
        data.put("refundAmount",orderAmt);
        sendToServer(AppService.ORDER_REFUND, data, new ICallback() {
            @Override
            public void exec(Object params) {
                Map<String,String> respData = (Map<String, String>) params;
                if(iActivity != null){
                    iActivity.updateModel("refund",respData);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iActivity != null) {
                    iActivity.updateModel("refundFail", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iActivity != null) {
                    iActivity.updateModel("exception", params);
                }
            }
        });
    }
    public void queryOrderDetail(String orderNo){
        Map<String, String> data = new HashMap<>();
        data.put("orderNo",orderNo);

        sendToServer(AppService.ORDER_QUERY, data, new ICallback() {
            @Override
            public void exec(Object params) {
                Map<String,String> respData = (Map<String, String>) params;
               if(iActivity!=null){
                   iActivity.updateModel("orderQuery",respData);
               }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iActivity != null) {
                    iActivity.updateModel("orderQueryFail", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iActivity != null) {
                    iActivity.updateModel("exception", params);
                }
            }
        });
    }
}
