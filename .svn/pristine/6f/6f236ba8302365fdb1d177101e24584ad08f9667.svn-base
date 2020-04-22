package com.fdsj.credittreasure.activities;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.fdsj.credittreasure.adapter.PrintTypeAdapter;
import com.fdsj.credittreasure.constant.Constants;
import com.utils.SharedPreferenceTool;

import butterknife.BindView;

/**
 * 小票打印选择页面
 * Created by lang on 2018/5/3.
 */

public class PrintTypeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_title)
    TextView tvTitle;

    @BindView(R.id.title_back)
    ImageView iv_back;

    @BindView(R.id.lv_print_type)
    ListView lvPrintType;

    private String[] printTypes;
    //选择的position和旧的position
    private int choosePosition, oldPosition;
    private PrintTypeAdapter adapter;
    private Intent intent;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_print_type;
    }

    @Override
    protected void initView() {
        printTypes = new String[] {baseApplication.getResources().getString(R.string.single_copy)
                , baseApplication.getResources().getString(R.string.double_copy)
                , baseApplication.getResources().getString(R.string.three_copy)};

        tvTitle.setText(getResources().getString(R.string.print_receipts));
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        initListView();
    }

    @Override
    protected void initData() {

    }

    /**
     * 设置listView
     */
    private void initListView() {
        choosePosition = SharedPreferenceTool.getInstance().getSaveShareInt(this
                , Constants.USER_INFO
                , Constants.PRINT_TYPE_CHOOSE
                , 1);

        oldPosition = choosePosition;

        adapter = new PrintTypeAdapter(this, printTypes, choosePosition);
        lvPrintType.setAdapter(adapter);

        lvPrintType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (choosePosition == position) {
                    return;
                }
                choosePosition = position;
                adapter.selectItem(choosePosition);
                SharedPreferenceTool.getInstance().SaveShare(PrintTypeActivity.this
                        , Constants.USER_INFO
                        , Constants.PRINT_TYPE_CHOOSE
                        , choosePosition);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                if (oldPosition != choosePosition) {
                    intent = new Intent();
                    intent.putExtra(Constants.PRINT_TYPE_CHOOSE, printTypes[choosePosition]);
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (oldPosition != choosePosition) {
                intent = new Intent();
                intent.putExtra(Constants.PRINT_TYPE_CHOOSE, printTypes[choosePosition]);
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED, intent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
