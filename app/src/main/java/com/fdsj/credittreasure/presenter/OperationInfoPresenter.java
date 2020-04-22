package com.fdsj.credittreasure.presenter;

import android.app.Activity;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.entity.Operation;
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
 * Created by BXND on 2017-01-16.
 */

public class OperationInfoPresenter {

    IActivity iActivity;

    public OperationInfoPresenter(IActivity iActivity) {
        this.iActivity = iActivity;
    }

    /**
     * 查询操作信息
     *
     * @param pageIndex
     */
    public void queryOperating(int pageIndex, final Activity activity) {
        String userName = Utilities.getUsernameForLogin(activity);
        HttpModel.getInstance().queryOperating(userName, pageIndex, 10, new HashMapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                iActivity.stopRecyclerView();
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        String json = response.get("data");
                        try {
                            Operation operation = new Gson().fromJson(json, Operation.class);
                            iActivity.UpdateModel(operation.getList(), 1);
                        } catch (Exception e) {
                            iActivity.stopRecyclerView();
                        }
                    } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                        DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                    } else {
                        iActivity.stopRecyclerView();
                    }
                } catch (Exception ex) {
                    ToastUtils.showToast(activity, "数据异常");
                    LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                }
            }
        });
    }

}
