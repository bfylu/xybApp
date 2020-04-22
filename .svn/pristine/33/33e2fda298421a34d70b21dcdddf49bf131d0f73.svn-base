package com.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by chery on 2016/11/20.
 */

public class ShowProgress {

    /**
     * 点击登录按钮后，弹出验证对话框
     */
    public static Dialog mDialog = null;

    /**
     * 弹出查询对话框，记得用完回收mDialog
     */
    public static Dialog creatRequestDialog(Context context, String loadingText) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = Utilities.getScreenWidth(context);
        lp.width = (int) (0.6 * width);
        TextView titleTxtv = (TextView) dialog.findViewById(R.id.tvLoad);
        if (!TextUtils.isEmpty(loadingText)) {
            titleTxtv.setText(loadingText);
        } else {
            titleTxtv.setText(context.getResources().getString(R.string.sending_request));
        }
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 弹出查询对话框，记得用完回收mDialog
     */
    public static void showRequestDialog(Context context, String title) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = creatRequestDialog(context, title);
        mDialog.show();
    }

    /**
     * 弹出查询对话框，记得用完回收mDialog
     */
    public static void showRequestDialog(Context context) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
        mDialog = creatRequestDialog(context, null);
        mDialog.show();
    }

//    //底部弹出Dialog
//    public static Dialog showDialog(View view, Activity activity) {
//        Dialog dialog = new Dialog(activity, R.style.transparentFrameWindowStyle);
//        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        Window window = dialog.getWindow();
//        window.setWindowAnimations(R.style.main_menu_animstyle);
//        WindowManager.LayoutParams wl = window.getAttributes();
//        wl.x = 0;
//        wl.y = activity.getWindowManager().getDefaultDisplay().getHeight();
//        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        dialog.onWindowAttributesChanged(wl);
//        return dialog;
//    }

}
