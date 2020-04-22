package cn.payadd.majia.entity.aistore;

/**
 * 商户周边用户
 */
public class PeopleNumberBean extends AIBaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private int peopleCount;
        private int onLinePeopleCount;

        public int getPeopleCount() {
            return peopleCount;
        }

        public void setPeopleCount(int peopleCount) {
            this.peopleCount = peopleCount;
        }

        public int getOnLinePeopleCount() {
            return onLinePeopleCount;
        }

        public void setOnLinePeopleCount(int onLinePeopleCount) {
            this.onLinePeopleCount = onLinePeopleCount;
        }
    }
}
