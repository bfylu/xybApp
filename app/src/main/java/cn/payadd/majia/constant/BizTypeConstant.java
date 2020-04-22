package cn.payadd.majia.constant;

/**
 * Created by df on 2017/10/9.
 */

public class BizTypeConstant {
    private static final String typeName_1 = "广告收益";
    private static final String typeName_2 = "费率返现";

    public static String getTypeName(String bizType) {
        String typeName = null;
        switch (bizType){
            case "1":
                typeName =  typeName_1;
                break;
            case "2":
                typeName =  typeName_2;
                break;
        }
        return typeName;
    }
}
