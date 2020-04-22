package cn.payadd.majia.activity.voicenotif;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.lljjcoder.citylist.Toast.ToastUtils;

import butterknife.BindView;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.util.JumpPermissionManagement;

/**
 * Created by lang on 2018/5/9.
 */

public class VoiceNotificationActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_launch)
    TextView tv_launch;
    @BindView(R.id.tv_background_run)
    TextView tv_backgroundRun;
    @BindView(R.id.tv_battery)
    TextView tv_battery;

    @Override
    public int getLayoutId() {
        return R.layout.activitiy_voice_notification;
    }

    @Override
    public void initView() {
        setTitleCenterText(getResources().getString(R.string.voice_notification));
        setTitleBackButton();

        tv_launch.setOnClickListener(this);
        tv_backgroundRun.setOnClickListener(this);
        tv_battery.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPermission() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_launch:
                JumpPermissionManagement.GoToSetting(this);
//                ApplicationInfo(this);
                break;
            case R.id.tv_background_run:
//                ApplicationInfo(this);
                JumpPermissionManagement.GoToSetting(this);
                break;
            case R.id.tv_battery:
                ignoreBatteryOptimization();
                break;
        }
    }

    /**
     * 应用信息界面
     *
     * @param context
     */
    public static void ApplicationInfo(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void ignoreBatteryOptimization() {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(this.getPackageName());
        //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
        if (!hasIgnored) {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            startActivity(intent);
        } else {
            ToastUtils.showShortToast(this, getResources().getString(R.string.has_been_set));
        }
    }
}
