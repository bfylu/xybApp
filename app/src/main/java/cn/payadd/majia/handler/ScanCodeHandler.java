package cn.payadd.majia.handler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.decoding.DecodeFormatManager;
import com.uuzuche.lib_zxing.view.ViewfinderResultPointCallback;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import cn.payadd.majia.face.ICallback;

/**
 * Created by zhengzhen.wang on 2017/6/14.
 */

public class ScanCodeHandler extends Handler {

    private static final String LOG_TAG = "ScanCodeHandler";

    public static final String BARCODE_BITMAP = "barcode_bitmap";

    private Activity activity;

    private ViewfinderView viewfinderView;

    private ScanCodeHandler.State state;

    private ICallback callback;

    private DecodeQrcodeHandler decodeQrcodeHandler;

    private Thread decodeThread;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public ScanCodeHandler(Activity activity, final ViewfinderView viewfinderView, ICallback callback) {

        this.activity = activity;
        this.viewfinderView = viewfinderView;
        this.callback = callback;

        Runnable runnable = new Runnable() {

            private CountDownLatch handlerInitLatch;

            private Map<DecodeHintType, Object> hints;

            {
                handlerInitLatch = new CountDownLatch(1);

                Vector<BarcodeFormat> decodeFormats = new Vector<>();
                decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
                decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
                decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);

                hints = new HashMap<>(3);
                hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

//                if (charset != null) {
//                    hints.put(DecodeHintType.CHARACTER_SET, charset);
//                }

                hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, new ViewfinderResultPointCallback(viewfinderView));
            }

            @Override
            public void run() {
                Looper.prepare();
                decodeQrcodeHandler = new DecodeQrcodeHandler(ScanCodeHandler.this, hints);
                handlerInitLatch.countDown();
                Looper.loop();
            }
        };

        decodeThread = new Thread(runnable);
        decodeThread.start();
        state = ScanCodeHandler.State.SUCCESS;
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {

        if (message.what == com.uuzuche.lib_zxing.R.id.auto_focus) {
            if (state == ScanCodeHandler.State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, com.uuzuche.lib_zxing.R.id.auto_focus);
            }
        } else if (message.what == com.uuzuche.lib_zxing.R.id.restart_preview) {
            Log.d(LOG_TAG, "Got restart preview message");
            restartPreviewAndDecode();
        } else if (message.what == com.uuzuche.lib_zxing.R.id.decode_succeeded) {
            Log.d(LOG_TAG, "Got decode succeeded message");
            state = ScanCodeHandler.State.SUCCESS;
            Bundle bundle = message.getData();

            /***********************************************************************/
            Bitmap barcode = bundle == null ? null : (Bitmap) bundle.getParcelable(ScanCodeHandler.BARCODE_BITMAP);
            callback.exec(message.obj);
            /***********************************************************************/
        } else if (message.what == com.uuzuche.lib_zxing.R.id.decode_failed) {
            // We're decoding as fast as possible, so when one decode fails, start another.
            state = ScanCodeHandler.State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeQrcodeHandler, com.uuzuche.lib_zxing.R.id.decode);
        } else if (message.what == com.uuzuche.lib_zxing.R.id.return_scan_result) {
            Log.d(LOG_TAG, "Got return scan result message");
            activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
            activity.finish();
        } else if (message.what == com.uuzuche.lib_zxing.R.id.launch_product_query) {
            Log.d(LOG_TAG, "Got product query message");
            String url = (String) message.obj;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            activity.startActivity(intent);
        }
    }

    public void quitSynchronously() {
        state = ScanCodeHandler.State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeQrcodeHandler, com.uuzuche.lib_zxing.R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(com.uuzuche.lib_zxing.R.id.decode_succeeded);
        removeMessages(com.uuzuche.lib_zxing.R.id.decode_failed);
    }

    public void restartPreviewAndDecode() {
        if (state == ScanCodeHandler.State.SUCCESS) {
            state = ScanCodeHandler.State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeQrcodeHandler, com.uuzuche.lib_zxing.R.id.decode);
            CameraManager.get().requestAutoFocus(this, com.uuzuche.lib_zxing.R.id.auto_focus);
            viewfinderView.drawViewfinder();
        }
    }
}
