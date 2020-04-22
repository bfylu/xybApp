package com.fdsj.credittreasure.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.fdsj.credittreasure.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;


import butterknife.BindView;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 我的固定码
 * <p>当前版本： V1.0.0
 */

public class FixedCodeFragment extends BaseFragment {
    @BindView(R.id.imageViewCode)
    ImageView imageViewCode;
    private String data;
    private int rData = R.mipmap.logo;
    private int rId = 0;
    Bitmap mBitmap;

    public static FixedCodeFragment getFixedCodeFragment(String data, int rData) {
        FixedCodeFragment codeFragment = new FixedCodeFragment();
        codeFragment.data = data;
        codeFragment.rData = rData;
        return codeFragment;
    }

    public static FixedCodeFragment getFixedCodeFragment(int rId) {
        FixedCodeFragment codeFragment = new FixedCodeFragment();
        codeFragment.rId = rId;
        return codeFragment;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_fixedcode;
    }

    @Override
    protected void initView() {
        if (rId != 0) {
            imageViewCode.setImageResource(rId);
        } else if (data != null) {
            mBitmap = CodeUtils.createImage(data, 700, 700, BitmapFactory.decodeResource(getResources(), rData));
            imageViewCode.setImageBitmap(mBitmap);
        }
    }


    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(data)) {
            mBitmap = CodeUtils.createImage(data, 700, 700, BitmapFactory.decodeResource(getResources(), rData));
            imageViewCode.setImageBitmap(mBitmap);
        }
    }

    public void setImageViewCode(int rId) {
        this.rId = rId;
        this.rData = R.mipmap.logo;
        this.data = null;
        if (imageViewCode != null) {
            imageViewCode.setImageResource(rId);
        }
    }

    public void setImageViewCode(String data, int rData) {
        this.data = data;
        this.rId = 0;
        this.rData = rData;
        if (imageViewCode != null) {
            mBitmap = CodeUtils.createImage(data, 700, 700, BitmapFactory.decodeResource(getResources(), rData));
            imageViewCode.setImageBitmap(mBitmap);
        }
    }
}
