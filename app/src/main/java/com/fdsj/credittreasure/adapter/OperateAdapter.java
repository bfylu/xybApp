package com.fdsj.credittreasure.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.entity.Gathering;
import com.fdsj.credittreasure.entity.Statistic;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.utils.Enums;
import com.utils.Utilities;

import java.util.List;

/**
 * Created by 冷佳兴
 * 我是大傻逼，所在公司:博信诺达
 * 作者：chery on 2017/1/23 - 18:06
 * 包名：com.fdsj.credittreasure.adapter
 */
@Deprecated
public class OperateAdapter extends RecyclerView.Adapter<OperateAdapter.MyViewHolder> {

    Context context;
    List<Statistic.StatisticBean.Bean> stringList;
    private LayoutInflater inflater;


    public OperateAdapter(Context context, List<Statistic.StatisticBean.Bean> strings) {
        this.context = context;
        this.stringList = strings;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_text, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);

        }
    }
}
