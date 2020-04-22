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
import com.fdsj.credittreasure.fragment.Flow2Fragment;
import com.utils.Enums;

import java.util.ArrayList;

import butterknife.BindView;
import cn.payadd.majia.activity.CollectFormActivity;
import cn.payadd.majia.activity.OrderFiltrateActivity;
import cn.payadd.majia.config.EnvironmentConfig;

/**
 * Created by BXND on 2016-08-18.
 * 流水
 */
public class Flow2Activity extends BaseActivity {

    private static final String LOG_TAG = Flow2Activity.class.getName();

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;//今日交易金额
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;//今日交易笔数

    @BindView(R.id.tv_total_amount_title)
    TextView tvTotalAmountTitle;//交易金额
    @BindView(R.id.tv_total_count_title)
    TextView tvTotalCountTitle;//交易笔数

//    @BindView(tv_refund)
//    TextView tvRefund;//退款

    public static String condition = "";
    public final static int REQUEST = 1;
    public final static int REQUESTTo = 2;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyViewPagerAdapter myViewPagerAdapter;
    private Enums.httpPayType payType;
    private String prodType;
    private String startTime = "", endTime = "";

    @Override
    protected int getLayoutView() {
        return R.layout.activity_flow2;
    }

    @Override
    protected void initView() {
        super.setBackOnclick();
        super.setCenterText(getResources().getString(R.string.account_statement));
        super.setRightText("筛选", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Flow2Activity.this, OrderFiltrateActivity.class);
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
                            Intent intent = new Intent(Flow2Activity.this, CollectFormActivity.class);
                            startActivity(intent);
                        }
                    }
            );
        }
    }

    @Override
    protected void initData() {
        mFragments.add(Flow2Fragment.getFlowFragment(Enums.transType.所有, false));
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), viewPager, mFragments);
    }

    public void initTotalView(String income, String count, String refund) {
        tvTotalAmount.setText(income);//实收
        tvTotalCount.setText(count);//交易笔数
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
                mFragments.add(Flow2Fragment.getFlowFragment(Enums.transType.所有, true));
                myViewPagerAdapter.setFragments(viewPager, mFragments);// = new MyViewPagerAdapter(getSupportFragmentManager(), viewPager, mFragments);

                tvTotalAmountTitle.setText(getResources().getString(R.string.terminal_amount_2));
                tvTotalCountTitle.setText(getResources().getString(R.string.deal_amount_2));
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
