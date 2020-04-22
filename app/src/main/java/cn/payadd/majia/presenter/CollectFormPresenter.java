package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;

import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.entity.CheryHttpUtils;
import com.fdsj.credittreasure.entity.FlowBean;
import com.fdsj.credittreasure.utils.DialogFactory;
import com.google.gson.Gson;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.zhy.http.okhttp.callback.HashMapCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import okhttp3.Call;

/**
 * Created by df on 2017/8/7.
 */

public class CollectFormPresenter extends BasePresenter{

    public static final String LOG_TAG = "CollectFormPresenter";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    private IActivity iActivity;

    public CollectFormPresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void query(JSONObject reqData){
        String condition = reqData.toString();
        String merchantKey = BaseApplication.getMerchantKey();
        CheryHttpUtils.getInstance()
                .get()
                .service(AppService.QUERY_COLLECT_FORM)
                .sessionToken(BaseApplication.getSessionToKen())
                .addParams("condition", condition)
                .build(merchantKey)
                .execute(new HashMapCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        iActivity.updateModel("error","");
                    }

                    @Override
                    public void onResponse(Map<String, String> response, int id) {
                        try {
                            if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                                String json = response.get("data");
                                response.remove("respCode");
                                response.remove("respDesc");
                                iActivity.updateModel(SUCCESS,response);
                            } else if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) || response.get("respCode").equals(Enums.apiState.没有找到密钥.getString())) {
                                DialogFactory.userSignOutDialog((Activity) CollectFormPresenter.this.ctx, response.get("respDesc"));
                            } else {
                                ToastUtils.showToast((Activity) CollectFormPresenter.this.ctx, response.get("respDesc"));
                            }
                        } catch (Exception ex) {
                            LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                            iActivity.updateModel("","");
                        }
                    }
                }, merchantKey);
    }
}
