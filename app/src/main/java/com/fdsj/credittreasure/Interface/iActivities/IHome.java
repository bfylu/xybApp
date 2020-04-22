package com.fdsj.credittreasure.Interface.iActivities;

import com.fdsj.credittreasure.entity.TradeBean;

import java.util.List;

/**
 * Created by 冷佳兴 on 2017/1/8-15:31.
 * 我是大傻逼，所在公司:博信诺达
 */

public interface IHome {
    void setChartsData(List<TradeBean.Bean> beanList);

//    /**
//     * 设置退款
//     *
//     * @param data
//     */
//    void setRefund(String data);

    /**
     * 设置今日金额
     *
     * @param data
     */
    void setMoney(String data);

    /**
     * 设置实收数目
     *
     * @param data
     */
    void setMoneyCount(String data);

    /**
     * 设置实收金额
     *
     * @param data
     */
    void setIncome(String data);

    void stopRecyclerView();

    /**
     * 设置今日收益
     * @param data
     */
    void setTodayProfitAmount(String data);

    void getMerCodeAndChanPlatId(String merCode, String chanPlatId);
}
