package cn.payadd.majia.adapter.aistore;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.util.StringUtil;

public class ActionRecordAdapter extends RecyclerArrayAdapter<ActionRecordBean.DataBean.ListBean> {
    private Context context;

    public ActionRecordAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    class MyHolder extends BaseViewHolder<ActionRecordBean.DataBean.ListBean> {

        TextView tvContent;
        ImageView ivIcon, ivGender;

        public MyHolder(ViewGroup parent) {
            super(parent, R.layout.item_action_record);
            this.tvContent = $(R.id.tv_content);
            this.ivIcon = $(R.id.iv_icon);
            this.ivGender = $(R.id.iv_gender);
        }

        @Override
        public void setData(final ActionRecordBean.DataBean.ListBean data) {
            ImageLoader.getInstance().displayImage(data.getUserJmgUrl(), ivIcon);
            if (1 == data.getSex()) {
                ivGender.setVisibility(View.VISIBLE);
                ivGender.setImageResource(R.mipmap.icon_male);
            } else if (2 == data.getSex()) {
                ivGender.setVisibility(View.VISIBLE);
                ivGender.setImageResource(R.mipmap.icon_female);
            } else {
                ivGender.setVisibility(View.GONE);
            }
            tvContent.setText(dealText(data.getContent()));
            super.setData(data);
        }
    }

    private SpannableString dealText(String str) {
        if (!str.contains("#sb#")) {
            return new SpannableString(str);
        }
        String[] strs = str.split("#sb#");
        str = str.replace("#sb#", "");
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_f6901b))
                , strs[0].length()
                , strs[0].length() + strs[1].length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
