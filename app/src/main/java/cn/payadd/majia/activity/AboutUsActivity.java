package cn.payadd.majia.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.presenter.DownLoadPresenter;
import com.utils.AppUtil;
import com.utils.Config;
import com.utils.Utilities;

import cn.payadd.majia.constant.ActivityTitle;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.VersionUpdatePresenter;

/**
 * Created by df on 2017/9/18.
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener, IActivity{
    private TextView tvContactNumber,tvCommonWX,tvVersion;
    private RelativeLayout rlCheckUpdate;
    private ImageView ivUpdate;
    private DownLoadPresenter presenter;
    private VersionUpdatePresenter updatePresenter;
    private String userName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText(ActivityTitle.ABOUT_US);
        updatePresenter = new VersionUpdatePresenter(this);

        tvContactNumber = (TextView) findViewById(R.id.tv_contact_number);
        tvCommonWX = (TextView) findViewById(R.id.tv_common_wx);
        rlCheckUpdate = (RelativeLayout) findViewById(R.id.rl_check_update);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        ivUpdate = (ImageView) findViewById(R.id.iv_update);
        ivUpdate.setVisibility(View.GONE);

        tvVersion.setText(AppUtil.getVersionName(this));
        tvCommonWX.setOnClickListener(this);
        tvContactNumber.setOnClickListener(this);
        rlCheckUpdate.setOnClickListener(this);
    }

    @Override
    public void initData() {
        userName = Utilities.getUsernameForLogin(this);
        presenter = new DownLoadPresenter();
        updatePresenter.updateApp(userName);
    }

    @Override
    protected void initPermission() {

    }
    public void dialPhoneNumber(String phoneNumber) {
            TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if(Config.isMobile()) {
            if (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_common_wx:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvCommonWX.getText());
//                Intent intent = new Intent(this,BarCodeActivity.class);
                Toast.makeText(this,"已复制到剪贴板",Toast.LENGTH_SHORT).show();
//                startActivity(intent);
                break;
            case R.id.tv_contact_number:
                String phoneNumber = tvContactNumber.getText().toString().trim();
                dialPhoneNumber(phoneNumber);
                break;
            case R.id.rl_check_update:
                updatePresenter.updateApp(userName);
                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {

    }
}
