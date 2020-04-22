package cn.payadd.majia.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.exception.BusinessRuntimeException;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.util.HttpUtil;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by zhengzhen.wang on 2017/6/12.
 */

public class RequestServerTask extends AsyncTask<String, Object, String> {

    private String actionUrl;

    private ICallback callback;

    private String contentType;

    public RequestServerTask() {

    }

    public RequestServerTask(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public RequestServerTask(ICallback callback) {

        this.callback = callback;
    }

    public RequestServerTask(String actionUrl, ICallback callback) {
        this.actionUrl = actionUrl;
        this.callback = callback;
    }

    public RequestServerTask(String actionUrl, ICallback callback, String contentType) {
        this.actionUrl = actionUrl;
        this.callback = callback;
        this.contentType = contentType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        if (TextUtils.isEmpty(actionUrl)) {
            actionUrl = AppConfig.getServerInterface();
        }
        String msg = null;
        try {
            if (StringUtil.isEmpty(contentType)) {
                msg = HttpUtil.post(actionUrl, params[0]);
            } else {
                msg = HttpUtil.post(actionUrl, params[0], contentType);
            }


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
