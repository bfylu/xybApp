package cn.payadd.majia.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.Map;

/**
 * Created by df on 2017/12/27.
 */

public class GoodsAapter extends RecyclerArrayAdapter<Map<String,String>>{

    public GoodsAapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoodsHolder(parent);
    }


}
