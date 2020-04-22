package cn.payadd.majia.pojo;

import android.support.annotation.NonNull;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by df on 2017/9/21.
 */

public class DayProfitPojo implements Comparable<DayProfitPojo>{
    private Date date;
    private String dayTotalAmount;
    private List<ProfitRecordPojo> recordList;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDayTotalAmount() {
        return dayTotalAmount;
    }

    public void setDayTotalAmount(String dayTotalAmount) {
        this.dayTotalAmount = dayTotalAmount;
    }

    public List<ProfitRecordPojo> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<ProfitRecordPojo> recordList) {
        this.recordList = recordList;
    }

    @Override
    public int compareTo(@NonNull DayProfitPojo o) {
        return o.getDate().compareTo(this.date);
    }
}
