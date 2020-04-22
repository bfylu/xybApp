package cn.payadd.majia.Interface;

import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.ExpressCompanyBean;
import cn.payadd.majia.entity.MonthPaymentBean;
import cn.payadd.majia.entity.OrderNumBean;
import cn.payadd.majia.entity.ShopOrderBean;
import cn.payadd.majia.entity.ShopOrderCloseReasonBean;
import cn.payadd.majia.entity.ShopOrderDetailBean;

/**
 * Created by 冷佳兴 on 2017/1/8-16:58.
 * 我是大傻逼，所在公司:博信诺达
 */
public interface IShopOrder {

    void stopRecyclerView();

    void getShopBean(ShopOrderBean bean, int status);

    void getShopProtectionBean(ShopOrderBean bean, int status);

    void closeOrder(BaseBean data);

    void getShopOrderCloseReason(ShopOrderCloseReasonBean data);

    void getExpressCompanyList(ExpressCompanyBean data);

    void deliverGoods(BaseBean data);

    void getShopOrderDetail(ShopOrderDetailBean bean);

    void getMonthPayment(MonthPaymentBean bean);

    void getOrderNum(OrderNumBean bean);
}
