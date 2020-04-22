package cn.payadd.majia.util.aistore;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.application.BaseApplication;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cn.payadd.majia.activity.aistore.AIStoreMainActivity;
import cn.payadd.majia.activity.aistore.ChatActivity;
import cn.payadd.majia.entity.aistore.CustomMessage;
import cn.payadd.majia.entity.aistore.Message;
import cn.payadd.majia.entity.aistore.MessageFactory;
import cn.payadd.majia.im.presentation.event.MessageEvent;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * 在线消息通知展示
 */
public class PushUtil implements Observer {

    private static final String TAG = PushUtil.class.getSimpleName();

    private static int pushNum=0;

    private final int pushId=1;

    private String senderStr, contentStr;

    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder mBuilder;

    private PendingIntent intent;

    private static PushUtil instance = new PushUtil();

    private PushUtil() {
        MessageEvent.getInstance().addObserver(this);
    }

    public static PushUtil getInstance(){
        return instance;
    }



    private void PushNotify(TIMMessage msg){
        //系统消息，自己发的消息，程序在前台的时候不通知
        if (msg==null||Foreground.get().isForeground()||
                (msg.getConversation().getType()!= TIMConversationType.Group&&
                        msg.getConversation().getType()!= TIMConversationType.C2C)||
                msg.isSelf()||
                msg.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify ||
                MessageFactory.getMessage(msg) instanceof CustomMessage) return;

        Message message = MessageFactory.getMessage(msg);
        if (message == null) return;

        contentStr = message.getSummary();
        Log.d(TAG, "recv msg " + contentStr);
        mNotificationManager = (NotificationManager) BaseApplication.getAppContext().getSystemService(BaseApplication.getAppContext().NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(BaseApplication.getAppContext());
        Intent notificationIntent = new Intent(BaseApplication.getAppContext(), ChatActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.putExtra("identify", message.getSender());
        notificationIntent.putExtra("type", TIMConversationType.C2C);
        intent = PendingIntent.getActivity(BaseApplication.getAppContext(), 0,
                notificationIntent, FLAG_UPDATE_CURRENT);

        List<String> list = new ArrayList<>();
        list.add(message.getSender());
        TIMFriendshipManager.getInstance().getUsersProfile(list, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                senderStr = timUserProfiles.get(0).getNickName();

                mBuilder.setContentTitle(senderStr)//设置通知栏标题
                        .setContentText(contentStr)
                        .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                        .setTicker(senderStr+":"+contentStr) //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        .setSmallIcon(R.mipmap.logo);//设置通知小ICON
                Notification notify = mBuilder.build();
                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotificationManager.notify(pushId, notify);
            }
        });
    }

    public static void resetPushNum(){
        pushNum=0;
    }

    public void reset(){
        NotificationManager notificationManager = (NotificationManager) BaseApplication.getAppContext().getSystemService(BaseApplication.getAppContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(pushId);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            if (data instanceof TIMMessage) {
                TIMMessage msg = (TIMMessage) data;
                if (msg != null){
                    PushNotify(msg);
                }
            }
        }
    }
}
