package cn.payadd.majia.Interface;

import cn.payadd.majia.entity.ClientBean;

public interface IClient {
    void stopRecyclerView();
    void getClientList(ClientBean clientBean, int status);
}
