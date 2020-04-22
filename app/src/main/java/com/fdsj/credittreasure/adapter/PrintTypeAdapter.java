package com.fdsj.credittreasure.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

/**
 * Created by lang on 2018/5/3.
 */

public class PrintTypeAdapter extends BaseAdapter {
    private Context context;
    private String [] datas;
    private int choosePosition;
    private LayoutInflater inflater;

    public PrintTypeAdapter(Context context, String[] datas, int choosePosition) {
        this.context = context;
        this.datas = datas;
        this.choosePosition = choosePosition;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.length;
    }

    @Override
    public Object getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_print_type, null);
            holder = new ViewHolder();
            holder.tvPrintType = (TextView) convertView.findViewById(R.id.tv_print_type);
            holder.ivPrintType = (ImageView) convertView.findViewById(R.id.iv_print_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
         String printName = datas[position];

        holder.tvPrintType.setText(printName);
        if (choosePosition == position) {
            holder.ivPrintType.setImageResource(R.mipmap.close);
        } else {
            holder.ivPrintType.setImageResource(R.mipmap.open);
        }
        return convertView;
    }

    /**
     * 点击选择item改变状态
     * @param position
     */
    public void selectItem(int position) {
        choosePosition = position;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvPrintType;
        ImageView ivPrintType;
    }
}
