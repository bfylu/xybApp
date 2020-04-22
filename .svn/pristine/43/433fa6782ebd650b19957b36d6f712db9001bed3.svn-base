package cn.payadd.majia.activity;

import android.view.View;
import android.widget.Button;

import com.fdsj.credittreasure.R;

/**
 * Created by df on 2017/11/20.
 */
//TODO
public class CaculatorActivity extends BaseActivity implements View.OnClickListener{


    String text;
    String[] numbers;
    @Override
    public int getLayoutId() {
        return R.layout.activity_receivables;
    }

    @Override
    public void initView() {
        findViewById(R.id.btn0).setOnClickListener(this);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_reduce).setOnClickListener(this);
        findViewById(R.id.btn_spot).setOnClickListener(this);


    }

    @Override
    public void initData() {

    }

    @Override
    protected void initPermission() {

    }

    private void addText(String symbol){
        //
        if(this.text.length() >= 20){
            return;
        }
        //判断



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn0:
                if( !text.contains("+") && !text.contains("-")){

                }
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn1:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn2:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn3:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn4:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn5:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn6:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn7:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn8:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn9:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn_spot:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn_delete:
                addText(((Button) v).getText().toString());
                break;
            case R.id.btn_add:
                if(text.contains("+")){
                    if(!text.endsWith("+")){

                    }
                }else{
                    addText(((Button) v).getText().toString());
                }

                break;
            default:
                break;
        }
    }
}
