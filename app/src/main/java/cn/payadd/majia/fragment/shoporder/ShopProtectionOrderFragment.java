package cn.payadd.majia.fragment.shoporder;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.MyViewPagerAdapter;
import com.fdsj.credittreasure.fragment.BaseFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.utils.Enums;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lang on 2018/5/17.
 */

public class ShopProtectionOrderFragment extends BaseFragment {

    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private List<String> titleList;
    private List<Fragment> fragmentList;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_shop_protection_order;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add(getActivity().getResources().getString(R.string.all));
        titleList.add(getActivity().getResources().getString(R.string.wait_buyer_deal));
        titleList.add(getActivity().getResources().getString(R.string.agree_refund));
        titleList.add(getActivity().getResources().getString(R.string.repeal_protection));

        fragmentList.add(ChildProtectionFragment.getChildProtectionFragment(Enums.shopOrder.维权全部));
        fragmentList.add(ChildProtectionFragment.getChildProtectionFragment(Enums.shopOrder.待买家处理));
        fragmentList.add(ChildProtectionFragment.getChildProtectionFragment(Enums.shopOrder.同意退款));
        fragmentList.add(ChildProtectionFragment.getChildProtectionFragment(Enums.shopOrder.维权撤销));

        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), viewPager, fragmentList);
        viewPager.setAdapter(myViewPagerAdapter);
        slidingTabLayout.setViewPager(viewPager, titleList);
    }
}
