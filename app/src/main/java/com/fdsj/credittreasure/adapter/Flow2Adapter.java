package com.fdsj.credittreasure.adapter;

import android.content.Context;
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

import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.util.StringUtil;


/**
 * Created by 冷佳兴 on 2017/1/8-17:04.
 * 我是大傻逼，所在公司:博信诺达
 */

public class Flow2Adapter extends RecyclerArrayAdapter<FlowBean.Bean> {

    Context context;
    private boolean isScreen;
    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    public Flow2Adapter(Context context, boolean isScreen) {
        super(context);
        this.context = context;
        this.isScreen = isScreen;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GROUP_ITEM && !isScreen) {
            return new GroupViewHolder(parent);
        } else if (viewType == NORMAL_ITEM) {
            return new MyViewHolder(parent);
        } else {
            return new MyViewHolder(parent);
        }
    }

    @Override
    public int getViewType(int position) {
        //第一个要显示时间
        if (position == 0) {
            return GROUP_ITEM;
        }

        if (position != getAllData().size()) {
            String currentDate = Utilities.setFormatTime(getAllData().get(position).getOrderTime(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
            int prevIndex = position - 1;
            boolean isDifferent = !Utilities.setFormatTime(getAllData().get(prevIndex).getOrderTime(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd").equals(currentDate);
            return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
        }
        return super.getViewType(position);
    }

    class GroupViewHolder extends BaseViewHolder<FlowBean.Bean> {

        RelativeLayout rlTodayTotal;
        TextView tvDate;//日期
        TextView tvTotalNum;//今日总笔数
        TextView tvTotalAmount;//今日总金额
        ImageView ivPayTypePic;//支付类型图片
        TextView tvPayStatus;//支付状态
        TextView tvPayAmount;//支付金额
        TextView tvTime;//时间
        TextView tvPayOrRefund;//支付或退款
        TextView tvPayType;//支付类型

        public GroupViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_flow_2);
            this.rlTodayTotal = $(R.id.rl_today_total);
            this.tvDate = $(R.id.tv_date);
            this.tvTotalNum = $(R.id.tv_total_num);
            this.tvTotalAmount = $(R.id.tv_total_amount);
            this.ivPayTypePic = $(R.id.iv_pay_type_pic);
            this.tvPayStatus = $(R.id.tv_pay_status);
            this.tvPayAmount = $(R.id.tv_pay_amount);
            this.tvTime = $(R.id.tv_time);
            this.tvPayOrRefund = $(R.id.tv_pay_or_refund);
            this.tvPayType = $(R.id.tv_pay_type);
        }

        @Override
        public void setData(FlowBean.Bean data) {
            super.setData(data);
            rlTodayTotal.setVisibility(View.VISIBLE);
            tvDate.setText(Utilities.setFormatTime(data.getOrderTime(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"));
            tvTotalNum.setText(data.getDateTransCount());
            tvTotalAmount.setText(data.getDateTotalAcquire());

            if (!isScreen) {
                tvTime.setText(Utilities.setFormatTime(data.getOrderTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm:ss"));
            } else {
                tvTime.setText(data.getOrderTime());
            }

            if (StringUtil.equals(data.getOrderStatus(), Enums.orderStatusName.成功.getString())) {
                if (data.getPayType().equals(Enums.httpPayType.微信支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_weixin_normal);
                    tvPayType.setText(context.getResources().getString(R.string.weixin_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.支付宝.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_zhifubao_normal);
                    tvPayType.setText(context.getResources().getString(R.string.ali_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.银联支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_normal);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.预授信.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_normal);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));
                }

                tvPayAmount.setTextColor(context.getResources().getColor(R.color.account_current_merchant));
                if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.付款.getString())) {
                    tvPayType.setVisibility(View.VISIBLE);
                    tvPayOrRefund.setVisibility(View.VISIBLE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.account_current_merchant));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getOrderAmount())));
                    tvPayOrRefund.setText(ProductType.getValue(data.getBizType()));

                } else if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.退款.getString())) {
                    tvPayType.setVisibility(View.GONE);
                    tvPayOrRefund.setVisibility(View.GONE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.red_ff554c));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, "-", StringUtil.toString(data.getOrderAmount())));

                }

            } else if (StringUtil.equals(data.getOrderStatus(), Enums.orderStatusName.处理中.getString())) {
                if (data.getPayType().equals(Enums.httpPayType.微信支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_weixin_gray);
                    tvPayType.setText(context.getResources().getString(R.string.weixin_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.支付宝.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_zhifubao_gray);
                    tvPayType.setText(context.getResources().getString(R.string.ali_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.银联支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.预授信.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));
                }

                tvPayAmount.setTextColor(context.getResources().getColor(R.color.color_9));
                if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.付款.getString())) {
                    tvPayType.setVisibility(View.VISIBLE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayOrRefund.setText(ProductType.getValue(data.getBizType()));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getOrderAmount())));

                } else if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.退款.getString())) {
                    tvPayType.setVisibility(View.GONE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayOrRefund.setText(StringUtil.append("-", StringUtil.toString(data.getOrderAmount())));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, "-", StringUtil.toString(data.getOrderAmount())));

                }

            } else if (StringUtil.equals(data.getOrderStatus(), Enums.orderStatusName.失败.getString())) {
                if (data.getPayType().equals(Enums.httpPayType.微信支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_weixin_gray);
                    tvPayType.setText(context.getResources().getString(R.string.weixin_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.支付宝.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_zhifubao_gray);
                    tvPayType.setText(context.getResources().getString(R.string.ali_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.银联支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.预授信.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));
                }

                tvPayAmount.setTextColor(context.getResources().getColor(R.color.color_9));
                if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.付款.getString())) {
                    tvPayType.setVisibility(View.VISIBLE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
                    tvPayStatus.setText(context.getResources().getString(R.string.unpay));
                    tvPayOrRefund.setText(ProductType.getValue(data.getBizType()));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getOrderAmount())));

                } else if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.退款.getString())) {
                    tvPayType.setVisibility(View.GONE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayOrRefund.setText(StringUtil.append("-", StringUtil.toString(data.getOrderAmount())));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, "-", StringUtil.toString(data.getOrderAmount())));

                }
            }
        }
    }

    class MyViewHolder extends BaseViewHolder<FlowBean.Bean> {

        RelativeLayout rlTodayTotal;
        TextView tvDate;//日期
        TextView tv_TotalNum;//今日总笔数
        TextView tvTotalAmount;//今日总金额
        ImageView ivPayTypePic;//支付类型图片
        TextView tvPayStatus;//支付状态
        TextView tvPayAmount;//支付金额
        TextView tvTime;//时间
        TextView tvPayOrRefund;//支付或退款
        TextView tvPayType;//支付类型

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_flow_2);
            this.rlTodayTotal = $(R.id.rl_today_total);
            this.tvDate = $(R.id.tv_date);
            this.tv_TotalNum = $(R.id.tv_total_num);
            this.tvTotalAmount = $(R.id.tv_total_amount);
            this.ivPayTypePic = $(R.id.iv_pay_type_pic);
            this.tvPayStatus = $(R.id.tv_pay_status);
            this.tvPayAmount = $(R.id.tv_pay_amount);
            this.tvTime = $(R.id.tv_time);
            this.tvPayOrRefund = $(R.id.tv_pay_or_refund);
            this.tvPayType = $(R.id.tv_pay_type);
        }

        @Override
        public void setData(final FlowBean.Bean data) {
            super.setData(data);
            rlTodayTotal.setVisibility(View.GONE);

            if (!isScreen) {
                tvTime.setText(Utilities.setFormatTime(data.getOrderTime(), "yyyy-MM-dd HH:mm:ss", "HH:mm:ss"));
            } else {
                tvTime.setText(data.getOrderTime());
            }

            if (StringUtil.equals(data.getOrderStatus(), Enums.orderStatusName.成功.getString())) {
                if (data.getPayType().equals(Enums.httpPayType.微信支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_weixin_normal);
                    tvPayType.setText(context.getResources().getString(R.string.weixin_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.支付宝.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_zhifubao_normal);
                    tvPayType.setText(context.getResources().getString(R.string.ali_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.银联支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_normal);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.预授信.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_normal);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));
                }

                tvPayAmount.setTextColor(context.getResources().getColor(R.color.account_current_merchant));
                if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.付款.getString())) {
                    tvPayType.setVisibility(View.VISIBLE);
                    tvPayOrRefund.setVisibility(View.VISIBLE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.account_current_merchant));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayOrRefund.setText(ProductType.getValue(data.getBizType()));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getOrderAmount())));

                } else if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.退款.getString())) {
                    tvPayType.setVisibility(View.GONE);
                    tvPayOrRefund.setVisibility(View.GONE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.red_ff554c));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, "-", StringUtil.toString(data.getOrderAmount())));

                }

            } else if (StringUtil.equals(data.getOrderStatus(), Enums.orderStatusName.处理中.getString())) {
                if (data.getPayType().equals(Enums.httpPayType.微信支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_weixin_gray);
                    tvPayType.setText(context.getResources().getString(R.string.weixin_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.支付宝.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_zhifubao_gray);
                    tvPayType.setText(context.getResources().getString(R.string.ali_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.银联支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.预授信.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));
                }

                tvPayAmount.setTextColor(context.getResources().getColor(R.color.color_9));
                if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.付款.getString())) {
                    tvPayType.setVisibility(View.VISIBLE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayOrRefund.setText(ProductType.getValue(data.getBizType()));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getOrderAmount())));

                } else if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.退款.getString())) {
                    tvPayType.setVisibility(View.GONE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayOrRefund.setText(StringUtil.append("-", StringUtil.toString(data.getOrderAmount())));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, "-", StringUtil.toString(data.getOrderAmount())));

                }

            } else if (StringUtil.equals(data.getOrderStatus(), Enums.orderStatusName.失败.getString())) {
                if (data.getPayType().equals(Enums.httpPayType.微信支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_weixin_gray);
                    tvPayType.setText(context.getResources().getString(R.string.weixin_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.支付宝.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_zhifubao_gray);
                    tvPayType.setText(context.getResources().getString(R.string.ali_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.银联支付.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));

                } else if (data.getPayType().equals(Enums.httpPayType.预授信.getString())) {
                    ivPayTypePic.setImageResource(R.mipmap.icon_yinlian_gray);
                    tvPayType.setText(context.getResources().getString(R.string.card_pay));
                }

                tvPayAmount.setTextColor(context.getResources().getColor(R.color.color_9));
                if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.付款.getString())) {
                    tvPayType.setVisibility(View.VISIBLE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
//                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
//                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayStatus.setText(context.getResources().getString(R.string.unpay));
                    tvPayOrRefund.setText(ProductType.getValue(data.getBizType()));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getOrderAmount())));

                } else if (StringUtil.equals(data.getOrderType(), Enums.orderTypeName.退款.getString())) {
                    tvPayType.setVisibility(View.GONE);
                    tvPayStatus.setTextColor(context.getResources().getColor(R.color.color_9));
                    tvPayStatus.setText(StringUtil.append(Enums.orderTypeName.getNameString(data.getOrderType())
                            , Enums.orderStatusName.getNameString(data.getOrderStatus())));
                    tvPayOrRefund.setText(StringUtil.append("-", StringUtil.toString(data.getOrderAmount())));
                    tvPayAmount.setText(StringUtil.append(SymbolConstant.RMB, "-", StringUtil.toString(data.getOrderAmount())));

                }
            }
        }

    }
}
