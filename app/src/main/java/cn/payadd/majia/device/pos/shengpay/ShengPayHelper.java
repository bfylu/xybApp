package cn.payadd.majia.device.pos.shengpay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.shengpay.smartpos.shengpaysdk.ITerminalAidl;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import cn.payadd.majia.device.pos.IPosHelper;
import cn.payadd.majia.device.pos.PosScanListener;

/**
 * Created by df on 2017/8/16.
 */

public class ShengPayHelper implements IPosHelper {

    private static ShengPayHelper helper;

    private static final int UNIONPAY_REQ_CODE = 1;


    public synchronized static ShengPayHelper getInstance() {
        if (null == helper) {
            helper = new ShengPayHelper();
        }
        return helper;
    }

    private ShengPayHelper() {

    }

    @Override
    public void init(Context context) {
        ShengPosManager.getInstance().bindPosService(context);
    }

    @Override
    public void initScanner() {

    }

    @Override
    public boolean printReceipt(List<String> contentList, Context context) {
        try {

            ShengPosManager.getInstance().terminalAidl.setAppId(context.getPackageName());
            ITerminalAidl terminalAidl = ShengPosManager.getInstance().terminalAidl;
            terminalAidl.print_setBatchPrint(true);
            StringBuffer printText = new StringBuffer();
            for (String jsonStr : contentList) {
                JSONObject json = new JSONObject(jsonStr);
                terminalAidl.print_setFontSize(json.getInt("fontSize") > 0 ? json.getInt("fontSize") : 2);
                terminalAidl.print_printText(json.getString("content"));
                int lineFeedCount = json.getInt("lineFeedCount");

                for (int i = 0, size = lineFeedCount; i < size; i++) {
                    terminalAidl.print_printText(" ");
                }
            }
            terminalAidl.print_batchPrint();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean printQrCode(int offset, String qrCode, int height, Context context) {
        try {
            ShengPosManager.getInstance().terminalAidl.setAppId(context.getPackageName());
            ITerminalAidl terminalAidl = ShengPosManager.getInstance().terminalAidl;
            terminalAidl.print_setBatchPrint(true);
            terminalAidl.print_printQrCode(offset, qrCode, height);
            terminalAidl.print_batchPrint();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getTerminalNo() {
        String terminalNo = null;
        try {
            terminalNo = ShengPosManager.getInstance().terminalAidl.getSerialNo();
//            terminalNo = null;
        } catch (Exception e) {
            e.printStackTrace();
//            return null;
        }
        return terminalNo;
    }

    @Override
    public Object payForWeixin(BigDecimal amount) {
        return null;
    }

    @Override
    public Object payForAlipay(BigDecimal amount) {
        return null;
    }

    @Override
    public Object payForCard(BigDecimal amount, String orderNo, Activity activity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.shengpay.smartpos.shengpaysdk", "com.shengpay.smartpos.shengpaysdk.activity.MainActivity"));
        intent.putExtra("transName", "0");
        // “0”：银行卡 “1”：微信支付 “2”：支付宝 “3”：盛付通钱包
        intent.putExtra("barcodeType", "0");
        intent.putExtra("appId", activity.getPackageName());
        DecimalFormat df = new DecimalFormat("000000000000");
        intent.putExtra("amount", df.format(amount.multiply(new BigDecimal(100))));
        intent.putExtra("orderNoSFT", orderNo);
//              intent.putExtra("oldTraceNo", "");
//              intent.putExtra("oldReferenceNo", "");
        // 非必填，消费退货，如果不传入则调起收单退货界面
        activity.startActivityForResult(intent, UNIONPAY_REQ_CODE);
        return null;
    }

    @Override
    public Object payForScan(BigDecimal amount) {
        return null;
    }

    @Override
    public void scanQrcode(String scanType, PosScanListener listener) {

    }

    @Override
    public void closeScan() {

    }

    @Override
    public void refundForCard(BigDecimal amount, String orderNo, String proc_cd, Activity activity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.shengpay.smartpos.shengpaysdk", "com.shengpay.smartpos.shengpaysdk.activity.MainActivity"));
        intent.putExtra("transName", "0");
        // “0”：银行卡 “1”：微信支付 “2”：支付宝 “3”：盛付通钱包
        intent.putExtra("barcodeType", "0");
        intent.putExtra("appId", activity.getPackageName());
        DecimalFormat df = new DecimalFormat("000000000000");
        intent.putExtra("amount", df.format(amount.multiply(new BigDecimal(100))));
        intent.putExtra("orderNoSFT", orderNo);
        intent.putExtra("oldTraceNo", "");
        intent.putExtra("oldReferenceNo", "");
        // 非必填，消费退货，如果不传入则调起收单退货界面
        activity.startActivityForResult(intent, UNIONPAY_REQ_CODE);
    }

    @Override
    public void close(Context context) {
        ShengPosManager.getInstance().unbindPosService(context);
    }
}
