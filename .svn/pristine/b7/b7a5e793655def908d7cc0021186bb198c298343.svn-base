package cn.payadd.majia.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.presenter.FundAuthPresenter;
import cn.payadd.majia.util.AuthCodeCountDownTimer;

/**
 * Created by df on 2017/6/20.
 *
 * @author ming.li
 */

public class BindAlipayFragment extends Fragment implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener, IFragment {

    private EditText etSafeCode, etAlipayUid, etAlipayAcc, etImgCode;

    private TextView tvGetSafeCode, tvCurrentMer;

    private ImageView ivClearName, ivClearAcc, ivCode,ivPidHelp;

    private FundAuthPresenter fundAuthPresenter;

    private Button btnBind;

    private RelativeLayout rlImgCodeArea;

    private AuthCodeCountDownTimer myCountDownTimer;

    private ProgressDialog pDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_bind_alipay,
                container, false);
        etSafeCode = (EditText) layout.findViewById(R.id.edit_safe_code);
        etAlipayUid = (EditText) layout.findViewById(R.id.edit_alipay_pid);
        etAlipayAcc = (EditText) layout.findViewById(R.id.edit_alipay_acc);
        etImgCode = (EditText) layout.findViewById(R.id.et_img_code);

        tvGetSafeCode = (TextView) layout.findViewById(R.id.text_get_safe_code);
        tvCurrentMer = (TextView) layout.findViewById(R.id.tv_current_mer);

        ivClearName = (ImageView) layout.findViewById(R.id.iv_clear_name);
        ivClearAcc = (ImageView) layout.findViewById(R.id.iv_clear_acc);
        ivCode = (ImageView) layout.findViewById(R.id.iv_code);
        ivPidHelp = (ImageView) layout.findViewById(R.id.PIDhelp);
        btnBind = (Button) layout.findViewById(R.id.btn_bind);

        rlImgCodeArea = (RelativeLayout) layout.findViewById(R.id.rl_imgcode_area);

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

        fundAuthPresenter = new FundAuthPresenter(getActivity(), this);
        //显示当前商户
        String mobilePhone = Utilities.getMobilePhone(getActivity());
//        String email = Utilities.getEmail(this);
        String email = Utilities.getEmail(getActivity());
        String merAccountNo;
        if (!TextUtils.isEmpty(mobilePhone)) {
            merAccountNo = mobilePhone;
        } else {
            merAccountNo = email;
        }
        tvCurrentMer.setText(merAccountNo);
        return layout;
    }

    @Override
    public void onClick(View v) {
        String username = Utilities.getUsernameForLogin(getActivity());
        switch (v.getId()) {
            case R.id.text_get_safe_code:
                myCountDownTimer = new AuthCodeCountDownTimer(60000, 1000, tvGetSafeCode);
                myCountDownTimer.start();

                String sendNo = tvCurrentMer.getText().toString();
                String viewCode = etImgCode.getText().toString();
                if (!TextUtils.isEmpty(viewCode)) {
                    fundAuthPresenter.sendAuthCode(username, sendNo, viewCode);
                } else {
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
                final String safeCode = etSafeCode.getText().toString();
                final String alipayUid = etAlipayUid.getText().toString();
                final String alipayAcc = etAlipayAcc.getText().toString();

                showDialog(alipayUid, alipayAcc, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sendNo = tvCurrentMer.getText().toString();
                        //确定点击事件
                        bindAlipayAcc(sendNo, safeCode, alipayUid, alipayAcc);
                    }
                });

                break;
            case R.id.iv_code:
                //刷新图形验证码
                username = Utilities.getUsernameForLogin(getActivity());
                fundAuthPresenter.showImageCode(username, ivCode);
                break;
            case R.id.PIDhelp:
                showPIDHelp();
            default:
                break;
        }
    }

    private void showDialog(String alipayUid, String alipayAcc, DialogInterface.OnClickListener onClickListener) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_bind_account, null);//获取自定义布局
        TextView tvAlipayUid = (TextView) layout.findViewById(R.id.tv_bind_name);
        TextView tvAliayAcc = (TextView) layout.findViewById(R.id.tv_alipay_acc);
        tvAlipayUid.setText(alipayUid);
        tvAliayAcc.setText(alipayAcc);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //设置button样式
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        negativeButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.btn_cancel));
    }
    private void showPIDHelp() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_pid_help, null);//获取自定义布局
        final TextView tvPidUrl = (TextView) layout.findViewById(R.id.tv_pid_url);
        final TextView tvClipboard= (TextView) layout.findViewById(R.id.clipboard);
        final Button btnDismiss = (Button) layout.findViewById(R.id.btn_dialog_dismiss);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvPidUrl.getText());
                Toast.makeText(getActivity(), getResources().getString(R.string.copied), Toast.LENGTH_LONG).show();
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

    private void bindAlipayAcc(String sendNo, String safeCode, String alipayUid, String alipayAcc) {
        String username = Utilities.getUsernameForLogin(getActivity());
        pDialog = ProgressDialog.show(getActivity(), null, "请稍等...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        pDialog.setCanceledOnTouchOutside(false);
        fundAuthPresenter.bindAlipayAcc(username,sendNo, safeCode, alipayUid, alipayAcc);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etSafeCode.length() > 0 && (!etAlipayUid.getText().toString().trim().equals("")) && (!etAlipayAcc.getText().toString().trim().equals(""))) {
            //设置按钮可点击
            btnBind.setEnabled(true);

        } else {
            btnBind.setEnabled(false);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edit_alipay_pid:
                if (hasFocus) {
                    ivClearName.setVisibility(View.VISIBLE);
                } else {
                    ivClearName.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.edit_alipay_acc:
                if (hasFocus) {
                    ivClearAcc.setVisibility(View.VISIBLE);
                } else {
                    ivClearAcc.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {

        switch (key) {
            case "authCode": {
                Map<String, String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                String respDesc = respData.get("respDesc");
                String json = respData.get("data");
                boolean viewGraphCode = false;
                try {
                    if (!TextUtils.isEmpty(json)) {
                        JSONObject jsonObject = new JSONObject(json);
                        viewGraphCode = jsonObject.getBoolean("viewGraphCode");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ("000047".equals(respCode)) {
                    int visibility = rlImgCodeArea.getVisibility();
                    if (viewGraphCode) {
                        if (View.GONE == visibility) {
                            String username = Utilities.getUsernameForLogin(getActivity());
                            fundAuthPresenter.showImageCode(username, ivCode);
                            rlImgCodeArea.setVisibility(View.VISIBLE);
                            if (myCountDownTimer != null) {
                                myCountDownTimer.cancel();
                                tvGetSafeCode.setText("获取验证码");
                                tvGetSafeCode.setClickable(true);
                            }
                            Toast.makeText(getActivity(), "请输入图形验证码，再获取短信验证码", Toast.LENGTH_SHORT).show();
                        } else if (View.VISIBLE == visibility) {
                            Toast.makeText(getActivity(), respDesc, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if ("000000".equals(respCode)) {
                    int visibility = rlImgCodeArea.getVisibility();
                    if (viewGraphCode) {
                        if (View.GONE == visibility) {
                            String username = Utilities.getUsernameForLogin(getActivity());
                            fundAuthPresenter.showImageCode(username, ivCode);
                            rlImgCodeArea.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), respDesc, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case "bindAlipay": {
                Map<String, String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                String respDesc = respData.get("respDesc");
                if ("000000".equals(respCode)) {
                    //绑定成功
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    ListBindAccountFragment fragment = new ListBindAccountFragment();
                    transaction.replace(R.id.content_account, fragment);
                    transaction.commit();
                } else {
                    //绑定失败
                    Toast.makeText(getActivity(), respDesc, Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
                break;
            }
            default:
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
