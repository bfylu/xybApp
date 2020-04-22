package com.zhy.http.okhttp.callback;

import android.text.TextUtils;

import com.utils.Config;
import com.utils.EncodeUtil;
import com.utils.LogUtil;
import com.utils.Utilities;
import com.zhy.http.okhttp.utils.L;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by BXND on 2017-01-06.
 */

public abstract class HashMapCallback extends Callback<Map<String, String>> {
    @Override
    public Map<String, String> parseNetworkResponse(Response response, String merchantKey, int id) throws IOException {
        String data = response.body().string();
        LogUtil.info("解密前", data);
        if (!TextUtils.isEmpty(merchantKey)) {
            data = EncodeUtil.decryptMode(merchantKey, data);
        } else {
            data = EncodeUtil.getURLEncoder(data);
        }
        LogUtil.info("解密后", data);
        return Utilities.fromHashMap(data);
    }
}

