package cn.payadd.majia.face;

/**
 * Created by zhengzhen.wang on 2017/6/22.
 */

public interface ReqProgressCallBack<T> extends ReqCallBack<T> {

    void onProgress(long total, long current);
}