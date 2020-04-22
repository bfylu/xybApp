package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.adapter.GoodsAdapter;
import cn.payadd.majia.adapter.InstallmentOrderAdapter;
import cn.payadd.majia.constant.InstallmentStatus;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.listener.NoDoubleClickListener;
import cn.payadd.majia.listener.RecyclerViewClickListener;
import cn.payadd.majia.presenter.InstallmentPresenter;
import cn.payadd.majia.view.RepayOfMonthDialog;

/**
 * Created by zhengzhen.wang on 2017/6/19.
 */

public class InstallmentDetailActivity extends BaseActivity implements IActivity, View.OnClickListener {

    public static final String LOG_TAG = "InstallmentDetailActivity";

    public static final String KEY_ORDER_NO = "orderNo";



    private InstallmentPresenter installmentPresenter;

    private TextView  tvViewReject, tvOrderStatus, tvOrderNo, tvOrderTime, tvVerifyTime, tvApplyName, tvMobilePhone,
                        tvInstallmentType, tvOrderAmt, tvInstallmentAmt, tvDownPayment, tvInstallmentOfMonth;

    private String orderNo;


    private String status;
    private String isUpdateInfo;
    private String verifyInfo, signInfo;
    private String verifyTime, signTime, signUpdateTime;

    private Dialog rejectDialog;
    private RepayOfMonthDialog repayOfMonthDialog;

    private boolean mIsReject;

    private LinearLayout llUpdateInfo;

    private LinearLayout llRepayDetail;

    private LinearLayout llToSign;

    private TextView tvToSign;

    private RecyclerView recyclerView;

    private GoodsAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_installment_detail;
    }

    @Override
    public void initView() {

        setTitleCenterText("分期订单详情");
        setTitleBackButton();
        tvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
        tvViewReject = (TextView) findViewById(R.id.tv_view_reject);
        tvViewReject.setOnClickListener(this);
        tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvVerifyTime = (TextView) findViewById(R.id.tv_verify_time);
        tvApplyName = (TextView) findViewById(R.id.tv_apply_name);
        tvMobilePhone = (TextView) findViewById(R.id.tv_mobile_phone);
        tvInstallmentType = (TextView) findViewById(R.id.tv_installment_type);
        tvOrderAmt = (TextView) findViewById(R.id.tv_order_amount);
        tvInstallmentAmt = (TextView) findViewById(R.id.tv_installment_amount);
        tvDownPayment = (TextView) findViewById(R.id.tv_down_payment);
        tvInstallmentOfMonth = (TextView) findViewById(R.id.tv_installment_month);

        tvToSign = (TextView) findViewById(R.id.btn_to_sign);
        llUpdateInfo = (LinearLayout) findViewById(R.id.ll_update_info);
        llRepayDetail = (LinearLayout) findViewById(R.id.ll_repay_detail);
        llToSign = (LinearLayout) findViewById(R.id.ll_to_sign);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        llUpdateInfo.setOnClickListener(this);
        llRepayDetail.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                installmentPresenter.queryRepayOfMonth(orderNo);
            }
        });
        llToSign.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new GoodsAdapter(this));
        recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this,new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<Map<String,String>> data = adapter.getAllData();
                Map<String,String> map = data.get(position);
                String id = map.get("id");
                Intent intent = new Intent(InstallmentDetailActivity.this,GoodsDetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .imageDecoder(new BaseImageDecoder(true))
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void initData() {

        installmentPresenter = new InstallmentPresenter(this);

        orderNo = getIntent().getStringExtra(KEY_ORDER_NO);
        status = getIntent().getStringExtra("status");
        isUpdateInfo = getIntent().getStringExtra("isUpdateInfo");
        installmentPresenter.queryDetail(orderNo);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void updateModel(String key, Object data) {

        Map<String, String> map = (Map<String, String>) data;
        if ("detail".equals(key)) {
            verifyInfo = map.get("verifyInfo");
            verifyTime = map.get("verifyTime");
            signInfo = map.get("signInfo");
            signTime = map.get("signTime");
            signUpdateTime = map.get("signUpdateTime");
            status = map.get("status");
            isUpdateInfo = map.get("supplementFlag");
            String goodsList = map.get("goodsList");
            switch (status){
                case InstallmentStatus.VERIFY_PENDING:
                    tvOrderStatus.setText(InstallmentOrderAdapter.statusMap.get(InstallmentStatus.VERIFY_PENDING));
//                    findViewById(R.id.ll_update_info).setVisibility(View.VISIBLE);
                    break;
                case InstallmentStatus.PENDING_SIGN:
                    tvOrderStatus.setText(InstallmentOrderAdapter.statusMap.get(InstallmentStatus.PENDING_SIGN));
                    findViewById(R.id.ll_update_info).setVisibility(View.VISIBLE);
                    llToSign.setVisibility(View.VISIBLE);
                    tvToSign.setText("立即签约");
                    break;
                case InstallmentStatus.VERIFY_REJECT:
                    tvOrderStatus.setText(InstallmentOrderAdapter.statusMap.get(InstallmentStatus.VERIFY_REJECT));
                    findViewById(R.id.ll_update_info).setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(signInfo)||!TextUtils.isEmpty(verifyInfo)) {
                        tvViewReject.setVisibility(View.VISIBLE);
                    } else {
                        tvViewReject.setVisibility(View.GONE);
                    }
                    break;
                case InstallmentStatus.SIGN_PENDING:
                    tvOrderStatus.setText(InstallmentOrderAdapter.statusMap.get(InstallmentStatus.SIGN_PENDING));
                    findViewById(R.id.ll_update_info).setVisibility(View.VISIBLE);
                    llToSign.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(signInfo)||!TextUtils.isEmpty(verifyInfo)) {
                        tvViewReject.setVisibility(View.VISIBLE);
                    } else {
                        tvViewReject.setVisibility(View.GONE);
                    }
                    tvToSign.setText("重新签约");
                    break;
                case InstallmentStatus.SIGNED:
                    tvOrderStatus.setText(InstallmentOrderAdapter.statusMap.get(InstallmentStatus.SIGNED));
                    findViewById(R.id.ll_update_info).setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            tvOrderNo.setText(map.get("orderNo"));
            tvOrderTime.setText(map.get("createTime"));
            tvVerifyTime.setText(map.get("verifyTime"));
            tvApplyName.setText(map.get("buyerRealName"));
            tvMobilePhone.setText(map.get("buyerMobilePhone"));
            String type = map.get("installmentType");
            if ("1".equals(type)) {
                tvInstallmentType.setText("个人");
            } else if ("2".equals(type)) {
                tvInstallmentType.setText("企业(团体)");
            } else {
                tvInstallmentType.setText("未定义：" + type);
            }
            tvOrderAmt.setText("¥" + map.get("orderAmount"));
            tvInstallmentAmt.setText("¥" + map.get("installmentAmount"));
            tvDownPayment.setText("¥" + map.get("downPayment"));
            tvInstallmentOfMonth.setText(map.get("installmentMonth") + "个月");
            List<Map<String,String>> list = new ArrayList<>();
            try {
                list =  parseJSONToList(new JSONArray(goodsList));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(list != null){
                adapter.addAll(list);
                adapter.notifyDataSetChanged();
            }
        } else if ("repayOfMonth".equals(key)) {

            try {
                JSONArray array = new JSONArray(map.get("list"));
                if (null == repayOfMonthDialog) {
                    repayOfMonthDialog = new RepayOfMonthDialog();
                    repayOfMonthDialog.setmContext(this);
                    repayOfMonthDialog.setData(array);
                    repayOfMonthDialog.setInstallmentAmt(tvInstallmentAmt.getText().toString());
                    repayOfMonthDialog.setInstallmentOfMonth(tvInstallmentOfMonth.getText().toString());
                    repayOfMonthDialog.show(getFragmentManager(), null);
                } else {
                    repayOfMonthDialog.setInstallmentAmt(tvInstallmentAmt.getText().toString());
                    repayOfMonthDialog.setInstallmentOfMonth(tvInstallmentOfMonth.getText().toString());
                    repayOfMonthDialog.setData(array);
                    repayOfMonthDialog.show(getFragmentManager(), null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_update_info: {
                    Intent intent = new Intent(this, UploadExtInfoActivity.class);
                    intent.putExtra(UploadExtInfoActivity.KEY_ORDER_NO, orderNo);
                    startActivity(intent);
                break;
            }
            case R.id.tv_view_reject: {
                if (null == rejectDialog) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    View view = getLayoutInflater().inflate(R.layout.dialog_installment_reject_info, null);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.iv_close: {
                                    rejectDialog.dismiss();
                                    break;
                                }
                                case R.id.tv_isee: {
                                    rejectDialog.dismiss();
                                    break;
                                }
                            }
                        }
                    };
                    LinearLayout llInfo = (LinearLayout) view.findViewById(R.id.ll_info);
                    view.findViewById(R.id.iv_close).setOnClickListener(listener);
                    view.findViewById(R.id.tv_isee).setOnClickListener(listener);
                    TextView tvRejectInfo = (TextView) view.findViewById(R.id.tv_reject_info);
                    TextView tvRejectTime= (TextView) view.findViewById(R.id.tv_reject_time);
                    if(InstallmentStatus.VERIFY_REJECT.equals(status)){
                            tvRejectInfo.setText(verifyInfo);
                            tvRejectTime.setText(verifyTime);
                            llInfo.setVisibility(View.VISIBLE);
                    }else if(InstallmentStatus.SIGN_PENDING.equals(status)){
                        if (TextUtils.isEmpty(signInfo)) {
                            llInfo.setVisibility(View.GONE);
                        } else {
                            tvRejectInfo.setText(signInfo);
                            tvRejectTime.setText(signUpdateTime);
                            llInfo.setVisibility(View.VISIBLE);
                        }
                    }
                    builder.setView(view);
                    rejectDialog = builder.create();
                    rejectDialog.setCanceledOnTouchOutside(false);
                }
                rejectDialog.show();
                break;
            }
            case R.id.ll_to_sign:{
                if("Y".equals(isUpdateInfo)){
                    Intent intent = new Intent(this, InstallmentContractActivity.class);
                    intent.putExtra("orderNo",orderNo);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this,"请先补充资料后继续签约",Toast.LENGTH_LONG).show();
                }

                break;
            }
        }
    }

    public List<Map<String,String>> parseJSONToList(JSONArray jsonArray){
        if(jsonArray == null || jsonArray.length() <= 0){
            return null;
        }
        List<Map<String,String>> list = new ArrayList<>();
        try {
            for (int i = 0,size = jsonArray.length(); i<size;i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Iterator<String> iterator = jsonObject.keys();
                Map<String,String> map =  map = new HashMap<>();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    String value = jsonObject.getString(key);
                    map.put(key,value);
                }
                if(map!=null){
                    list.add(map);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
