package com.zhy.http.okhttp.request;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/15.
 * 对OkHttpRequest的封装，对外提供更多的接口：cancel(),readTimeOut()...
 */
public class RequestCall {
    private OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

    private OkHttpClient clone;

    public RequestCall(OkHttpRequest request) {
        this.okHttpRequest = request;
    }

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall writeTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public Call buildCall(Callback callback) {
        request = generateRequest(callback);

        if (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0) {
            readTimeOut = readTimeOut > 0 ? readTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut = writeTimeOut > 0 ? writeTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut = connTimeOut > 0 ? connTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            clone = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(connTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            call = clone.newCall(request);
        } else {
            call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        }
        return call;
    }

    private Request generateRequest(Callback callback) {
        return okHttpRequest.generateRequest(callback);
    }


    /**
     * 执行请求，异步，不参与解密，不带进度条
     *
     * @param callback
     */
    public void execute(Callback callback) {
        buildCall(callback);

        if (callback != null) {
            callback.onBefore(request, getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this, callback);
    }

    /**
     * 执行请求，异步,参与解密，带进度条
     *
     * @param merchantKey 密钥
     * @param callback    回调
     * @param context
     */
    public void execute(Callback callback, String merchantKey, Context context) {
        buildCall(callback);

        if (callback != null) {
            callback.onBefore(request, getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this, merchantKey, callback, context);
    }

    /**
     * 执行请求，异步,不参与解密，带进度条
     *
     * @param callback 回调
     * @param context
     */
    public void execute(Callback callback, Context context) {
        buildCall(callback);

        if (callback != null) {
            callback.onBefore(request, getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this, callback, context);
    }

    /**
     * 执行请求，异步,参与解密，不带进度条
     *
     * @param callback    回调
     * @param merchantKey 密钥
     */
    public void execute(Callback callback, String merchantKey) {
        buildCall(callback);

        if (callback != null) {
            callback.onBefore(request, getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this, merchantKey, callback);
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    /**
     * 同步请求
     *
     * @return
     * @throws IOException
     */
    public Response execute() throws IOException {
        buildCall(null);
        return call.execute();
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }


}
