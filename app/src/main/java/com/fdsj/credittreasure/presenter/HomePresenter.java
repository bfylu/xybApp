package com.fdsj.credittreasure.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.fdsj.credittreasure.Interface.iActivities.IHome;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.entity.TradeBean;
import com.fdsj.credittreasure.fragment.HomeFragment;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.google.gson.Gson;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;

import java.util.Map;

import cn.payadd.majia.fragment.PosHomeFragment;
import okhttp3.Call;

/**
 * Created by 冷佳兴 on 2017/1/8-15:30.
 * 我是大傻逼，所在公司:博信诺达
 */

public class HomePresenter {

    IHome iHome;

    public HomePresenter(HomeFragment fragment, PosHomeFragment posHomeFragment) {
        if(fragment == null){
            this.iHome = posHomeFragment;
        }else{
            this.iHome = fragment;
        }

    }

    /**
     * 交易概况
     */
    public void tradeOverview(final Activity activity) {
        String userName = Utilities.getUsernameForLogin(activity);
        HttpModel.getInstance().tradeOverview(userName, new HashMapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                iHome.stopRecyclerView();
            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                iHome.stopRecyclerView();
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
//                        iHome.setMoneyCount(!TextUtils.isEmpty(response.get("transCount")) ? response.get("transCount") : "0");//总笔数
                        iHome.setMoneyCount(!TextUtils.isEmpty(response.get("todayCount")) ? response.get("todayCount") : "0");//总笔数
//                        iHome.setRefund(!TextUtils.isEmpty(response.get("totalRefund")) ? response.get("totalRefund") : "0");//退款
//                        iHome.setIncome(!TextUtils.isEmpty(response.get("totalAcquire")) ? response.get("totalAcquire") : "0");//总是实收
                        iHome.setIncome(!TextUtils.isEmpty(response.get("turnoverOf7Day")) ? response.get("turnoverOf7Day") : "0");//七天总计
//                        iHome.setMoney(!TextUtils.isEmpty(response.get("turnover")) ? response.get("turnover") : "0");//营业额
                        iHome.setMoney(!TextUtils.isEmpty(response.get("todayAcquire")) ? response.get("todayAcquire") : "0");//营业额
                        iHome.setTodayProfitAmount(!TextUtils.isEmpty(response.get("todayProfitAmount")) ? response.get("todayProfitAmount") : "0");
                        String json = response.get("perTotal");
                        TradeBean tradeBean = new Gson().fromJson(json, TradeBean.class);
                        //播放音频
                        Utilities.playerMusic(activity, R.raw.pulse);
                        iHome.setChartsData(tradeBean.getList());
                    } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                        DialogFactory.userSignOutDialog(activity, response.get("respDesc"));
                    } else {
                        ToastUtils.showToast(activity, response.get("respDesc"));
                    }
                } catch (Exception ex) {
                    ToastUtils.showToast(activity, "数据异常");
                    LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                }
            }
        });

    }

}
