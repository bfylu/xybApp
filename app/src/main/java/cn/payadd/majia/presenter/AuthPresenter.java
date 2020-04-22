package cn.payadd.majia.presenter;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by df on 2017/11/29.
 */

public class AuthPresenter extends BasePresenter{
    private IActivity iActivity;

    public AuthPresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void auth(String name,String idCard,ICallback failCallback) {
        Map<String, String> data = new HashMap<>();
        data.put("idCard", idCard);
        data.put("ownerName",name);
        sendToServer(AppService.AUTH, data, new ICallback() {

            @Override
            public void exec(Object params) {
                iActivity.updateModel(AppService.AUTH, params);
            }
        }, failCallback, failCallback);
    }
}
