package cn.payadd.majia.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.BaseActivity;
import com.fdsj.credittreasure.activities.Flow2Activity;
import com.fdsj.credittreasure.activities.MainActivity;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.fdsj.credittreasure.fragment.BaseFragment;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import cn.payadd.majia.activity.FundAuthOrderActivity;
import cn.payadd.majia.activity.InstallmentOrderActivity;
import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.presenter.StatisticsPresenter;

public class StatisticsFragment extends BaseFragment implements View.OnClickListener, IFragment {

    public static boolean isFlush;

    private StatisticsPresenter statisticsPresenter;
    private Dialog choiceDialog;

    private TextView tvAquireTotalAmt, tvTransCount, tvRefundTotalAmt,
                    tvWxpayAmt, tvWxpayCount, tvAlipayAmt, tvAlipayCount, tvUnionpayAmt, tvUnionpayCount,
                    tvInstallmentTransAmt, tvInstallmentTransCount, tvInstallmentDownPaymentTotalAmt;
    private TextView tvPreTotalAmt,tvPreSettleCount,tvPreWaitSettleTotalAmt;
    private Date startDate, endDate;
    private PayPushBroadcastReceiver receiver;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_statistics;
    }


    @Override
    protected void initView() {
        ((MainActivity)getActivity()).setTitleVisible(true);
        ((BaseActivity) activity).setRightText("筛选", this);
        activity.findViewById(R.id.tl_show_detail).setOnClickListener(this);
        activity.findViewById(R.id.ll_show_installment_detail).setOnClickListener(this);
        activity.findViewById(R.id.ll_show_pre_detail).setOnClickListener(this);

        tvAquireTotalAmt = (TextView) activity.findViewById(R.id.aquire_total_amt);
        tvTransCount = (TextView) activity.findViewById(R.id.tv_trans_count);
        tvRefundTotalAmt = (TextView) activity.findViewById(R.id.tv_refund_total_amt);
        tvWxpayAmt = (TextView) activity.findViewById(R.id.tv_wxpay_amt);
        tvWxpayCount = (TextView) activity.findViewById(R.id.tv_wxpay_count);
        tvAlipayAmt = (TextView) activity.findViewById(R.id.tv_alipay_amt);
        tvAlipayCount = (TextView) activity.findViewById(R.id.tv_alipay_count);
        tvUnionpayAmt = (TextView) activity.findViewById(R.id.tv_unionpay_amt);
        tvUnionpayCount = (TextView) activity.findViewById(R.id.tv_unionpay_count);
        tvInstallmentTransAmt = (TextView) activity.findViewById(R.id.tv_installment_total_amt);
        tvInstallmentTransCount = (TextView) activity.findViewById(R.id.tv_installment_trans_count);
        tvInstallmentDownPaymentTotalAmt = (TextView) activity.findViewById(R.id.tv_installment_down_payment_total_amt);

        tvPreTotalAmt = (TextView) activity.findViewById(R.id.tv_pre_total_amt);
        tvPreSettleCount = (TextView) activity.findViewById(R.id.tv_pre_settle_count);
        tvPreWaitSettleTotalAmt = (TextView) activity.findViewById(R.id.tv_pre_wait_settle_total_amt);
    }

    @Override
    protected void initData() {

        statisticsPresenter = new StatisticsPresenter(activity, this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
        receiver = new PayPushBroadcastReceiver(new PayPushBroadcastReceiver.Callback() {

            @Override
            public void exec(Bundle data) {

                if (isHidden()) {
                    isFlush = true;
                } else {
                    statisticsPresenter.query(startDate, endDate);
                }
            }
        });
        activity.registerReceiver(receiver, filter);
        statisticsPresenter.query(startDate, endDate);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tl_show_detail: {
                Intent intent = new Intent(activity, Flow2Activity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_show_installment_detail: {
                Intent intent = new Intent(activity, InstallmentOrderActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.ll_show_pre_detail:{
                Intent intent = new Intent(activity, FundAuthOrderActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.tv_right: {
                // 筛选
                if (null == choiceDialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    final String[] items = new String[]{"全部", "昨天", "近七天", "本月", "上月"};
                    builder.setTitle("筛选日期").setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (items[which]) {
                                case "全部": {
                                    startDate = null;
                                    endDate = null;
                                    statisticsPresenter.query(startDate, endDate);
                                    break;
                                }
                                case "昨天": {
                                    Calendar c = Calendar.getInstance();
                                    c.add(Calendar.DATE, -1);
                                    startDate = c.getTime();
                                    endDate = c.getTime();
                                    statisticsPresenter.query(startDate, endDate);
                                    break;
                                }
                                case "近七天": {
                                    Calendar c = Calendar.getInstance();
                                    c.add(Calendar.DATE, -7);
                                    startDate = c.getTime();
                                    endDate = new Date();
                                    statisticsPresenter.query(startDate, endDate);
                                    break;
                                }
                                case "本月": {
                                    Calendar c = Calendar.getInstance();
                                    c.set(Calendar.DATE, c.getMinimum(Calendar.DATE));
                                    startDate = c.getTime();
                                    endDate = new Date();
                                    statisticsPresenter.query(startDate, endDate);
                                    break;
                                }
                                case "上月": {
                                    Calendar c1 = Calendar.getInstance();
                                    c1.add(Calendar.MONTH, -1);
                                    c1.set(Calendar.DATE, c1.getMinimum(Calendar.DATE));
                                    Calendar c2 = Calendar.getInstance();
                                    c2.add(Calendar.MONTH, -1);
                                    c2.set(Calendar.DATE, c2.getMaximum(Calendar.DATE));
                                    startDate = c1.getTime();
                                    endDate = c2.getTime();
                                    statisticsPresenter.query(startDate, endDate);
                                    break;
                                }
                            }
                            dialog.dismiss();
                        }
                    });
                    choiceDialog = builder.create();
                }
                choiceDialog.show();
                break;
            }
        }
    }

    @Override
    public void updateModel(String key, Object data) {

        Map<String, String> map = (Map<String, String>) data;
        tvAquireTotalAmt.setText(map.get("totalAcquire"));
        tvTransCount.setText(map.get("totalTransCount"));
        tvRefundTotalAmt.setText(map.get("totalRefund"));
        tvInstallmentTransAmt.setText(map.get("totalInstallment"));
        tvInstallmentTransCount.setText(map.get("installmentCount"));
        tvInstallmentDownPaymentTotalAmt.setText(map.get("totalDownPayment"));
        tvPreTotalAmt.setText(map.get("preAuthAcquire"));
        tvPreSettleCount.setText(map.get("preAuthSettleCount"));
        tvPreWaitSettleTotalAmt.setText(map.get("freezeTotal"));
        try {
            JSONObject transDetail = new JSONObject(map.get("transDetail"));
            JSONObject wxpay = transDetail.getJSONObject("weixin");
            tvWxpayAmt.setText(wxpay.getString("transAmount"));
            tvWxpayCount.setText(wxpay.getString("transCount"));
            JSONObject alipay = transDetail.getJSONObject("alipay");
            tvAlipayAmt.setText(alipay.getString("transAmount"));
            tvAlipayCount.setText(alipay.getString("transCount"));
            JSONObject unionpay = transDetail.getJSONObject("unionpay");
            tvUnionpayAmt.setText(unionpay.getString("transAmount"));
            tvUnionpayCount.setText(unionpay.getString("transCount"));
            isFlush = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity)getActivity()).setTitleVisible(true);
            if (isFlush) {
                statisticsPresenter.query(startDate, endDate);
            }
            ((BaseActivity) activity).setRightText("筛选", this);
            activity.findViewById(R.id.tv_right).setVisibility(View.VISIBLE);
            if (isFlush) {
                statisticsPresenter.query(startDate, endDate);
            }
        } else {
            activity.findViewById(R.id.tv_right).setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(receiver);
    }
}