package cn.payadd.majia.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

import cn.payadd.majia.activity.FundAuthOrderActivity;
import cn.payadd.majia.activity.PreOrderDetailActivity;
import cn.payadd.majia.adapter.PreOrderItemAdapter;
import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.presenter.FundAuthOrderPresenter;

/**
 * Created by df on 2017/6/21.
 */

public class UnbalancedPreOrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, IFragment,View.OnClickListener{

    private ListView listView;

    private SwipeRefreshLayout refreshLayout;

    private int totalCount;// 数据总条数

    private FundAuthOrderPresenter fundAuthOrderPresenter;

    private TextView tvNotOrder;

    private View footView;

    private View noDataView;

    private int lastVisibleIndex;

    private PreOrderItemAdapter adapter;

    private ImageView ivToTop;

    private boolean append;

    private String mUsername;

    private String mStatus = "01";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_all_pre_order,
                container, false);

        listView = (ListView) layout.findViewById(R.id.lv_order_data);
        refreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_refresh_layout);
        footView = getActivity().getLayoutInflater().inflate(R.layout.view_more, null);
        noDataView = getActivity().getLayoutInflater().inflate(R.layout.view_no_data, null);
        tvNotOrder = (TextView) layout.findViewById(R.id.tv_not_order);
        fundAuthOrderPresenter = new FundAuthOrderPresenter(getActivity(), UnbalancedPreOrderFragment.this);
        ivToTop = (ImageView) layout.findViewById(R.id.iv_to_top);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PreOrderItemAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PreOrderDetailActivity.class);
                TextView tvOrderNo = (TextView) view.findViewById(R.id.tv_order_no);
                String orderNo = tvOrderNo.getText().toString();
                intent.putExtra("orderNo",orderNo);
                startActivity(intent);
            }
        });
        listView.setOnScrollListener(this);

        refreshLayout.setOnRefreshListener(this);
        ivToTop.setVisibility(View.INVISIBLE);
        ivToTop.setOnClickListener(this);
        mUsername = Utilities.getUsernameForLogin(getActivity());
        Bundle bundle = ((FundAuthOrderActivity)getActivity()).getCondition();
        if (bundle!=null){
            String orderNo = bundle.getString("orderNo");
            String startTime = bundle.getString("startTime");
            String endTime = bundle.getString("endTime");
            if(TextUtils.isEmpty(orderNo)){
                orderNo = null;
            }
            if(TextUtils.isEmpty(startTime)){
                startTime = null;
            }
            if(TextUtils.isEmpty(endTime)){
                endTime = null;
            }
            fundAuthOrderPresenter.queryOrderList(adapter, true,mUsername,mStatus,startTime,endTime,orderNo);
        }else {

            fundAuthOrderPresenter.queryOrderList(adapter, true,mUsername,mStatus);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        listView.removeFooterView(footView);
        listView.removeFooterView(noDataView);
        //刷新数据
        append = false;
        Bundle bundle = ((FundAuthOrderActivity)getActivity()).getCondition();
        if (bundle!=null){
            String orderNo = bundle.getString("orderNo");
            String startTime = bundle.getString("startTime");
            String endTime = bundle.getString("endTime");
            if(TextUtils.isEmpty(orderNo)){
                orderNo = null;
            }
            if(TextUtils.isEmpty(startTime)){
                startTime = null;
            }
            if(TextUtils.isEmpty(endTime)){
                endTime = null;
            }
            fundAuthOrderPresenter.queryOrderList(adapter, true,mUsername,mStatus,startTime,endTime,orderNo);
        }else {

            fundAuthOrderPresenter.queryOrderList(adapter, true,mUsername,mStatus);
        }
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_to_top:
                listView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

            boolean enable = false;
            if (null != listView && listView.getChildCount() > 0) {
                // check if the first item of the list is visible
                boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                // check if the top of the first item is visible
                boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                // enabling or disabling the refresh layout
                enable = firstItemVisible && topOfFirstItemVisible;
            }
            if (scrollState == SCROLL_STATE_IDLE && lastVisibleIndex == adapter.getCount()-1) {
                Log.e("Tag", "loadMore");
                loadMoreData();// 加载更多数据
            }
            refreshLayout.setEnabled(enable);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        Log.e("TAG", "firstVisibleItem = " + firstVisibleItem);
        Log.e("TAG", "visibleItemCount = " + visibleItemCount);
        Log.e("TAG", "totalItemCount = " + totalItemCount);
        //滑动到第3屛时才显示返回顶部图标
        if(firstVisibleItem >= 2*adapter.getRow()){
            ivToTop.setVisibility(View.VISIBLE);
        }else{
            ivToTop.setVisibility(View.INVISIBLE);
        }
//        // 当adapter中的所有条目数已经和要加载的数据总条数相等时，则移除底部的View
//        if (totalItemCount == totalCount + 1) {
//            // 移除底部的加载布局
//            listView.removeFooterView(footView);
//            //TODO 加上底部没有数据的布局
//        }
    }

    @Override
    public void updateModel(String key, Object data) {
        List<Map<String, String>> mapList = (List<Map<String, String>>) data;
        listView.removeFooterView(footView);
        listView.removeFooterView(noDataView);
        if(mapList.isEmpty()){
            listView.addFooterView(noDataView,null,false);
        }else{
            if(mapList.size() < adapter.getRow()){
                listView.addFooterView(noDataView,null,false);
            }
            try {
                adapter.updateData(mapList, append);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (adapter.getCount() == 0) {
            listView.removeFooterView(footView);
            listView.removeFooterView(noDataView);
            tvNotOrder.setVisibility(View.VISIBLE);
        } else {
            tvNotOrder.setVisibility(View.GONE);
        }
    }
    private void loadMoreData() {
        listView.addFooterView(footView,null,false);
        append = true;
        Bundle bundle = ((FundAuthOrderActivity)getActivity()).getCondition();
        if (bundle!=null){
            String orderNo = bundle.getString("orderNo");
            String startTime = bundle.getString("startTime");
            String endTime = bundle.getString("endTime");
            if(TextUtils.isEmpty(orderNo)){
                orderNo = null;
            }
            if(TextUtils.isEmpty(startTime)){
                startTime = null;
            }
            if(TextUtils.isEmpty(endTime)){
                endTime = null;
            }
            fundAuthOrderPresenter.queryOrderList(adapter, false,mUsername,mStatus,startTime,endTime,orderNo);
        }else {
            fundAuthOrderPresenter.queryOrderList(adapter, false,mUsername,mStatus);
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("Unbalanced", isVisibleToUser + "");
        if (isVisibleToUser) {

        }
    }
}
