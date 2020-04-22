package cn.payadd.majia.constant;

/**
 * Created by zhengzhen.wang on 2017/6/20.
 */

public final class InstallmentStatus {

    //待审核
    public static final String VERIFY_PENDING = "1";

    //待签约
    public static final String PENDING_SIGN = "2";

    //已拒绝
    public static final String VERIFY_REJECT = "3";

    //签约待确定
    public static final String SIGN_PENDING = "4";

    //已签约
    public static final String SIGNED = "5";
}
