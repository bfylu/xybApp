package com.fdsj.credittreasure.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.Interface.iActivities.IQRCode;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.fdsj.credittreasure.fragment.QRCodeFragment;
import com.fdsj.credittreasure.fragment.ScanPayFragment;
import com.fdsj.credittreasure.presenter.PaymentPresenter;
import com.utils.Config;
import com.utils.Enums;
import com.utils.StatusBarUtils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.device.pos.IPosHelper;
import cn.payadd.majia.device.pos.PosDeviceUtil;

/**
 * 收款的主界面，包含三个fragment
 */
public class PaymentActivity extends BaseActivity implements View.OnClickListener, IQRCode, IActivity {

    private static final int UNIONPAY_REQ_CODE = 1;

    private ScanPayFragment scanPayFragment;//扫一扫frament
    private QRCodeFragment qrCodeFragment;//二维码fragment
    private BigDecimal amount = new BigDecimal(0);
    public static int anInt = 1;
    private PaymentPresenter presenter;
    public String currentOrderNo;

    /**
     * 智能扫码
     */
    public final static int Intelligence = 0;

    /**
     * 微信
     */
    public final static int WeChat = 1;

    /**
     * 支付宝
     */
    public final static int Alipay = 2;

    /**
     * 银联
     */
    public final static int Unionpay = 3;

    private PayPushBroadcastReceiver receiver;


    @Override
    protected int getLayoutView() {
        return R.layout.activity_gathering;
    }


    @Override
    protected void initView() {
        anInt = 1;
//        super.setCenterText(getResources().getString(R.string.gathering_title));
//        super.setBackOnclick();
        StatusBarUtils.setBackGroundResource(this, R.mipmap.a);
//        super.setActionBarBackground(R.mipmap.b);
        Intent intent = getIntent();
        if (intent != null) {
            amount = new BigDecimal(intent.getStringExtra("amount"));
        }
    }

    @Override
    protected void initData() {
        presenter = new PaymentPresenter(this);
        fragmentManager = getSupportFragmentManager();
        qrCodeFragment = QRCodeFragment.getQRCodeFragment(amount);
        switchFragment(qrCodeFragment);

        if(EnvironmentConfig.isPos()){
            IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(),this);
            helper.init(this);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
        receiver = new PayPushBroadcastReceiver(new PayPushBroadcastReceiver.Callback() {

            @Override
            public void exec(Bundle data) {

                final String orderStatus = data.getString("orderStatus");

                String bizType = data.getString("bizType");
                if (!"majia".equals(bizType)) {
                    return;
                }

                final String orderNo = data.getString("orderNo");
                if (!orderNo.equals(currentOrderNo)) {
                    return;
                }

                if (!"pay_succ".equals(orderStatus)) {
                    return;
                }


                final String orderAmt = data.getString("orderAmount");
                final String orderDate = data.getString("orderDate");
                final String orderDesc = data.getString("orderDesc");
                final String merCode = data.getString("merCode");
                final String merName = data.getString("merName");
                final String merShortName = data.getString("merShortName");
                final String payType = data.getString("payType");
                final String address = data.getString("address");

                String msg = "交易成功";

                if (Config.isPos()) {
                    if ("unionpay".equals(payType)) {
                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                    builder.setTitle("提示").setMessage(msg + "，是否打印小票？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(), PaymentActivity.this);
                            List<String> contentList = new ArrayList<>();
                            contentList.add(transJson(3, "", 1).toString());
                            contentList.add(transJson(3,"    " + merShortName, 0).toString());
                            contentList.add(transJson(2, "", 1).toString());
                            contentList.add(transJson(2, "商户编号："+merCode, 0).toString());
                            contentList.add(transJson(2, "终端号："+ helper.getTerminalNo(), 0).toString());
                            contentList.add(transJson(2, "订单号："+orderNo, 0).toString());
                            contentList.add(transJson(2, "交易时间："+orderDate, 0).toString());
                            contentList.add(transJson(2, "交易金额："+orderAmt+"元", 0).toString());
                            contentList.add(transJson(2, "店铺地址："+address, 0).toString());
                            contentList.add(transJson(2, "备注："+orderDesc, 1).toString());
                            contentList.add(transJson(2, "————————————————————", 1).toString());
                            contentList.add(transJson(2, "技术支持：PAY+支付", 0).toString());
                            contentList.add(transJson(3, "", 1).toString());
                            helper.printReceipt(contentList,PaymentActivity.this);
                            dialog.dismiss();
                            finish();
                        }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                    builder.setTitle("提示").setMessage(msg).setPositiveButton(msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.create().show();
                }
            }
        });
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.image_scan://扫码
//                include_title.setVisibility(View.GONE);
//                super.setActionBarBackgroundColor(R.color.transparent);
//                line_action.setBackgroundColor(getResources().getColor(R.color.bgColor_overlay));
                StatusBarUtils.setColor(this, R.color.bgColor_overlay);
                if (scanPayFragment == null) {
                    scanPayFragment = ScanPayFragment.getZxingFragment(amount);
                    switchFragment(scanPayFragment);
                } else {
                    scanPayFragment.setTextColor(anInt);
                    switchFragment(scanPayFragment);
                }
                break;
            case R.id.image_qrcode://二维码
                StatusBarUtils.setBackGroundResource(this, R.mipmap.a);
//                include_title.setVisibility(View.VISIBLE);
//                super.setActionBarBackground(R.mipmap.b);
                if (qrCodeFragment == null) {
                    qrCodeFragment = QRCodeFragment.getQRCodeFragment(amount);
                    switchFragment(qrCodeFragment);
                } else {
                    qrCodeFragment.setTextColor(anInt);
                    switchFragment(qrCodeFragment);
                }
                break;
            case R.id.image_unionpay:

                DecimalFormat df = new DecimalFormat("###############0.00");
                presenter.submitOrder(PaymentActivity.anInt, df.format(amount), "", Enums.payMethod.刷卡, Enums.httpPayType.银联支付, this);

                break;
        }
    }

    public void nowUnionpay() {
        View v = findViewById(R.id.image_unionpay);
        onClick(v);
    }

    public JSONObject transJson(int fontSize, String content, int lineFeedCount) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("fontSize", fontSize);
            jsonObject.put("content", content);
            jsonObject.put("lineFeedCount", lineFeedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case UNIONPAY_REQ_CODE: {
                // 银行卡支付
                switch(resultCode) {
                    case Activity.RESULT_CANCELED:
                        String reason = data.getStringExtra("reason");
                        if(reason != null) {
                            Toast.makeText(this, reason, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case Activity.RESULT_OK:
                        Toast.makeText(this, "交易成功", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
                break;
            }
        }
    }

    @Override
    public void stopRecyclerView() {

    }

    @Override
    public void UpdateModel(Object model, int status) {

    }

    @Override
    public void NoModel() {

    }

    @Override
    public void UpdateModel(String payUrl, String orderNo, int status) {

        currentOrderNo = orderNo;
        if (TextUtils.isEmpty(currentOrderNo)) {
            Toast.makeText(this, "订单号不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.shengpay.smartpos.shengpaysdk", "com.shengpay.smartpos.shengpaysdk.activity.MainActivity"));
        intent.putExtra("transName", "0");
        // “0”：银行卡 “1”：微信支付 “2”：支付宝 “3”：盛付通钱包
//       intent.putExtra("barcodeType", "0");
        intent.putExtra("appId", getPackageName());
        DecimalFormat df = new DecimalFormat("000000000000");
        intent.putExtra("amount", df.format(amount.multiply(new BigDecimal(100))));
        intent.putExtra("orderNoSFT", currentOrderNo);
//                intent.putExtra("oldTraceNo", "");
//                intent.putExtra("oldReferenceNo", "");
        // 非必填，消费退货，如果不传入则调起收单退货界面
        startActivityForResult(intent, UNIONPAY_REQ_CODE);
    }

    @Override
    public void finish() {
        super.finish();
        if(EnvironmentConfig.isPos()){
            IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(),this);
            helper.close(this);
        }
        if (null != receiver) {
            unregisterReceiver(receiver);
        }
    }
}
