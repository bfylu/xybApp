package cn.payadd.majia.adapter.shoporder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import java.util.List;

import cn.payadd.majia.entity.MonthPaymentBean;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by lang on 2018/5/24.
 */

public class MonthPaymentAdapter extends BaseAdapter {
    private Context context;
    private List<MonthPaymentBean.DataBean.InstallmentMonthDetailBean> datas;
    private LayoutInflater inflater;

    public MonthPaymentAdapter(Context context, List<MonthPaymentBean.DataBean.InstallmentMonthDetailBean> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_month_payment, null);
            holder = new ViewHolder();
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvPayment = (TextView) convertView.findViewById(R.id.tv_payment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MonthPaymentBean.DataBean.InstallmentMonthDetailBean temp = datas.get(position);
        holder.tvDate.setText(StringUtil.toString(Utilities.setFormatTime(temp.getRepayDate(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd")));
        holder.tvPayment.setText(StringUtil.append("¥ ", temp.getRepayAmt(), "(含手续费¥ ", StringUtil.toString(temp.getChargeAmt()), ")"));
        return convertView;
    }

    private class ViewHolder {
        TextView tvDate, tvPayment;
    }
}
