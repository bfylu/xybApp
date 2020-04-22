package com.fdsj.credittreasure.presenter;

import android.app.Activity;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by BXND on 2017-01-12.
 */

public class FixedCodePresenter {

    IActivity iActivity;

    public FixedCodePresenter(IActivity iActivity) {
        this.iActivity = iActivity;
    }


    /**
     * 获取我的固定码
     *
     * @param activity
     */
    public void getMerchantCode(final Activity activity) {
        String userName = Utilities.getUsernameForLogin(activity);
        HttpModel.getInstance().getMerchantCode(userName, new HashMapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                iActivity.stopRecyclerView();
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        iActivity.UpdateModel(response.get("myQrcode"), 1);
                    } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                        DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                    } else {
                        ToastUtils.showToast(activity, response.get("respDesc"));
                        iActivity.stopRecyclerView();
                    }
                } catch (Exception ex) {
                    LogUtil.info("Exception", ex.getMessage());
                    iActivity.stopRecyclerView();
                }
            }
        });
    }

}
