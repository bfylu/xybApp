package cn.payadd.majia.entity;

public class MerCodeAndChanPlatIdBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private String chanPlatId;
        private String chanMerCode;

        public String getChanPlatId() {
            return chanPlatId;
        }

        public void setChanPlatId(String chanPlatId) {
            this.chanPlatId = chanPlatId;
        }

        public String getChanMerCode() {
            return chanMerCode;
        }

        public void setChanMerCode(String chanMerCode) {
            this.chanMerCode = chanMerCode;
        }
    }
}
