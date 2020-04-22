package com.fdsj.credittreasure.activities;

import android.support.v4.widget.SwipeRefreshLayout;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.CollectionsInfoAdapter;
import com.fdsj.credittreasure.entity.Gathering;
import com.fdsj.credittreasure.presenter.CollectionsInfoPresenter;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * <p>创 建 人： qinsiyi
 * //收款信息
 */

public class CollectionsInfoActivity extends BaseActivity implements RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, IActivity {

    @BindView(R.id.myRecyclerView)
    MyRecyclerView myRecyclerView;


    private CollectionsInfoAdapter adapter;
    private CollectionsInfoPresenter presenter;
    private int pageIndex = 1;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_collectionsinfo;
    }

    @Override
    protected void initView() {
        super.setBackOnclick();
        super.setCenterText(getResources().getString(R.string.gather_info));
        adapter = new CollectionsInfoAdapter(this);
        presenter = new CollectionsInfoPresenter(this);
        myRecyclerView.setLinearLayoutRecyclerView(adapter, this, this);
    }

    @Override
    protected void initData() {
        presenter.queryGathering(pageIndex, this);
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        presenter.queryGathering(pageIndex, this);
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        presenter.queryGathering(pageIndex, this);
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
        List<Gathering.Bean> beanList = (List<Gathering.Bean>) model;
        adapter.addAll(beanList);
    }
}
