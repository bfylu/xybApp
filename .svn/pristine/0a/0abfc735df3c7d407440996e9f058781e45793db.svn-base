package cn.payadd.majia.exception;

/**
 * Created by zhengzhen.wang on 2017/6/7.
 */

public class BusinessRuntimeException extends RuntimeException {

    private String errCode;
    private String errMsg;

    public BusinessRuntimeException(String errCode, String errMsg) {
        super();
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public BusinessRuntimeException(String errCode) {
        super();
        this.errCode = errCode;
        this.errMsg = ExceptionUtil.getCodeDesc(errCode);
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
