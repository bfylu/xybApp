package cn.payadd.majia.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

import com.fdsj.credittreasure.R;

public class QRCodeVPDialog extends Dialog {

    private ViewPager mViewPager;

    public QRCodeVPDialog(@NonNull Context context) {
        super(context, R.style.dialog_custom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mini_pro);

    }
}
