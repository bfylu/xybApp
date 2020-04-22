package com.zhy.http.okhttp;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.utils.Config;
import com.utils.EncodeUtil;
import com.utils.LogUtil;
import com.utils.ShowProgress;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.HeadBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFileBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;
import com.zhy.http.okhttp.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }


    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }


    /**
     * 执行http请求，不参与解密，不带进度条
     *
     * @param requestCall
     * @param callback
     */
    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                LogUtil.info("请求失败-onFailure--", call.isCanceled() + "----IOException----" + e.getMessage());
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        LogUtil.info("请求失败", call.isCanceled() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        LogUtil.info("响应失败", "response.message：" + response.message() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }
                    Object o = finalCallback.parseNetworkResponse(response, null, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    LogUtil.info("响应失败", "Exception" + e.getMessage() + "response.message：" + response.message() + "----code----" + response.code());
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    /**
     * 执行请求 不参与解密，带进度条
     *
     * @param requestCall 响应回调
     * @param callback
     * @param context
     */
    public void execute(final RequestCall requestCall, Callback callback, Context context) {
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        ShowProgress.showRequestDialog(context);//进度条
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                ShowProgress.mDialog.dismiss();
                LogUtil.info("请求失败-onFailure--", call.isCanceled() + "----IOException----" + e.getMessage());
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                ShowProgress.mDialog.dismiss();
                try {
                    if (call.isCanceled()) {
                        LogUtil.info("请求失败", call.isCanceled() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        LogUtil.info("响应失败", "response.message：" + response.message() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }
                    Object o = finalCallback.parseNetworkResponse(response, null, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    LogUtil.info("响应失败", "Exception" + e.getMessage() + "response.message：" + response.message() + "----code----" + response.code());
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {

                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    /**
     * 执行请求，参与解密，不带进度条
     *
     * @param requestCall
     * @param callback
     */
    public void execute(final RequestCall requestCall, final String merchantKey, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                LogUtil.info("请求失败-onFailure--", call.isCanceled() + "----IOException----" + e.getMessage());
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        LogUtil.info("请求失败", call.isCanceled() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        LogUtil.info("响应失败", "response.message：" + response.message() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, merchantKey, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    LogUtil.info("响应失败", "Exception" + e.getMessage() + "response.message：" + response.message() + "----code----" + response.code());
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }

    /**
     * 执行请求 参与解密，带进度条
     *
     * @param requestCall 响应回调
     * @param merchantKey 加密密钥
     * @param callback
     * @param context
     */
    public void execute(final RequestCall requestCall, final String merchantKey, Callback callback, Context context) {
        if (callback == null) {
            callback = Callback.CALLBACK_DEFAULT;
        }
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        ShowProgress.showRequestDialog(context);//进度条
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                ShowProgress.mDialog.dismiss();
                LogUtil.info("请求失败-onFailure--", call.isCanceled() + "----IOException----" + e.getMessage());
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                ShowProgress.mDialog.dismiss();
                try {
                    if (call.isCanceled()) {
                        LogUtil.info("请求失败", call.isCanceled() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        LogUtil.info("响应失败", "response.message：" + response.message() + "----code----" + response.code());
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, merchantKey, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                    LogUtil.info("响应失败", "Exception" + e.getMessage() + "response.message：" + response.message() + "----code----" + response.code());
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {

                    if (response.body() != null)
                        response.body().close();
                }

            }
        });
    }


    /**
     * 相应失败
     *
     * @param call
     * @param e
     * @param callback
     * @param id
     */
    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 相应成功
     *
     * @param object
     * @param callback
     * @param id
     */
    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {

        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

