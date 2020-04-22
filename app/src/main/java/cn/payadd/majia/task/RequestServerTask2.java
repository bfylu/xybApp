package cn.payadd.majia.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.exception.BusinessRuntimeException;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.util.HttpUtil;

/**
 * Created by zhengzhen.wang on 2017/6/12.
 */

public class RequestServerTask2 extends AsyncTask<String, Object, String> {

    private String actionUrl;

    private ICallback callback;

    public RequestServerTask2() {

    }

    public RequestServerTask2(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public RequestServerTask2(ICallback callback) {

        this.callback = callback;
    }

    public RequestServerTask2(String actionUrl, ICallback callback) {
        this.actionUrl = actionUrl;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        if (TextUtils.isEmpty(actionUrl)) {
            actionUrl = AppConfig.getNewServerInterface();
        }
        String msg = null;
        try {
             msg = HttpUtil.post(actionUrl, params[0], "application/json");
        } catch (BusinessRuntimeException e) {
            e.printStackTrace();
        }
        return msg;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
        if (null != callback) {
            callback.exec(msg);
        }
    }
}
