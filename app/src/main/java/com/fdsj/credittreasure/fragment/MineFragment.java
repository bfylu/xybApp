package com.fdsj.credittreasure.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.MainActivity;
import com.fdsj.credittreasure.activities.SettingActivity;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.presenter.DownLoadPresenter;
import com.fdsj.credittreasure.utils.FinishComponent;
import com.fdsj.credittreasure.utils.NextComponent;
import com.fdsj.credittreasure.utils.guideview.Guide;
import com.fdsj.credittreasure.utils.guideview.GuideBuilder;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.utils.StatusBarUtils;
import com.utils.Utilities;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.payadd.majia.activity.AboutUsActivity;
import cn.payadd.majia.activity.AccountActivity;
import cn.payadd.majia.activity.DredgeProductActivity;
import cn.payadd.majia.activity.FundAuthOrderActivity;
import cn.payadd.majia.activity.InstallmentOrderActivity;
import cn.payadd.majia.activity.MyBankCardActivity;
import cn.payadd.majia.activity.ShopInfoActivity;
import cn.payadd.majia.activity.shoporder.ShopOrderActivity;
import cn.payadd.majia.activity.voicenotif.VoiceNotificationActivity;
import cn.payadd.majia.config.EnvironmentConfig;
import rx.functions.Action1;

import static com.fdsj.credittreasure.constant.Constants.APP_ID;

/**
 * Created by BXND on 2017-01-03.
 * 个人中心
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MineFragment";

    @BindView(R.id.userImage)
    ImageView userImage;
    @BindView(R.id.text_userName)
    TextView textUserName;
    @BindView(R.id.text_phone)
    TextView textPhone;


    @BindView(R.id.relative_earnings)
    RelativeLayout relativeEarnings;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

//    @BindView(R.id.relative_manage)
//    RelativeLayout relativeManage;

//    @BindView(R.id.relative_fixcode)
//    RelativeLayout relativeFixcode;

    @BindView(R.id.relative_download)
    RelativeLayout relative_download;
    @BindView(R.id.line)
    View line;

    @BindView(R.id.relative_setting)
    RelativeLayout relative_setting;
    @BindView(R.id.relative_voiceNotification)
    RelativeLayout relative_voiceNotification;
    @BindView(R.id.relative_account)
    RelativeLayout relative_account;
    @BindView(R.id.relative_fund_auth)
    RelativeLayout relative_fund_auth;
    @BindView(R.id.relative_my_bankcard)
    RelativeLayout relative_my_bankcard;
    @BindView(R.id.relative_shopOrder)
    RelativeLayout relative_shopOrder;
    @BindView(R.id.relative_shop)
    RelativeLayout relative_shop;
    @BindView(R.id.relative_share)
    RelativeLayout relative_share;
    @BindView(R.id.relative_dredge_product)
    RelativeLayout relative_dredge_product;

//    @BindView(R.id.SignOut)
//    Button SignOut;

    DownLoadPresenter presenter;

    private boolean isFirst;
    private Guide nextGuide, finishGuide;

    @Override
    protected int getLayoutView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {

        if (EnvironmentConfig.isPos()) {
            relative_voiceNotification.setVisibility(View.GONE);
            relative_share.setVisibility(View.GONE);
        } else {
            relative_voiceNotification.setVisibility(View.VISIBLE);
            relative_share.setVisibility(View.GONE);
        }

        relative_shop.setVisibility(View.GONE);

        ((MainActivity)getActivity()).setTitleVisible(true);
        relativeEarnings.setOnClickListener(this);
//        relativeManage.setOnClickListener(this);
//        relativeFixcode.setOnClickListener(this);
        relative_account.setOnClickListener(this);
        relative_fund_auth.setOnClickListener(this);
        relative_my_bankcard.setOnClickListener(this);
        relative_shopOrder.setOnClickListener(this);
        relative_shop.setOnClickListener(this);
        relative_share.setOnClickListener(this);
        relative_dredge_product.setOnClickListener(this);
        relative_voiceNotification.setOnClickListener(this);
//        SignOut.setOnClickListener(this);
        presenter = new DownLoadPresenter();
        RxView.clicks(relative_download).throttleFirst(3, TimeUnit.SECONDS)//版本更新
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                        startActivity(intent);
                    }
                });

        isFirst = Utilities.getGuideIsFirst(getContext());

        getActivity()
                .getWindow()
                .getDecorView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        initNextGuideView();
                    }
                });

        relative_dredge_product.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        textUserName.setText(Utilities.getShowMerName(activity));
        textPhone.setText(Utilities.getMobilePhone(activity));
    }


    @OnClick({R.id.relative_earnings, R.id.relative_setting})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.relative_earnings://分期订单
                intent.setClass(activity, InstallmentOrderActivity.class);
                startActivity(intent);
                break;
//            case R.id.relative_manage://门店管理
//                break;
//            case R.id.relative_fixcode://我的固定码
//                intent.setClass(activity, FixedCodeActivity.class);
//                startActivity(intent);
//                break;
            case R.id.relative_account:
                intent.setClass(activity, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_fund_auth:
                intent.setClass(activity, FundAuthOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_my_bankcard:

                intent.setClass(activity, MyBankCardActivity.class);
                intent.putExtra("type","check");
                startActivity(intent);
                break;
            case R.id.relative_shopOrder:
                intent.setClass(activity, ShopOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_dredge_product:
                intent.setClass(activity, DredgeProductActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_shop:
                intent.setClass(activity, ShopInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_share:
                shareMiniPro("http://www.qq.com", WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE
                        , "gh_afc43e0300c7", "/pages/index/index"
                        , "测试", "测试", null);
                break;
            case R.id.relative_setting:
                intent.setClass(activity, SettingActivity.class);
                startActivity(intent);
//                openMiniPro("gh_afc43e0300c7", "/pages/index/index");
                break;
            case R.id.relative_voiceNotification:
                intent.setClass(activity, VoiceNotificationActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initNextGuideView() {
        if (isFirst) {
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(line)
                    .setAutoDismiss(false)
                    .setAlpha(150)
                    .setHighTargetCorner(0)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(false);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                    Utilities.setGuideIsFirst(true, getContext());
                }

                @Override
                public void onDismiss() {
                    initFinishGuideView();
                }
            });

            builder.addComponent(new NextComponent(getContext(), nextListener));
            nextGuide = builder.createGuide();
            nextGuide.setShouldCheckLocInWindow(true);
            nextGuide.show(getActivity());
        }
    }

    private void initFinishGuideView() {
        GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(line)
                .setAutoDismiss(false)
                .setAlpha(150)
                .setHighTargetCorner(0)
                .setOverlayTarget(false)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOutsideTouchable(false);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
                Utilities.setGuideIsFirst(true, getContext());
            }

            @Override
            public void onDismiss() {

            }
        });

        builder1.addComponent(new FinishComponent(getContext(), finishListener));
        finishGuide = builder1.createGuide();
        finishGuide.setShouldCheckLocInWindow(true);
        finishGuide.show(getActivity());
    }

    View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nextGuide != null) {
                nextGuide.dismiss();
            }
        }
    };

    View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (finishGuide != null) {
                finishGuide.dismiss();
            }
        }
    };

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                :type + System.currentTimeMillis();
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

    /**
     * 分享小程序
     * @param oldUrl 兼容低版本的网页链接
     * @param miniprogramType 正式版:0，测试版:1，体验版:2
     * @param userName 小程序原始id
     * @param path 小程序页面路径
     * @param title 小程序消息title
     * @param description 小程序消息desc
     * @param thumbData 小程序消息封面图片，小于128k
     */
    private void shareMiniPro(String oldUrl, int miniprogramType, String userName, String path, String title, String description, Bitmap thumbData) {
        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = oldUrl;
        miniProgramObj.miniprogramType = miniprogramType;
        miniProgramObj.userName = userName;
        miniProgramObj.path = path;
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title;
        msg.description = description;
        if (thumbData != null) {
            msg.setThumbImage(thumbData);
        } else {
            Bitmap temp = BitmapFactory.decodeResource(getActivity().getResources(),
                    R.mipmap.about_us);
            msg.setThumbImage(temp);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        BaseApplication.getIwxapi().sendReq(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.findViewById(R.id.tv_right).setVisibility(View.GONE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            StatusBarUtils.setBackGroundResource(getActivity(), R.mipmap.user_statusbar);//更改状态栏背景图片
            ((MainActivity)getActivity()).setTitleVisible(true);
        }
    }
}
