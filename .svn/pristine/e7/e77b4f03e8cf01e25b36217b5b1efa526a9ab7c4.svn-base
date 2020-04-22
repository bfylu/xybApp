package com.fdsj.credittreasure.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.util.List;

/**
 * Created by BXND on 2017-01-04.
 */

public class CalculationAdapter extends RecyclerView.Adapter<CalculationAdapter.MyViewHolder> {

    Context context;
    List<String> stringList;
    private LayoutInflater inflater;


    public CalculationAdapter(Context context, List<String> strings) {
        this.context = context;
        this.stringList = strings;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_text, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);

        }
    }
}
