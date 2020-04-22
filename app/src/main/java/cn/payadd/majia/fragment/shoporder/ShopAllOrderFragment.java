package cn.payadd.majia.fragment.shoporder;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.MyViewPagerAdapter;
import com.fdsj.credittreasure.fragment.BaseFragment;
import com.flyco.tablayout.SlidingTabLayout;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.utils.Enums;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.payadd.majia.Interface.IShopOrder;
import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.ExpressCompanyBean;
import cn.payadd.majia.entity.MonthPaymentBean;
import cn.payadd.majia.entity.OrderNumBean;
import cn.payadd.majia.entity.ShopOrderBean;
import cn.payadd.majia.entity.ShopOrderCloseReasonBean;
import cn.payadd.majia.entity.ShopOrderDetailBean;
import cn.payadd.majia.presenter.ShopOrderPresenter;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by lang on 2018/5/17.
 */

public class ShopAllOrderFragment extends BaseFragment implements IShopOrder {

    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout slidingTabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private List<String> titleList;
    private List<Fragment> fragmentList;

    private MyViewPagerAdapter myViewPagerAdapter;

    private ShopOrderPresenter mPresenter;

    private String state;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_shop_all_order;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mPresenter = new ShopOrderPresenter(getActivity(), ShopAllOrderFragment.this);
        state = "0,1,2,3,4";
        mPresenter.queryOrderNum(state);
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add(getActivity().getResources().getString(R.string.all));
        titleList.add(getActivity().getResources().getString(R.string.obligation));
        titleList.add(getActivity().getResources().getString(R.string.wait_send));
        titleList.add(getActivity().getResources().getString(R.string.is_send));
        titleList.add(getActivity().getResources().getString(R.string.is_done));
        titleList.add(getActivity().getResources().getString(R.string.is_closed));

        fragmentList.add(ChildAllFragment.getChildAllFragment(Enums.shopOrder.全部));
        fragmentList.add(ChildAllFragment.getChildAllFragment(Enums.shopOrder.待付款));
        fragmentList.add(ChildWaitSendFragment.getChildWaitSendFragment(Enums.shopOrder.待发货));
        fragmentList.add(ChildSendOutFragment.getChildSendOutFragment(Enums.shopOrder.已发货));
        fragmentList.add(ChildAllFragment.getChildAllFragment(Enums.shopOrder.已完成));
        fragmentList.add(ChildCloseFragment.getChildCloseFragment(Enums.shopOrder.已关闭));

        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(), viewPager, fragmentList);
        viewPager.setAdapter(myViewPagerAdapter);

        slidingTabLayout.setViewPager(viewPager, titleList);
    }

    public void initTitleList(String waitPayCount, String WaitSendCount, String isSendCount, String isDoneCount, String isCloseCount) {
        if (titleList != null) {
            if (StringUtil.isNotEmpty(waitPayCount)) {
                titleList.set(1, StringUtil.append(getActivity().getResources().getString(R.string.obligation)
                        , stipulationInt(StringUtil.toLong(waitPayCount))));
            }

            if (StringUtil.isNotEmpty(WaitSendCount)) {
                titleList.set(2, StringUtil.append(getActivity().getResources().getString(R.string.wait_send)
                        , stipulationInt(StringUtil.toLong(WaitSendCount))));
            }

            if (StringUtil.isNotEmpty(isSendCount)) {
                titleList.set(3, StringUtil.append(getActivity().getResources().getString(R.string.is_send)
                        , stipulationInt(StringUtil.toLong(isSendCount))));
            }

            if (StringUtil.isNotEmpty(isDoneCount)) {
                titleList.set(4, StringUtil.append(getActivity().getResources().getString(R.string.is_done)
                        , stipulationInt(StringUtil.toLong(isDoneCount))));
            }

            if (StringUtil.isNotEmpty(isCloseCount)) {
                titleList.set(5, StringUtil.append(getActivity().getResources().getString(R.string.is_closed)
                        , stipulationInt(StringUtil.toLong(isCloseCount))));
            }
        }
        slidingTabLayout.setmTitles(titleList);
        slidingTabLayout.notifyDataSetChanged();
    }

    private String stipulationInt(long count) {
        if (count < 0) {
            return "";
        } else if (count >= 0 && count < 100) {
            return StringUtil.append("(", StringUtil.toString(count), ")");
        } else {
            return "(99+)";
        }
    }

    @Override
    public void stopRecyclerView() {

    }

    @Override
    public void getShopBean(ShopOrderBean bean, int status) {

    }

    @Override
    public void getShopProtectionBean(ShopOrderBean bean, int status) {

    }

    @Override
    public void closeOrder(BaseBean data) {

    }

    @Override
    public void getShopOrderCloseReason(ShopOrderCloseReasonBean data) {

    }

    @Override
    public void getExpressCompanyList(ExpressCompanyBean data) {

    }

    @Override
    public void deliverGoods(BaseBean data) {

    }

    @Override
    public void getShopOrderDetail(ShopOrderDetailBean bean) {

    }

    @Override
    public void getMonthPayment(MonthPaymentBean bean) {

    }

    @Override
    public void getOrderNum(OrderNumBean bean) {
        if (StringUtil.equals("000000", bean.getRespCode())) {
            initTitleList(bean.getData().getPendingPaymentCount()
                    , bean.getData().getPendingDeliveryCount()
                    , bean.getData().getDeliverGoodsCount()
                    , bean.getData().getCompleteCount()
                    , bean.getData().getCloseCount());
        } else {
            ToastUtils.showShortToast(getActivity(), bean.getRespDesc());
        }
    }
}
