package cn.payadd.majia.presenter;

import android.app.Activity;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by zhengzhen.wang on 2017/6/12.
 */

public class AcquirePresenter extends BasePresenter {

    private IActivity iActivity;

    public AcquirePresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void pay(String amt, String payMethod, String payType, String authCode,ICallback failCallback){

        pay(amt,payMethod,payType,authCode,null,failCallback);
    }
    public void pay(String amt, String payMethod, String payType, String authCode,String goodsList, ICallback failCallback) {

        Map<String, String> data = new HashMap<>();
        data.put("amount", amt);
        if (!TextUtils.isEmpty(authCode)) {
            data.put("authCode", authCode);
        }
        if (!TextUtils.isEmpty(goodsList)) {
            data.put("goodsList", goodsList);
        }
        if(EnvironmentConfig.isPos()){
            data.put("bradging",EnvironmentConfig.getPosType());
        }
        data.put("payMethod", payMethod);
        if(!TextUtils.isEmpty(payType)){
            data.put("payType", payType);
        }


        sendToServer(AppService.ORDER_SUBMIT, data, new ICallback() {

            @Override
            public void exec(Object params) {

                iActivity.updateModel("pay", params);

            }
        }, failCallback, failCallback);
    }

}
