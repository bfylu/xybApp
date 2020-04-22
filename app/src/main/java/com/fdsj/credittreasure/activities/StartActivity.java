package com.fdsj.credittreasure.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.ILogin;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.huawei.android.pushagent.PushManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.activity.LoginActivity;
import cn.payadd.majia.activity.aistore.DialogActivity;
import cn.payadd.majia.constant.AppService2;
import cn.payadd.majia.entity.aistore.IMSigBean;
import cn.payadd.majia.entity.aistore.UserInfo;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.aistore.IIMSig;
import cn.payadd.majia.im.presentation.business.InitBusiness;
import cn.payadd.majia.im.presentation.business.LoginBusiness;
import cn.payadd.majia.im.presentation.event.FriendshipEvent;
import cn.payadd.majia.im.presentation.event.GroupEvent;
import cn.payadd.majia.im.presentation.event.MessageEvent;
import cn.payadd.majia.im.presentation.event.RefreshEvent;
import cn.payadd.majia.im.presentation.presenter.SplashPresenter;
import cn.payadd.majia.im.presentation.viewfeatures.SplashView;
import cn.payadd.majia.im.tls.service.TLSService;
import cn.payadd.majia.im.tls.service.TlsBusiness;
import cn.payadd.majia.presenter.LoginPresenter;
import cn.payadd.majia.presenter.VersionUpdatePresenter;
import cn.payadd.majia.presenter.aistore.IMLoginPresenter;
import cn.payadd.majia.util.Center3Dialog;
import cn.payadd.majia.util.aistore.PushUtil;


/**
 * Created by BXND on 2016-08-18.
 */
public class StartActivity extends BaseActivity implements ILogin, IActivity, SplashView, IIMSig, TIMCallBack {

    private static final String LOG_TAG = "StartActivity";

    private final int PERMISSION = 1;

    @BindView(R.id.line)
    RelativeLayout relativeLayout;

    LoginPresenter presenter;

    VersionUpdatePresenter updatePresenter;

    private SplashPresenter mSplashPresenter;

    private IMLoginPresenter imLoginPresenter;

    private Center3Dialog dialog;

    private boolean animationThread = false;
    private ProgressDialog pDialog;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        //Log.i("开始解压时间---", System.currentTimeMillis()+"");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        presenter = new LoginPresenter(this);
        updatePresenter = new VersionUpdatePresenter(this);
        Resources resources = this.getResources();
        LogUtil.info("现在读取的x100是", resources.getDimension(R.dimen.x100) + "屏幕宽度：" + Utilities.getScreenWidth(this));
        LogUtil.info("现在读取的y100是", resources.getDimension(R.dimen.y100) + "屏幕高度：" + Utilities.getScreenHeight(this));

        imLoginPresenter = new IMLoginPresenter(this, this);

        //解压资源包
       /* InputStream input = null;
        try {

            File resFile = new File(getApplicationContext().getExternalFilesDir(null),"app_res");
            if(!resFile.exists()){
                input = getAssets().open(ResourceConstants.RESOURCE_ZIP_NAME);
                File file = getApplicationContext().getExternalFilesDir(null);
                ZIP.UnZipFolderByInputStream(input,file.getAbsolutePath());
            }
            //读取资源包配置文件
            try {
                FileUtils.readResourceInfo(getApplicationContext().getExternalFilesDir(ResourceConstants.RES_INFO_PATH).getAbsolutePath(), "info.txt", this);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (pDialog == null) {
                pDialog = ProgressDialog.show(this, null, "正在检查更新...", true, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setCancelable(true);
            }
            pDialog.show();
            String userName = Utilities.getUsernameForLogin(this);
            updatePresenter.updateApp(userName);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //NewLogin(true);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this
//                    , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
//                            , Manifest.permission.READ_PHONE_STATE
//                            , Manifest.permission.CAMERA}, PERMISSION);
//        } else {
//            //Log.e("开始",System.currentTimeMillis()+"");
//            userLogin();
//        }

        initPermission();

    }

    private void initPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if ((checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)) permissionsList.add(Manifest.permission.CAMERA);

            if (permissionsList.size() == 0) {
                init();
                userLogin();
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        PERMISSION);
            }
        } else {
            init();
            userLogin();
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        userLogin();
        init();
    }

    private void init(){
        SharedPreferences pref = this.getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.OFF.ordinal());
        //初始化IMSDK
        InitBusiness.start(BaseApplication.getAppContext(), loglvl);
        //初始化TLS
        TlsBusiness.init(BaseApplication.getAppContext());
        String id =  TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
    }

    /**
     * 调用登录线程
     */
    public void userLogin() {
        String loginToken = Utilities.getLoginToken(this);
        String userName = Utilities.getUsernameForLogin(StartActivity.this);
        if (!TextUtils.isEmpty(loginToken) && !TextUtils.isEmpty(userName)) {
            presenter.userLogin(loginToken, userName, "", StartActivity.this, false);//登录请求
        } else {
            userLoginError("",null);
        }
    }

    public void myStartActivity() {

//        AnimationSet animationSet = new AnimationSet(true);
//        //参数1～2：x轴的开始位置
//        //参数3～4：y轴的开始位置
//        //参数5～6：x轴的结束位置
//        //参数7～8：x轴的结束位置
//
//
//        animationSet.addAnimation(getTranslateAnimation(2000));
//        animationSet.addAnimation(getAlphaAnimation());
//        animationSet.setFillAfter(true);
//        animationSet.setRepeatCount(1);
//
//
//        final AnimationSet animationSet1 = new AnimationSet(true);
//        animationSet1.addAnimation(getTranslateAnimation(2000));
//        animationSet1.addAnimation(getAlphaAnimation());
//        animationSet1.setFillAfter(true);
//        animationSet1.setRepeatCount(1);
//        animationSet1.setStartOffset(500);
//
//
//        final AnimationSet animationSet2 = new AnimationSet(true);
//        animationSet2.addAnimation(getTranslateAnimation(2000));
//        animationSet2.addAnimation(getAlphaAnimation());
//        animationSet2.setFillAfter(true);
//        animationSet2.setRepeatCount(1);
//        animationSet2.setStartOffset(800);
//
//        animationSet2.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                LogUtil.info("onAnimationEnd", "onAnimationEnd");
//                NewLogin(animationThread);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });

    }

//    public TranslateAnimation getTranslateAnimation(long l) {
//        TranslateAnimation translateAnimation =
//                new TranslateAnimation(
//                        Animation.RELATIVE_TO_SELF, 0f,
//                        Animation.RELATIVE_TO_SELF, 0f,
//                        Animation.RELATIVE_TO_SELF, -1f,
//                        Animation.RELATIVE_TO_SELF, 10f);
//        translateAnimation.setDuration(l);
//        return translateAnimation;
//    }
//
//    public AlphaAnimation getAlphaAnimation() {
//        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
//        alphaAnimation.setDuration(1000);
//        return alphaAnimation;
//    }


    /**
     *
     * @param loginToken
     * @param username
     * @param usernameForLogin
     * @param passWord
     * @param mobilePhone
     * @param nickName
     * @param shopName
     * @param Welcome
     * @param sessionToken
     * @param mposKey
     * @param showMerName
     * @param email
     */
    @Override
    public void userLogin(String loginToken, String username, String usernameForLogin
            , String passWord, String mobilePhone, String nickName, String shopName
            , String Welcome, String sessionToken, String mposKey, String showMerName
            , String email, String agentFlag, String authFlag, String ismtNoticeCount
            , String merCode, String goodsSelectionUrl, String merPwd) {
        animationThread = true;
        Utilities.setGoodsSelectionUrl(this, goodsSelectionUrl);
        Utilities.setMerCode(this, merCode);
        new Handler().postDelayed(new Runnable(){
            public void run() {
                //execute the task
                NewLogin(animationThread);
            }
        }, 2000);

        if (Utilities.isAgent(this)) {
            mSplashPresenter = new SplashPresenter(this);
            mSplashPresenter.start();
        }

    }

    /**
     * 登录失败
     *
     * @param message
     */
    @Override
    public void userLoginError(String message, Map<String, String> respData) {
        Log.d(LOG_TAG, "登录失败");
        animationThread = false;
        new Handler().postDelayed(new Runnable(){
            public void run() {
                //execute the task
                NewLogin(animationThread);
            }
        }, 2000);
    }

    public void NewLogin(boolean status) {
        //Log.e("结束",System.currentTimeMillis()+"");
        Intent intent = null;

        if (status) {
            Intent sourceIntent = getIntent();
            intent = new Intent(this, MainActivity.class);
            Bundle bundle = sourceIntent.getExtras();
            if(bundle != null){
                intent.putExtras(sourceIntent.getExtras());
            }
            startActivity(intent);
            //Log.i("开始解压时间---", System.currentTimeMillis()+"");
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }

    private long exitTime;// 退出时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToast(this, getResources().getString(R.string.once_again));
                exitTime = System.currentTimeMillis();
            } else {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

  /*  @Override
    public void updateModel(String key, Object data) {
        if(AppService2.CHECK_UPDATE.equals(key)){
            if (pDialog != null) {
                pDialog.hide();
            }

        }
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        if(AppService2.CHECK_UPDATE.equals(key)){
            if (pDialog != null) {
                pDialog.hide();
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, PERMISSION);
            } else {
                userLogin();
            }
        }
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
                Intent intent = new Intent(StartActivity.this, DialogActivity.class);
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
        userConfig = GroupEvent.getInstance().init(userConfig);
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
}
