package cn.payadd.majia.constant;

/**
 * Created by df on 2017/10/9.
 */

public class WithdrawStatus {
    private static final String PROCESS = "提现中";
    private static final String FAIL = "提现失败";
    private static final String SUCESS = "提现成功";

    public static String getStatusName(String status) {
        String statusName = null;
        switch (status){
            case "0":
                statusName =  PROCESS;
                break;
            case "1":
                statusName =  SUCESS;
                break;
            case "2":
                statusName =  PROCESS;
                break;
            case "3":
                statusName = FAIL;
                break;
        }
        return statusName;
    }
}
