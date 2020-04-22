package cn.payadd.majia.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.IHome;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.CalculationActivity;
import com.fdsj.credittreasure.activities.Flow2Activity;
import com.fdsj.credittreasure.activities.MainActivity;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.fdsj.credittreasure.entity.TradeBean;
import com.fdsj.credittreasure.fragment.BaseFragment;
import com.fdsj.credittreasure.presenter.HomePresenter;
import com.utils.StatusBarUtils;
import com.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.payadd.majia.activity.FixedCodeActivity;
import cn.payadd.majia.activity.GoodsManagerActivity;
import cn.payadd.majia.activity.IncomeManagerActivity;
import cn.payadd.majia.activity.MsgNoticeActivity;
import cn.payadd.majia.activity.SelectGoodsActivity;
import cn.payadd.majia.adapter.ManageAdapter;
import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.listener.NoItemDoubleClickListener;
import cn.payadd.majia.security.AppSecurity;
import cn.payadd.majia.task.RequestServerTask;
import cn.payadd.majia.util.StringUtil;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.PointValue;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by df on 2018/1/16.
 */

public class PosHomeFragment extends BaseFragment implements IHome{
    @BindView(R.id.tv_mer_name)
    TextView tvMerName;
    @BindView(R.id.gv_main)
    GridView gvMain;
    @BindView(R.id.gv_function)
    GridView gvManage;
    private List<Map<String, Object>> mainList;
    private List<Map<String, Object>> manageList;
    private HomePresenter presenter;
    private Map<Integer,QBadgeView> msgViewMap ;

    private int noticeMsgCount;
    private int[] manageResId = {R.mipmap.icon_news, R.mipmap.icon_commodity, R.mipmap
            .index_icon_main_scan, R.mipmap.icon_code, R.mipmap.index_icon_main_shouyi, R.mipmap.icon_shop};
    private String[] manageTexts = {"消息通知", "商品管理", "扫一扫", "收款码", "收益管理", "物料商城"};
    private int[] mainResId = {R.mipmap.index_icon_liushui,R.mipmap.index_icon_goods,R.mipmap.index_icon_gathering};
    private String[] mainTexts = {"交易流水","商品收款","快速收款"};

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;


    private PayPushBroadcastReceiver receiver;

    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    @Override
    protected int getLayoutView() {
        return R.layout.fragment_pos_home;
    }

    @Override
    protected void initView() {
        tvMerName.setText(Utilities.getShowMerName(getActivity()));
        msgViewMap = new HashMap<>();
        presenter = new HomePresenter(null,this);
        presenter.tradeOverview(activity);

        swipeRefreshLayout.setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPointValues.clear();
                mAxisXValues.clear();
                presenter.tradeOverview(activity);
            }
        });

        manageList = new ArrayList<>();
        for (int i = 0; i < manageResId.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", manageResId[i]);
            item.put("text", manageTexts[i]);
            manageList.add(item);
        }

        ((MainActivity)getActivity()).setTitleVisible(false);
        ManageAdapter adapter = new ManageAdapter(activity, manageList,gvManage);
        adapter.setMsgViewData(msgViewMap);
        adapter.setTabBackgroundColor(R.color.white);
        adapter.setTextSize(15);
        gvManage.setAdapter(adapter);
        gvManage.setOnItemClickListener(new NoItemDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(activity, MsgNoticeActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(activity, GoodsManagerActivity.class);
                        startActivity(intent);
                        break;
//                    case 2:
//                        //TODO 扫一扫
////                        intent.setClass(activity, GoodsManagerActivity.class);
////                        startActivity(intent);
//                        break;
                    case 3:
                        intent.setClass(activity, FixedCodeActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(activity, IncomeManagerActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        Toast.makeText(getActivity(),"功能暂未开通，敬请期待",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        mainList = new ArrayList<>();
        for (int i = 0; i < mainResId.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", mainResId[i]);
            item.put("text", mainTexts[i]);
            mainList.add(item);
        }
        ManageAdapter mainAdapter = new ManageAdapter(activity, mainList,gvMain);
        mainAdapter.setTabBackgroundColor(R.color.selected_color);
        mainAdapter.setTextColor(R.color.white);
        mainAdapter.setTextSize(16);
        gvMain.setAdapter(mainAdapter);
        gvMain.setOnItemClickListener(new NoItemDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        intent.setClass(activity, Flow2Activity.class);//流水
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(activity, SelectGoodsActivity.class);//商品收款
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(activity, CalculationActivity.class);//快速收款
                        startActivity(intent);
                        break;
                }
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPointValues.clear();
                mAxisXValues.clear();
                presenter.tradeOverview(activity);
                sendToServer(getActivity(), AppService.GET_ISMT_NOTICE_COUNT, null, true, new ICallback() {
                    @Override
                    public void exec(Object params) {
                        Map<String,String> map = (Map<String, String>) params;
                        int noticeMsgCount = Integer.valueOf(map.get("ismtNoticeCount"));
                        setNoticeMsgCount(noticeMsgCount);
                    }
                }, null, null);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            ((MainActivity)getActivity()).setTitleVisible(false);
        }
    }

    @Override
    public void onResume() {
        StatusBarUtils.setColor(getActivity(), getResources().getColor(R.color.color_Home));
        setNoticeMsgCount(Utilities.getIsmtNoticeCount(getActivity()));
        super.onResume();

    }

    @Override
    protected void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
        receiver = new PayPushBroadcastReceiver(new PayPushBroadcastReceiver.Callback() {
            @Override
            public void exec(Bundle data) {
                mPointValues.clear();
                mAxisXValues.clear();
                presenter.tradeOverview(activity);
            }
        });
        activity.registerReceiver(receiver, filter);
    }

    @Override
    public void setChartsData(List<TradeBean.Bean> beanList) {

    }

    @Override
    public void setMoney(String data) {

    }

    @Override
    public void setMoneyCount(String data) {

    }

    @Override
    public void setIncome(String data) {

    }

    @Override
    public void stopRecyclerView() {
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void setTodayProfitAmount(String data) {
        MainActivity parentActivity = (MainActivity) activity;
        parentActivity.setTodayProfitAmount(data);
    }

    @Override
    public void getMerCodeAndChanPlatId(String merCode, String chanPlatId) {

    }

    public void setNoticeMsgCount(int noticeMsgCount) {
        this.noticeMsgCount = noticeMsgCount;
        noticeMsgBindView(noticeMsgCount,0);

    }

    private void noticeMsgBindView(int noticeMsgCount,int position){
        QBadgeView qBadgeView = null;
        if(noticeMsgCount > 0) {
            qBadgeView = ((ManageAdapter)gvManage.getAdapter()).getMsgViewData().get(position);
            if(qBadgeView == null){
                qBadgeView = new QBadgeView(getActivity());
                qBadgeView.setBadgeGravity(Gravity.TOP | Gravity.END);
                qBadgeView.setBadgeNumber(-1);
                qBadgeView.setBadgePadding(5, true);

            }
            msgViewMap.put(position,qBadgeView);
            ((ManageAdapter) gvManage.getAdapter()).notifyDataSetChanged();
        }else{
            msgViewMap.remove(position);
            ((ManageAdapter) gvManage.getAdapter()).notifyDataSetChanged();
        }
    }

    private void sendToServer(final Context ctx, String service, Map<String, String> data, boolean encrypt, final ICallback succCallbak, final ICallback failCallback, final ICallback excpCallback){
        Map<String, String> reqData = new HashMap<>();
        reqData.put("version", "1.0.0");
        reqData.put("charset", "UTF-8");
        reqData.put("platform", "android");
        reqData.put("service", service);
        reqData.put("signMethod", "MD5");
        reqData.put("signature", "");
        reqData.put("sessionToken", Utilities.getSessionToKen(ctx));
        // （h5专用，非h5去掉该字段）
//      reqData.put("loginToken", "");
        reqData.put("terminalType", EnvironmentConfig.getTerminalType());

        if(data != null) {
            if (encrypt) {
                reqData.put("body", AppSecurity.encryptAndSign(data));
            } else {
//                JSONObject jsonObj = new JSONObject(data);
//                String plaintext = jsonObj.toString();
                reqData.put("body", StringUtil.linkAndEncode(data));
            }
        }
        String content = StringUtil.linkAndEncode(reqData);
        Log.e("send:",content);
        RequestServerTask task = new RequestServerTask(new ICallback() {
            @Override
            public void exec(Object params) {

                String msg = (String) params;

                if (TextUtils.isEmpty(msg)) {
                    if (null != excpCallback) {
                        excpCallback.exec(null);
                    }
                    return;
                }
                try {
                    String respMsg = AppSecurity.decryptAndVerify(msg);
                    Map<String, String> map = StringUtil.separateAndURLDecode(respMsg);
                    String respCode = map.get("respCode");
                    if (!"000000".equals(respCode)) {
                        if(null != failCallback){
                            failCallback.exec(map);
                        }
                    }else {
                        if (null != succCallbak) {
                            succCallbak.exec(map);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        task.execute(content);
    }
}
