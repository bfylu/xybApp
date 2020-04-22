package cn.payadd.majia.config;

import com.fdsj.credittreasure.application.BaseApplication;

import java.io.File;

/**
 * Created by zhengzhen.wang on 2017/6/22.
 */

public class StoreDirConfig {

    public static File getTempForUploadDir() {

        File dir = BaseApplication.getAppContext().getExternalFilesDir("tmp/upload");
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getDownloadDir() {

        File dir = BaseApplication.getAppContext().getExternalFilesDir("tmp/download");
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

}
