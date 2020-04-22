package cn.payadd.majia.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.Observer;

/**
 * Created by zhengzhen.wang on 2017/6/7.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private BaseApplication baseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        initView();
        initData();
        initPermission();

        baseApplication = (BaseApplication) getApplication();
        baseApplication.addActivity(this);
    }

    public abstract int getLayoutId();

    public abstract void initView();

    protected abstract void initData();

    protected abstract void initPermission();

    public void setTitleBackButton(View.OnClickListener onclick) {

        findViewById(R.id.title_back).setVisibility(View.VISIBLE);
        findViewById(R.id.relative_title_back).setOnClickListener(onclick);
    }

    public void setTitleBackButton() {

        findViewById(R.id.title_back).setVisibility(View.VISIBLE);
        findViewById(R.id.relative_title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setTitleCenterText(String txt) {

        TextView tv = (TextView) findViewById(R.id.title_title);
        tv.setText(txt);
    }

    public void setTitleCenterText(String txt, View.OnClickListener onclick) {

        TextView tv = (TextView) findViewById(R.id.title_title);
        tv.setText(txt);
        tv.setClickable(true);
        tv.setOnClickListener(onclick);
    }

    public void setTitleRightText(String txt) {

        TextView tv = (TextView) findViewById(R.id.tv_right);
        tv.setText(txt);
        tv.setVisibility(View.VISIBLE);
    }

    public void setTitleRightText(String txt, View.OnClickListener onclick) {

        TextView tv = (TextView) findViewById(R.id.tv_right);

        tv.setText(txt);
        tv.setVisibility(View.VISIBLE);
        tv.setClickable(true);
        tv.setOnClickListener(onclick);
    }

    public void setTitleRightImage(Drawable drawable, View.OnClickListener onclick) {

        ImageView iv = (ImageView) findViewById(R.id.image_right);
        iv.setImageDrawable(drawable);
        iv.setVisibility(View.VISIBLE);
        iv.setClickable(true);
        iv.setOnClickListener(onclick);
    }

    public void setTitleRightImage(int id, View.OnClickListener onclick) {

        ImageView iv = (ImageView) findViewById(R.id.image_right);
        iv.setImageDrawable(getResources().getDrawable(id));
        iv.setVisibility(View.VISIBLE);
        iv.setClickable(true);
        iv.setOnClickListener(onclick);
    }

    /**
     * 更改导航栏背景颜色
     *
     * @param color
     */
    public void setActionBarBackgroundColor(int color) {
        findViewById(R.id.line_action).setBackgroundColor(getResources().getColor(color));
    }

    /**
     * 防止重复点击
     *
     * @param target 目标view
     * @param listener 监听器
     */
    public static void preventRepeatedClick(final View target, final View.OnClickListener listener) {
        RxView.clicks(target).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Observer<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object object) {
                listener.onClick(target);
            }
        });
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showSoftKeyboard(Activity act, View view) {
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
//        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

}
