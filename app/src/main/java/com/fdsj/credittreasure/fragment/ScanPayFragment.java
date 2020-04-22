package com.fdsj.credittreasure.fragment;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fdsj.credittreasure.Interface.iActivities.IActivity;
import com.fdsj.credittreasure.Interface.iActivities.IQRCode;
import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.PaymentActivity;
import com.fdsj.credittreasure.presenter.FlowDetailPresenter;
import com.fdsj.credittreasure.presenter.PaymentPresenter;
import com.fdsj.credittreasure.zxing.CheryActivityHandler;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.utils.Enums;
import com.utils.LogUtil;
import com.utils.StatusBarUtils;
import com.utils.ToastUtils;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.decoding.InactivityTimer;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>项目名称：MAJIA
 * <p>描   述： 扫一扫付款
 * <p>当前版本： V1.0.0
 */

public class ScanPayFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener, View.OnTouchListener, IQRCode, IActivity {


    private CheryActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    //    private CodeUtils.AnalyzeCallback analyzeCallback;//扫码回调
    private Camera camera;
    private String result;//扫码结果


    @BindView(R.id.text_Intelligence)
    TextView text_Intelligence;//智能
    @BindView(R.id.text_wx)
    TextView text_wx;//微信
    @BindView(R.id.text_alipay)
    TextView text_alipay;//支付宝
    @BindView(R.id.text_unionpay)
    TextView text_unionpay;//银联

    @BindView(R.id.text_amount)
    TextView text_amount;

    // 滑动手势
    private GestureDetector detector;
    private Enums.httpPayType[] payTypes = {Enums.httpPayType.智能支付, Enums.httpPayType.微信支付, Enums.httpPayType.支付宝, Enums.httpPayType.银联支付};
    public static Enums.httpPayType payType = Enums.httpPayType.智能支付;
    private Activity activity;
    private PaymentPresenter presenter;//p
    private FlowDetailPresenter flowDetailPresenter;//获取订单状态
    private BigDecimal amount = new BigDecimal(0);//"0.00";
    private DecimalFormat df = new DecimalFormat("######0.00");
    private String orderNo = "";
    private TimeCount timeCount;

    public static ScanPayFragment getZxingFragment(BigDecimal amount) {
        ScanPayFragment scanPayFragment = new ScanPayFragment();
        scanPayFragment.amount = amount;
        return scanPayFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.init(getActivity().getApplication());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zxing, container, false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        viewfinderView = (ViewfinderView) view.findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) view.findViewById(R.id.preview_view);
        surfaceHolder = surfaceView.getHolder();
        view.setOnTouchListener(this);
        detector = new GestureDetector(activity, new MyOnGestureListener());
        presenter = new PaymentPresenter(this);
        timeCount = new TimeCount(2000, 1000);
        StatusBarUtils.setColor(activity, R.color.help_button_view);
        text_amount.setText(df.format(amount));
        flowDetailPresenter = new FlowDetailPresenter(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setTextColor(PaymentActivity.anInt);
    }

    @OnClick({R.id.text_Intelligence, R.id.text_wx, R.id.text_alipay, R.id.text_unionpay, R.id.bt_gain_status})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_Intelligence://智能
                setTextColor(PaymentActivity.Intelligence);
                break;
            case R.id.text_wx://微信
                setTextColor(PaymentActivity.WeChat);
                break;
            case R.id.text_alipay://支付宝
                setTextColor(PaymentActivity.Alipay);
                break;
            case R.id.text_unionpay://银联
                PaymentActivity payActivity = (PaymentActivity) getActivity();
                payActivity.nowUnionpay();
                break;
            case R.id.bt_gain_status:
                if (!TextUtils.isEmpty(orderNo)) {
//                    Intent intent = new Intent();
//                    intent.setClass(activity, PaymentDetailActivity.class);//支付详情
//                    intent.putExtra("orderNo", orderNo);
//                    startActivity(intent);
                    flowDetailPresenter.queryOrderStatus(orderNo, activity, 0);
                } else {
                    ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.no_order));
                }
                break;
        }
    }

    public void setTextColor(int r) {
        if (r == PaymentActivity.Intelligence) {
            ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.no_function));
        } else {
            PaymentActivity.anInt = r;
            text_unionpay.setTextColor(getResources().getColor(r == 3 ? R.color.color_c7c03b : R.color.color_f));
            text_alipay.setTextColor(getResources().getColor(r == 2 ? R.color.color_c7c03b : R.color.color_f));
            text_wx.setTextColor(getResources().getColor(r == 1 ? R.color.color_c7c03b : R.color.color_f));
            text_Intelligence.setTextColor(getResources().getColor(r == 0 ? R.color.color_c7c03b : R.color.color_f));
            payType = payTypes[r];
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    /**
     * 扫码失败
     */
    @Override
    public void NoModel() {
//        ToastUtils.showToast(activity, activity.getResources().getString(R.string.scanError));
        timeCount.start();
    }

    /**
     * 扫码成功
     */
    @Override
    public void UpdateModel(String payUrl, String orderNo, int status) {

//        ((PaymentActivity)activity).currentOrderNo = orderNo;
//        LogUtil.info("orderNo", orderNo);
        if (!TextUtils.isEmpty(orderNo)) {
            this.orderNo = orderNo;
            ToastUtils.showToast(activity, activity.getResources().getString(R.string.scanOk));
        } else {
            ToastUtils.showToast(activity, activity.getResources().getString(R.string.scanError));
        }
        timeCount.start();
    }

    /**
     * 扫码结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        if (result == null || TextUtils.isEmpty(result.getText())) {
//            if (analyzeCallback != null) {
//                analyzeCallback.onAnalyzeFailed();
//            }
            ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.scanner_faile));
        } else {
            if (payType == Enums.httpPayType.智能支付) {
                ToastUtils.showToast(activity, getActivity().getResources().getString(R.string.no_function));
                timeCount.start();
            } else if (payType == Enums.httpPayType.银联支付) {

                PaymentActivity payActivity = (PaymentActivity) getActivity();
                payActivity.nowUnionpay();

            } else {
                this.result = result.getText().toString();
                LogUtil.info("123456", "扫码结果" + this.result);
                if (this.result.length() > 2) {
                    String code = this.result.substring(0, 2);
                    LogUtil.info("123456", "扫码Code:" + code);
                    //13 微信，， 28支付宝
                    if (code.equals("13") || code.equals("28")) {
                        if ((payType == Enums.httpPayType.微信支付 && code.equals("13")) || (payType == Enums.httpPayType.支付宝 && code.equals("28"))) {
                            presenter.submitOrder(PaymentActivity.anInt, df.format(amount), this.result, Enums.payMethod.扫码, payType, activity);
                        } else {
                            ToastUtils.showToast(activity, code.equals("13") ? activity.getResources().getString(R.string.alipayNotOk) : activity.getResources().getString(R.string.weixinNotOk));
                            timeCount.start();
                        }
                    } else {
                        presenter.submitOrder(PaymentActivity.anInt, df.format(amount), this.result, Enums.payMethod.扫码, payType, activity);
                    }
                } else {
                    presenter.submitOrder(PaymentActivity.anInt, df.format(amount), this.result, Enums.payMethod.扫码, payType, activity);
                }
            }
        }
    }

    @Override
    public void stopRecyclerView() {
        ToastUtils.showToast(activity, "未获取到订单信息");
    }

    @Override
    public void UpdateModel(Object model, int status) {
        Map<String, String> stringMap = (Map<String, String>) model;
        String orderStatus = stringMap.get("orderStatus");
        if (orderStatus.equals(Enums.orderStatusName.成功.getString())) {
            ToastUtils.showToast(activity, "支付成功");
        } else if (orderStatus.equals(Enums.orderStatusName.处理中.getString())) {
            ToastUtils.showToast(activity, "支付处理中");
        } else {
            ToastUtils.showToast(activity, "支付失败");
        }
    }

    //设置手势识别监听器
    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override//此方法必须重写且返回真，否则onFling不起效
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if ((e1.getX() - e2.getX() > 120) && Math.abs(velocityX) > 200) {
                if (PaymentActivity.anInt < 3) {
                    PaymentActivity.anInt = PaymentActivity.anInt + 1;
                }
                setTextColor(PaymentActivity.anInt);
                return true;
            } else if ((e2.getX() - e1.getX() > 120) && Math.abs(velocityX) > 200) {
                if (PaymentActivity.anInt > 0) {
                    PaymentActivity.anInt = PaymentActivity.anInt - 1;
                }
                setTextColor(PaymentActivity.anInt);
                return true;
            }
            return false;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("ScanPayFragment", "onResume");
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("ScanPayFragment", "pause");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        timeCount.cancel();
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ScanPayFragment", "onDestroy");
        inactivityTimer.shutdown();
        super.onDestroy();
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            camera = CameraManager.get().getCamera();
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CheryActivityHandler(this, decodeFormats, characterSet, viewfinderView);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
        if (camera != null) {
            if (camera != null && CameraManager.get().isPreviewing()) {
                if (!CameraManager.get().isUseOneShotPreviewCallback()) {
                    camera.setPreviewCallback(null);
                }
                camera.stopPreview();
                CameraManager.get().getPreviewCallback().setHandler(null, 0);
                CameraManager.get().getAutoFocusCallback().setHandler(null, 0);
                CameraManager.get().setPreviewing(false);
            }
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(com.uuzuche.lib_zxing.R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    /**
     * 两秒后让扫码重新开始
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            LogUtil.info("onFinish", "onFinish");
            if (handler != null)
                handler.restartPreviewAndDecode();
        }
    }

}