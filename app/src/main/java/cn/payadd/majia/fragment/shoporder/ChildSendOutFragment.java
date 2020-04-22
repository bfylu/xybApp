package cn.payadd.majia.fragment.shoporder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.fragment.BaseFragment;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.utils.Enums;
import com.utils.StatusBarUtils;

import java.util.List;

import butterknife.BindView;
import cn.payadd.majia.Interface.IShopOrder;
import cn.payadd.majia.Interface.OnItemViewClickListener;
import cn.payadd.majia.activity.shoporder.ShipActivity;
import cn.payadd.majia.activity.shoporder.ShopOrderDetailActivity;
import cn.payadd.majia.adapter.shoporder.ChildAllAdapter;
import cn.payadd.majia.adapter.shoporder.CloseOrderDialogAdapter;
import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.ExpressCompanyBean;
import cn.payadd.majia.entity.MonthPaymentBean;
import cn.payadd.majia.entity.OrderNumBean;
import cn.payadd.majia.entity.ShopOrderBean;
import cn.payadd.majia.entity.ShopOrderCloseReasonBean;
import cn.payadd.majia.entity.ShopOrderDetailBean;
import cn.payadd.majia.presenter.ShopOrderPresenter;
import cn.payadd.majia.util.CenterDialog;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by lang on 2018/5/17.
 */

public class ChildSendOutFragment extends BaseFragment implements IShopOrder, RecyclerArrayAdapter.OnLoadMoreListener
        , SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener, OnItemViewClickListener {

    @BindView(R.id.myRecyclerView)
    MyRecyclerView mRecyclerView;

    private Enums.shopOrder transType;

    private int pageIndex = 1;//查询页码

    private int row = 10;//每次查询条数

    private int closePosition = -1;

    private ChildAllAdapter mChildAllAdapter;

    private CloseOrderDialogAdapter mDialogAdapter;

    private CenterDialog mCloseOrderDialog;

    private String mCloseReason, mChooseReason, mCloseCode;

    private Intent mIntent;

    private ShopOrderPresenter mPresenter;

    private RefreshBroadcastReceiver refreshBroadcastReceiver;

    private EditText etCloseReason;

    public static ChildSendOutFragment getChildSendOutFragment(Enums.shopOrder transType) {
        ChildSendOutFragment fragment = new ChildSendOutFragment();
        fragment.transType = transType;
        return fragment;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_flow;
    }

    @Override
    protected void initView() {
        mPresenter = new ShopOrderPresenter(getActivity(), ChildSendOutFragment.this);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        mChildAllAdapter = new ChildAllAdapter(activity);
        mRecyclerView.setLinearLayoutRecyclerView(mChildAllAdapter, this, this);
        mChildAllAdapter.setOnItemClickListener(this);

        mChildAllAdapter.setOnItemViewClickListener(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.payadd.majia.fragment.shoporder.ChildSendOutFragment");
        refreshBroadcastReceiver = new RefreshBroadcastReceiver();
        getContext().registerReceiver(refreshBroadcastReceiver, intentFilter);
    }

    @Override
    protected void initData() {
        mPresenter.queryShopOrder(transType.getString(), pageIndex, row);
    }

    @Override
    public void stopRecyclerView() {
        mRecyclerView.stopRecyclerView();
    }

    @Override
    public void getShopBean(ShopOrderBean bean, int status) {
        if (pageIndex == 1) {
            mChildAllAdapter.clear();
        }
        mChildAllAdapter.addAll(bean.getData().getDataList());
    }

    @Override
    public void getShopProtectionBean(ShopOrderBean bean, int status) {

    }

    @Override
    public void closeOrder(BaseBean data) {
        if (StringUtil.equals("000000", data.getRespCode())) {
            mChildAllAdapter.remove(closePosition);
            mChildAllAdapter.notifyDataSetChanged();
        }
        ToastUtils.showShortToast(getActivity(), data.getRespDesc());
    }

    @Override
    public void getShopOrderCloseReason(ShopOrderCloseReasonBean data) {
        if (StringUtil.equals("000000", data.getRespCode())) {
            showDialog(data.getData().getCauseList(), closePosition);
        } else {
            ToastUtils.showShortToast(getActivity(), data.getRespDesc());
        }
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

    }

    @Override
    public void onRefresh() {
        this.pageIndex = 1;
        mPresenter.queryShopOrder(transType.getString(), pageIndex, row);
    }

    @Override
    public void onLoadMore() {
        this.pageIndex++;
        mPresenter.queryShopOrder(transType.getString(), pageIndex, row);
    }

    @Override
    public void onItemClick(int position) {
        mIntent = new Intent(getActivity(), ShopOrderDetailActivity.class);
        mIntent.putExtra("orderNo", mChildAllAdapter.getItem(position).getOrderNo());
        startActivity(mIntent);
    }

    @Override
    public void onItemViewClick(int position, int status) {
        if (0 == status) {
            closePosition = position;
            mPresenter.queryCloseOrderReason();
        } else if (1 == status) {
            //TODO 跳转至发货页面
            mIntent = new Intent(getActivity(), ShipActivity.class);
            mIntent.putExtra("orderNo", mChildAllAdapter.getAllData().get(position).getOrderNo());
            startActivity(mIntent);
        }
    }

    private void showDialog(final List<ShopOrderCloseReasonBean.DataBean.CauseListBean> dataList, final int position) {
        mChooseReason = "";
        mCloseOrderDialog = new CenterDialog(getActivity(), R.layout.dialog_close_order, new int[] {R.id.cancel, R.id.confirm});
        mCloseOrderDialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.confirm:
                        if (!StringUtil.equals(mChooseReason, etCloseReason.getText().toString().trim())) {
                            mChooseReason = etCloseReason.getText().toString().trim();
                            mCloseCode = "";
                        }
                        if (StringUtil.isEmpty(mChooseReason)) {
                            ToastUtils.showShortToast(getActivity(), getResources().getString(R.string.choose_close_reason));
                        } else {
                            mCloseReason = mChooseReason;
                            mPresenter.closeOrder(mChildAllAdapter.getAllData().get(position).getOrderNo()
                                    , mCloseCode
                                    , mCloseReason
                                    , mChildAllAdapter.getAllData().get(position).getOrderStatus());
                            hideCloseOrderDialog();
                        }
                        break;
                    case R.id.cancel:
                        hideCloseOrderDialog();
                        break;
                }
            }
        });
        mCloseOrderDialog.show();
        mCloseOrderDialog.setCanceledOnTouchOutside(true);

        etCloseReason = (EditText) mCloseOrderDialog.findViewById(R.id.et_close_reason);
        ListView listView = (ListView) mCloseOrderDialog.findViewById(R.id.listView);
        mDialogAdapter = new CloseOrderDialogAdapter(getActivity(), dataList);
        listView.setAdapter(mDialogAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDialogAdapter.selectPosition(position);
                mChooseReason = dataList.get(position).getCauseContent();
                mCloseCode = dataList.get(position).getCauseCode();
                etCloseReason.setText(mChooseReason);
            }
        });
    }

    private void hideCloseOrderDialog() {
        if (mCloseOrderDialog != null) {
            mCloseOrderDialog.dismiss();
            mCloseOrderDialog = null;
        }
    }

    public class RefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPresenter != null && StringUtil.equals("2", transType.getString())) {
                onRefresh();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.color_Home));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(refreshBroadcastReceiver);
    }
}
