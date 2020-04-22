package cn.payadd.majia.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.uuzuche.lib_zxing.camera.CameraManager;
import com.uuzuche.lib_zxing.camera.PlanarYUVLuminanceSource;

import java.util.Map;

import cn.payadd.majia.util.AppLog;

/**
 * Created by zhengzhen.wang on 2017/6/14.
 */

public class DecodeQrcodeHandler extends Handler {

    private static final String LOG_TAG = "DecodeQrcodeHandler";

    private MultiFormatReader multiFormatReader;

    private Handler scanCodeHandler = null;

    public DecodeQrcodeHandler(Handler scanCodeHandler, Map<DecodeHintType, Object> hints) {
        this.scanCodeHandler = scanCodeHandler;
        this.multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == com.uuzuche.lib_zxing.R.id.decode) {
            decode((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == com.uuzuche.lib_zxing.R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    /**
     * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
     * reuse the same reader objects from one decode to the next.
     *
     * @param data   The YUV preview frame.
     * @param width  The width of the preview frame.
     * @param height The height of the preview frame.
     */
    private void decode(byte[] data, int width, int height) {

        long start = System.currentTimeMillis();
        Result rawResult = null;

        //modify here
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;

        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, width, height);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException re) {
            // continue
        } finally {
            multiFormatReader.reset();
        }

        if (rawResult != null) {
            long end = System.currentTimeMillis();
            AppLog.d(LOG_TAG, "Found barcode (" + (end - start) + " ms):\n" + rawResult.toString());
            Message message = Message.obtain(scanCodeHandler, com.uuzuche.lib_zxing.R.id.decode_succeeded, rawResult);
            Bundle bundle = new Bundle();
            bundle.putParcelable(ScanCodeHandler.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
            message.setData(bundle);
            //Log.d(TAG, "Sending decode succeeded message...");
            message.sendToTarget();
        } else {
            Message message = Message.obtain(scanCodeHandler, com.uuzuche.lib_zxing.R.id.decode_failed);
            message.sendToTarget();
        }
    }
}
