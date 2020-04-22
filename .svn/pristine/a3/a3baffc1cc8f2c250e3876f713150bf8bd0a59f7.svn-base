package cn.payadd.majia.device.pos;

import android.app.Activity;
import android.content.Context;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by df on 2017/8/16.
 */

public interface IPosHelper {

    void init(Context context);

    void initScanner();

    boolean printReceipt(List<String> contentList, Context context);

    boolean printQrCode(int offset, String qrCode, int height, Context context);

    String getTerminalNo();

    Object payForWeixin(BigDecimal amount);

    Object payForAlipay(BigDecimal amount);

    Object payForCard(BigDecimal amount, String orderNo, Activity activity);

    Object payForScan(BigDecimal amount);

    void scanQrcode(String scanType, PosScanListener listener);

    void closeScan();

    void refundForCard(BigDecimal amount, String orderNo, String proc_cd, Activity activity);

    void close(Context context);
}
