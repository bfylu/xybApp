package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.entity.SerializableMap;
import com.utils.Config;
import com.utils.Utilities;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.activity.refund.RefundActivity;
import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.constant.ApplyActivityContainer;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.device.pos.IPosHelper;
import cn.payadd.majia.device.pos.PosDeviceUtil;
import cn.payadd.majia.device.pos.PrintConstant;
import cn.payadd.majia.device.pos.PrintFormat;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.PaymentDetailPresenter;
import cn.payadd.majia.util.CenterDialog;
import cn.payadd.majia.util.DateUtils;
import cn.payadd.majia.util.StringUtil;

import static com.fdsj.credittreasure.R.id.tv_trade_no;
import static com.fdsj.credittreasure.constant.Constants.PAY_TYPE;
import static com.fdsj.credittreasure.constant.Constants.PROC_CD;
import static com.fdsj.credittreasure.constant.Constants.TRADE_AMT;
import static com.fdsj.credittreasure.constant.Constants.TRADE_NO;

/**
 * Created by Administrator on 2017/07/18 0018.
 */

public class PaymentDetailActivity extends BaseActivity implements IActivity, View.OnClickListener {

    @BindView(R.id.rl_refund_amount)
    RelativeLayout rlRefundAmount;
    @BindView(R.id.tv_refund_amount)
    TextView tvRefundAmount;
    @BindView(R.id.label_pay_status)
    TextView tvLabelPayStatus;
    @BindView(R.id.label_pay_type)
    TextView tvLabelPayType;

    private TextView tvTradeAmt, tvMerName, tvPayStatus, tvPayType, tvProduct, tvCashier,
            tvOrigOrderNo, tvTradeNo, tvTradeTime;

    private TextView labelTradeAmt, labelMerName, labelPayStatus, labelPayType, labelProduct,
            labelCashier, labelOrigOrderNo, labelTradeNo, labelTradeTime;

    private Button btnRefund, btnPosPrint, btnCheckOrder;

    private LinearLayout llBottom;

    private RelativeLayout rlOrigOrderNo;

    private String orderNo;

    private Map<String, String> stringStringMap;

    private PaymentDetailPresenter presenter;

    private ProgressDialog pDialog;

    private ImageView imCopyOrigOrderNo;

    private ImageView imCopyOrderNo;

    private NumberFormat currency = NumberFormat.getCurrencyInstance();

    private String terminalNo;

    private String tradeAmt, refundAmt, merName, orderStatus, orderType, payType, bizType, cashier, origOrderNo, refundOrderNo, tradeNo, refundNo, tradeTime, refundTime;

    private String onlineRefunds;//是否开通联机退货    true为开通  false 为未开通

    private Intent intent;

    private CenterDialog dialog;

    private SimpleDateFormat mFormat;
    private long orderTime, nowTime, ThirtyDaysTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_detail;
    }

    @Override
    public void initView() {

        if (ApplyActivityContainer.refundAct == null) {
            ApplyActivityContainer.refundAct = new LinkedList<>();
        }
        ApplyActivityContainer.refundAct.add(this);

        tvTradeAmt = (TextView) findViewById(R.id.tv_trade_amount);
        tvMerName = (TextView) findViewById(R.id.tv_merchant_name);
        tvPayStatus = (TextView) findViewById(R.id.tv_pay_status);
        tvPayType = (TextView) findViewById(R.id.tv_pay_type);
        tvProduct = (TextView) findViewById(R.id.tv_product);
        tvCashier = (TextView) findViewById(R.id.tv_cashier);
        tvOrigOrderNo = (TextView) findViewById(R.id.tv_orig_order_no);
        tvTradeNo = (TextView) findViewById(tv_trade_no);
        tvTradeTime = (TextView) findViewById(R.id.tv_trade_time);

        labelTradeAmt = (TextView) findViewById(R.id.label_trade_amount);
        labelMerName = (TextView) findViewById(R.id.label_merchant_name);
        labelPayStatus = (TextView) findViewById(R.id.label_pay_status);
        labelPayType = (TextView) findViewById(R.id.label_pay_type);
        labelProduct = (TextView) findViewById(R.id.label_product);
        labelCashier = (TextView) findViewById(R.id.label_cashier);
        labelOrigOrderNo = (TextView) findViewById(R.id.label_orig_order_no);
        labelTradeNo = (TextView) findViewById(R.id.label_trade_no);
        labelTradeTime = (TextView) findViewById(R.id.label_trade_time);

        btnRefund = (Button) findViewById(R.id.btn_refund);
        btnPosPrint = (Button) findViewById(R.id.btn_pos_print);
        btnCheckOrder = (Button) findViewById(R.id.btn_check_order);

        rlOrigOrderNo = (RelativeLayout) findViewById(R.id.rl_orig_order_no);

        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);

        imCopyOrigOrderNo = (ImageView) findViewById(R.id.orig_order_no_copy);
        imCopyOrderNo = (ImageView) findViewById(R.id.trade_no_copy);
        btnRefund.setOnClickListener(this);
        btnPosPrint.setOnClickListener(this);
        btnCheckOrder.setOnClickListener(this);

        imCopyOrigOrderNo.setOnClickListener(this);
        imCopyOrderNo.setOnClickListener(this);
    }

    private void queryOrder(String orderNo) {
        if (TextUtils.isEmpty(orderNo)) {
            Map<String, String> map = new HashMap<>();
            map.put("respDesc", "找不到该订单");
            updateModel("orderQueryErr", null);
        }
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            SerializableMap map = bundle.getParcelable("serializableMap");
            if (map != null && map.getMap() != null) {
                stringStringMap = map.getMap();
                updateModel("orderQuery", stringStringMap);
                return;
            }
            orderNo = intent.getStringExtra("orderNo");
        }
        presenter = new PaymentDetailPresenter(this);

        pDialog = ProgressDialog.show(this, null, "请稍等...", true, false);

        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setCancelable(true);
        presenter.queryOrderDetail(orderNo);

        if(EnvironmentConfig.isPos()){
            IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(),this);
            helper.init(this);
        }

        currency.setMaximumFractionDigits(2);
        currency.setMinimumFractionDigits(2);

    }

    @Override
    protected void initPermission() {

    }

    private void setPayContent(Map<String, String> map) {
        setTitleCenterText(getResources().getString(R.string.pay_info));
        setTitleBackButton();
        labelTradeNo.setText("交易单号：");
        tradeAmt = map.get("orderAmount");
        merName = Utilities.getShowMerName(this);
        orderStatus = map.get("orderStatus");
        orderType = map.get("orderType");
        payType = map.get("payType");
        bizType = map.get("bizType");
        cashier = map.get("operator");
        origOrderNo = map.get("origOrderNo");
        tradeNo = map.get("orderNo");
        tradeTime = map.get("orderTime");
        onlineRefunds = map.get("onlineRefunds");
        tvTradeAmt.setText(StringUtil.append(SymbolConstant.RMB, tradeAmt));
        tvMerName.setText(merName);
        if ("success".equals(orderStatus)) {
            tvPayStatus.setTextColor(ContextCompat.getColor(this, R.color.color_success));
            tvPayStatus.setText("支付成功");
        } else if ("failure".equals(orderStatus)) {
            tvPayStatus.setTextColor(ContextCompat.getColor(this, R.color.red_2));
            tvPayStatus.setText("支付失败");
        } else if ("process".equals(orderStatus)) {
            tvPayStatus.setText("支付中");
        }

        if ("01".equals(payType)) {
            tvPayType.setText("支付宝");
        } else if ("02".equals(payType)) {
            tvPayType.setText("微信支付");
        } else if ("03".equals(payType)) {
            tvPayType.setText("银联支付");
        }
        if ("01".equals(bizType)) {
            tvProduct.setText("码夹App");
        } else if ("02".equals(bizType)) {
            tvProduct.setText("固定收款码");
        } else if ("03".equals(bizType)) {
            tvProduct.setText("派派小盒");
        } else if ("04".equals(bizType)) {
            tvProduct.setText("POS机");
        } else if ("05".equals(bizType)) {
            tvProduct.setText("德克收银机");
        } else if ("06".equals(bizType)) {
            tvProduct.setText("信用宝PC客户端");
        }
        tvCashier.setText(cashier);
        if (!TextUtils.isEmpty(origOrderNo)) {
            tvOrigOrderNo.setText(origOrderNo);
            rlOrigOrderNo.setVisibility(View.VISIBLE);
        }
        tvTradeNo.setText(tradeNo);
        tvTradeTime.setText(tradeTime);

        orderTime = DateUtils.getStringTime(tradeTime, "yyyy-MM-dd HH:mm:ss");
        nowTime = System.currentTimeMillis();
        ThirtyDaysTime = DateUtils.addDaysForMillisecond(orderTime, 30);

        if (StringUtil.equals("success", orderStatus)) {
            setTitleRightText(getResources().getString(R.string.pay_logout), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DateUtils.getNumberOfDaysBetween(orderTime, nowTime) < 1) {
                        intent = new Intent(PaymentDetailActivity.this, RefundActivity.class);
                        intent.putExtra(TRADE_AMT, tradeAmt);
                        intent.putExtra(PAY_TYPE, payType);
                        intent.putExtra(TRADE_NO, tradeNo);
                        intent.putExtra(PROC_CD, "200000");
                        startActivity(intent);
                    } else if ((DateUtils.getNumberOfDaysBetween(orderTime, nowTime) > 1
                            && DateUtils.getNumberOfDaysBetween(orderTime, nowTime) < 30)
                            || DateUtils.getNumberOfDaysBetween(orderTime, nowTime) == 30) {
                        if (StringUtil.isEmpty(onlineRefunds) && StringUtil.equals("true", onlineRefunds)) {
                            intent = new Intent(PaymentDetailActivity.this, RefundActivity.class);
                            intent.putExtra(TRADE_AMT, tradeAmt);
                            intent.putExtra(PAY_TYPE, payType);
                            intent.putExtra(TRADE_NO, tradeNo);
                            intent.putExtra(PROC_CD, "500000");
                            startActivity(intent);
                        } else {
                            initDialog(1);
                        }
                    } else if (DateUtils.getNumberOfDaysBetween(orderTime, nowTime) > 30) {
                        initDialog(3);
                    }
                }
            });
        }

    }

    //退款状态下 refundOrderNo 对应的是 交易单号  tradeNo 对应 关联单号
    /**
     "payCompany" -> "收单机构"
     "refundFlag" -> "N"
     * @param map
     */
    private void setRefundContent(Map<String, String> map) {
        setTitleCenterText("退款详情");
        setTitleBackButton();

        rlRefundAmount.setVisibility(View.VISIBLE);
        tvLabelPayStatus.setText(getResources().getString(R.string.refund_status));
        tvLabelPayType.setText(getResources().getString(R.string.refund_type));
        labelTradeNo.setText(getResources().getString(R.string.refund_num));
        labelTradeTime.setText(getResources().getString(R.string.refund_date));

        refundAmt = map.get("orderAmount");
        tradeAmt = map.get("originalOrderAmount");
        merName = Utilities.getShowMerName(this);
        orderStatus = map.get("refundStatus");
        orderType = map.get("orderType");
        cashier = map.get("operator");
        origOrderNo = map.get("origOrderNo");
        refundOrderNo = map.get("origOrderNo");
        payType = map.get("payType");
        tradeNo = map.get("orderNo");
        refundNo = map.get("refundOrderNo");
        tradeTime = map.get("orderTime");
        refundTime = map.get("orderExpireTime");
        bizType = map.get("bizType");
        onlineRefunds = map.get("onlineRefunds");
        tvTradeAmt.setText(StringUtil.append(SymbolConstant.RMB, tradeAmt));
        tvMerName.setText(merName);
        tvRefundAmount.setText(StringUtil.append(SymbolConstant.RMB, refundAmt));
        if ("success".equals(orderStatus)) {
            tvPayStatus.setTextColor(ContextCompat.getColor(this, R.color.color_success));
            tvPayStatus.setText("退款成功");
        } else if ("failure".equals(orderStatus)) {
            tvPayStatus.setTextColor(ContextCompat.getColor(this, R.color.red_2));
            tvPayStatus.setText("退款失败");
        } else if ("process".equals(orderStatus)) {
            tvPayStatus.setText("退款中");
        }
        tvTradeNo.setText(tradeNo);

        if ("01".equals(payType)) {
            tvPayType.setText("支付宝");
        } else if ("02".equals(payType)) {
            tvPayType.setText("微信支付");
        } else if ("03".equals(payType)) {
            tvPayType.setText("银联支付");
        }

        tvCashier.setText(cashier);
        if (!TextUtils.isEmpty(origOrderNo)) {
            tvOrigOrderNo.setText(origOrderNo);
            rlOrigOrderNo.setVisibility(View.VISIBLE);
        }
//        if ("majia".equals(bizType)) {
//            tvProduct.setText("码夹");
//        } else if ("merH5".equals(bizType)) {
//            //
//        } else if ("box".equals(bizType)) {
//            tvProduct.setText("派派小盒");
//        }
        if ("01".equals(bizType)) {
            tvProduct.setText("码夹App");
        } else if ("02".equals(bizType)) {
            tvProduct.setText("固定收款码");
        } else if ("03".equals(bizType)) {
            tvProduct.setText("派派小盒");
        } else if ("04".equals(bizType)) {
            tvProduct.setText("POS机");
        } else if ("05".equals(bizType)) {
            tvProduct.setText("德克收银机");
        } else if ("06".equals(bizType)) {
            tvProduct.setText("信用宝PC客户端");
        }

//        tvTradeNo.setText(refundNo);
        tvTradeTime.setText(tradeTime);
    }

    private void setPayBottom(Map<String, String> map) {
        if ("Y".equals(map.get("refundFlag"))) {
            btnRefund.setVisibility(View.VISIBLE);
            btnRefund.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_btn_refund, null));
        }
    }

    private void setRefundBottom(Map<String, String> map) {
//        btnCheckOrder.setVisibility(View.VISIBLE);
//        btnCheckOrder.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_btn_refund, null));
    }

    private void setPOSRefundBottom(Map<String, String> map) {
//        btnPosPrint.setVisibility(View.VISIBLE);
//        btnPosPrint.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_btn_refund, null));
//        btnCheckOrder.setVisibility(View.VISIBLE);
//        btnCheckOrder.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_btn_print, null));
    }

    private void setPOSPayBottom(Map<String, String> map) {
//        if ("Y".equals(map.get("refundFlag"))) {
//            btnRefund.setVisibility(View.VISIBLE);
//            btnRefund.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_btn_refund, null));
//        }
        btnPosPrint.setVisibility(View.VISIBLE);
        btnPosPrint.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.selector_btn_print, null));
    }

    @Override
    public void updateModel(String key, Object data) {
        //orderQueryErr
        Map<String, String> respData = (Map<String, String>) data;
        stringStringMap = respData;
        if (respData == null) {
            key = "exception";
        }
        if (pDialog != null) {
            pDialog.hide();
        }
        switch (key) {
            case "orderQueryErr": {
                Toast.makeText(this, respData.get("respDesc"), Toast.LENGTH_SHORT).show();
                break;
            }
            case "orderQuery": {
                String orderType = respData.get("orderType");
                updateView(respData);
                break;
            }
            case "orderQueryFail": {
                Toast.makeText(this, respData.get("respDesc"), Toast.LENGTH_SHORT).show();
                break;
            }
            case "exception": {
                Toast.makeText(this, "连接超时，请稍后再试", Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }

    private void updateView(Map<String, String> map) {
        String orderType = map.get("orderType");
        String orderStatus = map.get("orderStatus");
        String terminalType = EnvironmentConfig.getTerminalType();
        switch (terminalType) {
            case "pos":
                if ("pay".equals(orderType)) {
                    setPayContent(map);
                    if ("success".equals(orderStatus)) {
                        setPOSPayBottom(map);
                    }
                } else if ("refund".equals(orderType)) {
                    setRefundContent(map);
                    if ("success".equals(orderStatus)) {
                        setPOSRefundBottom(map);
                    }
                }
                break;
            case "mobile":
                if ("pay".equals(orderType)) {
                    setPayContent(map);
//                    if ("success".equals(orderStatus)) {
//                        setPayBottom(map);
//                    }
                } else if ("refund".equals(orderType)) {
                    setRefundContent(map);
                    if ("success".equals(orderStatus)) {
                        setRefundBottom(map);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refund:
                String amountStr = tvTradeAmt.getText().toString();
                String tradeNo = tvTradeNo.getText().toString();
                showDialog(tradeNo, amountStr);
                break;
            case R.id.btn_pos_print:
                terminalNo = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(),PaymentDetailActivity.this).getTerminalNo();
                showDialog(stringStringMap);
                break;
            case R.id.btn_check_order:
                Intent intent = new Intent(this, PaymentDetailActivity.class);
                intent.putExtra("orderNo", stringStringMap.get("origOrderNo"));
                startActivity(intent);
                break;
            case R.id.orig_order_no_copy: {
                ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvOrigOrderNo.getText());
                Toast.makeText(this, "已复制单号", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.trade_no_copy: {
                ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvTradeNo.getText());
                Toast.makeText(this, "已复制单号", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    private void showDialog(String tradeNo, String amount) {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_refund_method, null);//获取自定义布局
        final RadioGroup rdRefundMethod = (RadioGroup) layout.findViewById(R.id.rd_refund_method);
        TextView cancel = (TextView) layout.findViewById(R.id.btnCancel);
        TextView sure = (TextView) layout.findViewById(R.id.btnSure);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String refundMethod = "";
                @IdRes int checkId = rdRefundMethod.getCheckedRadioButtonId();
                if (checkId == R.id.check_cash) {
                    String refundAmt =  tvTradeAmt.getText().toString().replace(SymbolConstant.RMB,"");
                    Log.d("refundAmt", "refundAmt=" + refundAmt);
                    presenter.orderRefund(orderNo,refundAmt);
                } else if (checkId == R.id.check_return) {
                    presenter.orderRefund(orderNo, tvTradeAmt.getText().subSequence(0,1).toString());
                }
//                if (TextUtils.isEmpty(refundMethod)) {
//                    Toast.makeText(PaymentDetailActivity.this, "请选择退款方式", Toast.LENGTH_SHORT).show();
//                }

                //TODO
                dialog.hide();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void showDialog(Map<String, String> dataMap) {
        LayoutInflater inflater = this.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_pre_print, null);//获取自定义布局
        TextView tvMerName = (TextView) layout.findViewById(R.id.tv_mer_name);
        TextView tvMerNo = (TextView) layout.findViewById(R.id.tv_mer_no);
        TextView tvTerminalNo = (TextView) layout.findViewById(R.id.tv_terminal_no);
        TextView tvTradeAmount = (TextView) layout.findViewById(R.id.tv_trade_amount);
        TextView tvPayType = (TextView) layout.findViewById(R.id.tv_pay_type);
        TextView tvOrigTradeNo = (TextView) layout.findViewById(R.id.tv_ass_trade_no);
        TextView tvTradeNo = (TextView) layout.findViewById(tv_trade_no);
        TextView tvTradeTime = (TextView) layout.findViewById(R.id.tv_trade_time);
        TextView tvShopSite = (TextView) layout.findViewById(R.id.tv_shop_site);
        RelativeLayout rlOrigOrderNo = (RelativeLayout) layout.findViewById(R.id.rl_orig_order_no);


        final String amount = stringStringMap.get("orderAmount");
        final String merCode = stringStringMap.get("merCode");
        BigDecimal decimal = new BigDecimal(amount);
        String payType = stringStringMap.get("payType");
        if ("01".equals(payType)) {
            payType = "支付宝";
        } else if ("02".equals(payType)) {
            payType = "微信支付";
        } else if ("03".equals(payType)) {
            payType = "银联支付";
        }
        tvPayType.setText(payType);
        final String payTypeStr = payType;
        final String merName = Utilities.getShowMerName(this);
        final String nickName = Utilities.getNickName(this);

        if (!TextUtils.isEmpty(merName)) {
            tvMerName.setText(merName);
        } else {
            tvMerName.setText(nickName);
        }
        final String tradeAmount = currency.format(decimal);
        tvTradeAmount.setText(tradeAmount);
        tvTerminalNo.setText(terminalNo);
        tvMerNo.setText(merCode);
        final String origOrderNo = stringStringMap.get("origOrderNo");
        if (!TextUtils.isEmpty(origOrderNo)) {
            rlOrigOrderNo.setVisibility(View.VISIBLE);
            tvOrigTradeNo.setText(origOrderNo);
        }
        final String orderTime = stringStringMap.get("orderTime");
        final String shopSite = stringStringMap.get("address");
        tvTradeNo.setText(orderNo);
        tvTradeTime.setText(orderTime);
        tvShopSite.setText(shopSite);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton("确定打印", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(), PaymentDetailActivity.this);
                List<String> contentList = new ArrayList<>();
                contentList.add(PrintFormat.transJson(3, "", 1).toString());
                contentList.add(PrintFormat.transJson(3
                        , StringUtil.append("\t\t\t\t\t\t\t\t\t\t", getResources().getString(R.string.sign_the_order))
                        , 1
                        , PrintConstant.Align.CENTER).toString());
                contentList.add(PrintFormat.transJson(2, "", 2).toString());
                contentList.add(PrintFormat.transJson(2
                        , StringUtil.append(getResources().getString(R.string.merchant_name), Utilities.getShowMerName(PaymentDetailActivity.this))
                        , 0).toString());
                contentList.add(PrintFormat.transJson(2
                        , StringUtil.append(getResources().getString(R.string.merchant_num), StringUtil.toString(merCode))
                        , 0).toString());
                contentList.add(PrintFormat.transJson(2
                        , StringUtil.append(getResources().getString(R.string.terminal_num), StringUtil.toString(terminalNo))
                        , 0).toString());
                contentList.add(PrintFormat.transJson(2
                        , StringUtil.append(getResources().getString(R.string.terminal_amount), StringUtil.toString(amount), getResources().getString(R.string.yuan))
                        , 0).toString());
                contentList.add(PrintFormat.transJson(2
                        , StringUtil.append(getResources().getString(R.string.pay_type), StringUtil.toString(payTypeStr))
                        , 0).toString());
                if(!TextUtils.isEmpty(origOrderNo)){
                    contentList.add(PrintFormat.transJson(2, StringUtil.append(getResources().getString(R.string.associated_order), origOrderNo), 0).toString());
                }
                contentList.add(PrintFormat.transJson(2, StringUtil.append(getResources().getString(R.string.deal_order), orderNo), 0).toString());
                contentList.add(PrintFormat.transJson(2, StringUtil.append(getResources().getString(R.string.terminal_date), orderTime), 0).toString());
                contentList.add(PrintFormat.transJson(2, "- - - - - - - - - - - - - - - -", 1).toString());
                contentList.add(PrintFormat.transJson(2
                        , StringUtil.append(getResources().getString(R.string.pay_service_support), getResources().getString(R.string.service_num))
                        , 0).toString());
                contentList.add(PrintFormat.transJson(2, "", 2).toString());
                contentList.add(PrintFormat.transJson(2, "", 2).toString());
                helper.printReceipt(contentList,PaymentDetailActivity.this);
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
        //设置button样式
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.btn_cancel));
    }

    /**
     * 提示信息dialog
     * @param type 1：超过一天但未超过三十天  2：呼叫电话  3：超过三十天
     */
    private void initDialog(final int type) {
        if (type == 1 || type == 2) {
            dialog = new CenterDialog(this, R.layout.center_dialog, new int[] {R.id.cancel, R.id.confirm});
        } else if (type == 3) {
            dialog = new CenterDialog(this, R.layout.dialog_refund_fail, new int[] {R.id.btn_confirm});
        }
        dialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.confirm:
                        if (type == 1) {
                            hideDialog();
                            initDialog(2);
                        } else if (type == 2) {
                            dialPhoneNumber(getResources().getString(R.string.service_num));
                            hideDialog();
                        }
                        break;
                    case R.id.cancel:
                        hideDialog();
                        break;
                    case R.id.btn_confirm:
                        hideDialog();
                        break;
                }
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        if (type == 1) {
            TextView tv_content = (TextView) dialog.findViewById(R.id.content);
            TextView tv_cancel = (TextView) dialog.findViewById(R.id.cancel);
            TextView tv_confirm = (TextView) dialog.findViewById(R.id.confirm);
            tv_content.setText(getResources().getString(R.string.need_to_make_a_phone_call));
            tv_cancel.setText(getResources().getString(R.string.i_know));
            tv_confirm.setText(getResources().getString(R.string.contact_now));
        } else if (type == 2) {
            TextView tv_content = (TextView) dialog.findViewById(R.id.content);
            TextView tv_cancel = (TextView) dialog.findViewById(R.id.cancel);
            TextView tv_confirm = (TextView) dialog.findViewById(R.id.confirm);
            tv_content.setText(getResources().getString(R.string.service_num));
            tv_cancel.setText(getResources().getString(R.string.dismiss));
            tv_confirm.setText(getResources().getString(R.string.call_phone));
        } else if (type == 3) {
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
            View titleLine = dialog.findViewById(R.id.title_line);
            Button btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
            tv_title.setVisibility(View.GONE);
            titleLine.setVisibility(View.GONE);
            tv_content.setText(getResources().getString(R.string.deal_Was_over_thirty_days));
            btn_confirm.setText(getResources().getString(R.string.i_know));
        }
    }

    private void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public void dialPhoneNumber(String phoneNumber) {
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if(Config.isMobile()) {
            if (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
