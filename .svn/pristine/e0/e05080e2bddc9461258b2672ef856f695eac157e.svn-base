package cn.payadd.majia.activity.shoporder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fdsj.credittreasure.R;
import com.lljjcoder.citylist.Toast.ToastUtils;

import butterknife.BindView;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by lang on 2018/5/28.
 */

public class RefuseRefundActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @BindView(R.id.et_input_refuse_reason)
    EditText etInputRefuseReason;
    @BindView(R.id.btn_refuse)
    Button btnRefuse;

    private String refuseReason;

    private String orderNo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refuse_refund;
    }

    @Override
    public void initView() {
        setTitleCenterText(getResources().getString(R.string.refuse_refund));
        setTitleBackButton();

        btnRefuse.setOnClickListener(this);
        etInputRefuseReason.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra("orderNo");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refuse:
                refuseReason = etInputRefuseReason.getText().toString().trim();
                if (StringUtil.isEmpty(refuseReason)) {
                    ToastUtils.showShortToast(this, getResources().getString(R.string.input_refuse_reason));
                    return;
                }
                ToastUtils.showShortToast(this, "点击拒绝申请按钮");
                break;
        }
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (StringUtil.isNotEmpty(etInputRefuseReason.getText().toString().trim())) {
            btnRefuse.setBackground(getResources().getDrawable(R.drawable.shape_btn_enable));
        } else {
            btnRefuse.setBackground(getResources().getDrawable(R.drawable.shape_btn_no_enable));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
