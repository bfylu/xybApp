package cn.payadd.majia.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dd.plist.NSDictionary;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by df on 2017/9/19.
 */

public class BankArrayAdapter extends ArrayAdapter<Map<String, String>> {
    private Context mContext;
    private List<Map<String, String>> banks;
    private LayoutInflater mInflater;
    private int resource;
    private int textViewResourceId;

    public BankArrayAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int
            textViewResourceId, @NonNull List<Map<String, String>> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mContext = context;
        if (objects != null) {
            this.banks = objects;
        } else {
            this.banks = new ArrayList<>();
        }
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return banks.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return banks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Map<String, String> map = getItem(position);
        String bankName = map.get("bankName");
        if (convertView == null)
            convertView = mInflater.inflate(resource, null);
        TextView text = (TextView) convertView.findViewById(textViewResourceId);
        text.setText(bankName);
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        Map<String, String> map = getItem(position);
        String bankName = map.get("bankName");
        if (convertView == null)
            convertView = mInflater.inflate(resource, null);
        TextView text = (TextView) convertView.findViewById(textViewResourceId);
        text.setText(bankName);
        return convertView;
    }
}
