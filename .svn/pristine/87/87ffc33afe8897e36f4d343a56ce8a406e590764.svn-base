package com.fdsj.credittreasure.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.utils.guideview.Component;

//import com.blog.www.guideview.Component;

/**
 * Created by lang on 2018/5/11.
 */

public class FinishComponent implements Component {

    private Context context;
    private View.OnClickListener clickListener;

    public FinishComponent(Context context, View.OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_next, null);
        TextView tv_next = (TextView) ll.findViewById(R.id.tv_next);
        TextView tv_content = (TextView) ll.findViewById(R.id.tv_content);
        TextView tv = (TextView) ll.findViewById(R.id.tv);
        ImageView iv = (ImageView) ll.findViewById(R.id.iv);
        iv.setImageResource(R.mipmap.yuyinbobao);
        tv.setText(context.getString(R.string.voice_notification));
        tv_content.setText(context.getResources().getString(R.string.click_set_voice));
        tv_next.setText(context.getResources().getString(R.string.i_know));
        tv_next.setBackground(context.getResources().getDrawable(R.drawable.shape_btn_enable));
        tv_next.setOnClickListener(clickListener);
        return ll;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 10;
    }
}
