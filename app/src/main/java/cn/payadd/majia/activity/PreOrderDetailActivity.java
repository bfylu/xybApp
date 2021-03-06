package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;

import java.math.BigDecimal;
import java.util.Map;

import cn.payadd.majia.constant.ActivityTitle;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.FundAuthOrderPresenter;

/**
 * Created by df on 2017/6/25.
 */

public class PreOrderDetailActivity extends BaseActivity implements View.OnClickListener, IActivity {
    private Button btnSettle;

    private TextView tvMerchantName, tvFreezeAmount, tvPayAmount, tvReturnAmount, tvReceiptAmount, tvAlipayAcc;

    private TextView tvOrderStatus, tvOrderNo, tvTradeTime, tvSettleTime;

    private FundAuthOrderPresenter fundAuthOrderPresenter;

    private ProgressDialog pDialog;

    private String mOrderNo;

    private PayPushBroadcastReceiver receiver;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pre_order_detail;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText(ActivityTitle.FUND_AUTH_ORDER_DETAIL);
        btnSettle = (Button) findViewById(R.id.btn_settle);
        btnSettle.setOnClickListener(this);
        btnSettle.setVisibility(View.GONE);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        tvFreezeAmount = (TextView) findViewById(R.id.tv_freeze_amount);
        tvPayAmount = (TextView) findViewById(R.id.tv_pay_amount);
        tvReturnAmount = (TextView) findViewById(R.id.tv_return_amount);
        tvReceiptAmount = (TextView) findViewById(R.id.tv_payee_amount);
        tvAlipayAcc = (TextView) findViewById(R.id.tv_alipay_acc);
        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        tvTradeTime = (TextView) findViewById(R.id.tv_trade_time);
        tvSettleTime = (TextView) findViewById(R.id.tv_settle_time);
    }

    @Override
    public void initData() {
        fundAuthOrderPresenter = new FundAuthOrderPresenter(this);
        mOrderNo = getIntent().getStringExtra("orderNo");
        getOrderDetail(this, mOrderNo);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
        receiver = new PayPushBroadcastReceiver(new PayPushBroadcastReceiver.Callback() {

            @Override
            public void exec(Bundle data) {

                final String orderStatus = data.getString("orderStatus");
                String payStatus = data.getString("payStatus");

                final String orderNo = data.getString("orderNo");
                if (!orderNo.equals(mOrderNo)) {
                    return;
                }

                if (!"pay_succ".equals(payStatus)) {
                    return;
                }
                if(!"finish".equals(orderStatus)){
                    return;
                }
                getOrderDetail(PreOrderDetailActivity.this, mOrderNo);
            }
        });
        registerReceiver(receiver, filter);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_settle:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final View layout = getLayoutInflater().inflate(R.layout.dialog_pre_settle, null);//获取自定义布局
                final TextView tvFreezeAmount = (TextView) layout.findViewById(R.id.tv_freeze_amount);
                final TextView tvPayAmount = (TextView) layout.findViewById(R.id.tv_pay_amount);
                final EditText etReturnAmount = (EditText) layout.findViewById(R.id.et_return_amount);
                tvFreezeAmount.setText(PreOrderDetailActivity.this.tvFreezeAmount.getText());
                etReturnAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().contains(".")) {
                            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                                s = s.toString().subSequence(0,
                                        s.toString().indexOf(".") + 3);
                                etReturnAmount.setText(s);
                                etReturnAmount.setSelection(s.length());
                            }
                        }
                        if (s.toString().trim().substring(0).equals(".")) {
                            s = "0" + s;
                            etReturnAmount.setText(s);
                            etReturnAmount.setSelection(2);
                        }
                        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                            if (!s.toString().substring(1, 2).equals(".")) {
                                etReturnAmount.setText(s.subSequence(0, 1));
                                etReturnAmount.setSelection(1);
                                return;
                            }
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        String freezeAmount = tvFreezeAmount.getText().toString().split(" ")[1];
                        String returnAmount = etReturnAmount.getText().toString();
                        if (TextUtils.isEmpty(returnAmount)) {
                            tvPayAmount.setText("");
                            return;
                        }
                        BigDecimal bdFreezeAmount = new BigDecimal(freezeAmount);
                        BigDecimal bdReturnAmount = new BigDecimal(returnAmount);
                        BigDecimal payAmount = bdFreezeAmount.subtract(bdReturnAmount);
                        bdFreezeAmount.setScale(2);
                        bdReturnAmount.setScale(2);
                        payAmount.setScale(2);
                        if (payAmount.compareTo(new BigDecimal(0)) < 0) {
                            Toast.makeText(PreOrderDetailActivity.this, "退回金额不能大于冻结金额", Toast.LENGTH_SHORT).show();
                            tvPayAmount.setText("");
                        } else {
                            tvPayAmount.setText(SymbolConstant.RMB + payAmount.toString());
                        }

//                        tvPayAmount.setText(bdFreezeAmount-bdReturnAmount);
                    }
                });
                builder.setView(layout);
                builder.setPositiveButton("确定结算", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                        String amtStr = tvPayAmount.getText().toString();
                        String payAmount;
                        if (!TextUtils.isEmpty(amtStr)) {
                            payAmount = amtStr.split(" ")[1];
                        } else {
                            return;
                        }

                        String freezeAmount = tvFreezeAmount.getText().toString();
                        String returnAmount = etReturnAmount.getText().toString();
                        if (TextUtils.isEmpty(payAmount) || TextUtils.isEmpty(freezeAmount) || TextUtils.isEmpty(returnAmount)) {
                            Toast.makeText(PreOrderDetailActivity.this, "请输入正确的退回金额", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        String orderNo = tvOrderNo.getText().toString();
                        settle(PreOrderDetailActivity.this, orderNo, payAmount, pDialog);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        Map<String, String> respData = (Map<String, String>) data;
        if (respData == null) {
            key = "exception";
        }
        if (pDialog != null) {
            pDialog.hide();
        }
        switch (key) {
            case "orderDetail": {
                String respCode = respData.get("respCode");
                if ("000000".equals(respCode)) {
                    tvOrderNo.setText(respData.get("orderNo"));
                    tvAlipayAcc.setText(respData.get("payerId"));
                    tvFreezeAmount.setText(SymbolConstant.RMB + respData.get("freezeAmount"));
                    tvSettleTime.setText(respData.get("settleTime"));
                    tvTradeTime.setText(respData.get("orderTime"));
                    tvPayAmount.setText(SymbolConstant.RMB + respData.get("payAmount"));
                    tvReturnAmount.setText(SymbolConstant.RMB + respData.get("thawAmount"));
                    tvReceiptAmount.setText(SymbolConstant.RMB + respData.get("acquireAmount"));
                    String status = respData.get("orderStatus");
                    btnSettle.setVisibility(View.INVISIBLE);
                    switch (status) {
                        case "00":
                            status = "未知";
                            break;
                        case "01":
                            status = "待结算";
                            btnSettle.setVisibility(View.VISIBLE);
                            break;
                        case "02":
                            status = "已结算";
                            break;
                        case "03":
                            status = "处理中";
                            break;
                        case "04":
                            status = "已关闭";
                            break;
                        default:
                            break;
                    }
                    tvOrderStatus.setText(status);

                } else {
                    Toast.makeText(this, respData.get("respDesc"), Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case "settle": {
                String respCode = respData.get("respCode");
                Toast.makeText(this, respData.get("respDesc"), Toast.LENGTH_SHORT).show();
                if ("000054".equals(respCode)) {
                    getOrderDetail(this, mOrderNo);
                } else if ("000000".equals(respCode)) {
                    getOrderDetail(this, mOrderNo);
                }
                break;
            }
            case "exception":
                Toast.makeText(this, "连接超时，请稍后再试", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }

    public void getOrderDetail(Context context, String orderNo) {
        if (pDialog != null) {
            pDialog.show();
        } else {
            pDialog = ProgressDialog.show(context, null, "请稍等...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(true);
        }
        fundAuthOrderPresenter.getOrderDetail(orderNo);

    }

    public void settle(Context context, String orderNo, String amount, ProgressDialog pDialog) {
        if (pDialog != null) {
            pDialog.show();
        } else {
            pDialog = ProgressDialog.show(context, null, "请稍等...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.setCancelable(true);
        }
        fundAuthOrderPresenter.orderSettle(orderNo, amount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if(pDialog!=null){
            pDialog.dismiss();
        }
    }
}
