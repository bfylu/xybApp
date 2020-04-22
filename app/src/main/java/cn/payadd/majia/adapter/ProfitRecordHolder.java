package cn.payadd.majia.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.payadd.majia.constant.BizTypeConstant;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.pojo.ProfitRecordPojo;
import cn.payadd.majia.util.DateUtils;
import cn.payadd.majia.util.StringUtil;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class ProfitRecordHolder extends BaseViewHolder<ProfitRecordPojo> {
    private TextView tvAddSymbol;
    private TextView tvIncomeType;
    private TextView tvIncomeTime;
    private TextView tvIncomeAmount;
    private ProfitRecordPojoAdapter adapter;
    private SimpleDateFormat format = new SimpleDateFormat(DateUtils.DEFAULT_DATETIME_FORMAT, Locale.getDefault());


    public ProfitRecordHolder(ViewGroup parent, ProfitRecordPojoAdapter adapter) {
        super(parent, R.layout.item_income_day_recode);
        this.adapter = adapter;
        tvAddSymbol = $(R.id.tv_add_symbol);
        tvIncomeType = $(R.id.tv_income_type);
        tvIncomeTime = $(R.id.tv_income_time);
        tvIncomeAmount = $(R.id.tv_income_amount);
    }

    @Override
    public void setData(final ProfitRecordPojo recordPojo) {
        if (StringUtil.equals("0", recordPojo.getMerOrderType()) || StringUtil.equals("1", recordPojo.getMerOrderType())) {
            tvAddSymbol.setText("+");
        } else if (StringUtil.equals("2", recordPojo.getMerOrderType()) || StringUtil.equals("3", recordPojo.getMerOrderType())) {
            tvAddSymbol.setText("-");
        }
        tvIncomeType.setText(BizTypeConstant.getTypeName(recordPojo.getBizType()));
        tvIncomeAmount.setText(SymbolConstant.RMB + recordPojo.getProfitAmount());
        tvIncomeTime.setText(format.format(recordPojo.getRecordTime()));

//        Glide.with(getContext())
//                .load(person.getFace())
//                .placeholder(R.mipmap.icon_agency)
//                .bitmapTransform(new CropCircleTransformation(getContext()))
//                .into(mImg_face);
    }
    public void setVisibility(boolean isVisible){
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
        if (isVisible){
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        }else{
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }
}
