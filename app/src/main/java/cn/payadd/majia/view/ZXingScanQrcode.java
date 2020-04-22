package cn.payadd.majia.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.CodaBarReader;
import com.google.zxing.qrcode.QRCodeReader;
import com.uuzuche.lib_zxing.camera.CameraManager;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.util.AppLog;

/**
 * Created by zhengzhen.wang on 2017/6/16.
 */

public class ZXingScanQrcode implements SurfaceHolder.Callback, Camera.PreviewCallback, Camera.AutoFocusCallback{

    public static final int PREVIEW = 0;
    public static final int SUCCESS = 1;
    public static final int FAILURE = 2;
    public static final int DONE = 3;
    public static final int RE_PREVIEW = 4;

    private static final String LOG_TAG = "ZXingScanQrcode";

    private static final float BEEP_VOLUME = 0.10f;

    private static final long VIBRATE_DURATION = 200L;

    private Context mContext;

    private SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;

    private ViewfinderView mViewfinderView;

    private boolean playBeep, vibrate;

    private MediaPlayer mediaPlayer;

    private QRCodeReader reader;

    private Map<DecodeHintType, Object> hints;



    private boolean onlyParseArea = false;

    private ICallback callback;

    private String readQrcodeContent;

    private Pattern pattern = Pattern.compile("\\d+");

    private volatile boolean isRelease;

    private Timer timer;

    public ZXingScanQrcode(Context context, SurfaceView surfaceView, ViewfinderView viewfinderView, boolean onlyParseArea, ICallback callback,int type) {

        this.mContext = context;
        this.mSurfaceView = surfaceView;
        this.mViewfinderView = viewfinderView;
        this.onlyParseArea = onlyParseArea;
        this.callback = callback;
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);

    }

    public ZXingScanQrcode(Context context, SurfaceView surfaceView, ViewfinderView viewfinderView, ICallback callback,int type) {

        this(context, surfaceView, viewfinderView, false, callback,type);
    }

    public void init() {
        isRelease = false;
        initCamera();
    }

    private void initCamera() {

        try {
            CameraManager.get().openDriver(mSurfaceHolder);
            final Camera mCamera = CameraManager.get().getCamera();
//            mCamera = Camera.open();//默认开启后置
            mCamera.setDisplayOrientation(90);//摄像头进行旋转90°
            if (null != mCamera) {
                Camera.Parameters parameters = mCamera.getParameters();
                //设置预览照片的大小
//                parameters.setPreviewFpsRange(viewWidth, viewHeight);
                //设置相机预览照片帧数
                parameters.setPreviewFpsRange(4, 10);
                //设置图片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //设置图片的质量
                parameters.set("jpeg-quality", 90);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                //设置照片的大小
//                parameters.setPictureSize(viewWidth, viewHeight);
                initBeepSound();
            }

            cameraHandler.sendEmptyMessage(PREVIEW);

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (!isRelease) {
                            mCamera.cancelAutoFocus();
                            mCamera.autoFocus(ZXingScanQrcode.this);
                    }
                }
            }, 1500, 3000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        cameraHandler.sendEmptyMessage(PREVIEW);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        try {
            if (null != timer) {
                timer.cancel();
                timer.purge();
            }
            CameraManager.get().closeDriver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler cameraHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            AppLog.d(LOG_TAG, "what -> " + msg.what);
            final Camera mCamera = CameraManager.get().getCamera();
            try {
                switch (msg.what) {
                    case PREVIEW: {
                        try {
                            if (null != mCamera) {
                                //通过SurfaceView显示预览
                                mCamera.setPreviewDisplay(mSurfaceHolder);
                                //开始预览
                                mCamera.startPreview();
                                mCamera.cancelAutoFocus();
//                                mCamera.setAutoFocusMoveCallback(ZXingScanQrcode.this);
                                mCamera.autoFocus(ZXingScanQrcode.this);
                                mCamera.setOneShotPreviewCallback(ZXingScanQrcode.this);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case RE_PREVIEW: {
                        AppLog.d(LOG_TAG, "isRelease: " + isRelease);
                        if (!isRelease) {
//                            mCamera.cancelAutoFocus();
//                            mCamera.autoFocus(ZXingScanQrcode.this);
                            mCamera.setOneShotPreviewCallback(ZXingScanQrcode.this);
                        }
                        break;
                    }
                    case SUCCESS: {
                        if (!isRelease) {
//                            mCamera.cancelAutoFocus();
//                            mCamera.autoFocus(ZXingScanQrcode.this);
                        }
                        break;
                    }
                    case FAILURE: {
                        if (!isRelease) {
//                            mCamera.cancelAutoFocus();
//                            mCamera.autoFocus(ZXingScanQrcode.this);
                            mCamera.setOneShotPreviewCallback(ZXingScanQrcode.this);
                        }
                        break;
                    }
                    case DONE: {
                        playBeepSoundAndVibrate();
                        String qrcodeText = (String) msg.obj;
                        if (null != callback) {
                            callback.exec(qrcodeText);
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

//        AppLog.d(LOG_TAG, "开始识别");
        try {
            mViewfinderView.drawViewfinder();

            int previewWidth = camera.getParameters().getPreviewSize().width;
            int previewHeight = camera.getParameters().getPreviewSize().height;

            LuminanceSource source = null;
            if (onlyParseArea) {
//                LuminanceSource source = CameraManager.get().buildLuminanceSource(data, previewHeight, previewWidth);
                Rect frame = CameraManager.get().getFramingRectInPreview();
                int preWidth = frame.width();
                int preHeight = frame.height();
                int top = frame.top;
                int left = frame.left;

                source = new PlanarYUVLuminanceSource(
                        data, previewWidth, previewHeight, top, left, preWidth,
                        preHeight, false);
            } else {
                source = new PlanarYUVLuminanceSource(
                        data, previewWidth, previewHeight, 0, 0, previewWidth,
                        previewHeight, false);
            }
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            if (null == reader) {

                reader = new QRCodeReader();
            }

            Result result = reader.decode(bitmap);
            String text = result.getText();
            if (!TextUtils.isEmpty(readQrcodeContent) && readQrcodeContent.equals(text)) {
                AppLog.w(LOG_TAG, "二维码已处理过，qrcode content: " + text);
//                Toast.makeText(mContext,"订单已失效，请退出重新收款",Toast.LENGTH_LONG).show();
                cameraHandler.sendEmptyMessage(RE_PREVIEW);
                return;
            }
            if (!pattern.matcher(text).matches()) {
                AppLog.w(LOG_TAG, "格式错误，qrcode content: " + text);
                cameraHandler.sendEmptyMessage(RE_PREVIEW);
                return;
            }
            readQrcodeContent = text;
            AppLog.d(LOG_TAG, "qrcode=" + text);
            Message msg = new Message();
            msg.what = DONE;
            msg.obj = text;
            cameraHandler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
            cameraHandler.sendEmptyMessage(FAILURE);
        }

    }

    private void playBeepSoundAndVibrate() {
        if (!playBeep && null != mediaPlayer) {
            playBeep = true;
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private void initBeepSound() {
        if (null == mediaPlayer) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            ((Activity)mContext).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = mContext.getResources().openRawResourceFd(com.uuzuche.lib_zxing.R.raw.beep);
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

    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
            playBeep = false;
        }
    };

    public void close() {
        isRelease = true;
        if (null != timer) {
            timer.cancel();
            timer.purge();
        }
        Camera mCamera = CameraManager.get().getCamera();
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
        }
        CameraManager.get().closeDriver();

    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    public void resetQrcode(){
        this.readQrcodeContent = null;
    }

    public Handler getHandler() {

        return cameraHandler;
    }
}
