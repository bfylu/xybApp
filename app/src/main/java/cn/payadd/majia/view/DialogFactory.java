package cn.payadd.majia.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by df on 2017/9/4.
 */

public class DialogFactory {
    public static String ALERT_DIALOG = "alert";

    public AlertDialog showAlertDialog(Context context, String title, String message, String
            pBtnName, String nBtnName, DialogInterface.OnClickListener pBtnListener,
                                      DialogInterface.OnClickListener nBtnListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setMessage(message).setPositiveButton
                (pBtnName, pBtnListener).setNegativeButton(nBtnName, nBtnListener);
        AlertDialog dialog = builder.create();
        return dialog;
    }


}
