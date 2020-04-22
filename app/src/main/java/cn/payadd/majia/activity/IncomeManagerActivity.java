package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import java.util.ArrayList;
import java.util.List;

import cn.payadd.majia.adapter.ProfitFragmentPagerAdapter;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.IRefreshFragment;
import cn.payadd.majia.fragment.Tab1Fragment;
import cn.payadd.majia.fragment.Tab2Fragment;
import cn.payadd.majia.listener.AppBarStateChangeListener;

/**
 * Created by df on 2017/10/8.
 */

public class IncomeManagerActivity extends BaseActivity implements IActivity,RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RadioGroup rgRecord;
    private Button btnWithdraw;
    private RadioButton rbIncomeRecode, rbWithdrawRecode;
    private AppBarLayout appBar;
    public TextView tvCanWithdrawAmount,tvFreezeAmount,tvWithdrawAmount,tvIncome;
    List<Fragment> list;

    @Override
    public int getLayoutId() {
        return R.layout.activity_profitmanage;
    }

    @Override
    public void initView() {
        setTitleCenterText("收益管理");
        setTitleBackButton();
        setTitleRightImage(R.mipmap.icon_help, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tvCanWithdrawAmount = (TextView) findViewById(R.id.tv_can_withdraw_amount);
        tvFreezeAmount = (TextView) findViewById(R.id.tv_freeze_amount);
        tvWithdrawAmount = (TextView) findViewById(R.id.tv_withdraw_amount);
        tvIncome = (TextView) findViewById(R.id.tv_income);
        btnWithdraw = (Button) findViewById(R.id.btn_withdraw);
        list = new ArrayList<>();
        list.add(new Tab1Fragment());
        list.add(new Tab2Fragment());
        viewPager.setAdapter(new ProfitFragmentPagerAdapter(getSupportFragmentManager(),list));
        rbIncomeRecode = (RadioButton) findViewById(R.id.rb_income_recode);
        rbWithdrawRecode = (RadioButton) findViewById(R.id.rb_withdraw_recode);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        rgRecord = (RadioGroup) findViewById(R.id.rg_record);
        rgRecord.setOnCheckedChangeListener(this);
        viewPager.addOnPageChangeListener(this);
        rbIncomeRecode.setChecked(true);

        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    Fragment fragment = list.get(viewPager.getCurrentItem());
                    if(fragment instanceof IRefreshFragment){
                        ((IRefreshFragment)fragment).refreshCallBack(true);
                    }
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    Fragment fragment = list.get(viewPager.getCurrentItem());
                    if(fragment instanceof IRefreshFragment){
                        ((IRefreshFragment)fragment).refreshCallBack(false);
                    }
                }else if(state == State.IDLE){
                    //中间状态
                    Fragment fragment = list.get(viewPager.getCurrentItem());
                    if(fragment instanceof IRefreshFragment){
                        ((IRefreshFragment)fragment).refreshCallBack(false);
                    }
                }
            }
        });
        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = tvCanWithdrawAmount.getText().toString();
                Intent intent = new Intent(IncomeManagerActivity.this,WithdrawActivity.class);
                intent.putExtra("canWithdrawAmount",amount);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPermission() {

    }
    private void showHelp() {
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_income_manage_help, null);//获取自定义布局
        final Button btnDismiss = (Button) layout.findViewById(R.id.btn_dialog_dismiss);
        final ImageView ivDismiss = (ImageView) layout.findViewById(R.id.iv_dialog_dismiss);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ivDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                rbIncomeRecode.setChecked(true);
                break;
            case 1:
                rbWithdrawRecode.setChecked(true);
                break;
        }
//        viewPager.resetHeight(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.rb_income_recode:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_withdraw_recode:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {

    }
}
