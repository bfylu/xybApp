package cn.payadd.majia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fdsj.credittreasure.R;

import java.util.ArrayList;
import java.util.List;
import cn.payadd.majia.adapter.PreOrderFragmentPagerAdapter;
import cn.payadd.majia.constant.ActivityTitle;
import cn.payadd.majia.fragment.AllPreOrderFragment;
import cn.payadd.majia.fragment.UnbalancedPreOrderFragment;

/**
 * Created by df on 2017/6/21.
 */

public class FundAuthOrderActivity extends BaseActivity implements RadioGroup
        .OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private RadioButton rbAllOrder, rbUnbalancedOrder;
    private RadioGroup rgOrder;
    private ViewPager vpOrder;

    private Bundle mCondition;
    private List<Fragment> fragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_auth_order;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText(ActivityTitle.FUND_AUTH_ORDER);
        setTitleRightText("筛选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //筛选
                Intent intent = new Intent(FundAuthOrderActivity.this, FiltrateActivity.class);

                //查找筛选的数据
                Bundle bundle = getCondition();
                if (bundle != null) {
                    intent.putExtra("filtrate", bundle);
                }
                startActivityForResult(intent, 0);
            }
        });
        rgOrder = (RadioGroup) findViewById(R.id.rg_order);
        rbAllOrder = (RadioButton) findViewById(R.id.rb_all);
        rbUnbalancedOrder = (RadioButton) findViewById(R.id.rb_unbalanced);
        vpOrder = (ViewPager) findViewById(R.id.vp_order);
    }

    @Override
    public void initData() {
        rgOrder.setOnCheckedChangeListener(this);
        vpOrder.addOnPageChangeListener(this);
        FragmentManager fm = getSupportFragmentManager();
        AllPreOrderFragment allPreOrder = new AllPreOrderFragment();
        UnbalancedPreOrderFragment unbalancedPreOrder = new UnbalancedPreOrderFragment();
        fragments = new ArrayList<>();
        fragments.add(allPreOrder);
        fragments.add(unbalancedPreOrder);
        vpOrder.setAdapter(new PreOrderFragmentPagerAdapter(fm, fragments));
        //查找筛选的数据
        Bundle bundle = getCondition();
        if (bundle != null) {
            String status = bundle.getString("status");
            if ("01".equals(status)) {
                rbUnbalancedOrder.setChecked(true);
            } else {
                rbAllOrder.setChecked(true);
            }
        } else {
            rbAllOrder.setChecked(true);
        }

    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_all:
                vpOrder.setCurrentItem(0);
                break;
            case R.id.rb_unbalanced:
                vpOrder.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rbAllOrder.setChecked(true);

                break;
            case 1:
                rbUnbalancedOrder.setChecked(true);

                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setTitleRightText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String status;
            if (bundle != null) {
                status = bundle.getString("status");
                setCondition(bundle);
            } else {
                rbAllOrder.setChecked(true);
                return;
            }

            fragments.clear();
            vpOrder.removeAllViews();
            FragmentManager fm = getSupportFragmentManager();
            AllPreOrderFragment allPreOrder = new AllPreOrderFragment();
            UnbalancedPreOrderFragment unbalancedPreOrder = new UnbalancedPreOrderFragment();
            fragments = new ArrayList<>();
            fragments.add(allPreOrder);
            fragments.add(unbalancedPreOrder);
            vpOrder.setAdapter(new PreOrderFragmentPagerAdapter(fm, fragments));

            if ("01".equals(status)) {
                rbUnbalancedOrder.setChecked(true);
            } else {
                rbAllOrder.setChecked(true);
            }
        }
    }

    public Bundle getCondition() {
        return this.mCondition;
    }

    public void setCondition(Bundle condition) {
        this.mCondition = condition;
    }

}
