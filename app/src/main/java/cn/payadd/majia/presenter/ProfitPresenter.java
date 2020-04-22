package cn.payadd.majia.presenter;

import android.app.Activity;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import cn.payadd.majia.adapter.ProfitDayRecordAdapter;
import cn.payadd.majia.adapter.ProfitRecordPojoAdapter;
import cn.payadd.majia.adapter.WithdrawRecordAdapter;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.face.IFragment;

/**
 * Created by df on 2017/9/21.
 */

public class ProfitPresenter extends BasePresenter {
    private IFragment iFragment;

    public ProfitPresenter(IFragment iFragment, Context context) {
        super(context);
        this.iFragment = iFragment;
    }

    public void queryProfitRecord(final ProfitRecordPojoAdapter adapter, final boolean resetPage) {
        if (resetPage) {
            adapter.setPage(1);
        } else {
            adapter.setPage(adapter.getPage() + 1);
        }
        Map<String, String> data = new HashMap<>();
        data.put("recordType", "income");
        data.put("current", adapter.getPage() + "");
        data.put("row", adapter.getRow() + "");
        sendToServer(AppService.PROFIT_MANAGE, data, new ICallback() {

            @Override
            public void exec(Object params) {
                iFragment.updateModel("profitRecord", params);
            }
        });
    }
    public void queryWithdrawRecord(final WithdrawRecordAdapter adapter, boolean resetPage) {
        if (resetPage) {
            adapter.setPage(1);
        } else {
            adapter.setPage(adapter.getPage() + 1);
        }
        Map<String, String> data = new HashMap<>();
        data.put("recordType", "withdraw");
        data.put("current", adapter.getPage() + "");
        data.put("row", adapter.getRow() + "");
        sendToServer(AppService.PROFIT_MANAGE, data, new ICallback() {

            @Override
            public void exec(Object params) {
                iFragment.updateModel("withdrawRecord", params);
            }
        });
    }
}
