package com.fdsj.credittreasure.entity;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;

import com.fdsj.credittreasure.application.BaseApplication;
import com.utils.AppUtil;
import com.utils.Config;
import com.utils.EncodeUtil;
import com.utils.Enums;
import com.utils.PhoneUtil;
import com.zhy.http.okhttp.callback.HashMapCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import cn.payadd.majia.config.AppConfig;

/**
 * <p>项目名称：MAJIA
 * <p>描   述：接口
 * <p>当前版本： V1.0.0
 */

public class HttpModel {

    private static HttpModel single = null;

    public static HttpModel getInstance() {
        if (single == null) {
            synchronized (HttpModel.class) {
                if (single == null) {
                    single = new HttpModel();
                }
            }
        }
        return single;
    }


    /**
     * 下载密钥
     *
     * @param merCode         商户号
     * @param activity
     * @param hashMapCallback
     */
    public void downloadKey(String merCode, Activity activity, HashMapCallback hashMapCallback) {
        String deviceIMIE = PhoneUtil.getDeviceIMIE(activity);
//        String body = "merCode=" + merCode +
//                "&terminalType=mobile" +
//                "&terminalNo=" + deviceIMIE +
//                "&systemType=android" +
//                "&systemVersionName=" + android.os.Build.VERSION.SDK +
//                "&imei=" + android.os.Build.MODEL +
//                "&systemVersionCode=" + android.os.Build.VERSION.RELEASE;
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.获取密钥.getString())
                .addParams("merCode", merCode)
                .addParams("terminalType", Config.getTerminalType())
                .addParams("terminalNo", deviceIMIE)
                .addParams("systemType", "android")
                .addParams("platform", "android")
                .addParams("systemVersionName", android.os.Build.VERSION.SDK_INT)
//                .addParams("imei", android.os.Build.MODEL)
                .addParams("systemVersionCode", android.os.Build.VERSION.RELEASE)
                .build().execute(hashMapCallback, activity);
//        getOkHttpUtils(body, Enums.httpType.获取密钥.getString(), activity, hashMapCallback);
    }

    public void downloadApk(Activity activity, HashMapCallback hashMapCallback) {
        CheryHttpUtils.getInstance()
                .get(AppConfig. getNewServerInterface())
                .service(Enums.httpType.检查升级.getString())
                .addParams("terminalType", Config.getTerminalType())
                .addParams("appVersionName", AppUtil.getVersionName(activity))
                .addParams("appVersionCode", AppUtil.getVersionCode(activity))
                .addParams("systemVersionName", Build.VERSION.SDK_INT)
                .addParams("systemVersionCode", Build.VERSION.RELEASE)
                .addParams("systemType", "android")
                .addParams("platform", "android")
                .build().execute(hashMapCallback);
    }

    /**
     * 用户登录
     *
     * @param userName
     * @param passWord
     * @param sessionToken
     * @param merchantKey
     * @param activity
     * @param hashMapCallback
     */
    public void userLogin(String loginToken, String userName, String passWord, String sessionToken, String merchantKey,String verifyCode, Activity activity, boolean status, HashMapCallback hashMapCallback) {
//        String body = "username=" + userName + "&password=" + EncodeUtil.getMD5Code(passWord, false) + "&loginToken=";
        String deviceIMIE = PhoneUtil.getDeviceIMIE(activity);
       CheryHttpUtils chu =  CheryHttpUtils.getInstance().get();
        chu.service(Enums.httpType.登录.getString())
                .sessionToken(sessionToken)
                .addParams("terminalType", "mobile")
                .addParams("terminalNo", "")
                .addParams("systemType", "android")
                .addParams("systemVersionName", Build.VERSION.RELEASE)
                .addParams("systemVersionCode", Build.VERSION.SDK_INT)
                .addParams("imei", deviceIMIE)
                .addParams("username", userName)
                .addParams("password", EncodeUtil.getMD5Code(passWord, false))
                .addParams("loginToken", loginToken);
                if(!TextUtils.isEmpty(verifyCode)){
                    chu.addParams("verifyCode",verifyCode);
                }
                RequestCall requestCall = chu.build();
        if (status) {
            requestCall.execute(hashMapCallback, activity);
        } else {
            requestCall.execute(hashMapCallback);
        }
//        getOkHttpUtils(body, Enums.httpType.登录.getString(), merchantKey, activity, stringCallback);
    }

    /**
     * 交易概况
     *
     * @param userName
     * @param hashMapCallback
     */
    public void tradeOverview(String userName, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.交易概况.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }

    /**
     * 统计查询
     */
    public void queryStatistic(String userName, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.统计查询.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }

    /**
     * 提交订单
     *
     * @param amount          订单金额
     * @param authCode        授权码，使用刷卡支付时条码或者二维码信息
     * @param payMethod       支付方式
     * @param payType         支付类型
     * @param hashMapCallback
     */
    public void submitOrder(String amount, String authCode, Enums.payMethod payMethod, Enums.httpPayType payType, Activity activity, StringCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        RequestCall requestCall = CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.提交订单.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("amount", amount)
                .addParams("authCode", authCode)
                .addParams("payMethod", payMethod.getIndex())
                .addParams("payType", payType.getString())
                .build(merchantKey);

        if (payMethod == Enums.payMethod.二维码) {
            requestCall.execute(hashMapCallback, merchantKey);
        } else {
            requestCall.execute(hashMapCallback, merchantKey, activity);
        }
    }

    /**
     * 订单状态查询
     *
     * @param orderNo
     * @param hashMapCallback
     */
    public void queryOrderStatus(String orderNo, Activity activity, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        RequestCall requestCall = CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.订单状态查询.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("orderNo", orderNo)
                .build(merchantKey);
        if (activity != null) {
            requestCall.execute(hashMapCallback, merchantKey, activity);
        } else {
            requestCall.execute(hashMapCallback, merchantKey);
        }

    }


    /**
     * 流水查询
     *
     * @param userName  用户名
     * @param page      页数
     * @param row       每页显示条数
     * @param transType 交易类型
     * @param condition 条件
     */
    public void queryWater(String userName, int page, int row, Enums.transType transType, String condition, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.流水查询.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .addParams("page", page)
                .addParams("row", row)
                .addParams("transType", transType.getString())
                .addParams("condition", condition)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }

    /**
     * 结算查询
     *
     * @param userName
     * @param hashMapCallback
     */
    public void queryClearing(String userName, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.结算查询.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }


    /**
     * 发起结算
     *
     * @param userName
     * @param hashMapCallback
     */
    public void addClearing(String userName, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.发起结算.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }


    /**
     * 查询收款信息
     *
     * @param userName
     * @param hashMapCallback
     */
    public void queryGathering(String userName, int page, int row, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.收款信息.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .addParams("page", page)
                .addParams("row", row)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }

    /**
     * 查询操作信息
     *
     * @param userName
     * @param page
     * @param row
     * @param hashMapCallback
     */
    public void queryOperating(String userName, int page, int row, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.操作信息.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .addParams("page", page)
                .addParams("row", row)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }


    /**
     * 查询商户固定码
     *
     * @param userName
     * @param hashMapCallback
     */
    public void getMerchantCode(String userName, HashMapCallback hashMapCallback) {
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(Enums.httpType.商户固定码.getString())
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("username", userName)
                .build(merchantKey)
                .execute(hashMapCallback, merchantKey);
    }

}
