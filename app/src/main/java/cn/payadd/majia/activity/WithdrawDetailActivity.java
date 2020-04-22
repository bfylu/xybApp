package cn.payadd.majia.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;

import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.WithdrawPresenter;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.ViewTab;

/**
 * Created by df on 2017/9/16.
 */

public class WithdrawDetailActivity extends BaseActivity implements IActivity{
    private ImageView ivFirst,ivSecond,ivThird;
    private TextView tvWithdrawAmount,tvFee,tvBank,tvBankAccount,tvWithdrawNo,tvWithdrawDate;
    private TextView tvRejectReason,tvFirst,tvSecond,tvThird;
    private RelativeLayout rlReject;

    private ImageView ivLineLeft,ivLineRight,ivCopy;

    private WithdrawPresenter presenter;

    private String refuseReason;
    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw_detail;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText("提现详情");
        ivFirst = (ImageView) findViewById(R.id.iv_first);
        ivSecond = (ImageView) findViewById(R.id.iv_second);
        ivThird = (ImageView) findViewById(R.id.iv_third);
        tvFirst = (TextView) findViewById(R.id.tv_first);
        tvSecond = (TextView) findViewById(R.id.tv_second);
        tvThird = (TextView) findViewById(R.id.tv_third);
        tvWithdrawAmount = (TextView) findViewById(R.id.tv_withdraw_amount);
        tvFee = (TextView) findViewById(R.id.tv_fee);
        tvBank = (TextView) findViewById(R.id.tv_bank);
        tvBankAccount = (TextView) findViewById(R.id.tv_bank_account);
        tvWithdrawNo = (TextView) findViewById(R.id.tv_withdraw_no);
        tvWithdrawDate = (TextView) findViewById(R.id.tv_withdraw_date);
        tvRejectReason = (TextView) findViewById(R.id.tv_reject_reason);
        rlReject = (RelativeLayout) findViewById(R.id.rl_reject);

        ivLineRight = (ImageView) findViewById(R.id.iv_line_right);
        ivLineLeft = (ImageView) findViewById(R.id.iv_line_left);
        ivCopy = (ImageView) findViewById(R.id.iv_copy);

        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvWithdrawNo.getText());
                Toast.makeText(WithdrawDetailActivity.this, "已复制单号", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void initData() {
        presenter = new WithdrawPresenter(this);
        String issueNo = getIntent().getStringExtra("issueNo");
        presenter.withdrawDetail(issueNo);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void updateModel(String key, Object data) {
        if(AppService.WITHDRAW_DETAIL.equals(key)){
            Map<String,String> respData = (Map<String, String>) data;
            String respCode = respData.get("respCode");
            if("000000".equals(respCode)){
                tvWithdrawAmount.setText(SymbolConstant.RMB+respData.get("withdrawAmount"));
                tvFee.setText(SymbolConstant.RMB+respData.get("fee"));
                tvBank.setText(respData.get("bankName"));
                tvBankAccount.setText(respData.get("bankCardNo"));
                tvWithdrawNo.setText(respData.get("orderNo"));
                tvWithdrawDate.setText(respData.get("withdrawTime"));
                String status = respData.get("status");
                refuseReason = respData.get("remark");
                switch (status){
                    case "0":
                        ivFirst.setImageResource(R.mipmap.one_selected);
                        ivSecond.setImageResource(R.mipmap.two_selected);
                        ivThird.setImageResource(R.mipmap.three_normal);
                        ivLineLeft.setImageResource(R.mipmap.line_selected);
                        ivLineRight.setImageResource(R.mipmap.line_half_left);
                        if (StringUtil.isNotEmpty(refuseReason)) {
                            rlReject.setVisibility(View.VISIBLE);
                            tvRejectReason.setText(refuseReason);
                        } else {
                            rlReject.setVisibility(View.GONE);
                        }
                        break;
                    case "1":
                        ivFirst.setImageResource(R.mipmap.one_selected);
                        ivSecond.setImageResource(R.mipmap.two_selected);
                        ivThird.setImageResource(R.mipmap.three_selected);
                        ivLineLeft.setImageResource(R.mipmap.line_selected);
                        ivLineRight.setImageResource(R.mipmap.line_selected);
                        tvThird.setText("提现成功");
                        break;
                    case "2":
                        ivFirst.setImageResource(R.mipmap.one_selected);
                        ivSecond.setImageResource(R.mipmap.two_selected);
                        ivThird.setImageResource(R.mipmap.three_normal);
                        ivLineLeft.setImageResource(R.mipmap.line_selected);
                        ivLineRight.setImageResource(R.mipmap.line_half_left);
                        break;
                    case "3":
                        ivFirst.setImageResource(R.mipmap.one_selected);
                        ivSecond.setImageResource(R.mipmap.two_selected);
                        ivThird.setImageResource(R.mipmap.three_selected);
                        ivLineLeft.setImageResource(R.mipmap.line_selected);
                        ivLineRight.setImageResource(R.mipmap.line_selected);
                        rlReject.setVisibility(View.VISIBLE);
                        tvRejectReason.setText("");
                        tvThird.setText("提现失败");
                        break;
                }
            }
        }
    }
}
