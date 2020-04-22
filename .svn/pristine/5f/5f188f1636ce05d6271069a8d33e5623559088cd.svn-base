package cn.payadd.majia.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.utils.Utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.adapter.InstallmentOrderAdapter;
import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.task.RequestServerTask;
import cn.payadd.majia.util.AppLog;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by zhengzhen.wang on 2017/6/19.
 */

public class InstallmentPresenter extends BasePresenter {

    public static final String LOG_TAG = "InstallmentPresenter";

    private IActivity iActivity;

    public InstallmentPresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void queryOrderList(InstallmentOrderAdapter adapter, boolean resetPage) {

        if (adapter.getTotalPage() > 0 && adapter.getPage() >= adapter.getTotalPage()) {
            AppLog.d(LOG_TAG, "已经是最后一页了");
            return;
        }

        if (resetPage) {
            adapter.setPage(1);
        } else {
            adapter.setPage(adapter.getPage() + 1);
        }

        Map<String, String> data = new HashMap<>();
        data.put("current", adapter.getPage() + "");
        data.put("status", adapter.getShowStatus());
        data.put("row", adapter.getRow() + "");
        sendToServer(AppService.INSTALLMENT_ORDER_LIST, data, new ICallback() {
            @Override
            public void exec(Object params) {
                iActivity.updateModel("orderList", params);
            }
        });

    }

    public void queryDetail(String orderNo) {

        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);
        sendToServer(AppService.INSTALLMENT_ORDER_DETAIL, data, new ICallback() {
            @Override
            public void exec(Object params) {
                iActivity.updateModel("detail", params);
            }
        });
    }

    public void queryRepayOfMonth(String orderNo) {

        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);
        sendToServer(AppService.INSTALLMENT_REPAY_OF_MONTH, data, new ICallback() {
            @Override
            public void exec(Object params) {
                iActivity.updateModel("repayOfMonth", params);
            }
        });
    }
    public void shareContract(String orderNo){

        String actionUrl = AppConfig.getShareContractInterface();

        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("sessionToken", Utilities.getSessionToKen(ctx));
        String content = StringUtil.linkAndEncode(data);
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
                            iActivity.updateModel("shareContract", jsonObj);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.execute(content);
    }


}
