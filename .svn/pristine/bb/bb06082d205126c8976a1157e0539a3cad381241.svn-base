package cn.payadd.majia.adapter.shoporder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.util.List;

import cn.payadd.majia.entity.ExpressCompanyBean;

/**
 * Created by lang on 2018/5/18.
 */

public class ChooseExpressDialogAdapter extends BaseAdapter {

    private Context context;
    private int choosePosition = -1;
    private List<ExpressCompanyBean.DataBean.DataListBean> dataBeanList;
    private LayoutInflater inflater;

    public ChooseExpressDialogAdapter(Context context, List<ExpressCompanyBean.DataBean.DataListBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataBeanList == null ? 0 : dataBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_close_order_dialog, null);
            holder = new ViewHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ExpressCompanyBean.DataBean.DataListBean temp = dataBeanList.get(position);
        holder.tv_content.setText(temp.getCompanyName());
        if (choosePosition == position) {
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.btn_clickable));
        } else {
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.color_9));
        }
        return convertView;
    }

    public void selectPosition(int position) {
        choosePosition = position;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tv_content;
    }
}
