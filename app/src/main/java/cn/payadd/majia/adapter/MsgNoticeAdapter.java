package cn.payadd.majia.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by df on 2017/6/21.
 */

public class MsgNoticeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Map<String,String>> mData;
    private Map<Integer,QBadgeView> msgViewData;
    private int page = 1;

    private int totalPage;

    private int row = 10;

    private boolean isShowStatus, isLastPage;

    public MsgNoticeAdapter(Context context){
        super();
        this.mContext = context;

        this.mData = new ArrayList<>();

        this.mInflater = LayoutInflater.from(mContext);
        this.msgViewData = new HashMap<>();
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        Object item = null;
        try {
            item = mData.get(position);
        }catch (Exception e){
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_notice_msg, parent, false);
        LinearLayout llTitle = (LinearLayout) v.findViewById(R.id.ll_title);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) v.findViewById(R.id.tv_content);
        TextView tvMsgTime = (TextView) v.findViewById(R.id.tv_msg_time);
        Map<String, String> noticeMsg = mData.get(position);

        tvTitle.setText(noticeMsg.get("title"));
        tvContent.setText(noticeMsg.get("content"));
        tvMsgTime.setText(noticeMsg.get("createTime"));
        if(msgViewData != null){
            QBadgeView badgeView = msgViewData.get(position);
            if(badgeView != null){
                badgeView.bindTarget(llTitle);
            }
        }

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
        notifyData();
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

        notifyData();
    }

    public void clearData() {
        mData.clear();
        notifyData();
    }

    public void notifyData(){
        msgViewData.clear();
        for(int i = 0;i<mData.size();i++){
            Map<String,String> notifyMsg = mData.get(i);
            String isRead = notifyMsg.get("isRead");
            if("N".equals(isRead)){
                QBadgeView qBadgeView = new QBadgeView(mContext);
                qBadgeView.setBadgeGravity(Gravity.CENTER|Gravity.START);
                qBadgeView.setBadgeNumber(-1);
                qBadgeView.setBadgePadding(5, true);
                msgViewData.put(i,qBadgeView);
            }
        }
        Utilities.setIsmtNoticeCount(mContext,msgViewData.size());
        notifyDataSetChanged();
    }

    public Map<Integer, QBadgeView> getMsgViewData() {
        return msgViewData;
    }

    public void setMsgViewData(Map<Integer, QBadgeView> msgViewData) {
        this.msgViewData = msgViewData;
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
