package com.fdsj.credittreasure.presenter;

import android.app.Activity;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.entity.CheryHttpUtils;
import com.fdsj.credittreasure.entity.Gathering;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.google.gson.Gson;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by 冷佳兴 on 2017/1/15-18:13.
 * 我是大傻逼，所在公司:博信诺达
 */

public class FlowDetailPresenter {
    IActivity iActivity;

    public FlowDetailPresenter(IActivity iActivity) {
        this.iActivity = iActivity;
    }

    /**
     * 查询收款信息
     *
     * @param
     */
    public void queryOrderStatus(String orderNo, final Activity activity, int status) {
        if (status == 1) {
            HttpModel.getInstance().queryOrderStatus(orderNo, null, new HashMapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    iActivity.stopRecyclerView();
                }

                @Override
                public void onResponse(Map<String, String> response, int id) {
                    try {
                        if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                            iActivity.UpdateModel(response, 1);
                        } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                            DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                        } else {
                            ToastUtils.showToast(activity, response.get("respDesc"));
                            iActivity.stopRecyclerView();
                        }
                    } catch (Exception ex) {
                        LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                        iActivity.stopRecyclerView();
                    }
                }
            });
        } else {
            HttpModel.getInstance().queryOrderStatus(orderNo, activity, new HashMapCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    iActivity.stopRecyclerView();
                }

                @Override
                public void onResponse(Map<String, String> response, int id) {
                    try {
                        if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                            iActivity.UpdateModel(response, 1);
                        } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                            DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                        } else {
                            iActivity.UpdateModel(response.get("respDesc"), 0);
                        }
                    } catch (Exception ex) {
                        LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                        iActivity.stopRecyclerView();
                    }
                }
            });
        }

    }


    /**
     * 统计查询
     */
    public void queryStatistic(String userName, final Activity activity) {
        HttpModel.getInstance().queryStatistic(userName, new HashMapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                iActivity.stopRecyclerView();
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        iActivity.UpdateModel(response, 1);
                    } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                        DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                    } else {
                        iActivity.UpdateModel(response.get("respDesc"), 0);
                    }
                } catch (Exception ex) {
                    LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                    iActivity.stopRecyclerView();
                }
            }
        });
    }


}

