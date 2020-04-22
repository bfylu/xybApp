package com.fdsj.credittreasure.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.fdsj.credittreasure.R;
import com.utils.LogUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * <p>描   述： 扫一扫获取订单号或者二维码信息
 * <p>创 建 人： qinsiyi
 * <p>创建时间： 2017-01-11 10:51
 * <p>当前版本： V1.0.0
 * <p>修订历史： (版本、修改时间、修改人、修改内容)
 */

public class ScanActivity extends BaseActivity {
    @Override
    protected int getLayoutView() {
        return R.layout.activity_saomiao;
    }

    @Override
    protected void initView() {
        CaptureFragment captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_scanning);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        fragmentManager = getSupportFragmentManager();
        switchFragment(captureFragment);
    }

    @Override
    protected void initData() {
        super.setCenterText(getResources().getString(R.string.scanner));
        super.setBackOnclick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                LogUtil.info("CodeUtils.RESULT_TYPE", bundle.getInt(CodeUtils.RESULT_TYPE) + "");
                LogUtil.info("CodeUtils.RESULT_STRING", bundle.getInt(CodeUtils.RESULT_STRING) + "");

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            LogUtil.info("扫码成功", result);
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            LogUtil.info("失败", "");
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };

}
