package cn.payadd.majia.activity.shoporder;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.StatusBarUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.Interface.IShopOrder;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.adapter.shoporder.MonthPaymentAdapter;
import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.ExpressCompanyBean;
import cn.payadd.majia.entity.MonthPaymentBean;
import cn.payadd.majia.entity.OrderNumBean;
import cn.payadd.majia.entity.ShopOrderBean;
import cn.payadd.majia.entity.ShopOrderCloseReasonBean;
import cn.payadd.majia.entity.ShopOrderDetailBean;
import cn.payadd.majia.presenter.ShopOrderPresenter;
import cn.payadd.majia.util.Center2Dialog;
import cn.payadd.majia.util.StringUtil;

import static com.fdsj.credittreasure.R.id.rl_pay_way;
import static com.fdsj.credittreasure.constant.Constants.SHOP_ORDER_DETAIL_ACTIVITY;

/**
 * 订单详情页面 ，订单详情分为 待付款、已付款、已发货、已完成、已关闭 状态
 */

public class ShopOrderDetailActivity extends BaseActivity implements View.OnClickListener, IShopOrder {

    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.iv_goods_img)
    ImageView ivGoodsImg;
    @BindView(R.id.tv_goods_content)
    TextView tvGoodsContent;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_num)
    TextView tvGoodsNum;
    @BindView(R.id.tv_refund_status)
    TextView tvRefundStatus;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_order_copy)
    TextView tvOrderCopy;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.tv_pay_way_detail)
    TextView tvPayWayDetail;
    @BindView(R.id.tv_first_payment)
    TextView tvFirstPayment;
    @BindView(R.id.tv_instalment_amount)
    TextView tvInstalmentAmount;
    @BindView(R.id.tv_staging_num)
    TextView tvStagingNum;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_express_way)
    TextView tvExpressWay;
    @BindView(R.id.tv_waybill_num)
    TextView tvWaybillNum;
    @BindView(R.id.tv_waybill_num_copy)
    TextView tvWaybillNumCopy;
    @BindView(R.id.tv_goods_amount)
    TextView tvGoodsAmount;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_real_payment)
    TextView tvRealPayment;
    @BindView(R.id.real_payment)
    TextView realPayment;
    @BindView(R.id.tv_close_reason)
    TextView tvCloseReason;
    @BindView(R.id.btn_send_out)
    Button btnSendOut;

    @BindView(R.id.rl_order_num)
    RelativeLayout rlOrderNum;
    @BindView(R.id.rl_order_date)
    RelativeLayout rlOrderDate;
    @BindView(rl_pay_way)
    RelativeLayout rlPayWay;
    @BindView(R.id.rl_first_payment)
    RelativeLayout rlFirstPayment;
    @BindView(R.id.rl_instalment_amount)
    RelativeLayout rlInstalmentAmount;
    @BindView(R.id.rl_staging_num)
    RelativeLayout rlStagingNum;
    @BindView(R.id.rl_pay_time)
    RelativeLayout rlPayTime;
    @BindView(R.id.rl_express_way)
    RelativeLayout rlExpressWay;
    @BindView(R.id.rl_waybill_num)
    RelativeLayout rlWaybillNum;
    @BindView(R.id.rl_close_reason)
    RelativeLayout rlCloseReason;

    private String orderNo;

    private String installmentOrderNo;

    private ShopOrderPresenter mPresenter;

    private Center2Dialog mMonthPaymentDialog;

    private Intent mIntent;

    private Gson gson;

    private Type type;

    private boolean isSend;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_order_detail;
    }

    @Override
    public void initView() {

        StatusBarUtils.setColor(this, getResources().getColor(R.color.color_9));
        mPresenter = new ShopOrderPresenter(this, this);

        llBack.setOnClickListener(this);
        tvOrderCopy.setOnClickListener(this);
        tvPayWayDetail.setOnClickListener(this);
        tvWaybillNumCopy.setOnClickListener(this);
        btnSendOut.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra("orderNo");
        mPresenter.queryShopOrderDetail(orderNo);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_order_copy:
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvOrderNum.getText().toString().trim());
                Toast.makeText(this, getResources().getString(R.string.copied), Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_pay_way_detail:
                mPresenter.queryMonthPayment(installmentOrderNo);
                break;
            case R.id.tv_waybill_num_copy:
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvWaybillNum.getText().toString().trim());
                Toast.makeText(this, getResources().getString(R.string.copied), Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_send_out:
                mIntent = new Intent(this, ShipActivity.class);
                mIntent.putExtra("orderNo", orderNo);
                startActivityForResult(mIntent, SHOP_ORDER_DETAIL_ACTIVITY);
                break;
        }
    }

    @Override
    public void stopRecyclerView() {

    }

    @Override
    public void getShopBean(ShopOrderBean bean, int status) {

    }

    @Override
    public void getShopProtectionBean(ShopOrderBean bean, int status) {

    }

    @Override
    public void closeOrder(BaseBean data) {

    }

    @Override
    public void getShopOrderCloseReason(ShopOrderCloseReasonBean data) {

    }

    @Override
    public void getExpressCompanyList(ExpressCompanyBean data) {

    }

    @Override
    public void deliverGoods(BaseBean data) {
    }

    @Override
    public void getShopOrderDetail(ShopOrderDetailBean data) {
        if (gson == null) {
            gson = new Gson();
        }
        if (type == null) {
            type = new TypeToken<Map<String, Object>>() {}.getType();
        }
        if (StringUtil.equals("000000", StringUtil.toString(data.getRespCode()))) {
            tvConsignee.setText(StringUtil.toString(data.getData().getUserName()));
            tvPhone.setText(StringUtil.toString(data.getData().getUserPhone()));
            tvShopName.setText(StringUtil.toString(data.getData().getMerShortName()));
            tvAddress.setText(StringUtil.append( StringUtil.toString(data.getData().getProvinceCode())
                    , StringUtil.toString(data.getData().getCityCode())
                    , StringUtil.toString(data.getData().getAreaCode())
                    , StringUtil.toString( data.getData().getAddress())));

            tvGoodsContent.setText(StringUtil.toString(data.getData().getGoodsName()));
            tvGoodsPrice.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getData().getGoodsPrice())));
            tvGoodsNum.setText(StringUtil.append(getResources().getString(R.string.multiply), StringUtil.toString(data.getData().getGoodsQuantity())));
            tvOrderNum.setText(StringUtil.toString(data.getData().getOrderNo()));
            tvOrderDate.setText(StringUtil.toString(data.getData().getOrderTime()));
            if (StringUtil.isNotEmpty(StringUtil.toString(data.getData().getOrderStatus()))) {
                if (5 == data.getData().getOrderStatus()) {
                    tvRefundStatus.setVisibility(View.VISIBLE);
                    tvRefundStatus.setText(getResources().getString(R.string.refunding));
                } else if (6 == data.getData().getOrderStatus()) {
                    tvRefundStatus.setVisibility(View.VISIBLE);
                    tvRefundStatus.setText(getResources().getString(R.string.refund_success));
                } else {
                    tvRefundStatus.setVisibility(View.GONE);
                }
            } else {
                tvRefundStatus.setVisibility(View.GONE);
            }

            if (StringUtil.isNotEmpty(data.getData().getGoodsPic())) {
                Map<String, String> map1 = gson.fromJson(data.getData().getGoodsPic(), type);
                String pic = AppConfig.getImageInterface().replace("%", map1.get("primary")).replace("$", StringUtil.toString(StringUtil.dp2px(this, 93)));
                ImageLoader.getInstance().displayImage(pic, ivGoodsImg);
            } else {
                ivGoodsImg.setImageResource(R.drawable.default_pic);
            }

            rlPayWay.setVisibility(View.VISIBLE);
            if (1 == data.getData().getOrderType()) {
                tvPayWayDetail.setVisibility(View.GONE);
                rlFirstPayment.setVisibility(View.GONE);
                rlInstalmentAmount.setVisibility(View.GONE);
                rlStagingNum.setVisibility(View.GONE);
                tvPayWay.setText(getResources().getString(R.string.weixin_pay));
            } else if (2 == data.getData().getOrderType()) {
                tvPayWayDetail.setVisibility(View.VISIBLE);
                rlFirstPayment.setVisibility(View.VISIBLE);
                rlInstalmentAmount.setVisibility(View.VISIBLE);
                rlStagingNum.setVisibility(View.VISIBLE);
                tvPayWay.setText(getResources().getString(R.string.installment));
                installmentOrderNo = StringUtil.toString(data.getData().getInstallmentOrderNo());
                tvFirstPayment.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getData().getDownPaymentAmt())));
                tvInstalmentAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getData().getInstallmentAmt())));
                tvStagingNum.setText(StringUtil.toString(data.getData().getInstallmentMonth()));
            } else if (3 == data.getData().getOrderType()) {
                tvPayWayDetail.setVisibility(View.GONE);
                rlFirstPayment.setVisibility(View.GONE);
                rlInstalmentAmount.setVisibility(View.GONE);
                rlStagingNum.setVisibility(View.GONE);
                tvPayWay.setText(getResources().getString(R.string.wx_and_grade));
            }
            if (2 == data.getData().getTakeMethod()
                    && StringUtil.isNotEmpty(data.getData().getExpressway())
                    && StringUtil.isNotEmpty(data.getData().getExpressNo())) {
                rlExpressWay.setVisibility(View.VISIBLE);
                rlWaybillNum.setVisibility(View.VISIBLE);

                tvExpressWay.setText(data.getData().getExpressway());
                tvWaybillNum.setText(data.getData().getExpressNo());
            } else if  (1 == data.getData().getTakeMethod()) {
                rlExpressWay.setVisibility(View.GONE);
                rlWaybillNum.setVisibility(View.GONE);
            }

            tvGoodsAmount.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getData().getTotalPrice())));
            tvFreight.setText(StringUtil.append("+", SymbolConstant.RMB, StringUtil.toString(data.getData().getGoodsFreight())));
            tvRealPayment.setText(StringUtil.append(SymbolConstant.RMB, StringUtil.toString(data.getData().getOrderAmt())));

            if (0 == data.getData().getSellerOrderStatus()) {//待付款
                tvOrderStatus.setText(getResources().getString(R.string.obligation));

                realPayment.setText(getResources().getString(R.string.goods_price));

                rlPayTime.setVisibility(View.GONE);
                rlCloseReason.setVisibility(View.GONE);
                btnSendOut.setVisibility(View.GONE);
            } else if (1 == data.getData().getSellerOrderStatus()) {//待发货
                tvOrderStatus.setText(getResources().getString(R.string.wait_send));

                realPayment.setText(getResources().getString(R.string.real_payment));

                rlExpressWay.setVisibility(View.GONE);
                rlWaybillNum.setVisibility(View.GONE);
                rlCloseReason.setVisibility(View.GONE);
                rlPayTime.setVisibility(View.VISIBLE);
                btnSendOut.setVisibility(View.VISIBLE);
                tvPayTime.setText(StringUtil.toString(data.getData().getPayTime()));
            } else if (2 == data.getData().getSellerOrderStatus()) {//已发货
                tvOrderStatus.setText(getResources().getString(R.string.is_send));

                realPayment.setText(getResources().getString(R.string.real_payment));

                rlPayTime.setVisibility(View.VISIBLE);
                tvPayTime.setText(StringUtil.toString(data.getData().getPayTime()));
                btnSendOut.setVisibility(View.GONE);
            } else if (3 == data.getData().getSellerOrderStatus()) {//已完成
                tvOrderStatus.setText(getResources().getString(R.string.is_done));

                realPayment.setText(getResources().getString(R.string.real_payment));

                rlPayTime.setVisibility(View.VISIBLE);
                rlCloseReason.setVisibility(View.GONE);
                tvPayTime.setText(StringUtil.toString(data.getData().getPayTime()));
                btnSendOut.setVisibility(View.GONE);
            } else if (4 == data.getData().getSellerOrderStatus()) {//已关闭
                tvOrderStatus.setText(getResources().getString(R.string.is_closed));

                realPayment.setText(getResources().getString(R.string.goods_price));

                btnSendOut.setVisibility(View.GONE);

                if (StringUtil.isNotEmpty(data.getData().getCloseCause())) {
                    rlCloseReason.setVisibility(View.VISIBLE);
                    tvCloseReason.setText(data.getData().getCloseCause());
                } else {
                    rlCloseReason.setVisibility(View.GONE);
                }
            }
        } else {
            ToastUtils.showShortToast(this, data.getRespDesc());
        }
    }

    @Override
    public void getMonthPayment(MonthPaymentBean bean) {
        if (StringUtil.equals(bean.getRespCode(), "000000")) {
            showMonthPaymentDialog(StringUtil.toString(bean.getData().getInstallmentAmt())
                    , bean.getData().getInstallmentMonth()
                    , bean.getData().getInstallmentMonthDetail());
        } else {
            ToastUtils.showShortToast(this, bean.getRespDesc());
        }
    }

    @Override
    public void getOrderNum(OrderNumBean bean) {

    }

    /**
     * 分期详情Dialog
     * @param totalAmt 分期总金额
     * @param installmentMonth 分期期数
     * @param beanList 每期详情
     */
    private void showMonthPaymentDialog(String totalAmt, int installmentMonth, List<MonthPaymentBean.DataBean.InstallmentMonthDetailBean> beanList) {
        mMonthPaymentDialog = new Center2Dialog(this, R.layout.dialog_month_payment, new int[] {R.id.btn_confirm, R.id.iv_close});
        mMonthPaymentDialog.setOnCenterItemClickListener(new Center2Dialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(Center2Dialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_confirm:
                        hideMonthPaymentDialog();
                        break;
                    case R.id.iv_close:
                        hideMonthPaymentDialog();
                        break;
                }
            }
        });
        mMonthPaymentDialog.show();
        TextView tvDialogTitle = (TextView) mMonthPaymentDialog.findViewById(R.id.tv_dialog_title);
        TextView tvInstalmentAmount = (TextView) mMonthPaymentDialog.findViewById(R.id.tv_instalment_amount);
        TextView tvStagingNum = (TextView) mMonthPaymentDialog.findViewById(R.id.tv_staging_num);
        ListView listView = (ListView) mMonthPaymentDialog.findViewById(R.id.listView);
        tvDialogTitle.setText(getResources().getString(R.string.instalment_detail));
        tvInstalmentAmount.setText(totalAmt);
        tvStagingNum.setText(StringUtil.toString(installmentMonth));
        MonthPaymentAdapter adapter = new MonthPaymentAdapter(this, beanList);
        listView.setAdapter(adapter);
    }

    private void hideMonthPaymentDialog() {
        if (mMonthPaymentDialog != null) {
            mMonthPaymentDialog.dismiss();
            mMonthPaymentDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHOP_ORDER_DETAIL_ACTIVITY) {
            if (resultCode != RESULT_OK) {
                return;
            }
            setResult(RESULT_OK);
            mPresenter.queryShopOrderDetail(orderNo);
        }
    }
}
