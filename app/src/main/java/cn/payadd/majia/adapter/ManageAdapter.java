package cn.payadd.majia.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.view.BadgeView;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by df on 2017/9/11.
 */

public class ManageAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private View mView;
    private List<Map<String,Object>> mData;
    private Map<Integer,QBadgeView> msgViewData;

    private int tabBackgroundColor;
    private int textColor;

    private int textSize;

    public ManageAdapter(Context context,List<Map<String,Object>> mData,View view){
        super();
        this.mContext = context;
        if(mData!=null){
            this.mData = mData;
        }else{
            this.mData = new ArrayList<>();
        }

        this.mInflater = LayoutInflater.from(mContext);
        this.mView = view;
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
        View v = mInflater.inflate(R.layout.widget_tab, parent, false);
        if(tabBackgroundColor != 0){
            v.setBackgroundColor(ContextCompat.getColor(mContext, tabBackgroundColor));
        }

        ImageView imageView = (ImageView) v.findViewById(R.id.iv_image);
        imageView.setImageResource((int)mData.get(position).get("image"));
        TextView textView = (TextView) v.findViewById(R.id.tv_text);
        if(textColor != 0){
            textView.setTextColor(ContextCompat.getColor(mContext, textColor));
        }
        if(textSize!= 0){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        }

        textView.setText((String)mData.get(position).get("text"));
        LinearLayout llTab = (LinearLayout) v.findViewById(R.id.ll_tab);
        if(msgViewData != null){
            QBadgeView badgeView = msgViewData.get(position);
            if(badgeView != null){
                badgeView.bindTarget(llTab);
            }
        }
        return v;
    }
    public void updateData(List<Map<String,Object>> data,boolean append){
        if (append) {
            mData.addAll(data);
        }else {
            mData.clear();
            mData.addAll(data);
        }
        if(mData.isEmpty()){
            mView.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }
    public Map<Integer, QBadgeView> getMsgViewData() {
        return msgViewData;
    }

    public void setMsgViewData(Map<Integer, QBadgeView> msgViewData) {
        this.msgViewData = msgViewData;
    }

    public int getTabBackgroundColor() {
        return tabBackgroundColor;
    }

    public void setTabBackgroundColor(int tabBackgroundColor) {
        this.tabBackgroundColor = tabBackgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
