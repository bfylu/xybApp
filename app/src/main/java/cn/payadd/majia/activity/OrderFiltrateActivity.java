package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.activities.Flow2Activity;
import com.fdsj.credittreasure.entity.SerializableMap;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.PaymentDetailPresenter;
import cn.payadd.majia.util.DateUtils;
import cn.payadd.majia.util.StringUtil;

import static com.fdsj.credittreasure.R.id.et_choose;
import static com.fdsj.credittreasure.constant.Constants.ORDER_FILTRATE_ACTIVITY;

/**
 * Created by df on 2017/7/19.
 */

public class OrderFiltrateActivity extends BaseActivity implements View.OnClickListener,IActivity{

    private EditText etChoose;

    private Button btnChooseCode,btnReset,btnChoose;

    @BindView(R.id.image_sao)
    ImageView image_sao;

    private TextView payTypeAll,payTypeWeixin,payTypeAlipay,payTypeCard,payTypePrecredit;

    private TextView productAll,productMajia,productQrcode,productBox,productPos,productPcclient,productPreCredit;

    private TextView tradeTypePay;

    private TextView tradeTypeRefund;

    private TextView refundTypeAll,refundTypeReturn,refundTypeCash;

    private TextView refundStatusAll,refundStatusSucc,refundStatusProcess,refundStatusFail;

    private TextView dateYesterday,dateWeek,dateMonth;

    private EditText etStartTime,etEndTime;

    private LinearLayout ll_refund_area;

    private PaymentDetailPresenter detailPresenter;

    private Map<Integer,Integer> groupView = new HashMap<>();

    private Map<Integer,View> selectView = new HashMap<>();

    private Map<Integer,View> unselectView = new HashMap<>();

    private int startYear, startMonth, startDay, startHour, startMinute;
    private int endYear, endMonth, endDay, endHour, endMinute;
    private boolean isChooseStartDate, isChooseEndDate;

    SimpleDateFormat strFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
    @Override
    public int getLayoutId() {
        return R.layout.activity_order_filtrate;

    }

    @Override
    public void initView() {
        setTitleCenterText("筛选");
        setTitleBackButton();
        etChoose = (EditText) findViewById(et_choose);
        btnChooseCode = (Button) findViewById(R.id.btn_choose_code);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnChoose = (Button) findViewById(R.id.btn_choose);

        payTypeAll = (TextView) findViewById(R.id.pay_type_all);
        payTypeWeixin = (TextView) findViewById(R.id.pay_type_weixin);
        payTypeAlipay = (TextView) findViewById(R.id.pay_type_alipay);
        payTypeCard = (TextView) findViewById(R.id.pay_type_card);
        payTypePrecredit = (TextView) findViewById(R.id.pay_type_precredit);
        productAll = (TextView) findViewById(R.id.product_all);
        productMajia = (TextView) findViewById(R.id.product_majia);
        productBox = (TextView) findViewById(R.id.product_box);
        productQrcode = (TextView) findViewById(R.id.product_qrcode);
        productPcclient = (TextView) findViewById(R.id.product_pcclient);
        productPos = (TextView) findViewById(R.id.product_pos);
        productPreCredit = (TextView) findViewById(R.id.product_pre_credit);

        tradeTypePay = (TextView) findViewById(R.id.trade_type_pay);
//        tradeTypeRefund = (TextView) findViewById(R.id.trade_type_refund);

        refundTypeAll = (TextView) findViewById(R.id.refund_type_all);
        refundTypeCash = (TextView) findViewById(R.id.refund_type_cash);
        refundTypeReturn = (TextView) findViewById(R.id.refund_type_return);

        refundStatusAll = (TextView) findViewById(R.id.refund_status_all);
        refundStatusSucc = (TextView) findViewById(R.id.refund_status_succ);
        refundStatusProcess = (TextView) findViewById(R.id.refund_status_process);
        refundStatusFail = (TextView) findViewById(R.id.refund_status_fail);

        dateYesterday = (TextView) findViewById(R.id.date_yesterday);
        dateWeek = (TextView) findViewById(R.id.date_week);
        dateMonth = (TextView) findViewById(R.id.date_month);

        etStartTime = (EditText) findViewById(R.id.et_start_time);
        etEndTime = (EditText) findViewById(R.id.et_end_time);

        ll_refund_area = (LinearLayout) findViewById(R.id.ll_refund_area);


        payTypeAll.setOnClickListener(this);
        productAll.setOnClickListener(this);
        refundTypeAll.setOnClickListener(this);
        refundStatusAll.setOnClickListener(this);
        payTypeWeixin.setOnClickListener(this);
        payTypeAlipay.setOnClickListener(this);
        payTypeCard.setOnClickListener(this);
        payTypePrecredit.setOnClickListener(this);
        productMajia.setOnClickListener(this);
        productBox.setOnClickListener(this);
        productQrcode.setOnClickListener(this);
        productPcclient.setOnClickListener(this);
        productPos.setOnClickListener(this);
        productPreCredit.setOnClickListener(this);
        refundTypeCash.setOnClickListener(this);
        refundTypeReturn.setOnClickListener(this);
        refundStatusSucc.setOnClickListener(this);
        refundStatusProcess.setOnClickListener(this);
        refundStatusFail.setOnClickListener(this);
        tradeTypePay.setOnClickListener(this);
//        tradeTypeRefund.setOnClickListener(this);
        dateYesterday.setOnClickListener(this);
        dateWeek.setOnClickListener(this);
        dateMonth.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        image_sao.setOnClickListener(this);
        detailPresenter = new PaymentDetailPresenter(this);
        btnChooseCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderNo = etChoose.getText().toString().trim();
                if(TextUtils.isEmpty(orderNo)){
                    Toast.makeText(OrderFiltrateActivity.this,"请输入订单号",Toast.LENGTH_SHORT).show();
                    return;
                }
                detailPresenter.queryOrderDetail(orderNo);
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle =  new Bundle();
                String startTime = etStartTime.getText().toString();
                String endTime = etEndTime.getText().toString();

                if(!TextUtils.isEmpty(startTime)){
                    if(TextUtils.isEmpty(endTime)){
                        Toast.makeText(OrderFiltrateActivity.this,"请输入结束日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(!TextUtils.isEmpty(endTime)){
                    if(TextUtils.isEmpty(startTime)){
                        Toast.makeText(OrderFiltrateActivity.this,"请输入开始日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if((!TextUtils.isEmpty(startTime))&&(!TextUtils.isEmpty(endTime))){
                    int result = startTime.compareTo(endTime);
                    if(result > 0){
                        Toast.makeText(OrderFiltrateActivity.this,"结束日期不能小于开始日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //组装条件
                JSONObject json = new JSONObject();
                JSONArray payTypeList = new JSONArray();
                JSONArray bizTypeList = new JSONArray();
                try {
                   String etStartTimeStr =  etStartTime.getText().toString();
                    String etEndTimeStr =  etEndTime.getText().toString();
                    if(!TextUtils.isEmpty(etStartTimeStr)){
                        json.put("startTime",etStartTimeStr+":00");
                    }
                    if(!TextUtils.isEmpty(etEndTimeStr)){
                        json.put("endTime",etEndTimeStr+":59");
                    }


                    if(selectView.containsKey(R.id.trade_type_pay)){
                        json.put("orderType","pay");
                    }
                    if(selectView.containsKey(R.id.pay_type_alipay)){
                        payTypeList.put("01");
                    }
                    if(selectView.containsKey(R.id.pay_type_weixin)){
                        payTypeList.put("02");
                    }
                    if(selectView.containsKey(R.id.pay_type_card)){
                        payTypeList.put("03");
                    }
                    if(selectView.containsKey(R.id.pay_type_precredit)){
                        payTypeList.put("04");
                    }
                    if(selectView.containsKey(R.id.product_majia)){
                        bizTypeList.put("01");
                    }
                    if(selectView.containsKey(R.id.product_qrcode)){
                        bizTypeList.put("02");
                    }
                    if(selectView.containsKey(R.id.product_box)){
                        bizTypeList.put("03");
                    }
                    if(selectView.containsKey(R.id.product_pos)){
                        bizTypeList.put("04");
                    }
                    if(selectView.containsKey(R.id.product_pcclient)){
                        bizTypeList.put("05");
                        bizTypeList.put("06");
                    }
                    if(selectView.containsKey(R.id.product_pre_credit)){
                        bizTypeList.put("07");
                    }
                    json.put("payTypeList",payTypeList);
                    json.put("bizTypeList",bizTypeList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String condition = json.toString();
                bundle.putString("condition", condition);
                intent.putExtras(bundle);
                setResult(Flow2Activity.REQUEST, intent);
                finish();
            }
        });

        groupView.put(payTypeAll.getId(),0);
        groupView.put(payTypeWeixin.getId(),payTypeAll.getId());
        groupView.put(payTypeAlipay.getId(),payTypeAll.getId());
        groupView.put(payTypeCard.getId(),payTypeAll.getId());
        groupView.put(payTypePrecredit.getId(),payTypeAll.getId());

        groupView.put(productAll.getId(),0);
        groupView.put(productMajia.getId(),productAll.getId());
        groupView.put(productBox.getId(),productAll.getId());
        groupView.put(productPcclient.getId(),productAll.getId());
        groupView.put(productPos.getId(),productAll.getId());
        groupView.put(productQrcode.getId(),productAll.getId());
        groupView.put(productPreCredit.getId(),productAll.getId());

        groupView.put(refundTypeAll.getId(),0);
        groupView.put(refundTypeReturn.getId(),refundTypeAll.getId());
        groupView.put(refundTypeCash.getId(),refundTypeAll.getId());

        groupView.put(refundStatusAll.getId(),0);
        groupView.put(refundStatusSucc.getId(),refundStatusAll.getId());
        groupView.put(refundStatusFail.getId(),refundStatusAll.getId());
        groupView.put(refundStatusProcess.getId(),refundStatusAll.getId());

    }

    @Override
    public void initData() {


        Bundle bundle = getIntent().getExtras();
        String condition = bundle.getString("condition");
        JSONObject json = null;
        if (TextUtils.isEmpty(condition)){
            initSelect();
        }else {
            try {
                json = new JSONObject(condition);
                if (json != null) {
                    String startTimeStr = null;
                    String endTimeStr = null;
                    String orderTypeStr = null;
                    JSONArray bizTypeList = null;
                    JSONArray payTypeList = null;
                    if(!json.isNull("startTime")){
                        startTimeStr = json.getString("startTime");
                    }
                    if(!json.isNull("endTime")){
                        endTimeStr = json.getString("endTime");
                    }
                    if(!json.isNull("orderTypeStr")){
                        orderTypeStr = json.getString("orderType");
                    }
                    if(!json.isNull("bizTypeList")){
                        bizTypeList = json.getJSONArray("bizTypeList");
                    }
                    if(!json.isNull("payTypeList")){
                        payTypeList = json.getJSONArray("payTypeList");
                    }
                    if (!TextUtils.isEmpty(startTimeStr)) {
                        Date startTime = dateFormat.parse(startTimeStr);
                        etStartTime.setText(strFormat.format(startTime));
                    }
                    if (!TextUtils.isEmpty(endTimeStr)) {
                        Date endTime = dateFormat.parse(endTimeStr);
                        etEndTime.setText(strFormat.format(endTime));
                    }
                    if(!TextUtils.isEmpty(orderTypeStr)){
                        if("pay".equals(orderTypeStr)){
                            selectView.put(tradeTypePay.getId(),tradeTypePay);
                        }else if("refund".equals(orderTypeStr)){
                            //
                        }
                    }else{
                        selectView.put(tradeTypePay.getId(),tradeTypePay);
                    }
                    if(bizTypeList!=null){
                        boolean selectAll = true;
                       for(int i=0,size=bizTypeList.length();i<size;i++){
                           String bizType = (String) bizTypeList.get(i);
                           switch (bizType){
                               case "01":
                                   selectView.put(productMajia.getId(),productMajia);
                                   selectAll = false;
                                   break;
                               case "02":
                                   selectView.put(productQrcode.getId(),productQrcode);
                                   selectAll = false;
                                   break;
                               case "03":
                                   selectView.put(productBox.getId(),productBox);
                                   selectAll = false;
                                   break;
                               case "04":
                                   selectView.put(productPos.getId(),productPos);
                                   selectAll = false;
                                   break;
                               case "05":
                                   selectView.put(productPcclient.getId(),productPcclient);
                                   selectAll = false;
                                   break;
                               case "06":
                                   selectView.put(productPcclient.getId(),productPcclient);
                                   selectAll = false;
                                   break;
                               case "07":
                                   selectView.put(productPreCredit.getId(),productPreCredit);
                                   selectAll = false;
                                   break;
                           }

                       }
                        if(selectAll){
                            selectView.put(productAll.getId(),productAll);
                        }
                        if(!selectView.containsKey(productMajia.getId())){
                            unselectView.put(productMajia.getId(),productMajia);
                        }
                        if(!selectView.containsKey(productBox.getId())){
                            unselectView.put(productBox.getId(),productBox);
                        }
                        if(!selectView.containsKey(productPos.getId())){
                            unselectView.put(productPos.getId(),productPos);
                        }
                        if(!selectView.containsKey(productQrcode.getId())){
                            unselectView.put(productQrcode.getId(),productQrcode);
                        }
                        if(!selectView.containsKey(productPcclient.getId())){
                            unselectView.put(productPcclient.getId(),productPcclient);
                        }
                        if(!selectView.containsKey(productPreCredit.getId())){
                            unselectView.put(productPreCredit.getId(),productPreCredit);
                        }
                    }
                    if(payTypeList!=null){
                        boolean selectAll = true;
                        for(int i=0,size=payTypeList.length();i<size;i++){
                            String payType = (String) payTypeList.get(i);
                            switch (payType){
                                case "01":
                                    selectView.put(payTypeAlipay.getId(),payTypeAlipay);
                                    selectAll = false;
                                    break;
                                case "02":
                                    selectView.put(payTypeWeixin.getId(),payTypeWeixin);
                                    selectAll = false;
                                    break;
                                case "03":
                                    selectView.put(payTypeCard.getId(),payTypeCard);
                                    selectAll = false;
                                    break;
                                case "04":
                                    selectView.put(payTypePrecredit.getId(),payTypePrecredit);
                                    selectAll = false;
                                    break;

                            }

                        }
                        if(selectAll){
                            selectView.put(payTypeAll.getId(),payTypeAll);
                        }
                        if(!selectView.containsKey(payTypeCard.getId())){
                            unselectView.put(payTypeCard.getId(),payTypeCard);
                        }
                        if(!selectView.containsKey(payTypeWeixin.getId())){
                            unselectView.put(payTypeWeixin.getId(),payTypeWeixin);
                        }
                        if(!selectView.containsKey(payTypeAlipay.getId())){
                            unselectView.put(payTypeAlipay.getId(),payTypeAlipay);
                        }
                    }

                    unselectView.put(dateYesterday.getId(),dateYesterday);
                    unselectView.put(dateWeek.getId(),dateWeek);
                    unselectView.put(dateMonth.getId(),dateMonth);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        select();

    }

    @Override
    protected void initPermission() {

    }

    public void initSelectView(){
        selectView.clear();
        selectView.put(payTypeAll.getId(),payTypeAll);
        selectView.put(productAll.getId(),productAll);
        selectView.put(refundTypeAll.getId(),refundTypeAll);
        selectView.put(refundStatusAll.getId(),refundStatusAll);
        selectView.put(tradeTypePay.getId(),tradeTypePay);
    }

    public void initUnselectView(){
        unselectView.clear();
        unselectView.put(payTypeWeixin.getId(),payTypeWeixin);
        unselectView.put(payTypeAlipay.getId(),payTypeAlipay);
        unselectView.put(payTypeCard.getId(),payTypeCard);
        unselectView.put(payTypePrecredit.getId(),payTypePrecredit);
        unselectView.put(productMajia.getId(),productMajia);
        unselectView.put(productMajia.getId(),productMajia);
        unselectView.put(productBox.getId(),productBox);
        unselectView.put(productQrcode.getId(),productQrcode);
        unselectView.put(productPcclient.getId(),productPcclient);
        unselectView.put(productPos.getId(),productPos);
        unselectView.put(productPreCredit.getId(),productPreCredit);

        unselectView.put(refundTypeCash.getId(),refundTypeCash);
        unselectView.put(refundTypeReturn.getId(),refundTypeReturn);
        unselectView.put(refundStatusSucc.getId(),refundStatusSucc);
        unselectView.put(refundStatusProcess.getId(),refundStatusProcess);
        unselectView.put(refundStatusFail.getId(),refundStatusFail);
//        unselectView.put(tradeTypeRefund.getId(),tradeTypeRefund);
        unselectView.put(dateYesterday.getId(),dateYesterday);
        unselectView.put(dateWeek.getId(),dateWeek);
        unselectView.put(dateMonth.getId(),dateMonth);
    }

    private void initSelect(){
//        ll_refund_area.setVisibility(View.GONE);
        initSelectView();
        initUnselectView();
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog datePickerDialog;

        if (groupView.containsKey(v.getId())) {
            @IdRes int viewId = groupView.get(v.getId());
            if (0 == viewId) {
                //全部
                selectView(v.getId(),v);
                Set<Integer> keyset =  groupView.keySet();
                Iterator<Integer> iterator = keyset.iterator();
                while (iterator.hasNext()){
                    int key = iterator.next();
                    int value = groupView.get(key);
                    if(value == v.getId()){
                        View view = findViewById(key);

                        removeView(key,view);
                    }
                }
            } else {
                if(!v.isSelected()){
                    selectView(v.getId(),v);
                    removeView(viewId,findViewById(viewId));
                }else{
                    boolean selectedAll = true;
                    removeView(v.getId(),v);
                    Set<Integer> keyset =  groupView.keySet();
                    Iterator<Integer> iterator = keyset.iterator();
                    while (iterator.hasNext()){
                        int key = iterator.next();
                        int value = groupView.get(key);
                        if(value == viewId){
                            if (selectView.containsKey(key)){
                                selectedAll = false;
                            }
                        }
                    }
                    if(selectedAll){
                        View viewAll = findViewById(viewId);
                        selectView(viewId,viewAll);
                    }
                }
            }
        }else{
            switch (v.getId()){
                case R.id.trade_type_pay:

                    ll_refund_area.setVisibility(View.GONE);
                    selectView(v.getId(),tradeTypePay);
//                    removeView(tradeTypeRefund.getId(),tradeTypeRefund);
                    break;
//                case R.id.trade_type_refund:
//
//                    ll_refund_area.setVisibility(View.VISIBLE);
//                    selectView(v.getId(),tradeTypeRefund);
//                    removeView(tradeTypePay.getId(),tradeTypePay);
//                    break;
                case R.id.date_yesterday:
                    if(dateYesterday.isSelected()){
                        removeView(dateYesterday.getId(),dateYesterday);
                        clearDate();
                    }else{

                        selectView(v.getId(),dateYesterday);
                        removeView(dateWeek.getId(),dateWeek);
                        removeView(dateMonth.getId(),dateMonth);
                        chooseDate(v.getId());
                    }
                    break;
                case R.id.date_week:
                    if(dateWeek.isSelected()){
                        removeView(dateWeek.getId(),dateWeek);
                        clearDate();
                    }else{
                        selectView(v.getId(),dateWeek);
                        chooseDate(v.getId());
                        removeView(dateYesterday.getId(),dateYesterday);
                        removeView(dateMonth.getId(),dateMonth);
                    }

                    break;
                case R.id.date_month:
                    if(dateMonth.isSelected()){
                        removeView(dateMonth.getId(),dateMonth);
                        clearDate();
                    }else{
                        selectView(v.getId(),dateMonth);
                        removeView(dateYesterday.getId(),dateYesterday);
                        removeView(dateWeek.getId(),dateWeek);
                        chooseDate(v.getId());
                    }

                    break;
                case R.id.et_start_time: {
                    Calendar c = Calendar.getInstance();
                    final int year;
                    final int month;
                    final int day;
                    final int hourOfDay;
                    final int minute;
                    if (isChooseStartDate) {
                        year = startYear;
                        month = startMonth;
                        day = startDay;
                        hourOfDay = startHour;
                        minute = startMinute;
                    } else {
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);
                        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);
                    }

                    datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        boolean mFired = false;
                        @Override
                        public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                            if(mFired){
                                return;
                            }
                            new TimePickerDialog(OrderFiltrateActivity.this, new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String monthStr = StringUtil.toString(month + 1 < 10 ? StringUtil.append("0", (month + 1)) : month);
                                    String dayStr = StringUtil.toString(dayOfMonth < 10 ? StringUtil.append("0", dayOfMonth) : dayOfMonth);
                                    String yearStr = StringUtil.toString(year);
                                    String hourStr = StringUtil.toString(hourOfDay < 10 ? StringUtil.append("0", hourOfDay) : hourOfDay);
                                    String minuteStr = StringUtil.toString(minute < 10 ? StringUtil.append("0", minute) : minute);
                                    try {

                                        Date date = strFormat.parse(String.format("%s-%s-%s %s:%s", yearStr, monthStr, dayStr, hourStr, minuteStr));
                                        etStartTime.setText(strFormat.format(date));
                                        removeView(dateMonth.getId(),dateMonth);
                                        removeView(dateWeek.getId(),dateWeek);
                                        removeView(dateYesterday.getId(),dateYesterday);
                                        select();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    startYear = year;
                                    startMonth = month;
                                    startDay = dayOfMonth;
                                    startHour = hourOfDay;
                                    startMinute = minute;

                                    isChooseStartDate = true;
                                }
                            }, hourOfDay, minute, true).show();
                            mFired = true;
                        }
                    }, year, month, day);
                    if (isChooseEndDate) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(endYear, endMonth, endDay, endHour, endMinute);
                        datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
                    } else {
                        datePickerDialog.getDatePicker().setMaxDate((new Date()).getTime());
                    }
                    datePickerDialog.show();
                    break;
                }
                case R.id.et_end_time: {
                    final int year;
                    final int month;
                    final int day;
                    final int hourOfDay;
                    final int minute;
                    Calendar c = Calendar.getInstance();

                    if (isChooseEndDate) {
                        year = endYear;
                        month = endMonth;
                        day = endDay;
                        hourOfDay = endHour;
                        minute = endMinute;
                    } else {
                        year = c.get(Calendar.YEAR);
                        month = c.get(Calendar.MONTH);
                        day = c.get(Calendar.DAY_OF_MONTH);
                        hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);
                    }

                    datePickerDialog =  new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        boolean mFired = false;
                        @Override
                        public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                            if(mFired){
                                return;
                            }
                            new TimePickerDialog(OrderFiltrateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String monthStr = StringUtil.toString(month + 1 < 10 ? StringUtil.append("0", (month + 1)) : month);
                                    String dayStr = StringUtil.toString(dayOfMonth < 10 ? StringUtil.append("0", dayOfMonth) : dayOfMonth);
                                    String yearStr = StringUtil.toString(year);
                                    String hourStr = StringUtil.toString(hourOfDay < 10 ? StringUtil.append("0", hourOfDay) : hourOfDay);
                                    String minuteStr = StringUtil.toString(minute < 10 ? StringUtil.append("0", minute) : minute);
                                    try {
                                        Date date = strFormat.parse(String.format("%s-%s-%s %s:%s", yearStr, monthStr, dayStr, hourStr, minuteStr));
                                        etEndTime.setText(strFormat.format(date));
                                        removeView(dateMonth.getId(),dateMonth);
                                        removeView(dateWeek.getId(),dateWeek);
                                        removeView(dateYesterday.getId(),dateYesterday);
                                        select();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    endYear = year;
                                    endMonth = month;
                                    endDay = dayOfMonth;
                                    endHour = hourOfDay;
                                    endMinute = minute;

                                    isChooseEndDate = true;
                                }
                            }, hourOfDay, minute, true).show();
                            mFired = true;
                        }
                    }, year, month, day);
                    if (isChooseStartDate) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(startYear, startMonth, startDay, startHour, startMinute);
                        datePickerDialog.getDatePicker().setMinDate(calendar.getTime().getTime());
                    }
                    datePickerDialog.getDatePicker().setMaxDate((new Date()).getTime());
                    datePickerDialog.show();
                    break;
                }
                case R.id.btn_reset:
                    clearDate();
                    initSelect();
                    break;
                case R.id.image_sao:
                    Intent intent = new Intent(this, BarCodeActivity.class);
                    startActivityForResult(intent, ORDER_FILTRATE_ACTIVITY);
                    break;
            }
        }
        select();

    }
    public void select(){
        Set<Integer> selectKeyset =  selectView.keySet();
        Iterator<Integer> selectIterator = selectKeyset.iterator();
        while (selectIterator.hasNext()){
            int key = selectIterator.next();
            View value = selectView.get(key);
            value.setSelected(true);
        }
        Set<Integer> unSelectKeyset =  unselectView.keySet();
        Iterator<Integer> unselectIterator = unSelectKeyset.iterator();
        while (unselectIterator.hasNext()){
            int key = unselectIterator.next();
            View value = unselectView.get(key);
            value.setSelected(false);
        }
    }
    public void clearDate(){
        etStartTime.setText("");
        etEndTime.setText("");
    }
    public void chooseDate(@IdRes int dateId){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
        Date startTime = null;
        Date endTime = null;
        switch (dateId){
            case R.id.date_yesterday: {

                startTime = DateUtils.getYesterdayZeroTime();
                endTime = DateUtils.getYesterdayEndTime();
                break;
            }
            case R.id.date_week: {
                startTime = DateUtils.getDateStartTime(-7);
                endTime = DateUtils.getYesterdayEndTime();
            }
                break;
            case R.id.date_month:
                startTime = DateUtils.getLastMonthStartTime();
                endTime = DateUtils.getLastMonthEndTime();
                break;
        }
        etStartTime.setText(simpleDateFormat.format(startTime));
        etEndTime.setText(simpleDateFormat.format(endTime));
    }

//    public void selectTextView(TextView selectView,TableLayout table,boolean isAll){
//        boolean isSelected = selectView.isSelected();
//        if(isAll){
//            if (!isSelected) {
//                for (int i = 0, count = table.getChildCount(); i < count; i++) {
//                    TableRow tableRow = (TableRow) table.getChildAt(i);
//                    for (int j = 0, rowCount = tableRow.getChildCount(); j < rowCount; j++) {
//                        TextView textView = (TextView) tableRow.getChildAt(j);
//                        textView.setSelected(false);
//                    }
//                }
//                selectView.setSelected(true);
//            }
//        }else {
//
//        }
//
//    }
    public void selectView(@IdRes int viewId,View v){
        selectView.put(v.getId(), v);
        unselectView.remove(v.getId());
    }
    public void removeView(@IdRes int viewId,View v){
        unselectView.put(v.getId(), v);
        selectView.remove(v.getId());
    }

    @Override
    public void updateModel(String key, Object data) {
        Map<String,String> respData = (Map<String, String>) data;
        switch (key){
            case "orderQuery":
                SerializableMap serializableMap = new SerializableMap();
                serializableMap.setMap((Map<String, String>) data);
                Intent intent = new Intent();
                intent.setClass(OrderFiltrateActivity.this, PaymentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("serializableMap", serializableMap);
                bundle.putString("orderNo", etChoose.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case "orderQueryFail":
                Toast.makeText(this, respData.get("respDesc"), Toast.LENGTH_SHORT).show();
                break;
            case "exception":
                Toast.makeText(this, respData.get("respDesc"), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ORDER_FILTRATE_ACTIVITY && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                etChoose.setText(bundle.getString(CodeUtils.RESULT_STRING));
            }
        }
    }
}
