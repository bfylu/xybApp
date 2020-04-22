package cn.payadd.majia.pojo;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by df on 2017/9/21.
 */

public class ProfitRecordPojo implements Comparable<ProfitRecordPojo>{
    private Date recordTime;
    private String merOrderType;//0、默认 1、支付 2、退款 3、撤销  对应的加减号 为  0、1 为加  2、3为减
    private String bizType;//1.广告收益
    private String profitAmount;

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public String getMerOrderType() {
        return merOrderType;
    }

    public void setMerOrderType(String merOrderType) {
        this.merOrderType = merOrderType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(String profitAmount) {
        this.profitAmount = profitAmount;
    }

    @Override
    public int compareTo(@NonNull ProfitRecordPojo o) {
        return o.getRecordTime().compareTo(this.recordTime);
    }
}
