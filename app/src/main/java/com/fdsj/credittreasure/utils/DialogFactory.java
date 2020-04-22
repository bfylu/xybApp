package com.fdsj.credittreasure.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.fdsj.credittreasure.activities.DownLoadKeyActivity;
import com.fdsj.credittreasure.application.BaseApplication;
import com.utils.Utilities;

import cn.payadd.majia.activity.LoginActivity;


/**
 * Created by Administrator on 2015/12/22.
 */
public class DialogFactory {

    public static void showAlertDialog(Context context, String title, String msg, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("确定", onClickListener).setNegativeButton("取消", null).create().show();
    }

    public static void showAlertDialog(Context context, String title, String msg) {
        new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("确定", null).setNegativeButton("取消", null).create().show();
    }

    public static void userSignOutDialog(final Activity activity) {
        new AlertDialog.Builder(activity).setTitle("提示").setMessage("您的账号在其他地方登录，若非本人请尽快更改密码")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utilities.delUserInfo(activity);
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        BaseApplication baseApplication = (BaseApplication) activity.getApplication();
                        baseApplication.removeAll();
                    }
                }).create().show();
    }

    public static void userSignOutDialog(final Activity activity, String message) {
        new AlertDialog.Builder(activity).setTitle("提示").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utilities.delUserInfo(activity);
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        BaseApplication baseApplication = (BaseApplication) activity.getApplication();
                        baseApplication.removeAll();
                    }
                }).setCancelable(false).create().show();
    }

    public static void userDownCode(final Activity activity, String message) {
        new AlertDialog.Builder(activity).setTitle("提示").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utilities.delUserInfo(activity);
                        activity.startActivity(new Intent(activity, DownLoadKeyActivity.class));
                    }
                }).setCancelable(false).create().show();
    }

    public static void showAlertDialog(final Context context, String message, String title, String positiveBtnMsg, String negativeBtnMsg, final Class<?> cls) {
        Dialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(positiveBtnMsg, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != cls) {
                    Intent intent = new Intent(context, cls);
//                    intent.putExtra("amount", str);
                    context.startActivity(intent);
                }
            }
        }).setNegativeButton(negativeBtnMsg, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public static AlertDialog.Builder getAlertDialogBuilder(final Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title);

        return builder;
    }

    public static void showAlertDialog(final Context context, String message, String title, String positiveBtnMsg, String negativeBtnMsg,DialogInterface.OnClickListener onClickListener) {
        Dialog dialog =new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton(positiveBtnMsg, onClickListener).setNegativeButton(negativeBtnMsg, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
