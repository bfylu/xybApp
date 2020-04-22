package com.fdsj.credittreasure.fragment;

import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.entity.Statistic;
import com.fdsj.credittreasure.presenter.FlowDetailPresenter;
import com.google.gson.Gson;
import com.utils.LogUtil;
import com.utils.ToastUtils;
import com.utils.Utilities;

import java.util.Map;

import butterknife.BindView;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 运营概括
 * <p>当前版本： V1.0.0
 */

public class OperateFragment extends BaseFragment implements IActivity {


    private int code = 1;

    public static OperateFragment getOperateFragment(int code) {
        OperateFragment operateFragment = new OperateFragment();
        operateFragment.code = code;
        return operateFragment;
    }

    @BindView(R.id.d_green)
    ImageView d_green;

    @BindView(R.id.d_gray)
    ImageView d_gray;

//    @BindView(R.id.recycler_view)
//    RecyclerView recycler_view;


    @BindView(R.id.text_zfb_money)
    TextView text_zfb_money;


    @BindView(R.id.text_wx_money)
    TextView text_wx_money;

    @BindView(R.id.text_one)
    TextView textViewOne;
    @BindView(R.id.text_two)
    TextView textViewTwo;


    @BindView(R.id.text_name_one)
    TextView textViewNameOne;
    @BindView(R.id.text_name_two)
    TextView textViewNameTwo;
    @BindView(R.id.text_money_one)
    TextView textViewMoneyOne;
    @BindView(R.id.text_money_two)
    TextView textViewMoneyTwo;
    @BindView(R.id.text_count_one)
    TextView textViewCountOne;
    @BindView(R.id.text_count_two)
    TextView textViewCountTwo;

    @BindView(R.id.image_one)
    ImageView imageOne;
    @BindView(R.id.image_two)
    ImageView imageTwo;


    @BindView(R.id.text_all)
    TextView text_all;

    @BindView(R.id.text_all_count)
    TextView text_all_count;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;


    FlowDetailPresenter presenter;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_operate;
    }

    @Override
    protected void initView() {
        presenter = new FlowDetailPresenter(this);
        if (code == 1) {
            d_green.setImageResource(R.mipmap.d_green);
            d_gray.setImageResource(R.mipmap.d_gray);
        } else {
            d_gray.setImageResource(R.mipmap.d_green);
            d_green.setImageResource(R.mipmap.d_gray);
        }
        Typeface fontFace = Typeface.createFromAsset(activity.getAssets(), "pp.ttf");
        textViewOne.setTypeface(fontFace);
        textViewTwo.setTypeface(fontFace);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_Home, R.color.color_6cbe3a, R.color.color_6cbe3a, R.color.colorAccent);

    }

    @Override
    protected void initData() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                presenter.queryStatistic(Utilities.getUsernameForLogin(activity), activity);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.queryStatistic(Utilities.getUsernameForLogin(activity), activity);
            }
        });
    }


    @Override
    public void stopRecyclerView() {
        swipeRefreshLayout.setRefreshing(false);
        ToastUtils.showToast(activity, "未查询到数据");
    }

    @Override
    public void UpdateModel(Object model, int status) {
        swipeRefreshLayout.setRefreshing(false);
        if (status == 1) {
            Map<String, String> stringMap = (Map<String, String>) model;
            try {
                String topOfMonth = stringMap.get("topOfMonth");//当月流量排名
                Statistic statistic = new Gson().fromJson(topOfMonth, Statistic.class);
                if (statistic.getList().getAlipay().getTop() == "1") {
                    textViewNameOne.setText("支付宝");
                    textViewNameTwo.setText("微信支付");
                    imageOne.setImageResource(R.mipmap.zhifubao);
                    imageTwo.setImageResource(R.mipmap.statistics_weixin);
                    textViewMoneyOne.setText(statistic.getList().getAlipay().getTotalTrans());
                    textViewMoneyTwo.setText(statistic.getList().getWeixin().getTotalTrans());
                    textViewCountOne.setText(statistic.getList().getAlipay().getTransCount());
                    textViewCountTwo.setText(statistic.getList().getWeixin().getTransCount());
                } else {
                    textViewNameTwo.setText("支付宝");
                    textViewNameOne.setText("微信支付");
                    imageTwo.setImageResource(R.mipmap.zhifubao);
                    imageOne.setImageResource(R.mipmap.statistics_weixin);
                    textViewMoneyTwo.setText(statistic.getList().getAlipay().getTotalTrans());
                    textViewMoneyOne.setText(statistic.getList().getWeixin().getTotalTrans());
                    textViewCountTwo.setText(statistic.getList().getAlipay().getTransCount());
                    textViewCountOne.setText(statistic.getList().getWeixin().getTransCount());
                }
                text_all.setText("¥" + stringMap.get("totalTrans"));
                text_all_count.setText(stringMap.get("transCount"));
            } catch (Exception e) {
                LogUtil.info("数据解析失败，OperateFragment", e.getMessage() + "******数据topOfMonth为：" + stringMap.get("topOfMonth"));
                ToastUtils.showToast(activity, "数据异常");
            }

            try {
                String channelTotal = stringMap.get("channelTotal");//梁道交易统计
                Statistic statistic = new Gson().fromJson(channelTotal, Statistic.class);
                text_wx_money.setText("¥" + statistic.getList().getWeixin().getTransTotal());
                text_zfb_money.setText("¥" + statistic.getList().getAlipay().getTransTotal());
            } catch (Exception e) {
                LogUtil.info("数据解析失败，OperateFragment", e.getMessage() + "******数据channelTotal为：" + stringMap.get("channelTotal"));
                ToastUtils.showToast(activity, "数据异常");
            }

        } else {
            ToastUtils.showToast(activity, model.toString());
        }
    }
}
