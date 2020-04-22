package cn.payadd.majia.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.jude.rollviewpager.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.payadd.majia.activity.IncomeManagerActivity;
import cn.payadd.majia.activity.WithdrawDetailActivity;
import cn.payadd.majia.adapter.WithdrawRecordAdapter;
import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.face.IRefreshFragment;
import cn.payadd.majia.pojo.WithdrawRecordPojo;
import cn.payadd.majia.presenter.ProfitPresenter;
import cn.payadd.majia.util.DateUtils;

/**
 * Created by df on 2017/10/6.
 */

public class Tab2Fragment extends Fragment implements IFragment, SwipeRefreshLayout
        .OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, IRefreshFragment {
    public static final String TAG = Tab1Fragment.class.getSimpleName();
    public static final String GOOGLE_REFRESH_TYPE = "google_refresh_type";

    private int mType;
    private EasyRecyclerView recyclerView;
    private WithdrawRecordAdapter adapter;
    private ProfitPresenter profitPresenter;
    private ImageView ivToTop;
    private boolean resetPage;
    List<WithdrawRecordPojo> allRecordPojo;

    public Tab2Fragment(){
        allRecordPojo = new ArrayList<>();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mType = getArguments().getInt(GOOGLE_REFRESH_TYPE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final int layoutId = R.layout.fragment_1;
        View v = inflater.inflate(layoutId, container, false);
        recyclerView = (EasyRecyclerView) v.findViewById(R.id.recyclerView);
        ivToTop = (ImageView) v.findViewById(R.id.iv_to_top);
        ivToTop.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setRefreshListener(this);
        recyclerView.setAdapter(adapter = new WithdrawRecordAdapter(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(Color.parseColor("#f0f0f0"), Util.dip2px
                (getActivity(), 2f), 0, 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_no_data);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), WithdrawDetailActivity.class);
                intent.putExtra("issueNo",allRecordPojo.get(position).getIssueNo());
                startActivity(intent);
            }
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    if(lastItemPosition >= 3*adapter.getRow()){
                        ivToTop.setVisibility(View.VISIBLE);
                    }else{
                        ivToTop.setVisibility(View.GONE);
                    }
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    System.out.println(lastItemPosition + "   " + firstItemPosition);
                }
            }
        });
        ivToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
                ivToTop.setVisibility(View.GONE);
            }
        });
        profitPresenter = new ProfitPresenter(this, getActivity());

//        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                //判断是当前layoutManager是否为LinearLayoutManager
//                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                if (layoutManager instanceof LinearLayoutManager) {
//                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                    //获取最后一个可见view的位置
//                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
//                    //获取第一个可见view的位置
//                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    System.out.println(lastItemPosition + "   " + firstItemPosition);
//                }
//            }
//        });
        onRefresh();


        return v;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRefresh() {
        resetPage = true;
        profitPresenter.queryWithdrawRecord(adapter, resetPage);
    }

    @Override
    public void updateModel(String key, Object data) {
        Map<String, String> respData = (Map<String, String>) data;
        try {
            if (key.equals("withdrawRecord")) {
                String respCode = respData.get("respCode");
                if (respCode.equals("000000")) {
                    ((IncomeManagerActivity)getActivity()).tvIncome.setText(respData.get("totalAmount"));
                    ((IncomeManagerActivity)getActivity()).tvCanWithdrawAmount.setText(respData.get("availableAmount"));
                    ((IncomeManagerActivity)getActivity()).tvFreezeAmount.setText(respData.get("freezeAmount"));
                    ((IncomeManagerActivity)getActivity()).tvWithdrawAmount.setText(respData.get("expensesAmount"));
                    JSONArray withdrawList = new JSONArray(respData.get("withdrawList"));
                    List<WithdrawRecordPojo> withdrawRecordPojos = parseWithdraw(withdrawList);

                    if (!withdrawRecordPojos.isEmpty()) {
                        if (resetPage) {
                            allRecordPojo.clear();
                            allRecordPojo.addAll(withdrawRecordPojos);
                            adapter.clear();
                            adapter.addAll(withdrawRecordPojos);
                        } else {
                            allRecordPojo.addAll(withdrawRecordPojos);
                            adapter.addAll(withdrawRecordPojos);
                        }
                    }else { //加入空集合，意为没有更多数据
                        adapter.addAll(new ArrayList<WithdrawRecordPojo>());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private List<WithdrawRecordPojo> parseWithdraw(JSONArray withdrawInfoList)throws JSONException, ParseException{
        List<WithdrawRecordPojo> withdrawRecordPojos = new ArrayList<>();
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(DateUtils.DEFAULT_DATETIME_FORMAT,
                Locale.getDefault());
        for (int i = 0;i<withdrawInfoList.length();i++){
            JSONObject withdrawInfoJson =  withdrawInfoList.getJSONObject(i);
            WithdrawRecordPojo withdrawRecordPojo = new WithdrawRecordPojo();
            withdrawRecordPojo.setIssueNo(withdrawInfoJson.getString("issueNo"));
            withdrawRecordPojo.setStatus(withdrawInfoJson.getString("status"));
            withdrawRecordPojo.setWithdrawAmount(withdrawInfoJson.getString("withdrawAmount"));
            withdrawRecordPojo.setWithdrawTime(datetimeFormat.parse(withdrawInfoJson.getString("withdrawTime")));
            withdrawRecordPojos.add(withdrawRecordPojo);
        }
        return withdrawRecordPojos;
    }

    @Override
    public void refreshCallBack(boolean canRefresh) {
        recyclerView.getSwipeToRefresh().setEnabled(canRefresh);
    }

    @Override
    public void onLoadMore() {
        resetPage = false;
        profitPresenter.queryWithdrawRecord(adapter, resetPage);
    }
}
