package com.zhy.http.okhttp.callback;

import android.text.TextUtils;
import android.util.Log;

import com.utils.EncodeUtil;
import com.utils.LogUtil;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, String merchantKey, int id) throws IOException {
        String data = response.body().string();
        LogUtil.info("解密前", data);
        if (!TextUtils.isEmpty(merchantKey)) {
            data = EncodeUtil.getURLEncoder(EncodeUtil.decryptMode(merchantKey, data));
        } else {
            data = EncodeUtil.getURLEncoder(data);
        }
        LogUtil.info("解密后", data);
        return data;
    }
}
