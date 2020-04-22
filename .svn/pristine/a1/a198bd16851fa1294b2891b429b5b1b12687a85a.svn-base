package cn.payadd.majia.presenter;

import android.content.Context;
import android.os.Build;

import com.utils.AppUtil;
import com.utils.Config;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.ICallback;

/**
 * Created by zhengzhen.wang on 2017/7/3.
 */

public class UpdatePresenter extends BasePresenter {

    public UpdatePresenter(Context ctx) {
        super(ctx);
    }

    public void check() {

        Map<String, String> data = new HashMap<>();
        data.put("terminalType", Config.getTerminalType());
        data.put("appVersionName", AppUtil.getVersionName(ctx));
        data.put("appVersionCode", AppUtil.getVersionCode(ctx) + "");
        data.put("systemVersionName", Build.VERSION.SDK_INT + "");
        data.put("systemVersionCode", Build.VERSION.RELEASE);
        data.put("systemType", "android");
        data.put("platform", "android");

        sendToServer(AppService.CHECK_UPDATE, data, new ICallback() {

            @Override
            public void exec(Object params) {


            }
        });

    }

}
