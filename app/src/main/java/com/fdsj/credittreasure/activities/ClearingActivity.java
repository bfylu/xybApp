package com.fdsj.credittreasure.activities;

import android.text.TextUtils;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.fdsj.credittreasure.widgtes.SwipeRefreshWebView;
import com.utils.Config;
import com.utils.LogUtil;
import com.utils.Utilities;

import butterknife.BindView;

/**
 * Created by BXND on 2017-01-05.
 * 结算
 */

public class ClearingActivity extends BaseActivity {

//    @BindView(R.id.item_clearing)
//    View itemclearing;//交易数目

    @BindView(R.id.swipeRefreshWebView)
    SwipeRefreshWebView swipeRefreshWebView;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_clearing;
    }

    @Override
    protected void initView() {
        super.setCenterText(getResources().getString(R.string.deal_with));
        super.setBackOnclick();
    }

    @Override
    protected void initData() {
        String userName = Utilities.getUsernameForLogin(this);
        String sessionToken = Utilities.getSessionToKen(this);
        String Url = Config.getH5Account() + "?&username=" + userName +
                "&platform=android" +
                "&loginToken=" + Utilities.getLoginToken(this) +
                "&platform=android" +
                "&version=" + BaseApplication.getVersionName() +
                "&charset=utf-8" +
//                "&service=000011" +
                "&sessionToken=" + sessionToken;
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(sessionToken)) {
            LogUtil.info("Url", Url);
            swipeRefreshWebView.setWebLoadUrl(Url);
        } else {
            DialogFactory.userSignOutDialog(this, getResources().getString(R.string.no_login));
        }
    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && swipeRefreshWebView.canGoBack()) {
//            swipeRefreshWebView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        swipeRefreshWebView.webDestroy();
    }
}
