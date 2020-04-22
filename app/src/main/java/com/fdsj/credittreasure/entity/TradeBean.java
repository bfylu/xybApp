package com.fdsj.credittreasure.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 交易信息实体
 * <p>当前版本： V1.0.0
 */

public class TradeBean {
    //totalAcquire 总实收，transCount=总笔数，totalRefund=退款，turnover=营业额

    private List<Bean> list = new ArrayList<>();

    public List<Bean> getList() {
        return list;
    }

    public void setList(List<Bean> list) {
        this.list = list;
    }

    public static class Bean {
        /**
         * 时间
         */
        private String date;

        /**
         * 营业额
         */
        private float turnover;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public float getTurnover() {
            return turnover;
        }

        public void setTurnover(float turnover) {
            this.turnover = turnover;
        }
    }

}
