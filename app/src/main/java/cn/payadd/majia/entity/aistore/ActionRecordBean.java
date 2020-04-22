package cn.payadd.majia.entity.aistore;

import java.util.List;

public class ActionRecordBean extends AIBaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private int total;
        private int pageNum;
        private int pageSize;
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public class ListBean {
            private int id;
            private String userId;
            private String merCode;
            private String goodsName;
            private String refNo;
            private String actionState;
            private String createDt;
            private String updateDt;
            private String userJmgUrl;
            private String nick;
            private int sex;
            private String curAddress;
            private String pcaCode;
            private double longitude;
            private double latitude;
            private int status;
            private double merLongitude;
            private double merLatitude;
            private String distance;
            private String phone;
            private String content;
            private String activityDate;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getMerCode() {
                return merCode;
            }

            public void setMerCode(String merCode) {
                this.merCode = merCode;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getRefNo() {
                return refNo;
            }

            public void setRefNo(String refNo) {
                this.refNo = refNo;
            }

            public String getActionState() {
                return actionState;
            }

            public void setActionState(String actionState) {
                this.actionState = actionState;
            }

            public String getCreateDt() {
                return createDt;
            }

            public void setCreateDt(String createDt) {
                this.createDt = createDt;
            }

            public String getUpdateDt() {
                return updateDt;
            }

            public void setUpdateDt(String updateDt) {
                this.updateDt = updateDt;
            }

            public String getUserJmgUrl() {
                return userJmgUrl;
            }

            public void setUserJmgUrl(String userJmgUrl) {
                this.userJmgUrl = userJmgUrl;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public int getSex() {
                return sex;
            }

            public void setSex(int sex) {
                this.sex = sex;
            }

            public String getCurAddress() {
                return curAddress;
            }

            public void setCurAddress(String curAddress) {
                this.curAddress = curAddress;
            }

            public String getPcaCode() {
                return pcaCode;
            }

            public void setPcaCode(String pcaCode) {
                this.pcaCode = pcaCode;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public double getMerLongitude() {
                return merLongitude;
            }

            public void setMerLongitude(double merLongitude) {
                this.merLongitude = merLongitude;
            }

            public double getMerLatitude() {
                return merLatitude;
            }

            public void setMerLatitude(double merLatitude) {
                this.merLatitude = merLatitude;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getActivityDate() {
                return activityDate;
            }

            public void setActivityDate(String activityDate) {
                this.activityDate = activityDate;
            }
        }
    }
}
