package com.fdsj.credittreasure.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.constant.Constants;
import com.utils.SharedPreferenceTool;
import com.utils.Utilities;

import butterknife.BindView;
import cn.payadd.majia.activity.merpwd.SetOrChangeMerchantPwdAActivity;
import cn.payadd.majia.activity.merpwd.SetOrResetMerchantPwdActivity;
import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.entity.aistore.FriendshipInfo;
import cn.payadd.majia.entity.aistore.UserInfo;
import cn.payadd.majia.im.tls.service.TlsBusiness;
import cn.payadd.majia.util.StringUtil;

import static com.fdsj.credittreasure.constant.Constants.MINE_FRAGMENT;
import static com.fdsj.credittreasure.constant.Constants.WHERE_GO;

/**
 * Created by zhengzhen.wang on 2017/4/28.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private SharedPreferences sp;

    @BindView(R.id.iv_receive_installment_push)
    ImageView ivReceiveInstallmentPush;

    @BindView(R.id.relative_play_close)
    RelativeLayout relativePlayClose;

    @BindView(R.id.iv_play_close)
    ImageView ivPlayClose;

    @BindView(R.id.relative_play_succ)
    RelativeLayout relativePlaySucc;

    @BindView(R.id.iv_play_succ)
    ImageView ivPlaySucc;

    @BindView(R.id.relative_play_all)
    RelativeLayout relativePlayAll;

    @BindView(R.id.iv_play_all)
    ImageView ivPlayAll;

    @BindView(R.id.relative_print_type)
    RelativeLayout relativePrintType;

    @BindView(R.id.relative_merPwd)
    RelativeLayout relativeMerPwd;

    @BindView(R.id.tv_print_type)
    TextView tvPrintType;

    @BindView(R.id.SignOut)
    Button SignOut;

    private static final String TAG = "MineFragment";

    //选择的小票打印联  0为单联 1为二联 2为三联
    private int printPosition;

    //选择的语音播报类型  0为关闭 1为播报收款成功  2为播报金额
    private int voicePsoition;

    private String merPwd;

    private Intent intent;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

        TextView tvTitle = (TextView) findViewById(R.id.title_title);
        tvTitle.setText("设置");
        View titleBack = findViewById(R.id.title_back);
        titleBack.setVisibility(View.VISIBLE);
        titleBack.setOnClickListener(this);

        if (EnvironmentConfig.isPos()) {
            relativePrintType.setVisibility(View.VISIBLE);
        } else {
            relativePrintType.setVisibility(View.GONE);
        }

        SignOut.setOnClickListener(this);

        ivReceiveInstallmentPush.setOnClickListener(this);
        sp = getSharedPreferences(getClass().getName(), Context.MODE_PRIVATE);
        boolean receiveInstallmentPush = sp.getBoolean("receiveInstallmentPush", true);
        setReceiveInstallmentPush(receiveInstallmentPush);

        relativePlayClose.setOnClickListener(this);
        relativePlaySucc.setOnClickListener(this);
        relativePlayAll.setOnClickListener(this);

        relativePrintType.setOnClickListener(this);
        relativeMerPwd.setOnClickListener(this);

        voicePsoition = SharedPreferenceTool.getInstance().getSaveShareInt(this
                , Constants.USER_INFO
                , Constants.VOICE_NOTIFICATION
                , 2);

        setPlayStatus(voicePsoition, false);

        printPosition = SharedPreferenceTool.getInstance().getSaveShareInt(this
                , Constants.USER_INFO
                , Constants.PRINT_TYPE_CHOOSE
                , 1);

        if (printPosition == 0) {
            tvPrintType.setText(getResources().getString(R.string.single_copy));
        } else if (printPosition == 1) {
            tvPrintType.setText(getResources().getString(R.string.double_copy));
        } else if (printPosition == 2) {
            tvPrintType.setText(getResources().getString(R.string.three_copy));
        }
    }

    @Override
    protected void initData() {
        merPwd = StringUtil.toString(Utilities.getMerPwd(this));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_receive_installment_push: {
                boolean receiveInstallmentPush = sp.getBoolean("receiveInstallmentPush", true);
                receiveInstallmentPush = !receiveInstallmentPush;
                CloudPushService pushService = PushServiceFactory.getCloudPushService();
                if(receiveInstallmentPush){
                    pushService.turnOnPushChannel(new CommonCallback() {
                        @Override
                        public void onSuccess(String s) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("receiveInstallmentPush", true);
                            editor.commit();
                            setReceiveInstallmentPush(true);
                        }

                        @Override
                        public void onFailed(String s, String s1) {
                        }
                    });
                }else{
                    pushService.turnOffPushChannel(new CommonCallback() {
                        @Override
                        public void onSuccess(String s) {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("receiveInstallmentPush", false);
                            editor.commit();
                            setReceiveInstallmentPush(false);
                        }

                        @Override
                        public void onFailed(String s, String s1) {
                        }
                    });
                }
                break;
            }
            case R.id.relative_play_close:
                setPlayStatus(0, true);
                break;
            case R.id.relative_play_succ:
                setPlayStatus(1, true);
                break;
            case R.id.relative_play_all:
                setPlayStatus(2, true);
                break;
            case R.id.relative_print_type:
                startActivityForResult(new Intent(this, PrintTypeActivity.class), Constants.SETTING_ACTIVITY);
                break;

            case R.id.relative_merPwd:
                intent = new Intent();
                if (StringUtil.equals(merPwd, "Y")) {
                    intent.setClass(this, SetOrChangeMerchantPwdAActivity.class);
                } else {
                    intent.setClass(this, SetOrResetMerchantPwdActivity.class);
                }
                intent.putExtra(WHERE_GO, MINE_FRAGMENT);
                startActivity(intent);
                break;
            case R.id.SignOut://退出登录
                CloudPushService pushService = PushServiceFactory.getCloudPushService();
                pushService.unbindAccount(new CommonCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d(TAG, "aliyun push -> unbindAccount success");
                    }

                    @Override
                    public void onFailed(String errorCode, String errorMessage) {
                        Log.d(TAG, "aliyun push -> unbindAccount failed -- errorcode:" +
                                errorCode + " -- errorMessage:" + errorMessage);
                    }
                });
                Utilities.delUserInfo(this);//清除用户信息
                if (!Utilities.getCheckBox(this)) {
                    Utilities.delUser(this);//清除登录信息 ，，用户密码和Token
                }
//                Utilities.delMerchantKey(activity);//清除密钥
//                BaseApplication.setMerchantKey("");
//                BaseApplication.setSessionToKen("");

                if (Utilities.isAgent(SettingActivity.this)) {
                    logout();
                }

                Intent intent = new Intent();
                intent.setClass(this, cn.payadd.majia.activity.LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.title_back: {
                onBackPressed();
                break;
            }
        }
    }

    /**
     * IM退出登录
     */
    private void logout(){
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        FriendshipInfo.getInstance().clear();
    }

    private void setPlayStatus(int position, boolean isSave) {
        if (position == 0) {
            ivPlayClose.setSelected(true);
            ivPlaySucc.setSelected(false);
            ivPlayAll.setSelected(false);
        } else if (position == 1) {
            ivPlaySucc.setSelected(true);
            ivPlayClose.setSelected(false);
            ivPlayAll.setSelected(false);
        } else if (position == 2) {
            ivPlayAll.setSelected(true);
            ivPlayClose.setSelected(false);
            ivPlaySucc.setSelected(false);
        }

        if (isSave) {
            SharedPreferenceTool.getInstance().SaveShare(SettingActivity.this
                    , Constants.USER_INFO
                    , Constants.VOICE_NOTIFICATION
                    , position);
        }
    }

    private void setReceiveInstallmentPush(boolean receiveInstallmentPush) {
        if (receiveInstallmentPush) {
            ivReceiveInstallmentPush.setImageDrawable(getResources().getDrawable(R.mipmap
                    .icon_on));
        } else {
            ivReceiveInstallmentPush.setImageDrawable(getResources().getDrawable(R.mipmap
                    .icon_off));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SETTING_ACTIVITY) {
            if (resultCode != RESULT_OK) {
                return;
            }
            tvPrintType.setText(data.getStringExtra(Constants.PRINT_TYPE_CHOOSE));
        }
    }
}
