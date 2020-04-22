package com.fdsj.credittreasure.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.Interface.MyOnClickListener;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.Observer;

/**
 * Created by 冷佳兴 on 2017/1/2-22:42.
 *
 */

public abstract class BaseActivity extends AppCompatActivity {

    //    private final String TAG = "123456";
    private InputMethodManager mManager;
    protected BaseApplication baseApplication;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(BaseActivity.this, "5628399fe0f55a493c00540f", "bxnd"));
        mManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(getLayoutView());
        ButterKnife.bind(this);
        baseApplication = (BaseApplication) getApplication();
        baseApplication.addActivity(this);
        initView();
        initData();
    }

    public void setCenterText(String Text) {
        ((TextView) findViewById(R.id.title_title)).setText(Text);
    }

    public void setBackOnclick() {
        setBackShow();
        $(R.id.relative_title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 更改导航栏背景颜色
     *
     * @param color
     */
    public void setActionBarBackgroundColor(int color) {
        $(R.id.line_action).setBackgroundColor(getResources().getColor(color));
    }

    public void setActionBarBackgroundGone() {
        $(R.id.line_action).setVisibility(View.GONE);
    }

    /**
     * 更改导航栏背景图片
     *
     * @param r
     */
    public void setActionBarBackground(int r) {
        $(R.id.line_action).setAlpha(1);
        $(R.id.line_action).setBackgroundResource(r);
    }


    public void setBackOnclick(View.OnClickListener onclick) {
        setBackShow();
        findViewById(R.id.relative_title_back).setOnClickListener(onclick);
    }

    private void setBackShow() {
        findViewById(R.id.title_back).setVisibility(View.VISIBLE);
    }

//    public void setRightOnclick(View.OnClickListener onclick) {
//        findViewById(R.id.relative_title_register).setOnClickListener(onclick);
//    }
//
//    public void setRightHide() {
//        findViewById(R.id.title_register).setVisibility(View.GONE);
//    }
//
//    public void setRightTitle(String data) {
//        ((TextView) findViewById(R.id.title_register)).setText(data);
//    }

    public void setRightImage(int resId) {
        ((ImageView) findViewById(R.id.image_right)).setImageResource(resId);
    }


    public void setRightImage(int resId, View.OnClickListener onClickListener) {
        ((ImageView) findViewById(R.id.image_right)).setImageResource(resId);
        findViewById(R.id.relative_title_right).setOnClickListener(onClickListener);
//        ((ImageView) findViewById(R.id.image_right)).setImageResource(resId);
    }

    public void setRightText(String txt, View.OnClickListener onClickListener) {
        TextView tv = (TextView) findViewById(R.id.tv_right);
        tv.setText(txt);
        tv.setVisibility(View.VISIBLE);
        tv.setOnClickListener(onClickListener);
    }
    public void setExtendText(String txt, View.OnClickListener onClickListener) {
        TextView tv = (TextView) findViewById(R.id.tv_extend);
        tv.setText(txt);
        tv.setVisibility(View.VISIBLE);
        tv.setOnClickListener(onClickListener);
    }



    public void setRightImage(Bitmap bm) {
        ((ImageView) findViewById(R.id.image_right)).setImageBitmap(bm);
    }


    /**
     * 返回值为所要加载的布局文件
     *
     * @return
     */
    protected abstract int getLayoutView();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    public EditText getSearchEditText(int resId, final MyOnClickListener onClickListener) {
        EditText editText = $(resId);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //do something;
                    onClickListener.Onclick(v, "", actionId);
                    return true;
                }
                return false;
            }
        });
        return editText;
    }


    protected FragmentManager fragmentManager;
    protected Fragment currentFragment;

    /**
     * 替换中间部分布局
     *
     * @param fragment
     */
    public void switchFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (null == currentFragment) {
                // 第一次切换fragment
                fragmentTransaction.add(R.id.framelayout, fragment).show(fragment).commit();
            } else {
                if (fragment.isAdded()) {
                    fragmentTransaction.hide(currentFragment).show(fragment).commit();
                } else {
                    fragmentTransaction.add(R.id.framelayout, fragment).hide(currentFragment).show(fragment).commit();
                }
            }
            currentFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected <T extends View> T $(int resId) {
        T t = (T) getWindow().findViewById(resId);
        return t;
    }


    /**
     * 所有子类
     * 点击键盘外空白,键盘消失
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
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

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.v(TAG, "Activity创建或者从后台重新回到前台时被调用-onStart");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.v(TAG, "Activity从后台重新回到前台时被调用-onRestart");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MobclickAgent.onResume(this);
//        Log.v(TAG, "Activity创建或者从被覆盖|后台重新回到前台时被调用-onResume");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MobclickAgent.onPause(this);
//        Log.v(TAG, "Activity被覆盖|锁屏时被调用-onPause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.v(TAG, "退出当前Activity或者跳转到新Activity时被调用-onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.v(TAG, "退出当前Activity时被调用,调用之后Activity就结束了-onDestroy");
//    }
}
