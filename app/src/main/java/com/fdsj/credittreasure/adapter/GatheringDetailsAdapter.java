package com.fdsj.credittreasure.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.entity.Gathering;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.utils.Enums;
import com.utils.Utilities;

/**
 * Created by BXND on 2017-01-13.
 * 收款信息
 */

public class GatheringDetailsAdapter extends RecyclerArrayAdapter<Gathering.Bean> {


    public GatheringDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    class MyViewHolder extends BaseViewHolder<Gathering.Bean> {
        private TextView tv_gather_time;
        private TextView tv_gather_type;
        private TextView tv_gather_money;

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_gather_detail);
            tv_gather_time = $(R.id.tv_gather_time);
            tv_gather_type = $(R.id.tv_gather_type);
            tv_gather_money = $(R.id.tv_gather_money);

        }

        @Override
        public void setData(Gathering.Bean data) {
            super.setData(data);
            tv_gather_time.setText(data.getAcquireTime());
            tv_gather_type.setText(Enums.payMethodName.getNameString(data.getAcquireMethod()));

//            if (data.getAcquireMethod().equals(Enums.payType.微信.getString())) {
//            } else if (data.getAcquireMethod().equals(Enums.payType.支付宝.getString())) {
//                tv_gather_type.setText(Enums.payType.支付宝.name());
//            } else {
//                tv_gather_type.setText(Enums.payType.银联.name());
//            }
            tv_gather_money.setText(Utilities.getDoubleTwo(data.getAcquireAmount()) + "");

        }
    }
}
