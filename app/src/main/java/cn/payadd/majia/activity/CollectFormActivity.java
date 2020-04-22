package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cn.payadd.majia.config.EnvironmentConfig;
import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.device.pos.IPosHelper;
import cn.payadd.majia.device.pos.PosDeviceUtil;
import cn.payadd.majia.device.pos.PrintConstant;
import cn.payadd.majia.device.pos.PrintFormat;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.CollectFormPresenter;
import cn.payadd.majia.util.DateUtils;
import cn.payadd.majia.util.StringUtil;

/**
 * Created by df on 2017/8/7.
 */

public class CollectFormActivity extends BaseActivity implements IActivity, View.OnClickListener {

    private TextView productAll, productMajia, productQrcode, productBox, productPos, productPcclient,productPreCredit;

    private TextView dateToday, dateYesterday, dateMonth;

    private EditText etStartTime, etEndTime;

    private Map<Integer, Integer> groupView = new HashMap<>();

    private Map<Integer, View> selectView = new HashMap<>();

    private Map<Integer, View> unselectView = new HashMap<>();

    private Button btnPreview;

    private CollectFormPresenter presenter;

    SimpleDateFormat strFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private JSONObject mCondition;

    private ArrayList<String> productNames;
//
//    private NumberFormat currency = NumberFormat.getCurrencyInstance("CNY");

    @Override
    public void updateModel(String key, Object data) {

        switch (key) {
            case CollectFormPresenter.SUCCESS:
                Map<String, String> map = (Map<String, String>) data;
                showDialog(map);
                break;
            case CollectFormPresenter.ERROR:
                break;
            case CollectFormPresenter.FAIL:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect_form;
    }

    @Override
    public void initView() {
        setTitleCenterText("打印汇总单");
        setTitleBackButton();

        presenter = new CollectFormPresenter(this);
        productAll = (TextView) findViewById(R.id.product_all);
        productMajia = (TextView) findViewById(R.id.product_majia);
        productBox = (TextView) findViewById(R.id.product_box);
        productQrcode = (TextView) findViewById(R.id.product_qrcode);
        productPcclient = (TextView) findViewById(R.id.product_pcclient);
        productPos = (TextView) findViewById(R.id.product_pos);
        productPreCredit = (TextView) findViewById(R.id.product_pre_credit);

        dateYesterday = (TextView) findViewById(R.id.date_yesterday);
        dateToday = (TextView) findViewById(R.id.date_today);
        dateMonth = (TextView) findViewById(R.id.date_month);

        etStartTime = (EditText) findViewById(R.id.et_start_time);
        etEndTime = (EditText) findViewById(R.id.et_end_time);

        btnPreview = (Button) findViewById(R.id.btn_preview);
        productAll.setOnClickListener(this);
        productMajia.setOnClickListener(this);
        productBox.setOnClickListener(this);
        productQrcode.setOnClickListener(this);
        productPcclient.setOnClickListener(this);
        productPos.setOnClickListener(this);

        dateYesterday.setOnClickListener(this);
        dateToday.setOnClickListener(this);
        dateMonth.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productNames.clear();
                String startTime = etStartTime.getText().toString();
                String endTime = etEndTime.getText().toString();
                if (!TextUtils.isEmpty(startTime)) {
                    if (TextUtils.isEmpty(endTime)) {
                        Toast.makeText(CollectFormActivity.this, "请输入结束日期", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!TextUtils.isEmpty(endTime)) {
                    if (TextUtils.isEmpty(startTime)) {
                        Toast.makeText(CollectFormActivity.this, "请输入开始日期", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if ((!TextUtils.isEmpty(startTime)) && (!TextUtils.isEmpty(endTime))) {
                    int result = startTime.compareTo(endTime);
                    if (result > 0) {
                        Toast.makeText(CollectFormActivity.this, "结束日期不能小于开始日期", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                JSONObject params = new JSONObject();
                JSONArray bizTypes = new JSONArray();
                for (int viewId : selectView.keySet()) {
                    switch (viewId) {
                        case R.id.product_majia:
                            bizTypes.put("01");
                            break;
                        case R.id.product_qrcode:
                            bizTypes.put("02");
                            break;
                        case R.id.product_box:
                            bizTypes.put("03");
                            break;
                        case R.id.product_pos:
                            bizTypes.put("04");
                            break;
                        case R.id.product_pcclient:
                            bizTypes.put("05");
                            bizTypes.put("06");
                            break;
                        case R.id.product_pre_credit:
                            bizTypes.put("07");
                            break;
                    }
                }
                try {

                    if (!TextUtils.isEmpty(startTime)) {
                        params.put("startTime", startTime + ":00");
                    }
                    if (!TextUtils.isEmpty(endTime)) {
                        params.put("endTime", endTime + ":59");
                    }
                    params.put("bizTypeList", bizTypes);
                    mCondition = params;
                    presenter.query(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        groupView.put(productAll.getId(), 0);
        groupView.put(productMajia.getId(), productAll.getId());
        groupView.put(productBox.getId(), productAll.getId());
        groupView.put(productPcclient.getId(), productAll.getId());
        groupView.put(productPos.getId(), productAll.getId());
        groupView.put(productQrcode.getId(), productAll.getId());
        groupView.put(productPreCredit.getId(), productAll.getId());

    }

    @Override
    public void initData() {
        initSelect();
        select();
        chooseDate(R.id.date_today);
        if(EnvironmentConfig.isPos()){
            IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(),this);
            helper.init(this);
        }
        productNames = new ArrayList<>();
    }

    @Override
    protected void initPermission() {

    }

    private void initSelect() {
//        ll_refund_area.setVisibility(View.GONE);
        initSelectView();
        initUnselectView();
    }

    public void initSelectView() {
        selectView.clear();
        selectView.put(productAll.getId(), productAll);
        selectView.put(dateToday.getId(), dateToday);
    }

    public void initUnselectView() {
        unselectView.clear();
        unselectView.put(productMajia.getId(), productMajia);
        unselectView.put(productMajia.getId(), productMajia);
        unselectView.put(productBox.getId(), productBox);
        unselectView.put(productQrcode.getId(), productQrcode);
        unselectView.put(productPcclient.getId(), productPcclient);
        unselectView.put(productPos.getId(), productPos);
        unselectView.put(productPreCredit.getId(),productPreCredit);

        unselectView.put(dateYesterday.getId(), dateYesterday);
        unselectView.put(dateMonth.getId(), dateMonth);
    }

    public void select() {
        Set<Integer> selectKeyset = selectView.keySet();
        Iterator<Integer> selectIterator = selectKeyset.iterator();
        while (selectIterator.hasNext()) {
            int key = selectIterator.next();
            View value = selectView.get(key);
            value.setSelected(true);
        }
        Set<Integer> unSelectKeyset = unselectView.keySet();
        Iterator<Integer> unselectIterator = unSelectKeyset.iterator();
        while (unselectIterator.hasNext()) {
            int key = unselectIterator.next();
            View value = unselectView.get(key);
            value.setSelected(false);
        }
    }

    public void clearDate() {
        etStartTime.setText("");
        etEndTime.setText("");
    }

    public void selectView(@IdRes int viewId, View v) {
        selectView.put(v.getId(), v);
        unselectView.remove(v.getId());
    }

    public void removeView(@IdRes int viewId, View v) {
        unselectView.put(v.getId(), v);
        selectView.remove(v.getId());
    }

    public void chooseDate(@IdRes int dateId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
        Date startTime = null;
        Date endTime = null;
        switch (dateId) {
            case R.id.date_yesterday: {
                startTime = DateUtils.getYesterdayZeroTime();
                endTime = DateUtils.getYesterdayEndTime();
                break;
            }
            case R.id.date_today: {
                startTime = DateUtils.getTodayZeroTime();
                endTime = DateUtils.getTodayEndTime();
                break;
            }
            case R.id.date_month: {
                startTime = DateUtils.getLastMonthStartTime();
                endTime = DateUtils.getLastMonthEndTime();
                break;
            }
        }
        etStartTime.setText(simpleDateFormat.format(startTime));
        etEndTime.setText(simpleDateFormat.format(endTime));

    }

    @Override
    public void onClick(View v) {
        if (groupView.containsKey(v.getId())) {
            @IdRes int viewId = groupView.get(v.getId());
            if (0 == viewId) {
                //全部
                selectView(v.getId(), v);
                Set<Integer> keyset = groupView.keySet();
                Iterator<Integer> iterator = keyset.iterator();
                while (iterator.hasNext()) {
                    int key = iterator.next();
                    int value = groupView.get(key);
                    if (value == v.getId()) {
                        View view = findViewById(key);
                        removeView(key, view);
                    }
                }
            } else {
                if (!v.isSelected()) {
                    selectView(v.getId(), v);
                    removeView(viewId, findViewById(viewId));
                } else {
                    boolean selectedAll = true;
                    removeView(v.getId(), v);
                    Set<Integer> keyset = groupView.keySet();
                    Iterator<Integer> iterator = keyset.iterator();
                    while (iterator.hasNext()) {
                        int key = iterator.next();
                        int value = groupView.get(key);
                        if (value == viewId) {
                            if (selectView.containsKey(key)) {
                                selectedAll = false;
                            }
                        }
                    }
                    if (selectedAll) {
                        View viewAll = findViewById(viewId);
                        selectView(viewId, viewAll);
                    }
                }
            }
        } else {
            switch (v.getId()) {
                case R.id.date_yesterday:
                    if (dateYesterday.isSelected()) {

                        removeView(dateYesterday.getId(), dateYesterday);
                        clearDate();
                    } else {

                        selectView(v.getId(), dateYesterday);
                        removeView(dateToday.getId(), dateToday);
                        removeView(dateMonth.getId(), dateMonth);
                        chooseDate(v.getId());
                    }
                    break;
                case R.id.date_today:
                    if (dateToday.isSelected()) {
                        removeView(dateToday.getId(), dateToday);
                        clearDate();
                    } else {
                        selectView(v.getId(), dateToday);
                        chooseDate(v.getId());
                        removeView(dateYesterday.getId(), dateYesterday);
                        removeView(dateMonth.getId(), dateMonth);
                    }

                    break;
                case R.id.date_month:
                    if (dateMonth.isSelected()) {
                        removeView(dateMonth.getId(), dateMonth);
                        clearDate();
                    } else {
                        selectView(v.getId(), dateMonth);
                        removeView(dateYesterday.getId(), dateYesterday);
                        removeView(dateToday.getId(), dateToday);
                        chooseDate(v.getId());
                    }

                    break;
                case R.id.et_start_time: {
                    Calendar c = Calendar.getInstance();
                    final int year = c.get(Calendar.YEAR);
                    final int month = c.get(Calendar.MONTH);
                    final int day = c.get(Calendar.DAY_OF_MONTH);
                    final int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                    final int minute = c.get(Calendar.MINUTE);

                    new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        boolean mFired = false;

                        @Override
                        public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                            if (mFired) {
                                return;
                            }
                            new TimePickerDialog(CollectFormActivity.this, new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String monthStr = month + 1 < 10 ? "0" + (month + 1) : month + "";
                                    String dayStr = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                                    String yearStr = year + "";
                                    String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                                    String minuteStr = minute < 10 ? "0" + minute : minute + "";
                                    try {

                                        Date date = strFormat.parse(String.format("%s-%s-%s %s:%s", yearStr, monthStr, dayStr, hourStr, minuteStr));
                                        etStartTime.setText(strFormat.format(date));
                                        removeView(dateMonth.getId(), dateMonth);
                                        removeView(dateToday.getId(), dateToday);
                                        removeView(dateYesterday.getId(), dateYesterday);
                                        select();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, hourOfDay, minute, true).show();
                            mFired = true;
                        }
                    }, year, month, day).show();
                    break;
                }
                case R.id.et_end_time: {
                    Calendar c = Calendar.getInstance();
                    final int year = c.get(Calendar.YEAR);
                    final int month = c.get(Calendar.MONTH);
                    final int day = c.get(Calendar.DAY_OF_MONTH);
                    final int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                    final int minute = c.get(Calendar.MINUTE);

                    new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        boolean mFired = false;

                        @Override
                        public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {
                            if (mFired) {
                                return;
                            }
                            new TimePickerDialog(CollectFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String monthStr = month + 1 < 10 ? "0" + (month + 1) : month + "";
                                    String dayStr = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                                    String yearStr = year + "";
                                    String hourStr = hourOfDay < 10 ? "0" + hourOfDay : hourOfDay + "";
                                    String minuteStr = minute < 10 ? "0" + minute : minute + "";
                                    try {
                                        Date date = strFormat.parse(String.format("%s-%s-%s %s:%s", yearStr, monthStr, dayStr, hourStr, minuteStr));
                                        etEndTime.setText(strFormat.format(date));
                                        removeView(dateMonth.getId(), dateMonth);
                                        removeView(dateToday.getId(), dateToday);
                                        removeView(dateYesterday.getId(), dateYesterday);
                                        select();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, hourOfDay, minute, true).show();
                            mFired = true;
                        }
                    }, year, month, day).show();
                    break;
                }
            }
        }

        select();
    }

    @Override
    public void finish() {
        super.finish();
        if(EnvironmentConfig.isPos()){
            IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(),this);
            helper.close(this);
        }
    }

    private void showDialog(final Map<String, String> dataMap) {

        LayoutInflater inflater = this.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_collect_form, null);//获取自定义布局
        TextView tvMerName = (TextView) layout.findViewById(R.id.tv_mer_name);
        TextView tvTotalAmount = (TextView) layout.findViewById(R.id.tv_total_amount);
        TextView tvWeixinAmount = (TextView) layout.findViewById(R.id.tv_weixin_amount);
        TextView tvAlipayAmount = (TextView) layout.findViewById(R.id.tv_alipay_amount);
        TextView tvUnionpayAmount = (TextView) layout.findViewById(R.id.tv_unionpay_amount);
        TextView tvTradeCount = (TextView) layout.findViewById(R.id.tv_trade_count);

        TextView tvStartTime = (TextView) layout.findViewById(R.id.tv_start_time);
        TextView tvEndTime = (TextView) layout.findViewById(R.id.tv_end_time);
        TextView tvPrintTime = (TextView) layout.findViewById(R.id.tv_prin_time);
        final TableLayout products = (TableLayout) layout.findViewById(R.id.tl_product);
        try {
            setTable(products, mCondition.getJSONArray("bizTypeList"));
            tvMerName.setText(Utilities.getShowMerName(CollectFormActivity.this));
            tvTotalAmount.setText(StringUtil.append(SymbolConstant.RMB + dataMap.get("totalTransAmt")));
            tvWeixinAmount.setText(StringUtil.append(SymbolConstant.RMB + dataMap.get("weixinTransAmt")));
            tvAlipayAmount.setText(StringUtil.append(SymbolConstant.RMB + dataMap.get("alipayTransAmt")));
            tvUnionpayAmount.setText(StringUtil.append(SymbolConstant.RMB + dataMap.get("unionpayTransAmt")));

//            tvTotalAmount.setText(SymbolConstant.RMB + dataMap.get("totalTransAmt"));
//            tvWeixinAmount.setText(SymbolConstant.RMB + dataMap.get("weixinTransAmt"));
//            tvAlipayAmount.setText(SymbolConstant.RMB + dataMap.get("alipayTransAmt"));
//            tvUnionpayAmount.setText(SymbolConstant.RMB + dataMap.get("unionpayTransAmt"));
            tvTradeCount.setText(dataMap.get("payCount"));
            tvPrintTime.setText(dateFormat.format(new Date()));
            tvStartTime.setText(mCondition.getString("startTime"));
            tvEndTime.setText(mCondition.getString("endTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton("打印小票", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    IPosHelper helper = PosDeviceUtil.getPosHelper(EnvironmentConfig.getPosType(), CollectFormActivity.this);
                    if("shengpos".equals(EnvironmentConfig.getPosType())){
                        List<String> contentList = new ArrayList<>();
                        contentList.add(PrintFormat.transJson(3, "", 1).toString());
                        contentList.add(PrintFormat.transJson(3, "\t\t\t\t\t\t\t\t\t\t\t\t汇总单", 0, PrintConstant.Align.CENTER).toString());
                        contentList.add(PrintFormat.transJson(2, "", 2).toString());
                        contentList.add(PrintFormat.transJson(2, "商家：" + Utilities.getShowMerName(CollectFormActivity.this), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "交易总额： " + new BigDecimal(dataMap.get("totalTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "  微信支付： " + new BigDecimal(dataMap.get("weixinTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "  支付宝：  " + new BigDecimal(dataMap.get("alipayTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "  银联：   " + new BigDecimal(dataMap.get("unionpayTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "交易笔数： " + dataMap.get("payCount"), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());

                        StringBuilder sb = new StringBuilder();
                        for (String text : productNames) {
                            sb.append(text + " ");
                        }
                        contentList.add(PrintFormat.transJson(2, "收款产品："+ sb.toString(), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "日期范围：" + mCondition.getString("startTime"), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "       " + mCondition.getString("endTime"), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "打印日期：" + dateFormat.format(new Date()), 0).toString());
                        contentList.add(PrintFormat.transJson(3, " ", 2).toString());
                        helper.printReceipt(contentList, CollectFormActivity.this);
                    }else if("newland".equals(EnvironmentConfig.getPosType())){
                        List<String> contentList = new ArrayList<>();
                        contentList.add(PrintFormat.transJson(3, "", 1).toString());
                        contentList.add(PrintFormat.transJson(3, "汇总单", 0, PrintConstant.Align.CENTER).toString());
                        contentList.add(PrintFormat.transJson(2, "", 2).toString());
                        contentList.add(PrintFormat.transJson(2, "商家：" + Utilities.getShowMerName(CollectFormActivity.this), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "交易总额： " + new BigDecimal(dataMap.get("totalTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "  微信支付： " + new BigDecimal(dataMap.get("weixinTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "  支付宝：   " + new BigDecimal(dataMap.get("alipayTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "  银联：     " + new BigDecimal(dataMap.get("unionpayTransAmt")).setScale(2).toString() + "元", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "交易笔数： " + dataMap.get("payCount"), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());

                        StringBuilder sb = new StringBuilder();
                        for (String text : productNames) {
                            sb.append(text + " ");
                        }
                        contentList.add(PrintFormat.transJson(2, "收款产品："+ sb.toString(), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "日期范围：" + mCondition.getString("startTime"), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "          " + mCondition.getString("endTime"), 0).toString());
                        contentList.add(PrintFormat.transJson(2, "————————————————————", 0).toString());
                        contentList.add(PrintFormat.transJson(2, "打印日期：" + dateFormat.format(new Date()), 0).toString());
                        contentList.add(PrintFormat.transJson(3, " ", 2).toString());
                        helper.printReceipt(contentList, CollectFormActivity.this);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //设置button样式
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.btn_cancel));

    }

    public void setTable(TableLayout table, JSONArray jsonArray) {
        table.removeAllViews();
        try {
            if (jsonArray.length() <= 0) {
                addText(table, "信用商圈APP");
                addText(table, "固定收款码");
                addText(table, "派派小盒");
                addText(table, "POS机");
                addText(table, "收银机");
                return;
            }
            for (int i = 0, length = jsonArray.length(); i < length; i++) {
                String product = jsonArray.getString(i);
                switch (product) {
                    case "01":
                        addText(table, "码夹App");
                        break;
                    case "02":
                        addText(table, "固定收款码");
                        break;
                    case "03":
                        addText(table, "派派小盒");
                        break;
                    case "04":
                        addText(table, "POS机");
                        break;
                    case "05":
                        addText(table, "收银机");
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void addText(TableLayout table, String text) {
        productNames.add(text);
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.color_3));
        textView.setTextSize(15);
        textView.setPadding(5, 5, 5, 5);
        if (table.getChildCount() == 0) {
            TableRow newTableRow = new TableRow(this);
            newTableRow.setLayoutParams(new TableRow.LayoutParams(2));
            newTableRow.addView(textView);
            table.addView(newTableRow);
        } else {
            TableRow tableRow = (TableRow) table.getChildAt(table.getChildCount() - 1);
            if (tableRow.getChildCount() >= 2) {
                TableRow newTableRow = new TableRow(this);
                newTableRow.setLayoutParams(new TableRow.LayoutParams(2));
                newTableRow.addView(textView);
                table.addView(newTableRow);
            } else {
                tableRow.addView(textView);
            }
        }

    }

}
