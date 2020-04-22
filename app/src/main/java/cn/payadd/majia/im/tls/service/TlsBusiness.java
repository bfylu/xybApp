package cn.payadd.majia.im.tls.service;

import android.content.Context;

import com.fdsj.credittreasure.constant.Constants;


/**
 * 初始化tls登录模块
 */
public class TlsBusiness {


    private TlsBusiness(){}

    public static void init(Context context){
        TLSConfiguration.setSdkAppid(Constants.SDK_APPID);
        TLSConfiguration.setAccountType(Constants.ACCOUNT_TYPE);
        TLSConfiguration.setTimeout(8000);
        TLSConfiguration.setQqAppIdAndAppKey("222222", "CXtj4p63eTEB2gSu");
        TLSConfiguration.setWxAppIdAndAppSecret("wx65f71c2ea2b122da", "1d30d40f8db6d3ad0ee6492e62ad5d57");
        TLSService.getInstance().initTlsSdk(context);
    }

    public static void logout(String id){
        TLSService.getInstance().clearUserInfo(id);

    }
}
