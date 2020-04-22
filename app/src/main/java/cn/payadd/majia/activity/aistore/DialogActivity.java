package cn.payadd.majia.activity.aistore;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.huawei.android.pushagent.PushManager;
import com.tencent.imsdk.TIMCallBack;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import cn.payadd.majia.entity.aistore.FriendshipInfo;
import cn.payadd.majia.entity.aistore.UserInfo;
import cn.payadd.majia.im.presentation.business.LoginBusiness;
import cn.payadd.majia.im.tls.service.TlsBusiness;

public class DialogActivity extends Activity implements View.OnClickListener {

    private TextView tvContent, tvConfirm, tvCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.center_dialog);
        setFinishOnTouchOutside(false);

        tvContent = (TextView) findViewById(R.id.content);
        tvConfirm = (TextView) findViewById(R.id.confirm);
        tvCancel = (TextView) findViewById(R.id.cancel);
        tvContent.setText(getResources().getString(R.string.kick_logout));

        tvConfirm.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm:
                LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(DialogActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                        logout();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DialogActivity.this, getString(R.string.login_succ), Toast.LENGTH_SHORT).show();
                        String deviceMan = android.os.Build.MANUFACTURER;
                        //注册小米和华为推送
                        if (deviceMan.equals("Xiaomi") && shouldMiInit()){
                            MiPushClient.registerPush(getApplicationContext(), "2882303761517826564", "5711782626564");
                        }else if (deviceMan.equals("HUAWEI")){
                            PushManager.requestToken(getApplicationContext());
                        }
                        finish();
                    }
                });
                break;
            case R.id.cancel:
                logout();
                break;
        }
    }

    private void logout(){
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        FriendshipInfo.getInstance().clear();
//        GroupInfo.getInstance().clear();

        finish();
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
