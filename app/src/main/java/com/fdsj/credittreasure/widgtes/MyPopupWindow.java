package com.fdsj.credittreasure.widgtes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.MyOnClickListener;
import com.fdsj.credittreasure.R;

import rx.Observer;

/**
 * Created by 冷佳兴 on 2016/11/23-18:21.
 * 我是大傻逼，所在公司:博信诺达
 */

public class MyPopupWindow extends PopupWindow {
    private Activity activity;
    private LayoutInflater inflater;
    private View defaultView;
    private MyOnClickListener onClickListener;

    public void setMyOnClickListener(MyOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MyPopupWindow(Activity activity, View view) {
        super(activity);
        this.activity = activity;
        this.defaultView = view;
        initPopupWindow();
    }

    public MyPopupWindow(Activity activity, int resId) {
        super(activity);
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        defaultView = inflater.inflate(resId, null);
        initPopupWindow();
    }

    public void initPopupWindow() {
        defaultView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(defaultView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00111111")));
        setFocusable(true);
        setOutsideTouchable(true);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    @Override
    public void showAsDropDown(View anchor) {
        backgroundAlpha(0.7f);
        super.showAsDropDown(anchor);
    }

    @Override
    public void dismiss() {
        backgroundAlpha(1f);
        super.dismiss();
        if (onClickListener != null) {
            onClickListener.Onclick(null, null, 0);
        }
    }

    public void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = f; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public View getDefaultView() {
        return defaultView;
    }
}
