package com.fdsj.credittreasure.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cn.payadd.majia.fragment.StatisticsFragment;

/**
 * Created by zhengzhen.wang on 2017/4/26.
 */

public class PayPushBroadcastReceiver extends BroadcastReceiver {

    public static final String PAY_RESULT_PUSH_BROADCAST_NAME = "cn.payadd.majia.pay.result.push";

    private Callback callback;

    public PayPushBroadcastReceiver(Callback callback) {

        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (PAY_RESULT_PUSH_BROADCAST_NAME.equals(action)) {
            if (null != callback) {
                callback.exec(intent.getExtras());
            }
        }
        StatisticsFragment.isFlush = true;
    }

    public interface Callback {

        void exec(Bundle data);
    }
}
