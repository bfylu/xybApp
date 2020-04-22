package com.fdsj.credittreasure.constant;

import android.os.Environment;

/**
 * Created by lang on 2018/5/3.
 */

public class Constants {

    public static final String USER_INFO = "UserInfo";//SharedPreferences表名
    public static final String PRINT_TYPE_CHOOSE = "print_type_choose";//小票打印SharedPreferences键名
    public static final String VOICE_NOTIFICATION = "voice_notification";//语音播报类型SharedPreferences键名

    public static final String WHERE_GO = "where_go";
    public static final String MER_PWD = "mer_pwd";//商户密码
    public static final String TRADE_AMT = "trade_amt";//交易金额
    public static final String PAY_TYPE = "pay_type";//支付类型 0：支付宝  1：微信  2：银联
    public static final String TRADE_NO = "trade_no";//交易单号
    public static final String PROC_CD = "proc_cd";//交易处理码 消费撤销（仅限当日）：200000   联机退货：500000

    public static final int SETTING_ACTIVITY = 100;
    public static final int PRINT_TYPE_ACTIVITY = 101;
    public static final int SET_OR_RESET_MERCHANT_PWD_ACTIVITY = 102;
    public static final int SET_OR_CHANGE_MERCHANT_PWD_A_ACTIVITY = 103;
    public static final int SET_OR_CHANGE_MERCHANT_PWD_B_ACTIVITY = 104;
    public static final int MINE_FRAGMENT = 105;
    public static final int ORDER_FILTRATE_ACTIVITY = 106;
    public static final int SHIP_ACTIVITY = 107;
    public static final int SHOP_ORDER_DETAIL_ACTIVITY = 108;
    public static final int RADAR_FRAGMENT = 109;

    public static final String APP_ID = "wx5994fd1eb6dea56c";

    public static final int ACCOUNT_TYPE = 792;
    //sdk appid 由腾讯分配
    public static final int SDK_APPID = 1400102354;

    public final static String SDCARD_CACHE_NAME;
    /*****手机缓存路径,所有缓存文件全部放入该路径下*******/

    public final static String SDCARD_CACHE_PATH;
    public final static String SDCARD_CACHE_PATH_DATABASE;
    public final static String SDCARD_CACHE_PATH_APKPATH;

    static{
        SDCARD_CACHE_NAME="payadd";
        SDCARD_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+SDCARD_CACHE_NAME+"/cache/";
        SDCARD_CACHE_PATH_DATABASE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+SDCARD_CACHE_NAME+ "/tempDB";
        SDCARD_CACHE_PATH_APKPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+SDCARD_CACHE_NAME+"/apk";
    }
}
