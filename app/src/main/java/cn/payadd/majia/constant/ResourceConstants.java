package cn.payadd.majia.constant;

import android.net.Uri;

import com.fdsj.credittreasure.application.BaseApplication;

/**
 * Created by df on 2018/1/16.
 */

public class ResourceConstants {
    public static final String RESOURCE_ZIP_NAME = "app_res.zip";

    public static final String RES_INFO_PATH_TXT = "app_res/info.txt";
    public static final String RES_INFO_PATH = "app_res";
    public static final String RES_INFO_PATH_TEXT_NAME = "/info.txt";
    public static String getRegisterURI() {
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/regisOrForget/register.html")).toString();
    }
    public static String getResetByEmailURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/regisOrForget/resetPwd-Email.html")).toString();
    }
    public static String getResetByPhoneURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/regisOrForget/resetPwd-Phone.html")).toString();
    }
    public static String getChooseGoodsURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/goods/chooseGoods.html")).toString();
    }
    public static String getGoodsManageURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/goods/goodsManager.html")).toString();
    }

    public static String getShopInfoURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/goods/shopInfo.html")).toString();
    }

    public static String getOpenProductURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/installment/openProduct.html")).toString();
    }

    public static String getAgentURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/agent/main/home.html")).toString();
    }

    public static String getGoodsDetailURI(){
        return Uri.fromFile(BaseApplication.getAppContext().getExternalFilesDir("app_res/html/goods/appGoodsDetails.html")).toString();
    }

}
