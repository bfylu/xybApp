package com.fdsj.credittreasure.zxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fdsj.credittreasure.fragment.ScanPayFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.uuzuche.lib_zxing.R;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.view.ViewfinderResultPointCallback;
import com.uuzuche.lib_zxing.view.ViewfinderView;

import java.util.Vector;

/**
 * Created by 冷佳兴 on 2017/1/15-20:54.
 * 我是大傻逼，所在公司:博信诺达
 */

public class CheryActivityHandler extends Handler {

    private static final String TAG = CheryActivityHandler.class.getSimpleName();

    private final ScanPayFragment fragment;
    private final CheryThread decodeThread;
    private CheryActivityHandler.State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public CheryActivityHandler(ScanPayFragment fragment, Vector<BarcodeFormat> decodeFormats, String characterSet, ViewfinderView viewfinderView) {
        this.fragment = fragment;
        decodeThread = new CheryThread(fragment, decodeFormats, characterSet, new ViewfinderResultPointCallback(viewfinderView));
        decodeThread.start();
        state = CheryActivityHandler.State.SUCCESS;
        // Start ourselves capturing previews and decoding.
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.auto_focus) {
            //Log.d(TAG, "Got auto-focus message");
            // When one auto focus pass finishes, start another. This is the closest thing to
            // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
            if (state == CheryActivityHandler.State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            Log.d(TAG, "Got restart preview message");
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            Log.d(TAG, "Got decode succeeded message");
            state = CheryActivityHandler.State.SUCCESS;
            Bundle bundle = message.getData();

            /***********************************************************************/
            Bitmap barcode = bundle == null ? null : (Bitmap) bundle.getParcelable(CheryThread.BARCODE_BITMAP);

            fragment.handleDecode((Result) message.obj, barcode);
            /***********************************************************************/
        } else if (message.what == R.id.decode_failed) {
            // We're decoding as fast as possible, so when one decode fails, start another.
            state = CheryActivityHandler.State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        } else if (message.what == R.id.return_scan_result) {
            Log.d(TAG, "Got return scan result message");
            fragment.getActivity().setResult(Activity.RESULT_OK, (Intent) message.obj);
            fragment.getActivity().finish();
        } else if (message.what == R.id.launch_product_query) {
            Log.d(TAG, "Got product query message");
            String url = (String) message.obj;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            fragment.getActivity().startActivity(intent);
        }
    }

    public void quitSynchronously() {
        state = CheryActivityHandler.State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    public void restartPreviewAndDecode() {
        if (state == CheryActivityHandler.State.SUCCESS) {
            state = CheryActivityHandler.State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            fragment.drawViewfinder();
        }
    }

}