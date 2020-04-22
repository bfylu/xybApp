package com.fdsj.credittreasure.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.fdsj.credittreasure.Interface.iActivities.IFlow;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.FlowActivity;
import com.fdsj.credittreasure.adapter.FlowAdapter;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.fdsj.credittreasure.entity.FlowBean;
import com.fdsj.credittreasure.presenter.FlowPresenter;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.utils.Enums;
import com.utils.StatusBarUtils;

import butterknife.BindView;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 流水fragment
 * <p>当前版本： V1.0.0
 */

public class FlowFragment extends BaseFragment implements IFlow, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.myRecyclerView)
    MyRecyclerView myRecyclerView;

    private FlowAdapter flowAdapter;
    private int pageIndex = 1;
    private Enums.transType transType;
    private FlowPresenter presenter;
    private PayPushBroadcastReceiver receiver;
    private RefreshBroadcastReceiver refreshBroadcastReceiver;

    public static FlowFragment getFlowFragment(Enums.transType transType) {
        FlowFragment flowFragment = new FlowFragment();
        flowFragment.transType = transType;
        return flowFragment;

    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_flow;
    }

    @Override
    protected void initView() {
        presenter = new FlowPresenter(this);
        flowAdapter = new FlowAdapter(activity);
        myRecyclerView.setLinearLayoutRecyclerView(flowAdapter, this, this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
        receiver = new PayPushBroadcastReceiver(new PayPushBroadcastReceiver.Callback() {
            @Override
            public void exec(Bundle data) {
                presenter.queryWater(FlowActivity.condition, pageIndex, transType, activity);
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
        presenter.queryWater(FlowActivity.condition, pageIndex, transType, activity);
    }

//    private void initData(final String condition) {
//        pageIndex = 1;
//        rootView.post(new Runnable() {
//            @Override
//            public void run() {
//                myRecyclerView.setRefreshing(true);
//                presenter.queryWater(condition, pageIndex, transType, activity);
//            }
//        });
//    }

    @Override
    public void stopRecyclerView() {
        myRecyclerView.stopRecyclerView();
    }

//    @Override
//    public void UpdateModel(Object model, int status) {
//        flowAdapter.clear();
//        Map<String, String> stringMap = (Map<String, String>) model;
//        String result = "";
//        if (stringMap.get("orderStatus").equals(Enums.orderStatusName.成功.getString())) {
//            result = Enums.transType.已收款.getString();
//        } else if (stringMap.get("orderStatus").equals(Enums.orderStatusName.处理中.getString())) {
//            result = Enums.transType.待收款.getString();
//        } else {
//            result = Enums.transType.待收款.getString();
//        }
//        if (result.equals(transType.getString())) {
//            FlowBean.Bean bean = new FlowBean.Bean();
//            bean.setPayType();
//            bean.setOrderAmount();
//            bean.setOrderAmount();
//            flowAdapter.add(bean);
//        } else {
//            stopRecyclerView();
//        }
//    }

    public class RefreshBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (presenter != null) {
                presenter.queryWater(FlowActivity.condition, pageIndex, transType, activity);
            }
        }
    }

    @Override
    public void getFlowBean(FlowBean flowBean, int status) {
        if (pageIndex == 1) {
            flowAdapter.clear();
        }

        FlowActivity flowAct = (FlowActivity) getActivity();
        flowAct.initTotalView(flowBean.getTotalAcquire(), flowBean.getTransCount(), flowBean.getTotalRefund());
        flowAdapter.addAll(flowBean.getList());
    }

    @Override
    public void onRefresh() {
        this.pageIndex = 1;
        presenter.queryWater(FlowActivity.condition, pageIndex, transType, activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.color_Home));
//        ((MainActivity)getActivity()).setTitleVisible(true);
    }

    @Override
    public void onLoadMore() {
        this.pageIndex++;
        presenter.queryWater(FlowActivity.condition, pageIndex, transType, activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
        getContext().unregisterReceiver(refreshBroadcastReceiver);
    }
}
