package com.fdsj.credittreasure.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.IMain;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.fragment.HomeFragment;
import com.fdsj.credittreasure.fragment.MineFragment;
import com.fdsj.credittreasure.presenter.SyncDataPresenter;
import com.utils.StatusBarUtils;
import com.utils.Utilities;

import butterknife.BindView;
import cn.payadd.majia.activity.InstallmentDetailActivity;
import cn.payadd.majia.activity.InstallmentOrderActivity;
import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.entity.aistore.AIBaseBean;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.fragment.ManageFragment;
import cn.payadd.majia.fragment.PosHomeFragment;
import cn.payadd.majia.fragment.StatisticsFragment;
import cn.payadd.majia.presenter.VersionUpdatePresenter;
import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends BaseActivity implements View.OnClickListener, IActivity, IMain {

    @BindView(R.id.image_home)
    ImageView image_home;

    @BindView(R.id.text_home)
    TextView text_home;

    @BindView(R.id.image_statistics)
    ImageView image_statistics;

    @BindView(R.id.text_statistics)
    TextView text_statistics;

    @BindView(R.id.image_manage)
    ImageView image_manage;

    @BindView(R.id.text_manage)
    TextView text_manage;

    @BindView(R.id.image_mine)
    ImageView image_mine;

    @BindView(R.id.text_mine)
    TextView text_mine;

    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_statistics)
    LinearLayout ll_statistics;
    @BindView(R.id.ll_manage)
    LinearLayout ll_manage;
    @BindView(R.id.ll_mine)
    LinearLayout ll_mine;
    @BindView(R.id.relative_news)
    RelativeLayout rlManage;
    HomeFragment homeFragment;//首页
    PosHomeFragment posHomeFragment;//pos机首页
    StatisticsFragment statisticsFragment;//统计
    //    WebStatisticsFragment statisticsFragment;
    ManageFragment manageFragment;//消息
    MineFragment mineFragment;//个人中心

    VersionUpdatePresenter updatePresenter;

    @BindView(R.id.include_title)
    View title;
    public String todayProfitAmount;

    private QBadgeView qBadgeView;

    private Fragment currentFragment;

    private int noticeMsgCount;

    private SyncDataPresenter mSyncDataPresenter;



    public void setTitleVisible(boolean flag){
        if(flag){
            title.setVisibility(View.VISIBLE);
        }else{
            title.setVisibility(View.GONE);
        }
    }
    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        BaseApplication.setMainActivity(this);
        super.setCenterText("");
        updatePresenter = new VersionUpdatePresenter(this);
        mSyncDataPresenter = new SyncDataPresenter(this, this);
        //presenter = new DownLoadPresenter();
        if(getIntent().getExtras()!= null){
            String openType = getIntent().getExtras().getString("openType");
            boolean isNew = getIntent().getExtras().getBoolean("isNew");
            String orderNo = getIntent().getExtras().getString(InstallmentDetailActivity.KEY_ORDER_NO);
            if("notice".equals(openType)){
                if(isNew){
                    Intent intent = new Intent(this, InstallmentOrderActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(this, InstallmentDetailActivity.class);
                    intent.putExtra("orderNo",orderNo);
                    startActivity(intent);
                }
            }
        }
        if(EnvironmentConfig.isPos()){
            rlManage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        setNoticeMsgCount(Utilities.getIsmtNoticeCount(this));

        mSyncDataPresenter.synchMerData(Utilities.getMerCode(this)
                , Utilities.getNickName(this)
                , "https://ums.payadd.cn/images/shop.png");

        fragmentManager = getSupportFragmentManager();

        if (null == statisticsFragment) {
            statisticsFragment = new StatisticsFragment();
        }
        if (null == homeFragment) {
            homeFragment = new HomeFragment();
        }
        if (null == manageFragment) {
            manageFragment = new ManageFragment();
        }
        if(null == posHomeFragment){
            posHomeFragment = new PosHomeFragment();
        }
        if(EnvironmentConfig.isPos()){
            currentFragment = posHomeFragment;
        }else{
            currentFragment = homeFragment;
        }
        setBottom(1);
        switchFragment(currentFragment);
       // presenter.downloadApk(MainActivity.this);
        //请求更新操作
        String userName = Utilities.getUsernameForLogin(this);
        updatePresenter.updateApp(userName);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_home:

                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                if(null == posHomeFragment){
                    posHomeFragment = new PosHomeFragment();
                }
                if(EnvironmentConfig.isPos()){
                    currentFragment = posHomeFragment;
                }else{
                    currentFragment = homeFragment;
                }
                setBottom(1);
                break;
            case R.id.relative_statistics:

                if (statisticsFragment == null) {
                    statisticsFragment = new StatisticsFragment();
                }
                currentFragment = statisticsFragment;
                setBottom(2);
                break;
            case R.id.relative_news:

                if (manageFragment == null) {
                    manageFragment = new ManageFragment();
                }
                currentFragment = manageFragment;
                setBottom(3);
                break;
            case R.id.relative_mine:

                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                }
                currentFragment = mineFragment;
                setBottom(4);
                break;
        }
        switchFragment(currentFragment);
    }

    public void setBottom(int r) {
        image_home.setImageResource(r == 1 ? R.mipmap.home : R.mipmap.home_un);
        text_home.setTextColor(getResources().getColor(r == 1 ? R.color.color_Home : R.color.color_6));

        image_statistics.setImageResource(r == 2 ? R.mipmap.statistics : R.mipmap.statistics_un);
        text_statistics.setTextColor(getResources().getColor(r == 2 ? R.color.color_Home : R.color.color_6));

        image_manage.setImageResource(r == 3 ? R.mipmap.footnav_manage_selected : R.mipmap.footnav_manage_normal);
        text_manage.setTextColor(getResources().getColor(r == 3 ? R.color.color_Home : R.color.color_6));

        image_mine.setImageResource(r == 4 ? R.mipmap.mine : R.mipmap.mine_un);
        text_mine.setTextColor(getResources().getColor(r == 4 ? R.color.color_Home : R.color.color_6));

        switch (r) {
            case 1:
                if(currentFragment instanceof  HomeFragment){
                    StatusBarUtils.setBackGroundResource(this, R.mipmap.duaichilana);//更改状态栏背景图片
                    super.setActionBarBackground(R.mipmap.biaotia);
                }else if(currentFragment instanceof  PosHomeFragment){
                    StatusBarUtils.setColor(this, getResources().getColor(R.color.color_Home));//更改状态栏背景颜色
                }

                super.setCenterText("");
                break;
            case 2:
                super.setCenterText(getResources().getString(R.string.statistics));
                StatusBarUtils.setColor(this, getResources().getColor(R.color.color_Home));//更改状态栏背景颜色
                super.setActionBarBackgroundColor(R.color.color_Home);
                break;
            case 3:
                super.setCenterText(getResources().getString(R.string.manage));
                StatusBarUtils.setColor(this, getResources().getColor(R.color.color_Home));//更改状态栏背景颜色
                super.setActionBarBackgroundColor(R.color.color_Home);
                break;
            case 4:
                StatusBarUtils.setBackGroundResource(this, R.mipmap.user_statusbar);//更改状态栏背景图片
                super.setActionBarBackground(R.mipmap.user_actionbar);
                super.setCenterText("");
                break;
        }
    }

    private long exitTime;// 退出时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
               Toast.makeText(this, getResources().getString(R.string.once_again),Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                android.os.Process.killProcess(android.os.Process.myPid());
                BaseApplication baseApplication = (BaseApplication) getApplication();
                baseApplication.removeAll();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.noticeMsgCount = Utilities.getIsmtNoticeCount(this);
        noticeMsgBindView(noticeMsgCount,ll_manage);
    }

    public int getNoticeMsgCount() {
        return noticeMsgCount;
    }

    public void setNoticeMsgCount(int noticeMsgCount) {
        this.noticeMsgCount = noticeMsgCount;
        Utilities.setIsmtNoticeCount(this,noticeMsgCount);
        noticeMsgBindView(noticeMsgCount,ll_manage);
        if(manageFragment!=null){
            if(EnvironmentConfig.isPos()){
                posHomeFragment.setNoticeMsgCount(noticeMsgCount);
            }else{
                manageFragment.setNoticeMsgCount(noticeMsgCount);
            }
        }
    }

    public void noticeMsgBindView(int noticeMsgCount,View view){
        if(view != null){
            if(noticeMsgCount > 0) {
                if(qBadgeView == null) {
                    qBadgeView = new QBadgeView(this);
                    qBadgeView.setBadgeGravity(Gravity.TOP | Gravity.END);
                    qBadgeView.setBadgeNumber(-1);
                    qBadgeView.setBadgePadding(5, true);
                    qBadgeView.bindTarget(view);
                }else{
                    qBadgeView.reset();
                    qBadgeView.setBadgeGravity(Gravity.TOP | Gravity.END);
                    qBadgeView.setBadgeNumber(-1);
                    qBadgeView.setBadgePadding(5, true);
                    qBadgeView.bindTarget(view);
                }

            }else{
                if(qBadgeView!=null){
                    qBadgeView.hide(true);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(qBadgeView!=null){
            qBadgeView.hide(true);
        }
    }

    public void setTodayProfitAmount(String amount){
        todayProfitAmount = amount;
    }
    public String getTodayProfitAmount(){
        return todayProfitAmount;
    }

    @Override
    public void updateModel(String key, Object data) {
        ///Log.d("请求成功了","----");
    }

    @Override
    public void synchMerData(AIBaseBean data) {

    }
}
