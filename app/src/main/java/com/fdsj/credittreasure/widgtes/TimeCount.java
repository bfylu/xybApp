package com.fdsj.credittreasure.widgtes;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by BXND on 2016-08-18.
 */
public class TimeCount extends CountDownTimer {

    Button button;

    public TimeCount(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setEnabled(false);
        button.setText(millisUntilFinished / 1000 + "秒后重新发送");
    }

    @Override
    public void onFinish() {
        button.setText("发送验证码");
        button.setEnabled(true);
    }
}
