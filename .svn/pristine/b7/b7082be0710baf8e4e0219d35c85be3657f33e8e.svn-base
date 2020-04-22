package cn.payadd.majia.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cn.payadd.majia.pojo.Ad;
import cn.payadd.majia.util.Utils;

/**
 * Created by Mr.Jude on 2016/1/6.
 */
public class AdViewHolder extends BaseViewHolder<Ad> {
    public AdViewHolder(ViewGroup parent) {
        super(new ImageView(parent.getContext()));
        ImageView imageView = (ImageView) itemView;
        imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Utils.convertDpToPixel(156,getContext())));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void setData(final Ad data) {
        ImageView imageView = (ImageView) itemView;
//        Glide.with(getContext())
//                .load(data.getImage())
//                .placeholder(R.mipmap.icon_agency)
//                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrl())));
            }
        });
    }

}
