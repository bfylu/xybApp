package cn.payadd.majia.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import org.json.JSONArray;

import java.util.Map;

import cn.payadd.majia.adapter.InstallmentOrderAdapter;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.listener.NoItemDoubleClickListener;
import cn.payadd.majia.presenter.InstallmentPresenter;
import cn.payadd.majia.util.AppLog;
import cn.payadd.majia.util.DensityUtil;
import cn.payadd.majia.view.RepayOfMonthDialog;

/**
 * Created by zhengzhen.wang on 2017/6/19.
 */

public class InstallmentOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AbsListView.OnScrollListener, IActivity,View.OnTouchListener {

    public static final String LOG_TAG = "InstallmentOrderActivity";

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView tvNotOrder;

    private ListView lvData;

    private InstallmentOrderAdapter installmentAdapter;

    private InstallmentPresenter installmentPresenter;

    private TextView tvCurrentStatus;

    private HorizontalScrollView hsvCondition;

    private RepayOfMonthDialog repayOfMonthDialog;

    private String InstallmentOfMonth;

    private String repayAmt;
    private float touchDownX, touchUpX;

    private View[] vCondition;
    private int conditionIndex = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_installment_order;
    }

    @Override
    public void initView() {

        setTitleCenterText("分期订单");
        setTitleBackButton();

        tvNotOrder = (TextView) findViewById(R.id.tv_not_order);

       vCondition = new View[]{findViewById(R.id.tv_condition_all), findViewById(R.id.tv_condition_pending),
                findViewById(R.id.tv_condition_pass), findViewById(R.id.tv_condition_reject), findViewById(R.id.tv_condition_sign_pending), findViewById(R.id.tv_condition_sign_pass)};
        for (View v : vCondition) {
            v.setOnClickListener(this);
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnTouchListener(this);

        lvData = (ListView) findViewById(R.id.lv_data);
        lvData.setOnItemClickListener(new NoItemDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = installmentAdapter.getItem(position);
                String orderNo = map.get("orderNo");
                String status = map.get("status");
                String isUpdateInfo = (String) view.getTag(R.id.is_update_info);
                Intent intent = new Intent(InstallmentOrderActivity.this, InstallmentDetailActivity.class);
                intent.putExtra(InstallmentDetailActivity.KEY_ORDER_NO, orderNo);
                intent.putExtra("status", status);
                intent.putExtra("isUpdateInfo", isUpdateInfo);
                startActivity(intent);
            }
        });
        lvData.setOnScrollListener(this);
        lvData.setOnTouchListener(this);

        hsvCondition = (HorizontalScrollView) findViewById(R.id.hsv_condition);
    }

    @Override
    public void initData() {

        installmentAdapter = new InstallmentOrderAdapter(this);
        lvData.setAdapter(installmentAdapter);

        installmentPresenter = new InstallmentPresenter(this);

        onClick(findViewById(R.id.tv_condition_all));
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_condition_all: {
                conditionIndex = 0;
                installmentAdapter.setShowStatus("0");
                selectTopCondition(v);
                break;
            }
//            case R.id.tv_condition_submit: {
//                conditionIndex = 1;
//                installmentAdapter.setVerifyStatus(InstallmentStatus.VerifyStatus.SUBMIT);
//                installmentAdapter.setSignStatus(null);
//                selectTopCondition(v);
//                break;
//            }
            case R.id.tv_condition_pending: {
                conditionIndex = 1;
                installmentAdapter.setShowStatus("1");
                selectTopCondition(v);
                break;
            }
            case R.id.tv_condition_pass: {
                conditionIndex = 2;
                installmentAdapter.setShowStatus("2");
                selectTopCondition(v);
                break;
            }
            case R.id.tv_condition_reject: {
                conditionIndex = 3;
                installmentAdapter.setShowStatus("3");
                selectTopCondition(v);
                break;
            }
            case R.id.tv_condition_sign_pending: {
                conditionIndex = 4;
                installmentAdapter.setShowStatus("4");
                selectTopCondition(v);
                break;
            }
            case R.id.tv_condition_sign_pass: {
                conditionIndex = 5;
                installmentAdapter.setShowStatus("5");
                selectTopCondition(v);
                break;
            }
        }
    }

    public void selectTopCondition(View v) {

        TextView tv = (TextView) v;

        if (null != tvCurrentStatus && tvCurrentStatus.getId() == v.getId()) {
            return;
        }

        if (null != tvCurrentStatus) {
            tvCurrentStatus.setTextColor(tv.getCurrentTextColor());
            tvCurrentStatus.setBackgroundDrawable(null);
        }

        installmentAdapter.setShowStatus(true);
//        if (v.getId() == R.id.tv_condition_all || v.getId() == R.id.tv_condition_pass) {
//
//
//        } else {
//            installmentAdapter.setShowStatus(false);
//        }
        tv.setTextColor(getResources().getColor(R.color.blue2));
        tvCurrentStatus = tv;
        tvCurrentStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.border_bottom));
        installmentAdapter.clearData();
        installmentPresenter.queryOrderList(installmentAdapter, true);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

            boolean enable = false;
            if(null != lvData && lvData.getChildCount() > 0){
                // check if the first item of the list is visible
                boolean firstItemVisible = lvData.getFirstVisiblePosition() == 0;
                // check if the top of the first item is visible
                boolean topOfFirstItemVisible = lvData.getChildAt(0).getTop() == 0;
                // enabling or disabling the refresh layout
                enable = firstItemVisible && topOfFirstItemVisible;
            }
            swipeRefreshLayout.setEnabled(enable);

            if (null != installmentPresenter && lvData.getLastVisiblePosition() + 1 == lvData.getCount()) {
                installmentPresenter.queryOrderList(installmentAdapter, false);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void updateModel(String key, Object data) {
        if("orderList".equals(key)){
            Map<String, String> map = (Map<String, String>) data;
            String list = map.get("list");

            try {
                JSONArray json = new JSONArray(list);
                installmentAdapter.updateData(json, true);
                if (installmentAdapter.getCount() == 0) {
                    tvNotOrder.setVisibility(View.VISIBLE);
                } else {
                    tvNotOrder.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if ("repayOfMonth".equals(key)) {

            try {
                Map<String, String> map = (Map<String, String>) data;
                JSONArray array = new JSONArray(map.get("list"));
                if (null == repayOfMonthDialog) {
                    repayOfMonthDialog = new RepayOfMonthDialog();
                    repayOfMonthDialog.setmContext(this);
                    repayOfMonthDialog.setData(array);
                    repayOfMonthDialog.setInstallmentAmt(repayAmt);
                    repayOfMonthDialog.setInstallmentOfMonth(InstallmentOfMonth);
                    repayOfMonthDialog.show(getFragmentManager(), null);
                } else {
                    repayOfMonthDialog.setInstallmentAmt(repayAmt);
                    repayOfMonthDialog.setInstallmentOfMonth(InstallmentOfMonth);
                    repayOfMonthDialog.setData(array);
                    repayOfMonthDialog.show(getFragmentManager(), null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {

                break;
            }
            case MotionEvent.ACTION_DOWN: {
                touchDownX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                touchUpX = event.getX();
                float f = touchUpX - touchDownX;
                int i = (int) Math.abs(f);
                int dp = DensityUtil.dip2px(this, 100);
                AppLog.d(LOG_TAG, "touchDownX=" + touchDownX + ", touchUpX=" + touchUpX + ", i=" + i + ", dp=" + dp);
                if (i > dp) {
                    if (f < 0) {
                        // 向左滑，向后翻
                        conditionIndex++;
                        if (conditionIndex < vCondition.length) {
                            View tv = vCondition[conditionIndex];
                            onClick(tv);
                            hsvCondition.scrollTo(hsvCondition.getScrollX() + tv.getWidth(), hsvCondition.getScrollY());
                        } else {
                            conditionIndex--;
                        }
                    } else {
                        // 向右滑，向前翻
                        conditionIndex--;
                        if (conditionIndex >= 0) {
                            View tv = vCondition[conditionIndex];
                            onClick(tv);
                            hsvCondition.scrollTo(hsvCondition.getScrollX() - tv.getWidth(), hsvCondition.getScrollY());
                        } else {
                            conditionIndex++;
                        }
                    }

                }
                break;
            }
        }
        return false;
    }

    public InstallmentPresenter getInstallmentPresenter() {
        return installmentPresenter;
    }

    public void setInstallmentOfMonth(String installmentOfMonth) {
        InstallmentOfMonth = installmentOfMonth;
    }

    public void setRepayAmt(String repayAmt) {
        this.repayAmt = repayAmt;
    }
}
