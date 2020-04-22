package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.adapter.WithdrawRecordAdapter;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by df on 2017/9/29.
 */

public class WithdrawPresenter extends BasePresenter{
    private IActivity iActivity;

    public WithdrawPresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void withdraw(String bankRecordId,String amount) {

        Map<String, String> data = new HashMap<>();
        data.put("id", bankRecordId);
        data.put("withdrawAmount", amount);
        sendToServer(AppService.WITHDRAW, data, new ICallback() {

            @Override
            public void exec(Object params) {
                iActivity.updateModel(AppService.WITHDRAW, params);
            }
        });
    }
    public void withdrawDetail(String issueNo) {

        Map<String, String> data = new HashMap<>();
        data.put("issueNo", issueNo);
        sendToServer(AppService.WITHDRAW_DETAIL, data, new ICallback() {

            @Override
            public void exec(Object params) {
                iActivity.updateModel(AppService.WITHDRAW_DETAIL, params);
            }
        });
    }
}
