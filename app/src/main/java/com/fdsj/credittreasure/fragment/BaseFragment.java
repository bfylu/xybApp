package com.fdsj.credittreasure.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fdsj.credittreasure.R;

import butterknife.ButterKnife;


/**
 * Created by 冷佳兴 on 2016/08/19.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible, isPrepared;
    protected Activity activity;


    protected View rootView;

    /**
     * 获得布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.activity = getActivity();
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutView(), container, false);
        }
        ButterKnife.bind(this, rootView);
        ViewGroup p = (ViewGroup) rootView.getParent();
        if (p != null) {
            p.removeView(rootView);
        }
        return rootView;
    }


    /**
     * 初始化组件
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.rootView = view;
        initView();
    }

    protected View returnView() {
        return this.rootView;
    }

    protected <T extends View> T $(int resId) {
        T t = (T) rootView.findViewById(resId);
        return t;
    }

    /**
     * 初始化控件
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            //不可见
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        if (!isPrepared || !isVisible) {
            return;
        }
        lazyLoad();
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

    //    /**
//     * 延迟加载
//     * 子类必须重写此方法
//     */
    protected void lazyLoad() {

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
}
