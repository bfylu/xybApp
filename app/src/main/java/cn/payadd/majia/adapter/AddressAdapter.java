package cn.payadd.majia.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by df on 2017/9/19.
 */

public class AddressAdapter extends ArrayAdapter<Map<String, String>> {
    private Context mContext;
    private List<Map<String, String>> addresses;
    private LayoutInflater mInflater;
    private int resource;
    private int textViewResourceId;

    public AddressAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int
            textViewResourceId, @NonNull List<Map<String, String>> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
        if (objects != null) {
            this.addresses = objects;
        } else {
            this.addresses = new ArrayList<>();
        }
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Map<String, String> address = getItem(position);
        String addressName = address.get("addressName");
        if (convertView == null)
            convertView = mInflater.inflate(resource, null);
        TextView text = (TextView) convertView.findViewById(textViewResourceId);
        text.setText(addressName);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        Map<String, String> address = getItem(position);
        String addressName = address.get("addressName");
        if (convertView == null)
            convertView = mInflater.inflate(resource, null);
        TextView text = (TextView) convertView.findViewById(textViewResourceId);
        text.setText(addressName);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        return convertView;
    }
}
