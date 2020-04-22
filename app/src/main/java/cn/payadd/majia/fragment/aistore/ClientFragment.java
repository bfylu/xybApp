package cn.payadd.majia.fragment.aistore;

import android.support.v4.app.Fragment;
import android.view.View;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.MyViewPagerAdapter;
import com.fdsj.credittreasure.fragment.BaseFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.utils.Enums;

import java.util.ArrayList;

import butterknife.BindView;
import cn.payadd.majia.entity.ClientTabListEntities;
import cn.payadd.majia.view.CustomViewPager;

/**
 * Created by lang on 2018/6/15.
 */

public class ClientFragment extends BaseFragment {

    @BindView(R.id.commonTabLayout)
    CommonTabLayout commonTabLayout;
    @BindView(R.id.viewpager)
    CustomViewPager mCustomViewPager;

    private ArrayList<CustomTabEntity> titleList;

    private ArrayList<Fragment> fragmentList;

    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_client;
    }

    @Override
    protected void initView() {
        commonTabLayout.setVisibility(View.GONE);
        mCustomViewPager.setScanScroll(false);
    }

    @Override
    protected void initData() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        titleList.add(new ClientTabListEntities(getActivity().getResources().getString(R.string.client_type)
                , R.mipmap.button_golden
                , R.mipmap.button_white));
        titleList.add(new ClientTabListEntities(getActivity().getResources().getString(R.string.client_tag)
                , R.mipmap.button_golden
                , R.mipmap.button_white));

        fragmentList.add(ClientTypeFragment.getClientTypeFragment(Enums.clientType.客户标签));
        fragmentList.add(ClientTypeFragment.getClientTypeFragment(Enums.clientType.客户类型));

        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), mCustomViewPager, fragmentList);
        mCustomViewPager.setAdapter(myViewPagerAdapter);

        commonTabLayout.setTabData(titleList, getActivity(), R.id.viewpager, fragmentList);
    }
}
