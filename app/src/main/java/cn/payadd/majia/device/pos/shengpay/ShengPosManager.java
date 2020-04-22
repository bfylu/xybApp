package cn.payadd.majia.device.pos.shengpay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.shengpay.smartpos.shengpaysdk.ITerminalAidl;

/**
 * Created by zhengzhen.wang on 2017/5/9.
 */

public class ShengPosManager {

    public ITerminalAidl terminalAidl;

    private TerminalServConn terminalServConn;

    private static ShengPosManager manager;

    public synchronized static ShengPosManager getInstance() {
        if (null == manager) {
            manager = new ShengPosManager();
        }
        return manager;
    }

    public void bindPosService(Context context) {

        Intent terminalIntent = new Intent();
        terminalIntent.setClassName("com.shengpay.smartpos.shengpaysdk", "com.shengpay.smartpos.shengpaysdk.Service.TerminalService");
        terminalServConn = new TerminalServConn();
        context.bindService(terminalIntent, terminalServConn, Context.BIND_AUTO_CREATE);

    }

    public void unbindPosService(Context context) {

        context.unbindService(terminalServConn);
    }

    class TerminalServConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            terminalAidl = ITerminalAidl.Stub.asInterface(service);

            try {

                ShengPosManager.getInstance().terminalAidl.setAppId(name.getPackageName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            terminalAidl = null;
        }
    }

}
