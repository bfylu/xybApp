package cn.payadd.majia.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.payadd.majia.pojo.DayProfitPojo;
import cn.payadd.majia.pojo.ProfitRecordPojo;
import cn.payadd.majia.util.DateUtils;

/**
 * Created by df on 2017/9/22.
 */

public class ProfitRecordAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ProfitRecordPojo> mData;

    public ProfitRecordAdapter(Context context) {
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
        Log.d("position", position + "");
        View v = mInflater.inflate(R.layout.item_income_day_recode, parent, false);
        TextView tvIncomeType = (TextView) v.findViewById(R.id.tv_income_type);
        TextView tvIncomeTime = (TextView) v.findViewById(R.id.tv_income_time);
        TextView tvIncomeAmount = (TextView) v.findViewById(R.id.tv_income_amount);
        ProfitRecordPojo recordPojo = mData.get(position);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        tvIncomeType.setText(recordPojo.getBizType());
        tvIncomeTime.setText(format.format(recordPojo.getRecordTime()));
        tvIncomeAmount.setText(recordPojo.getProfitAmount());
        return v;


    }

    public void updateData(List<ProfitRecordPojo> data, boolean append) {
        if (append) {
            mData.addAll(data);
        } else {
            mData.clear();
            mData.addAll(data);
        }

        notifyDataSetChanged();
    }
}
