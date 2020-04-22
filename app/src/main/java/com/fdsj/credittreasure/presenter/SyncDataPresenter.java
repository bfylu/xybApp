package com.fdsj.credittreasure.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.IHome;
import com.fdsj.credittreasure.Interface.iActivities.IMain;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.MerCodeAndChanPlatIdBean;
import cn.payadd.majia.entity.aistore.AIBaseBean;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.presenter.BasePresenter;
import cn.payadd.majia.util.StringUtil;
import okhttp3.Call;
import okhttp3.Response;

public class SyncDataPresenter extends BasePresenter {

    private Activity ctx;
    private IMain iMain;
    private Gson gson;

    public SyncDataPresenter(Activity ctx, IMain iMain) {
        super(ctx);
        this.iMain = iMain;
        this.ctx = ctx;
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 数据同步
     * @param merCode 商户编码
     * @param merchantName 商户名
     * @param merchantUrl 商户头像
     */
    public void synchMerData(String merCode, String merchantName, String merchantUrl) {

        String url = AppConfig.synchMerData();

//        Map<String, String> data = new HashMap<>();
//        data.put("chanCode", "000030");
//        data.put("merCode", merCode);

        OkGo.post(url).tag(this)
                .params("merCode", merCode)
                .params("merchantName", merchantName)
                .params("merchantUrl", merchantUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        AIBaseBean bean = gson.fromJson(s, AIBaseBean.class);
                        iMain.synchMerData(bean);
                    }
                });

//        sendUrl5ToServer(url, AppService.UNBIND, data, new ICallback(){
//
//            @Override
//            public void exec(Object params) {
//                String jsonData = StringUtil.toString(params);
//
//                AIBaseBean bean = gson.fromJson(jsonData, AIBaseBean.class);
//
//                iMain.synchMerData(bean);
//            }
//
//        }, new ICallback() {
//            @Override
//            public void exec(Object params) {
//                String jsonData = StringUtil.toString(params);
//                AIBaseBean bean = gson.fromJson(jsonData, AIBaseBean.class);
//                iMain.synchMerData(bean);
//            }
//        });
    }
}
