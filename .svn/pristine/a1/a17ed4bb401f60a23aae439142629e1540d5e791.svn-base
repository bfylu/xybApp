package cn.payadd.majia.push;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.android.qzs.voiceannouncementlibrary.VoiceUtils;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.StartActivity;
import com.fdsj.credittreasure.application.BaseApplication;
import com.fdsj.credittreasure.broadcast.PayPushBroadcastReceiver;
import com.fdsj.credittreasure.constant.Constants;
import com.utils.Config;
import com.utils.SharedPreferenceTool;
import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.payadd.majia.activity.InstallmentDetailActivity;
import cn.payadd.majia.activity.InstallmentOrderActivity;
import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.constant.OrderType;
import cn.payadd.majia.device.pos.IPosHelper;
import cn.payadd.majia.device.pos.PosDeviceUtil;
import cn.payadd.majia.device.pos.PrintConstant;
import cn.payadd.majia.device.pos.PrintFormat;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.security.AppSecurity;
import cn.payadd.majia.task.RequestServerTask;
import cn.payadd.majia.util.MyLifecycleHandler;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by df on 2017/12/21.
 */

public class NoticeMsgReceiver extends MessageReceiver{

    // 消息接收部分的LOG_TAG
    private static final String LOG_TAG = "NoticeMsgReceiver";

    @Override
    public void onNotification(final Context context, String title, String summary, Map<String, String> extraMap) {
        Log.e(LOG_TAG, "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        sendToServer(context, AppService.GET_ISMT_NOTICE_COUNT, null, true, new ICallback() {
            @Override
            public void exec(Object params) {
                Map<String,String> map = (Map<String, String>) params;
                int noticeMsgCount = Integer.valueOf(map.get("ismtNoticeCount"));
                Utilities.setIsmtNoticeCount(context,noticeMsgCount);
            }
        }, null, null);


    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e(LOG_TAG, "onMessage , messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
        try {
            JSONObject jsonObj = new JSONObject(cPushMessage.getContent());
            String username = jsonObj.getString("username");
            String orderType = jsonObj.getString("orderType");
            String orderAmount = jsonObj.getString("orderAmount");
            String currentUsername = Utilities.getUsername(context);
            if (!TextUtils.isEmpty(currentUsername) && currentUsername.equals(username)) {
                int payNotifyRingTonePosition = SharedPreferenceTool.getInstance().getSaveShareInt(BaseApplication.getAppContext()
                        , Constants.USER_INFO
                        , Constants.VOICE_NOTIFICATION
                        , 2);

                if (payNotifyRingTonePosition == 1) {
                    if (OrderType.PAY.equals(orderType)) {
                        if(!TextUtils.isEmpty(orderAmount)){
                            VoiceUtils.with(context).PlaySoundList(1, "$", 1);
                        }
                    }
                } else if (payNotifyRingTonePosition == 2) {
                    if (OrderType.PAY.equals(orderType)) {
                        if(!TextUtils.isEmpty(orderAmount)){
                            Play(context, orderAmount);
                        }
                    }
                }
//
//                SharedPreferences sp = context.getSharedPreferences(SettingActivity.class.getName(), Context.MODE_PRIVATE);
//                boolean playerPayNotifyRingTone = sp.getBoolean("playerPayNotifyRingTone", true);
//
//
//                if (playerPayNotifyRingTone) {
//                    if (OrderType.PAY.equals(orderType)) {
//                        if(!TextUtils.isEmpty(orderAmount)){
//                            Play(context, orderAmount);
//                        }
//                    }
//                }
//                boolean isForeground = MyLifecycleHandler.isApplicationInForeground();
//                if(isForeground) {
//                    //前台
//
//
//                } else{
//                    //后台
//                }

                Set<String> set = Utilities.getOrderNoSet(BaseApplication.getAppContext());

                Intent intent = new Intent(PayPushBroadcastReceiver.PAY_RESULT_PUSH_BROADCAST_NAME);
                Iterator<String> itrs = jsonObj.keys();
                while (itrs.hasNext()) {
                    String key = itrs.next();
                    intent.putExtra(key, jsonObj.getString(key));
                }
                ticketPrinter(intent.getExtras(), set);
                context.sendBroadcast(intent);

            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private synchronized void Play(final Context context,final String str) {

        if (VoiceUtils.with(context).GetIsPlay()){
            System.out.println("正在播放语音 ");
            new Thread() {
                @Override
                public  void run() {
                    super.run();
                    try {
                        Thread.sleep(100);//休眠0.1秒
                        Play(context,str);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 要执行的操作
                     */
                }
            }.start();
        }else {
            System.out.println("不冲突");
            VoiceUtils.with(context).Play(str, true);
        }
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e(LOG_TAG, "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationClickedWithNoAction(final Context context, String title, String summary, String extraMap) {
        Log.e(LOG_TAG, "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        try {
            JSONObject extraJson = new JSONObject(extraMap);
            String msgId = extraJson.getString("id");
            boolean isNew = extraJson.getBoolean("isNew");
            String orderNo = extraJson.getString("orderNo");

            Bundle bundle = new Bundle();
            bundle.putString("id",msgId);
            bundle.putBoolean("isNew",isNew);
            bundle.putString(InstallmentDetailActivity.KEY_ORDER_NO,orderNo);
            bundle.putString("openType","notice");

            Map<String, String> data = new HashMap<>();
            data.put("id", msgId);
            //通知已读
            sendToServer(context, AppService.SIGN_MSG_READ, data, true, new ICallback() {
                @Override
                public void exec(Object params) {
                    Utilities.setIsmtNoticeCount(context,Utilities.getIsmtNoticeCount(context) - 1);
                }
            }, null, null);

            //判断程序是否在前台运行，1.程序在前台运行则直接跳转，2.程序在后台运行则先跳转到MainActivity
            boolean isForeground = MyLifecycleHandler.isApplicationInForeground();
            if(isForeground) {
                //前台
                if(isNew){
                    Intent intent = new Intent(context, InstallmentOrderActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, InstallmentDetailActivity.class);
                    intent.putExtra("orderNo",orderNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            } else{
                //后台
                Intent intent = new Intent(context, StartActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //TODO
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e(LOG_TAG, "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e(LOG_TAG, "onNotificationRemoved");
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

    private void ticketPrinter (Bundle data, Set<String> set) {
        int printType = SharedPreferenceTool.getInstance().getSaveShareInt(BaseApplication.getAppContext()
                , Constants.USER_INFO
                , Constants.PRINT_TYPE_CHOOSE
                , 1);

        if (StringUtil.equals(data.getString("bizType"), "merH5")) {
            return;
        }

        if (!set.contains(data.getString("orderNo"))) {
            return;
        }

        final String orderAmt = data.getString("orderAmount");
        final String orderDate = data.getString("orderDate");
        final String orderDesc = data.getString("orderDesc");
        final String merCode = data.getString("merCode");
        final String merName = data.getString("merName");
        final String merShortName = data.getString("merShortName");
        final String payType = data.getString("payType");
        final String address = data.getString("address");
        final String orderNo = data.getString("orderNo");
        //银行卡信息
        final String cardNum = data.getString("cardNum");
        final String receiveSpecial = data.getString("receiveSpecial");
        final String cardIssuers = data.getString("cardIssuers");
        final String periodOfValidity = data.getString("periodOfValidity");

        String msg = "交易成功";
        String payTypeStr = "";
        if ("unionpay".equals(payType)) {
            payTypeStr = BaseApplication.getAppContext().getResources().getString(R.string.card_pay);
        } else if ("weixin".equals(payType)) {
            payTypeStr = BaseApplication.getAppContext().getResources().getString(R.string.weixin_pay);
        } else if ("alipay".equals(payType)) {
            payTypeStr = BaseApplication.getAppContext().getResources().getString(R.string.ali_pay);
        }

        if (Config.isPos()) {
            if ("unionpay".equals(payType)) {
                return;
            }
            IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(), BaseApplication.getAppContext());
            helper.init(BaseApplication.getAppContext());
            List<String> contentList = new ArrayList<>();
            contentList.add(PrintFormat.transJson(3, "", 1).toString());
            if (StringUtil.equals("newland", EnvironmentConfig.getPosType())) {
                contentList.add(PrintFormat.transJson(3
                        , BaseApplication.getAppContext().getResources().getString(R.string.sign_the_order)
                        , 1
                        , PrintConstant.Align.CENTER).toString());
                if (printType == 0) {//小票单联打印
                    initContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);
                } else if (printType == 1) {//小票二联打印
                    //商户存根联 含持卡人签名
                    contentList.add(PrintFormat.transJson(1, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.merchant_copy), 1).toString());

                    initContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);

                    contentList.add(PrintFormat.transJson(2
                            , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.pay_service_support)
                                    , BaseApplication.getAppContext().getResources().getString(R.string.service_num))
                            , 0).toString());
                    contentList.add(PrintFormat.transJson(1, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(3, " ", 0).toString());
                    //持卡人存根
//                        if ("unionpay".equals(payType)) {
//                            contentList.add(PrintFormat.transJson(3, getResources().getString(R.string.sign_the_order), 1, PrintConstant.Align.CENTER).toString());
//                        }
                    contentList.add(PrintFormat.transJson(1, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.customer_copy), 1).toString());

                    initContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);
                }  else if (printType == 2) {//小票三联打印
                    //商户存根联 含持卡人签名
                    contentList.add(PrintFormat.transJson(1, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.merchant_copy), 1).toString());

                    initContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);

                    contentList.add(PrintFormat.transJson(2
                            , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.pay_service_support)
                                    , BaseApplication.getAppContext().getResources().getString(R.string.service_num))
                            , 0).toString());
                    contentList.add(PrintFormat.transJson(1, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(3, " ", 0).toString());
                    //商户存根 不含持卡人签名
//                        if ("unionpay".equals(payType)) {
//                            contentList.add(PrintFormat.transJson(3, getResources().getString(R.string.sign_the_order), 1, PrintConstant.Align.CENTER).toString());
//                        }
                    contentList.add(PrintFormat.transJson(1, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.merchant_copy), 1).toString());

                    initContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);

                    contentList.add(PrintFormat.transJson(2
                            , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.pay_service_support)
                                    , BaseApplication.getAppContext().getResources().getString(R.string.service_num))
                            , 0).toString());
                    contentList.add(PrintFormat.transJson(3, " ", 0).toString());
                    //持卡人存根
//                        if ("unionpay".equals(payType)) {
//                            contentList.add(PrintFormat.transJson(3, getResources().getString(R.string.sign_the_order), 1, PrintConstant.Align.CENTER).toString());
//                        }
                    contentList.add(PrintFormat.transJson(1, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.customer_copy), 1).toString());

                    initContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);
                }
            } else if (StringUtil.equals("shengpos", EnvironmentConfig.getPosType())) {//小票单联打印
                contentList.add(PrintFormat.transJson(3, "\t\t\t\t\t\t\t\t\t\t\t\t签购单", 0, PrintConstant.Align.CENTER).toString());
                if (printType == 0) {
                    initShengPayContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);
                } else if (printType == 1) {//小票二联打印
                    //商户存根联 含持卡人签名
                    contentList.add(PrintFormat.transJson(2, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.merchant_copy), 0).toString());

                    initShengPayContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);

                    contentList.add(PrintFormat.transJson(2
                            , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.pay_service_support)
                                    , BaseApplication.getAppContext().getResources().getString(R.string.service_num))
                            , 0).toString());
                    contentList.add(PrintFormat.transJson(2, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(3, " ", 0).toString());
                    //持卡人存根
//                        if ("unionpay".equals(payType)) {
//                            contentList.add(PrintFormat.transJson(3, "\t\t\t\t\t\t\t\t\t\t\t\t签购单", 0, PrintConstant.Align.CENTER).toString());
//                        }
                    contentList.add(PrintFormat.transJson(2, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.customer_copy), 0).toString());

                    initShengPayContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);
                } else if (printType == 2) {//小票三联打印
                    //商户存根联 含持卡人签名
                    contentList.add(PrintFormat.transJson(2, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.merchant_copy), 0).toString());

                    initShengPayContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);

                    contentList.add(PrintFormat.transJson(2
                            , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.pay_service_support)
                                    , BaseApplication.getAppContext().getResources().getString(R.string.service_num))
                            , 0).toString());
                    contentList.add(PrintFormat.transJson(2, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(3, " ", 0).toString());
                    //商户存根 不含持卡人签名
//                        if ("unionpay".equals(payType)) {
//                            contentList.add(PrintFormat.transJson(3, "\t\t\t\t\t\t\t\t\t\t\t\t签购单", 0, PrintConstant.Align.CENTER).toString());
//                        }
                    contentList.add(PrintFormat.transJson(2, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.merchant_copy), 0).toString());

                    initShengPayContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);

                    contentList.add(PrintFormat.transJson(2
                            , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.pay_service_support)
                                    , BaseApplication.getAppContext().getResources().getString(R.string.service_num))
                            , 0).toString());
                    contentList.add(PrintFormat.transJson(3, " ", 0).toString());
                    //持卡人存根
//                        if ("unionpay".equals(payType)) {
//                            contentList.add(PrintFormat.transJson(3, "\t\t\t\t\t\t\t\t\t\t\t\t签购单", 0, PrintConstant.Align.CENTER).toString());
//                        }
                    contentList.add(PrintFormat.transJson(2, "——————————————————————", 0).toString());
                    contentList.add(PrintFormat.transJson(2, BaseApplication.getAppContext().getResources().getString(R.string.customer_copy), 0).toString());

                    initShengPayContentList(contentList, merShortName, merCode, helper.getTerminalNo(), ""
                            , payType, cardNum, receiveSpecial, cardIssuers, periodOfValidity, payTypeStr, orderDate, orderAmt, orderDesc, orderNo);
                }
            }

            contentList.add(PrintFormat.transJson(2
                    , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.pay_service_support)
                            , BaseApplication.getAppContext().getResources().getString(R.string.service_num))
                    , 0).toString());
            contentList.add(PrintFormat.transJson(3, " ", 0).toString());
            contentList.add(PrintFormat.transJson(3, " ", 0).toString());

            helper.printReceipt(contentList, BaseApplication.getAppContext());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseApplication.getAppContext());
            builder.setTitle("提示").setMessage(msg).setPositiveButton(msg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    /**
     * 小票打印内容
     * @param contentList 打印列表
     * @param merShortName 商户名称
     * @param merCode 商户编号
     * @param terminalNo 终端编号
     * @param storesNum 门店号
     * @param payType 支付类型
     * @param cardNum 卡号
     * @param receiveSpecial 收单行
     * @param cardIssuers 发卡行
     * @param periodOfValidity 有效期
     * @param payTypeStr 支付类型中文
     * @param orderDate 日期时间
     * @param orderAmt 交易金额
     * @param orderDesc 备注
     * @param orderNo 订单号
     */
    private void initContentList(List<String> contentList, String merShortName, String merCode
            , String terminalNo, String storesNum, String payType, String cardNum, String receiveSpecial
            , String cardIssuers, String periodOfValidity, String payTypeStr
            , String orderDate, String orderAmt, String orderDesc, String orderNo) {
        contentList.add(PrintFormat.transJson(1
                , "——————————————————————"
                , 0).toString());
        contentList.add(PrintFormat.transJson(2,
                StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.merchant_name), merShortName)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2,
                StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.merchant_num), merCode)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.terminal_num), terminalNo)
                , 0).toString());
        //TODO 门店号暂无
        if (StringUtil.isNotEmpty(storesNum)) {
            contentList.add(PrintFormat.transJson(2
                    , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.stores_num), storesNum)
                    , 0).toString());
        }
        contentList.add(PrintFormat.transJson(1
                , "——————————————————————"
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.transaction_type), payTypeStr)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.date_and_time), orderDate)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , BaseApplication.getAppContext().getResources().getString(R.string.deal_money)
                , 0).toString());
        contentList.add(PrintFormat.transJson(3
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.rmb), " ", orderAmt)
                , 0
                , PrintConstant.Align.CENTER).toString());
        contentList.add(PrintFormat.transJson(1
                , "——————————————————————"
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.note), StringUtil.toString(orderDesc))
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.order_num), orderNo)
                , 0).toString());
        contentList.add(PrintFormat.transJson(1
                , "——————————————————————"
                , 0).toString());
    }

    /**
     * 盛付通小票打印内容
     * @param contentList 打印列表
     * @param merShortName 商户名称
     * @param merCode 商户编号
     * @param terminalNo 终端编号
     * @param storesNum 门店号
     * @param payType 支付类型
     * @param cardNum 卡号
     * @param receiveSpecial 收单行
     * @param cardIssuers 发卡行
     * @param periodOfValidity 有效期
     * @param payTypeStr 支付类型中文
     * @param orderDate 日期时间
     * @param orderAmt 交易金额
     * @param orderDesc 备注
     * @param orderNo 订单号
     */
    private void initShengPayContentList(List<String> contentList, String merShortName, String merCode
            , String terminalNo, String storesNum, String payType, String cardNum, String receiveSpecial
            , String cardIssuers, String periodOfValidity, String payTypeStr
            , String orderDate, String orderAmt, String orderDesc, String orderNo) {
        contentList.add(PrintFormat.transJson(2
                , "——————————————————————"
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.merchant_name), merShortName)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.merchant_num), merCode)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.terminal_num), terminalNo)
                , 0).toString());
        //TODO 门店号暂无
        if (StringUtil.isNotEmpty(storesNum)) {
            contentList.add(PrintFormat.transJson(2
                    , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.stores_num), storesNum)
                    , 0).toString());
        }
        contentList.add(PrintFormat.transJson(2
                , "——————————————————————"
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.transaction_type), payTypeStr)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.date_and_time), orderDate)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , BaseApplication.getAppContext().getResources().getString(R.string.deal_money)
                , 0).toString());
        contentList.add(PrintFormat.transJson(3
                , StringUtil.append("\t\t\t\t\t\t\t\t\t\t", BaseApplication.getAppContext().getResources().getString(R.string.rmb), " ", orderAmt)
                , 0
                , PrintConstant.Align.CENTER).toString());
        contentList.add(PrintFormat.transJson(2
                , "——————————————————————"
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.note), StringUtil.toString(orderDesc))
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , StringUtil.append(BaseApplication.getAppContext().getResources().getString(R.string.order_num), orderNo)
                , 0).toString());
        contentList.add(PrintFormat.transJson(2
                , "——————————————————————"
                , 0).toString());
    }
}
