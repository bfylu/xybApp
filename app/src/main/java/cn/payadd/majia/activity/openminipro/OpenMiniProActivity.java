package cn.payadd.majia.activity.openminipro;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.fdsj.credittreasure.R;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.adapter.openminipro.OpenMiniProAdapter;
import cn.payadd.majia.constant.ApplyActivityContainer;

public class OpenMiniProActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.listView)
    ListView listView;

    private String[] contents;
    private int[] icons;

    private OpenMiniProAdapter mOpenMiniProAdapter;
    private Intent mIntent;

    @Override
    public int getLayoutId() {
        return R.layout.activity_open_mini_pro;
    }

    @Override
    public void initView() {
        if (ApplyActivityContainer.openMiniProAct == null) {
            ApplyActivityContainer.openMiniProAct = new LinkedList<>();
        }
        ApplyActivityContainer.openMiniProAct.add(this);
        setTitleCenterText(getResources().getString(R.string.open_my_mini_pro));
        setTitleBackButton();
    }

    @Override
    protected void initData() {
        contents =  new String[] {getResources().getString(R.string.have_mini_pro_shop)
                , getResources().getString(R.string.share_us)
                , getResources().getString(R.string.ai_manage)
                , getResources().getString(R.string.recommend)};

        icons = new int[] {R.mipmap.icon1, R.mipmap.icon2, R.mipmap.icon3, R.mipmap.icon4};

        mOpenMiniProAdapter = new OpenMiniProAdapter(contents, icons, this);
        listView.setAdapter(mOpenMiniProAdapter);
    }

    @Override
    protected void initPermission() {

    }

    @OnClick({R.id.btn_open_mini_pro, R.id.btn_not_open})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_mini_pro:
                mIntent = new Intent(this, OpenResultActivity.class);
                startActivity(mIntent);
                break;
            case R.id.btn_not_open:
                if (ApplyActivityContainer.openMiniProAct != null) {
                    for (Activity act : ApplyActivityContainer.openMiniProAct) {
                        act.finish();
                    }
                }
                break;
        }
    }
}
