package cn.payadd.majia.util;

import android.text.TextUtils;

/**
 * Created by zhengzhen.wang on 2017/6/23.
 */

public class FileFormat {

    public static boolean isImage(String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            return false;
        }

        if (fileName.endsWith("jpg") || fileName.endsWith("png") || fileName.endsWith("gif")) {
            return true;
        }

        return false;
    }

}
