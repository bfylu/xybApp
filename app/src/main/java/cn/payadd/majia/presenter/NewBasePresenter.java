package cn.payadd.majia.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fdsj.credittreasure.application.BaseApplication;
import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.exception.BusinessRuntimeException;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.task.RequestServerTask2;

/**
 * Created by df on 2018/1/18.
 */

public class NewBasePresenter extends BasePresenter{


    private boolean pending;

    public NewBasePresenter(Context ctx) {
        super(ctx);
    }

    @Override
    protected void sendJsonToServer(String service, JSONObject data, boolean encrypt,final ICallback
            succCallbak,final ICallback failCallback,final ICallback excpCallback) {
        if (pending) {
            return;
        }
        pending = true;
        try {
            JSONObject reqData = new JSONObject();
            reqData.put("version", BaseApplication.getVersionName());
            reqData.put("platform", 3);
            reqData.put("funCode", service);
            if(EnvironmentConfig.isMobile()){
                reqData.put("terminalType",1);
            }else if(EnvironmentConfig.isPos()){
                reqData.put("terminalType",2);
            }
            reqData.put("sessionToken", Utilities.getSessionToKen(ctx));
            // （h5专用，非h5去掉该字段）
//            reqData.put("loginToken", "");
            reqData.put("body", data);
            String content = reqData.toString();
            Log.e("send:",content);
            RequestServerTask2 task = new RequestServerTask2(new ICallback() {
                @Override
                public void exec(Object params) {

                    String msg = (String) params;

                    if (TextUtils.isEmpty(msg)) {
                        if (null != excpCallback) {
                            excpCallback.exec(null);
                        } else {
                            Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                    JSONObject respMsg = null;
                    try {
                        respMsg = new JSONObject(msg);
                        String respCode = respMsg.getString("respCode");
                        String respDesc = respMsg.getString("respDesc");

                        if (!"000000".equals(respCode)) {
                            if (null != failCallback) {
                                failCallback.exec(respMsg);
                            } else {
                                Toast.makeText(ctx, respDesc, Toast.LENGTH_LONG).show();
                            }
                        }else {
                            if (null != succCallbak) {
                                succCallbak.exec(respMsg);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            task.execute(content);
        } catch (BusinessRuntimeException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getErrCode() + "：" + e.getErrMsg(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            pending = false;
        }
    }

    protected void sendToServer(String service, Map<String, String> data, boolean encrypt, final ICallback succCallbak, final ICallback failCallback, final ICallback excpCallback) {


    }
}
