package cn.payadd.majia.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.payadd.majia.pojo.BankPojo;

/**
 * Created by df on 2017/9/11.
 */

public class BankCardAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Map<String,Object>> mData;

    public static Map<String,BankPojo> bankInfoMap;

    private int selectPosition;


    private boolean isSetKick;

    {
        bankInfoMap = new HashMap<>();
        bankInfoMap.put("建设银行",new BankPojo(R.mipmap.bank_logo_js,R.drawable.shape_bankcard_blue));
        bankInfoMap.put("工商银行",new BankPojo(R.mipmap.bank_logo_gs,R.drawable.shape_bankcard_pink));
        bankInfoMap.put("招商银行",new BankPojo(R.mipmap.bank_logo_zs,R.drawable.shape_bankcard_pink));
        bankInfoMap.put("平安银行",new BankPojo(R.mipmap.bank_logo_pa,R.drawable.shape_bankcard_orange));
        bankInfoMap.put("光大银行",new BankPojo(R.mipmap.bank_logo_gd,R.drawable.shape_bankcard_yellow));
        bankInfoMap.put("农业银行",new BankPojo(R.mipmap.bank_logo_ny,R.drawable.shape_bankcard_green));
        bankInfoMap.put("兴业银行",new BankPojo(R.mipmap.bank_logo_xy,R.drawable.shape_bankcard_blue));
        bankInfoMap.put("广发银行",new BankPojo(R.mipmap.bank_logo_gf,R.drawable.shape_bankcard_red));
        bankInfoMap.put("邮政储蓄银行",new BankPojo(R.mipmap.bank_logo_yz,R.drawable.shape_bankcard_green));
        bankInfoMap.put("中国银行",new BankPojo(R.mipmap.bank_logo_zg,R.drawable.shape_bankcard_red));
        bankInfoMap.put("浦发银行",new BankPojo(R.mipmap.bank_logo_pf,R.drawable.shape_bankcard_blue));
        bankInfoMap.put("交通银行",new BankPojo(R.mipmap.bank_logo_jt,R.drawable.shape_bankcard_blue));
        bankInfoMap.put("民生银行",new BankPojo(R.mipmap.bank_logo_ms,R.drawable.shape_bankcard_green));
        bankInfoMap.put("华夏银行",new BankPojo(R.mipmap.bank_logo_hx,R.drawable.shape_bankcard_red));
        bankInfoMap.put("中信银行",new BankPojo(R.mipmap.bank_logo_zx,R.drawable.shape_bankcard_red));

    }

    public BankCardAdapter(Context context,List<Map<String,Object>> mData){
        super();
        this.mContext = context;

        if(mData!=null){
            this.mData = mData;
        }else{
            this.mData = new ArrayList<>();
        }

        this.mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_bankcard, parent, false);
        ImageView ivBankIcon = (ImageView) v.findViewById(R.id.iv_bank_icon);
        ImageView ivChoose = (ImageView) v.findViewById(R.id.iv_choose);
        TextView tvBankName = (TextView) v.findViewById(R.id.tv_bank_name);
        TextView tvCardType = (TextView) v.findViewById(R.id.tv_card_type);
        TextView tvCardNo = (TextView) v.findViewById(R.id.tv_card_no);
        Map<String,Object> item = mData.get(position);
        tvBankName.setText((String)item.get("bankName"));
        String cardTypeStr = (String)item.get("cardType");

        if(selectPosition == position && isSetKick){
            ivChoose.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(cardTypeStr)){
            switch (Integer.valueOf(cardTypeStr)){
                case 1:
                    tvCardType.setText("储蓄卡");
                    break;
                case 2:
                    tvCardType.setText("信用卡");
                    break;
            }
        }
        BankPojo bank = bankInfoMap.get(item.get("bankName"));
        if(bank == null){
            v.getRootView().setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_bankcard_red));
        }else{
            ivBankIcon.setImageResource(bank.getBankLogo());
            v.getRootView().setBackground(ContextCompat.getDrawable(mContext,bank.getCardBackground()));
        }


        tvCardNo.setText((String)item.get("bankCardNo"));
        return v;
    }

    public void updateData(JSONArray jsonArray, boolean append) throws JSONException {
        if (!append) {
            mData.clear();
        }
        for (int i = 0, j = jsonArray.length(); i < j; i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Map<String, Object> map = new HashMap<>();
            Iterator<String> itr = jsonObj.keys();
            while (itr.hasNext()) {
                String key = itr.next();
                map.put(key, jsonObj.getString(key));
            }
            mData.add(map);
        }
        notifyDataSetChanged();
    }
    public void updateData(List<Map<String,Object>> data, boolean append) throws JSONException {
        if (append) {
            mData.addAll(data);
        }else {
            mData.clear();
            mData.addAll(data);
        }

        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void selectPosition(int position){

        this.selectPosition =position;
    }
    public int getselectPosition(){
        return selectPosition;
    }

    public boolean isSetKick() {
        return isSetKick;
    }

    public void setSetKick(boolean setKick) {
        isSetKick = setKick;
    }
}
