package cn.payadd.majia.adapter.aistore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.util.List;
import java.util.Map;

import cn.payadd.majia.entity.aistore.ClientScreenBean;

public class PopClientAdapter extends BaseAdapter {

    private Context context;
    private List<ClientScreenBean.DataBean> data;
    private int choosePosition = -1;
    private LayoutInflater inflater;

    public PopClientAdapter(Context context, List<ClientScreenBean.DataBean> data, int choosePosition) {
        this.context = context;
        this.data = data;
        this.choosePosition = choosePosition;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_pop_client, null);
            holder = new ViewHolder();
            holder.tvChoose = (TextView) convertView.findViewById(R.id.tv_choose);
            holder.ivChecked = (ImageView) convertView.findViewById(R.id.iv_checked);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClientScreenBean.DataBean temp = data.get(position);
        holder.tvChoose.setText(temp.getDesc());
        if (position == choosePosition) {
            holder.ivChecked.setVisibility(View.VISIBLE);
        } else {
            holder.ivChecked.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void selectPosition(int position) {
        choosePosition = position;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvChoose;
        ImageView ivChecked;
    }
}
