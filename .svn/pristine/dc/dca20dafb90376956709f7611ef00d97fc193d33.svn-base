package cn.payadd.majia.presenter;

import android.content.Context;

import com.fdsj.credittreasure.application.BaseApplication;
import com.google.gson.Gson;
import com.utils.Utilities;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.Interface.IShopOrder;
import cn.payadd.majia.activity.shoporder.ShipActivity;
import cn.payadd.majia.activity.shoporder.ShopOrderDetailActivity;
import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.ExpressCompanyBean;
import cn.payadd.majia.entity.MonthPaymentBean;
import cn.payadd.majia.entity.OrderNumBean;
import cn.payadd.majia.entity.ShopOrderBean;
import cn.payadd.majia.entity.ShopOrderCloseReasonBean;
import cn.payadd.majia.entity.ShopOrderDetailBean;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.fragment.shoporder.ChildAllFragment;
import cn.payadd.majia.fragment.shoporder.ChildCloseFragment;
import cn.payadd.majia.fragment.shoporder.ChildProtectionFragment;
import cn.payadd.majia.fragment.shoporder.ChildSendOutFragment;
import cn.payadd.majia.fragment.shoporder.ChildWaitSendFragment;
import cn.payadd.majia.fragment.shoporder.ShopAllOrderFragment;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by lang on 2018/5/21.
 */

public class ShopOrderPresenter extends BasePresenter {
    IShopOrder iShopOrder;
    private Gson gson;

    public ShopOrderPresenter(Context ctx, ShopAllOrderFragment iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public ShopOrderPresenter(Context ctx, ChildAllFragment iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public ShopOrderPresenter(Context ctx, ChildCloseFragment iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public ShopOrderPresenter(Context ctx, ChildSendOutFragment iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public ShopOrderPresenter(Context ctx, ChildWaitSendFragment iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public ShopOrderPresenter(Context ctx, ChildProtectionFragment iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public ShopOrderPresenter(Context ctx, ShopOrderDetailActivity iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public ShopOrderPresenter(Context ctx, ShipActivity iShopOrder) {
        super(ctx);
        this.iShopOrder = iShopOrder;
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 查询线上商城订单列表
     * @param orderStatus 订单状态
     * @param page
     * @param row
     */
    public void queryShopOrder(String orderStatus, int page, int row) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("orderStatus", orderStatus);
        data.put("merCode", Utilities.getMerCode(BaseApplication.getAppContext()));
        data.put("page", StringUtil.toString(page));
        data.put("row", StringUtil.toString(row));

        sendUrlTo3Server(actionUrl, AppService.APPLY_INSTALLMENT, data, new ICallback() {
            @Override
            public void exec(Object params) {
                ShopOrderBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderBean.class);
                iShopOrder.getShopBean(bean, 1);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
//                ShopOrderBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderBean.class);
//                iShopOrder.getShopBean(bean, 2);
                BaseBean baseBean = gson.fromJson(StringUtil.toString(params), BaseBean.class);
                iShopOrder.getShopBean((ShopOrderBean) baseBean, 2);
                iShopOrder.stopRecyclerView();
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                iShopOrder.stopRecyclerView();
            }
        }, false);
    }

    /**
     * 查询线上商城维权订单列表
     * @param orderStatus 订单状态
     * @param page
     * @param row
     */
    public void queryShopProtectionOrder(String orderStatus, int page, int row) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("orderStatus", orderStatus);
        data.put("merCode", Utilities.getMerCode(BaseApplication.getAppContext()));
        data.put("page", StringUtil.toString(page));
        data.put("row", StringUtil.toString(row));

        sendUrlTo3Server(actionUrl, AppService.APPLY_INSTALLMENT, data, new ICallback() {
            @Override
            public void exec(Object params) {
                ShopOrderBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderBean.class);
                iShopOrder.getShopProtectionBean(bean, 1);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
//                ShopOrderBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderBean.class);
//                iShopOrder.getShopProtectionBean(bean, 2);
                BaseBean baseBean = gson.fromJson(StringUtil.toString(params), BaseBean.class);
                iShopOrder.getShopProtectionBean((ShopOrderBean) baseBean, 2);
                iShopOrder.stopRecyclerView();
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                iShopOrder.stopRecyclerView();
            }
        }, false);
    }

    /**
     * 订单关闭理由列表查询接口
     */
    public void queryCloseOrderReason() {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();

        sendUrlTo3Server(actionUrl, AppService.INSTALLMENT_ORDER_DETAIL, data, new ICallback() {
            @Override
            public void exec(Object params) {
                ShopOrderCloseReasonBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderCloseReasonBean.class);
                iShopOrder.getShopOrderCloseReason(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                ShopOrderCloseReasonBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderCloseReasonBean.class);
                iShopOrder.getShopOrderCloseReason(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
            }
        }, false);
    }

    /**
     * 快递公司列表查询接口
     */
    public void queryExpressCompany() {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();

        sendUrlTo3Server(actionUrl, AppService.QUERY_EXPRESS_COMPANY, data, new ICallback() {
            @Override
            public void exec(Object params) {
                ExpressCompanyBean bean = gson.fromJson(StringUtil.toString(params), ExpressCompanyBean.class);
                iShopOrder.getExpressCompanyList(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                ExpressCompanyBean bean = gson.fromJson(StringUtil.toString(params), ExpressCompanyBean.class);
                iShopOrder.getExpressCompanyList(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
            }
        }, false);
    }

    /**
     * 关闭订单接口
     * @param orderNo 订单号
     * @param causeCode 关闭原因编号
     * @param orderStatus 订单状态
     */
    public void closeOrder(String orderNo, String causeCode, String cause, int orderStatus) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("causeCode", causeCode);
        data.put("cause", cause);
        data.put("orderStatus", StringUtil.toString(orderStatus));

        sendUrlTo3Server(actionUrl, AppService.INSTALLMENT_REPAY_OF_MONTH, data, new ICallback() {
            @Override
            public void exec(Object params) {
                BaseBean bean = gson.fromJson(StringUtil.toString(params), BaseBean.class);
                iShopOrder.closeOrder(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                BaseBean bean = gson.fromJson(StringUtil.toString(params), BaseBean.class);
                iShopOrder.closeOrder(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
            }
        }, false);
    }

    /**
     * 发货接口
     * @param orderNo 订单号
     * @param logistics 是否需要物流   Y是  N否
     * @param courierNumber 快递单号
     * @param logisticsCompanyCode 物流公司编码
     * @param logisticsCompanyName 物流公司名称
     */
    public void deliverGoods(String orderNo, String logistics, String courierNumber, String logisticsCompanyCode, String logisticsCompanyName) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("logistics", logistics);
        data.put("courierNumber", courierNumber);
        data.put("logisticsCompanyCode", logisticsCompanyCode);
        data.put("logisticsCompanyName", logisticsCompanyName);

        sendUrlTo3Server(actionUrl, AppService.QUERY_COLLECT_FORM, data, new ICallback() {
            @Override
            public void exec(Object params) {
                BaseBean bean = gson.fromJson(StringUtil.toString(params), BaseBean.class);
                iShopOrder.deliverGoods(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                BaseBean bean = gson.fromJson(StringUtil.toString(params), BaseBean.class);
                iShopOrder.deliverGoods(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
            }
        }, false);
    }

    /**
     * 查询线上商城订单
     * @param orderNo 订单号
     */
    public void queryShopOrderDetail(String orderNo) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);

        sendUrlTo3Server(actionUrl, AppService.NEW_STATISTICS, data, new ICallback() {
            @Override
            public void exec(Object params) {
                ShopOrderDetailBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderDetailBean.class);
                iShopOrder.getShopOrderDetail(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                ShopOrderDetailBean bean = gson.fromJson(StringUtil.toString(params), ShopOrderDetailBean.class);
                iShopOrder.getShopOrderDetail(bean);
            }
        }, false);
    }

    /**
     * 查询分期订单列表
     * @param orderNo 订单号
     */
    public void queryMonthPayment(String orderNo) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);

        sendUrlTo3Server(actionUrl, AppService.QUERY_BANK_CARD, data, new ICallback() {
            @Override
            public void exec(Object params) {
                MonthPaymentBean bean = gson.fromJson(StringUtil.toString(params), MonthPaymentBean.class);
                iShopOrder.getMonthPayment(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                MonthPaymentBean bean = gson.fromJson(StringUtil.toString(params), MonthPaymentBean.class);
                iShopOrder.getMonthPayment(bean);
            }
        }, false);
    }

    /**
     * 查询各个订单数量
     * @param state 订单号
     */
    public void queryOrderNum(String state) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("state", state);
        data.put("merCode", Utilities.getMerCode(BaseApplication.getAppContext()));

        sendUrlTo3Server(actionUrl, AppService.BIND_CARD, data, new ICallback() {
            @Override
            public void exec(Object params) {
                OrderNumBean bean = gson.fromJson(StringUtil.toString(params), OrderNumBean.class);
                iShopOrder.getOrderNum(bean);
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                OrderNumBean bean = gson.fromJson(StringUtil.toString(params), OrderNumBean.class);
                iShopOrder.getOrderNum(bean);
            }
        }, false);
    }

}
