package cn.payadd.majia.activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fdsj.credittreasure.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.payadd.majia.constant.ActivityTitle;

/**
 * Created by df on 2017/6/22.
 */

public class FiltrateActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    private EditText etOrderNo,etStartTime,etEndTime;

    private Button btnSubmit;
    private Spinner spOrderStatus;
    private List<String> list;
    @Override
    public int getLayoutId() {
        return R.layout.activity_filtrate;
    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText(ActivityTitle.FILTRATE);

        etOrderNo = (EditText) findViewById(R.id.et_order_no);
        etStartTime = (EditText) findViewById(R.id.et_start_time);
        etEndTime = (EditText) findViewById(R.id.et_end_time);

        btnSubmit = (Button) findViewById(R.id.btn_submit);

        spOrderStatus = (Spinner) findViewById(R.id.sp_order_status);


        etStartTime.setKeyListener(null);
        etEndTime.setKeyListener(null);

        etStartTime.setFocusable(false);
        etEndTime.setFocusable(false);

        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        spOrderStatus.setOnItemSelectedListener(this);

    }

    @Override
    public void initData() {
        list = new ArrayList<>();
        list.add("全部");
        list.add("待结算");
        list.add("已结算");
        list.add("处理中");
        list.add("已关闭");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,list);
        //此处为设置Item框的样式，可以自己定义Item的边框以及背景样式，然后通过这个方法加载进来
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrderStatus.setAdapter(adapter);

        Bundle bundle = getIntent().getBundleExtra("filtrate");
        if(bundle!=null){
            etOrderNo.setText(bundle.getString("orderNo"));
            etStartTime.setText(bundle.getString("startTime"));
            etEndTime.setText(bundle.getString("endTime"));
            String status = bundle.getString("status");
            int index;
            if (status == null){
                status = "";
            }
            switch (status){
                case "01":
                    index = 1;
                    break;
                case "02":
                    index = 2;
                    break;
                case "03":
                    index = 3;
                    break;
                case "04":
                    index = 4;
                    break;
                default:
                    index = 0;
                    break;
            }
            adapter.notifyDataSetChanged();
            spOrderStatus.setSelection(index);

        }
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String orderNo =etOrderNo.getText().toString().trim();
                String startTime = etStartTime.getText().toString().trim();
                String endTime = etEndTime.getText().toString().trim();
                if(!TextUtils.isEmpty(startTime)){
                    if(TextUtils.isEmpty(endTime)){
                        Toast.makeText(this,"请输入结束日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if(!TextUtils.isEmpty(endTime)){
                    if(TextUtils.isEmpty(startTime)){
                        Toast.makeText(this,"请输入开始日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if((!TextUtils.isEmpty(startTime))&&(!TextUtils.isEmpty(endTime))){
                    int result = startTime.compareTo(endTime);
                    if(result > 0){
                        Toast.makeText(this,"结束日期不能小于开始日期",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String status = (String) spOrderStatus.getSelectedItem();
                switch (status){
                    case "待结算":
                        status = "01";
                        break;
                    case "已结算":
                        status = "02";
                        break;
                    case "处理中":
                        status = "03";
                        break;
                    case "已关闭":
                        status = "04";
                        break;
                    default:
                        status = null;
                        break;
                }
                Intent intent = new Intent(FiltrateActivity.this,FundAuthOrderActivity.class);
                Bundle bundle = new Bundle();
                //传输的内容仍然是键值对的形式
                bundle.putString("orderNo", orderNo);
                bundle.putString("startTime", startTime);
                bundle.putString("endTime", endTime);
                bundle.putString("status", status);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.et_start_time: {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthStr = month + 1 < 10?"0"+(month+1):month+"";
                        String dayStr = dayOfMonth < 10?"0"+dayOfMonth:dayOfMonth+"";
                        String yearStr = year+"";
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date = simpleDateFormat.parse(String.format("%s-%s-%s", yearStr,monthStr, dayStr));
                            etStartTime.setText(simpleDateFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day).show();
                break;
            }
            case R.id.et_end_time: {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthStr = month + 1 < 10?"0"+(month+1):month+"";
                        String dayStr = dayOfMonth < 10?"0"+dayOfMonth:dayOfMonth+"";
                        String yearStr = year+"";
                        try {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
                            Date date = simpleDateFormat.parse(String.format("%s-%s-%s", yearStr,monthStr, dayStr));
                            etEndTime.setText(simpleDateFormat.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day).show();
                break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("filtrate",position+"");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
