package cn.payadd.majia.entity.aistore;

public class ClientDetailBean extends AIBaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private String userId;
        private String nick;
        private String userJmgUrl;
        private int sex;
        private String phone;
        private String distance;
        private String activityDate;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getUserJmgUrl() {
            return userJmgUrl;
        }

        public void setUserJmgUrl(String userJmgUrl) {
            this.userJmgUrl = userJmgUrl;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getActivityDate() {
            return activityDate;
        }

        public void setActivityDate(String activityDate) {
            this.activityDate = activityDate;
        }
    }
}
