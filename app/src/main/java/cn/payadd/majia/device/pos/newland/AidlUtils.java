package cn.payadd.majia.device.pos.newland;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.nld.cloudpos.aidl.AidlDeviceService;
import com.nld.cloudpos.aidl.printer.AidlPrinter;
import com.nld.cloudpos.aidl.printer.AidlPrinterListener;
import com.nld.cloudpos.aidl.printer.PrintItemObj;
import com.nld.cloudpos.aidl.scan.AidlScanner;
import com.nld.cloudpos.aidl.scan.AidlScannerListener;
import com.nld.cloudpos.aidl.system.AidlSystem;
import com.nld.cloudpos.data.PrinterConstant;
import com.nld.cloudpos.data.ScanConstant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.device.pos.PosScanListener;
import cn.payadd.majia.device.pos.PrintConstant;

public class AidlUtils {
	public final String TAG = "lyc";
	public Context context;
	public AidlDeviceService aidlDeviceService;

	private static Map<Integer,Font> fontSizes;
	{
		fontSizes = new HashMap<>();
		fontSizes.put(PrintConstant.FontSize.SMALL,new Font(PrinterConstant.FontScale.FONTSCALE_W_H,PrinterConstant.FontType.FONTTYPE_S));
		fontSizes.put(PrintConstant.FontSize.NORMAL,new Font(PrinterConstant.FontScale.FONTSCALE_W_H,PrinterConstant.FontType.FONTTYPE_N));
		fontSizes.put(PrintConstant.FontSize.LARGE,new Font(PrinterConstant.FontScale.FONTSCALE_DW_DH,PrinterConstant.FontType.FONTTYPE_N));
	}
	/**
	 * 打印机
	 */
	AidlPrinter aidlPrinter = null;

	public AidlUtils(final AidlDeviceService aidlDeviceService, Context context) {
		this.aidlDeviceService = aidlDeviceService;
		this.context = context;

	}

	public void getPrinter() throws RemoteException {
		Log.i(TAG, "获取打印机设备实例...");
		aidlPrinter = AidlPrinter.Stub.asInterface(aidlDeviceService.getPrinter());
	}
	/**
	 * 打印文本
	 */
	public void printText(List<String> contentList) {
		try {
			final List<PrintItemObj> data = new ArrayList<>();

			for (String jsonStr : contentList) {
				JSONObject json = new JSONObject(jsonStr);
				int fontSize = json.getInt("fontSize");
				String content =json.getString("content");
				Font font = fontSizes.get(fontSize);
				if(json.isNull("align")){
					data.add(new PrintItemObj(content, font.getFontScale(), font.getFontType(), PrintItemObj.ALIGN.LEFT, false, 6));
				}else{
					String align = json.getString("align");
					switch (align){
						case "left":
							data.add(new PrintItemObj(content, font.getFontScale(), font.getFontType(), PrintItemObj.ALIGN.LEFT, false, 6));
							break;
						case "center":
							data.add(new PrintItemObj(content, font.getFontScale(), font.getFontType(), PrintItemObj.ALIGN.CENTER, false, 6));
							break;
						case "right":
							data.add(new PrintItemObj(content, font.getFontScale(), font.getFontType(), PrintItemObj.ALIGN.RIGHT, false, 6));
							break;
					}
				}

				int lineFeedCount = json.getInt("lineFeedCount");

				for (int i = 0, size = lineFeedCount; i < size; i++) {
					data.add(new PrintItemObj("\n"));
				}
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (aidlPrinter != null) {
						try {
							aidlPrinter.open();
							aidlPrinter.printText(data);
							aidlPrinter.start(new AidlPrinterListener.Stub() {

								@Override
								public void onPrintFinish() throws RemoteException {
									Log.e(TAG, "打印结束");
								}

								@Override
								public void onError(int errorCode) throws RemoteException {
									Log.e(TAG, "打印异常");
								}
							});
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}else {
						Log.e(TAG, "未检测到打印机模块访问权限");
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打印二维码
	 * @param offset 偏移量
	 * @param height 高度
	 * @param qrCode 二维码
	 */
	public void printQrCode(final int offset, final int height, final String qrCode) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (aidlPrinter != null) {
						try {
							aidlPrinter.open();
							aidlPrinter.printQrCode(offset, height, qrCode);
							aidlPrinter.start(new AidlPrinterListener.Stub() {

								@Override
								public void onPrintFinish() throws RemoteException {
									Log.e(TAG, "打印结束");
								}

								@Override
								public void onError(int errorCode) throws RemoteException {
									Log.e(TAG, "打印异常");
								}
							});
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}else {
						Log.e(TAG, "未检测到打印机模块访问权限");
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	AidlSystem aidlSystem = null;
	/**
	 * 获取AidlSystem实例
	 */
	public void getSystemInfo() throws RemoteException{
		aidlSystem = AidlSystem.Stub.asInterface(aidlDeviceService.getSystemService());
	}

	/**
	 * 获取终端号
	 */
	public String getSerialNo() {
		String serialNo = null;
		try {
			serialNo = aidlSystem.getSerialNo();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		return serialNo;
	}

	AidlScanner aidlScanner = null;

	/**
	 * 初始化扫码设备
	 */
	public void initScanner() {
		try {
			if (aidlScanner == null)
				aidlScanner = AidlScanner.Stub.asInterface(aidlDeviceService.getScanner());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 前置扫码
	 */
	public void frontScan(final PosScanListener scanListener){
		try {
			Log.i(TAG, "-------------scan-----------");
			String authCode = null;
			aidlScanner= AidlScanner
					.Stub.asInterface(aidlDeviceService.getScanner());
			aidlScanner.startScan(ScanConstant.ScanType.FRONT, 60, new AidlScannerListener.Stub() {

				@Override
				public void onScanResult(String[] arg0) throws RemoteException {
					Log.w(TAG,"onScanResult-----"+arg0[0]);
					if(scanListener != null){
						scanListener.onScanResult(arg0);
					}

				}

				@Override
				public void onFinish() throws RemoteException {
					if(scanListener != null){
						scanListener.onFinish();
					}
				}

				@Override
				public void onError(int arg0, String arg1) throws RemoteException {
					if(scanListener != null){
						scanListener.onFail(arg0,arg1);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopScan(){
		try {
			Log.i(TAG, "------------stopscan-----------");
			aidlScanner= AidlScanner
					.Stub.asInterface(aidlDeviceService.getScanner());
			aidlScanner.stopScan();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
