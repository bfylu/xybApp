package com.fdsj.credittreasure.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengzhen.wang on 2017/4/11.
 */

public class ProductType {

    public static final String MAJIA = "01";

    public static final String QRCODE = "02";

    public static final String BOXPAY = "03";

    public static final String POS = "04";

    public static final String DEKEPC = "05";

    public static final String PC_CLIENT = "06";

    public static final String PRE_CREDIT = "07";

    private static Map<String, String> map;

    static {
        map = new HashMap<>();
        map.put(MAJIA, "信用商圈APP");
        map.put(QRCODE, "固定码收款");
        map.put(BOXPAY, "派派小盒");
        map.put(POS, "POS机");
        map.put(DEKEPC,"收银机");
        map.put(PC_CLIENT, "收银机");
        map.put(PRE_CREDIT,"预授信收款");
    }

    public static String getValue(String prodType) {

        String val = map.get(prodType);
        return null == val ? "" : val;
    }

}
