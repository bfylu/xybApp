package cn.payadd.majia.activity.aistore;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.imsdk.TIMConversationType;
import com.utils.Config;
import com.utils.StatusBarUtils;
import com.utils.Utilities;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.adapter.aistore.ClientDetailAdapter;
import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.entity.aistore.ClientDetailBean;
import cn.payadd.majia.entity.aistore.ClientDetailListBean;
import cn.payadd.majia.entity.aistore.ClientScreenBean;
import cn.payadd.majia.face.aistore.IClient;
import cn.payadd.majia.presenter.aistore.ClientPresenter;
import cn.payadd.majia.util.Center3Dialog;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.CircleImageView;

import static com.fdsj.credittreasure.constant.Constants.RADAR_FRAGMENT;
import static com.fdsj.credittreasure.constant.Constants.WHERE_GO;

public class ClientDetailActivity extends BaseActivity implements View.OnClickListener, IClient, RecyclerArrayAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_last_time)
    TextView tvLastTime;
    @BindView(R.id.iv_icon)
    CircleImageView ivIcon;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_client)
    LinearLayout llClient;
    @BindView(R.id.tv_client_tag)
    TextView tvClientTag;
    @BindView(R.id.tv_client)
    TextView tvClient;
    @BindView(R.id.myRecyclerView)
    MyRecyclerView mRecyclerView;
    @BindView(R.id.tv_add_tag)
    TextView tvAddTag;
    @BindView(R.id.tv_add_record)
    TextView tvAddRecord;
    @BindView(R.id.tv_send_msg)
    TextView tvSendMsg;

    private String iconUrl;

    private String userId;

    private String lastDate;

    private String distance;

    private String nickname;

    private String phone;

    private int WHERE_COME;

    private String clientTag;//暂无

    private String client;//暂无

    private int gender;

    private ClientPresenter mClientPresenter;

    private ClientDetailAdapter mClientDetailAdapter;

    private int pageIndex = 1;

    private int row = 10;//每次查询条数

    private Center3Dialog mCallPhoneDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_client_detail;
    }

    @Override
    public void initView() {
        StatusBarUtils.setColor(this, getResources().getColor(R.color.ai_store_title));//更改状态栏背景颜色
        super.setActionBarBackgroundColor(R.color.ai_store_title);

        setTitleCenterText(getResources().getString(R.string.client_detail));
        setTitleBackButton();
        llClient.setVisibility(View.GONE);
        tvAddTag.setVisibility(View.GONE);
        tvAddRecord.setVisibility(View.GONE);

    }

    @Override
    protected void initData() {
        WHERE_COME = getIntent().getIntExtra(WHERE_GO, 0);

        iconUrl = getIntent().getStringExtra("iconUrl");
        userId = getIntent().getStringExtra("userId");
        lastDate = getIntent().getStringExtra("lastDate");
        distance = getIntent().getStringExtra("distance");
        nickname = getIntent().getStringExtra("nickname");
        phone = getIntent().getStringExtra("phone");
        gender = getIntent().getIntExtra("gender", 0);

        mClientPresenter = new ClientPresenter(this, this);

        if (WHERE_COME == RADAR_FRAGMENT) {
            mClientPresenter.getCustomerInfo(Utilities.getMerCode(this), userId);
        } else {
            setInfo(nickname, lastDate, distance, iconUrl, phone, gender);
        }

        mClientDetailAdapter = new ClientDetailAdapter(this, iconUrl);
        mRecyclerView.setLinearLayoutRecyclerView(mClientDetailAdapter, this, this);

        mClientPresenter.getCustomerInfoList(Utilities.getMerCode(this), userId, StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @OnClick({R.id.tv_add_tag, R.id.tv_add_record, R.id.tv_send_msg, R.id.tv_phone})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:
                if (StringUtil.isNotEmpty(phone)) {
                    showCallPhoneDialog(phone);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.no_phone_num), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_send_msg:
//                if ( UserInfo.getInstance().getId() != null && (TLSService.getInstance().needLogin(UserInfo.getInstance().getId()))) {
                ChatActivity.navToChat(this, userId, TIMConversationType.C2C);
//                } else {
//                    startActivity(new Intent(this, DialogActivity.class));
//                }
                break;
            case R.id.tv_add_record:
                break;
            case R.id.tv_add_tag:
                break;
        }
    }

    @Override
    public void stopRecyclerView() {
        mRecyclerView.stopRecyclerView();
    }

    @Override
    public void getCustomerList(ActionRecordBean data) {
    }

    @Override
    public void getCustomerInfoList(ClientDetailListBean data) {
        if (data.getData() != null) {
            if (data.getData().getList() != null) {
                if (pageIndex == 1) {
                    mClientDetailAdapter.clear();
                }
                mClientDetailAdapter.addAll(data.getData().getList());
            } else if (mClientDetailAdapter.getAllData().size() == 0) {
                mRecyclerView.stopRecyclerView();
                mRecyclerView.showEmpty();
            }
        } else if (mClientDetailAdapter.getAllData().size() == 0) {
            mRecyclerView.stopRecyclerView();
            mRecyclerView.showEmpty();
        }
    }

    @Override
    public void getCustomerInfo(ClientDetailBean data) {
        userId = data.getData().getUserId();
        iconUrl = data.getData().getUserJmgUrl();
        lastDate = data.getData().getActivityDate();
        distance = data.getData().getDistance();
        nickname = data.getData().getNick();
        phone = data.getData().getPhone();
        gender = data.getData().getSex();

        setInfo(nickname, lastDate, distance, iconUrl, phone, gender);
    }

    @Override
    public void getClientScreenList(ClientScreenBean data) {

    }

    private void setInfo(String nickname, String date, String distance, String iconUrl, String phone, int gender) {
        ImageLoader.getInstance().displayImage(iconUrl, ivIcon);
        tvLastTime.setText(date);
        if (StringUtil.isNotEmpty(distance)) {
            tvDistance.setVisibility(View.VISIBLE);
            tvDistance.setText(getDistance(distance));
        } else {
            tvDistance.setVisibility(View.GONE);
        }
        tvNickname.setText(nickname);
        if (StringUtil.isEmpty(phone)) {
            phone = "";
        }
        tvPhone.setText(StringUtil.append(getResources().getString(R.string.phone_num), phone));
        if (1 == gender) {
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.mipmap.icon_male);
        } else if (2 == gender) {
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.mipmap.icon_female);
        } else {
            ivGender.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        this.pageIndex = 1;
        mClientPresenter.getCustomerInfoList(Utilities.getMerCode(this), userId, StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @Override
    public void onLoadMore() {
        this.pageIndex++;
        mClientPresenter.getCustomerInfoList(Utilities.getMerCode(this), userId, StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @Override
    protected void initPermission() {

    }

    private void showCallPhoneDialog(final String phoneNum) {
        mCallPhoneDialog = new Center3Dialog(this, R.layout.center_dialog, new int[] {R.id.confirm, R.id.cancel});
        mCallPhoneDialog.show();

        mCallPhoneDialog.setOnCenterItemClickListener(new Center3Dialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(Center3Dialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.confirm:
                        dialPhoneNumber(phoneNum);
                        hideCallPhoneDialog();
                        break;
                    case R.id.cancel:
                        hideCallPhoneDialog();
                        break;
                }
            }
        });

        TextView tvContent = (TextView) mCallPhoneDialog.findViewById(R.id.content);
        tvContent.setText(StringUtil.append(getResources().getString(R.string.confirm_call_phone), phoneNum));
    }

    private void hideCallPhoneDialog() {
        if (mCallPhoneDialog != null) {
            if (mCallPhoneDialog.isShowing()) {
                mCallPhoneDialog.dismiss();
            }
            mCallPhoneDialog = null;
        }
    }

    /**
     * 拨打电话
     * @param phoneNumber
     */
    private void dialPhoneNumber(String phoneNumber) {
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        if(Config.isMobile()) {
            if (tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private String getDistance(String distanceStr) {
        String str = "";
        float distance = StringUtil.toFloat(distanceStr);
        if (distance > 1000) {
            distance = distance / 1000;
            DecimalFormat df = new DecimalFormat("###############0.000");
            str = StringUtil.append(df.format(distance), "km");
        } else {
            DecimalFormat df = new DecimalFormat("###############0");
            str = StringUtil.append(df.format(distance), "m");
        }
        return str;
    }
}
