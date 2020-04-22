package com.fdsj.credittreasure.activities;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.presenter.DownLoadPresenter;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.utils.Utilities;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by BXND on 2017-01-06.
 * 下载钥密
 */

public class DownLoadKeyActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.edit_mode)
    EditText editMode;
    @BindView(R.id.btn_down)
    Button btnDown;

    DownLoadPresenter presenter;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_downloadkey;
    }

    @Override
    protected void initView() {
        super.setCenterText(getResources().getString(R.string.down_key));
        super.setBackOnclick();
        presenter = new DownLoadPresenter();
    }

    @Override
    protected void initData() {
        String code = Utilities.getDownloadKey(DownLoadKeyActivity.this);
        if (!TextUtils.isEmpty(code)) {
            editMode.setText(code);
        }
    }

    @OnClick(R.id.btn_down)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_down:
                String code = editMode.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    Utilities.setDownloadKey(DownLoadKeyActivity.this, code);
                    presenter.downloadKey(code, DownLoadKeyActivity.this);
                } else {
                    DialogFactory.showAlertDialog(DownLoadKeyActivity.this, getResources().getString(R.string.hint), getResources().getString(R.string.inpt_msg));
                }
                break;
        }
    }
}
