package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.task.RequestServerTask;
import cn.payadd.majia.util.StringUtil;
import okhttp3.Call;

/**
 * Created by BXND on 2017-01-12.
 */

public class FixedCodePresenter extends BasePresenter {

    private IActivity iActivity;

    public FixedCodePresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    /**
     * 获取我的固定码
     *
     * @param
     */
    public void getFixedCode(){

            String actionUrl = AppConfig.getFixedQrcodeInterface();

            Map<String, String> data = new HashMap<>();
            data.put("sessionToken", Utilities.getSessionToKen(ctx));
            String content = StringUtil.linkAndEncode(data);
            RequestServerTask task = new RequestServerTask(actionUrl, new ICallback() {
                @Override
                public void exec(Object params) {
                    String msg = (String) params;
                    if (TextUtils.isEmpty(msg)) {
                        Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        JSONObject jsonObj = new JSONObject(msg);
                        String respCode = jsonObj.getString("respCode");
                        String respDesc = jsonObj.getString("respDesc");
                        if (!"000000".equals(respCode)) {
                            Toast.makeText(ctx, respDesc, Toast.LENGTH_LONG).show();
                        } else {
                            if (null != iActivity) {
                                iActivity.updateModel("shareContract", jsonObj);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            task.execute(content);
        }

}
