package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;

import cn.payadd.majia.constant.AppService2;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.ScanPayPresenter;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by df on 2018/1/22.
 */

public class PreliminaryCreditActivity extends BaseActivity implements View.OnClickListener,IActivity{

    private CaptureFragment captureFragment;
    private LinearLayout llPreCredit,llWriteOff;

    private AlertDialog dialog;
    private ImageView ivPreCredit,ivWriteOff;

    private int scanType;//1.付款，2核销

    private String scanResult;

    private ScanPayPresenter payPresenter;

    private String authCode;



    @Override
    public int getLayoutId() {
        return R.layout.activity_pre_credit;
    }

    @Override
    public void initView() {
        setTitleCenterText("扫一扫");
        setTitleBackButton();
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        llPreCredit = (LinearLayout) findViewById(R.id.ll_pre_credit);
        llWriteOff = (LinearLayout) findViewById(R.id.ll_write_off);
        ivPreCredit = (ImageView) findViewById(R.id.iv_pre_credit);
        ivWriteOff = (ImageView) findViewById(R.id.iv_write_off);
        llPreCredit.setOnClickListener(this);
        llWriteOff.setOnClickListener(this);
        ivPreCredit.setSelected(true);
        scanType = 1;
    }

    @Override
    public void initData() {

        payPresenter = new ScanPayPresenter(this);
    }

    @Override
    protected void initPermission() {

    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            scanResult = result;
            authCode = StringUtil.getValueByName(scanResult,"authCode");
            payPresenter.scanPay(authCode,scanType);

//            Intent intent = new Intent();

//            if(scanType == 0){
////                intent.setClass(PreliminaryCreditActivity.this,);
//            }else if(scanType == 1){
////                intent.setClass(PreliminaryCreditActivity.this,);
//            }
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
//            resultIntent.putExtras(bundle);
//            PreliminaryCreditActivity.this.setResult(RESULT_OK, resultIntent);
//            PreliminaryCreditActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
//            bundle.putString(CodeUtils.RESULT_STRING, "");
//            resultIntent.putExtras(bundle);
//            PreliminaryCreditActivity.this.setResult(RESULT_OK, resultIntent);
//            PreliminaryCreditActivity.this.finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_pre_credit:
//                确认收款
                scanType = 1;
                ivPreCredit.setSelected(true);
                ivWriteOff.setSelected(false);

                break;
            case R.id.ll_write_off:
//                确定核销
                scanType = 2;
                ivPreCredit.setSelected(false);
                ivWriteOff.setSelected(true);

                break;
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        try {


        if(AppService2.SCANPAY.equals(key)){
            JSONObject respData  = (JSONObject) data;
            String respCode = respData.getString("respCode");
            if("000000".equals(respCode)){
               JSONObject jsonData =  respData.getJSONObject("data");
                Intent intent = new Intent(this,AcquireConfirmActivity.class);
                intent.putExtra("orderAmount",jsonData.getString("orderAmount"));
                intent.putExtra("authCode",authCode);
                intent.putExtra("bizType",scanType);
                startActivity(intent);
                finish();
            }else if("000015".equals(respCode)){
                final AlertDialog.Builder builder = new AlertDialog.Builder(PreliminaryCreditActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_pre_credit_err, null);//获取自定义布局
                builder.setView(layout);
                Button tvBindName = (Button) layout.findViewById(R.id.btn_dialog_dismiss);
                ImageView iv = (ImageView) layout.findViewById(R.id.iv_dialog_dismiss);
                dialog = builder.create();
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

            }else {
                Toast.makeText(this,respData.getString("respDesc"),Toast.LENGTH_SHORT).show();
            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
