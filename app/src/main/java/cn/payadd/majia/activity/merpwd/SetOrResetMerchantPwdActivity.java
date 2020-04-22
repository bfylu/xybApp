package cn.payadd.majia.activity.merpwd;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.MerchantPwdPresenter;
import cn.payadd.majia.util.StringUtil;

import static cn.payadd.majia.constant.ApplyActivityContainer.willClearAct;
import static com.fdsj.credittreasure.constant.Constants.MINE_FRAGMENT;
import static com.fdsj.credittreasure.constant.Constants.SET_OR_CHANGE_MERCHANT_PWD_A_ACTIVITY;
import static com.fdsj.credittreasure.constant.Constants.SET_OR_RESET_MERCHANT_PWD_ACTIVITY;
import static com.fdsj.credittreasure.constant.Constants.WHERE_GO;

/**
 * Created by lang on 2018/5/7.
 */

public class SetOrResetMerchantPwdActivity extends BaseActivity implements TextWatcher, View.OnClickListener, IActivity {

    @BindView(R.id.tv_current_user)
    TextView tv_currentUser;
    @BindView(R.id.et_code)
    EditText et_code;
    @BindView(R.id.tv_get_code)
    TextView tv_getCode;
    @BindView(R.id.ll_img_code)
    LinearLayout ll_imgCode;
    @BindView(R.id.et_img_code)
    EditText et_imgCode;
    @BindView(R.id.iv_img_code)
    ImageView iv_imgCode;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;

    //获取验证码倒计时
    private CountDownTimer timer;
    private String userName, msgCode, msgCodeKey = "", imgCode, imgCodeKey, phoneNum, email;
    private String respCodeKey;
    private int whereGo;

    private MerchantPwdPresenter merchantPwdPresenter;

    private Intent intent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set_or_reset_merchant_pwd;
    }

    @Override
    public void initView() {
        if (willClearAct == null) {
            willClearAct = new LinkedList<>();
        }
        willClearAct.add(this);

        whereGo = getIntent().getIntExtra(WHERE_GO, 0);
        if (whereGo == SET_OR_CHANGE_MERCHANT_PWD_A_ACTIVITY) {
            setTitleCenterText(getResources().getString(R.string.reset_merchant_pwd));
        } else if (whereGo == MINE_FRAGMENT) {
            setTitleCenterText(getResources().getString(R.string.set_merchant_pwd));
        }

        setTitleBackButton();

        et_code.addTextChangedListener(this);
        tv_getCode.setOnClickListener(this);
        iv_imgCode.setOnClickListener(this);
        btn_confirm.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        userName = Utilities.getUsernameForLogin(this);
        merchantPwdPresenter = new MerchantPwdPresenter(this);
        //显示当前商户
        phoneNum = Utilities.getMobilePhone(this);
        email = Utilities.getEmail(this);
        if (StringUtil.isNotEmpty(phoneNum)){
            tv_currentUser.setText(phoneNum);
        } else if (StringUtil.isNotEmpty(email)) {
            tv_currentUser.setText(email);
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
        if (StringUtil.isEmpty(et_code.getText().toString().trim()) || StringUtil.isNotValidePwd(et_code.getText().toString().trim())) {
            btn_confirm.setBackground(getResources().getDrawable(R.drawable.shape_btn_no_enable));
            btn_confirm.setEnabled(false);
        } else {
            btn_confirm.setBackground(getResources().getDrawable(R.drawable.shape_btn_enable));
            btn_confirm.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_code:
                imgCode = et_imgCode.getText().toString().trim();
                if (StringUtil.isEmpty(phoneNum)) {
                    return;
                }
                if (StringUtil.isEmpty(userName)) {
                    return;
                }
               getCode(imgCode);
                break;
            case R.id.iv_img_code:
                if (StringUtil.isEmpty(userName)) {
                    return;
                }
                merchantPwdPresenter.showImageCode(userName,iv_imgCode);
                break;
            case R.id.btn_confirm:
                msgCode = et_code.getText().toString().trim();
                if (StringUtil.isEmpty(msgCode)) {
                    ToastUtils.showShortToast(this, getResources().getString(R.string.no_verify_code));
                    return;
                }
                merchantPwdPresenter.ValidationAuthCode(msgCodeKey, msgCode);
                break;
        }
    }

    private void getCode(String imgCode) {
        tv_getCode.setClickable(false);
        timer = new CountDownTimer(59 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_getCode.setTextColor(getResources().getColor(R.color.btn_unclickable));
                tv_getCode.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                tv_getCode.setClickable(true);
                tv_getCode.setTextColor(getResources().getColor(R.color.btn_clickable));
                tv_getCode.setText(getResources().getString(R.string.get_code_again));
                timer = null;
            }
        };

        if (StringUtil.isEmpty(imgCode)) {
            merchantPwdPresenter.sendAuthCode(this, phoneNum, "1", null, null);
        } else {
            merchantPwdPresenter.sendAuthCode(this, phoneNum, "1", imgCodeKey, imgCode);
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        Map<String, String> respData = (Map<String, String>) data;
        String respCode;
        String respDesc;
        String json;

        switch (key) {
            //获取短信验证码返回
            case "authCode" :
                respCode = respData.get("respCode");
                respCodeKey = respData.get("phoneMsgCodeKey");
                respDesc = respData.get("respDesc");
                json = respData.get("data");

                msgCodeKey = respCodeKey;

                boolean viewGraphCode = false;
                try {
                    if(!TextUtils.isEmpty(json)){
                        JSONObject jsonObject = new JSONObject(json);
                        viewGraphCode = jsonObject.getBoolean("viewGraphCode");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (StringUtil.equals("0000", respCode) || StringUtil.equals("000000", respCode)) {
                    if (viewGraphCode) {
                        if (ll_imgCode.getVisibility() == View.GONE
                                || ll_imgCode.getVisibility() == View.INVISIBLE) {
                            //获取图形验证码
                            merchantPwdPresenter.showImageCode(userName,iv_imgCode);
                            ll_imgCode.setVisibility(View.VISIBLE);
                        }
                    }
                    timer.start();
                } else if (StringUtil.equals("0047", respCode) || StringUtil.equals("000047", respCode)) {
                    if (viewGraphCode) {
                        if (ll_imgCode.getVisibility() == View.GONE
                                || ll_imgCode.getVisibility() == View.INVISIBLE) {
                            merchantPwdPresenter.showImageCode(userName,iv_imgCode);
                            ll_imgCode.setVisibility(View.VISIBLE);
                            ToastUtils.showShortToast(this, getResources().getString(R.string.input_img_code));
                        } else {
                            ToastUtils.showShortToast(this, respDesc);
                        }
                    }
                    timer.start();
                } else {
                    tv_getCode.setClickable(true);
                    ToastUtils.showShortToast(this, respDesc);
                }
                break;
            //验证验证码返回
            case "validation":
                respCode = respData.get("respCode");
                respDesc = respData.get("respDesc");
                if (StringUtil.equals("000000", respCode)) {
                    intent = new Intent(this, SetOrChangeMerchantPwdAActivity.class);
                    intent.putExtra(WHERE_GO, SET_OR_RESET_MERCHANT_PWD_ACTIVITY);
                    startActivity(intent);
                } else {
                    ToastUtils.showShortToast(this, respDesc);
                    et_code.setText("");
                    et_imgCode.setText("");
                }
                break;
        }
    }
}
