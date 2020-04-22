package com.fdsj.credittreasure.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.entity.Gathering;
import com.fdsj.credittreasure.entity.Operation;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.utils.Enums;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by BXND on 2017-01-16.
 * 操作信息
 */

public class OperationInfoAdapter extends RecyclerArrayAdapter<Operation.Bean> {

    private String mStyle = "yyy-MM-dd HH:mm:ss";//时间格式
    private SimpleDateFormat mFormat;

    public OperationInfoAdapter(Context context) {
        super(context);
        mFormat = new SimpleDateFormat(mStyle, Locale.getDefault());
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    class MyViewHolder extends BaseViewHolder<Operation.Bean> {
        private TextView tv_operation_time;//时间，包含十分秒
        private TextView tv_operation_thing;//事件
        private TextView tv_operation_man;//操作员

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_operation_info);
            tv_operation_time = $(R.id.tv_operation_time);
            tv_operation_thing = $(R.id.tv_operation_thing);
            tv_operation_man = $(R.id.tv_operation_man);

        }

        @Override
        public void setData(Operation.Bean data) {
            super.setData(data);
            Date date = new Date(data.getEventTime());
            tv_operation_time.setText(mFormat.format(date));
            tv_operation_thing.setText(data.getEventLog());
            tv_operation_man.setText(data.getUsername());
        }
    }
}
