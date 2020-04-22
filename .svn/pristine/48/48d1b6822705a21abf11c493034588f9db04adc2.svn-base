package cn.payadd.majia.entity;

import java.util.List;

/**
 * Created by lang on 2018/5/22.
 */

public class ShopOrderCloseReasonBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private List<CauseListBean> causeList;

        public List<CauseListBean> getCauseList() {
            return causeList;
        }

        public void setCauseList(List<CauseListBean> causeList) {
            this.causeList = causeList;
        }

        public class CauseListBean {
            private String causeCode;//关闭原因编号
            private String causeContent;//关闭原因

            public String getCauseCode() {
                return causeCode;
            }

            public void setCauseCode(String causeCode) {
                this.causeCode = causeCode;
            }

            public String getCauseContent() {
                return causeContent;
            }

            public void setCauseContent(String causeContent) {
                this.causeContent = causeContent;
            }
        }
    }
}
