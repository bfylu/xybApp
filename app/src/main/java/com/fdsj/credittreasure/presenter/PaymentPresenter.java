package com.fdsj.credittreasure.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.Interface.iActivities.IQRCode;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.entity.HttpModel;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.utils.Config;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by BXND on 2017-01-17.
 */

public class PaymentPresenter {
    IQRCode iActivity;

    public PaymentPresenter(IQRCode iActivity) {
        this.iActivity = iActivity;
    }

    /**
     * 提交订单
     *
     * @param amount    金额
     * @param authCode  授权码，使用刷卡支付时条码或者二维码信息
     * @param payMethod 支付方式
     * @param payType   支付类型
     */
    public synchronized void submitOrder(final int i, String amount, String authCode, final Enums.payMethod payMethod, Enums.httpPayType payType, final Activity activity) {

        if (Enums.httpPayType.银联支付 == payType && !Config.isPos()) {
            ToastUtils.showToast(activity, activity.getResources().getString(R.string.no_function));
            return;
        }

        HttpModel.getInstance().submitOrder(amount, authCode, payMethod, payType, activity, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                iActivity.NoModel();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Map<String, String> hashMap = Utilities.fromHashMap(response);// new HashMap<>();
                    if (hashMap.get("respCode").equals(Enums.apiState.成功.getString())) {
                        String[] temp = response.split("\\&");
                        String payUrl = "";
                        for (String s : temp) {
                            if (!TextUtils.isEmpty(s) && s.contains("payUrl")) {
                                payUrl = s.replace("payUrl=", "");
                            }
                        }
                        LogUtil.info("payUrl", payUrl);
                        if (payMethod == Enums.payMethod.二维码) {
                            if (!TextUtils.isEmpty(payUrl)) {
                                iActivity.UpdateModel(payUrl, hashMap.get("orderNo"), i);
                            } else {
                                ToastUtils.showToast(activity, hashMap.get("respDesc"));
                                iActivity.NoModel();
                            }
                        } else if (payMethod == Enums.payMethod.扫码) {
                            iActivity.UpdateModel(payUrl, hashMap.get("orderNo"), i);

                        } else if (payMethod == Enums.payMethod.刷卡) {
                            iActivity.UpdateModel(payUrl, hashMap.get("orderNo"), i);
                        }
                    } else if (hashMap.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || hashMap.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                        DialogFactory.userSignOutDialog(activity, hashMap.get("respDesc"));
                    } else {
                        ToastUtils.showToast(activity, hashMap.get("respDesc"));
                        iActivity.NoModel();
                    }
                } catch (Exception ex) {
                    ToastUtils.showToast(activity, "数据异常");
                    LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                }
            }
        });
    }
}
