package cn.payadd.majia.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.utils.Config;
import com.utils.Utilities;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.payadd.majia.listener.NoDoubleClickListener;
import cn.payadd.majia.presenter.InstallmentPresenter;
import cn.payadd.majia.util.DensityUtil;

/**
 * Created by zhengzhen.wang on 2017/6/19.
 */

public class InstallmentPayActivity extends BaseActivity {

    public static final String KEY_INSTALLMENT_AMOUNT = "installmentAmount";

    public static final String KEY_INSTALLMENT_URL = "installmentUrl";

    public static final String KEY_ORDER_AMOUNT = "orderAmount";

    public static final String KEY_DOWN_PAYMENT = "downPayment";

    public static final String KEY_LINK_URL = "linkUrl";

    public static final String KEY_SOURCE = "source";
    private LinearLayout llBottom;

    private ProgressDialog pDialog;

    private InstallmentPresenter presenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_installment_pay;
    }

    @Override
    public void initView() {

        setTitleCenterText("分期支付");
        setTitleBackButton();

        TextView tvOrderAmt = (TextView) findViewById(R.id.tv_order_amount);
        tvOrderAmt.setText("￥" + getIntent().getStringExtra(KEY_ORDER_AMOUNT));

        TextView tvInstallmentAmt = (TextView) findViewById(R.id.tv_installment_amount);
        tvInstallmentAmt.setText("￥" + getIntent().getStringExtra(KEY_INSTALLMENT_AMOUNT));

        final TextView tvDownPayment = (TextView) findViewById(R.id.tv_down_payment);
        tvDownPayment.setText("￥" + getIntent().getStringExtra(KEY_DOWN_PAYMENT));

        ImageView ivQrcode = (ImageView) findViewById(R.id.iv_installment_qrcode);
        int size = DensityUtil.dip2px(this, 200);
        int logo = R.mipmap.logo;
        String url = getIntent().getStringExtra(KEY_INSTALLMENT_URL);
        Bitmap qrcodeImg = CodeUtils.createImage(url, size, size, BitmapFactory.decodeResource(getResources(), logo));
        ivQrcode.setImageBitmap(qrcodeImg);

        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        String source = getIntent().getStringExtra(KEY_SOURCE);
        if(Config.isMobile()){
            if("app".equals(source)){
                llBottom.setVisibility(View.GONE);
            }else if("h5".equals(source)){
                llBottom.setVisibility(View.VISIBLE);
            }
            Button btnShare = (Button) findViewById(R.id.btn_share);
            btnShare.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    String url = getIntent().getStringExtra(KEY_LINK_URL);
                    Intent share_intent = new Intent();
                    share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
                    share_intent.setType("text/plain");//设置分享内容的类型
                    share_intent.putExtra(Intent.EXTRA_SUBJECT, "分期支付-"+ Utilities.getShowMerName(InstallmentPayActivity.this));//添加分享内容标题
                    share_intent.putExtra(Intent.EXTRA_TEXT, url);//添加分享内容
                    //创建分享的Dialog
                    share_intent = Intent.createChooser(share_intent,"分期支付-"+ Utilities.getShowMerName(InstallmentPayActivity.this));
                    startActivity(share_intent);
                }
            });
        }


    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPermission() {

    }
}
