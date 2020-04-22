package com.fdsj.credittreasure.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.MyViewPagerAdapter;
import com.fdsj.credittreasure.fragment.FlowFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.utils.Enums;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.payadd.majia.activity.CollectFormActivity;
import cn.payadd.majia.activity.OrderFiltrateActivity;
import cn.payadd.majia.config.EnvironmentConfig;

/**
 * Created by BXND on 2016-08-18.
 * 流水
 */
public class FlowActivity extends BaseActivity {

    private static final String LOG_TAG = FlowActivity.class.getName();

    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tv_collection_amount)
    TextView tv_collection_amount;//实收
    @BindView(R.id.tv_total_count)
    TextView tv_total_count;//交易笔数

    @BindView(R.id.tv_refund)
    TextView tv_refund;//退款

    public static String condition = "";
    public final static int REQUEST = 1;
    public final static int REQUESTTo = 2;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyViewPagerAdapter myViewPagerAdapter;
    private List<String> stringList;
    private Enums.httpPayType payType;
    private String prodType;
    private String startTime = "", endTime = "";

    @Override
    protected int getLayoutView() {
        return R.layout.activity_flow;
    }

    @Override
    protected void initView() {
        super.setBackOnclick();
        super.setCenterText(getResources().getString(R.string.account_statement));
        super.setRightText("筛选", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FlowActivity.this, OrderFiltrateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("condition", condition);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST);
                    }
                }

        );
        if(EnvironmentConfig.isPos()) {
            super.setExtendText("汇总单", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(FlowActivity.this, CollectFormActivity.class);
                            startActivity(intent);
                        }
                    }
            );
        }
    }


    @Override
    protected void initData() {
        mFragments.add(FlowFragment.getFlowFragment(Enums.transType.已收款));
        mFragments.add(FlowFragment.getFlowFragment(Enums.transType.待收款));
        mFragments.add(FlowFragment.getFlowFragment(Enums.transType.所有));
        stringList = new ArrayList<>();
        stringList.add(getResources().getString(R.string.already_collection));
        stringList.add(getResources().getString(R.string.agency_fund));
        stringList.add(getResources().getString(R.string.all));
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), viewPager, mFragments);
        slidingTabLayout.setViewPager(viewPager, stringList);
    }

    public void initTotalView(String income, String count, String refund) {
        tv_collection_amount.setText(income);//实收
        tv_total_count.setText(count);//交易笔数
        tv_refund.setText(refund);//退款
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (resultCode == REQUEST) {
                Bundle bundle = data.getExtras();

                condition = bundle.getString("condition","");
                Log.d(LOG_TAG, condition);
                mFragments.clear();
                viewPager.removeAllViews();
                myViewPagerAdapter.removeAllViews();
                mFragments.add(FlowFragment.getFlowFragment(Enums.transType.已收款));
                mFragments.add(FlowFragment.getFlowFragment(Enums.transType.待收款));
                mFragments.add(FlowFragment.getFlowFragment(Enums.transType.所有));
                myViewPagerAdapter.setFragments(viewPager, mFragments);// = new MyViewPagerAdapter(getSupportFragmentManager(), viewPager, mFragments);
                slidingTabLayout.setViewPager(viewPager, stringList);
                slidingTabLayout.updateTabSelection(0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.condition = "";
    }
}
