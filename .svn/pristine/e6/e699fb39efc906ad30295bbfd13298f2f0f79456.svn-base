package com.fdsj.credittreasure.entity;

import com.fdsj.credittreasure.application.BaseApplication;
import com.utils.Config;
import com.utils.EncodeUtil;
import com.utils.LogUtil;
import com.utils.Utilities;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by 冷佳兴 on 2017/1/7-19:59.
 * 我是大傻逼，所在公司:博信诺达
 */

public class CheryHttpUtils {

    private GetBuilder builder;

    public CheryHttpUtils() {

    }

    private static CheryHttpUtils single = null;

    public static CheryHttpUtils getInstance() {
        if (single == null) {
            synchronized (CheryHttpUtils.class) {
                if (single == null) {
                    single = new CheryHttpUtils();
                }
            }
        }
        return single;
    }

    public CheryHttpUtils get() {
        builder = OkHttpUtils.get().url(Config.getServerInterface())
                .addParams("terminalType", Config.getTerminalType())
                .addParams("platform", "android")
                .addParams("version", BaseApplication.getVersionName())
                .addParams("charset", "utf-8");
        return this;
    }

    public CheryHttpUtils get(String url) {
        builder = OkHttpUtils.get().url(url)
                .addParams("terminalType", Config.getTerminalType())
                .addParams("platform", "android")
                .addParams("version", BaseApplication.getVersionName())
                .addParams("charset", "utf-8");
        return this;
    }

    public CheryHttpUtils service(String data) {
        builder.addParams("service", data);
        return this;
    }

    public CheryHttpUtils sessionToken(String data) {
        builder.addParams("sessionToken", data);
        return this;
    }

    private CheryHttpUtils body() {
        builder.addParams("body", getBody());
        return this;
    }

    private CheryHttpUtils body(String merchantKey) {
        builder.addParams("body", EncodeUtil.encryptMode(merchantKey, getBody()));
        return this;
    }

    public RequestCall build() {
        return body().builder.build();
    }

    public RequestCall build(String merchantKey) {
        return body(merchantKey).builder.build();
    }

    private Map<String, String> stringMap;

    public CheryHttpUtils addParams(String key, String val) {
        if (this.stringMap == null) {
            stringMap = new LinkedHashMap<>();
        }
        stringMap.put(key, val);
        return this;
    }

    public CheryHttpUtils addParams(String key, int val) {
        if (this.stringMap == null) {
            stringMap = new LinkedHashMap<>();
        }
        stringMap.put(key, String.valueOf(val));
        return this;
    }

    public CheryHttpUtils addParams(String key, double val) {
        if (this.stringMap == null) {
            stringMap = new LinkedHashMap<>();
        }
        stringMap.put(key, String.valueOf(val));
        return this;
    }

    private String getBody() {
        if (stringMap == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = stringMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            sb.append("&" + entry.getKey() + "=" + entry.getValue());
        }
        stringMap.clear();
        String body = Utilities.replaceStr(sb.toString());
        LogUtil.info("body", body);
        return body;
    }


//    /**
//     * http get 请求，不带进度条，不参与加密解密
//     *
//     * @param body         请求体
//     * @param service      服务名，请求那个接口 查看枚举Enums.httpType
//     * @param version      版本号
//     * @param sessionToken 会话Token
//     * @param callback     回调
//     */
//    public void getOkHttpUtils(String body, String service, Callback callback) {
//        OkHttpUtils.get().url(Config.URL)
//                .addParams("platform", "android")
//                .addParams("version", BaseApplication.getVersionName())
//                .addParams("charset", "utf-8")
//                .addParams("sessionToken", BaseApplication.getSessionToKen())
//                .addParams("service", service)
//                .addParams("body", body).build().execute(callback);
//
//    }
//
//
//    /**
//     * http get 请求，无进度条，参与加密解密
//     *
//     * @param body         请求体
//     * @param service      服务名，请求那个接口 查看枚举Enums.httpType
//     * @param version      版本号
//     * @param sessionToken 会话Token
//     * @param merchantKey  密钥
//     * @param callback     回调
//     */
//    public void getOkHttpUtils(String body, String service, String merchantKey, Callback callback) {
//        OkHttpUtils.get().url(Config.URL)
//                .addParams("platform", "android")
//                .addParams("version", BaseApplication.getVersionName())
//                .addParams("charset", "utf-8")
//                .addParams("sessionToken", BaseApplication.getSessionToKen())
//                .addParams("service", service)
//                .addParams("body", EncodeUtil.encryptMode(merchantKey, body)).build().execute(merchantKey, callback);
//
//    }
//
//    /**
//     * http get 请求，带进度条，不参与加密解密
//     *
//     * @param body         请求体
//     * @param service      服务名，请求那个接口 查看枚举Enums.httpType
//     * @param version      版本号
//     * @param sessionToken 会话Token
//     * @param callback     回调
//     */
//    public void getOkHttpUtils(String body, String service, Context context, Callback callback) {
//        OkHttpUtils.get().url(Config.URL)
//                .addParams("platform", "android")
//                .addParams("version", BaseApplication.getVersionName())
//                .addParams("charset", "utf-8")
//                .addParams("sessionToken", BaseApplication.getSessionToKen())
//                .addParams("service", service)
//                .addParams("body", body).build().execute(callback, context);
//
//    }
//
//    /**
//     * http get 请求，带进度条，参与加密解密
//     *
//     * @param body         请求体
//     * @param service      服务名，请求那个接口 查看枚举Enums.httpType
//     * @param version      版本号
//     * @param sessionToken 会话Token
//     * @param merchantKey  密钥
//     * @param callback
//     */
//    public CheryHttpUtils getOkHttpUtils(String body, String service, String merchantKey, Context context, Callback callback) {
//        OkHttpUtils.get().url(Config.URL)
//                .addParams("platform", "android")
//                .addParams("version", BaseApplication.getVersionName())
//                .addParams("charset", "utf-8")
//                .addParams("sessionToken", BaseApplication.getSessionToKen())
//                .addParams("service", service)
//                .addParams("body", EncodeUtil.encryptMode(merchantKey, body)).build().execute(merchantKey, callback, context);
//        return this;
//    }

}
