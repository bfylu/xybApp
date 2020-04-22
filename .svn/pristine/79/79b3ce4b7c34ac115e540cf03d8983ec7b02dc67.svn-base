package cn.payadd.majia.presenter;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.adapter.PreOrderItemAdapter;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.face.IFragment;

/**
 * Created by df on 2017/6/22.
 */

public class FundAuthOrderPresenter extends BasePresenter {
    private IFragment iFragment;
    private IActivity iActivity;
    public static final String LOG_TAG = "InstallmentPresenter";

    public FundAuthOrderPresenter(Context context, IFragment iFragment) {
        super(context);
        this.iFragment = iFragment;
    }

    public FundAuthOrderPresenter(Context context) {
        super(context);
        this.iActivity = (IActivity) ctx;
    }
    public void queryOrderList(final PreOrderItemAdapter adapter, boolean resetPage, String username) {
        queryOrderList(adapter,resetPage,username,null,null,null,null);
    }
    public void queryOrderList(final PreOrderItemAdapter adapter, boolean resetPage, String username,String status) {
        queryOrderList(adapter,resetPage,username,status,null,null,null);
    }
    public void queryOrderList(final PreOrderItemAdapter adapter, boolean resetPage, String username, String status, String startTime, String endTime,String orderNo) {
        if (resetPage) {
            adapter.setPage(1);
        } else {
            adapter.setPage(adapter.getPage() + 1);
        }
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("current", adapter.getPage() + "");
        data.put("row", adapter.getRow() + "");
        if (!TextUtils.isEmpty(status)) {
            data.put("status", status + "");
        }
        if (!TextUtils.isEmpty(startTime)) {
            data.put("startTime", startTime);
        }
        if (!TextUtils.isEmpty(endTime)) {
            data.put("endTime", endTime);
        }
        if(!TextUtils.isEmpty(orderNo)){
            data.put("orderNo",orderNo);
        }
        sendToServer(AppService.FUND_AUTH_ORDER, data,new ICallback() {
            @Override
            public void exec(Object params) {
                Map<String,String> respData = (Map<String, String>) params;
                String jsonStr = respData.get("list");
                List<Map<String, String>> showInfo = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    for (int i =0,size = jsonArray.length();i<size;i++){
                        Map<String, String> map = new HashMap<>();
                        JSONObject orderObj = (JSONObject) jsonArray.get(i);
                        map.put("orderAmount",orderObj.getString("orderAmount"));
                        map.put("orderNo",orderObj.getString("orderNo"));
                        map.put("payerId",orderObj.getString("payerId"));
                        map.put("orderTime",orderObj.getString("orderTime"));
                        map.put("orderStatus",orderObj.getString("orderStatus"));
                        showInfo.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (iFragment != null) {
                    iFragment.updateModel("fundAuthOrder", showInfo);
                } else if (iActivity != null) {
                    iActivity.updateModel("fundAuthOrder", showInfo);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                List<Map<String, String>> showInfo = new ArrayList<>();

                if (iFragment != null) {
                    iFragment.updateModel("fundAuthOrder", showInfo);
                } else if (iActivity != null) {
                    iActivity.updateModel("fundAuthOrder", showInfo);
                }
            }
        });

    }
    public void getOrderDetail(String orderNo){
        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);
        sendToServer(AppService.FUND_AUTH_ORDER_DETAIL, data,new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("orderDetail", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("orderDetail", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("orderDetail", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("orderDetail", params);
                }
            }
        });
    }

    public void orderSettle(String orderNo,String settleAmount){
        Map<String, String> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("settleAmount",settleAmount);
        sendToServer(AppService.ORDER_SETTLE, data,true, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("settle", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("settle", params);
                }
            }
        }, new ICallback() {
            @Override
            public void exec(Object params) {
                if (iFragment != null) {
                    iFragment.updateModel("settle", params);
                } else if (iActivity != null) {
                    iActivity.updateModel("settle", params);
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
}
