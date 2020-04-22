package com.fdsj.credittreasure.entity;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 统计实体类
 * <p>当前版本： V1.0.0
 */

public class Statistic {
    //channelTotal 渠道交易统计   topOfMonth当月流量排行
    private StatisticBean list = new StatisticBean();

    public StatisticBean getList() {
        return list;
    }

    public void setList(StatisticBean list) {
        this.list = list;
    }

    public static class StatisticBean {
        private Bean weixin = new Bean();

        private Bean alipay = new Bean();

        public Bean getWeixin() {
            return weixin;
        }

        public void setWeixin(Bean weixin) {
            this.weixin = weixin;
        }

        public Bean getAlipay() {
            return alipay;
        }

        public void setAlipay(Bean alipay) {
            this.alipay = alipay;
        }

        public static class Bean {
            private String top;
            private String transCount;
            private String totalTrans;
            private String transTotal;


            public String getTop() {
                return top;
            }

            public void setTop(String top) {
                this.top = top;
            }

            public String getTransCount() {
                return transCount;
            }

            public void setTransCount(String transCount) {
                this.transCount = transCount;
            }

            public String getTotalTrans() {
                return totalTrans;
            }

            public void setTotalTrans(String totalTrans) {
                this.totalTrans = totalTrans;
            }

            public String getTransTotal() {
                return transTotal;
            }

            public void setTransTotal(String transTotal) {
                this.transTotal = transTotal;
            }
        }


    }
}
