package cn.payadd.majia.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.utils.BitmapUtils;
import com.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.activity.AgentActivity;
import cn.payadd.majia.activity.FixedCodeActivity;
import cn.payadd.majia.activity.GoodsManagerActivity;
import cn.payadd.majia.activity.IncomeManagerActivity;
import cn.payadd.majia.activity.MsgNoticeActivity;
import cn.payadd.majia.activity.aistore.AIStoreMainActivity;
import cn.payadd.majia.activity.openminipro.OpenMiniProActivity;
import cn.payadd.majia.adapter.ManageAdapter;
import cn.payadd.majia.entity.openminipro.AccessTokenBean;
import cn.payadd.majia.face.openminipro.IMiniPro;
import cn.payadd.majia.listener.NoItemDoubleClickListener;
import cn.payadd.majia.presenter.openminipro.MiniProPresenter;
import cn.payadd.majia.util.BottomFullDialog;
import cn.payadd.majia.util.Center2Dialog;
import cn.payadd.majia.util.ImageUtil;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.view.CircleImageView;
import q.rorbin.badgeview.QBadgeView;

import static com.fdsj.credittreasure.constant.Constants.APP_ID;

/**
 * Created by df on 2017/9/11.
 */

public class ManageFragment extends Fragment implements View.OnClickListener, IMiniPro {
    private RelativeLayout rlIncome;
    private TextView tvIncome;
    private GridView gvManage;
    private GridView gvAgency;
    private List<Map<String, Object>> manageList;
    private List<Map<String, Object>> agencyList;

    private Map<Integer,QBadgeView> msgViewMap;

    private BottomFullDialog shareDialog;

    private Center2Dialog miniProDialog;

    private MiniProPresenter miniProPresenter;

    private int noticeMsgCount;
    private int[] manageResId = {R.mipmap.icon_news, R.mipmap.icon_commodity, R.mipmap
            .icon_store, R.mipmap.icon_vip, R.mipmap.icon_code, R.mipmap.icon_shop};
    private String[] manageTexts = {"消息通知", "商品管理", "门店管理", "会员管理", "收款码", "物料商城"};
    private int[] agencyResId = {R.mipmap.icon_agency/*, R.mipmap.icon*/, R.mipmap.guanli_icon_ai, R.mipmap.share};
    private String[] agencyTexts = {"代理后台"/*,"商户成长贷"*/, "AI店铺", "分享"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_manage,
                container, false);
        ((MainActivity)getActivity()).setTitleVisible(true);
        msgViewMap = new HashMap<>();
        rlIncome = (RelativeLayout) layout.findViewById(R.id.rl_income);
        gvManage = (GridView) layout.findViewById(R.id.gv_manage);
        gvAgency = (GridView) layout.findViewById(R.id.gv_agency);
        tvIncome = (TextView) layout.findViewById(R.id.tv_today_income);

        rlIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(getActivity(), IncomeManagerActivity.class);
                getActivity().startActivity(intent);
            }
        });
        manageList = new ArrayList<>();
        for (int i = 0; i < manageResId.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", manageResId[i]);
            item.put("text", manageTexts[i]);
            manageList.add(item);
        }

        ManageAdapter adapter = new ManageAdapter(getActivity(), manageList, gvManage);
        adapter.setMsgViewData(msgViewMap);
        adapter.setTabBackgroundColor(R.color.white);
        gvManage.setAdapter(adapter);
        gvManage.setOnItemClickListener(new NoItemDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent.setClass(getActivity(), MsgNoticeActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getActivity(), GoodsManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(getActivity(), OpenMiniProActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(getActivity(), FixedCodeActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //设置界面顶部图片，不设置默认为星pos主题图片
//                        NldInstallMent.setmResTitleImg(R.mipmap.logo);
//                      //通过显示启动启动Arr中的分期Activity
//                        intent.putExtra("txnAmt", 1000);
//                        intent.setClassName(getActivity(), "com.newland.starpos.installmentsdk.MainActivity");
//                        startActivity(intent);

                        break;


                    default:
                        Toast.makeText(getActivity(),"功能暂未开通，敬请期待",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        agencyList = new ArrayList<>();
        for (int i = 0; i < agencyResId.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", agencyResId[i]);
            item.put("text", agencyTexts[i]);
            agencyList.add(item);
        }
        ManageAdapter agencyAdapter = new ManageAdapter(getActivity(), agencyList, gvAgency);
        agencyAdapter.setTabBackgroundColor(R.color.white);
        gvAgency.setAdapter(agencyAdapter);
        gvAgency.setOnItemClickListener(new NoItemDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (StringUtil.toInt(agencyList.get(position).get("image"))){
                    case R.mipmap.icon_agency:
                        intent =  intent.setClass(getActivity(), AgentActivity.class);
                        startActivity(intent);
                        break;
                    case R.mipmap.icon:
//                        intent.setClass(getActivity(), NldLoanActivity.class);
//                        startActivity(intent);
                        break;
                    case R.mipmap.guanli_icon_ai:
//                        initPermission();
                        intent.setClass(getActivity(), AIStoreMainActivity.class);
                        startActivity(intent);
                        break;
                    case R.mipmap.share:
                        showShareDialog();
                        break;
                }
            }
        });

        if(!Utilities.isAgent(getActivity())){
            agencyList.remove(0);
//            agencyAdapter.updateData(agencyList,false);
            agencyAdapter.notifyDataSetChanged();
        }

        miniProPresenter = new MiniProPresenter(getActivity(), this);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String username = Utilities.getUsernameForLogin(getActivity());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        shareDialog = new BottomFullDialog(getActivity(), R.layout.dialog_share, new int[] {R.id.ll_wechat_friend, R.id.ll_promo_code, R.id.btn_cancel});
        shareDialog.show();
        shareDialog.setOnCenterItemClickListener(new BottomFullDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(BottomFullDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.ll_wechat_friend:
                        openMiniPro("gh_afc43e0300c7", "/pages/index/index");
                        hideShareDialog();
                        break;
                    case R.id.ll_promo_code:
                        miniProPresenter.getAccessToken("wx12b2f0186f110bf6", "f53a84cc5ec7d44030a3df33e3e35214", "client_credential");
                        hideShareDialog();
                        break;
                    case R.id.btn_cancel:
                        hideShareDialog();
                        break;
                }
            }
        });

    }

    private void showMiniProDialog(Bitmap file) {
        miniProDialog = new Center2Dialog(getActivity(), R.layout.dialog_mini_pro, new int[] {R.id.btn_save_local});
        miniProDialog.show();

        miniProDialog.setOnCenterItemClickListener(new Center2Dialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(Center2Dialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.btn_save_local:

                        break;
                }
            }
        });

        CircleImageView ivHead = (CircleImageView) miniProDialog.findViewById(R.id.iv_head);
        TextView tvName = (TextView) miniProDialog.findViewById(R.id.tv_name);
//        ViewPager viewPager = (ViewPager) miniProDialog.findViewById(R.id.viewPager);
//        ImageView iv_pic = (ImageView) miniProDialog.findViewById(R.id.iv_pic);
        View dot1 = miniProDialog.findViewById(R.id.dot_1);
        View dot2 = miniProDialog.findViewById(R.id.dot_2);
//        iv_pic.setImageBitmap(file);
    }

    /**
     * 直接打开小程序
     * @param userName 小程序原始id
     * @param path 小程序页面路径
     */
    private void openMiniPro(String userName, String path) {
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), APP_ID, true);//appid

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = userName; // 填小程序原始id
        req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    private void hideShareDialog() {
        if (shareDialog != null) {
            if (shareDialog.isShowing()) {
                shareDialog.dismiss();
            }
            shareDialog = null;
        }
    }

    private void hideMiniProDialog() {
        if (miniProDialog != null) {
            if (miniProDialog.isShowing()) {
                miniProDialog.dismiss();
            }
            miniProDialog = null;
        }
    }

    @Override
    public void getAccessToken(AccessTokenBean data) {
        miniProPresenter.getMiniProPic(data.getAccess_token(), "page/index/index", "abc");
    }

    @Override
    public void getMiniProPic(Bitmap data) {
        showMiniProDialog(data);
    }

    public int getNoticeMsgCount() {
        return noticeMsgCount;
    }

    public void setNoticeMsgCount(int noticeMsgCount) {
        this.noticeMsgCount = noticeMsgCount;
        noticeMsgBindView(noticeMsgCount,0);

    }

    @Override
    public void onResume() {
        super.onResume();
        setNoticeMsgCount(Utilities.getIsmtNoticeCount(getActivity()));
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            ((MainActivity)getActivity()).setTitleVisible(true);
            getActivity().findViewById(R.id.tv_right).setVisibility(View.GONE);
            ((MainActivity)getActivity()).setRightText(getActivity().getResources().getString(R.string.share), this);
            MainActivity activity = (MainActivity) getActivity();
            tvIncome.setText(activity.getTodayProfitAmount());
            noticeMsgBindView(noticeMsgCount,0);
        } else {
            getActivity().findViewById(R.id.tv_right).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideShareDialog();
        hideMiniProDialog();
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.tv_right).setVisibility(View.GONE);
        MainActivity activity = (MainActivity) getActivity();
        activity.setRightText(getActivity().getResources().getString(R.string.share), this);
        tvIncome.setText(activity.getTodayProfitAmount());
        super.onStart();

    }
}
