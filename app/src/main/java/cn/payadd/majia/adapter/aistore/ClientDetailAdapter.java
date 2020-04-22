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

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.payadd.majia.entity.aistore.ClientDetailListBean;
import cn.payadd.majia.util.StringUtil;

public class ClientDetailAdapter extends RecyclerArrayAdapter<ClientDetailListBean.DataBean.ListBean> {

    private Context context;

    private String icon;

    private static final int NORMAL_ITEM = 0;

    private static final int GROUP_ITEM = 1;

    public ClientDetailAdapter(Context context, String icon) {
        super(context);
        this.context = context;
        this.icon = icon;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == GROUP_ITEM) {
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
            String currentDate = getAllData().get(position).getDate();
            int prevIndex = position - 1;
            boolean isDifferent = !StringUtil.equals(currentDate, getAllData().get(prevIndex).getDate());
            return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
        }
        return super.getViewType(position);
    }

    class GroupViewHolder extends BaseViewHolder<ClientDetailListBean.DataBean.ListBean> {

        TextView tvDate;//日期
        ImageView ivIcon;
        TextView tvContent;
        TextView tvStayTime;
        TextView tvBeforeTime;

        public GroupViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_client_detail);
            this.tvDate = $(R.id.tv_date);
            this.ivIcon = $(R.id.iv_icon);
            this.tvContent = $(R.id.tv_content);
            this.tvStayTime = $(R.id.tv_stay_time);
            this.tvBeforeTime = $(R.id.tv_before_time);
        }

        @Override
        public void setData(ClientDetailListBean.DataBean.ListBean data) {
            super.setData(data);
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(data.getDate());
            ImageLoader.getInstance().displayImage(icon, ivIcon);
            tvContent.setText(data.getContent());
            tvStayTime.setText(StringUtil.append(context.getResources().getString(R.string.view_count)
                    , StringUtil.toString(data.getActionCount())));
            tvBeforeTime.setText(getTime(data.getTimeStamp()));
        }
    }

    class MyViewHolder extends BaseViewHolder<ClientDetailListBean.DataBean.ListBean> {

        TextView tvDate;//日期
        ImageView ivIcon;
        TextView tvContent;
        TextView tvStayTime;
        TextView tvBeforeTime;

        public MyViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_client_detail);
            this.tvDate = $(R.id.tv_date);
            this.ivIcon = $(R.id.iv_icon);
            this.tvContent = $(R.id.tv_content);
            this.tvStayTime = $(R.id.tv_stay_time);
            this.tvBeforeTime = $(R.id.tv_before_time);
        }

        @Override
        public void setData(final ClientDetailListBean.DataBean.ListBean data) {
            super.setData(data);
            tvDate.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(icon, ivIcon);
            tvContent.setText(data.getContent());
            tvStayTime.setText(StringUtil.append(context.getResources().getString(R.string.view_count)
                    , StringUtil.toString(data.getActionCount())));
            tvBeforeTime.setText(getTime(data.getTimeStamp()));
        }
    }

    private String getTime (long timeStamp) {
        long nowTime = System.currentTimeMillis();

        if (timeStamp > nowTime || timeStamp == 0) {
            return "";
        }

        if ((nowTime - timeStamp) <= (3 * 60 * 1000)) {
            return context.getResources().getString(R.string.just_now);
        } else if ((nowTime - timeStamp) > (3 * 60 * 1000) && (nowTime - timeStamp) <= (60 * 60 * 1000)) {
            return StringUtil.append((nowTime - timeStamp) / (60 * 1000), context.getResources().getString(R.string.minutes_ago));
        } else {
            SimpleDateFormat format = new SimpleDateFormat ("HH:mm:ss");

            return format.format(new Date(timeStamp));
        }
    }
}