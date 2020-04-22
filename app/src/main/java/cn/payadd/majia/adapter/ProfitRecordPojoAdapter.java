package cn.payadd.majia.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cn.payadd.majia.pojo.ProfitRecordPojo;


/**
 * Created by Mr.Jude on 2015/7/18.
 */
public class  ProfitRecordPojoAdapter extends RecyclerArrayAdapter<ProfitRecordPojo> {
    private int page = 1;

    private int totalPage;

    private int row = 10;
    public ProfitRecordPojoAdapter(Context context) {
        super(context);
    }

    @Override
    public void OnBindViewHolder(BaseViewHolder holder, int position) {
        ProfitRecordPojo recordPojo = this.getItem(position);
        if(recordPojo.getProfitAmount().equals("0.00")){
            ((ProfitRecordHolder)holder).setVisibility(false);
        }else{
            ((ProfitRecordHolder)holder).setVisibility(true);
        }
        super.OnBindViewHolder(holder, position);
    }


    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {


        return new ProfitRecordHolder(parent,this);
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
