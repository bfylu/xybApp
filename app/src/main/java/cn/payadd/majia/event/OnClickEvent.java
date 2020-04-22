package cn.payadd.majia.event;

import android.view.View;

/**
 * 防止重复点击
 * Created by df on 2017/9/21.
 */

public abstract class OnClickEvent implements View.OnClickListener {

    private static long lastTime;

    public abstract void singleClick(View v);
    private long delay;

    public OnClickEvent(long delay) {
        this.delay = delay;
    }

    @Override
    public void onClick(View v) {
        if (onMoreClick(v)) {
            return;
        }
        singleClick(v);
    }

    public boolean onMoreClick(View v) {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;
        if (time < delay) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }
}
