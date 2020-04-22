package com.fdsj.credittreasure.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.MyViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;
import com.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * <p>描   述：扫一扫界面
 * <p>当前版本： V1.0.0
 */
@Deprecated
public class SaoSaoFragment extends BaseFragment {
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    MyViewPagerAdapter adapter;

    List<String> stringList;
    List<Fragment> fragmentList;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_gathering_saosao;
    }

    @Override
    protected void initView() {
        stringList = new ArrayList<>();
        stringList.add(getActivity().getResources().getString(R.string.smart_sweep));
        stringList.add(getActivity().getResources().getString(R.string.weixin_pay));
        stringList.add(getActivity().getResources().getString(R.string.alipay));
        stringList.add(getActivity().getResources().getString(R.string.card));
        fragmentList = new ArrayList<>();
//        /**
//         * 执行扫面Fragment的初始化操作
//         */
//        CaptureFragment captureFragment = new CaptureFragment();
//        // 为二维码扫描界面设置定制化界面
//        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
//
//        captureFragment.setAnalyzeCallback(analyzeCallback);
//
//        CaptureFragment captureFragment1 = new CaptureFragment();
//        // 为二维码扫描界面设置定制化界面
//        CodeUtils.setFragmentArgs(captureFragment1, R.layout.my_camera);
//
//        captureFragment1.setAnalyzeCallback(analyzeCallback);


        fragmentList.add(CaptureFragment.getInstance().setAnalyzeCallback(analyzeCallback));
        fragmentList.add(CaptureFragment.getInstance().setAnalyzeCallback(analyzeCallback));
        fragmentList.add(CaptureFragment.getInstance().setAnalyzeCallback(analyzeCallback));
        fragmentList.add(CaptureFragment.getInstance().setAnalyzeCallback(analyzeCallback));
//        fragmentList.add(new ScanPayFragment());
//        adapter = new FragAdapter(getChildFragmentManager(), fragmentList);
    }

    @Override
    protected void initData() {
        viewPager.setOffscreenPageLimit(0);
        adapter = new MyViewPagerAdapter(getFragmentManager(), viewPager, fragmentList);
        slidingTabLayout.setViewPager(viewPager, stringList);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            ToastUtils.showToast(activity, result);
            resultIntent.putExtras(bundle);
            activity.setResult(RESULT_OK, resultIntent);
            activity.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            activity.setResult(RESULT_OK, resultIntent);
            activity.finish();
        }
    };
}
