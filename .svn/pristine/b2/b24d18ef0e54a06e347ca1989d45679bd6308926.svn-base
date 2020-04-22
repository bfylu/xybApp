package cn.payadd.majia.activity.merpwd;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.utils.Utilities;

import java.util.LinkedList;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.MerchantPwdPresenter;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.PasswordInputView;

import static cn.payadd.majia.constant.ApplyActivityContainer.willClearAct;
import static com.fdsj.credittreasure.constant.Constants.MER_PWD;
import static com.fdsj.credittreasure.constant.Constants.MINE_FRAGMENT;
import static com.fdsj.credittreasure.constant.Constants.SET_OR_RESET_MERCHANT_PWD_ACTIVITY;
import static com.fdsj.credittreasure.constant.Constants.WHERE_GO;

/**
 * Created by lang on 2018/5/8.
 */

public class SetOrChangeMerchantPwdBActivity extends BaseActivity implements PasswordInputView.OnFinishListener
        , View.OnClickListener, IActivity {

    @BindView(R.id.tv_pwd_type)
    TextView tv_pwdType;
    @BindView(R.id.passwordInputView)
    PasswordInputView mPasswordInputView;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    private int whereGo;
    private Intent intent;
    private String merPwd, confirmMerPwd;
    private String userName;
    private String merCode;

    private MerchantPwdPresenter merchantPwdPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_or_change_merchant_pwd;
    }

    @Override
    public void initView() {
        if (willClearAct == null) {
            willClearAct = new LinkedList<>();
        }
        willClearAct.add(this);

        setTitleCenterText(getResources().getString(R.string.set_merchant_pwd));
        setTitleBackButton();
        whereGo = getIntent().getIntExtra(WHERE_GO, 0);
        merPwd = getIntent().getStringExtra(MER_PWD);
        if (whereGo == SET_OR_RESET_MERCHANT_PWD_ACTIVITY) {
            tv_pwdType.setText(getResources().getString(R.string.input_merchant_pwd_again));
        } else if (whereGo == MINE_FRAGMENT) {
            tv_pwdType.setText(getResources().getString(R.string.input_new_merchant_pwd));
        }
        btn_confirm.setVisibility(View.VISIBLE);
        btn_confirm.setEnabled(false);
        btn_confirm.setOnClickListener(this);

        showSoftKeyboard(this, mPasswordInputView);
        mPasswordInputView.setOnFinishListener(this);
    }

    @Override
    protected void initData() {
        userName = Utilities.getUsernameForLogin(this);
        merCode = Utilities.getMerCode(this);
        merchantPwdPresenter = new MerchantPwdPresenter(this);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void setOnPasswordFinished() {
        if (mPasswordInputView.getOriginText().length() == mPasswordInputView.getMaxPasswordLength()) {
            hideSoftKeyboard(this);
            confirmMerPwd = mPasswordInputView.getOriginText();
            btn_confirm.setBackground(getResources().getDrawable(R.drawable.shape_btn_enable));
            btn_confirm.setEnabled(true);
        } else {
            btn_confirm.setBackground(getResources().getDrawable(R.drawable.shape_btn_no_enable));
            btn_confirm.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (StringUtil.isEmpty(confirmMerPwd)) {
                    ToastUtils.showShortToast(this, tv_pwdType.getText().toString());
                    return;
                }
                if (!StringUtil.equals(merPwd, confirmMerPwd) && whereGo == SET_OR_RESET_MERCHANT_PWD_ACTIVITY) {
                    ToastUtils.showShortToast(this, getResources().getString(R.string.two_input_are_inconsistent));
                    return;
                }

                if (whereGo == SET_OR_RESET_MERCHANT_PWD_ACTIVITY) {
                    merchantPwdPresenter.SetMerchantPwd(merCode, userName, 1, merPwd, confirmMerPwd, "");
                } else if (whereGo == MINE_FRAGMENT) {
                    merchantPwdPresenter.SetMerchantPwd(merCode, userName, 3, "", confirmMerPwd, "");
                }
                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        switch (key) {
            case "setMerchantPwd":
                Map<String, String> respData = (Map<String, String>) data;
                String respCode;
                String respDesc;
                respCode = respData.get("respCode");
                respDesc = respData.get("respDesc");
                if (StringUtil.equals("000000", respCode)) {
                    Utilities.saveMerPwd(confirmMerPwd, this);
                    for (Activity act : willClearAct) {
                        act.finish();
                    }
                }
                ToastUtils.showShortToast(this, respDesc);
                break;
        }
    }
}
