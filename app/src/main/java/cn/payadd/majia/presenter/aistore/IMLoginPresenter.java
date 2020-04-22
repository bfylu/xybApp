package cn.payadd.majia.presenter.aistore;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.entity.aistore.IMSigBean;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.aistore.IIMSig;
import cn.payadd.majia.presenter.BasePresenter;
import cn.payadd.majia.util.StringUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhengzhen.wang on 2017/6/12.
 */

public class IMLoginPresenter extends BasePresenter {

    private IIMSig callback;

    private IActivity iActivity;

    private Context context;

    private Gson gson;

    public IMLoginPresenter(Activity activity, IIMSig callback) {
        super(activity);
        context = activity;
        this.callback = callback;
        iActivity = (IActivity) activity;
        if (gson == null) {
            gson = new Gson();
        }
    }

    public void accountIM(String identifier, String nick, String faceUrl) {
        String url = AppConfig.getIMAccountAndSig();

        OkGo.get(url).tag(context)
                .params("identifier", identifier)
                .params("nick", nick)
                .params("faceUrl", faceUrl)
                .execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (StringUtil.isEmpty(s)) {
                    Toast.makeText(context, "返回报文为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                IMSigBean bean = gson.fromJson(s, IMSigBean.class);
                callback.getIMSig(bean);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

}
