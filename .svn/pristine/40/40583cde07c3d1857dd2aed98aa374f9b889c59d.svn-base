package cn.payadd.majia.presenter;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.adapter.MsgNoticeAdapter;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;

/**
 * Created by df on 2017/12/19.
 */

public class MsgNoticePresenter extends BasePresenter{
    public static final String LOG_TAG = "MsgNoticePresenter";

    private IActivity iActivity;

    public MsgNoticePresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void queryNoticeMsgList(String username, MsgNoticeAdapter adapter,boolean resetPage){
        if (resetPage) {
            adapter.setPage(1);
        } else {
            adapter.setPage(adapter.getPage() + 1);
        }

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("page", adapter.getPage() + "");
        data.put("row", adapter.getRow() + "");

        sendToServer(AppService.QUERY_NOTICE_MSG,data,new ICallback(){

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
                        map.put("id",orderObj.getString("id"));
                        map.put("content",orderObj.getString("content"));
                        map.put("createTime",orderObj.getString("createTime"));
                        map.put("title",orderObj.getString("title"));
                        map.put("extInfo",orderObj.getString("extInfo"));
                        map.put("bizType",orderObj.getString("bizType"));
                        map.put("isRead",orderObj.getString("isRead"));
                        showInfo.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (iActivity != null) {
                    iActivity.updateModel(AppService.QUERY_NOTICE_MSG, showInfo);
                }
            }

        },new ICallback() {
            @Override
            public void exec(Object params) {
                List<Map<String, String>> showInfo = new ArrayList<>();

               if (iActivity != null) {
                    iActivity.updateModel(AppService.QUERY_NOTICE_MSG, showInfo);
                }
            }
        });
    }

    public void readMsg(String username,String id,String type){
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("type", type + "");
        data.put("id", id + "");
        sendToServer(AppService.SIGN_MSG_READ, data, new ICallback() {
            @Override
            public void exec(Object params) {
                Map<String,String> respData = (Map<String, String>) params;

                if (iActivity != null) {
                    iActivity.updateModel(AppService.SIGN_MSG_READ, respData);
                }
            }
        });
    }
}
