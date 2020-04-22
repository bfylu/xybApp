package cn.payadd.majia.fragment;

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
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.jude.rollviewpager.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.payadd.majia.activity.IncomeManagerActivity;
import cn.payadd.majia.adapter.ProfitRecordPojoAdapter;
import cn.payadd.majia.adapter.StickyHeaderAdapter;
import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.face.IRefreshFragment;
import cn.payadd.majia.pojo.DayProfitPojo;
import cn.payadd.majia.pojo.ProfitRecordPojo;
import cn.payadd.majia.presenter.ProfitPresenter;
import cn.payadd.majia.util.DateUtils;

/**
 * Created by df on 2017/10/6.
 */

public class Tab1Fragment extends Fragment implements IFragment, SwipeRefreshLayout
        .OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener, IRefreshFragment {
    public static final String TAG = Tab1Fragment.class.getSimpleName();
    public static final String GOOGLE_REFRESH_TYPE = "google_refresh_type";

    private int mType;
    private EasyRecyclerView recyclerView;
    private ProfitRecordPojoAdapter adapter;
    private StickyHeaderAdapter headerAdapter;
    private ProfitPresenter profitPresenter;
    private ImageView ivToTop;
    private boolean resetPage;

    private boolean hasMoreData;


    public Tab1Fragment () {
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
        recyclerView.setAdapter(adapter = new ProfitRecordPojoAdapter(getActivity()));
        DividerDecoration itemDecoration = new DividerDecoration(Color.parseColor("#f0f0f0"), Util.dip2px
                (getActivity(), 2f), 0, 0);
        itemDecoration.setDrawLastItem(false);
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(headerAdapter = new
                StickyHeaderAdapter(getActivity(),adapter));
        decoration.setIncludeHeader(false);
        recyclerView.addItemDecoration(decoration);
        adapter.setMore(R.layout.view_more, this);
        adapter.setNoMore(R.layout.view_no_data);
        profitPresenter = new ProfitPresenter(this, getActivity());
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
//        App.getRequestQueue().cancelAll(TAG + "refresh" + mType);
//        App.getRequestQueue().cancelAll(TAG + "loadmore" + mType);
//        if (swipeToLoadLayout.isRefreshing()) {
//            swipeToLoadLayout.setRefreshing(false);
//        }
//        if (swipeToLoadLayout.isLoadingMore()) {
//            swipeToLoadLayout.setLoadingMore(false);
//        }
//        mAdapter.stop();
    }

    @Override
    public void onRefresh() {
        resetPage = true;
        profitPresenter.queryProfitRecord(adapter, resetPage);
    }

    @Override
    public void updateModel(String key, Object data) {
        Map<String, String> respData = (Map<String, String>) data;
        try {
            if (key.equals("profitRecord")) {
                String respCode = respData.get("respCode");
                if (respCode.equals("000000")) {
                    ((IncomeManagerActivity) getActivity()).tvIncome.setText(respData.get
                            ("totalAmount"));
                    ((IncomeManagerActivity) getActivity()).tvCanWithdrawAmount.setText(respData
                            .get("availableAmount"));
                    ((IncomeManagerActivity) getActivity()).tvFreezeAmount.setText(respData.get
                            ("freezeAmount"));
                    ((IncomeManagerActivity) getActivity()).tvWithdrawAmount.setText(respData.get
                            ("expensesAmount"));
                    JSONArray incomeList = null;
                    incomeList = new JSONArray(respData.get("incomeList"));
                    JSONArray dayInfoList = new JSONArray(respData.get("dayInfoList"));
                    List<DayProfitPojo> dayProfitPojoList = parseDayProfit(dayInfoList);
                    List<ProfitRecordPojo> profitRecordPojos = parseProfitRecord(incomeList);
                    if (!profitRecordPojos.isEmpty()) {
                        boolean hasTodayData = false;
                        boolean hasYesterDayData = false;
                        if (resetPage) {
                            adapter.clear();
                            for (int i = 0; i < profitRecordPojos.size(); i++) {
                                Date date = profitRecordPojos.get(i).getRecordTime();
                                if (DateUtils.isSameDate(date, DateUtils.getTodayZeroTime())) {
                                    hasTodayData = true;
                                }
                                if (DateUtils.isSameDate(date, DateUtils.getYesterdayZeroTime())) {
                                    hasYesterDayData = true;
                                }
                            }
//                            ProfitRecordPojo todayTempPojo = null;
//                            if(!hasTodayData){
//                                todayTempPojo= new ProfitRecordPojo();
//                                todayTempPojo.setRecordTime(DateUtils.getTodayZeroTime());
//                                todayTempPojo.setBizType("1");
//                                todayTempPojo.setProfitAmount("0.00");
//                                profitRecordPojos.add(todayTempPojo);
//
//                                DayProfitPojo todayPojo = new DayProfitPojo();
//                                todayPojo.setDate(DateUtils.getTodayZeroTime());
//                                todayPojo.setDayTotalAmount("0.00");
//                                dayProfitPojoList.add(todayPojo);
//
//                            }
//                            ProfitRecordPojo yesterdayTempPojo = null;
//                            if(!hasYesterDayData){
//                                yesterdayTempPojo= new ProfitRecordPojo();
//                                yesterdayTempPojo.setBizType("1");
//                                yesterdayTempPojo.setRecordTime(DateUtils.getYesterdayZeroTime());
//                                yesterdayTempPojo.setProfitAmount("0.00");
//                                profitRecordPojos.add(yesterdayTempPojo);
//                                DayProfitPojo yesterdayPojo = new DayProfitPojo();
//                                yesterdayPojo.setDate(DateUtils.getYesterdayZeroTime());
//                                yesterdayPojo.setDayTotalAmount("0.00");
//                                dayProfitPojoList.add(yesterdayPojo);
//                            }

                            Collections.sort(profitRecordPojos);
                            Collections.sort(dayProfitPojoList);
                            adapter.addAll(profitRecordPojos);
                            adapter.notifyDataSetChanged();
//                            if (todayTempPojo != null) {
//                                List<ProfitRecordPojo> list = adapter.getAllData();
//                                int position = list.indexOf(todayTempPojo);
//                                adapter.notifyItemRemoved(position);
//                                if (position != list.size()) {
//                                    adapter.notifyItemRangeChanged(position, list.size() - position);
//                                }
//                            }
//                            if(yesterdayTempPojo != null){
//                                List<ProfitRecordPojo> list = adapter.getAllData();
//                                int position = list.indexOf(yesterdayTempPojo);
//                                adapter.notifyItemRemoved(position);
//                                if (position != list.size()) {
//                                    adapter.notifyItemRangeChanged(position, list.size() - position);
//                                }
//                            }
                        }else{
                            adapter.addAll(profitRecordPojos);
                            adapter.notifyDataSetChanged();
                        }
                        headerAdapter.update(dayProfitPojoList,resetPage);

                    } else {
                        //加入空集合，意为没有更多数据
                        adapter.addAll(new ArrayList<ProfitRecordPojo>());
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private List<ProfitRecordPojo> parseProfitRecord(JSONArray incomeList)throws
            JSONException, ParseException{
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(DateUtils.DEFAULT_DATETIME_FORMAT,
                Locale.getDefault());
        List<ProfitRecordPojo> recordPojos = new ArrayList<>();
        for (int j = 0; j < incomeList.length(); j++) {
            JSONObject profitRecordJson = incomeList.getJSONObject(j);
            Date date = datetimeFormat.parse(profitRecordJson.getString("recordTime"));
            ProfitRecordPojo profitRecordPojo = new ProfitRecordPojo();
            profitRecordPojo.setRecordTime(date);
            profitRecordPojo.setMerOrderType(profitRecordJson.getString("merOrderType"));
            profitRecordPojo.setBizType(profitRecordJson.getString("bizType"));
            profitRecordPojo.setProfitAmount(profitRecordJson.getString("profitAmount"));
            recordPojos.add(profitRecordPojo);
        }
        return recordPojos;
    }
    private List<DayProfitPojo> parseDayProfit(JSONArray dayInfoList)throws
            JSONException, ParseException{
        List<DayProfitPojo> dayProfitPojoList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT,Locale.getDefault());
        for (int i = 0; i < dayInfoList.length(); i++) {
            JSONObject dayInfoJson = dayInfoList.getJSONObject(i);
            DayProfitPojo dayProfitPojo = new DayProfitPojo();
            dayProfitPojo.setDate(dateFormat.parse(dayInfoJson.getString("date")));
            dayProfitPojo.setDayTotalAmount(dayInfoJson.getString("dayTotalAmount"));
            dayProfitPojoList.add(dayProfitPojo);
        }
        return dayProfitPojoList;
    }


    private List<DayProfitPojo> parseProfit(JSONArray dayInfoList, JSONArray incomeList) throws
            JSONException, ParseException {
        List<DayProfitPojo> dayProfitPojoList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT,Locale.getDefault());
        SimpleDateFormat datetimeFormat = new SimpleDateFormat(DateUtils.DEFAULT_DATETIME_FORMAT,Locale.getDefault());
        for (int i = 0; i < dayInfoList.length(); i++) {
            JSONObject dayInfoJson = dayInfoList.getJSONObject(i);
            DayProfitPojo dayProfitPojo = new DayProfitPojo();
            dayProfitPojo.setDate(dateFormat.parse(dayInfoJson.getString("date")));
            dayProfitPojo.setDayTotalAmount(dayInfoJson.getString("dayTotalAmount"));
            List<ProfitRecordPojo> recordPojos = new ArrayList<>();
            for (int j = 0; j < incomeList.length(); j++) {
                JSONObject profitRecordJson = incomeList.getJSONObject(j);
                Date date = datetimeFormat.parse(profitRecordJson.getString("recordTime"));
                boolean isSameDay = DateUtils.isSameDate(dayProfitPojo.getDate(), date);
                if (isSameDay) {
                    ProfitRecordPojo profitRecordPojo = new ProfitRecordPojo();
                    profitRecordPojo.setRecordTime(date);
                    profitRecordPojo.setBizType(profitRecordJson.getString("bizType"));
                    profitRecordPojo.setProfitAmount(profitRecordJson.getString("profitAmount"));
                    recordPojos.add(profitRecordPojo);
                }
            }
            dayProfitPojo.setRecordList(recordPojos);
            dayProfitPojoList.add(dayProfitPojo);
        }
        return dayProfitPojoList;
    }

    @Override
    public void refreshCallBack(boolean canRefresh) {
        recyclerView.getSwipeToRefresh().setEnabled(canRefresh);
    }

    @Override
    public void onLoadMore() {
        resetPage = false;
        profitPresenter.queryProfitRecord(adapter, resetPage);
    }
}
