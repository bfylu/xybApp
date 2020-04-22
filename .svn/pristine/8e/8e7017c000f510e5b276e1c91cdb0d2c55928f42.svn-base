package cn.payadd.majia.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.pojo.DayProfitPojo;
import cn.payadd.majia.pojo.ProfitRecordPojo;
import cn.payadd.majia.util.DateUtils;

/**
 * Created by df on 2017/9/21.
 */

public class ProfitDayRecordAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<DayProfitPojo> mData;
    private ExpandableListView mListView;
    private int page = 1;

    private int totalPage;

    private int row = 10;

    private boolean isShowStatus, isLastPage;
    private SimpleDateFormat format = new SimpleDateFormat(DateUtils.DEFAULT_DATETIME_FORMAT, Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT,Locale.getDefault());
    private Handler handler;
    public ProfitDayRecordAdapter(Context context,ExpandableListView listView){
        super();
        this.mContext = context;

        this.mData = new ArrayList<>();

        this.mInflater = LayoutInflater.from(mContext);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                notifyDataSetInvalidated();
                super.handleMessage(msg);
            }
        };
        this.mListView = listView;
    }
    public int getChildCount(){
        int count = 0;
        for(int i = 1 ; i<=getGroupCount();i++){
            count += getChildrenCount(i);
        }
        return count;
    }
    public void updateData(List<DayProfitPojo> data, boolean append){
        if(data.isEmpty()){
            return;
        }
        if (append) {
            DayProfitPojo appendPojo = data.get(0);
            DayProfitPojo pojo = mData.get(mData.size()-1);
            if(DateUtils.isSameDate(appendPojo.getDate(),pojo.getDate())){
                pojo.getRecordList().addAll(data.remove(0).getRecordList());
                mData.addAll(data);
            }else{
                mData.addAll(data);
            }
        }else {
            mData.clear();
            //判断是否有今日收益和昨日收益
            if(DateUtils.isSameDate(DateUtils.getTodayZeroTime(),data.get(0).getDate())){
                mData.add(0,data.get(0));
            }else {
                //创建今日的收益
                DayProfitPojo todayProfit = new DayProfitPojo();
                todayProfit.setDate(new Date());
                todayProfit.setDayTotalAmount("0.00");
                todayProfit.setRecordList(new ArrayList<ProfitRecordPojo>());
                mData.add(0,todayProfit);
            }
            if(DateUtils.isSameDate(DateUtils.getYesterdayZeroTime(),data.get(1).getDate())){
                mData.add(1,data.get(1));
            }else {
                //创建昨日收益
                DayProfitPojo yesterdayProfit = new DayProfitPojo();
                yesterdayProfit.setDate(DateUtils.getYesterdayZeroTime());
                yesterdayProfit.setDayTotalAmount("0.00");
                yesterdayProfit.setRecordList(new ArrayList<ProfitRecordPojo>());
                mData.add(1,yesterdayProfit);
            }

            mData.addAll(data);
        }
       notifyDataSetInvalidated();
        for (int i = 0;i<mData.size();i++){
            mListView.expandGroup(i);
        }
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isShowStatus() {
        return isShowStatus;
    }

    public void setShowStatus(boolean showStatus) {
        isShowStatus = showStatus;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public List<DayProfitPojo> getmData() {
        return mData;
    }

    public void setmData(List<DayProfitPojo> mData) {
        this.mData = mData;
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(groupPosition).getRecordList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getRecordList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup
            parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_income_recode, null);
        }

        DayProfitPojo dayProfitPojo = mData.get(groupPosition);
        convertView.setTag(R.layout.item_income_recode, groupPosition);
        convertView.setTag(R.layout.item_income_day_recode, -1);
        TextView labDate = (TextView) convertView.findViewById(R.id.lab_day_income);
        TextView tvDayIncome = (TextView) convertView.findViewById(R.id.tv_day_income);
        tvDayIncome.setText(SymbolConstant.RMB+dayProfitPojo.getDayTotalAmount());
        if(groupPosition == 0){
            //今日收益
              labDate.setText("今日收益");
        }else if(groupPosition == 1){
            labDate.setText("昨日收益");
        }else{
            labDate.setText(dateFormat.format(dayProfitPojo.getDate()));
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_income_day_recode, null);
        }
        convertView.setTag(R.layout.item_income_recode, groupPosition);
        convertView.setTag(R.layout.item_income_day_recode, childPosition);
        TextView tvIncomeType = (TextView) convertView.findViewById(R.id.tv_income_type);
        TextView tvIncomeTime = (TextView) convertView.findViewById(R.id.tv_income_time);
        TextView tvIncomeAmount = (TextView) convertView.findViewById(R.id.tv_income_amount);
        ProfitRecordPojo recordPojo = mData.get(groupPosition).getRecordList().get(childPosition);

        tvIncomeType.setText(recordPojo.getBizType());
        tvIncomeTime.setText(format.format(recordPojo.getRecordTime()));
        tvIncomeAmount.setText(SymbolConstant.RMB+recordPojo.getProfitAmount());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

//    @Override
//    public int getHeaderState(int groupPosition, int childPosition) {
//        final int childCount = getChildrenCount(groupPosition);
//        if (childPosition == childCount - 1) {
//            return PINNED_HEADER_PUSHED_UP;
//        } else if (childPosition == -1
//                && !listView.isGroupExpanded(groupPosition)) {
//            return PINNED_HEADER_GONE;
//        } else {
//            return PINNED_HEADER_VISIBLE;
//        }
//    }
//
//    @Override
//    public void configureHeader(View header, int groupPosition, int childPosition, int alpha) {
//        DayProfitPojo groupData =  this.mData.get(groupPosition);
//        ((TextView) header.findViewById(R.id.tv_day_income)).setText(groupData.getDayTotalAmount());
//    }
//
//    private SparseIntArray groupStatusMap = new SparseIntArray();
//
//    @Override
//    public void setGroupClickStatus(int groupPosition, int status) {
//        groupStatusMap.put(groupPosition, status);
//    }
//
//    @Override
//    public int getGroupClickStatus(int groupPosition) {
//        if (groupStatusMap.keyAt(groupPosition)>=0) {
//            return groupStatusMap.get(groupPosition);
//        } else {
//            return 0;
//        }
//    }
}
