package cn.payadd.majia.face.aistore;

import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.entity.aistore.PeopleNumberBean;
import cn.payadd.majia.entity.aistore.RelativePositionBean;

public interface IRadar {
    void stopRecyclerView();
    //获取商户周边用户
    void getRadar(RelativePositionBean data);
    //获取用户人数
    void getPeopleNumber(PeopleNumberBean data);
    //获取用户行为
    void getActionRecord(ActionRecordBean data);
}
