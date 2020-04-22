package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import java.util.Map;

import cn.payadd.majia.constant.ActivityTitle;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.fragment.BindAlipayFragment;
import cn.payadd.majia.fragment.ListBindAccountFragment;
import cn.payadd.majia.presenter.FundAuthPresenter;

/**
 * Created by Administrator on 2017/06/18 0018.
 */

public class AccountActivity extends BaseActivity implements IActivity{
    private static final String LOG_TAG = "AccountActivity";
    private FundAuthPresenter fundAuthPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText(ActivityTitle.ACCOUNT);
        setTitleRightImage(R.mipmap.icon_help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_pre_help, null);//获取自定义布局
                builder.setView(layout);
                Button tvBindName = (Button) layout.findViewById(R.id.btn_dialog_dismiss);
                ImageView iv = (ImageView) layout.findViewById(R.id.iv_dialog_dismiss);
                final AlertDialog dialog = builder.create();
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                tvBindName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
        fundAuthPresenter = new FundAuthPresenter(this);



    }

    @Override
    public void initData() {
        String username = Utilities.getUsernameForLogin(this);
        fundAuthPresenter.queryBindInfo(username);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void updateModel(String key, Object data) {
        boolean isBind = false;
        switch (key){
            case "bindInfo":
                Map<String,String> respData = (Map<String, String>) data;
                String respCode = respData.get("respCode");
                String respDesc = respData.get("respDesc");
                if("000000".equals(respCode)){
                    String alipayUserId = respData.get("alipayUserId");
                    String alipayLogonId = respData.get("alipayLogonId");
                    if((!TextUtils.isEmpty(alipayUserId))&&(!TextUtils.isEmpty(alipayLogonId))){
                        isBind = true;
                    }else{
                        isBind =false;
                    }
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    if (isBind) {
                        ListBindAccountFragment fragment = new ListBindAccountFragment();
                        transaction.add(R.id.content_account,fragment);
                    }else{
                        BindAlipayFragment fragment = new BindAlipayFragment();
                        transaction.add(R.id.content_account,fragment);
                    }
                    transaction.commit();
                }else{
                    Toast.makeText(this,respDesc,Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
