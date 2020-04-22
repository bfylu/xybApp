package cn.payadd.majia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.pojo.DayProfitPojo;
import cn.payadd.majia.pojo.ProfitRecordPojo;
import cn.payadd.majia.pojo.WithdrawRecordPojo;
import cn.payadd.majia.util.DateUtils;

/**
 * Created by df on 2017/9/21.
 */

public class WithdrawRecordAdapter extends RecyclerArrayAdapter<WithdrawRecordPojo> {
    private int page = 1;

    private int totalPage;

    private int row = 10;
    public WithdrawRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new WithdrawRecordHolder(parent);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
