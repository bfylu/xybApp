package cn.payadd.majia.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;
import com.fdsj.credittreasure.R;
import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.adapter.BankArrayAdapter;
import cn.payadd.majia.constant.CardType;
import cn.payadd.majia.constant.SettleType;
import cn.payadd.majia.event.OnClickEvent;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.presenter.BankCardPresenter;
import cn.payadd.majia.view.CommonEditText;

/**
 * Created by df on 2017/9/5.
 */

public class BindCardActivity extends BaseActivity implements IActivity,View.OnClickListener{

    private List<Map<String, String>> mBanks = null;
    private CommonEditText cetCardNo, cetMobile;
    private Spinner spSettleType;
    private Spinner spBankName;
    private Button btnSubmit;
    private RelativeLayout rlCardArea;
    private TextView tvAddress;
    private Spinner spCardType;

    private static JSONObject jsonProvinces = null;
    private static JSONObject jsonCity = null;
    private static JSONObject jsonRegion = null;

    private List<Map<String, String>> mProvinces;
    private List<Map<String, String>> mCities;
    private List<Map<String, String>> mRegions;

    private String mSelectBank;
    private String mSelectedProvince;
    private String mSelectedCity;
    private String mSelectedRegion;
    private String mSelectBankId;

    private BankCardPresenter presenter;
    private  CityPickerView cityPicker;
    private ProgressDialog progressDialog;

    private String type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_card;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText("添加银行卡");

        type = getIntent().getStringExtra("type");

        spSettleType = (Spinner) findViewById(R.id.sp_settle_type);
        cetCardNo = (CommonEditText) findViewById(R.id.cet_card_no);
        cetCardNo.setRightImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cetCardNo.getCetEditText().setText("");
            }
        });
        spBankName = (Spinner) findViewById(R.id.sp_bank_name);
        spCardType = (Spinner) findViewById(R.id.sp_card_type);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        rlCardArea = (RelativeLayout) findViewById(R.id.rl_card_area);
        cetMobile = (CommonEditText) findViewById(R.id.cet_mobile);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        btnSubmit.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        cityPicker = new CityPickerView.Builder(BindCardActivity.this)
                .textSize(15)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .titleTextColor("#696969")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        rlCardArea.setOnClickListener(new OnClickEvent(1000) {
            @Override
            public void singleClick(View v) {
                cityPicker.show();

                //监听方法，获取选择结果
                cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        //返回结果
                        //ProvinceBean 省份信息
                        //CityBean     城市信息
                        //DistrictBean 区县信息
                        StringBuilder sb = new StringBuilder();
                        sb.append(province.getName());
                        sb.append("-");
                        sb.append(city.getName());
                        sb.append("-");
                        sb.append(district.getName());
                        String provinceId = province.getId();
                        String cityId = city.getId();
                        String districtId = district.getId();

                        mSelectedProvince = provinceId.substring(0,1);
                        mSelectedCity = cityId.substring(0,3);
                        mSelectedRegion =districtId;
                        tvAddress.setText(sb.toString());
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    @Override
    public void initData() {
        presenter = new BankCardPresenter(this);
        if (mBanks == null) {
            mBanks = parseBankCard("banks.plist");
        }
        if (jsonProvinces == null) {
            jsonProvinces = parseAddress("province.txt");
        }
        if (jsonCity == null) {
            jsonCity = parseAddress("city.txt");
        }
        if (jsonRegion == null) {
            jsonRegion = parseAddress("region.txt");
        }

        BankArrayAdapter adapter = new BankArrayAdapter(this, android.R.layout
                .simple_list_item_1, android.R.id.text1, mBanks);
        spBankName.setAdapter(adapter);
        spBankName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectBank = mBanks.get(position).get("bankName");
                mSelectBankId = mBanks.get(position).get("bankcode");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (jsonProvinces != null) {
            mProvinces = jsonToList(jsonProvinces);
        }
//        AddressAdapter provinceAdapter = new AddressAdapter(this, android.R.layout
//                .simple_spinner_item, android.R.id.text1, mProvinces);
//        spProvince.setAdapter(provinceAdapter);
//        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long
//                    id) {
//                try {
//                    mSelectedProvince = mProvinces.get(position).get("addressCode");
//                    JSONObject json = jsonCity.getJSONObject(mSelectedProvince);
//                    mCities = jsonToList(json);
//                    AddressAdapter cityAdapter = new AddressAdapter(BindCardActivity.this,
//                            android.R.layout
//                                    .simple_spinner_item, android.R.id.text1, mCities);
//                    spCity.setAdapter(cityAdapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long
//                    id) {
//                try {
//                    mSelectedCity = mCities.get(position).get("addressCode");
//                    JSONObject json = jsonRegion.getJSONObject(mSelectedCity);
//                    mRegions = jsonToList(json);
//                    AddressAdapter regionAdapter = new AddressAdapter(BindCardActivity.this,
//                            android.R.layout
//                                    .simple_spinner_item, android.R.id.text1, mRegions);
//                    spRegion.setAdapter(regionAdapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long
//                    id) {
//                mSelectedRegion = mRegions.get(position).get("addressCode");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


    }

    private JSONObject parseAddress(String fileName) {
        JSONObject jsonObject;
        try {
            InputStream is = getApplication().getAssets().open(fileName);
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
            String str = out.toString();
            if (str != null) {
                jsonObject = new JSONObject(str);
                return jsonObject;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Map<String, String>> parseBankCard(String fileName) {
        List<Map<String, String>> banks = new ArrayList<>();
        try {
            NSArray ary = (NSArray) PropertyListParser.parse(getAssets().open(fileName));
            for (int i = 0; i < ary.count(); i++) {
                Map<String, String> bank = new HashMap<>();
                String bankCode = (((NSDictionary) ary.objectAtIndex(i)).objectForKey("bankcode")
                        .toJavaObject().toString());
                String bankName = (((NSDictionary) ary.objectAtIndex(i)).objectForKey("bankname")
                        .toJavaObject().toString());
                String bankEng = (((NSDictionary) ary.objectAtIndex(i)).objectForKey("bankeng")
                        .toJavaObject().toString());
                String bankImg = (((NSDictionary) ary.objectAtIndex(i)).objectForKey("img")
                        .toJavaObject().toString());
                bank.put("bankCode", bankCode);
                bank.put("bankName", bankName);
                bank.put("bankEng", bankEng);
                bank.put("bankImg", bankImg);
                banks.add(bank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return banks;
    }

    @Override
    protected void initPermission() {

    }

    /**
     * 注意：该方法非通用,只针对绑卡省市区单独处理
     *
     * @param jsonObject
     * @return
     */
    public static List<Map<String, String>> jsonToList(JSONObject jsonObject) {
        List<Map<String, String>> list = new ArrayList<>();
        try {

            Iterator<String> it = jsonObject.keys();
            while (it.hasNext()) {
                Map<String, String> map = new HashMap<>();
                String key = it.next();
                String value = (String) jsonObject.get(key);
                map.put("addressCode", key);
                map.put("addressName", value);
                list.add(map);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void updateModel(String key, Object data) {
        if(key.equals("bindCard")){
            Map<String,String> respData = (Map<String, String>) data;
            if(respData.get("respCode").equals("000000")){
                Intent intent = new Intent();
                setResult(MyBankCardActivity.REQUEST, intent);
                finish();

            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                boolean flag = true;
                String settleType = null;
                final int position = spSettleType.getSelectedItemPosition();
                switch (position) {
                    case 0:
                        settleType = SettleType.INDIVIDUAL;
                        break;
                    case 1:
                        settleType = SettleType.OPEN;
                        break;
                    default:
                        flag = false;
                        break;
                }
                String cardType = null;
                final int cardTypeIndex = spCardType.getSelectedItemPosition();
                switch (cardTypeIndex) {
                    case 0:
                        cardType = CardType.DEBIT;
                        break;
                    case 1:
                        settleType = CardType.CREDIT;
                        break;
                    default:
                        flag = false;
                        break;
                }
                String cardNo = cetCardNo.getValue();
                String mobile = cetMobile.getValue();
                if (TextUtils.isEmpty(cardNo) || TextUtils.isEmpty(mSelectBank)|| TextUtils.isEmpty(mobile)) {
                    flag = false;
                }
                if(cardNo.length()>19 || cardNo.length()<16){
                    Toast.makeText(this,"银行卡格式错误",Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (flag == true) {
                    presenter.bindCard(settleType, cardNo, mobile, mSelectBank,
                            mSelectedProvince, mSelectedCity, mSelectedRegion,mSelectBankId,cardType,new ICallback() {
                                @Override
                                public void exec(Object params) {
                                    Map<String,String> respData = (Map<String, String>) params;
                                    Toast.makeText(BindCardActivity.this,respData.get("respDesc"),Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;

        }

    }
}
