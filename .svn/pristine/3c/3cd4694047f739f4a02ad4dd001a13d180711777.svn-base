package cn.payadd.majia.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.utils.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.task.RequestServerTask;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by zhengzhen.wang on 2017/6/23.
 */

public class InstallmentFilePresenter extends BasePresenter {

    private IActivity iActivity;

    public InstallmentFilePresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void showSignInfo(String orderNo) {
        queryFile("sign", orderNo);
    }

    public void showSupplementInfo(String orderNo) {

        queryFile("ext", orderNo);
    }

    private void queryFile(final String type, String orderNo) {

        String actionUrl = null;
        if ("ext".equals(type)) {
            actionUrl = AppConfig.getInstallmentFileInterface() + "/supplement";
        } else if ("sign".equals(type)) {
            actionUrl = AppConfig.getInstallmentFileInterface() + "/sign";
        }
        Map<String, String> map = new HashMap<>();
        map.put("orderNo", orderNo);
        map.put("sessionToken", Utilities.getSessionToKen(ctx));
        String content = StringUtil.linkAndEncode(map);
        RequestServerTask task = new RequestServerTask(actionUrl, new ICallback() {
            @Override
            public void exec(Object params) {
                String msg = (String) params;
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jsonObj = new JSONObject(msg);
                    String respCode = jsonObj.getString("respCode");
                    String respDesc = jsonObj.getString("respDesc");
                    if (!"000000".equals(respCode)) {
                        Toast.makeText(ctx, respDesc, Toast.LENGTH_LONG).show();
                    } else {
                        if (null != iActivity) {
                            iActivity.updateModel(type, jsonObj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute(content);
    }

    public void saveSupplementInfo(String orderNo, String commodityInfo) {
        save("ext", orderNo, commodityInfo);
    }

    public void saveSignInfo(String orderNo) {
        save("sign", orderNo, null);
    }

    private void save(String type, String orderNo, String goodsInfo) {

        String actionUrl = null;
        if ("ext".equals(type)) {
            actionUrl = AppConfig.getInstallmentSaveContract() + "/supplement";
        } else if ("sign".equals(type)) {
            actionUrl = AppConfig.getInstallmentSaveContract() + "/sign";
        }
        Map<String, String> map = new HashMap<>();
        map.put("orderNo", orderNo);
        map.put("sessionToken", Utilities.getSessionToKen(ctx));
        if (!TextUtils.isEmpty(goodsInfo)) {
            map.put("goodsInfo", goodsInfo);
        }
        String content = StringUtil.linkAndEncode(map);
        RequestServerTask task = new RequestServerTask(actionUrl, new ICallback() {
            @Override
            public void exec(Object params) {
                String msg = (String) params;
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jsonObj = new JSONObject(msg);
                    String respCode = jsonObj.getString("respCode");
                    String respDesc = jsonObj.getString("respDesc");
                    if (!"000000".equals(respCode)) {
                        Toast.makeText(ctx, respDesc, Toast.LENGTH_LONG).show();
                    } else {
                        if (null != iActivity) {
                            iActivity.updateModel("saveContract", jsonObj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute(content);
    }
    public void deleteConsumeVoucherPic(String orderNo, String filename) {

        Map<String, String> map = new HashMap<>();
        map.put("orderNo", orderNo);
        map.put("fileName", filename);
        map.put("sessionToken", Utilities.getSessionToKen(ctx));
        String content = StringUtil.linkAndEncode(map);
        RequestServerTask task = new RequestServerTask(AppConfig.getInstallmentDeleteConsumeVoucher(), new ICallback() {
            @Override
            public void exec(Object params) {
                String msg = (String) params;
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jsonObj = new JSONObject(msg);
                    String respCode = jsonObj.getString("respCode");
                    String respDesc = jsonObj.getString("respDesc");
                    if (!"000000".equals(respCode)) {
                        Toast.makeText(ctx, respDesc, Toast.LENGTH_LONG).show();
                    } else {
                        if (null != iActivity) {
                            iActivity.updateModel("deleteConsumeVoucher", jsonObj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute(content);
    }
    public void deleteContract(String orderNo, String filename) {

        Map<String, String> map = new HashMap<>();
        map.put("orderNo", orderNo);
        map.put("fileName", filename);
        map.put("sessionToken", Utilities.getSessionToKen(ctx));
        String content = StringUtil.linkAndEncode(map);
        RequestServerTask task = new RequestServerTask(AppConfig.getInstallmentDeleteContract(), new ICallback() {
            @Override
            public void exec(Object params) {
                String msg = (String) params;
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject jsonObj = new JSONObject(msg);
                    String respCode = jsonObj.getString("respCode");
                    String respDesc = jsonObj.getString("respDesc");
                    if (!"000000".equals(respCode)) {
                        Toast.makeText(ctx, respDesc, Toast.LENGTH_LONG).show();
                    } else {
                        if (null != iActivity) {
                            iActivity.updateModel("deleteContract", jsonObj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute(content);
    }

    public void saveContractSign(String orderNo,String sessionToken){

    }

}
