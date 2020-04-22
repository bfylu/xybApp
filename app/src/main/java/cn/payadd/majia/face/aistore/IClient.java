package cn.payadd.majia.face.aistore;

import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.entity.aistore.ClientDetailBean;
import cn.payadd.majia.entity.aistore.ClientDetailListBean;
import cn.payadd.majia.entity.aistore.ClientScreenBean;

public interface IClient {
    void stopRecyclerView();
    //获取客户列表
    void getCustomerList(ActionRecordBean data);
    //获取客户详情列表
    void getCustomerInfoList(ClientDetailListBean data);
    //获取客户详情
    void getCustomerInfo(ClientDetailBean data);
    //获取客户筛选列表
    void getClientScreenList(ClientScreenBean data);
}
