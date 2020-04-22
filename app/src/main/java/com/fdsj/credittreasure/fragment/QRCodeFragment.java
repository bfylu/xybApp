package com.fdsj.credittreasure.fragment;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.Interface.iActivities.IQRCode;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.PaymentActivity;
import com.fdsj.credittreasure.presenter.FlowDetailPresenter;
import com.fdsj.credittreasure.presenter.PaymentPresenter;
import com.utils.Enums;
import com.utils.Fragment.ProgressFragment;
import com.utils.ToastUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <p>项目名称：成功付款二维码界面
 * <p>创建时间： 2017-01-10 16:11
 * <p>当前版本： V1.0.0
 */

public class QRCodeFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener, IQRCode, IActivity {

    @BindView(R.id.text_Intelligence)
    TextView text_Intelligence;//智能
    @BindView(R.id.text_wx)
    TextView text_wx;//微信
    @BindView(R.id.text_alipay)
    TextView text_alipay;//支付宝
    @BindView(R.id.text_unionpay)
    TextView text_unionpay;//银联
    @BindView(R.id.text_refresh)
    TextView text_refresh;//二维码刷新计时

    @BindView(R.id.text_amount)
    TextView text_amount;



    ProgressFragment progressFragment;//进度条Fragment
    FixedCodeFragment codeFragment;//扫码界面

    private PaymentPresenter presenter;//支付
    private FlowDetailPresenter flowDetailPresenter;//获取订单状态

    private GestureDetector detector;// 滑动手势

    private BigDecimal amount = new BigDecimal(0);//订单金额
    private Integer[] intents = {R.mipmap.logo, R.mipmap.weixin, R.mipmap.showzhifubao, R.mipmap.showyinlian};//生成二维码中间的logo
    private Enums.httpPayType[] payTypes = {Enums.httpPayType.智能支付, Enums.httpPayType.微信支付, Enums.httpPayType.支付宝, Enums.httpPayType.银联支付};//支付方式
    public Enums.httpPayType payType = Enums.httpPayType.微信支付;//默认状态为智能
    public String orderNo = "";//生成的订单号
    DecimalFormat df = new DecimalFormat("###############0.00");

    public static QRCodeFragment getQRCodeFragment(BigDecimal amount) {
        QRCodeFragment qrCodeFragment = new QRCodeFragment();
        qrCodeFragment.amount = amount;
        return qrCodeFragment;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_gathering_qrcode;
    }


    @Override
    protected void initView() {
        fragmentManager = getChildFragmentManager();
        presenter = new PaymentPresenter(this);
        flowDetailPresenter = new FlowDetailPresenter(this);
        rootView.setOnTouchListener(this);
        text_amount.setText(df.format(amount));
        detector = new GestureDetector(activity, new MyOnGestureListener());
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        setTextColor(PaymentActivity.anInt);
    }

    @OnClick({R.id.text_Intelligence, R.id.framelayout, R.id.text_wx, R.id.text_alipay, R.id.text_unionpay, R.id.bt_gain_status})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.framelayout:
                setTextColor(PaymentActivity.anInt);
                break;
            case R.id.text_Intelligence://智能
                if (PaymentActivity.anInt != PaymentActivity.Intelligence) {
                    setTextColor(PaymentActivity.Intelligence);
                }
                break;
            case R.id.text_wx://微信
                if (PaymentActivity.anInt != PaymentActivity.WeChat) {
                    setTextColor(PaymentActivity.WeChat);
                }
                break;
            case R.id.text_alipay://支付宝
                if (PaymentActivity.anInt != PaymentActivity.Alipay) {
                    setTextColor(PaymentActivity.Alipay);
                }
                break;
            case R.id.text_unionpay://银联
                PaymentActivity payActivity = (PaymentActivity) getActivity();
                payActivity.nowUnionpay();
                break;
            case R.id.bt_gain_status:
                if (!TextUtils.isEmpty(orderNo)) {
                    flowDetailPresenter.queryOrderStatus(orderNo, activity, 0);
                } else {
                    ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.no_order));
                }
                break;
        }
    }

    public void setTextColor(int r) {
        if (r == PaymentActivity.WeChat || r == PaymentActivity.Alipay) {
            PaymentActivity.anInt = r;
            text_unionpay.setTextColor(getResources().getColor(r == 3 ? R.color.color_c7c03b : R.color.color_f));
            text_alipay.setTextColor(getResources().getColor(r == 2 ? R.color.color_c7c03b : R.color.color_f));
            text_wx.setTextColor(getResources().getColor(r == 1 ? R.color.color_c7c03b : R.color.color_f));
            text_Intelligence.setTextColor(getResources().getColor(r == 0 ? R.color.color_c7c03b : R.color.color_f));
            payType = payTypes[r];
            initData(true);
        } else {
            ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.no_function));
        }
    }

    protected void initData(boolean status) {
        if (payType == Enums.httpPayType.智能支付) {
            if (status) {
                ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.no_function));
            }
            orderNo = "";//清空订单信息
            text_refresh.setText("");
            if (codeFragment == null) {
                codeFragment = FixedCodeFragment.getFixedCodeFragment(R.mipmap.e);
            }
            codeFragment.setImageViewCode(R.mipmap.e);
            switchFragment(codeFragment);
        } else if (payType == Enums.httpPayType.银联支付){

            PaymentActivity payActivity = (PaymentActivity) getActivity();
            payActivity.nowUnionpay();

        } else {
            if (progressFragment == null) {
                progressFragment = ProgressFragment.getProgressFragment(Color.WHITE);
            }
            switchFragment(progressFragment);
            presenter.submitOrder(PaymentActivity.anInt, df.format(amount), "", Enums.payMethod.二维码, payType, activity);
        }
    }


    /**
     * 网络加载失败
     */
    @Override
    public void NoModel() {
        if (codeFragment == null) {
            codeFragment = FixedCodeFragment.getFixedCodeFragment(R.mipmap.e);// EmptyFragment.getEmptyFragment(R.mipmap.de, false);
        }
        orderNo = "";//清空订单
        text_refresh.setText("");
        codeFragment.setImageViewCode(R.mipmap.e);
        switchFragment(codeFragment);
//        ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.net_wrong));
    }

    /**
     * 网络加载成功
     *
     * @param status
     */
    @Override
    public void UpdateModel(String payUrl, String orderNo, int status) {
//        ((PaymentActivity)activity).currentOrderNo = orderNo;
//        LogUtil.info("orderNo", orderNo);
        if (status == PaymentActivity.anInt) {
            if (codeFragment == null) {
                codeFragment = FixedCodeFragment.getFixedCodeFragment(payUrl, intents[PaymentActivity.anInt]);
            }
            this.orderNo = orderNo;
            codeFragment.setImageViewCode(payUrl, intents[PaymentActivity.anInt]);
            switchFragment(codeFragment);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public void stopRecyclerView() {
        ToastUtils.showToast(activity, "未获取到订单信息");
    }

    @Override
    public void UpdateModel(Object model, int status) {
        Map<String, String> stringMap = (Map<String, String>) model;
        String orderStatus = stringMap.get("orderStatus");
        if (orderStatus.equals(Enums.orderStatusName.成功.getString())) {
            ToastUtils.showToast(activity, "支付成功");
        } else if (orderStatus.equals(Enums.orderStatusName.处理中.getString())) {
            ToastUtils.showToast(activity, "支付处理中");
        } else {
            ToastUtils.showToast(activity, "支付失败");
        }
    }

    //设置手势识别监听器
    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override//此方法必须重写且返回真，否则onFling不起效
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if ((e1.getX() - e2.getX() > 120) && Math.abs(velocityX) > 200) {
                if (PaymentActivity.anInt < 3) {
                    PaymentActivity.anInt = PaymentActivity.anInt + 1;
                    setTextColor(PaymentActivity.anInt);
                }
                return true;
            } else if ((e2.getX() - e1.getX() > 120) && Math.abs(velocityX) > 200) {
                if (PaymentActivity.anInt > 0) {
                    PaymentActivity.anInt = PaymentActivity.anInt - 1;
                    setTextColor(PaymentActivity.anInt);
                }
                return true;
            }
            return false;
        }
    }
}
