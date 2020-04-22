package cn.payadd.majia.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.payadd.majia.adapter.BankCardAdapter;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.pojo.BankPojo;
import cn.payadd.majia.presenter.AuthPresenter;
import cn.payadd.majia.presenter.BankCardPresenter;
import cn.payadd.majia.view.CommonDialog;
import cn.payadd.majia.view.CommonEditText;
import cn.payadd.majia.view.DrawableTextView;

/**
 * Created by df on 2017/9/4.
 */

public class MyBankCardActivity extends BaseActivity implements View.OnClickListener, IActivity {

    private DrawableTextView dtvBindCard;

    private ListView lvBankCard;
    private Button btnAddCard;

    private boolean mHasBankCard;

    private List<Map<String, Object>> mBankCardList;

    private BankCardPresenter presenter;
    private AuthPresenter authPresenter;

    private BankCardAdapter bankCardAdapter;

    public static Handler handler,authHandler;

    private View footView;
    private View marginView;

    private TextView tvInfo;

    private String type;

    private static int SELECT_POSITION;

    private CommonDialog commonDialog;

    public final static int REQUEST = 2;


    public MyBankCardActivity() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_card;

    }

    @Override
    public void initView() {
        setTitleBackButton();
        setTitleCenterText("我的银行卡");
        marginView = findViewById(R.id.margin_view);
        lvBankCard = (ListView) findViewById(R.id.lv_bankcard);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        footView = getLayoutInflater().inflate(R.layout.item_add_bank_card, null);
        btnAddCard = (Button) footView.findViewById(R.id.btn_add_bankcard);
        btnAddCard.setOnClickListener(this);
        bankCardAdapter = new BankCardAdapter(this, null);
        bankCardAdapter.selectPosition(SELECT_POSITION);
        lvBankCard.setAdapter(bankCardAdapter);
        lvBankCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Map<String, Object> bankCard = mBankCardList.get(position);
                    if ("check".equals(type)) {
                        //跳转至银行卡详情页
                        Intent intent = new Intent(MyBankCardActivity.this,
                                BankCardDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("bankName", (String) bankCard.get("bankName"));
                        bundle.putString("cardNo", (String) bankCard.get("bankCardNo"));
                        bundle.putString("id", (String) bankCard.get("id"));
                        BankPojo bankPojo = BankCardAdapter.bankInfoMap.get(bankCard.get
                                ("bankName"));
                        if (bankPojo != null) {
                            bundle.putInt("background", bankPojo.getCardBackground());
                            bundle.putInt("bankLogo", BankCardAdapter.bankInfoMap.get(bankCard
                                    .get("bankName")).getBankLogo());
                        } else {
                            bundle.putInt("background", R.drawable.shape_bankcard_red);
                        }
                        intent.putExtra("bankCard", bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        //返回绑定结果
                        Intent intent = new Intent();
                        SELECT_POSITION = position;
                        intent.putExtra("bankcard", (Serializable) bankCard);
                        setResult(WithdrawActivity.REQUEST, intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        lvBankCard.addFooterView(footView);
        dtvBindCard = (DrawableTextView) findViewById(R.id.dtv_bind_card);
        dtvBindCard.setOnClickListener(this);
    }

    public void setHasBankCard(boolean flag) {
        this.mHasBankCard = flag;
    }

    private boolean hasBankCard() {
        return mHasBankCard;
    }

    @Override
    public void initData() {
        presenter = new BankCardPresenter(this);
        authPresenter = new AuthPresenter(this);
        presenter.queryBankCard();
        type = getIntent().getStringExtra("type");
        if ("check".equals(type)) {
            marginView.setVisibility(View.VISIBLE);
            bankCardAdapter.setSetKick(false);
            tvInfo.setVisibility(View.GONE);
        } else {
            marginView.setVisibility(View.GONE);
            tvInfo.setVisibility(View.VISIBLE);
            bankCardAdapter.setSetKick(true);
        }

    }

    @Override
    protected void initPermission() {

    }

    public void auth(){
        //实名认证
        commonDialog = new CommonDialog(this, R.layout.dialog_auth, "实名认证", "立即认证",
                "取消");
        View dialogView = commonDialog.getDialogView();
        final CommonEditText etRealName = (CommonEditText) dialogView.findViewById(R
                .id.et_real_name);
        final CommonEditText etIdCard = (CommonEditText) dialogView.findViewById(R.id
                .et_idCard);
        //内容改变监听
        etRealName.getCetEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etRealName.getCetEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    etRealName.getCetRightImage().setVisibility(View.VISIBLE);
                } else {
                    etRealName.getCetRightImage().setVisibility(View.GONE);
                }
            }
        });
        authHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 1) {
                    commonDialog.getPositiveBtn().setText("立即认证");
                    commonDialog.getPositiveBtn().setEnabled(true);
                } else if (msg.what == 0) {

                    Intent intent = new Intent(MyBankCardActivity.this, BindCardActivity.class);
                    intent.putExtra("type", type);
                    startActivityForResult(intent, REQUEST);
                    commonDialog.dismiss();
                }
            }

            ;
        };
//                    etRealName..setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(etRealName.isFocused()){
//                                etIdCard.getCetRightImage().setVisibility(View.GONE);
//                                String text = etRealName.getCetEditText().getText().toString();
//                                if(TextUtils.isEmpty(text)){
//                                    etRealName.getCetRightImage().setVisibility(View.VISIBLE);
//                                }else{
//                                    etRealName.getCetRightImage().setVisibility(View.GONE);
//                                }
//                            }else{
//                                etRealName.getCetRightImage().setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                    //焦点事件监听
        etRealName.getCetEditText().setOnFocusChangeListener(new View
                .OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String text = etRealName.getCetEditText().getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        etRealName.getCetRightImage().setVisibility(View.VISIBLE);
                    } else {
                        etRealName.getCetRightImage().setVisibility(View.GONE);
                    }
                } else {
                    etRealName.getCetRightImage().setVisibility(View.GONE);
                }
            }
        });
        //内容改变监听
        etIdCard.getCetEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = etIdCard.getCetEditText().getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    etIdCard.getCetRightImage().setVisibility(View.VISIBLE);
                } else {
                    etIdCard.getCetRightImage().setVisibility(View.GONE);
                }
            }
        });

        //焦点事件监听
        etIdCard.getCetEditText().setOnFocusChangeListener(new View
                .OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String text = etIdCard.getCetEditText().getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        etIdCard.getCetRightImage().setVisibility(View.VISIBLE);
                    } else {
                        etIdCard.getCetRightImage().setVisibility(View.GONE);
                    }
                } else {
                    etIdCard.getCetRightImage().setVisibility(View.GONE);
                }
            }
        });
        //清除按钮点击事件
        etIdCard.setRightImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etIdCard.getCetEditText().setText("");
            }
        });
        //清除按钮点击事件
        etRealName.setRightImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etRealName.getCetEditText().setText("");
            }
        });

        commonDialog.setOnDiaLogListener(new CommonDialog.OnDialogListener() {
            @Override
            public void dialogListener(String btnType, View customView,
                                       DialogInterface dialogInterface, int which) {

                if (btnType.equals("positive")) {

                    final String realName = etRealName.getCetEditText().getText().toString
                            ().trim();
                    String idCard = etIdCard.getCetEditText().getText().toString()
                            .trim();
                    if (TextUtils.isEmpty(realName)) {
                        Toast.makeText(MyBankCardActivity.this, "请输入真实姓名", Toast
                                .LENGTH_SHORT).show();
                        return;
                    } else if (TextUtils.isEmpty(idCard)) {
                        Toast.makeText(MyBankCardActivity.this, "请输入身份证号码", Toast
                                .LENGTH_SHORT).show();
                        return;
                    }
                    commonDialog.getPositiveBtn().setText("认证中...");
                    commonDialog.getPositiveBtn().setEnabled(false);
                    authPresenter.auth(realName, idCard, new ICallback() {
                        @Override
                        public void exec(Object params) {
                            commonDialog.getPositiveBtn().setText("立即认证");
                            commonDialog.getPositiveBtn().setEnabled(true);
                            Map<String, String> respMap = (Map<String, String>) params;
                            if ("000002".equals(respMap.get("respCode"))) {
                                Toast.makeText(MyBankCardActivity.this,
                                        "实名认证失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                authHandler.sendEmptyMessage(1);
                            }else if("000001".equals(respMap.get("respCode"))){
                                Toast.makeText(MyBankCardActivity.this,
                                        "认证失败，姓名与身份证号码不匹配", Toast.LENGTH_SHORT).show();
                                authHandler.sendEmptyMessage(1);
                            } else {
                                Toast.makeText(MyBankCardActivity.this, respMap.get
                                        ("respDesc"), Toast.LENGTH_SHORT).show();
                                authHandler.sendEmptyMessage(1);
                            }
                        }
                    });
                } else if (btnType.equals("negative")) {
                    commonDialog.dismiss();
                }

            }
        });
        commonDialog.showDialog();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dtv_bind_card: {
                boolean flag = Utilities.isAuth(this);
                if (!flag) {
                    auth();
                } else {
                    Intent intent = new Intent(this, BindCardActivity.class);
                    intent.putExtra("type", type);
                    startActivityForResult(intent, REQUEST);
                }
                break;
            }
            case R.id.btn_add_bankcard: {
                Intent intent = new Intent(this, BindCardActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, REQUEST);
                break;

            }

        }
    }

    @Override
    public void updateModel(String key, Object data) {
        if (key.equals(AppService.QUERY_BANK_CARD)) {

            Map<String, String> respData = (Map<String, String>) data;
            String list = respData.get("list");
            try {
                JSONArray jsonArray = new JSONArray(list);
                if (jsonArray.length() <= 0) {
                    mHasBankCard = false;
                } else {
                    mHasBankCard = true;
                    mBankCardList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, Object> bank = new HashMap<>();
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Iterator<String> it = jsonObject.keys();
                        while (it.hasNext()) {
                            String paramKey = it.next();
                            bank.put(paramKey, jsonObject.getString(paramKey));
                        }
                        mBankCardList.add(bank);
                        bankCardAdapter.updateData(mBankCardList, false);
                    }
                }
                if (hasBankCard()) {
                    findViewById(R.id.layout_bankcard).setVisibility(View.VISIBLE);
                    findViewById(R.id.layout_bankcard_none).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.layout_bankcard).setVisibility(View.GONE);
                    findViewById(R.id.layout_bankcard_none).setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (key.equals(AppService.AUTH)) {
            Map<String, String> respData = (Map<String, String>) data;
            String respCode = respData.get("respCode");
            if ("000000".equals(respCode)) {
                Utilities.setIsAuth(this, true);
                Toast.makeText(this, "实名认证成功", Toast.LENGTH_SHORT).show();
                authHandler.sendEmptyMessage(0);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST) {
            if (presenter == null) {
                presenter = new BankCardPresenter(this);
            }
            presenter.queryBankCard();
        }

    }


}
