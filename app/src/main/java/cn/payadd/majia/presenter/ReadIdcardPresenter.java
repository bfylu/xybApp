package cn.payadd.majia.presenter;

import android.app.Activity;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by zhengzhen.wang on 2017/6/19.
 */

public class ReadIdcardPresenter extends BasePresenter {

    private IActivity iActivity;

    public ReadIdcardPresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void applyInstallment(String installmentType, String installmentAmt) {

        applyInstallment(installmentType, installmentAmt,null, null, null);
    }
    public void applyInstallment(String installmentType, String installmentAmt,String goodsList) {

        applyInstallment(installmentType, installmentAmt,goodsList, null, null);
    }

    public void applyInstallment(String installmentType, String installmentAmt,String realName, String idcard) {

        applyInstallment(installmentType, installmentAmt,null, realName, idcard);
    }

    public void applyInstallment(String installmentType, String installmentAmt,String goodsList,String realName, String idcard) {

        Map<String, String> data = new HashMap<>();
        data.put("installmentType", installmentType);
        data.put("orderAmount", installmentAmt);
        if (!TextUtils.isEmpty(realName)) {
            data.put("realName", realName);
        }
        if (!TextUtils.isEmpty(idcard)) {
            data.put("idCard", idcard);
        }
        if(!TextUtils.isEmpty(goodsList)){
            data.put("goodsList",goodsList);
        }
        sendToServer(AppService.APPLY_INSTALLMENT, data, new ICallback() {

            @Override
            public void exec(Object params) {

                iActivity.updateModel("applyInstallment", params);
            }
        });
    }

}
