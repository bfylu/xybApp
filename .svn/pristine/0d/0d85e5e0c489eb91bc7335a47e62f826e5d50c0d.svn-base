package cn.payadd.majia.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import cn.payadd.majia.constant.PayType;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.face.IFragment;

/**
 * Created by df on 2017/6/27.
 */

public class FundAuthPresenter extends BasePresenter {

    private IFragment iFragment;

    private IActivity iActivity;

    private ExecutorService es = Executors.newSingleThreadExecutor();

    public FundAuthPresenter(Context ctx, IFragment iFragment) {
        super(ctx);
        this.iFragment = iFragment;
    }

    public FundAuthPresenter(Context ctx){
        super(ctx);
        this.iActivity = (IActivity) ctx;
    }

    public void sendAuthCode(String username,String getVerifyCodeNo,String graphValiCode) {
        Map<String, String> data = new HashMap<>();
        data.put("getVerifyCodeNo", getVerifyCodeNo);
        data.put("username",username);
        if(!TextUtils.isEmpty(graphValiCode)){
            data.put("graphValiCode",graphValiCode);
        }

        sendToServer(AppService.GET_AUTH_CODE, data, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("authCode", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("authCode", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("authCode", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("authCode", params);
                }
            }
        });

    }

    public void bindAlipayAcc(String username,String sendNo,String safeCode,String alipayUid,String alipayAccNo){
        Map<String, String> data = new HashMap<>();
        data.put("username",username);
        data.put("verifyCode", safeCode);
        data.put("getVerifyCodeNo",sendNo);
        data.put("alipayLogonId",alipayAccNo);
        data.put("alipayUserId",alipayUid);
        sendToServer(AppService.BIND_ALIPAY_ACC, data, new ICallback() {
            @Override
            public void exec(Object params) {
                Log.d("bindAlipay", params.toString());
                if (iFragment != null) {
                    iFragment.updateModel("bindAlipay", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("bindAlipay", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("bindAlipay", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("bindAlipay", params);
                }
            }
        });
    }


//    public void getViewCode(String username) {
//        Map<String, String> data = new HashMap<>();
//        data.put("username", username);
//        String content = StringUtil.linkAndEncode(data);
//        sendToServer(AppService.BIND_ALIPAY_ACC, data, new ICallback() {
//            @Override
//            public void exec(Object params) {
//                if (iFragment!=null) {
//                    iFragment.updateModel("viewCode", params);
//                }else if (iActivity!=null){
//                    iActivity.updateModel("viewCode", params);
//                }
//            }
//        });
//    }
    public void queryBindInfo(String username){
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        sendToServer(AppService.QUERY_BIND_INFO, data, new ICallback() {

            @Override
            public void exec(Object params) {
                if (iFragment!=null) {
                    iFragment.updateModel("bindInfo", params);
                }else if (iActivity!=null){
                    iActivity.updateModel("bindInfo", params);
                }
            }
        });
    }

    public void submitOrderFreeze(String username,String authCode,String amount){

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("authCode",authCode);
        data.put("amount",amount);
        data.put("payType", PayType.ALIPAY);
        sendToServer(AppService.FUND_AUTH_FREEZE, data,true, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("freeze", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("freeze", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("freeze", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("freeze", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("exception", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("exception", params);
                }
            }
        });
    }

    public void showImageCode(final String username,final View v) {
        es.execute(new Runnable() {
            @Override
            public void run() {
                InputStream input = null;

                try {

                    URL url = new URL(AppConfig.getViewCode()+"?username="+username);
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
//                    bitmap = ImageUtil.zoom(bitmap, iv.getWidth(), iv.getHeight());
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
