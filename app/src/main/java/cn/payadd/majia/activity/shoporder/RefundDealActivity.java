package cn.payadd.majia.activity.shoporder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import butterknife.BindView;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.util.CenterDialog;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.MyStepperIndicator;

/**
 * Created by lang on 2018/5/28.
 */

public class RefundDealActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_refund_type)
    ImageView ivRefundType;
    @BindView(R.id.tv_refund_type)
    TextView tvRefundType;
    @BindView(R.id.ll_deal_progress)
    RelativeLayout llDealProgress;
    @BindView(R.id.si_deal_progress)
    MyStepperIndicator siDealProgress;
    @BindView(R.id.ll_merchant_deal)
    LinearLayout llMerchantDeal;
    @BindView(R.id.rl_deal_time)
    RelativeLayout rlDealTime;
    @BindView(R.id.tv_deal_time)
    TextView tvDealTime;
    @BindView(R.id.tv_buyer_phone)
    TextView tvBuyerPhone;
    @BindView(R.id.tv_refund_amount)
    TextView tvRefundAmount;
    @BindView(R.id.tv_process_mode)
    TextView tvProcessMode;
    @BindView(R.id.tv_goods_state)
    TextView tvGoodsState;
    @BindView(R.id.tv_application_date)
    TextView tvApplicationDate;
    @BindView(R.id.tv_refund_reason)
    TextView tvRefundReason;
    @BindView(R.id.tv_refund_order_no)
    TextView tvRefundOrderNo;
    @BindView(R.id.tv_see_all_record)
    TextView tvSeeAllRecord;
    @BindView(R.id.ll_deal_application)
    LinearLayout llDealApplication;
    @BindView(R.id.tv_refuse_application)
    TextView tvRefuseApplication;
    @BindView(R.id.tv_agree_application)
    TextView tvAgreeApplication;

    private Intent mIntent;

    private CenterDialog mRefundDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_deal;
    }

    @Override
    public void initView() {
        setTitleCenterText(getResources().getString(R.string.deal_refund));
        setTitleBackButton();

        tvSeeAllRecord.setOnClickListener(this);
        tvRefuseApplication.setOnClickListener(this);
        tvAgreeApplication.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_see_all_record:
                break;
            case R.id.tv_refuse_application:
                mIntent = new Intent(this, RefuseRefundActivity.class);
                mIntent.putExtra("orderNo", "");
                startActivity(mIntent);
                break;
            case R.id.tv_agree_application:
                showRefundDialog("0.01");
                break;
        }
    }

    /**
     * 退款dialog
     * @param refundAmount 退款金额
     */
    private void showRefundDialog(String refundAmount) {
        mRefundDialog = new CenterDialog(this, R.layout.dialog_refund_fail, new int[] {R.id.btn_confirm});
        mRefundDialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_confirm:
                        hideRefundDialog();
                        break;
                }
            }
        });

        TextView tvTitle = (TextView) mRefundDialog.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) mRefundDialog.findViewById(R.id.tv_content);

        tvTitle.setText(getResources().getString(R.string.agree_buyer_refund));
        tvContent.setTextSize(30f);
        tvContent.setText(StringUtil.append("¥", tvContent));
    }

    private void hideRefundDialog() {
        if (mRefundDialog != null || mRefundDialog.isShowing()) {
            mRefundDialog.dismiss();
            mRefundDialog = null;
        }
    }

    @Override
    protected void initPermission() {

    }
}
