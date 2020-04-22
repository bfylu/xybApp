package cn.payadd.majia.util;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fdsj.credittreasure.R;

import cn.payadd.majia.view.CommonDialog;

/**
 * Created by df on 2018/1/19.
 */

public class PermissionCheckUtil {
    public static boolean checkCameraPermission(final Context context) {
        boolean permissionFlag;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            permissionFlag = false;
            final CommonDialog commonDialog = new CommonDialog(context, "当前没有相机权限，暂时无法使用扫码功能，请授予相机权限后使用。", null, "重新授权", "取消");
            commonDialog.setOnDiaLogListener(new CommonDialog.OnDialogListener() {
                @Override
                public void dialogListener(String btnType, View customView, DialogInterface
                        dialogInterface, int which) {
                    switch (btnType) {
                        case "positive":
                            JumpPermissionManagement.GoToSetting(context);
                            break;
                        case "negative":
                            commonDialog.dismiss();
                            break;
                    }
                }
            });
            commonDialog.showDialog();
        }else{
            permissionFlag = true;
        }
        return permissionFlag;
    }
}
