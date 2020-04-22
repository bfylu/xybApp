package cn.payadd.majia.presenter;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.face.IFragment;
import cn.payadd.majia.util.AppLog;

/**
 * Created by zhengzhen.wang on 2017/6/25.
 */

public class StatisticsPresenter extends BasePresenter {

    public static final String LOG_TAG = "StatisticsPresenter";
    private IFragment iFragment;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public StatisticsPresenter(Context ctx, IFragment iFragment) {
        super(ctx);
        this.iFragment = iFragment;
    }

    public void query(Date startDate, Date endDate) {

        Map<String, String> data = new HashMap<>();
        if (null != startDate) {
            data.put("startDate", sdf.format(startDate));
        }
        if (null != endDate) {
            data.put("endDate", sdf.format(endDate));
        }
        AppLog.d(LOG_TAG, "data -> " + data);
        sendToServer(AppService.NEW_STATISTICS, data, new ICallback() {

            @Override
            public void exec(Object params) {

                iFragment.updateModel("", params);
            }
        });

    }

}
