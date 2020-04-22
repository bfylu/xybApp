package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.utils.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.task.RequestServerTask;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by df on 2017/6/27.
 */

public class MerchantPwdPresenter extends BasePresenter {

    private IFragment iFragment;

    private IActivity iActivity;

    private ExecutorService es = Executors.newSingleThreadExecutor();

    public MerchantPwdPresenter(Context ctx, IFragment iFragment) {
        super(ctx);
        this.iFragment = iFragment;
    }

    public MerchantPwdPresenter(Context ctx){
        super(ctx);
        this.iActivity = (IActivity) ctx;
    }

    /**
     * 提交短信验证码
     * @param msgCodeKey 短信验证密钥
     * @param msgCode 短信验证码
     */
    public void ValidationAuthCode(String msgCodeKey, String msgCode) {
        ValidationAuthCode(msgCodeKey, msgCode, null, null);
    }

    /**
     * 提交短信验证码
     * @param msgCodeKey 短信验证密钥
     * @param msgCode 短信验证码
     * @param imgCodeKey 图片验证密钥
     * @param imgCode 图片验证码
     */
    public void ValidationAuthCode(String msgCodeKey, String msgCode, String imgCodeKey, String imgCode) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("msgCodeKey", msgCodeKey);
        data.put("msgCode",msgCode);
        if(StringUtil.isNotEmpty(imgCode)){
            data.put("imgCodeKey",imgCodeKey);
            data.put("imgCode",imgCode);
        }

        sendUrlToServer(actionUrl, AppService.ORDER_SETTLE, data, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("validation", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("validation", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("validation", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("validation", params);
                }
            }
        }, false);

    }

    /**
     * 密码设置
     * @param merCode 商户编码
     * @param userName 用户名
     * @param setType 设置密码类型  1：新设密码  2：修改密码验证旧密码   3：修改密码传入新密码
     * @param password1 新设密码或忘记密码时的第一次输入密码
     * @param password2 新设密码或忘记密码时的第二次输入密码
     * @param queryPassword 修改密码时的旧密码
     */
    public void SetMerchantPwd(String merCode, String userName, int setType, String password1, String password2, String queryPassword) {
        String actionUrl = AppConfig.getMerchantPwdInterface();

        Map<String, String> data = new HashMap<>();
        data.put("merCode", merCode);
        data.put("userName",userName);
        if (setType == 1) {
            data.put("password1",password1);
            data.put("password2",password2);
        } else if (setType == 2) {
            data.put("queryPassword",queryPassword);
        } else if (setType == 3) {
            data.put("password2",password2);
        }

        sendUrlToServer(actionUrl, AppService.QUERY_BIND_INFO, data, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("setMerchantPwd", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("setMerchantPwd", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("setMerchantPwd", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("setMerchantPwd", params);
                }
            }
        }, false);
    }

    /**
     * 支付宝微信退款
     * @param orderNo 订单单号
     * @param refundAmount 退款金额
     * @param managePasswd 商户管理密码
     */
    public void orderRefund(String orderNo, String refundAmount, String managePasswd) {
        String actionUrl = AppConfig.getRefundInterface();

        Map<String, String> data = new HashMap<>();
        data.put("managePasswd",managePasswd);
        data.put("refundAmount",refundAmount);
        data.put("orderNo", orderNo);

        sendUrl2ToServer(actionUrl, AppService.ORDER_REFUND, data, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("orderRefund", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("orderRefund", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("orderRefund", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("orderRefund", params);
                }
            }
        });
    }

    /**
     * 银联退款上传
     * @param orderNo 订单单号
     * @param refundAmount 退款金额
     * @param refundTime 退款时间
     * @param refundStatus 退款状态  1为成功 失败暂时不用请求此接口
     */
    public void refundUpdate(String orderNo, String refundAmount, String refundTime, String refundStatus) {
        String actionUrl = AppConfig.getRefundInterface();

        Map<String, String> data = new HashMap<>();
        data.put("refundTime",refundTime);
        data.put("refundAmount",refundAmount);
        data.put("orderNo", orderNo);
        data.put("refundStatus", refundStatus);

        sendUrl2ToServer(actionUrl, AppService.REFUND_UPDATE, data, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("refundUpdate", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("refundUpdate", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("refundUpdate", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("refundUpdate", params);
                }
            }
        });
    }

    /**
     * 获取验证码
     * @param phone 手机号码
     * @param typeCode 业务类型编码（）
     * @param imgCodeKey 图片验证码key
     * @param imgCode 图片验证码
     */
    public void sendAuthCode(final Activity act, String phone, String typeCode, String imgCodeKey, String imgCode) {
        String actionUrl = AppConfig.getMerchantPwdCodeInterface();

        Map<String, String> data = new HashMap<>();
        data.put("mobilePhone", phone);
        data.put("typeCode",typeCode);
        data.put("sessionToken", Utilities.getSessionToKen(ctx));
        if(StringUtil.isNotEmpty(imgCode)){
            data.put("imgCodeKey",imgCodeKey);
            data.put("imgCode",imgCode);
        }

        Log.e("reqData:",data.toString());
        String content = StringUtil.linkAndEncode(data);
        Log.e("send:",content);

        RequestServerTask task = new RequestServerTask(actionUrl, new ICallback() {
            @Override
            public void exec(Object params) {
                if (StringUtil.isNotEmpty(StringUtil.toString(params))) {
                    Map<String, Object> map = (Map<String, Object>) JSON.parse(StringUtil.toString(params));
                    if (iFragment != null) {
                        iFragment.updateModel("authCode", map);
                    } else if (iActivity != null) {
                        iActivity.updateModel("authCode", map);
                    }
                }
            }
        });
        task.execute(content);
    }

    /**
     * 获取与显示图片验证码
     * @param username 用户名称
     * @param v 显示View
     */
    public void showImageCode(final String username, final View v) {
        es.execute(new Runnable() {
            @Override
            public void run() {
                InputStream input = null;

                try {

                    URL url = new URL(AppConfig.getViewCode() + "?username=" + username);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    ImageView iv = null;
                    if (v instanceof ImageView) {
                        iv = (ImageView) v;
                    } else {
                        RelativeLayout rl = (RelativeLayout) v;
                        iv = (ImageView) rl.getChildAt(0);
                    }
                    iv.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (null != input) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
