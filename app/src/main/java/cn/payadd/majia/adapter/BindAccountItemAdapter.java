package cn.payadd.majia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by df on 2017/6/20.
 */

public class BindAccountItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Map<String,String>> mData;

    public BindAccountItemAdapter(Context context,List<Map<String,String>> mData){
        super();
        this.mContext = context;
        if(mData!=null){
            this.mData = mData;
        }else{
            this.mData = new ArrayList<>();
        }

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
        View v = mInflater.inflate(R.layout.item_bind_acc, parent, false);
        TextView tvAlipayAcc = (TextView) v.findViewById(R.id.alipay_acc);
        Map<String, String> map = mData.get(position);
        tvAlipayAcc.setText(map.get("accountNo"));
        return v;
    }

    public List<Map<String, String>> getmData() {
        return mData;
    }

    public void setmData(List<Map<String, String>> mData) {
        this.mData = mData;
    }
}
