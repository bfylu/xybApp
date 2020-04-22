package cn.payadd.majia.activity;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Created by df on 2017/12/11.
 */

public class BarCodeActivity extends BaseActivity{
    private static final String LOG_TAG = "BarCodeActivity";

    private CaptureFragment captureFragment;

    private TextView textView;
    @Override
    public int getLayoutId() {
        return R.layout.activity_bar_code;
    }

    @Override
    public void initView() {
        setTitleCenterText("扫描条形码");
        setTitleBackButton();
        textView = (TextView) findViewById(R.id.tv_info);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();


    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPermission() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
//        DisplayMetrics  dm = new DisplayMetrics();
//        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//        //获取Textiew宽高
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        textView.measure(w, h);
//        int height = textView.getMeasuredHeight();
//        viewfinderView.measure(w,h);
//        int viewfinderViewHeight = viewfinderView.getMeasuredHeight();
//        //取得窗口属性
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        //窗口的宽度
//        //窗口高度
//        int screenHeight = dm.heightPixels;
//        int densityDpi = dm.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
//
//        //250dp
//        int size = viewfinderViewHeight * (densityDpi / 160);
//        int margin =  25 * (densityDpi / 160);
//        //screenHeight/2 - 175  = dp * (densityDpi / 160).
//        TextView textView = (TextView)findViewById(R.id.tv_info);
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) textView.getLayoutParams();
//        layoutParams.setMargins(0,(screenHeight/2) - (size/ 2) - height - margin,0,0);//4个参数按顺序分别是左上右下

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            BarCodeActivity.this.setResult(RESULT_OK, resultIntent);
            BarCodeActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            BarCodeActivity.this.setResult(RESULT_OK, resultIntent);
            BarCodeActivity.this.finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
