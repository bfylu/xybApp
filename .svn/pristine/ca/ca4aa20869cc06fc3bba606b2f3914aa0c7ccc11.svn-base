

package cn.payadd.majia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.StickyHeaderDecoration;
import com.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.pojo.DayProfitPojo;
import cn.payadd.majia.pojo.ProfitRecordPojo;
import cn.payadd.majia.util.DateUtils;

/**
 * 当前类注释：悬浮headerAdapter
 * PackageName：com.jude.dome.sticky
 * Created by Qyang on 16/11/4
 * Email: yczx27@163.com
 */
public class StickyHeaderAdapter implements StickyHeaderDecoration.IStickyHeaderAdapter<StickyHeaderAdapter.HeaderHolder> {

    private LayoutInflater mInflater;
    private List<DayProfitPojo> list;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT, Locale.getDefault());
    private ProfitRecordPojoAdapter adapter;
    public StickyHeaderAdapter(Context context,ProfitRecordPojoAdapter adapter) {
        mInflater = LayoutInflater.from(context);
        list = new ArrayList<>();
        this.adapter = adapter;
    }

    @Override
    public long getHeaderId(int position) {
        Log.d("getHeaderId", "getHeaderId: "+ position);
        //防止下标越界
        if(position>=adapter.getAllData().size()){
            return list.size()-1;
        }
            ProfitRecordPojo recordPojo = adapter.getAllData().get(position);
            Date dateTime = recordPojo.getRecordTime();
            int headerId = 0;
            for (int i = 0;i<list.size();i++){
                Date date = list.get(i).getDate();
                if(DateUtils.isSameDate(date,dateTime)){
                    headerId = i;
                }
            }
            return headerId;
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.item_income_recode, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        DayProfitPojo dayProfitPojo = list.get((int) getHeaderId(position));
        if(DateUtils.isSameDate(dayProfitPojo.getDate(),DateUtils.getTodayZeroTime())){
            viewholder.tvDayIncome.setText(SymbolConstant.RMB + dayProfitPojo.getDayTotalAmount());
            viewholder.labDayIncome.setText("今日收益");
            return;
        }
        if(DateUtils.isSameDate(dayProfitPojo.getDate(),DateUtils.getYesterdayZeroTime())){
            viewholder.tvDayIncome.setText(SymbolConstant.RMB + dayProfitPojo.getDayTotalAmount());
            viewholder.labDayIncome.setText("昨日收益");
            return;
        }
        viewholder.tvDayIncome.setText(SymbolConstant.RMB + dayProfitPojo.getDayTotalAmount());
        viewholder.labDayIncome.setText(dateFormat.format(dayProfitPojo.getDate()));
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView tvDayIncome;
        public TextView labDayIncome;

        public HeaderHolder(View itemView) {
            super(itemView);
            tvDayIncome = (TextView) itemView.findViewById(R.id.tv_day_income);
            labDayIncome = (TextView) itemView.findViewById(R.id.lab_day_income);

        }
    }
    public void update(List<DayProfitPojo> list,boolean resetPage){
        if(resetPage){
            this.list.clear();
        }
        this.list.addAll(list);

    }
}
