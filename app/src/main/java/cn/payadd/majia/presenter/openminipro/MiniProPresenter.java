package cn.payadd.majia.presenter.openminipro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.BitmapConvert;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.entity.openminipro.AccessTokenBean;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.face.openminipro.IMiniPro;
import cn.payadd.majia.task.RequestServerTask;
import cn.payadd.majia.util.HttpUtil;
import cn.payadd.majia.util.StringUtil;
import okhttp3.Call;
import okhttp3.Response;

public class MiniProPresenter {

    private Context ctx;

    private IMiniPro iMiniPro;

    private boolean pending;

    private Gson gson;

    public MiniProPresenter(Context ctx, IMiniPro iMiniPro) {
        this.ctx = ctx;
        this.iMiniPro = iMiniPro;
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 获取小程序的access_token
     * @param appid 小程序的APPID
     * @param secret 小程序appsecret
     * @param grant_type 获取access_token填写client_credential
     */
    public void getAccessToken(String appid, String secret, String grant_type) {
        if (pending) {
            return;
        }
        pending = true;
        try {
            String actionUrl = AppConfig.getAccessToken();

            OkGo.get(actionUrl).tag(ctx)
                    .params("appid", appid)
                    .params("secret", secret)
                    .params("grant_type", grant_type)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (!s.contains("errcode")) {
                                AccessTokenBean bean = gson.fromJson(s, AccessTokenBean.class);
                                iMiniPro.getAccessToken(bean);
                            }
                        }
                    });

        } catch (Exception e) {

        } finally {
            pending = false;
        }
    }

    /**
     * 获取小程序二维码
     * @param access_token 上一接口获取的access_token
     * @param path 需要跳转的小程序地址
     * @param scene 小程序参数
     */
    public void getMiniProPic(String access_token, String path, String scene) {
        if (pending) {
            return;
        }
        pending = true;
        try {
            String actionUrl = AppConfig.getMiniProPic().replace("&&", access_token);
//            JSONObject json = new JSONObject();
//            json.put("path",path);
//            json.put("scene",scene);
//            OkGo.post(actionUrl).tag(ctx)
//                    .headers("Content-type","application/json")
//                    .upJson(json.toJSONString())
////                    .params("path", path)
////                    .params("scene", scene)
//                    .execute(new AbsCallback<Object>() {
//
//                        @Override
//                        public Byte convertSuccess(Response response) throws Exception {
//                            return null;
//                        }
//
//                        @Override
//                        public void onSuccess(Object object, Call call, Response response) {
//                            try {
////                                StringBuilder sb = new StringBuilder();
////                                String line;
////
////                                BufferedReader br = new BufferedReader(new InputStreamReader(response.body().byteStream()));
////                                while ((line = br.readLine()) != null) {
////                                    sb.append(line);
////                                }
////                                String str = sb.toString();
////                                Log.d("imageResp", "onSuccess: " + str);
////                                Log.d("imageResp", "onSuccess: " + response.isSuccessful());
////                                Log.d("imageResp", "onSuccess: " + response.message());
////                                Log.d("imageResp", "onSuccess: " + response.toString());
//                                Bitmap bm = BitmapConvert.create().convertSuccess(response);
//                                iMiniPro.getMiniProPic(bm);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            Toast.makeText(ctx, "11", Toast.LENGTH_SHORT).show();
//                        }
//                    });

//            Map<String, String> map = new HashMap<>();
//            map.put("path", path);
//            map.put("scene", scene);
//            String content = StringUtil.linkAndEncode(map);
            JSONObject json = new JSONObject();
            json.put("path",path);
            json.put("width",430);
//            new Runnable() {
//                @Override
//                public void run() {
//                    InputStream is = (InputStream) HttpUtil.post2(actionUrl,json.toJSONString(),"application/json");
//                    Bitmap bm = BitmapFactory.decodeStream(is);
//                    iMiniPro.getMiniProPic(bm);
//                }
//            }.run();

            RequestServerTask task = new RequestServerTask(actionUrl, new ICallback() {
                @Override
                public void exec(Object params) {
                    ByteArrayInputStream is = null;
                    try {
                        byte[] b = ((String)params).getBytes("UTF-8");


//                    Bitmap bm = BitmapFactory.decodeStream(new ByteArrayInputStream("abc".getBytes()));
                    Bitmap bm = BitmapFactory.decodeByteArray(b,0,b.length);
                    iMiniPro.getMiniProPic(bm);
//                    Log.d("imageResp", "onSuccess: " + ((InputStream)params));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, "application/json");
            task.execute(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pending = false;
        }
    }
}
