package com.fdsj.credittreasure.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.task.UpdateManger;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.utils.Enums;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by BXND on 2017-01-06.
 */

public class DownLoadPresenter {

    public void downloadApk(final Activity activity) {
        HttpModel.getInstance().downloadApk(activity, new HashMapCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        if (response.get("versionCheckFlag").equals("1")) {
                            UpdateManger updateManger = new UpdateManger(activity, response.get("appDownloadUrl"), "");
                            if (response.get("isForce").toUpperCase().equals("Y")) {
                                updateManger.showNoticeDialog(response.get("updateLog"),response.get("lastVersionName"));
                            } else {
                                updateManger.showDialog(response.get("updateLog"),response.get("lastVersionName"));
                            }
                        } else if (response.get("versionCheckFlag").equals("0")) {
//                            ToastUtils.showToast(activity, "当前已经是最新版本");
                            Toast.makeText(activity, "当前已经是最新版本", Toast.LENGTH_SHORT).show();
                        }else{
//                            ToastUtils.showToast(activity, "当前已经是最新版本");
                            Toast.makeText(activity, "当前已经是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }
    public void checkUpdate(final Activity activity, final View view) {
        HttpModel.getInstance().downloadApk(activity, new HashMapCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        if (response.get("versionCheckFlag").equals("1")) {
                          view.setVisibility(View.VISIBLE);
                        } else if (response.get("versionCheckFlag").equals("0")) {
                            view.setVisibility(View.GONE);
                            ToastUtils.showToast(activity, "当前已经是最新版本");
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
    }


    /**
     * 下载密钥
     */
    public void downloadKey(String merCode, final Activity activity) {
        HttpModel.getInstance().downloadKey(merCode, activity, new HashMapCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast(activity, activity.getResources().getString(R.string.try_net_wrong));
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                    Utilities.setMerchantKey(activity, response.get("mposKey"));
                    Utilities.setSessionToKen(activity, response.get("sessionToken"));
                    BaseApplication.setSessionToKen(response.get("sessionToken"));
                    DialogFactory.showAlertDialog(activity, activity.getResources().getString(R.string.hint), activity.getResources().getString(R.string.code_suc), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    });
                } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                    DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                } else {
                    ToastUtils.showToast(activity, response.get("respDesc"));
                    DialogFactory.showAlertDialog(activity, activity.getResources().getString(R.string.hint), response.get("respDesc"));
                }
            }
        });

    }
}
