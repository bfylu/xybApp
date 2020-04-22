package cn.payadd.majia.presenter.aistore;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.entity.aistore.AIBaseBean;
import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.entity.aistore.ClientDetailBean;
import cn.payadd.majia.entity.aistore.ClientDetailListBean;
import cn.payadd.majia.entity.aistore.ClientScreenBean;
import cn.payadd.majia.face.aistore.IClient;
import cn.payadd.majia.util.StringUtil;
import okhttp3.Call;
import okhttp3.Response;

public class ClientPresenter {
    private Context ctx;

    private IClient iClient;

    private boolean pending;

    private Gson gson;

    public ClientPresenter(Context ctx, IClient iClient) {
        this.ctx = ctx;
        this.iClient = iClient;
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 获取客户列表
     * @param mercode 商户号
     * @param screen 筛选编号
     * @param page 页数
     * @param pageSize 每页请求
     */
    public void getCustomerList(String mercode, int screen, String page, String pageSize) {
        if (pending) {
            return;
        }
        pending = true;
        try {
            String actionUrl = AppConfig.getCustomerList();

            OkGo.get(actionUrl).tag(ctx)
                    .params("merCode", mercode)
                    .params("screen", screen)
                    .params("pageNum", page)
                    .params("pageSize", pageSize)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AIBaseBean baseBean = gson.fromJson(s, AIBaseBean.class);
                            if (baseBean.getCode() == 0) {
                                ActionRecordBean bean = gson.fromJson(s, ActionRecordBean.class);
                                iClient.getCustomerList(bean);
                            } else {
                                Toast.makeText(ctx, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {

        } finally {
            pending = false;
        }
    }

    /**
     * 获取客户行为列表
     * @param mercode 商户号
     * @param page 页数
     * @param pageSize 每页请求
     */
    public void getCustomerInfoList(String mercode, String userId, String page, String pageSize) {
        if (pending) {
            return;
        }
        pending = true;
        try {
            String actionUrl = AppConfig.getCustomerInfoList();

            OkGo.get(actionUrl).tag(ctx)
                    .params("merCode", mercode)
                    .params("userId", userId)
                    .params("pageNum", page)
                    .params("pageSize", pageSize)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AIBaseBean baseBean = gson.fromJson(s, AIBaseBean.class);
                            if (baseBean.getCode() == 0) {
                                ClientDetailListBean bean = gson.fromJson(s, ClientDetailListBean.class);
                                iClient.getCustomerInfoList(bean);
                            } else {
                                Toast.makeText(ctx, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {

        } finally {
            pending = false;
        }
    }

    /**
     * 获取客户详情
     * @param mercode 商户号
     * @param userId 用户ID
     */
    public void getCustomerInfo(String mercode, String userId) {
        if (pending) {
            return;
        }
        pending = true;
        try {
            String actionUrl = AppConfig.getCustomerInfo();

            OkGo.get(actionUrl).tag(ctx)
                    .params("merCode", mercode)
                    .params("userId", userId)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AIBaseBean baseBean = gson.fromJson(s, AIBaseBean.class);
                            if (baseBean.getCode() == 0) {
                                ClientDetailBean bean = gson.fromJson(s, ClientDetailBean.class);
                                iClient.getCustomerInfo(bean);
                            } else {
                                Toast.makeText(ctx, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                        }
                    });

        } catch (Exception e) {

        } finally {
            pending = false;
        }
    }

    public void getClientScreenList() {
        if (pending) {
            return;
        }
        pending = true;
        try {
            String actionUrl = AppConfig.getClientScreenList();

            OkGo.get(actionUrl).tag(ctx)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AIBaseBean baseBean = gson.fromJson(s, AIBaseBean.class);
                            if (baseBean.getCode() == 0) {
                                ClientScreenBean bean = gson.fromJson(s, ClientScreenBean.class);
                                iClient.getClientScreenList(bean);
                            } else {
                                iClient.getClientScreenList((ClientScreenBean) baseBean);
                                Toast.makeText(ctx, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {

        } finally {
            pending = false;
        }
    }
}
