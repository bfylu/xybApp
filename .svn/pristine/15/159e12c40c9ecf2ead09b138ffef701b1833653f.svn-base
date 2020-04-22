package cn.payadd.majia.activity;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.utils.Utilities;
import com.uuzuche.lib_zxing.camera.CameraManager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import cn.payadd.majia.constant.OrderType;
import cn.payadd.majia.constant.PayMethod;
import cn.payadd.majia.constant.PayType;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.presenter.FundAuthPresenter;
import cn.payadd.majia.view.ViewfinderView;
import cn.payadd.majia.view.ZXingScanQrcode;

/**
 * Created by df on 2017/6/27.
 */

public class FundAuthAcquireActivity extends BaseActivity implements View.OnClickListener, IActivity {

    private TextView tvAcquireAmt;

    private ImageView ivCenter;

    private TextView tvWeixin, tvAlipay, tvUnionpay, tvInstallment;

    private TextView tvScan, tvQrcode;

    private String tradeAmt, payMethod, payType, authCode;

    private TextView tvPayType;

    private RelativeLayout rlQrcode;

    private SurfaceView surfaceView;

    private ViewfinderView viewfinderView;

    private ZXingScanQrcode zXingScanQrcode;

    private FundAuthPresenter fundAuthPresenter;

    private ProgressDialog pDialog;

    private PayPushBroadcastReceiver receiver;

    private String mOrderNo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_acquire;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText("预授权收款");

        tvAcquireAmt = (TextView) findViewById(R.id.tv_acquire_amt);
        DecimalFormat df = new DecimalFormat("#################0.00");
        tradeAmt = df.format(new BigDecimal(getIntent().getStringExtra("amount")));
        tvAcquireAmt.setText(SymbolConstant.RMB + tradeAmt);

        rlQrcode = (RelativeLayout) findViewById(R.id.rl_qrcode);

        tvScan = (TextView) findViewById(R.id.tv_scan);
        tvQrcode = (TextView) findViewById(R.id.tv_qrcode);
        tvScan.setSelected(true);
        tvQrcode.setSelected(false);
        tvQrcode.setOnClickListener(this);

        ivCenter = (ImageView) findViewById(R.id.img_center);

        tvAlipay = (TextView) findViewById(R.id.btn_alipay);
        tvWeixin = (TextView) findViewById(R.id.btn_weixinpay);
        tvUnionpay = (TextView) findViewById(R.id.btn_unionpay);
        tvInstallment = (TextView) findViewById(R.id.btn_installment_pay);

        tvWeixin.setOnClickListener(this);
        tvUnionpay.setOnClickListener(this);
        tvInstallment.setOnClickListener(this);

        tvInstallment.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_unclickable));
        tvWeixin.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_unclickable));
        tvUnionpay.setBackgroundColor(ContextCompat.getColor(this, R.color.btn_unclickable));

        Drawable dWeixin = ContextCompat.getDrawable(this, R.mipmap.icon_weixin_unselected);
        dWeixin.setBounds(0, 0, dWeixin.getMinimumWidth(), dWeixin.getMinimumHeight());
        tvWeixin.setCompoundDrawables(dWeixin, null, null, null);
        Drawable dCard = ContextCompat.getDrawable(this, R.mipmap.icon_card_unselected);
        dCard.setBounds(0, 0, dCard.getMinimumWidth(), dCard.getMinimumHeight());
        tvUnionpay.setCompoundDrawables(dCard, null, null, null);
        Drawable dInstallment = ContextCompat.getDrawable(this, R.mipmap.icon_fenqi_selected);
        dInstallment.setBounds(0, 0, dInstallment.getMinimumWidth(), dInstallment.getMinimumHeight());
        tvInstallment.setCompoundDrawables(dInstallment, null, null, null);

//        tvInstallment.getBackground().setAlpha(102);
//        tvWeixin.getBackground().setAlpha(102);
//        tvUnionpay.getBackground().setAlpha(102);
        tvAlipay.setSelected(true);

        CameraManager.init(this);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);

    }

    @Override
    public void initData() {
        fundAuthPresenter = new FundAuthPresenter(this);
        zXingScanQrcode = new ZXingScanQrcode(this, surfaceView, viewfinderView, false, new ICallback() {
            @Override
            public void exec(Object params) {
                pDialog = ProgressDialog.show(FundAuthAcquireActivity.this, null, "请稍等...", true, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setCancelable(true);
                authCode = (String) params;
                String username = Utilities.getUsernameForLogin(FundAuthAcquireActivity.this);
                fundAuthPresenter.submitOrderFreeze(username, authCode, tradeAmt);
            }
        },0);
        scan();

        IntentFilter filter = new IntentFilter();
        filter.addAction(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
        receiver = new PayPushBroadcastReceiver(new PayPushBroadcastReceiver.Callback() {

            @Override
            public void exec(Bundle data) {
                final String orderStatus = data.getString("orderStatus");
                final String orderType = data.getString("orderType");
                final String orderNo = data.getString("orderNo");
                final String orderAmount = data.getString("orderAmount");
                final String orderTime = data.getString("orderTime");
                if (OrderType.FREEZE.equals(orderType)) {
                    if ("freeze".equals(orderStatus)) {
                        if (pDialog != null) {
                            pDialog.hide();
                        }
                        if(!mOrderNo.equals(orderNo)){
                            return;
                        }
                        Intent intent = new Intent(FundAuthAcquireActivity.this, FreezeSuccessActivity.class);
                        intent.putExtra("orderAmount", orderAmount);
                        intent.putExtra("orderNo", orderNo);
                        intent.putExtra("orderTime", orderTime);
                        startActivity(intent);
                        finish();
                    }
                }


            }
        });
        registerReceiver(receiver, filter);


    }

    private void scan() {
        zXingScanQrcode.init();
        rlQrcode.setBackgroundDrawable(null);
        ivCenter.setVisibility(View.GONE);
        tvQrcode.setSelected(false);
        tvScan.setSelected(true);
        tvQrcode.setTextColor(getResources().getColor(R.color.aquire_pay_method));
        tvScan.setTextColor(getResources().getColor(R.color.acquire_text_selected));
        this.payMethod = PayMethod.SCAN;
        selectPayType();

    }

    private void selectPayType() {

        tvPayType = tvAlipay;
        this.payType = PayType.ALIPAY;

    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_weixinpay:
            case R.id.btn_unionpay:
            case R.id.btn_installment_pay:
                Toast.makeText(FundAuthAcquireActivity.this, "预授权暂时只支持支付宝", Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_qrcode:
                Toast.makeText(FundAuthAcquireActivity.this, "支付宝预授权只支持主扫", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        zXingScanQrcode.getHandler().sendEmptyMessage(ZXingScanQrcode.RE_PREVIEW);
        switch (key) {
            case "freeze": {
                Map<String, String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                if ("000000".equals(respCode)) {
                    if (pDialog != null) {
                        pDialog.hide();
                    }
                    Intent intent = new Intent(FundAuthAcquireActivity.this, FreezeSuccessActivity.class);
                    intent.putExtra("orderAmount", respData.get("orderAmount"));
                    intent.putExtra("orderNo", respData.get("orderNo"));
                    intent.putExtra("orderTime", respData.get("orderTime"));
                    startActivity(intent);

                    finish();
                } else if ("000051".equals(respCode)) {
                    mOrderNo = respData.get("orderNo");

                } else {
                    if (pDialog != null) {
                        pDialog.hide();
                    }
                    Toast.makeText(this, respData.get("respDesc"), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case "exception": {
                if (pDialog != null) {
                    pDialog.hide();
                }
                Toast.makeText(this, "连接超时，请稍后再试", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
        }
        unregisterReceiver(receiver);
        if (zXingScanQrcode != null) {
            zXingScanQrcode.close();
        }
    }
}
