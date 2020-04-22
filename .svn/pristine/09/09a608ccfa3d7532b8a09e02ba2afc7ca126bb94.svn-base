package com.zhy.http.okhttp.log;

import android.text.TextUtils;
import android.util.Log;

import com.utils.LogUtil;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by zhy on 16/3/1.
 */
public class LoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private boolean showResponse;

    public LoggerInterceptor(boolean showResponse) {

        this.showResponse = showResponse;
    }

    public LoggerInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    /**
     * 响应log
     * @param response
     * @return
     */
    private Response logForResponse(Response response) {
//        try {
//            //===>response log
////            LogUtil.info(TAG, "========response'log=======");
//            Response.Builder builder = response.newBuilder();
//            Response clone = builder.build();
////            LogUtil.info(TAG, "response-url : " + clone.request().url());
////            LogUtil.info(TAG,"content："+clone.body().contentType().toString());
////            LogUtil.info(TAG, "code : " + clone.code());
////            LogUtil.info(TAG, "protocol : " + clone.protocol());
////            if (!TextUtils.isEmpty(clone.message()))
////                LogUtil.info(TAG, "message : " + clone.message());
//
//            if (showResponse) {
//                ResponseBody body = clone.body();
//                if (body != null) {
//                    MediaType mediaType = body.contentType();
//                    if (mediaType != null) {
//                        LogUtil.info(TAG, "responseBody's contentType : " + mediaType.toString());
//                        if (isText(mediaType)) {
//                            String resp = body.string();
//                            LogUtil.info(TAG, "responseBody's content : " + resp);
//
//                            body = ResponseBody.create(mediaType, resp);
//                            return response.newBuilder().body(body).build();
//                        } else {
//                            LogUtil.info(TAG, "responseBody's content : " + " maybe [file part] , too large too print , ignored!");
//                        }
//                    }
//                }
//            }
////            LogUtil.info(TAG, "========response'log=======end");
//        } catch (Exception e) {
////            e.printStackTrace();
//        }

        return response;
    }

    /**
     * 请求log
     * @param request
     */
    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

//            LogUtil.info(TAG, "========request'log=======");
//            LogUtil.info(TAG, "method : " + request.method());
            LogUtil.info(TAG, request.method()+"----url : " + url);
            if (headers != null && headers.size() > 0) {
                LogUtil.info(TAG, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    LogUtil.info(TAG, "requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        LogUtil.info(TAG, "requestBody's content : " + bodyToString(request));
                    } else {
                        LogUtil.info(TAG, "requestBody's content : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
//            LogUtil.info(TAG, "========request'log=======end");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
