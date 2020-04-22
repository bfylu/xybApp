package cn.payadd.majia.adapter.openminipro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import cn.payadd.majia.view.CircleImageView;

public class OpenMiniProAdapter extends BaseAdapter {

    private String[] contents;
    private int[] icons;
    private Context context;
    private LayoutInflater inflater;

    public OpenMiniProAdapter(String[] contents, int[] icons, Context context) {
        this.contents = contents;
        this.icons = icons;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contents == null ? 0 : contents.length;
    }

    @Override
    public Object getItem(int position) {
        return contents[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_open_mini_pro, null);
            holder = new ViewHolder();
            holder.ivHead = (CircleImageView) convertView.findViewById(R.id.iv_head);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ivHead.setImageResource(icons[position]);
        holder.tvContent.setText(contents[position]);
        return convertView;
    }

    private class ViewHolder {
        CircleImageView ivHead;
        TextView tvContent;
    }
}
