package com.fdsj.credittreasure.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import cn.payadd.majia.config.StoreDirConfig;
import cn.payadd.majia.updateapk.ApkDownloadService;
import cn.payadd.majia.view.CommonDialog;

/**
 * 版本更新
 */
public class UpdateManger {


    private Context mContext;

    String Url;

    private String updateMsg;

    private CommonDialog commonDialog, noticeDialog, negativeDialog;

    //保存到sd卡的路径
    private static final String savePath = StoreDirConfig.getDownloadDir().getPath();

    public UpdateManger(Context context, String url, String updateMsg) {
        this.mContext = context;
        this.Url = url;
        this.updateMsg = updateMsg;
    }

    //手动
    public void showDialog(String serverMes,String lastVersionName) {
        commonDialog = new CommonDialog(mContext,R.layout.update_dialog,"发现新版本 "+ lastVersionName,"立即更新","取消");
        View view = commonDialog.getDialogView();
        TextView tvUpdateMsg = (TextView) view.findViewById(R.id.tv_update_msg);
        tvUpdateMsg.setText(serverMes);
        commonDialog.setOnDiaLogListener(new CommonDialog.OnDialogListener() {
            @Override
            public void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which) {
                switch (btnType){
                    case "positive":
                        startDownloadService();
                        break;
                }
            }
        }).showDialog();
    }

    //强制
    public void showNoticeDialog(String serverMes,String lastVersionName) {
        noticeDialog = new CommonDialog(mContext,R.layout.update_dialog,"发现新版本 "+ lastVersionName,"现在更新","取消");
        View view = noticeDialog.getDialogView();
        TextView tvUpdateMsg = (TextView) view.findViewById(R.id.tv_update_msg);
        tvUpdateMsg.setText(serverMes);
        noticeDialog.setOnDiaLogListener(new CommonDialog.OnDialogListener() {
            @Override
            public void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which) {
                switch (btnType){
                    case "positive":
                        startDownloadService();
                        break;
                    case "negative":
                        showNegativeDialog();
                        break;
                }
            }
        }).showDialog();
    }

    private void showNegativeDialog() {
        negativeDialog = new CommonDialog(mContext,mContext.getResources()
                .getString(R.string.reject_update),null,"立即更新","暂不更新");
        negativeDialog.setOnDiaLogListener(new CommonDialog.OnDialogListener() {
            @Override
            public void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which) {
                switch (btnType){
                    case "positive":
                        startDownloadService();
                        break;
                    case "negative":
                        System.exit(0);
                        break;
                }
            }
        }).showDialog();
    }

    public void startDownloadService() {
        hideCommonDialog();
        hideNoticeDialog();
        hideNegativeDialog();
        Intent intent = new Intent(mContext, ApkDownloadService.class);
        intent.putExtra("url", Url);
        intent.putExtra("savePath", savePath);
        mContext.startService(intent);
    }

    private void hideCommonDialog() {
        if (commonDialog != null) {
            commonDialog.dismiss();
            commonDialog = null;
        }
    }

    private void hideNoticeDialog() {
        if (noticeDialog != null) {
            noticeDialog.dismiss();
            noticeDialog = null;
        }
    }

    private void hideNegativeDialog() {
        if (negativeDialog != null) {
            negativeDialog.dismiss();
            negativeDialog = null;
        }
    }
}
