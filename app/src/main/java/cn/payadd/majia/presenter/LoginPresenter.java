package cn.payadd.majia.presenter;

import android.app.Activity;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.fdsj.credittreasure.Interface.iActivities.ILogin;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.StartActivity;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.entity.HttpModel;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;
import com.zhy.http.okhttp.callback.HashMapCallback;

import java.util.Map;

import cn.payadd.majia.activity.LoginActivity;
import okhttp3.Call;

/**
 * Created by BXND on 2017-01-06.
 */

public class LoginPresenter {

    private static final String TAG = "LoginPresenter";

    ILogin login;

    public LoginPresenter(LoginActivity activity) {
        this.login = activity;
    }

    public LoginPresenter(StartActivity activity) {
        this.login = activity;
    }

    /**
     * 用户登录:不带验证码
     *
     * @param usernameForLogin 账号
     * @param passWord         密码
     * @param activity
     */
    public void userLogin(final String loginToken, final String usernameForLogin, final String
            passWord, final Activity activity, final Boolean status) {
        userLogin(loginToken, usernameForLogin, passWord, activity, status, null);
    }

    /**
     * 用户登录
     *
     * @param usernameForLogin 账号
     * @param passWord         密码
     * @param activity
     */
    public void userLogin(final String loginToken, final String usernameForLogin, final String
            passWord, final Activity activity, final Boolean status, final String verifyCode) {
        String sessionToken = BaseApplication.getSessionToKen();// Utilities.getSessionToKen
        // (activity);
//        String merchantKey = BaseApplication.getMerchantKey();// Utilities.getMerchantKey
// (activity);
//        if (!TextUtils.isEmpty(sessionToken) && !TextUtils.isEmpty(merchantKey)) {
        HttpModel.getInstance().userLogin(loginToken, usernameForLogin, passWord, sessionToken,
                "", verifyCode, activity, status, new HashMapCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                    if (activity != null)
//                        ToastUtils.showToast(activity, "网络连接失败，请稍后再试");
                login.userLoginError(activity.getResources().getString(R.string.try_net_wrong),
                        null);

            }

            @Override
            public void onResponse(Map<String, String> response, int id) {
                try {
                    if (response.get("respCode").equals(Enums.apiState.成功.getString())) {
                        // 绑定推送账号
                        CloudPushService pushService = PushServiceFactory.getCloudPushService();
                        pushService.bindAccount(response.get("username"), new CommonCallback() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d(TAG, "aliyun push -> bindAccount success");
                            }

                            @Override
                            public void onFailed(String errorCode, String errorMessage) {
                                Log.d(TAG, "aliyun push -> bindAccount failed -- errorcode:" +
                                        errorCode + " -- errorMessage:" + errorMessage);
                            }
                        });
                        login.userLogin(response.get("loginToken")
                                , response.get("username")
                                , usernameForLogin
                                , passWord
                                , response.get("mobilePhone")
                                , response.get("nickName")
                                , response.get("shopName")
                                , response.get("welcome")
                                , response.get("sessionToken")
                                , response.get("mposKey")
                                , response.get("showMerName")
                                , response.get("email")
                                , response.get("agentFlag")
                                , response.get("authFlag")
                                , response.get("ismtNoticeCount")
                                , response.get("merCode")
                                , response.get("goodsSelectionUrl")
                                , response.get("appSecurity"));

                    } else {
                        Utilities.delUserInfo(activity);
                        if (response.get("respCode").equals(Enums.apiState.密钥已过期.getString()) ||
                                response.get("respCode").equals(Enums.apiState.没有找到密钥.getString()
                                )) {
                            Utilities.delUser(activity);
                        }
                        login.userLoginError(response.get("respDesc"), response);
                    }
                } catch (Exception ex) {
                    ToastUtils.showToast(activity, "数据异常");
                    LogUtil.writeInfoToSDcard("Exception", ex.getMessage());
                }
            }
        });
//        } else {
//            DialogFactory.userDownCode(activity, activity.getResources().getString(R.string
// .ple_download));
//        }
    }

}
