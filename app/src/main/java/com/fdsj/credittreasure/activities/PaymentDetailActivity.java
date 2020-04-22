/*
package com.fdsj.credittreasure.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.constant.ProductType;
import com.fdsj.credittreasure.entity.SerializableMap;
import com.fdsj.credittreasure.presenter.FlowDetailPresenter;
import com.utils.Enums;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.utils.view.CheryDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

*/
/**
 * Created by BXND on 2017-01-05.
 * 支付详情
 *//*


public class PaymentDetailActivity extends BaseActivity implements View.OnClickListener, IActivity, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.include_title)
    View includetitle;//账号

    @BindView(R.id.tv_pay_method)
    TextView tv_pay_method;//支付方式
    @BindView(R.id.im_pay_icon)
    ImageView imPayIcon;
    @BindView(R.id.tv_pay_name)
    TextView tvPayName;//

    @BindView(R.id.tv_door)
    TextView tvDoor;//商家名称
    @BindView(R.id.tv_order)
    TextView tvOrder;//收款金额
    @BindView(R.id.tv_Operator)
    TextView tvOperator;//操作员

    //    @BindView(R.id.tv_settle_time)
//    TextView tvSettleTime;//支付状态
    @BindView(R.id.tv_start_settle_time)
    TextView tvStartSettleTime;//支付状态

//    @BindView(R.id.settle_time)
//    TextView settleTime;

    @BindView(R.id.end_settle_time)
    TextView endSettleTime;//支付账号
    @BindView(R.id.tv_total_recepits)
    TextView tvTotalRecepits;//支付类型
    @BindView(R.id.tv_total_refund)
    TextView tvTotalRefund;//收购机关
    @BindView(R.id.tv_total_deal)
    TextView tvTotalDeal;//交易时间
    @BindView(R.id.btn_choose)
    Button btnChoose;//退款

    @BindView(R.id.tv_prod_type)
    TextView tvProdType;

    @BindView(R.id.tv_danhao)
    TextView tv_danhao;//交易单号
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    //    private CheryDialog dialog;
    private Map<String, String> stringStringMap;
    private String orderNo;
    private FlowDetailPresenter presenter;


    @Override
    protected int getLayoutView() {
        return R.layout.activity_flow_detail;
    }

    @Override
    protected void initView() {
        super.setCenterText(getResources().getString(R.string.pay_info));
        super.setBackOnclick();
//      swipeRefreshLayout.setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            SerializableMap map = bundle.getParcelable("serializableMap");
            if (map != null && map.getMap() != null) {
                stringStringMap = map.getMap();
            }
            orderNo = intent.getStringExtra("orderNo");
        }
        presenter = new FlowDetailPresenter(this);
    }

    @Override
    protected void initData() {
        if (stringStringMap != null) {
            UpdateModel(stringStringMap, 1);
        } else {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    presenter.queryOrderStatus(orderNo, PaymentDetailActivity.this, 1);
                }
            });
        }
//        View view = LayoutInflater.from(this).inflate(R.layout.edittext_dialog, null);
//        dialog = new CheryDialog(this, view);
//        dialog.setGravity(Gravity.TOP);
//        dialog.setAnimationId(R.anim.welcome);
//        dialog.setY((int) getResources().getDimension(R.dimen.y50));
//        dialog.initView();
    }

    @OnClick({R.id.btn_choose})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose:
//                dialog.show();
                ToastUtils.showToast(PaymentDetailActivity.this, "此功能暂未开通");
                break;
        }
    }

    @Override
    public void stopRecyclerView() {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtils.showToast(PaymentDetailActivity.this, "暂无信息");
    }

    @Override
    public void UpdateModel(Object model, int status) {
        swipeRefreshLayout.setRefreshing(false);
        if (status == 1) {
            Map<String, String> response = (Map<String, String>) model;
            tvTotalRecepits.setText(Enums.httpPayType.getNameString(response.get("payMethod")));
            imPayIcon.setVisibility(View.VISIBLE);
            imPayIcon.setImageResource(Enums.httpPayType.getImageResource(response.get("payMethod")));
            tvDoor.setText(Utilities.getShopName(this));
            tvOrder.setText(response.get("orderAmount"));
            tvProdType.setText(ProductType.getValue(response.get("bizType")));
            tvOperator.setText(response.get("operator"));
            String orderStatus = response.get("orderStatus");
            tvStartSettleTime.setText(Enums.orderStatusName.getNameString(orderStatus));
            if (Enums.orderStatusName.成功.getString().equals(orderStatus)) {
                btnChoose.setVisibility(View.VISIBLE);
//                btnChoose.setBackgroundColor(getResources().getColor(R.color.color_Home));
            } else {
                btnChoose.setVisibility(View.GONE);
//                btnChoose.setBackgroundColor(getResources().getColor(R.color.color_9));
            }
            endSettleTime.setText(response.get("payAccount"));
            tvPayName.setText(Enums.httpPayType.getNameString(response.get("payMethod")));
            tvTotalRefund.setText(response.get("payCompany"));
            tvTotalDeal.setText(response.get("orderTime"));
            tv_danhao.setText(response.get("orderNo"));
        } else {
            ToastUtils.showToast(this, model.toString());
        }
    }

    @Override
    public void onRefresh() {
        presenter.queryOrderStatus(orderNo, this, 0);
    }
}
*/
