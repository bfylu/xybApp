package cn.payadd.majia.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import cn.payadd.majia.adapter.MsgNoticeAdapter;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.listener.NoItemDoubleClickListener;
import cn.payadd.majia.presenter.MsgNoticePresenter;

/**
 * Created by df on 2017/12/19.
 */

public class MsgNoticeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener, IActivity,View.OnClickListener{

    private ListView listView;

    private SwipeRefreshLayout refreshLayout;

    private int totalCount;// 数据总条数

    private MsgNoticePresenter msgNoticePresenter;

    private TextView tvNotOrder;

    private View footView;

    private View noDataView;

    private int lastVisibleIndex;

    private MsgNoticeAdapter adapter;

    private ImageView ivToTop;

    private boolean append;

    private String mUsername;

    private String type;
    @Override
    public int getLayoutId() {
        return R.layout.activity_msg_notice;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText("消息通知");
        setTitleRightText("标记为已读", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "all";
                msgNoticePresenter.readMsg(mUsername,null,type);

            }
        });

        listView = (ListView) findViewById(R.id.lv_msg_data);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        footView = getLayoutInflater().inflate(R.layout.view_more, null);
        noDataView = getLayoutInflater().inflate(R.layout.view_no_data,null);
        tvNotOrder = (TextView) findViewById(R.id.tv_not_msg);
        ivToTop = (ImageView) findViewById(R.id.iv_to_top);

        adapter = new MsgNoticeAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new NoItemDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                type = "one";
                Map<String,String> msg = adapter.getmData().get(position);
                try {
                    if(msg!=null){
                        JSONObject extJson = new JSONObject(msg.get("extInfo"));
                        if(extJson!=null){
                            boolean isNew = extJson.getBoolean("isNew");
                            if(isNew){
                                Intent intent = new Intent(MsgNoticeActivity.this, InstallmentOrderActivity.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(MsgNoticeActivity.this, InstallmentDetailActivity.class);
                                intent.putExtra("orderNo",extJson.getString("orderNo"));
                                startActivity(intent);
                            }
                        }
                        msgNoticePresenter.readMsg(mUsername,msg.get("id"),null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//
////                QBadgeView qBadgeView = new QBadgeView(MsgNoticeActivity.this);
////                qBadgeView.setBadgeGravity(Gravity.TOP | Gravity.END);
////                qBadgeView.setBadgeNumber(-1);
////                qBadgeView.setBadgePadding(5, true);
////                qBadgeView.bindTarget(view.findViewById(R.id.ll_title));
//            }
//        });
        listView.setOnScrollListener(this);
        refreshLayout.setOnRefreshListener(this);
        ivToTop.setVisibility(View.INVISIBLE);
        ivToTop.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mUsername = Utilities.getUsernameForLogin(this);
        msgNoticePresenter = new MsgNoticePresenter(this);
        msgNoticePresenter.queryNoticeMsgList(mUsername,adapter,true);
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onRefresh() {
        listView.removeFooterView(footView);
        listView.removeFooterView(noDataView);
        append = false;
        //
        msgNoticePresenter.queryNoticeMsgList(mUsername,adapter, true);
        //
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_to_top:
                listView.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

            boolean enable = false;
            if (null != listView && listView.getChildCount() > 0) {
                // check if the first item of the list is visible
                boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                // check if the top of the first item is visible
                boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                // enabling or disabling the refresh layout
                enable = firstItemVisible && topOfFirstItemVisible;
            }
            if (scrollState == SCROLL_STATE_IDLE && lastVisibleIndex == adapter.getCount()-1) {
                Log.e("Tag","loadMore");
                loadMoreData();// 加载更多数据
            }
            refreshLayout.setEnabled(enable);


        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        Log.e("TAG", "firstVisibleItem = " + firstVisibleItem);
        Log.e("TAG", "visibleItemCount = " + visibleItemCount);
        Log.e("TAG", "totalItemCount = " + totalItemCount);
        //滑动到第3屛时才显示返回顶部图标
        if(lastVisibleIndex >= 1*adapter.getRow()){
            ivToTop.setVisibility(View.VISIBLE);
        }else{
            ivToTop.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        if(AppService.QUERY_NOTICE_MSG.equals(key)) {
            List<Map<String, String>> mapList = (List<Map<String, String>>) data;
            listView.removeFooterView(footView);
            listView.removeFooterView(noDataView);
            if (mapList.isEmpty()) {
                listView.addFooterView(noDataView, null, false);
            } else {
                if (mapList.size() < adapter.getRow()) {
                    listView.addFooterView(noDataView, null, false);
                }
                try {
                    adapter.updateData(mapList, append);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (adapter.getCount() == 0) {
                listView.removeFooterView(footView);
                listView.removeFooterView(noDataView);
                tvNotOrder.setVisibility(View.VISIBLE);
            } else {
                tvNotOrder.setVisibility(View.GONE);
            }
        }else if(AppService.SIGN_MSG_READ.equals(key)){
            Map<String,String> map = (Map<String, String>) data;
            String respCode = map.get("respCode");
            if("000000".equals(respCode)){
               onRefresh();
                if("all".equals(type)){
                    Utilities.setIsmtNoticeCount(MsgNoticeActivity.this,0);
                    BaseApplication.getMainActivity().setNoticeMsgCount(0);
                }else if("one".equals(type)){
                    Utilities.setIsmtNoticeCount(MsgNoticeActivity.this,Utilities.getIsmtNoticeCount(this) - 1);
                    BaseApplication.getMainActivity().setNoticeMsgCount(Utilities.getIsmtNoticeCount(this));
                }

            }
        }
    }

    private void loadMoreData() {

        listView.addFooterView(footView,null,false);
        append = true;
        //
        msgNoticePresenter.queryNoticeMsgList(mUsername,adapter, false);
        //
        adapter.notifyDataSetChanged();


    }
}
