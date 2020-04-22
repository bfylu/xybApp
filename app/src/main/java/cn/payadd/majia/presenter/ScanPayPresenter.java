package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.utils.AppUtil;
import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import cn.payadd.majia.constant.AppService2;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by df on 2018/2/6.
 */

public class ScanPayPresenter extends NewBasePresenter{
    private IActivity iActivity;


    public ScanPayPresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void scanPay(String authCode,int bizType){
        JSONObject data = new JSONObject();
        try {
            data.put("authCode", authCode);
            data.put("bizType", bizType);
            sendJsonToServer(AppService2.SCANPAY, data, false, new ICallback() {
                @Override
                public void exec(Object params) {
                    iActivity.updateModel(AppService2.SCANPAY,params);
                }
            }, new ICallback() {
                @Override
                public void exec(Object params) {
                    iActivity.updateModel(AppService2.SCANPAY,params);
                }
            }, new ICallback() {
                @Override
                public void exec(Object params) {
                    iActivity.updateModel(AppService2.SCANPAY,params);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void PayAcquireConfirm(String authCode,int bizType){
        JSONObject data = new JSONObject();
        try {
            data.put("authCode", authCode);
            data.put("bizType", bizType);
            sendJsonToServer(AppService2.ACQUIRE_CONFIRM, data, false, new ICallback() {
                @Override
                public void exec(Object params) {
                    iActivity.updateModel(AppService2.ACQUIRE_CONFIRM,params);
                }
            }, new ICallback() {
                @Override
                public void exec(Object params) {
                    iActivity.updateModel(AppService2.ACQUIRE_CONFIRM,params);
                }
            }, new ICallback() {
                @Override
                public void exec(Object params) {
                    iActivity.updateModel(AppService2.ACQUIRE_CONFIRM,params);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
