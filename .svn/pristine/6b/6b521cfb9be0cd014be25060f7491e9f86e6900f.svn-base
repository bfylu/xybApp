package com.fdsj.credittreasure.presenter;

import android.app.Activity;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.entity.Gathering;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.google.gson.Gson;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by BXND on 2017-01-13.
 */
@Deprecated
public class GatheringDetailsPresenter {

    IActivity iActivity;

    public GatheringDetailsPresenter(IActivity iActivity) {
        this.iActivity = iActivity;
    }

    /**
     * 查询收款信息
     *
     * @param pageIndex
     */
    public void queryGathering(int pageIndex, final Activity activity) {
        String userName = Utilities.getUsernameForLogin(activity);
        HttpModel.getInstance().queryGathering(userName, pageIndex, 10, new HashMapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                iActivity.stopRecyclerView();
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        String json = response.get("data");
                        Gathering gathering = new Gson().fromJson(json, Gathering.class);
                        iActivity.UpdateModel(gathering.getList(), 1);
                    } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                        DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                    } else {
                        iActivity.stopRecyclerView();
                    }
                } catch (Exception ex) {
                    LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                    iActivity.stopRecyclerView();
                }
            }
        });
    }

}
