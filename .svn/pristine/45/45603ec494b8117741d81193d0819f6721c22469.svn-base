package cn.payadd.majia.activity.shoporder;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.lljjcoder.citylist.Toast.ToastUtils;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.List;

import butterknife.BindView;
import cn.payadd.majia.Interface.IShopOrder;
import cn.payadd.majia.activity.BarCodeActivity;
import cn.payadd.majia.activity.BaseActivity;
import cn.payadd.majia.adapter.shoporder.ChooseExpressDialogAdapter;
import cn.payadd.majia.entity.BaseBean;
import cn.payadd.majia.entity.ExpressCompanyBean;
import cn.payadd.majia.entity.MonthPaymentBean;
import cn.payadd.majia.entity.OrderNumBean;
import cn.payadd.majia.entity.ShopOrderBean;
import cn.payadd.majia.entity.ShopOrderCloseReasonBean;
import cn.payadd.majia.entity.ShopOrderDetailBean;
import cn.payadd.majia.presenter.ShopOrderPresenter;
import cn.payadd.majia.util.CenterDialog;
import cn.payadd.majia.util.StringUtil;

import static com.fdsj.credittreasure.R.id.iv_scan;
import static com.fdsj.credittreasure.constant.Constants.SHIP_ACTIVITY;

/**
 * Created by lang on 2018/5/18.
 */

public class ShipActivity extends BaseActivity implements View.OnClickListener, IShopOrder {

    @BindView(R.id.rl_need_express)
    RelativeLayout rlNeedExpress;
    @BindView(R.id.rl_no_need_express)
    RelativeLayout rlNoNeedExpress;
    @BindView(R.id.tv_need_express)
    TextView tvNeedExpress;
    @BindView(R.id.tv_no_need_express)
    TextView tvNoNeedExpress;
    @BindView(R.id.et_input_express_num)
    EditText etInputExpressNum;
    @BindView(iv_scan)
    ImageView ivScan;
    @BindView(R.id.et_input_express)
    EditText etInputExpress;
    @BindView(R.id.tv_choose_express)
    TextView tvChooseExpress;

    private Intent mIntent;

    private String orderNo;

    private String mExpressNum, mExpressCompany, mChooseCompany, mExpressCode;

    private int mChoosePosition;

    private String logistics; //是否需要物流   Y:是  N:否

    private CenterDialog mChooseExpressDialog;

    private ShopOrderPresenter mShopOrderPresenter;

    private ChooseExpressDialogAdapter mDialogAdapter;

    private List<ExpressCompanyBean.DataBean.DataListBean> mCompanyList;

    private String state;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ship;
    }

    @Override
    public void initView() {
        setTitleCenterText(getResources().getString(R.string.write_express_info));
        setTitleBackButton();
        setTitleRightText(getResources().getString(R.string.send_out), this);

        mShopOrderPresenter = new ShopOrderPresenter(this, this);

        ivScan.setOnClickListener(this);
        tvNeedExpress.setOnClickListener(this);
        tvNoNeedExpress.setOnClickListener(this);
        tvChooseExpress.setOnClickListener(this);

        logistics = "Y";
        changeIsNeedExpress(logistics);
    }

    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra("orderNo");

        mShopOrderPresenter.queryExpressCompany();
    }

    @Override
    protected void initPermission() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_right:
                if (StringUtil.equals("Y", logistics)) {
                    mExpressNum = etInputExpressNum.getText().toString().trim();
                    if (StringUtil.isEmpty(mExpressNum)) {
                        ToastUtils.showShortToast(this, getResources().getString(R.string.input_express_num));
                        return;
                    }
                    if (!StringUtil.equals(mExpressCompany, etInputExpress.getText().toString().trim())) {
                        mExpressCompany = etInputExpress.getText().toString().trim();
                        mExpressCode = "";
                    }
                    if (StringUtil.isEmpty(mExpressCompany)) {
                        ToastUtils.showShortToast(this, getResources().getString(R.string.choose_express_company));
                        return;
                    }
                }
                mShopOrderPresenter.deliverGoods(orderNo, logistics, mExpressNum, mExpressCode, mExpressCompany);
                break;
            case R.id.tv_need_express:
                logistics = "Y";
                changeIsNeedExpress(logistics);
                break;
            case R.id.tv_no_need_express:
                logistics = "N";
                changeIsNeedExpress(logistics);
                break;
            case R.id.iv_scan:
                mIntent = new Intent(this, BarCodeActivity.class);
                startActivityForResult(mIntent, SHIP_ACTIVITY);
                break;
            case R.id.tv_choose_express:
                showDialog(mCompanyList);
                break;
        }
    }

    private void showDialog(final List<ExpressCompanyBean.DataBean.DataListBean> dataList) {
        mChooseCompany = "";
        mChooseExpressDialog = new CenterDialog(this, R.layout.dialog_close_order, new int[] {R.id.cancel, R.id.confirm});
        mChooseExpressDialog.setOnCenterItemClickListener(new CenterDialog.OnCenterItemClickListener() {
            @Override
            public void OnCenterItemClick(CenterDialog dialog, View view) {
                switch (view.getId()) {
                    case R.id.confirm:
                        if (StringUtil.isEmpty(mChooseCompany)) {
                            ToastUtils.showShortToast(ShipActivity.this, getResources().getString(R.string.choose_express_company));
                        } else {
                            mExpressCompany = mChooseCompany;
                            mExpressCode = dataList.get(mChoosePosition).getCompanyCode();
                            etInputExpress.setText(mExpressCompany);
                        }
                        break;
                    case R.id.cancel:
                        hideDialog();
                        break;
                }
            }
        });
        mChooseExpressDialog.show();
        mChooseExpressDialog.setCanceledOnTouchOutside(true);

        TextView tvTitle = (TextView) mChooseExpressDialog.findViewById(R.id.tv_title);
        RelativeLayout rlCloseReason = (RelativeLayout) mChooseExpressDialog.findViewById(R.id.rl_close_reason);
        ListView listView = (ListView) mChooseExpressDialog.findViewById(R.id.listView);

        tvTitle.setText(getResources().getString(R.string.choose_express_company));
        mDialogAdapter = new ChooseExpressDialogAdapter(this, dataList);
        listView.setAdapter(mDialogAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDialogAdapter.selectPosition(position);
                mChoosePosition = position;
                mChooseCompany = dataList.get(position).getCompanyName();
            }
        });

        rlCloseReason.setVisibility(View.GONE);
    }

    private void hideDialog() {
        if (mChooseExpressDialog != null) {
            mChooseExpressDialog.dismiss();
            mChooseExpressDialog = null;
        }
    }

    private void changeIsNeedExpress(String logistics) {
        Drawable drawableClose = getResources().getDrawable(R.mipmap.close);
        drawableClose.setBounds(0, 0, drawableClose.getMinimumWidth(), drawableClose.getMinimumWidth());
        Drawable drawableOpen = getResources().getDrawable(R.mipmap.open);
        drawableOpen.setBounds(0, 0, drawableOpen.getMinimumWidth(), drawableOpen.getMinimumWidth());
        if (StringUtil.equals("Y", logistics)) {
            tvNeedExpress.setCompoundDrawables(drawableClose, null, null, null);
            tvNoNeedExpress.setCompoundDrawables(drawableOpen, null, null, null);
            rlNeedExpress.setVisibility(View.VISIBLE);
            rlNoNeedExpress.setVisibility(View.GONE);
        } else if (StringUtil.equals("N", logistics)) {
            tvNoNeedExpress.setCompoundDrawables(drawableClose, null, null, null);
            tvNeedExpress.setCompoundDrawables(drawableOpen, null, null, null);
            rlNeedExpress.setVisibility(View.GONE);
            rlNoNeedExpress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stopRecyclerView() {

    }

    @Override
    public void getShopBean(ShopOrderBean bean, int status) {

    }

    @Override
    public void getShopProtectionBean(ShopOrderBean bean, int status) {

    }

    @Override
    public void closeOrder(BaseBean data) {

    }

    @Override
    public void getShopOrderCloseReason(ShopOrderCloseReasonBean data) {

    }

    @Override
    public void getExpressCompanyList(ExpressCompanyBean data) {
        if (StringUtil.equals("000000", data.getRespCode())) {
            mCompanyList = data.getData().getDataList();
        } else {
            ToastUtils.showShortToast(this, data.getRespDesc());
        }
    }

    @Override
    public void deliverGoods(BaseBean data) {
        if (StringUtil.equals("000000", data.getRespCode())) {
            mIntent = new Intent();
            mIntent.setAction("cn.payadd.majia.fragment.shoporder.ChildSendOutFragment");
            sendBroadcast(mIntent);

            setResult(RESULT_OK, mIntent);
            finish();
        }
        ToastUtils.showShortToast(this, data.getRespDesc());
    }

    @Override
    public void getShopOrderDetail(ShopOrderDetailBean bean) {

    }

    @Override
    public void getMonthPayment(MonthPaymentBean bean) {

    }

    @Override
    public void getOrderNum(OrderNumBean bean) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        hideDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHIP_ACTIVITY && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                etInputExpressNum.setText(bundle.getString(CodeUtils.RESULT_STRING));
            }
        }
    }
}
