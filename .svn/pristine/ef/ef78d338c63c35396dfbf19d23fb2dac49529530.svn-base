package com.fdsj.credittreasure.fragment;

import android.text.TextUtils;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.fdsj.credittreasure.widgtes.SwipeRefreshWebView;
import com.utils.Config;
import com.utils.LogUtil;
import com.utils.Utilities;

import butterknife.BindView;

/**
 * Created by 冷佳兴
 * 我是大傻逼，所在公司:博信诺达
 * 作者：chery on 2017/1/23 - 21:53
 * 包名：com.fdsj.credittreasure.fragment
 */
public class WebStatisticsFragment extends BaseFragment {
    @BindView(R.id.swipeRefreshWebView)
    SwipeRefreshWebView swipeRefreshWebView;

//    @BindView(R.id.slidingTabLayout)
//    SlidingTabLayout slidingTabLayout;
//
//
//    @BindView(R.id.viewpager)
//    ViewPager viewPager;
//
//    private MyViewPagerAdapter viewPagerAdapter;
//    private List<String> stringList;
//    private List<Fragment> fragmentList;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_web_statistics;
    }

    @Override
    protected void initView() {
//        stringList = new ArrayList<>();
//        stringList.add("运营概况");
//        stringList.add("当月流量统计");
//        fragmentList = new ArrayList<>();
//        fragmentList.add(OperateFragment.getOperateFragment(1));
//        fragmentList.add(OperateFragment.getOperateFragment(2));

//
//
        String Url = Config.getH5Statistics();
        String userName = Utilities.getUsernameForLogin(activity);
        String sessionToken = Utilities.getSessionToKen(activity);
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(sessionToken)) {
            Url = Url.replace("myusername", userName);
            Url = Url.replace("mysessionToken", sessionToken);
            LogUtil.info("url", Url);
            swipeRefreshWebView.setWebLoadUrl(Url);
        } else {
            DialogFactory.userSignOutDialog(activity, getActivity().getResources().getString(R.string.no_login));
        }
    }

    @Override
    protected void initData() {
//        viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), viewPager, fragmentList);
//        slidingTabLayout.setViewPager(viewPager, stringList);
    }

//    @Override
//    public void onRefresh() {
//
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshWebView.webDestroy();
    }

    //    @BindView(R.id.slidingTabLayout)
//    SlidingTabLayout slidingTabLayout;
//    @BindView(R.id.viewpager)
//    ViewPager viewPager;
//
//    ArrayList<Fragment> mFragments = new ArrayList<>();
//    MyViewPagerAdapter myViewPagerAdapter;
//
//    @Override
//    protected int getLayoutView() {
//        return R.layout.fragment_statistics;
//    }
//
//    @Override
//    protected void initView() {
//        mFragments.add(new OperateFragment());//运营概况
//        mFragments.add(new OperateFragment());//当月流量统计
//        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), viewPager, mFragments);
//        List<String> stringList = new ArrayList<>();
//        stringList.add("运营概况");
//        stringList.add("当月流量统计");
//        viewPager.setAdapter(myViewPagerAdapter);
//        slidingTabLayout.setViewPager(viewPager, stringList);
//    }
//
//    @Override
//    protected void initData() {
//
//    }
}