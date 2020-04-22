package cn.payadd.majia.activity.merpwd;

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
import static com.fdsj.credittreasure.constant.Constants.SET_OR_CHANGE_MERCHANT_PWD_A_ACTIVITY;
import static com.fdsj.credittreasure.constant.Constants.SET_OR_RESET_MERCHANT_PWD_ACTIVITY;
import static com.fdsj.credittreasure.constant.Constants.WHERE_GO;

/**
 * Created by lang on 2018/5/8.
 */

public class SetOrChangeMerchantPwdAActivity extends BaseActivity implements PasswordInputView.OnFinishListener, IActivity {

    @BindView(R.id.tv_pwd_type)
    TextView tv_pwdType;
    @BindView(R.id.passwordInputView)
    PasswordInputView mPasswordInputView;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    private int whereGo;
    private Intent intent;
    private String merPwd;
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
        if (whereGo == SET_OR_RESET_MERCHANT_PWD_ACTIVITY) {
            tv_pwdType.setText(getResources().getString(R.string.input_new_merchant_pwd));
        } else if (whereGo == MINE_FRAGMENT) {
            tv_pwdType.setText(getResources().getString(R.string.input_old_merchant_pwd));
            setTitleRightText(getResources().getString(R.string.forget_pwd), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(SetOrChangeMerchantPwdAActivity.this, SetOrResetMerchantPwdActivity.class);
                    intent.putExtra(WHERE_GO, SET_OR_CHANGE_MERCHANT_PWD_A_ACTIVITY);
                    startActivity(intent);
                }
            });
        }
        btn_confirm.setVisibility(View.GONE);

        mPasswordInputView.setOnFinishListener(this);
    }

    @Override
    protected void initData() {
        if (whereGo == MINE_FRAGMENT) {
            userName = Utilities.getUsernameForLogin(this);
            merCode = Utilities.getMerCode(this);
            merchantPwdPresenter = new MerchantPwdPresenter(this);
        }
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void setOnPasswordFinished() {
        if (mPasswordInputView.getOriginText().length() == mPasswordInputView.getMaxPasswordLength()) {
            hideSoftKeyboard(this);
            merPwd = mPasswordInputView.getOriginText();
            if (whereGo == MINE_FRAGMENT) {
                merchantPwdPresenter.SetMerchantPwd(merCode, userName, 2, "", "", merPwd);
            } else if (whereGo == SET_OR_RESET_MERCHANT_PWD_ACTIVITY) {
                intent = new Intent(SetOrChangeMerchantPwdAActivity.this, SetOrChangeMerchantPwdBActivity.class);
                intent.putExtra(MER_PWD, merPwd);
                intent.putExtra(WHERE_GO, whereGo);
                startActivity(intent);
            }
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
                    intent = new Intent(SetOrChangeMerchantPwdAActivity.this, SetOrChangeMerchantPwdBActivity.class);
                    intent.putExtra(MER_PWD, merPwd);
                    intent.putExtra(WHERE_GO, whereGo);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast(this, respDesc);
                }
                break;
        }
    }
}
