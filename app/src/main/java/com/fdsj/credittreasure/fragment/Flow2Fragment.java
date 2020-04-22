package com.fdsj.credittreasure.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.fdsj.credittreasure.Interface.iActivities.IFlow;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.Flow2Activity;
import com.fdsj.credittreasure.adapter.Flow2Adapter;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.fdsj.credittreasure.entity.FlowBean;
import com.fdsj.credittreasure.presenter.FlowPresenter;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.utils.Enums;
import com.utils.StatusBarUtils;

import butterknife.BindView;
import cn.payadd.majia.activity.PaymentDetailActivity;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 流水fragment
 * <p>当前版本： V1.0.0
 */

public class Flow2Fragment extends BaseFragment implements IFlow, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.myRecyclerView)
    MyRecyclerView myRecyclerView;
    private Flow2Adapter flowAdapter;

    private int pageIndex = 1;
    private Enums.transType transType;
    private FlowPresenter presenter;
    private PayPushBroadcastReceiver receiver;
    private RefreshBroadcastReceiver refreshBroadcastReceiver;
    private Intent mIntent;
    private static boolean isScreen;

    public static Flow2Fragment getFlowFragment(Enums.transType transType, boolean isFiltrate) {
        Flow2Fragment flowFragment = new Flow2Fragment();
        flowFragment.transType = transType;
        isScreen = isFiltrate;
        return flowFragment;

    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_flow;
    }

    @Override
    protected void initView() {
        presenter = new FlowPresenter(this);
        flowAdapter = new Flow2Adapter(activity, isScreen);
        myRecyclerView.setLinearLayoutRecyclerView(flowAdapter, this, this);

        flowAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mIntent = new Intent(getActivity(), PaymentDetailActivity.class);
                mIntent.putExtra("orderNo", flowAdapter.getAllData().get(position).getOrderNo());
                startActivity(mIntent);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
        receiver = new PayPushBroadcastReceiver(new PayPushBroadcastReceiver.Callback() {
            @Override
            public void exec(Bundle data) {
                presenter.queryWater(Flow2Activity.condition, pageIndex, transType, activity);
            }
        });
        getContext().registerReceiver(receiver, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.fdsj.credittreasure.fragment.FlowFragment");
        refreshBroadcastReceiver = new RefreshBroadcastReceiver();
        getContext().registerReceiver(refreshBroadcastReceiver, intentFilter);
    }

    @Override
    protected void initData() {
        presenter.queryWater(Flow2Activity.condition, pageIndex, transType, activity);
    }

    @Override
    public void stopRecyclerView() {
        myRecyclerView.stopRecyclerView();
    }

    public class RefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (presenter != null) {
                presenter.queryWater(Flow2Activity.condition, pageIndex, transType, activity);
            }
        }
    }

    @Override
    public void getFlowBean(FlowBean flowBean, int status) {
        if (pageIndex == 1) {
            flowAdapter.clear();
            Flow2Activity flowAct = (Flow2Activity) getActivity();
            flowAct.initTotalView(flowBean.getTodayTotalAcquire(), flowBean.getTodayTransCount(), flowBean.getTotalRefund());
        }

        flowAdapter.addAll(flowBean.getList());
    }

    @Override
    public void onRefresh() {
        this.pageIndex = 1;
        presenter.queryWater(Flow2Activity.condition, pageIndex, transType, activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.color_Home));
    }

    @Override
    public void onLoadMore() {
        this.pageIndex++;
        presenter.queryWater(Flow2Activity.condition, pageIndex, transType, activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
        getContext().unregisterReceiver(refreshBroadcastReceiver);
    }
}
