package cn.payadd.majia.entity.aistore;

import java.util.List;

/**
 * 商户周边用户
 */
public class RelativePositionBean extends AIBaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {

        private List<ListBean> list;
        private MerchantBean tmerchant;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public MerchantBean getTmerchant() {
            return tmerchant;
        }

        public void setTmerchant(MerchantBean tmerchant) {
            this.tmerchant = tmerchant;
        }

        public class ListBean {
            private int id;
            private String userId;
            private String userJmgUrl;
            private String nick;
            private int sex;
            private String curAddress;
            private String pcaCode;
            private double longitude;
            private double latitude;
            private int status;
            private String createDt;

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

            public String getCreateDt() {
                return createDt;
            }

            public void setCreateDt(String createDt) {
                this.createDt = createDt;
            }
        }

        public class MerchantBean {
            private int id;
            private String userId;
            private String userJmgUrl;
            private String nick;
            private String merchantAddress;
            private String merchantName;
            private String merchantUrl;
            private double longitude;
            private double latitude;
            private int status;
            private String createDt;

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

            public String getMerchantAddress() {
                return merchantAddress;
            }

            public void setMerchantAddress(String merchantAddress) {
                this.merchantAddress = merchantAddress;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public String getMerchantUrl() {
                return merchantUrl;
            }

            public void setMerchantUrl(String merchantUrl) {
                this.merchantUrl = merchantUrl;
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

            public String getCreateDt() {
                return createDt;
            }

            public void setCreateDt(String createDt) {
                this.createDt = createDt;
            }
        }
    }
}
