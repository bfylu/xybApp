package cn.payadd.majia.fragment.aistore;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.fragment.BaseFragment;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.utils.Enums;
import com.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.payadd.majia.activity.aistore.ClientDetailActivity;
import cn.payadd.majia.adapter.aistore.ClientAdapter;
import cn.payadd.majia.adapter.aistore.PopClientAdapter;
import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.entity.aistore.ClientDetailBean;
import cn.payadd.majia.entity.aistore.ClientDetailListBean;
import cn.payadd.majia.entity.aistore.ClientScreenBean;
import cn.payadd.majia.face.aistore.IClient;
import cn.payadd.majia.presenter.aistore.ClientPresenter;
import cn.payadd.majia.util.StringUtil;

public class ClientTypeFragment extends BaseFragment implements IClient, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @BindView(R.id.ll_screen)
    LinearLayout llScreen;
    @BindView(R.id.tv_screen)
    TextView tvScreen;
    @BindView(R.id.iv_screen)
    ImageView ivScreen;
    @BindView(R.id.tv_total_client_num)
    TextView tvTotalClientNum;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.myRecyclerView)
    MyRecyclerView myRecyclerView;

    private Enums.clientType clientType;

    private ClientAdapter mClientAdapter;

    private ClientPresenter mClientPresenter;

    private int screen;//筛选条件

    private int pageIndex = 1;

    private int row = 25;//每次查询条数

    private Intent mIntent;

    private PopupWindow mPop;

    private int choosePosition = -1;

    private List<ClientScreenBean.DataBean> clientScreenList;

    public static ClientTypeFragment getClientTypeFragment (Enums.clientType clientType) {
        ClientTypeFragment clientTypeFragment = new ClientTypeFragment();
        clientTypeFragment.clientType = clientType;
        return clientTypeFragment;
    }

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_client_type;
    }

    @Override
    protected void initView() {
        mClientPresenter = new ClientPresenter(getActivity(), this);

        mClientAdapter = new ClientAdapter(getActivity());
        myRecyclerView.setLinearLayoutRecyclerView(mClientAdapter, this, this);

        mClientAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                ActionRecordBean.DataBean.ListBean listBean = mClientAdapter.getAllData().get(position);
                mIntent = new Intent(getActivity(), ClientDetailActivity.class);
                mIntent.putExtra("iconUrl", listBean.getUserJmgUrl());
                mIntent.putExtra("lastDate", listBean.getActivityDate());
                mIntent.putExtra("distance", listBean.getDistance());
                mIntent.putExtra("nickname", listBean.getNick());
                mIntent.putExtra("phone", listBean.getPhone());
                mIntent.putExtra("userId", listBean.getUserId());
                mIntent.putExtra("gender", listBean.getSex());
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected void initData() {
        clientScreenList = new ArrayList<>();
        mClientPresenter.getClientScreenList();
    }

    @OnClick({R.id.ll_screen})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_screen:
                showPop(clientScreenList);
                break;
        }
    }

    private void showPop(final List<ClientScreenBean.DataBean> data) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_client, null);
        ListView listView = (ListView) view.findViewById(R.id.listView);

        final PopClientAdapter mPopClientAdapter = new PopClientAdapter(getActivity(), data, choosePosition);
        listView.setAdapter(mPopClientAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choosePosition = position;
                mPopClientAdapter.selectPosition(choosePosition);
                screen = StringUtil.toInt(clientScreenList.get(position).getNumber());
                tvScreen.setText(data.get(position).getDesc());
                onRefresh();
                hidePop();
            }
        });

        mPop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPop.setTouchable(true);
        mPop.setOutsideTouchable(true);
        mPop.setBackgroundDrawable(new ColorDrawable());
        mPop.setAnimationStyle(R.style.popwindow_anim);
        rotationImage(180f);
        mPop.showAsDropDown(line);
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rotationImage(0f);
            }
        });
    }

    private void rotationImage(float rotation) {
        ivScreen.setPivotX(ivScreen.getWidth() / 2);
        ivScreen.setPivotY(ivScreen.getHeight() / 2);
        ivScreen.setRotation(rotation);
    }

    private void hidePop() {
        if (mPop != null) {
            if (mPop.isShowing()) {
                mPop.dismiss();
            }
            mPop = null;
        }
    }

    @Override
    public void stopRecyclerView() {
        myRecyclerView.stopRecyclerView();
    }

    @Override
    public void getCustomerList(ActionRecordBean data) {
        if (data.getData() != null) {
            if (pageIndex == 1) {
                mClientAdapter.clear();
            }
            tvTotalClientNum.setText(getActivity().getResources().getString(R.string.client_num).replace("0", StringUtil.toString(data.getData().getTotal())));
            mClientAdapter.addAll(data.getData().getList());
        } else if (mClientAdapter.getAllData().size() == 0) {
            myRecyclerView.stopRecyclerView();
            myRecyclerView.showEmpty();
        }
    }

    @Override
    public void getCustomerInfoList(ClientDetailListBean data) {

    }

    @Override
    public void getCustomerInfo(ClientDetailBean data) {

    }

    @Override
    public void getClientScreenList(ClientScreenBean data) {
        if (data.getCode() == 0 && data.getData().size() != 0) {
            clientScreenList.addAll(data.getData());

            screen = StringUtil.toInt(data.getData().get(0).getNumber());
            tvScreen.setText(data.getData().get(0).getDesc());
        } else {
            screen = 1;
            tvScreen.setText("最后活动时间");
        }
        mClientPresenter.getCustomerList(Utilities.getMerCode(getActivity()), screen, StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @Override
    public void onRefresh() {
        this.pageIndex = 1;
        mClientPresenter.getCustomerList(Utilities.getMerCode(getActivity()), screen, StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @Override
    public void onLoadMore() {
        this.pageIndex++;
        mClientPresenter.getCustomerList(Utilities.getMerCode(getActivity()), screen, StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @Override
    public void onPause() {
        super.onPause();
        hidePop();
    }
}
