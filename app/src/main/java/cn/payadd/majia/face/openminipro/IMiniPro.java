package cn.payadd.majia.face.openminipro;

import android.graphics.Bitmap;

import java.io.File;

import cn.payadd.majia.entity.openminipro.AccessTokenBean;

public interface IMiniPro {

    /**
     * 获取小程序的access_token
     * @param data
     */
    void getAccessToken(AccessTokenBean data);

    /**
     * 获取小程序二维码
     * @param data
     */
    void getMiniProPic(Bitmap data);
}
