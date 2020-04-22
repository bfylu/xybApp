package cn.payadd.majia.activity.openminipro;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.activity.ShopInfoActivity;
import cn.payadd.majia.activity.aistore.AIStoreMainActivity;
import cn.payadd.majia.constant.ApplyActivityContainer;

import static com.fdsj.credittreasure.constant.Constants.APP_ID;

public class OpenResultActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_open_result)
    ImageView iv_open_result;
    @BindView(R.id.tv_open_result)
    TextView tv_open_result;

    private Intent mIntent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_result;
    }

    @Override
    public void initView() {
        if (ApplyActivityContainer.openMiniProAct == null) {
            ApplyActivityContainer.openMiniProAct = new LinkedList<>();
        }
        ApplyActivityContainer.openMiniProAct.add(this);
        setTitleCenterText(getResources().getString(R.string.open_success));
        setTitleBackButton();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initPermission() {

    }

    @OnClick({R.id.btn_manage_now, R.id.btn_enter_now, R.id.btn_enter_ai_shop})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_manage_now:
                mIntent = new Intent(this, ShopInfoActivity.class);
                startActivity(mIntent);
                if (ApplyActivityContainer.openMiniProAct != null) {
                    for (Activity act : ApplyActivityContainer.openMiniProAct) {
                        act.finish();
                    }
                }
                break;
            case R.id.btn_enter_now:
                Toast.makeText(this, "暂未开通，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_enter_ai_shop:
                mIntent = new Intent(this, AIStoreMainActivity.class);
                startActivity(mIntent);
                if (ApplyActivityContainer.openMiniProAct != null) {
                    for (Activity act : ApplyActivityContainer.openMiniProAct) {
                        act.finish();
                    }
                }
                break;
        }
    }
}
