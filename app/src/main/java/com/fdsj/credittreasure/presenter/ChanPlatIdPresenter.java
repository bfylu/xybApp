package com.fdsj.credittreasure.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.IHome;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.MerCodeAndChanPlatIdBean;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.presenter.BasePresenter;
import cn.payadd.majia.util.StringUtil;

public class ChanPlatIdPresenter extends BasePresenter {

    private Activity ctx;
    private IHome iHome;
    private Gson gson;

    public ChanPlatIdPresenter(Activity ctx, IHome iHome) {
        super(ctx);
        this.iHome = iHome;
        this.ctx = ctx;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public void getMerCodeAndChanPlatId(String merchantCode) {

        String url = AppConfig.getNewServerInterface();

        Map<String, String> data = new HashMap<>();
        data.put("chanCode", "000030");
        data.put("merchantCode", merchantCode);

        sendUrl5ToServer(url, AppService.UNBIND, data, new ICallback(){

            @Override
            public void exec(Object params) {
                String jsonData = StringUtil.toString(params);

                MerCodeAndChanPlatIdBean bean = gson.fromJson(jsonData, MerCodeAndChanPlatIdBean.class);

                iHome.getMerCodeAndChanPlatId(bean.getData().getChanMerCode(), bean.getData().getChanPlatId());
            }

        }, new ICallback() {
            @Override
            public void exec(Object params) {
                String jsonData = StringUtil.toString(params);
                BaseBean bean = gson.fromJson(jsonData, BaseBean.class);
                Toast.makeText(ctx, bean.getRespDesc(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
