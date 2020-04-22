package com.fdsj.credittreasure.activities;

import android.support.v4.widget.SwipeRefreshLayout;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.OperationInfoAdapter;
import com.fdsj.credittreasure.entity.Operation;
import com.fdsj.credittreasure.presenter.OperationInfoPresenter;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * <p>创 建 人： qinsiyi
 * //操作信息
 */

public class OperationInfoActivity extends BaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, IActivity {

    @BindView(R.id.myRecyclerView)
    MyRecyclerView myRecyclerView;


    private OperationInfoAdapter adapter;
    private OperationInfoPresenter presenter;
    private int pageIndex = 1;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_operation_info;
    }

    @Override
    protected void initView() {
        super.setBackOnclick();
        super.setCenterText( getResources().getString(R.string.operate_msg_title));
        adapter = new OperationInfoAdapter(this);
        presenter = new OperationInfoPresenter(this);
        myRecyclerView.setLinearLayoutRecyclerView(adapter, this, this);
    }

    @Override
    protected void initData() {
        presenter.queryOperating(pageIndex, this);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        presenter.queryOperating(pageIndex, this);
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        presenter.queryOperating(pageIndex, this);
    }

    @Override
    public void stopRecyclerView() {
        myRecyclerView.stopRecyclerView();
    }

    @Override
    public void UpdateModel(Object model, int status) {
        if (pageIndex == 1) {
            adapter.clear();
        }
        List<Operation.Bean> beanList = (List<Operation.Bean>) model;
        adapter.addAll(beanList);
    }
}
