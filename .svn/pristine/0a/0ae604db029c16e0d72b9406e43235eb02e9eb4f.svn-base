package com.fdsj.credittreasure.widgtes;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import static android.support.v7.appcompat.R.id.none;
import static com.jude.easyrecyclerview.R.id.insideOverlay;

/**
 * Created by 冷佳兴 on 2016/11/28-15:58.
 * 我是大傻逼，所在公司:博信诺达
 */

public class MyRecyclerView extends EasyRecyclerView {

    Activity activity;
    RecyclerArrayAdapter adapter;

    public MyRecyclerView(Context context) {
        super(context);
        this.activity = (Activity) context;
        initView();
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (Activity) context;
        initView();
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.activity = (Activity) context;
        initView();
    }

    public void initView() {
        this.setEmptyView(R.layout.view_empty);
        this.setProgressView(R.layout.view_progress);
        this.setScrollBarStyle(insideOverlay);
        this.setScrollBarStyle(none);
    }

    public void setLinearLayoutRecyclerView(RecyclerArrayAdapter adapter, final RecyclerArrayAdapter.OnLoadMoreListener listener, SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.adapter = adapter;
        this.setItemAnimator(new DefaultItemAnimator());
        this.getSwipeToRefresh().setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);
        this.setLayoutManager(new LinearLayoutManager(activity));
        this.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.view_more, listener);
        this.setRefreshListener(onRefreshListener);
    }

    public void setLinearLayoutRecyclerView(RecyclerArrayAdapter adapter, SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.adapter = adapter;
        this.setItemAnimator(new DefaultItemAnimator());
        this.getSwipeToRefresh().setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);
        this.setLayoutManager(new LinearLayoutManager(activity));
        this.setAdapterWithProgress(adapter);
        this.setRefreshListener(onRefreshListener);
    }

    public void setLinearLayoutRecyclerView(RecyclerArrayAdapter adapter) {
        this.adapter = adapter;
        this.setItemAnimator(new DefaultItemAnimator());
        this.setLayoutManager(new LinearLayoutManager(activity));
        this.setAdapter(adapter);
    }

    public void setGridLayoutRecyclerView(int size, RecyclerArrayAdapter adapter, final RecyclerArrayAdapter.OnLoadMoreListener listener, SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.adapter = adapter;
        this.setItemAnimator(new DefaultItemAnimator());
        this.getSwipeToRefresh().setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);
        this.setLayoutManager(new GridLayoutManager(activity, size));
        this.setAdapterWithProgress(adapter);
        adapter.setMore(R.layout.view_more, listener);
        this.setRefreshListener(onRefreshListener);
    }


    /**
     * 暂无数据
     */
    public void stopRecyclerView() {
        adapter.pauseMore();
        this.setRefreshing(false);
        if (adapter.getAllData() == null || adapter.getAllData().size() <= 0) {
            showEmpty();
        }
    }

}
