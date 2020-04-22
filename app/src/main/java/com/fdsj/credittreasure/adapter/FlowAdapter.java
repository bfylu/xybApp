package com.fdsj.credittreasure.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.constant.ProductType;
import com.fdsj.credittreasure.entity.FlowBean;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.utils.Enums;
import com.utils.Utilities;

import cn.payadd.majia.activity.PaymentDetailActivity;
import cn.payadd.majia.util.StringUtil;


/**
 * Created by 冷佳兴 on 2017/1/8-17:04.
 * 我是大傻逼，所在公司:博信诺达
 */

public class FlowAdapter extends RecyclerArrayAdapter<FlowBean.Bean> {

    Context context;

    public FlowAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    class MyViewHolder extends BaseViewHolder<FlowBean.Bean> {

        ImageView image_pay;//微信，支付宝图片
        TextView tv_pay;//支付方式
        TextView tv_flow_time;//交易时间
        //        ImageView image_into;
        TextView tv_flow_money;//交易金额
        TextView tv_flow_state;//交易状态
        RelativeLayout relative;
//        TextView tv_title_flow_state;

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_flow);
            this.image_pay = $(R.id.image_pay);
            this.tv_pay = $(R.id.tv_pay);
            this.tv_flow_time = $(R.id.tv_flow_time);
            this.tv_flow_money = $(R.id.tv_flow_money);
            this.tv_flow_state = $(R.id.tv_flow_state);
            this.relative = $(R.id.relative);
        }

        @Override
        public void setData(final FlowBean.Bean data) {
            super.setData(data);
            tv_pay.setText("产品类型：" + ProductType.getValue(data.getBizType()));
            tv_flow_time.setText(context.getResources().getString(R.string.deal_time) + Utilities.setFormatTime(data.getOrderTime(), "yyyyMMdd", "yyyy-MM-dd"));
            tv_flow_money.setText(context.getResources().getString(R.string.deal_money) + Utilities.getDoubleTwo(data.getOrderAmount()));

            if (StringUtil.isNotEmpty(data.getOrderType()) && StringUtil.isNotEmpty(data.getOrderStatus())) {
                tv_flow_state.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                , Enums.orderStatusName.getNameString(data.getOrderStatus())));
            }

            if (data.getPayType().equals(Enums.httpPayType.微信支付.getString())) {
                image_pay.setImageResource(R.mipmap.statistics_weixin);
            } else if (data.getPayType().equals(Enums.httpPayType.支付宝.getString())) {
                image_pay.setImageResource(R.mipmap.zhifubao);
            } else if (data.getPayType().equals(Enums.httpPayType.银联支付.getString())) {
                image_pay.setImageResource(R.mipmap.baidu);
            } else if (data.getPayType().equals(Enums.httpPayType.预授信.getString())) {
                image_pay.setImageResource(R.mipmap.baidu);
            }
            relative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PaymentDetailActivity.class);
                    intent.putExtra("orderNo", data.getOrderNo());
                    context.startActivity(intent);
                }
            });

        }
    }
}
