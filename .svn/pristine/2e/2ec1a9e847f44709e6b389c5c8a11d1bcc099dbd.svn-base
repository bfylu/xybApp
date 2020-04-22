package com.fdsj.credittreasure.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.MyViewPagerAdapter;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * /**
 * <p>项目名称：MAJIA
 * <p>描   述：  统计
 * <p>当前版本： V1.0.0
 * http://dev-zl.payadd.cn/mpos/html/count.html
 */
@Deprecated
public class StatisticsFragment1 extends BaseFragment {
//    @BindView(R.id.swipeRefreshWebView)
//    SwipeRefreshWebView swipeRefreshWebView;

    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;


    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private MyViewPagerAdapter viewPagerAdapter;
    private List<String> stringList;
    private List<Fragment> fragmentList;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_statistics1;
    }

    @Override
    protected void initView() {
        stringList = new ArrayList<>();
        stringList.add("运营概况");
        stringList.add("当月流量统计");
        fragmentList = new ArrayList<>();
        fragmentList.add(OperateFragment.getOperateFragment(1));
        fragmentList.add(OperateFragment.getOperateFragment(2));

//
//
//        String Url = Config.URLStatistics;
//        String userName = Utilities.getUserName(activity);
//        String sessionToken = Utilities.getSessionToKen(activity);
//        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(sessionToken)) {
//            Url = Url.replace("myusername", userName);
//            Url = Url.replace("mysessionToken", sessionToken);
//            LogUtil.info("url", Url);
//            swipeRefreshWebView.setWebLoadUrl(Url);
//        } else {
//            DialogFactory.userSignOutDialog(activity, getActivity().getResources().getString(R.string.no_login));
//        }
//
//        HttpModel.getInstance().queryStatistic(userName, new HashMapCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(Map<String, String> response, int id) {
//
//            }
//        });
    }

    @Override
    protected void initData() {
        viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), viewPager, fragmentList);
        slidingTabLayout.setViewPager(viewPager, stringList);
    }

//    @Override
//    public void onRefresh() {
//
//    }

//    private long exitTime;// 退出时间
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && swipeRefreshWebView.canGoBack()) {
//            swipeRefreshWebView.goBack();
//            return true;
//        } else if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if ((System.currentTimeMillis() - exitTime) > 2000) {
//                ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.once_again));
//                exitTime = System.currentTimeMillis();
//            } else {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                android.os.Process.killProcess(android.os.Process.myPid());
//            }
//            return true;
//        }
//        return false;
//
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        swipeRefreshWebView.webDestroy();
//    }

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
