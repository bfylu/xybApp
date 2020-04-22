package cn.payadd.majia.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.ILogin;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.MainActivity;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.huawei.android.pushagent.PushManager;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.utils.LogUtil;
import com.utils.SharedPreferenceTool;
import com.utils.Utilities;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.payadd.majia.activity.aistore.DialogActivity;
import cn.payadd.majia.entity.aistore.IMSigBean;
import cn.payadd.majia.entity.aistore.UserInfo;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.aistore.IIMSig;
import cn.payadd.majia.im.presentation.business.LoginBusiness;
import cn.payadd.majia.im.presentation.event.FriendshipEvent;
import cn.payadd.majia.im.presentation.event.MessageEvent;
import cn.payadd.majia.im.presentation.event.RefreshEvent;
import cn.payadd.majia.im.presentation.presenter.SplashPresenter;
import cn.payadd.majia.im.presentation.viewfeatures.SplashView;
import cn.payadd.majia.im.tls.service.TLSService;
import cn.payadd.majia.presenter.LoginPresenter;
import cn.payadd.majia.presenter.aistore.IMLoginPresenter;
import cn.payadd.majia.task.DownloadImageTask;
import cn.payadd.majia.util.Center3Dialog;
import cn.payadd.majia.util.aistore.PushUtil;
import cn.payadd.majia.view.CommonDialog;
import rx.functions.Action1;

/**
 * Created by df on 2017/9/1.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, ILogin, SplashView, IIMSig, TIMCallBack, IActivity {

    private static String LOG_TAG = "LoginActivity";

    private EditText etUsername, etPassword, etCheckCode;

    private Button btnLogin;

    private ImageView ivUserClear, ivPasswordClear, ivCheckCode;

    private CheckBox cbPassword;

    private RelativeLayout rlCheckCode;

    private TextView tvRegister, tvForget;
    LoginPresenter presenter;

    private SplashPresenter mSplashPresenter;

    private IMLoginPresenter imLoginPresenter;

    private boolean isVerify = false;
    private String verifyCodeUrl;

    private Center3Dialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login2;
    }

    @Override
    public void initView() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        etCheckCode = (EditText) findViewById(R.id.et_check_code);

        ivUserClear = (ImageView) findViewById(R.id.iv_user_clear);
        ivPasswordClear = (ImageView) findViewById(R.id.iv_password_clear);
        ivCheckCode = (ImageView) findViewById(R.id.iv_check_code);
        cbPassword = (CheckBox) findViewById(R.id.checkBox);
        btnLogin = (Button) findViewById(R.id.btn_login);

        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvForget = (TextView) findViewById(R.id.tv_forget);
        rlCheckCode = (RelativeLayout) findViewById(R.id.rl_check_code);

        ivUserClear.setOnClickListener(this);
        ivPasswordClear.setOnClickListener(this);
        ivCheckCode.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        cbPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        boolean checkboxIsCheck = SharedPreferenceTool.getInstance().GetSaveShareBoolean(this,
                "User", "checkboxPassword");//, checkboxPassword.isChecked());
        cbPassword.setChecked(checkboxIsCheck);
        presenter = new LoginPresenter(this);
        imLoginPresenter = new IMLoginPresenter(this, this);

        setTitle("登录");
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etUsername.getText().toString().isEmpty()) {
                    ivUserClear.setVisibility(View.GONE);
                } else {
                    ivUserClear.setVisibility(View.VISIBLE);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPassword.getText().toString().isEmpty()) {
                    ivPasswordClear.setVisibility(View.GONE);
                } else {
                    ivPasswordClear.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void initData() {
        String userName = Utilities.getUsernameForLogin(this);
        if (!TextUtils.isEmpty(userName)) {
            etUsername.setText(userName);
        }
        String passWord = Utilities.getPassWord(this);
        if (!TextUtils.isEmpty(userName)) {
            etPassword.setText(passWord);
        }
        RxView.clicks(btnLogin).throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String userName = etUsername.getText().toString().trim();//获取账号
                        if (!TextUtils.isEmpty(userName)) {
                            String passWord = etPassword.getText().toString().trim();//获取密码
                            if (!TextUtils.isEmpty(passWord)) {
                                if (isVerify == true) {
                                    String verifyCode = etCheckCode.getText().toString().trim();
                                    if (!TextUtils.isEmpty(verifyCode)) {
                                        presenter.userLogin("", userName, passWord, LoginActivity
                                                        .this,
                                                true, verifyCode);//登录
                                    } else {
                                        CommonDialog commonDialog = new CommonDialog
                                                (LoginActivity.this
                                                        , getResources().getString(R.string.no_verify_code)
                                                        , getResources()
                                                .getString(R.string.hint)
                                                        , null
                                                        , null);
                                        commonDialog.showDialog();
                                    }
                                } else {
                                    presenter.userLogin("", userName, passWord, LoginActivity.this,
                                            true);//登录
                                }

                            } else {
                                Toast.makeText(LoginActivity.this,getResources().getString(R.string.hint),Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this,getResources().getString(R.string.no_user),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                break;
            case R.id.iv_user_clear:
                etUsername.setText("");
                break;
            case R.id.iv_password_clear:
                etPassword.setText("");
                break;
            case R.id.iv_check_code:
                new DownloadImageTask(ivCheckCode, etUsername.getText().toString().trim())
                        .execute(verifyCodeUrl);
                break;
            case R.id.checkBox:
                SharedPreferenceTool.getInstance().SaveShare(LoginActivity.this
                        , "User"
                        , "checkboxPassword"
                        , cbPassword.isChecked());
                LogUtil.info("CheckBoxPassWord", cbPassword.isChecked() + "");
                break;
            case R.id.tv_register: {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_forget: {
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    /**
     * 登录成功回调
     */
    @Override
    public void userLogin(String loginToken, String username, String usernameForLogin, String passWord,
                          String mobilePhone, String nickName, String shopName, String welCome,
                          String sessionToken, String mposKey, String showMerName, String email,
                          String agentFlag, String isAuth,String ismtNoticeCount,String merCode,String goodSelectionUrl, String appSecurity) {
        Utilities.setUserInfo(this, loginToken, username, usernameForLogin, passWord,
                mobilePhone, nickName, shopName, welCome, sessionToken, mposKey, showMerName,
                cbPassword.isChecked(), email, agentFlag, isAuth,ismtNoticeCount,merCode,goodSelectionUrl, appSecurity);
        BaseApplication.setMerchantKey(mposKey);
        BaseApplication.setSessionToKen(sessionToken);

        if (Utilities.isAgent(this)) {
            mSplashPresenter = new SplashPresenter(this);
            mSplashPresenter.start();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void userLoginError(String message, Map<String, String> respData) {
        if (respData != null) {
            String respCode = respData.get("respCode");
            if (respCode.equals("000066") || respCode.equals("000034")) {
                isVerify = true;
                rlCheckCode.setVisibility(View.VISIBLE);
                verifyCodeUrl = respData.get("verifyCodeUrl");
                new DownloadImageTask(ivCheckCode, etUsername.getText().toString().trim())
                        .execute(verifyCodeUrl);

            }
        }
        DialogFactory.showAlertDialog(this, getResources().getString(R.string.hint), message);
    }

    private long exitTime;// 退出时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //ToastUtils.showToast(this, getResources().getString(R.string.once_again));
                Toast.makeText(this,getResources().getString(R.string.once_again),Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void updateModel(String key, Object data) {

    }

    @Override
    public void getIMSig(IMSigBean data) {
        if (data.getCode() == 0) {

            UserInfo.getInstance().setId(data.getData().getIdentifier());
            UserInfo.getInstance().setUserSig(data.getData().getUsersig());
            navToHome();
        }
    }

    @Override
    public void navToHome() {
//登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(LOG_TAG, "receive force offline message");
                Intent intent = new Intent(LoginActivity.this, DialogActivity.class);
                startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                showDialog(getString(R.string.tls_expire), 1);

            }}).setConnectionListener(new TIMConnListener() {
            @Override
            public void onConnected() {
                Log.i(LOG_TAG, "onConnected");
            }

            @Override
            public void onDisconnected(int code, String desc) {
                Log.i(LOG_TAG, "onDisconnected");
            }

            @Override
            public void onWifiNeedAuth(String name) {
                Log.i(LOG_TAG, "onWifiNeedAuth");
            }
        });

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
//        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), this);
    }

    @Override
    public void navToLogin() {
        imLoginPresenter.accountIM(Utilities.getMerCode(this), Utilities.getShowMerName(this), "https://ums.payadd.cn/images/shop.png");
    }

    @Override
    public boolean isUserLogin() {
        return UserInfo.getInstance().getId() != null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));
    }

    @Override
    public void onError(int i, String s) {
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                showDialog(getString(R.string.kick_logout), 2);
                break;
            case 6200:
//                Toast.makeText(this,getString(R.string.login_error_timeout),Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
            default:
//                Toast.makeText(this,getString(R.string.login_error),Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
        }
    }

    @Override
    public void onSuccess() {
//初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        String deviceMan = android.os.Build.MANUFACTURER;
        //注册小米和华为推送
        if (deviceMan.equals("Xiaomi") && shouldMiInit()){
            MiPushClient.registerPush(BaseApplication.getAppContext(), "2882303761517826564", "5711782626564");
        }else if (deviceMan.equals("HUAWEI")){
            PushManager.requestToken(this);
        }

        //魅族推送只适用于Flyme系统,因此可以先行判断是否为魅族机型，再进行订阅，避免在其他机型上出现兼容性问题
//        if(MzSystemUtils.isBrandMeizu(getApplicationContext())){
//            com.meizu.cloud.pushsdk.PushManager.register(this, "112662", "3aaf89f8e13f43d2a4f97a703c6f65b3");
//        }

//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "refreshed token: " + refreshedToken);
//
//        if(!TextUtils.isEmpty(refreshedToken)) {
//            TIMOfflinePushToken param = new TIMOfflinePushToken(169, refreshedToken);
//            TIMManager.getInstance().setOfflinePushToken(param, null);
//        }
//        MiPushClient.clearNotification(getApplicationContext());
        Toast.makeText(this, "IM登录成功", Toast.LENGTH_SHORT).show();
    }

    private void showDialog(String str, final int type) {
        dialog = new Center3Dialog(this, R.layout.center_dialog, new int[] {R.id.cancel, R.id.confirm});
        dialog.show();

        dialog.setOnCenterItemClickListener(new Center3Dialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(Center3Dialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.confirm:
                        if (type == 1) {
                            navToLogin();
                        } else {
                            navToHome();
                        }
                        break;
                }
            }
        });

        TextView tvContent = (TextView) dialog.findViewById(R.id.content);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.cancel);
        tvContent.setText(str);
        tvCancel.setVisibility(View.GONE);
    }

    /**
     * 判断小米推送是否已经初始化
     */
    private boolean shouldMiInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
