package cn.payadd.majia.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.CalculationActivity;

import cn.payadd.majia.constant.SymbolConstant;

/**
 * Created by Administrator on 2017/07/03 0003.
 */

public class FreezeSuccessActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvOrderAmount,tvOrderNo,tvOrderTime;

    private Button btnContinue;
    @Override
    public int getLayoutId() {
        return R.layout.activity_freeze_succ;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText("收款成功");

        tvOrderAmount = (TextView) findViewById(R.id.tv_freeze_amount);
        tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);

        btnContinue = (Button) findViewById(R.id.btn_continue);

        btnContinue.setOnClickListener(this);
    }

    @Override
    public void initData() {
        String orderAmount = getIntent().getStringExtra("orderAmount");
        String orderNo = getIntent().getStringExtra("orderNo");
        String orderTime = getIntent().getStringExtra("orderTime");
        tvOrderAmount.setText(SymbolConstant.RMB+orderAmount);
        tvOrderNo.setText(orderNo);
        tvOrderTime.setText(orderTime);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue:
                Intent intent = new Intent(FreezeSuccessActivity.this, CalculationActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
