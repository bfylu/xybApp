package cn.payadd.majia.activity.refund;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.utils.Utilities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.activity.PaymentDetailActivity;
import cn.payadd.majia.activity.merpwd.SetOrResetMerchantPwdActivity;
import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.constant.ApplyActivityContainer;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.device.pos.IPosHelper;
import cn.payadd.majia.device.pos.PosDeviceUtil;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.MerchantPwdPresenter;
import cn.payadd.majia.util.CenterDialog;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.PasswordInputView;

import static com.fdsj.credittreasure.constant.Constants.MINE_FRAGMENT;
import static com.fdsj.credittreasure.constant.Constants.PAY_TYPE;
import static com.fdsj.credittreasure.constant.Constants.PROC_CD;
import static com.fdsj.credittreasure.constant.Constants.SET_OR_CHANGE_MERCHANT_PWD_A_ACTIVITY;
import static com.fdsj.credittreasure.constant.Constants.TRADE_AMT;
import static com.fdsj.credittreasure.constant.Constants.TRADE_NO;
import static com.fdsj.credittreasure.constant.Constants.WHERE_GO;

/**
 * Created by lang on 2018/5/8.
 */

public class RefundActivity extends BaseActivity implements TextWatcher, View.OnClickListener, IActivity {

    private static final String TAG = "RefundActivity";

    @BindView(R.id.tv_refund_order)
    TextView tv_refundOrder;
    @BindView(R.id.tv_order_amount)
    TextView tv_orderAmount;
    @BindView(R.id.et_refund_amount)
    EditText et_refundAmount;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    private String refundAmount, tradeAmt, payType, tradeNo, procCd;
    private CenterDialog pwdDialog, inputPwdDialog, refundFailDialog, progressDialog;
    private Intent intent;
    private String merPwd;//商户管理密码
    private BigDecimal refundBigD;
    private Gson gson;
    private Type type;

    private MerchantPwdPresenter merchantPwdPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund;
    }

    @Override
    public void initView() {
        if (ApplyActivityContainer.refundAct == null) {
            ApplyActivityContainer.refundAct = new LinkedList<>();
        }
        ApplyActivityContainer.refundAct.add(this);

        setTitleCenterText(getResources().getString(R.string.pay_logout));
        setTitleBackButton();

        tradeAmt = getIntent().getStringExtra(TRADE_AMT);
        payType = getIntent().getStringExtra(PAY_TYPE);
        tradeNo = getIntent().getStringExtra(TRADE_NO);
        procCd = getIntent().getStringExtra(PROC_CD);
        et_refundAmount.addTextChangedListener(this);
        btn_confirm.setOnClickListener(this);
        et_refundAmount.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        merchantPwdPresenter = new MerchantPwdPresenter(this);

        tv_refundOrder.setText(tradeNo);
        tv_orderAmount.setText(StringUtil.append(tradeAmt, getResources().getString(R.string.yuan)));
        et_refundAmount.setText(tradeAmt);
        et_refundAmount.setSelection(et_refundAmount.getText().toString().length());
    }

    @Override
    protected void initPermission() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                refundAmount = et_refundAmount.getText().toString().trim();
                if (StringUtil.equals(refundAmount, ".") || 0 == StringUtil.decimal2Double(refundAmount)) {
                    ToastUtils.showShortToast(this, getResources().getString(R.string.refund_amount_not_zero));
                    et_refundAmount.setText("");
                    return;
                }
                if (StringUtil.toDouble(tradeAmt) < StringUtil.toDouble(refundAmount)) {
                    ToastUtils.showShortToast(this, getResources().getString(R.string.refund_amount_too_much));
                    et_refundAmount.setText("");
                    return;
                }
                initInputPwdDialog(refundAmount);
                break;
            case R.id.et_refund_amount:
                showSoftInputFromWindow(this, et_refundAmount);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (StringUtil.isEmpty(et_refundAmount.getText().toString().trim())) {
            btn_confirm.setEnabled(false);
            btn_confirm.setBackground(getResources().getDrawable(R.drawable.shape_btn_no_enable));
        } else {
            btn_confirm.setEnabled(true);
            btn_confirm.setBackground(getResources().getDrawable(R.drawable.shape_btn_enable));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 判断是否有设置商户管理密码，如果没有设置则跳出该dialog
     */
    private void initPwdDialog() {
        pwdDialog = new CenterDialog(this, R.layout.center_dialog, new int[] {R.id.cancel, R.id.confirm});
        pwdDialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.confirm:
                        intent = new Intent(RefundActivity.this, SetOrResetMerchantPwdActivity.class);
                        intent.putExtra(WHERE_GO, MINE_FRAGMENT);
                        startActivity(intent);
                        hidePwdDialog();
                        break;
                    case R.id.cancel:
                        hidePwdDialog();
                        finish();
                        break;
                }
            }
        });
        pwdDialog.setCanceledOnTouchOutside(true);
        pwdDialog.show();
        TextView tv_content = (TextView) pwdDialog.findViewById(R.id.content);
        TextView tv_cancel = (TextView) pwdDialog.findViewById(R.id.cancel);
        TextView tv_confirm = (TextView) pwdDialog.findViewById(R.id.confirm);
        tv_content.setText(getResources().getString(R.string.must_have_mer_pwd));
        tv_cancel.setText(getResources().getString(R.string.i_know));
        tv_confirm.setText(getResources().getString(R.string.go_setting));
    }

    /**
     * 输入密码dialog
     * @param refundAmount 退款金额
     */
    private void initInputPwdDialog(final String refundAmount) {
        et_refundAmount.setFocusable(false);

        inputPwdDialog = new CenterDialog(this, R.layout.dialog_input_pwd, new int[]{R.id.iv_close, R.id.tv_forget_pwd});
        inputPwdDialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.iv_close:
                        hideInputPwdDialog();
                        break;
                    case R.id.tv_forget_pwd:
                        intent = new Intent(RefundActivity.this, SetOrResetMerchantPwdActivity.class);
                        intent.putExtra(WHERE_GO, SET_OR_CHANGE_MERCHANT_PWD_A_ACTIVITY);
                        startActivity(intent);
                        hideInputPwdDialog();
                        break;
                }
            }
        });
        inputPwdDialog.show();
        inputPwdDialog.setCanceledOnTouchOutside(true);
        TextView tv_amount = (TextView) inputPwdDialog.findViewById(R.id.tv_amount);
        final PasswordInputView mPasswordInputView = (PasswordInputView) inputPwdDialog.findViewById(R.id.passwordInputView);
        final String amount = StringUtil.decimal2String(refundAmount);
        tv_amount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(amount)));


        //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        inputPwdDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //加上下面这一行弹出对话框时软键盘随之弹出
        inputPwdDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        showSoftInputFromWindow(this, mPasswordInputView);

        mPasswordInputView.setOnFinishListener(new PasswordInputView.OnFinishListener() {
            @Override
            public void setOnPasswordFinished() {
                if (mPasswordInputView.getOriginText().length() == mPasswordInputView.getMaxPasswordLength()) {
                    merPwd = mPasswordInputView.getOriginText();
                    refund(new BigDecimal(amount));
                }
            }
        });
    }

    /**
     * 退款
     */
    private void refund(BigDecimal refundBd) {
        if (StringUtil.equals("01", payType) || StringUtil.equals("02", payType)) {//支付宝退款
            merchantPwdPresenter.orderRefund(tradeNo, refundBd.toString(), merPwd);
        } else if (StringUtil.equals("03", payType)) {//银联支付
            refundBigD = refundBd;
            merchantPwdPresenter.SetMerchantPwd(Utilities.getMerCode(this), Utilities.getUsernameForLogin(this), 2, "", "", merPwd);
        }
        showProgressDialog();
        hideInputPwdDialog();
    }

    /**
     * 退款失败dialog
     */
    private void initRefundFailDialog(String failureReason) {
        refundFailDialog = new CenterDialog(this, R.layout.dialog_refund_fail, new int[] {R.id.btn_confirm});
        refundFailDialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_confirm:
                        hideRefundFailDialog();
                        break;
                }
            }
        });
        refundFailDialog.show();
        refundFailDialog.setCanceledOnTouchOutside(true);
        TextView tv_content = (TextView) refundFailDialog.findViewById(R.id.tv_content);
        if (StringUtil.isEmpty(failureReason)) {
            failureReason = "未知原因";
        }
        tv_content.setText(StringUtil.append(getResources().getString(R.string.failure_reason), failureReason));
    }

    private void showProgressDialog() {
        progressDialog = new CenterDialog(this, R.layout.dialog_progress, new int[] {});
        progressDialog.show();
    }

    /**
     * 关闭InputPwdDialog
     */
    private void hideInputPwdDialog() {
        if (inputPwdDialog != null) {
            inputPwdDialog.dismiss();
            inputPwdDialog = null;
        }
    }

    /**
     * 关闭RefundFailDialog
     */
    private void hideRefundFailDialog() {
        if (refundFailDialog != null) {
            refundFailDialog.dismiss();
            refundFailDialog = null;
        }
    }

    /**
     * 关闭PwdDialog
     */
    private void hidePwdDialog() {
        if (pwdDialog != null) {
            pwdDialog.dismiss();
            pwdDialog = null;
        }
    }

    /**
     * 关闭progressDialog
     */
    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        Map<String, String> respData;
        String respCode;
        String respDesc;
        String refundOrderNo;
        switch (key) {
            case "orderRefund":
                hideProgressDialog();
                respData = (Map<String, String>) data;
                respCode = respData.get("respCode");
                respDesc = respData.get("respDesc");
                refundOrderNo = respData.get("refundOrderNo");

                if ((StringUtil.equals("0000", respCode) || StringUtil.equals("000000", respCode))
                        && ApplyActivityContainer.refundAct != null) {
                    for (Activity act : ApplyActivityContainer.refundAct) {
                        act.finish();
                    }
                    intent = new Intent(this, PaymentDetailActivity.class);
                    intent.putExtra("orderNo", refundOrderNo);
                    startActivity(intent);

                    Intent mIntent = new Intent();
                    mIntent.setAction("com.fdsj.credittreasure.fragment.FlowFragment");
                    sendBroadcast(mIntent);
                } else {
                    initRefundFailDialog(respDesc);
                }
                break;
            case "setMerchantPwd":
                respData = (Map<String, String>) data;
                respCode = respData.get("respCode");
                respDesc = respData.get("respDesc");
                if (StringUtil.equals("000000", respCode)) {
                    IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(), RefundActivity.this);
                    helper.init(this);

                    helper.refundForCard(refundBigD, tradeNo, procCd, RefundActivity.this);
                }
                break;
            case "refundUpdate":
                respData = (Map<String, String>) data;
                respCode = respData.get("respCode");
                respDesc = respData.get("respDesc");
                if (StringUtil.equals("000000", respCode) && ApplyActivityContainer.refundAct != null) {
                    for (Activity act : ApplyActivityContainer.refundAct) {
                        act.finish();
                    }
                    intent = new Intent(this, PaymentDetailActivity.class);
                    intent.putExtra("orderNo", respData.get("refundOrderNo"));
                    startActivity(intent);

                    Intent mIntent = new Intent();
                    mIntent.setAction("com.fdsj.credittreasure.fragment.FlowFragment");
                    sendBroadcast(mIntent);
                } else {
                    ToastUtils.showShortToast(this, respDesc);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        if (requestCode == 1 && bundle != null) {
            switch (resultCode) {
                // 退款成功
                case Activity.RESULT_OK:
                    String time = "";
                    String msgTp = bundle.getString("msg_tp");
                    if (gson == null) {
                        gson = new Gson();
                    }
                    if (type == null) {
                        type = new TypeToken<Map<String, String>>() {}.getType();
                    }
                    Map<String, String> map = gson.fromJson(StringUtil.toString(bundle.get("txndetail")), type);
                    if (TextUtils.equals(msgTp, "0210")) {

                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date date = format.parse(bundle.getString("time_stamp"));
                            time = format1.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        merchantPwdPresenter.refundUpdate(tradeNo, map.get("transamount"), time, "1");
                    }
                    break;
                // 退款取消
                case Activity.RESULT_CANCELED:
                    String reason = bundle.getString("reason");
                    if (reason != null) {
                        ToastUtils.showShortToast(this, reason);
                        hideProgressDialog();
                    }
                    break;

                default:
                    hideProgressDialog();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        merPwd = StringUtil.toString(Utilities.getMerPwd(this));
        if (StringUtil.equals("N", merPwd) || StringUtil.isEmpty(merPwd)) {
            initPwdDialog();
        }
    }
}
