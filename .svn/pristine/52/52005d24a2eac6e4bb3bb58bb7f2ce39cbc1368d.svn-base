package com.fdsj.credittreasure.activities;

import android.text.TextUtils;
import android.widget.TextView;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.fragment.FixedCodeFragment;
import com.fdsj.credittreasure.presenter.FixedCodePresenter;
import com.utils.Fragment.EmptyFragment;
import com.utils.Fragment.ProgressFragment;
import com.utils.Utilities;

import butterknife.BindView;

/**
 * Created by BXND on 2017-01-12.
 * 我的固定码
 */

public class FixedCodeActivity extends BaseActivity implements IActivity {

    EmptyFragment emptyFragment;
    FixedCodeFragment codeFragment;

    FixedCodePresenter presenter;
    @BindView(R.id.text_store)
    TextView textStore;
    @BindView(R.id.text_nickName)
    TextView textNickName;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_fixedcode;
    }

    @Override
    protected void initView() {
        super.setCenterText(getResources().getString(R.string.fixcode_title));
        super.setBackOnclick();
        String shopName = Utilities.getShowMerName(this);
        if (!TextUtils.isEmpty(shopName)) {
            textStore.setText(shopName);
        }

//        String nickName = Utilities.getNickName(this);
//        if (!TextUtils.isEmpty(nickName)) {
//            textNickName.setText("");
//        }
        presenter = new FixedCodePresenter(this);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initData() {
        switchFragment(new ProgressFragment());
        presenter.getMerchantCode(this);
    }

    @Override
    public void stopRecyclerView() {
        if (emptyFragment == null) {
            emptyFragment = new EmptyFragment();
        }
        switchFragment(emptyFragment);
    }

    @Override
    public void UpdateModel(Object model, int status) {

        if (codeFragment == null && model != null) {
            codeFragment = FixedCodeFragment.getFixedCodeFragment(model.toString(), R.mipmap.logo);
        } else {
            codeFragment = FixedCodeFragment.getFixedCodeFragment("", R.mipmap.logo);
        }
        switchFragment(codeFragment);
    }
}
