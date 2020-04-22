package cn.payadd.majia.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zhengzhen.wang on 2017/6/21.
 */

public class RepayOfMonthDialog extends DialogFragment implements View.OnClickListener {

    public static final String LOG_TAG = "RepayOfMonthDialog";

    private JSONArray data;

    private Context mContext;

    private LinearLayout llRepay;

    private TextView tvInstallmentAmt, tvInstallmentMonth;

    private String installmentAmt, installmentOfMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_repay_of_month, null);
        tvInstallmentAmt = (TextView) v.findViewById(R.id.tv_installment_amount);
        tvInstallmentMonth = (TextView) v.findViewById(R.id.tv_installment_month);
        llRepay = (LinearLayout) v.findViewById(R.id.ll_repay);
//        v.findViewById(R.id.iv_close).setOnClickListener(this);
        v.findViewById(R.id.tv_isee).setOnClickListener(this);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {

        super.onResume();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.9), (int) (dm.heightPixels * 0.8));

        tvInstallmentAmt.setText(installmentAmt);
        tvInstallmentMonth.setText(installmentOfMonth);

        llRepay.removeAllViews();
        createRow();
    }

    private void createRow() {

        if (null == data) {
            return;
        }
        try {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            for (int i = 0, j = data.length(); i < j; i++) {

                JSONObject jsonObj = data.getJSONObject(i);

                View row = inflater.inflate(R.layout.row_installment_repay, null);
                TextView tvRepayDate = (TextView) row.findViewById(R.id.tv_repay_date);
                tvRepayDate.setText(jsonObj.getString("repayDate"));
                TextView tvRepayAmt = (TextView) row.findViewById(R.id.tv_repay_amt);
                tvRepayAmt.setText(String.format("¥%s(含手续费¥%s)", new Object[]{jsonObj.getString("repayAmount"), jsonObj.getString("chargeAmount")}));
                llRepay.addView(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {

        this.data = data;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getInstallmentAmt() {
        return installmentAmt;
    }

    public void setInstallmentAmt(String installmentAmt) {
        this.installmentAmt = installmentAmt;
    }

    public String getInstallmentOfMonth() {
        return installmentOfMonth;
    }

    public void setInstallmentOfMonth(String installmentOfMonth) {
        this.installmentOfMonth = installmentOfMonth;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_close: {
                dismiss();
                break;
            }
            case R.id.tv_isee: {
                dismiss();
                break;
            }
        }
    }
}
