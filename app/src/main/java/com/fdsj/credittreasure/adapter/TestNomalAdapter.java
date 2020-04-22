package com.fdsj.credittreasure.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fdsj.credittreasure.R;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

/**
 * Created by BXND on 2017-01-03.
 * 轮播图Adapter
 */

public class TestNomalAdapter extends StaticPagerAdapter {
    private int[] imgs = {
            R.mipmap.advertisement,
            R.mipmap.advertisement,
            R.mipmap.advertisement,
            R.mipmap.advertisement,
    };

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }
}
