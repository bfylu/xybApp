package cn.payadd.majia.presenter;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.entity.aistore.AIBaseBean;
import cn.payadd.majia.entity.aistore.ActionRecordBean;
import cn.payadd.majia.entity.aistore.PeopleNumberBean;
import cn.payadd.majia.entity.aistore.RelativePositionBean;
import cn.payadd.majia.face.aistore.IRadar;
import cn.payadd.majia.util.StringUtil;
import okhttp3.Call;
import okhttp3.Response;

public class RadarPresenter {

    private Context ctx;

    private IRadar iRadar;

    private boolean pending;

    private Gson gson;

    public RadarPresenter(Context ctx, IRadar iRadar) {
        this.ctx = ctx;
        this.iRadar = iRadar;
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 获取商户周边用户
     * @param mercode  商户ID
     * @param distance 距离
     */
    public void getRelativePosition(String mercode, String distance) {
        if (pending) {
            return;
        }
        pending = true;
        try {
//            String actionUrl = StringUtil.append(AppConfig.getRelativePosition(), "?merCode=", mercode, "&distance=", distance);
            String actionUrl = AppConfig.getRelativePosition();

            OkGo.get(actionUrl).tag(ctx)
                    .params("merCode", mercode)
                    .params("distance", distance)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AIBaseBean baseBean = gson.fromJson(s, AIBaseBean.class);
                            if (baseBean.getCode() == 0) {
                                RelativePositionBean bean = gson.fromJson(s, RelativePositionBean.class);
                                iRadar.getRadar(bean);
                            } else {
                                Toast.makeText(ctx, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {

        } finally {
            pending = false;
        }

    }

    /**
     * 获取商户店铺人数
     * @param mercode  商户ID
     */
    public void getPeopleNumber(String mercode, String distance) {
        if (pending) {
            return;
        }
        pending = true;
        try {
//            String actionUrl = StringUtil.append(AppConfig.getPeopleNumber(), "?merCode=", mercode);
            String actionUrl = AppConfig.getPeopleNumber();
            OkGo.get(actionUrl).tag(ctx)
                    .params("merCode", mercode)
                    .params("distance", distance)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AIBaseBean baseBean = gson.fromJson(s, AIBaseBean.class);
                            if (baseBean.getCode() == 0) {
                                PeopleNumberBean bean = gson.fromJson(s, PeopleNumberBean.class);
                                iRadar.getPeopleNumber(bean);
                            } else {
                                Toast.makeText(ctx, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {

        } finally {
            pending = false;
        }

    }
    /**
     * 获取用户行为记录
     * @param mercode  商户ID
     */
    public void getActionRecord(String mercode, String page, String pageSize) {
        if (pending) {
            return;
        }
        pending = true;
        try {
//            String actionUrl = StringUtil.append(AppConfig.getActionRecord(), "?merCode=", mercode, "&pageNum=", page, "&pageSize=", pageSize);
            String actionUrl = AppConfig.getActionRecord();

            OkGo.get(actionUrl).tag(ctx)
                    .params("merCode", mercode)
                    .params("pageNum", page)
                    .params("pageSize", pageSize)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (StringUtil.isEmpty(s)) {
                                Toast.makeText(ctx, "返回报文为空", Toast.LENGTH_LONG).show();
                                return;
                            }
                            AIBaseBean baseBean = gson.fromJson(s, AIBaseBean.class);
                            if (baseBean.getCode() == 0) {
                                ActionRecordBean bean = gson.fromJson(s, ActionRecordBean.class);
                                iRadar.getActionRecord(bean);
                            } else {
                                Toast.makeText(ctx, baseBean.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {

        } finally {
            pending = false;
        }

    }
}
