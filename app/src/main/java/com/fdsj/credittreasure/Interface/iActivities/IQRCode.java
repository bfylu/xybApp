package com.fdsj.credittreasure.Interface.iActivities;

/**
 * Created by 冷佳兴
 * 我是大傻逼，所在公司:博信诺达
 * 作者：chery on 2017/1/23 - 12:25
 * 包名：com.fdsj.credittreasure.Interface.iActivities
 */
public interface IQRCode {
    void NoModel();

    void UpdateModel(final String payUrl, String orderNo, int status);
}
