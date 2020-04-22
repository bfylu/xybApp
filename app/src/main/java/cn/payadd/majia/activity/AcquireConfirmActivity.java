package cn.payadd.majia.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;

import org.json.JSONObject;

import cn.payadd.majia.constant.AppService2;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.listener.NoDoubleClickListener;
import cn.payadd.majia.presenter.ScanPayPresenter;

/**
 * Created by df on 2018/1/22.
 */

public class AcquireConfirmActivity extends BaseActivity implements IActivity{
    private TextView tvAmount;

    private Button btnSubmit;

    private String orderAmount;
    private String authCode;
    private int bizType;
    private ScanPayPresenter payPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_acquire_confirm;
    }

    @Override
    public void initView() {
        setTitleCenterText("确认收款");
        setTitleBackButton();
        tvAmount = (TextView) findViewById(R.id.tv_order_amount);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                payPresenter.PayAcquireConfirm(authCode,bizType);
            }
        });
    }

    @Override
    public void initData() {
        orderAmount = getIntent().getStringExtra("orderAmount");
        authCode = getIntent().getStringExtra("authCode");
        bizType = getIntent().getIntExtra("bizType",0);
        if(TextUtils.isEmpty(orderAmount)){
            tvAmount.setText("");
        }
        tvAmount.setText(SymbolConstant.RMB + orderAmount);
        payPresenter = new ScanPayPresenter(this);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void updateModel(String key, Object data) {
        try {
            if(AppService2.ACQUIRE_CONFIRM.equals(key)){
                JSONObject respData  = (JSONObject) data;
                String respCode = null;

                respCode = respData.getString("respCode");
                if("000000".equals(respCode)){
                    JSONObject jsonData = respData.getJSONObject("data");
                    Intent intent = new Intent(AcquireConfirmActivity.this,AcquireSuccessActivity.class);
                    intent.putExtra("orderAmount",jsonData.getString("orderAmount"));
                    intent.putExtra("orderNo",jsonData.getString("orderNo"));
                    intent.putExtra("orderTime",jsonData.getString("orderTime"));
                    startActivity(intent);
                    finish();
                }else{
                   Toast.makeText(this,respData.getString("respDesc"),Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
