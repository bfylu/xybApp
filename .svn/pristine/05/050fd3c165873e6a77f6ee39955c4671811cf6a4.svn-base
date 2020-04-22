package cn.payadd.majia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.MainActivity;

import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.listener.NoDoubleClickListener;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by df on 2018/2/6.
 */

public class AcquireSuccessActivity extends BaseActivity{
    private TextView tvAcquireAmount,tvOrderNo,tvOrderTime;

    private Button btnContinue,btnExit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_acquire_succ;
    }

    @Override
    public void initView() {

        setTitleCenterText("收款成功");
        setTitleBackButton();
        tvAcquireAmount = (TextView) findViewById(R.id.tv_acquire_amount);
        tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        btnExit = (Button) findViewById(R.id.btn_return_main);
        btnContinue = (Button) findViewById(R.id.btn_continue);
        btnExit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent(AcquireSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnContinue.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
//                Intent intent = new Intent(AcquireSuccessActivity.this, CalculationActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            String orderAmt = data.getString("orderAmount");
            String orderDate = data.getString("orderDate");
            String orderNo = data.getString("orderNo");

            tvAcquireAmount.setText(StringUtil.append(SymbolConstant.RMB, orderAmt));
            tvOrderNo.setText(orderNo);
            tvOrderTime.setText(orderDate);
        }else{
            tvAcquireAmount.setText(StringUtil.append(SymbolConstant.RMB, getIntent().getStringExtra("orderAmount")));
            tvOrderNo.setText(getIntent().getStringExtra("orderNo"));
            tvOrderTime.setText(getIntent().getStringExtra("orderTime"));
        }

    }

    @Override
    protected void initPermission() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
