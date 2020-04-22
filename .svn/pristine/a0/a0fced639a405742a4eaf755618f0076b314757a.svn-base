package cn.payadd.majia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.constant.SymbolConstant;

/**
 * Created by df on 2017/6/21.
 */

public class PreOrderItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Map<String,String>> mData;
    private int page = 1;

    private int totalPage;

    private int row = 10;

    private boolean isShowStatus, isLastPage;
    public PreOrderItemAdapter(Context context){
        super();
        this.mContext = context;

        this.mData = new ArrayList<>();

        this.mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_pre_order, parent, false);
        TextView tvOrderNo = (TextView) v.findViewById(R.id.tv_order_no);
        TextView tvOrderAmount = (TextView) v.findViewById(R.id.tv_order_amount);
        TextView tvAccountNo = (TextView) v.findViewById(R.id.tv_pay_account);
        TextView tvOrderTime = (TextView) v.findViewById(R.id.tv_order_time);
        TextView tvOrderStatus = (TextView) v.findViewById(R.id.tv_order_status);

        Map<String, String> map = mData.get(position);
        tvOrderNo.setText(map.get("orderNo"));
        tvOrderAmount.setText(SymbolConstant.RMB + map.get("orderAmount"));
        tvAccountNo.setText(map.get("payerId"));
        tvOrderTime.setText(map.get("orderTime"));
        String status = map.get("orderStatus");
        switch (status){
            case "00":
                status = "未知";
                break;
            case "01":
                status = "待结算";
                break;
            case "02":
                status = "已结算";
                break;
            case "03":
                status = "处理中";
                break;
            case "04":
                status = "已关闭";
                break;
            default:
                break;
        }
        tvOrderStatus.setText(status);
        return v;
    }

    public void updateData(JSONArray jsonArray, boolean append) throws JSONException {
        if (!append) {
            mData.clear();
        }
        for (int i = 0, j = jsonArray.length(); i < j; i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            Iterator<String> itr = jsonObj.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                map.put(key, jsonObj.getString(key));
            }
            mData.add(map);
        }
        notifyDataSetChanged();
    }
    public void updateData(List<Map<String,String>> data, boolean append) throws JSONException {
        if (append) {
            mData.addAll(data);
        }else {
            mData.clear();
            mData.addAll(data);
        }
        if (getCount() == 0){
            totalPage = 1;
        }else{
           if(mData.size() % row != 0){
                totalPage = mData.size()/row +1;
           }else {
               totalPage = mData.size()/row;
           }
        }

        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
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

    public List<Map<String, String>> getmData() {
        return mData;
    }

    public void setmData(List<Map<String, String>> mData) {
        this.mData = mData;
    }
}
