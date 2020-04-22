package com.fdsj.credittreasure.presenter;

import android.app.Activity;

import com.fdsj.credittreasure.Interface.iActivities.IFlow;
import com.fdsj.credittreasure.entity.FlowBean;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.fragment.Flow2Fragment;
import com.fdsj.credittreasure.fragment.FlowFragment;
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
 * Created by 冷佳兴 on 2017/1/8-17:11.
 * 我是大傻逼，所在公司:博信诺达
 */

public class FlowPresenter {
    IFlow iActivity;

    public FlowPresenter(FlowFragment flowFragment) {
        this.iActivity = flowFragment;
    }

    public FlowPresenter(Flow2Fragment flowFragment) {
        this.iActivity = flowFragment;
    }


    /**
     * 流水查询
     *
     * @param condition json 条件
     * @param pageIndex 页数
     * @param type      支付类型
     * @param activity
     */
    public void queryWater(String condition, int pageIndex, Enums.transType type, final Activity activity) {
        String userName = Utilities.getUsernameForLogin(activity);
        HttpModel.getInstance().queryWater(userName, pageIndex, 10, type, condition, new HashMapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                iActivity.stopRecyclerView();
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        String totalRefund = response.get("totalRefund");
                        String transCount = response.get("transCount");
                        String totalAcquire = response.get("totalAcquire");
                        String json = response.get("data");
                        FlowBean flowBean = new Gson().fromJson(json, FlowBean.class);
                        flowBean.setTotalAcquire(totalAcquire);
                        flowBean.setTransCount(transCount);
                        flowBean.setTotalRefund(totalRefund);
                        flowBean.setTodayTotalAcquire(response.get("todayTotalAcquire"));
                        flowBean.setTodayTransCount(response.get("todayTransCount"));
                        flowBean.setPage(response.get("page"));
                        flowBean.setRow(response.get("row"));
                        iActivity.getFlowBean(flowBean, 1);
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

    }
}
