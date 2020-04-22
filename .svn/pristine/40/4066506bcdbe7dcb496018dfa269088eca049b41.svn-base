package cn.payadd.majia.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by df on 2017/6/27.
 */

public class AuthCodeCountDownTimer extends CountDownTimer {
    private TextView view;
    private long countDownInterval;
    public AuthCodeCountDownTimer(long millisInFuture, long countDownInterval,TextView view) {
        super(millisInFuture, countDownInterval);
        this.view = view;
        this.countDownInterval = countDownInterval;
    }

    @Override
    public void onTick(long millisUntilFinished) {
            view.setClickable(false);
            view.setText(millisUntilFinished / countDownInterval + "s");
    }

    @Override
    public void onFinish() {
        //重新给Button设置文字
        view.setText("重新发送验证码");
        //设置可点击
        view.setClickable(true);
    }
}
