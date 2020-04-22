package com.fdsj.credittreasure.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.utils.guideview.Component;

//import com.blog.www.guideview.Component;

/**
 * Created by lang on 2018/5/11.
 */

public class NextComponent implements Component {

    private Context context;
    private View.OnClickListener clickListener;

    public NextComponent(Context context, View.OnClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public View getView(LayoutInflater inflater) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_next, null);
        RelativeLayout rl = (RelativeLayout) ll.findViewById(R.id.rl);
        WindowManager wm1 = ((Activity)context).getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth();
        int height1 = wm1.getDefaultDisplay().getHeight();

//        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(width1, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        rl.setLayoutParams(p);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width1, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        ll.setLayoutParams(params);

        TextView tv_next = (TextView) ll.findViewById(R.id.tv_next);
        TextView tv_content = (TextView) ll.findViewById(R.id.tv_content);
        TextView tv = (TextView) ll.findViewById(R.id.tv);
        ImageView iv = (ImageView) ll.findViewById(R.id.iv);
        iv.setImageResource(R.mipmap.mimaguanli);
        tv.setText(context.getString(R.string.title_merchant_pwd));
        tv_content.setText(context.getResources().getString(R.string.click_set_pwd));
        tv_next.setText(context.getResources().getString(R.string.next));
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
