package cn.payadd.majia.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.payadd.majia.constant.ActivityTitle;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.FundAuthPresenter;
import cn.payadd.majia.util.AuthCodeCountDownTimer;

/**
 * Created by df on 2017/6/20.
 */

public class UpdateAccountActivity extends BaseActivity implements View.OnClickListener,TextWatcher,View.OnFocusChangeListener,IActivity{

    private EditText etAlipayAcc,etAlipayUid,etSafeCode,etImgCode;

    private TextView tvGetSafeCode,tvCurrentMer;

    private ImageView ivClearName,ivClearAcc,ivCode,ivPidHelp;

    private FundAuthPresenter fundAuthPresenter;

    private Button btnBind;

    private ProgressDialog pDialog;

    private RelativeLayout rlImgCodeArea;

    private AuthCodeCountDownTimer myCountDownTimer;
    @Override
    public int getLayoutId() {
        return R.layout.activity_update_alipay_acc;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText(ActivityTitle.UPDATE_BIND_ACCOUNT);

        etSafeCode = (EditText) findViewById(R.id.edit_safe_code);
        etAlipayUid = (EditText) findViewById(R.id.edit_alipay_pid);
        etAlipayAcc = (EditText) findViewById(R.id.edit_alipay_acc);
        etImgCode = (EditText) findViewById(R.id.et_img_code);
        tvGetSafeCode = (TextView) findViewById(R.id.text_get_safe_code);
        tvCurrentMer = (TextView) findViewById(R.id.tv_current_mer);

        ivClearName = (ImageView) findViewById(R.id.iv_clear_name);
        ivClearAcc = (ImageView) findViewById(R.id.iv_clear_acc);
        ivCode = (ImageView) findViewById(R.id.iv_code);
        ivPidHelp = (ImageView) findViewById(R.id.PIDhelp);
        btnBind = (Button) findViewById(R.id.btn_bind);
        rlImgCodeArea = (RelativeLayout) findViewById(R.id.rl_imgcode_area);

        tvGetSafeCode.setOnClickListener(this);
        ivClearName.setOnClickListener(this);
        ivClearAcc.setOnClickListener(this);
        ivCode.setOnClickListener(this);
        ivPidHelp.setOnClickListener(this);
        btnBind.setOnClickListener(this);

        etSafeCode.addTextChangedListener(this);
        etAlipayUid.addTextChangedListener(this);
        etAlipayAcc.addTextChangedListener(this);
        btnBind.setEnabled(false);

        etAlipayUid.setOnFocusChangeListener(this);
        etAlipayAcc.setOnFocusChangeListener(this);

    }

    @Override
    public void initData() {
        String username = Utilities.getUsernameForLogin(this);
        fundAuthPresenter = new FundAuthPresenter(this);
        fundAuthPresenter.queryBindInfo(username);
        //显示当前商户
        String mobilePhone = Utilities.getMobilePhone(this);
        String email = Utilities.getEmail(this);
        String merAccountNo;
        if (!TextUtils.isEmpty(mobilePhone)){
            merAccountNo = mobilePhone;
        }else{
            merAccountNo = email;
        }
        tvCurrentMer.setText(merAccountNo);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        String username = Utilities.getUsernameForLogin(this);
        switch (v.getId()) {
            case R.id.text_get_safe_code:
                myCountDownTimer = new AuthCodeCountDownTimer(60000,1000,tvGetSafeCode);
                myCountDownTimer.start();

                String sendNo = tvCurrentMer.getText().toString();
                String viewCode = etImgCode.getText().toString();
                if(!TextUtils.isEmpty(viewCode)){
                    fundAuthPresenter.sendAuthCode(username, sendNo, viewCode);
                }else{
                    fundAuthPresenter.sendAuthCode(username, sendNo, null);
                }

                break;
            case R.id.iv_clear_acc:
                etAlipayAcc.setText("");
                break;
            case R.id.iv_clear_name:
                etAlipayUid.setText("");
                break;
            case R.id.btn_bind:
                //弹窗给出提示
                String safeCode = etSafeCode.getText().toString();
                String alipayUid = etAlipayUid.getText().toString();
                String alipayAcc = etAlipayAcc.getText().toString();
                pDialog = ProgressDialog.show(UpdateAccountActivity.this, null, "请稍等...", true, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(UpdateAccountActivity.this,"取消",Toast.LENGTH_SHORT).show();
                    }
                });
                pDialog.setCanceledOnTouchOutside(false);
                //确定点击事件
                fundAuthPresenter.bindAlipayAcc(username,tvCurrentMer.getText().toString(),safeCode,alipayUid,alipayAcc);
                break;
            case R.id.iv_code:
                //刷新图形验证码
                username = Utilities.getUsernameForLogin(this);
                fundAuthPresenter.showImageCode(username,ivCode);
                break;
            case R.id.PIDhelp:
                showPIDHelp();
                break;
            default:
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(etSafeCode.length()>0&&(!etAlipayUid.getText().toString().trim().equals(""))&&(!etAlipayAcc.getText().toString().trim().equals(""))){
            //设置按钮可点击
            btnBind.setEnabled(true);

        }else{
            btnBind.setEnabled(false);
        }
    }

    private void showPIDHelp() {
        LayoutInflater inflater = this.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_pid_help, null);//获取自定义布局
        final TextView tvPidUrl = (TextView) layout.findViewById(R.id.tv_pid_url);
        final TextView tvClipboard= (TextView) layout.findViewById(R.id.clipboard);
        final Button btnDismiss = (Button) layout.findViewById(R.id.btn_dialog_dismiss);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();

        tvPidUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(tvPidUrl.getText().toString()));
                startActivity(intent);
            }
        });

        tvClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvPidUrl.getText());
                Toast.makeText(UpdateAccountActivity.this, getResources().getString(R.string.copied), Toast.LENGTH_LONG).show();
            }
        });
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setTitle("支付宝PID从哪里获取");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //设置button样式

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.edit_alipay_pid:
                if(hasFocus) {
                    ivClearName.setVisibility(View.VISIBLE);
                }else{
                    ivClearName.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.edit_alipay_acc:
                if(hasFocus) {
                    ivClearAcc.setVisibility(View.VISIBLE);
                }else{
                    ivClearAcc.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        switch (key) {
            case "bindInfo": {
                Map<String, String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                String respDesc = respData.get("respDesc");
                if ("000000".equals(respCode)) {
                    String alipayUserId = respData.get("alipayUserId");
                    String alipayLogonId = respData.get("alipayLogonId");
                    etAlipayUid.setText(alipayUserId);
                    etAlipayAcc.setText(alipayLogonId);
                }
                break;
            }
            case "authCode": {
                Map<String, String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                String respDesc = respData.get("respDesc");
                String json = respData.get("data");
                boolean viewGraphCode = false;
                try {
                    if(!TextUtils.isEmpty(json)){
                        JSONObject jsonObject = new JSONObject(json);
                        viewGraphCode = jsonObject.getBoolean("viewGraphCode");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if("000047".equals(respCode)){
                    int visibility = rlImgCodeArea.getVisibility();
                    if(viewGraphCode) {
                        if (View.GONE == visibility) {
                            String username = Utilities.getUsernameForLogin(this);
                            fundAuthPresenter.showImageCode(username,ivCode);
                            rlImgCodeArea.setVisibility(View.VISIBLE);
                            if(myCountDownTimer!=null){
                                myCountDownTimer.cancel();
                                tvGetSafeCode.setText(getResources().getString(R.string.acc_btn_safe_code));
                                tvGetSafeCode.setClickable(true);
                            }
                            Toast.makeText(this, getResources().getString(R.string.input_img_code), Toast.LENGTH_SHORT).show();
                        } else if (View.VISIBLE == visibility) {
                            Toast.makeText(this, respDesc, Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if("000000".equals(respCode)){
                    int visibility = rlImgCodeArea.getVisibility();
                    if(viewGraphCode) {
                        if (View.GONE == visibility) {
                            String username = Utilities.getUsernameForLogin(this);
                            fundAuthPresenter.showImageCode(username,ivCode);
                            rlImgCodeArea.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    Toast.makeText(this, respDesc, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case "bindAlipay": {
                Map<String, String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                String respDesc = respData.get("respDesc");
                if("000000".equals(respCode)){
                    //绑定成功
                    Intent intent = new Intent(UpdateAccountActivity.this,AccountActivity.class);
                    startActivity(intent);
                }else {
                    //绑定失败
                    Toast.makeText(this, respDesc, Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
                break;
            }
        }
    }
}
