package cn.payadd.majia.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.payadd.majia.activity.UploadExtInfoActivity;
import cn.payadd.majia.face.ReqProgressCallBack;
import cn.payadd.majia.util.AppLog;
import cn.payadd.majia.util.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by zhengzhen.wang on 2017/6/21.
 */

public class FileUpdateTask extends AsyncTask<String, Integer, String> {

    public static final int HANDLER_PROGRESS = 100;

    public static final String LOG_TAG = "FileUpdateTask";

    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");

    private OkHttpClient mOkHttpClient;
    private Context mContext;
    private String tag;
    private String actionUrl;
    private Handler handler;
    private Map<String, Object> paramsMap;

    public FileUpdateTask(Context ctx, String tag, Handler handler) {
        this.mContext = ctx;
        this.tag = tag;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        mOkHttpClient = new OkHttpClient();
        mOkHttpClient = OkHttpUtil.getUnsafeOkHttpClient();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            //追加参数
            if (null != paramsMap) {

                ReqProgressCallBack callBack = new ReqProgressCallBack() {
                    @Override
                    public void onProgress(long total, long current) {

                    }
                };

                //追加参数
                for (String key : paramsMap.keySet()) {
                    Object object = paramsMap.get(key);
                    if (!(object instanceof File)) {
                        builder.addFormDataPart(key, object.toString());
                    } else {
                        File file = (File) object;
                        String srcName = file.getName();
                        String name = srcName.substring(0, srcName.lastIndexOf("."));
                        name = name + srcName.substring(srcName.lastIndexOf("."));
                        builder.addFormDataPart(key, name, createProgressRequestBody(MEDIA_OBJECT_STREAM, file, callBack));
                    }
                }
            }

            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            AppLog.d(LOG_TAG, "actionUrl -> " + actionUrl);
            final Request request = new Request.Builder().url(actionUrl).post(body).build();
            final Call call = mOkHttpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    AppLog.e(LOG_TAG, "上传失败，" + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    if (response.isSuccessful()) {
                        AppLog.d(LOG_TAG, "上传成功，response ----->" + string);
                        if (null != handler) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("uploadStatus", "success");
                            map.put("taskObject", FileUpdateTask.this);
                            Message msg = new Message();
                            msg.obj = map;
                            msg.what = UploadExtInfoActivity.HANDLER_UPLOAD_FINISH;
                            handler.sendMessage(msg);
                        }
                    } else {
                        AppLog.e(LOG_TAG, "上传失败，response ----->" + string);
                        if (null != handler) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("uploadStatus", "failure");
                            map.put("taskObject", FileUpdateTask.this);
                            Message msg = new Message();
                            msg.obj = map;
                            msg.what = UploadExtInfoActivity.HANDLER_UPLOAD_FINISH;
                            handler.sendMessage(msg);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
//        AppLog.d(LOG_TAG,"progress: " + progress + "%");
//        if (null != handler) {
//            Message msg = new Message();
//            msg.what = HANDLER_PROGRESS;
//            msg.arg1 = progress;
//            handler.sendMessage(msg);
//        }
    }

    @Override
    protected void onPostExecute(String msg) {
        super.onPostExecute(msg);
    }

    public <T> RequestBody createProgressRequestBody(final MediaType contentType, final File file, final ReqProgressCallBack<T> callBack) {

        return new RequestBody() {

            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
//                        AppLog.e(LOG_TAG, "current------>" + current);
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private <T> void progressCallBack(final long total, final long current, final ReqProgressCallBack<T> callBack) {

        int rate = (int) ((current / total) * 100);
        publishProgress(rate);
    }

    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, Object> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
