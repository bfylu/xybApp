package cn.payadd.majia.device.pos;

import android.content.Context;
import android.widget.Toast;

import cn.payadd.majia.activity.CollectFormActivity;
import cn.payadd.majia.device.pos.newland.NewLandHelper;
import cn.payadd.majia.device.pos.shengpay.ShengPayHelper;

/**
 * Created by df on 2017/8/16.
 */

public class PosDeviceUtil {
    public static final String SHENG_POS_NAME = "shengpos";
    public static final String NEWLAND_POS_NAME = "newland";

    public static IPosHelper getPosHelper(String posName, Context context){
        IPosHelper helper = null;
        switch (posName){
            case SHENG_POS_NAME:
                helper = ShengPayHelper.getInstance();
                break;
            case NEWLAND_POS_NAME:
                helper = NewLandHelper.getInstance();
                break;
            default:
//                Toast.makeText(context, "POS机型号未找到", Toast.LENGTH_LONG).show();
                break;
        }
        return helper;
    }

}
