package cn.payadd.majia.entity;

import java.util.List;

/**
 * Created by lang on 2018/5/25.
 */

public class MonthPaymentBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private String installmentAmt;
        private int installmentMonth;
        private List<InstallmentMonthDetailBean> installmentMonthDetail;

        public String getInstallmentAmt() {
            return installmentAmt;
        }

        public void setInstallmentAmt(String installmentAmt) {
            this.installmentAmt = installmentAmt;
        }

        public int getInstallmentMonth() {
            return installmentMonth;
        }

        public void setInstallmentMonth(int installmentMonth) {
            this.installmentMonth = installmentMonth;
        }

        public List<InstallmentMonthDetailBean> getInstallmentMonthDetail() {
            return installmentMonthDetail;
        }

        public void setInstallmentMonthDetail(List<InstallmentMonthDetailBean> installmentMonthDetail) {
            this.installmentMonthDetail = installmentMonthDetail;
        }

        public class InstallmentMonthDetailBean {
            private int thePhase;//分期期数
            private String repayDate;//分期还款日期
            private String repayAmt;//还款金额
            private String chargeAmt;//利息

            public int getThePhase() {
                return thePhase;
            }

            public void setThePhase(int thePhase) {
                this.thePhase = thePhase;
            }

            public String getRepayDate() {
                return repayDate;
            }

            public void setRepayDate(String repayDate) {
                this.repayDate = repayDate;
            }

            public String getRepayAmt() {
                return repayAmt;
            }

            public void setRepayAmt(String repayAmt) {
                this.repayAmt = repayAmt;
            }

            public String getChargeAmt() {
                return chargeAmt;
            }

            public void setChargeAmt(String chargeAmt) {
                this.chargeAmt = chargeAmt;
            }
        }
    }
}
