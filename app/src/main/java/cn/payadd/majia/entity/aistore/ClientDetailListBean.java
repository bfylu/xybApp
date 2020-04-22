package cn.payadd.majia.entity.aistore;

import java.util.List;

public class ClientDetailListBean extends AIBaseBean {

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
            private String userJmgUrl;
            private String goodsName;
            private String content;
            private int actionState;
            private int actionCount;
            private String createDt;
            private String createDate;
            private String date;
            private long timeStamp;

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

            public String getUserJmgUrl() {
                return userJmgUrl;
            }

            public void setUserJmgUrl(String userJmgUrl) {
                this.userJmgUrl = userJmgUrl;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getActionState() {
                return actionState;
            }

            public void setActionState(int actionState) {
                this.actionState = actionState;
            }

            public int getActionCount() {
                return actionCount;
            }

            public void setActionCount(int actionCount) {
                this.actionCount = actionCount;
            }

            public String getCreateDt() {
                return createDt;
            }

            public void setCreateDt(String createDt) {
                this.createDt = createDt;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public long getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(long timeStamp) {
                this.timeStamp = timeStamp;
            }
        }
    }
}
