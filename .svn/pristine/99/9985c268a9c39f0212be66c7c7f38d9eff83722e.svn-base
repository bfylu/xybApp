package cn.payadd.majia.config;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.fdsj.credittreasure.application.BaseApplication;

/**
 * 编译前需要确认 RUN_ENV 和 TERMINAL_TYPE 是否正确
 * Created by zhengzhen.wang on 2017/6/7.
 */

public final class EnvironmentConfig {

    private static final String TERMINAL_POS = "pos";

    private static final String TERMINAL_MOBILE = "mobile";

    /**
     * 开发阶段
     */
    private static final String DEVEL = "devel";
    /**
     * 内部测试阶段
     */
    private static final String TEST = "test";
    /**
     * 正式版
     */
    private static final String RELEASE = "release";

    public static String getTerminalType() {

        return  getMetaValue("device.type");
    }

    public static boolean isPos() {

        String deviceType = getMetaValue("device.type");
        return TERMINAL_POS.equals(deviceType);
    }

    public static boolean isMobile() {

        String deviceType = getMetaValue("device.type");
        return TERMINAL_MOBILE.equals(deviceType);
    }

    public static boolean isDevel() {
        String deviceType = getMetaValue("run.env");
        return deviceType.equals(DEVEL);
    }

    public static boolean isTest() {
        String deviceType = getMetaValue("run.env");
        return deviceType.equals(TEST);
    }

    public static boolean isRelease() {

        String deviceType = getMetaValue("run.env");
        return deviceType.equals(RELEASE);
    }

    public static String getPosType(){

        return getMetaValue("pos.type");
    }

    private static String getMetaValue(String metaKey){
        PackageManager manager = BaseApplication.getAppContext().getPackageManager();
        ApplicationInfo appInfo = null;
        try {
            appInfo = manager.getApplicationInfo(BaseApplication.getAppContext().getPackageName(),
                    PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appInfo.metaData.getString(metaKey);
    }

}
