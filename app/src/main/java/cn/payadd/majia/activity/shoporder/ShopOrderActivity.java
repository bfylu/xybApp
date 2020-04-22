package cn.payadd.majia.activity.shoporder;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.adapter.shoporder.ShopOrderViewPagerAdapter;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.fragment.shoporder.ShopAllOrderFragment;
import cn.payadd.majia.fragment.shoporder.ShopProtectionOrderFragment;
import cn.payadd.majia.view.CustomViewPager;

/**
 * Created by df on 2018/3/13.
 */

public class ShopOrderActivity extends BaseActivity implements IActivity, View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_all_order)
    TextView tvAllOrder;
    @BindView(R.id.tv_protection_order)
    TextView tvProtectionOrder;
    @BindView(R.id.vp_father)
    CustomViewPager vpFather;

    private int changePosition;//点击的tab位置
    private List<Fragment> fragmentList;
    private ShopAllOrderFragment allOrderFragment;
    private ShopProtectionOrderFragment protectionOrderFragment;
    private ShopOrderViewPagerAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_order;
    }

    @Override
    public void initView() {
        changePosition = 0;
        fragmentList = new ArrayList<>();

        llAll.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);

        allOrderFragment = new ShopAllOrderFragment();
        protectionOrderFragment = new ShopProtectionOrderFragment();
        fragmentList.add(allOrderFragment);
        fragmentList.add(protectionOrderFragment);

        adapter = new ShopOrderViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        vpFather.setAdapter(adapter);
        vpFather.setCurrentItem(0);
        vpFather.addOnPageChangeListener(this);
        vpFather.setScanScroll(false);
        initTextView(0);

        ll_back.setOnClickListener(this);
        tvAllOrder.setOnClickListener(this);
        tvProtectionOrder.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_all_order:
                initTextView(0);
                vpFather.setCurrentItem(0);
                break;
            case R.id.tv_protection_order:
                initTextView(1);
                vpFather.setCurrentItem(1);
                break;
        }
    }
    @Override
    protected void initPermission() {

    }

    @Override
    public void updateModel(String key, Object data) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("position", changePosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        vpFather.setCurrentItem(savedInstanceState.getInt("position"));
        if (savedInstanceState.getInt("position") == 0) {
            initTextView(0);
        } else if (savedInstanceState.getInt("position") == 1) {
            initTextView(1);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initTextView(int position) {
        if (position == 0) {
            tvAllOrder.setBackgroundColor(getResources().getColor(R.color.color_f));
            tvAllOrder.setTextColor(getResources().getColor(R.color.color_Home));

            tvProtectionOrder.setBackgroundColor(getResources().getColor(R.color.color_Home));
            tvProtectionOrder.setTextColor(getResources().getColor(R.color.color_f));
        } else if (position == 1) {
            tvProtectionOrder.setBackgroundColor(getResources().getColor(R.color.color_f));
            tvProtectionOrder.setTextColor(getResources().getColor(R.color.color_Home));

            tvAllOrder.setBackgroundColor(getResources().getColor(R.color.color_Home));
            tvAllOrder.setTextColor(getResources().getColor(R.color.color_f));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changePosition = position;
        initTextView(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
