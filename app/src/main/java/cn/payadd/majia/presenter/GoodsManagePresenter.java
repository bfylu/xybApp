package cn.payadd.majia.presenter;

import android.app.Activity;

import java.util.HashMap;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by df on 2017/9/30.
 */

public class GoodsManagePresenter extends BasePresenter{
    private IActivity iActivity;

    public GoodsManagePresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void getPlatformUrl() {
        sendToServer(AppService.GET_GOOD_MANAGER_URL, new HashMap<String, String>(), new ICallback() {

            @Override
            public void exec(Object params) {
                iActivity.updateModel(AppService.GET_GOOD_MANAGER_URL, params);
            }
        });
    }
}
