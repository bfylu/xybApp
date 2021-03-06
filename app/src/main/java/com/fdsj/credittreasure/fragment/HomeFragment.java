package com.fdsj.credittreasure.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.Interface.iActivities.IHome;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.CalculationActivity;
import com.fdsj.credittreasure.activities.ClearingActivity;
import com.fdsj.credittreasure.activities.Flow2Activity;
import com.fdsj.credittreasure.activities.MainActivity;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.fdsj.credittreasure.entity.TradeBean;
import com.fdsj.credittreasure.presenter.ChanPlatIdPresenter;
import com.fdsj.credittreasure.presenter.HomePresenter;
import com.newland.cashservicesdk.NldCashServiceActivity;
import com.utils.LogUtil;
import com.utils.StatusBarUtils;
import com.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.payadd.majia.activity.SelectGoodsActivity;
import cn.payadd.majia.listener.NoDoubleClickListener;
import cn.payadd.majia.util.StringUtil;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 首页
 * <p>当前版本： V1.0.0
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, IHome {

    @BindView(R.id.btn_flow)
    Button btn_flow;

    @BindView(R.id.btn_receivables)
    Button btn_receivables;

    @BindView(R.id.clearing)
    RelativeLayout clearing;

    @BindView(R.id.chart)
    LineChartView lineChart;

    @BindView(R.id.text_home_money)
    TextView text_home_money;//今日金额
    @BindView(R.id.text_home_count)
    TextView text_home_count;//实收数目
    @BindView(R.id.text_home_income)
    TextView text_home_income;//实收总额
    @BindView(R.id.text_home_store)
    TextView text_home_store;//门店

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.line_jiesuan)
    LinearLayout line_jiesuan;

    @BindView(R.id.rl_goods_acquire)
    RelativeLayout goodsAcquire;

    @BindView(R.id.rl_withdraw)
    RelativeLayout rlWithdraw;

    @BindView(R.id.rl_acquire_scan)
    RelativeLayout rlAcquireScan;
    @BindView(R.id.iv_loan)
    ImageView ivLoan;

    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();

    private HomePresenter presenter;

    private ChanPlatIdPresenter mChanPlatIdPresenter;

    private PayPushBroadcastReceiver receiver;
    private String amountCount;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {

        StatusBarUtils.setBackGroundResource(getActivity(), R.mipmap.duaichilana);//设置状态栏背景图片
        ((MainActivity)getActivity()).setActionBarBackground(R.mipmap.biaotia);//设置导航栏背景图片
        presenter = new HomePresenter(this,null);
        mChanPlatIdPresenter = new ChanPlatIdPresenter(getActivity(), this);
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
        goodsAcquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectGoodsActivity.class);
                startActivity(intent);
            }
        });
        rlAcquireScan.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Toast.makeText(getActivity(),"该功能暂未开放",Toast.LENGTH_SHORT).show();

//                boolean flag = PermissionCheckUtil.checkCameraPermission(getActivity());
//                if(flag){
//                    Intent intent = new Intent(getActivity(), PreliminaryCreditActivity.class);
//                    startActivity(intent);
//                }

            }
        });
        lineChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int n = event.getPointerCount();
                if (n == 1) {
//                    LogUtil.info("onTouch", "允许ScrollView截断点击事件");
                    //允许ScrollView截断点击事件，ScrollView可滑动
                    scrollView.requestDisallowInterceptTouchEvent(false);
                    swipeRefreshLayout.requestDisallowInterceptTouchEvent(false);
                } else {
//                    LogUtil.info("onTouch", "不允许ScrollView截断点击事件");
                    //不允许ScrollView截断点击事件，点击事件由子View处理
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    swipeRefreshLayout.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        ivLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    protected void initData() {
        this.text_home_store.setText(Utilities.getShopName(activity));

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

    public void initLineChart() {
        Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.color_ffa126));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
//        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.GRAY);  //设置字体颜色
//        axisX.setName("七天实收(元)");  //表格名称
        axisX.setTextSize(5);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setHasLines(true); //x 轴分割线
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setHasLines(false);
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
//        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


//        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setMaxZoom((float) 1);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
//        /**注：下面的7，10只是代表一个数字去类比而已
//         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
//         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 6;
        v.top = lineChart.getMaximumViewport().height() + 2;
        LogUtil.info("top", v.top + "");
        v.bottom = lineChart.getMinimumHeight() - 2;
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
    }

    @OnClick({R.id.btn_flow, R.id.btn_receivables, R.id.clearing, R.id.rl_withdraw})//, R.id.line_camper
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_flow:
                intent.setClass(activity, Flow2Activity.class);//流水
                startActivity(intent);
                break;
            case R.id.btn_receivables:
                intent.setClass(activity, CalculationActivity.class);//收款
                startActivity(intent);
                break;
            case R.id.clearing:
                intent.setClass(activity, ClearingActivity.class);//结算
                startActivity(intent);
                break;
            case R.id.rl_withdraw:
                mChanPlatIdPresenter.getMerCodeAndChanPlatId(Utilities.getMerCode(activity));
                break;
        }
    }

    @Override
    public void setChartsData(List<TradeBean.Bean> beanList) {
//        getAxisXLabels();//获取x轴的标注
//        getAxisPoints();//获取坐标点
        int j = 0;
        for (int i = beanList.size() - 1; i >= 0; i--) {
            mPointValues.add(new PointValue(j, beanList.get(i).getTurnover()));
//            mPointValues.add(new PointValue(j, 100 * i));
            mAxisXValues.add(new AxisValue(j).setLabel(beanList.get(i).getDate()));
            j = j + 1;
        }
//        mPointValues.add(new PointValue(j, 10000));
//        mAxisXValues.add(new AxisValue(j).setLabel(beanList.get(0).getDate()));
        initLineChart();
    }

    /**
     * 设置今日金额
     *
     * @param data
     */
    @Override
    public void setMoney(String data) {
        this.text_home_money.setText(data);
    }

    /**
     * 设置实收数目
     *
     * @param data
     */
    @Override
    public void setMoneyCount(String data) {
        this.amountCount = data;
        this.text_home_count.setText(activity.getResources().getString(R.string.deal_amount) + data);
    }

    /**
     * 设置实收金额
     *
     * @param data
     */
    @Override
    public void setIncome(String data) {
        this.text_home_income.setText(data);
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
        if (StringUtil.isNotEmpty(chanPlatId)) {
            Intent mIntent = new Intent(activity, NldCashServiceActivity.class);
            mIntent.putExtra("merc_id", merCode);
            mIntent.putExtra("stoe_id", chanPlatId);
            startActivity(mIntent);
        } else {
            Toast.makeText(activity, "请开通门店后重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.findViewById(R.id.tv_right).setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unregisterReceiver(receiver);
    }
}
