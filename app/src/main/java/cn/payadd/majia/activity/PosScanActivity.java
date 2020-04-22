package cn.payadd.majia.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.nld.cloudpos.aidl.AidlDeviceService;
import com.nld.cloudpos.aidl.scan.AidlScanner;
import com.nld.cloudpos.aidl.scan.AidlScannerListener;
import com.nld.cloudpos.data.ScanConstant;

/**
 * Created by df on 2018/3/16.
 */

public class PosScanActivity extends BaseActivity{
    AidlDeviceService aidlDeviceService = null;

    AidlScanner aidlScanner=null;
    private String TAG = "lyc";

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "bind device service");
            aidlDeviceService = AidlDeviceService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "unbind device service");
            aidlDeviceService = null;
        }
    };
    /**
     * 绑定服务
     */
    public void bindServiceConnection() {
        bindService(new Intent("nld_cloudpos_device_service"), serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

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
    public void frontscan(){
        try {
            Log.i(TAG, "-------------scan-----------");
            aidlScanner= AidlScanner
                    .Stub.asInterface(aidlDeviceService.getScanner());
            aidlScanner.startScan(ScanConstant.ScanType.FRONT, 10, new AidlScannerListener.Stub() {

                @Override
                    public void onScanResult(final String[] arg0) throws RemoteException {
                        Log.w(TAG,"onScanResult-----"+arg0[0]);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PosScanActivity.this,arg0[0],Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFinish() throws RemoteException {

                }

                @Override
                public void onError(int arg0, String arg1) throws RemoteException {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_pos_scan;
    }

    @Override
    public void initView() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initScanner();
                frontscan();
            }
        });
    }

    @Override
    public void initData() {
        bindServiceConnection();

    }



    @Override
    protected void initPermission() {

    }
}
