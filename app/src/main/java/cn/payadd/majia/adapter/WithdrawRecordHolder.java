package cn.payadd.majia.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.payadd.majia.constant.BizTypeConstant;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.constant.WithdrawStatus;
import cn.payadd.majia.pojo.ProfitRecordPojo;
import cn.payadd.majia.pojo.WithdrawRecordPojo;
import cn.payadd.majia.util.DateUtils;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class WithdrawRecordHolder extends BaseViewHolder<WithdrawRecordPojo> {
    private TextView tvWithdrawType;
    private TextView tvWithdrawAmount;
    private TextView tvWithdrawTime;
    private SimpleDateFormat format = new SimpleDateFormat(DateUtils.DEFAULT_DATETIME_FORMAT, Locale.getDefault());


    public WithdrawRecordHolder(ViewGroup parent) {
        super(parent, R.layout.item_withdraw_record);
        tvWithdrawType = $(R.id.tv_withdraw_type);
        tvWithdrawTime = $(R.id.tv_withdraw_time);
        tvWithdrawAmount = $(R.id.tv_withdraw_amount);
    }


    @Override
    public void setData(final WithdrawRecordPojo recordPojo){
        super.setData(recordPojo);
        tvWithdrawType.setText(WithdrawStatus.getStatusName(recordPojo.getStatus()));
        tvWithdrawAmount.setText(SymbolConstant.RMB+recordPojo.getWithdrawAmount());
        tvWithdrawTime.setText(format.format(recordPojo.getWithdrawTime()));
    }
}
