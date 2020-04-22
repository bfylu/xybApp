package cn.payadd.majia.fragment.aistore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.widgtes.MyRecyclerView;
import com.fdsj.credittreasure.widgtes.MyScrollRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.imsdk.TIMConversationType;
import com.utils.BitmapUtils;
import com.utils.Utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.payadd.majia.activity.aistore.ChatActivity;
import cn.payadd.majia.activity.aistore.ClientDetailActivity;
import cn.payadd.majia.adapter.aistore.ActionRecordAdapter;
import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.entity.aistore.PeopleNumberBean;
import cn.payadd.majia.entity.aistore.RelativePositionBean;
import cn.payadd.majia.face.aistore.IRadar;
import cn.payadd.majia.presenter.RadarPresenter;
import cn.payadd.majia.util.Center3Dialog;
import cn.payadd.majia.util.ScreenUtil;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.CircleImageView;
import cn.payadd.majia.view.ScrollLayout;

import static com.fdsj.credittreasure.constant.Constants.RADAR_FRAGMENT;
import static com.fdsj.credittreasure.constant.Constants.WHERE_GO;

/**
 * Created by lang on 2018/6/15.
 */

public class RadarFragment extends Fragment implements com.amap.api.maps.AMap.OnMarkerClickListener, IRadar
        , SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener {

    private TextureMapView mapView;

    private TextView tvRealTimeOnlinePopulation;

    private TextView tvPotentialBuyers;

    private ImageView ivReturnLocation/*, ivLocation*/;

    private ScrollLayout mScrollLayout;

    private MyScrollRecyclerView mRecyclerView;

    private AMap aMap;

    private UiSettings mUiSettings;

    private MarkerOptions markerOption;

    private Marker shopMarker;

    private Marker marker;

    private LatLng merchantLatlng;

    private RadarPresenter mRadarPresenter;

    private List<Marker> mList;

    private List<RelativePositionBean.DataBean.ListBean> ListData;

    private Center3Dialog chatDialog;

    private ActionRecordAdapter mActionRecordAdapter;

    private int pageIndex = 1;//查询页码

    private int row = 10;//每次查询条数

    private boolean isHandlerPause;

    private ScrollLayout.Status mCurrentStatus;

    private boolean isFirst;

    private Intent mIntent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radar, null);
        initView(view);
        mapView.onCreate(savedInstanceState);

        mRadarPresenter = new RadarPresenter(getActivity(), this);
        mHandler.post(runnable);

        isHandlerPause = true;

        return view;
    }

    private void initView(View view) {
        mapView = (TextureMapView) view.findViewById(R.id.mapView);
        tvRealTimeOnlinePopulation = (TextView) view.findViewById(R.id.tv_real_time_online_population);
        tvPotentialBuyers = (TextView) view.findViewById(R.id.tv_potential_buyers);
        ivReturnLocation = (ImageView) view.findViewById(R.id.iv_return_location);
//        ivLocation = (ImageView) view.findViewById(R.id.iv_location);
        mScrollLayout = (ScrollLayout) view.findViewById(R.id.scroll_down_layout);
        mRecyclerView = (MyScrollRecyclerView) view.findViewById(R.id.myRecyclerView);

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mActionRecordAdapter = new ActionRecordAdapter(getActivity());
        mRecyclerView.setLinearLayoutRecyclerView(mActionRecordAdapter, this, this);

        /**设置 setting*/
        mScrollLayout.setMinOffset(0);
        mScrollLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight(getActivity()) * 0.5));
        mScrollLayout.setExitOffset(ScreenUtil.dip2px(getContext(), 50));
        mScrollLayout.setIsSupportExit(false);
        mScrollLayout.setAllowHorizontalScroll(true);
        mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        mScrollLayout.scrollTo(0, (int) -(ScreenUtil.getScreenHeight(getActivity()) * 0.5));

        mScrollLayout.getBackground().setAlpha(0);

        RelativeLayout.LayoutParams layout = (RelativeLayout.LayoutParams) ivReturnLocation.getLayoutParams();
        layout.setMargins(0, 0, StringUtil.dp2px(getActivity(), 12), (int) (ScreenUtil.getScreenHeight(getActivity()) * 0.22));
        ivReturnLocation.setLayoutParams(layout);

        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        aMap.setOnMarkerClickListener(this);

        ivReturnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToLoc();
            }
        });

        mActionRecordAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ActionRecordBean.DataBean.ListBean listBean = mActionRecordAdapter.getItem(position);

                mIntent = new Intent(getActivity(), ClientDetailActivity.class);
                mIntent.putExtra("userId", listBean.getUserId());
                mIntent.putExtra("iconUrl", listBean.getUserJmgUrl());
                mIntent.putExtra(WHERE_GO, RADAR_FRAGMENT);
                startActivity(mIntent);
            }
        });
    }

    private void initData() {
        if (aMap != null) {
            aMap.clear();
        }
        this.pageIndex = 1;
        mRadarPresenter.getRelativePosition(Utilities.getMerCode(getActivity()), "5000");
        mRadarPresenter.getPeopleNumber(Utilities.getMerCode(getActivity()), "5000");
        mRadarPresenter.getActionRecord(Utilities.getMerCode(getActivity()), StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @Override
    public void stopRecyclerView() {
        mRecyclerView.stopRecyclerView();
    }

    @Override
    public void getRadar(RelativePositionBean data) {
        if (null != data.getData()) {
            if (data.getData().getList() != null) {
                ListData = data.getData().getList();
                addMarkersToMap(ListData);
            }
            addMerchantMarkerToMap(data.getData().getTmerchant());
        } else {
            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getPeopleNumber(PeopleNumberBean data) {
        if (data.getData() != null) {
            tvRealTimeOnlinePopulation.setText(StringUtil.toString(data.getData().getOnLinePeopleCount()));
            tvPotentialBuyers.setText(StringUtil.toString(data.getData().getPeopleCount()));
        } else {
            tvRealTimeOnlinePopulation.setText("0");
            tvPotentialBuyers.setText("0");
        }
    }

    @Override
    public void getActionRecord(ActionRecordBean data) {
        if (pageIndex == 1) {
            mActionRecordAdapter.clear();
        }

        if (data.getData() != null) {
            if (data.getData().getList() != null ) {
                mActionRecordAdapter.addAll(data.getData().getList());
            } else {
                mRecyclerView.stopRecyclerView();
            }
        } else {
            mRecyclerView.stopRecyclerView();
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMerchantMarkerToMap(RelativePositionBean.DataBean.MerchantBean merchantBean) {

        merchantLatlng = new LatLng(merchantBean.getLatitude(), merchantBean.getLongitude());

        shopMarker = drawMarkerOnMap(merchantLatlng, null, R.mipmap.location_shop);
        if (!isFirst) {
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(merchantLatlng));
            isFirst = true;
        }
    }

    private void moveCameraToLoc() {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        aMap.animateCamera(CameraUpdateFactory.changeLatLng(merchantLatlng));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(shopMarker)) {

        } else {
            for (int i = 0; i < mList.size(); i++) {
                if (marker.equals(mList.get(i))) {
                    showChatDialog(ListData.get(i));
                }
            }
        }
        return false;
    }

    private void addMarkersToMap(List<RelativePositionBean.DataBean.ListBean> listBeanList) {
        mList = new ArrayList<>();
        if(listBeanList.size() != 0){
            Bitmap bm = null;
            MarkerOptions options;
            LatLng latLng;
            for (RelativePositionBean.DataBean.ListBean listBean : listBeanList) {
                latLng = new LatLng(listBean.getLatitude(), listBean.getLongitude());
                bm = ImageLoader.getInstance().loadImageSync(listBean.getUserJmgUrl());
                options = new MarkerOptions().anchor(0.5f, 1)
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getCircleBitmap(
                                BitmapUtils.resizeBitmap(
                                        bm
                                        , StringUtil.dp2px(getContext(), 40)
                                        , StringUtil.dp2px(getContext(), 40)))));
                mList.add(aMap.addMarker(options));
            }
//            bm.recycle();
        }

    }

    private Marker drawMarkerOnMap(final LatLng point, final String iconUrl, int iconRes) {
        if (aMap != null && point != null) {

            if (StringUtil.isNotEmpty(iconUrl)) {

                Bitmap bm = ImageLoader.getInstance().loadImageSync(iconUrl);

                marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
                        .position(point)
                        .icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getCircleBitmap(
                                BitmapUtils.resizeBitmap(
                                        bm
                                        , StringUtil.dp2px(getContext(), 30)
                                        , StringUtil.dp2px(getContext(), 30))))));
            } else if (StringUtil.isEmpty(iconUrl) && iconRes != 0) {
                marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
                        .position(point)
                        .icon(BitmapDescriptorFactory.fromResource(iconRes)));
            }

            return marker;
        }
        return null;
    }

    private void showChatDialog(final RelativePositionBean.DataBean.ListBean listBean) {
        isHandlerPause = false;
        chatDialog = new Center3Dialog(getActivity(), R.layout.dialog_input_chat, new int[] {R.id.iv_close, R.id.btn_send_msg});
        chatDialog.show();

        CircleImageView ivIcon = (CircleImageView) chatDialog.findViewById(R.id.iv_icon);
        ImageView ivGender = (ImageView) chatDialog.findViewById(R.id.iv_gender);
        TextView tvName = (TextView) chatDialog.findViewById(R.id.tv_name);
        TextView tvDistance = (TextView) chatDialog.findViewById(R.id.tv_distance);
        final EditText etChatContent = (EditText) chatDialog.findViewById(R.id.et_chat_content);

        chatDialog.setOnCenterItemClickListener(new Center3Dialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(Center3Dialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.iv_close:
                        hideChatDialog();
                        break;
                    case R.id.btn_send_msg:
                        String chatContent = etChatContent.getText().toString().trim();
                        if (StringUtil.isEmpty(chatContent)) {
                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.input_send_msg), Toast.LENGTH_SHORT).show();
                            return;
                        }

//                        if ( UserInfo.getInstance().getId() != null && (TLSService.getInstance().needLogin(UserInfo.getInstance().getId()))) {
                        ChatActivity.navToChat(getActivity(), listBean.getUserId(), chatContent, TIMConversationType.C2C);
//                        } else {
//                            startActivity(new Intent(getActivity(), DialogActivity.class));
//                        }

                        hideChatDialog();
                        break;
                }
            }
        });

        ImageLoader.getInstance().displayImage(listBean.getUserJmgUrl(), ivIcon);
        if (1 == listBean.getSex()) {
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.mipmap.icon_male);
        } else if (2 == listBean.getSex()) {
            ivGender.setVisibility(View.VISIBLE);
            ivGender.setImageResource(R.mipmap.icon_female);
        } else {
            ivGender.setVisibility(View.GONE);
        }
        tvName.setText(listBean.getNick());
        tvDistance.setText(getDistance(merchantLatlng, new LatLng(listBean.getLatitude(), listBean.getLongitude())));
    }

    private void hideChatDialog() {
        isHandlerPause = true;
        if (chatDialog != null) {
            if (chatDialog.isShowing()) {
                chatDialog.dismiss();
            }
            chatDialog = null;
        }
    }

    private String getDistance(LatLng merchantLatlng, LatLng userLatlng) {
        String str = "";
        float distance = AMapUtils.calculateLineDistance(merchantLatlng, userLatlng);
        distance = Math.abs(distance);
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

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isHandlerPause) {
                switch (msg.what) {
                    case 1:
                        initData();
                        break;
                }
            }

        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            mHandler.postDelayed(runnable, 10000);
            Message message = new Message( );
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                mScrollLayout.getBackground().setAlpha(255 - (int) precent);
            }
//            if (text_foot.getVisibility() == View.VISIBLE)
//                text_foot.setVisibility(View.GONE);
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            mCurrentStatus = currentStatus;
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
//                text_foot.setVisibility(View.VISIBLE);
            } else if (currentStatus.equals(ScrollLayout.Status.OPENED)) {
                isHandlerPause = true;
//                ivLocation.setVisibility(View.VISIBLE);
            } else if (currentStatus.equals(ScrollLayout.Status.CLOSED)) {
                isHandlerPause = false;
//                ivLocation.setVisibility(View.GONE);
            }
        }

        @Override
        public void onChildScroll(int top) {

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        if (ScrollLayout.Status.OPENED.equals(mCurrentStatus)) {
            isHandlerPause = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideChatDialog();
        isHandlerPause = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        this.pageIndex = 1;
        mRadarPresenter.getActionRecord(Utilities.getMerCode(getActivity()), StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

    @Override
    public void onLoadMore() {
        this.pageIndex++;
        mRadarPresenter.getActionRecord(Utilities.getMerCode(getActivity()), StringUtil.toString(pageIndex), StringUtil.toString(row));
    }

}
