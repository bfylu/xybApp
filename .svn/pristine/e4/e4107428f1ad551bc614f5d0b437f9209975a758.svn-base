package cn.payadd.majia.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.activity.InstallmentContractActivity;
import cn.payadd.majia.activity.InstallmentDetailActivity;
import cn.payadd.majia.activity.InstallmentOrderActivity;
import cn.payadd.majia.activity.UploadExtInfoActivity;
import cn.payadd.majia.activity.UploadSignInfoActivity;
import cn.payadd.majia.constant.InstallmentStatus;
import cn.payadd.majia.listener.NoDoubleClickListener;
import cn.payadd.majia.presenter.InstallmentPresenter;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by zhengzhen.wang on 2017/6/19.
 */

public class InstallmentOrderAdapter extends BaseAdapter{

    public static Map<String, String> statusMap;

    public static Map<String, String> btnMap;

    static {
        statusMap =  new HashMap<>();
        statusMap.put("1", "待审核");
        statusMap.put("2", "待签约");
        statusMap.put("3", "已拒绝");
        statusMap.put("4", "签约待确定");
        statusMap.put("5", "已签约");

    }


    private Context mContext;

    private LayoutInflater inflater;

    private List<Map<String, String>> data;

    private int page = 1;

    private int totalPage;

    private int row = 10;

    private String showStatus;

    private boolean isShowStatus, isLastPage;

    private boolean isContract;

    private DecimalFormat df = new DecimalFormat("##############0.00");

    public InstallmentOrderAdapter(Context ctx) {
        this.mContext = ctx;
        this.data = new ArrayList<>();
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = inflater.inflate(R.layout.item_installment_order, parent, false);
        v.setId(R.id.item_installment);

        Map<String, String> map = data.get(position);
        TextView tvApplyName = (TextView) v.findViewById(R.id.tv_apply_name);
        tvApplyName.setText(map.get("buyerRealName"));
        TextView tvOrderTime = (TextView) v.findViewById(R.id.tv_order_time);
        tvOrderTime.setText(map.get("createTime"));
        TextView tvOrderAmt = (TextView) v.findViewById(R.id.tv_order_amount);
        BigDecimal bigDecimal = new BigDecimal(map.get("orderAmount"));
        tvOrderAmt.setText(df.format(bigDecimal));
        TextView tvOrderStatus = (TextView) v.findViewById(R.id.tv_order_status);
        TextView tvBtnOpreator = (TextView) v.findViewById(R.id.btn_opreator);
        String status = map.get("status");
        String flag = map.get("supplementFlag");
        v.setTag(R.id.is_update_info,flag);
        tvBtnOpreator.setTag(R.id.installment_order_no, map.get("orderNo"));
        switch (status){
            case InstallmentStatus.VERIFY_PENDING:
                tvOrderStatus.setText(statusMap.get(InstallmentStatus.VERIFY_PENDING));
                tvBtnOpreator.setText("月供详情");
                tvBtnOpreator.setTag(R.id.installment_operation,"repayDetail");
                tvBtnOpreator.setTag(R.id.installment_amt,df.format(bigDecimal));
                tvBtnOpreator.setTag(R.id.installment_number,map.get("installmentMonth"));
                break;
            case InstallmentStatus.PENDING_SIGN:
                tvOrderStatus.setText(statusMap.get(InstallmentStatus.PENDING_SIGN));
                if("Y".equals(flag)){
                    tvBtnOpreator.setText("立即签约");
                    tvBtnOpreator.setTag(R.id.installment_operation,"sign");
                }else{
                    tvBtnOpreator.setText("补充资料");
                    tvBtnOpreator.setTag(R.id.installment_operation,"updateInfo");
                }
                break;
            case InstallmentStatus.VERIFY_REJECT:
                tvOrderStatus.setText(statusMap.get(InstallmentStatus.VERIFY_REJECT));
                tvBtnOpreator.setVisibility(View.GONE);
                break;
            case InstallmentStatus.SIGN_PENDING:
                tvOrderStatus.setText(statusMap.get(InstallmentStatus.SIGN_PENDING));
                tvBtnOpreator.setText("重新签约");
                tvBtnOpreator.setTag(R.id.installment_operation,"sign");

                break;
            case InstallmentStatus.SIGNED:
                tvOrderStatus.setText(statusMap.get(InstallmentStatus.SIGNED));
                tvBtnOpreator.setText("月供详情");
                tvBtnOpreator.setTag(R.id.installment_operation,"repayDetail");
                tvBtnOpreator.setTag(R.id.installment_amt,df.format(bigDecimal));
                tvBtnOpreator.setTag(R.id.installment_number,map.get("installmentMonth"));
                break;
            default:
                break;
        }
        tvBtnOpreator.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_opreator: {
                        String orderNo = (String) v.getTag(R.id.installment_order_no);
                        String operation = (String) v.getTag(R.id.installment_operation);
                        String installmentAmt = (String) v.getTag(R.id.installment_amt);
                        String installmentNumber = (String) v.getTag(R.id.installment_number);
//                String verifyStatus = (String) v.getTag(R.id.installment_verify_status);
//                String signStatus = (String) v.getTag(R.id.installment_sign_status);
                        if("sign".equals(operation)){
                            Intent intent = new Intent(mContext, InstallmentContractActivity.class);
                            intent.putExtra(UploadExtInfoActivity.KEY_ORDER_NO, orderNo);
                            mContext.startActivity(intent);
                        }else if("updateInfo".equals(operation)){
                            // 商家补充资料
                            Intent intent = new Intent(mContext, UploadExtInfoActivity.class);
                            intent.putExtra(UploadExtInfoActivity.KEY_ORDER_NO, orderNo);
                            mContext.startActivity(intent);
                        }else if("repayDetail".equals(operation)){
                            ((InstallmentOrderActivity)mContext).getInstallmentPresenter().queryRepayOfMonth(orderNo);
                            ((InstallmentOrderActivity)mContext).setInstallmentOfMonth(installmentNumber);
                            ((InstallmentOrderActivity)mContext).setRepayAmt(installmentAmt);
                        }
                        break;
                    }
                }
            }
        });

        return v;
    }

    public void updateData(JSONArray jsonArray, boolean append) throws JSONException {
        if (!append) {
            data.clear();
        }
        for (int i = 0, j = jsonArray.length(); i < j; i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            Iterator<String> itr = jsonObj.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                map.put(key, jsonObj.getString(key));
            }
            data.add(map);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
    }

    public boolean isShowStatus() {
        return isShowStatus;
    }

    public void setShowStatus(boolean showStatus) {
        isShowStatus = showStatus;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

}
