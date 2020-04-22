package com.utils.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.utils.R;

/**
 * Created by 冷佳兴 on 2017/1/8-14:03.
 * 我是大傻逼，所在公司:博信诺达
 */

public class CheryDialog extends Dialog {
    private Window window;
    private Activity activity;
    private int animationId = R.style.main_menu_animstyle;
    private int gravity = Gravity.CENTER;
    private int y = 0;
    private int width = ViewGroup.LayoutParams.WRAP_CONTENT;


    public CheryDialog(Activity activity, View view) {
        super(activity, R.style.transparentFrameWindowStyle);
        this.activity = activity;
        setContentView(view, new ViewGroup.LayoutParams(this.width, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public CheryDialog initView() {
        window = this.getWindow();
        if (animationId != 0)
            window.setWindowAnimations(animationId);
        if (gravity != 0)
            window.setGravity(gravity);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = y;// activity.getWindowManager().getDefaultDisplay().getHeight();
        wl.width = this.width;// ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.onWindowAttributesChanged(wl);
        return this;
    }


    public CheryDialog setWidth(int width) {
        this.width = width;
        return this;
    }

    public CheryDialog setAnimationId(int animationId) {
        this.animationId = animationId;
        return this;
    }


    public CheryDialog setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public CheryDialog setY(int y) {
        this.y = y;
        return this;
    }
}
