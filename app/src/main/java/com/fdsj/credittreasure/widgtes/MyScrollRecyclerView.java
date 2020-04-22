package com.fdsj.credittreasure.widgtes;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.payadd.majia.view.ScrollLayout;

import static android.support.v7.appcompat.R.id.none;
import static com.jude.easyrecyclerview.R.id.insideOverlay;

/**
 * Created by 冷佳兴 on 2016/11/28-15:58.
 * 我是大傻逼，所在公司:博信诺达
 */

public class MyScrollRecyclerView extends EasyRecyclerView {

    Activity activity;
    RecyclerArrayAdapter adapter;

    private final MyScrollRecyclerView.CompositeScrollListener compositeScrollListener =
            new MyScrollRecyclerView.CompositeScrollListener();

    public MyScrollRecyclerView(Context context) {
        super(context);
        this.activity = (Activity) context;
        initView();
    }

    public MyScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (Activity) context;
        initView();
    }

    public MyScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
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

    {
        super.getRecyclerView().addOnScrollListener(compositeScrollListener);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                ViewParent parent = getParent();
                while (parent != null) {
                    if (parent instanceof ScrollLayout) {
                        int height = ((ScrollLayout) parent).getMeasuredHeight() - ((ScrollLayout) parent).minOffset;
                        if (layoutParams.height == height) {
                            return;
                        } else {
                            layoutParams.height = height;
                            break;
                        }
                    }
                    parent = parent.getParent();
                }
                setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        while (parent != null) {
            if (parent instanceof ScrollLayout) {
                ((ScrollLayout) parent).setAssociatedRecyclerView(this);
                break;
            }
            parent = parent.getParent();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Must be invoked from the main thread.");
        }
    }

    private class CompositeScrollListener extends RecyclerView.OnScrollListener {
        private final List<RecyclerView.OnScrollListener> scrollListenerList = new
                ArrayList<RecyclerView.OnScrollListener>();

        public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
            if (listener == null) {
                return;
            }
            for (RecyclerView.OnScrollListener scrollListener : scrollListenerList) {
                if (listener == scrollListener) {
                    return;
                }
            }
            scrollListenerList.add(listener);
        }

        public void removeOnScrollListener(AbsListView.OnScrollListener listener) {
            if (listener == null) {
                return;
            }
            Iterator<RecyclerView.OnScrollListener> iterator = scrollListenerList.iterator();
            while (iterator.hasNext()) {
                RecyclerView.OnScrollListener scrollListener = iterator.next();
                if (listener == scrollListener) {
                    iterator.remove();
                    return;
                }
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            List<RecyclerView.OnScrollListener> listeners = new ArrayList<RecyclerView.OnScrollListener>(scrollListenerList);
            for (RecyclerView.OnScrollListener listener : listeners) {
                listener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            List<RecyclerView.OnScrollListener> listeners = new ArrayList<RecyclerView.OnScrollListener>(scrollListenerList);
            for (RecyclerView.OnScrollListener listener : listeners) {
                listener.onScrolled(recyclerView, dx, dy);
            }
        }
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
