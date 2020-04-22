package cn.payadd.majia.adapter.shoporder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.Interface.OnItemViewClickListener;
import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.entity.ShopOrderBean;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by lang on 2018/5/17.
 */

public class ChildAllAdapter extends RecyclerArrayAdapter<ShopOrderBean.DataBean.DataListBean> {

    private Context context;

    private OnItemViewClickListener mItemViewClickListener;

    private Gson gson;

    private Type type;

    public ChildAllAdapter(Context context) {
        super(context);
        this.context = context;
        if (gson == null) {
            gson = new Gson();
        }
        if (type == null) {
            type = new TypeToken<Map<String, Object>>() {}.getType();
        }
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    public void setOnItemViewClickListener(OnItemViewClickListener itemViewClickListener) {
        mItemViewClickListener = itemViewClickListener;
    }

    class MyHolder extends BaseViewHolder<ShopOrderBean.DataBean.DataListBean> {

        TextView tvPhone, tvOrderStatus, tvDate, tvOrderNo, tvGoodsContent, tvGoodsPrice
                , tvGoodsCount, tvTotalPrice, tvHaveFreight, tvRefundStatus, tvOrderType, tvGoodsNum;
        ImageView ivGoodsImg, ivOrderType;
        RelativeLayout rlOrderType;

        public MyHolder(ViewGroup parent) {
            super(parent, R.layout.item_child_all);
            this.tvPhone = $(R.id.tv_phone);
            this.tvOrderStatus = $(R.id.tv_order_status);
            this.tvDate = $(R.id.tv_date);
            this.tvOrderNo = $(R.id.tv_order_no);
            this.tvGoodsContent = $(R.id.tv_goods_content);
            this.tvGoodsPrice = $(R.id.tv_goods_price);
            this.tvGoodsCount = $(R.id.tv_goods_count);
            this.tvGoodsNum = $(R.id.tv_goods_num);
            this.tvTotalPrice = $(R.id.tv_total_price);
            this.tvHaveFreight = $(R.id.tv_have_freight);
            this.tvOrderType = $(R.id.tv_order_type);
            this.tvRefundStatus = $(R.id.tv_refund_status);
            this.ivGoodsImg = $(R.id.iv_goods_img);
            this.ivOrderType = $(R.id.iv_order_type);
            this.rlOrderType = $(R.id.rl_order_type);
        }

        @Override
        public void setData(final ShopOrderBean.DataBean.DataListBean data) {
            tvPhone.setText(data.getConsigneePhone());
            if (0 == data.getOrderStatus()) {
                tvOrderStatus.setText(context.getResources().getString(R.string.obligation));
                rlOrderType.setVisibility(View.VISIBLE);
                ivOrderType.setImageResource(R.mipmap.icon_close);
                tvOrderType.setText(context.getResources().getString(R.string.close));
                tvOrderType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemViewClickListener.onItemViewClick(getAdapterPosition(), data.getOrderStatus());
                    }
                });
            } else if (1 == data.getOrderStatus()) {
                tvOrderStatus.setText(context.getResources().getString(R.string.wait_send));
                rlOrderType.setVisibility(View.VISIBLE);
                ivOrderType.setImageResource(R.mipmap.icon_fahuo);
                tvOrderType.setText(context.getResources().getString(R.string.send_out));
                tvOrderType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemViewClickListener.onItemViewClick(getAdapterPosition(), data.getOrderStatus());
                    }
                });
            } else if (2 == data.getOrderStatus()) {
                rlOrderType.setVisibility(View.GONE);
                tvOrderStatus.setText(context.getResources().getString(R.string.is_send));
            } else if (3 == data.getOrderStatus()) {
                rlOrderType.setVisibility(View.GONE);
                tvOrderStatus.setText(context.getResources().getString(R.string.is_done));
            } else if (4 == data.getOrderStatus()) {
                rlOrderType.setVisibility(View.GONE);
                tvOrderStatus.setText(context.getResources().getString(R.string.is_closed));
            }
            tvDate.setText(data.getOrderTime());
            tvOrderNo.setText(data.getOrderNo());
            tvGoodsContent.setText(data.getGoodsName());
            tvGoodsPrice.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getGoodsPrice())));
            tvGoodsNum.setText(StringUtil.append("×", StringUtil.toString(data.getGoodsQuantity())));
            tvGoodsCount.setText(StringUtil.append("共", StringUtil.toString(data.getGoodsQuantity()), "件"));
            tvTotalPrice.setText(data.getOrderAmt());

            if (StringUtil.isNotEmpty(data.getGoodsPic())) {
                Map<String, String> map = gson.fromJson(data.getGoodsPic(), type);
                String pic = AppConfig.getImageInterface().replace("%", map.get("primary")).replace("$", StringUtil.toString(StringUtil.dp2px(context, 93)));
                ImageLoader.getInstance().displayImage(pic, ivGoodsImg);
            } else {
                ivGoodsImg.setImageResource(R.drawable.default_pic);
            }

            //判断是否有运费
            if (StringUtil.isEmpty(data.getGoodsFreight()) || 0 == StringUtil.toDouble(data.getGoodsFreight())) {
                tvHaveFreight.setText(context.getResources().getString(R.string.free_freight));
            } else {
                tvHaveFreight.setText(StringUtil.append("(运费：", data.getGoodsFreight(), "元)"));
            }
            super.setData(data);
        }
    }
}
