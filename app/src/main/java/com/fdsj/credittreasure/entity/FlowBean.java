package com.fdsj.credittreasure.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.payadd.majia.entity.BaseBean;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 流水实体类
 * <p>当前版本： V1.0.0
 */

public class FlowBean extends BaseBean {

    private List<FlowBean.Bean> list = new ArrayList<>();

    private String totalAcquire;

    private String transCount;

    private String totalRefund;

    private String row;

    private String todayTotalAcquire;

    private String todayTransCount;

    private String pageTotal;

    private String page;

    private String turnover;

    public List<FlowBean.Bean> getList() {
        return list;
    }

    public void setList(List<FlowBean.Bean> list) {
        this.list = list;
    }

    public static class Bean {
        private String orderTime;//订单时间
        private double orderAmount;//订单金额
        private String payType;//订单支付类型
        private String payMethod;//订单
        private String orderStatus;//订单支付状态
        private String orderNo;//订单编号
        private String bizType;//支付方式
        private String orderType;//
        private String dateTransCount;//当日总订单数
        private String dateTotalAcquire;//当日总金额

        public String getDateTransCount() {
            return dateTransCount;
        }

        public void setDateTransCount(String dateTransCount) {
            this.dateTransCount = dateTransCount;
        }

        public String getDateTotalAcquire() {
            return dateTotalAcquire;
        }

        public void setDateTotalAcquire(String dateTotalAcquire) {
            this.dateTotalAcquire = dateTotalAcquire;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public double getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getOrderStatus() {
            if (!TextUtils.isEmpty(orderStatus))
                return orderStatus;
            return null;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }

        public String getPayType() {
            if (!TextUtils.isEmpty(payType))
                return payType;
            return "";
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getBizType() {
            return bizType;
        }

        public void setBizType(String bizType) {
            this.bizType = bizType;
        }
    }

    public String getTotalAcquire() {
        return totalAcquire;
    }

    public String getTransCount() {
        return transCount;
    }

    public String getTotalRefund() {
        return totalRefund;
    }

    public void setTotalAcquire(String totalAcquire) {
        this.totalAcquire = totalAcquire;
    }

    public void setTransCount(String transCount) {
        this.transCount = transCount;
    }

    public void setTotalRefund(String totalRefund) {
        this.totalRefund = totalRefund;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getTodayTotalAcquire() {
        return todayTotalAcquire;
    }

    public void setTodayTotalAcquire(String todayTotalAcquire) {
        this.todayTotalAcquire = todayTotalAcquire;
    }

    public String getTodayTransCount() {
        return todayTransCount;
    }

    public void setTodayTransCount(String todayTransCount) {
        this.todayTransCount = todayTransCount;
    }

    public String getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(String pageTotal) {
        this.pageTotal = pageTotal;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }
}

