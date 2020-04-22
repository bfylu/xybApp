package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by df on 2017/9/19.
 */

public class BankCardPresenter extends BasePresenter{

    private IActivity iActivity;

    public BankCardPresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void queryBankCard(){
        sendToServer(AppService.QUERY_BANK_CARD, new HashMap<String, String>(), new ICallback() {

            @Override
            public void exec(Object params) {
                iActivity.updateModel(AppService.QUERY_BANK_CARD, params);
            }
        });
    }

    public void bindCard(String settleType, String cardNo, String mobile, String bankName,
                         String provinceCode, String cityCode, String regionCode,String bankId,String cardType,ICallback failCallback) {
        Map<String, String> data = new HashMap<>();
        data.put("bankCardNo", cardNo);
        data.put("settlementType", settleType);
        data.put("bankName", bankName);
        data.put("provinceCode", provinceCode);
        data.put("cityCode", cityCode);
        data.put("regionCode", regionCode);
        data.put("mobilePhone", mobile);
        data.put("bankId",bankId);
        data.put("cardType",cardType);

        sendToServer(AppService.BIND_CARD, data, new ICallback() {

            @Override
            public void exec(Object params) {
                iActivity.updateModel("bindCard", params);
            }
        }, failCallback, failCallback);
    }
    public void unbind(String bankCardId,ICallback failCallback){
        Map<String, String> data = new HashMap<>();
        data.put("id", bankCardId);
        sendToServer(AppService.UNBIND, data, new ICallback() {

            @Override
            public void exec(Object params) {
                iActivity.updateModel("unbind", params);
            }
        }, failCallback, failCallback);
    }
}
