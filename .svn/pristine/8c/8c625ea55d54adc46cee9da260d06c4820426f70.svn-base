package com.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    private static String path;
    private static File file;
    private static FileOutputStream outputStream;

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            path = externalStorageDirectory.getAbsolutePath() + "/CreditTreasure/";
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            file = new File(new File(path), "CreditTreasure.txt");
            try {
                outputStream = new FileOutputStream(file, true);
            } catch (FileNotFoundException e) {

            }
        } else {

        }
    }


    public static void info(String title, String msg) {

        if (Config.isDevel()) {
            Log.d(title,title+"-----------"+ msg);
        } else if (Config.isTest()) {
            writeInfoToSDcard(title, msg);
        } else if (Config.isRelease()) {

        }
    }


    /**
     * 写日志到SD卡
     *
     * @param title
     * @param msg
     */
    public static void writeInfoToSDcard(String title, String msg) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (outputStream != null) {
                try {
                    outputStream.write(time.getBytes());
                    outputStream.write(("    " + title + "\r\n").getBytes());
                    outputStream.write(msg.getBytes());
                    outputStream.write("\r\n".getBytes());
                    outputStream.flush();
                } catch (IOException e) {
                }
            }
        }
    }

}
