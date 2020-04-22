package cn.payadd.majia.adapter.aistore;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;

import cn.payadd.majia.entity.ClientBean;
import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.CircleImageView;

public class ClientAdapter extends RecyclerArrayAdapter<ActionRecordBean.DataBean.ListBean> {

    private Context context;

    public ClientAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(parent);
    }

    class MyViewHolder extends BaseViewHolder<ActionRecordBean.DataBean.ListBean> {

        CircleImageView ivHead;//用户头像
        TextView tvName;//用户姓名
        ImageView ivVip;//用户是否是VIP
        TextView tvDate;//最后活跃时间
        ImageView ivGender;//用户性别
        TextView tvDistance;//用户距离

        MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_client);
            this.ivHead = $(R.id.iv_head);
            this.tvName = $(R.id.tv_name);
            this.ivVip = $(R.id.iv_vip);
            this.tvDate = $(R.id.tv_date);
            this.ivGender = $(R.id.iv_gender);
            this.tvDistance = $(R.id.tv_distance);
        }

        @Override
        public void setData(final ActionRecordBean.DataBean.ListBean data) {
            super.setData(data);
            ImageLoader.getInstance().displayImage(data.getUserJmgUrl(), ivHead);
            if (1 == data.getSex()) {
                ivGender.setVisibility(View.VISIBLE);
                ivGender.setImageResource(R.mipmap.icon_male);
            } else if (2 == data.getSex()) {
                ivGender.setVisibility(View.VISIBLE);
                ivGender.setImageResource(R.mipmap.icon_female);
            } else {
                ivGender.setVisibility(View.GONE);
            }
            tvName.setText(data.getNick());
            ivVip.setVisibility(View.GONE);
            tvDate.setText(StringUtil.append(context.getResources().getString(R.string.last_active_time), data.getActivityDate()));
            if (StringUtil.isNotEmpty(data.getDistance())) {
                tvDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(getDistance(data.getDistance()));
            } else {
                tvDistance.setVisibility(View.GONE);
            }
        }
    }

    private String getDistance(String distanceStr) {
        String str = "";
        float distance = StringUtil.toFloat(distanceStr);
        if (distance > 1000) {
            distance = distance / 1000;
            DecimalFormat df = new DecimalFormat("###############0.000");
            str = StringUtil.append(df.format(distance), "km");
        } else {
            DecimalFormat df = new DecimalFormat("###############0");
            str = StringUtil.append(df.format(distance), "m");
        }

        return str;
    }
}
