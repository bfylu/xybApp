package cn.payadd.majia.config;

import android.content.Context;

import com.utils.Utilities;

import cn.payadd.majia.util.StringUtil;

/**
 * Created by zhengzhen.wang on 2017/6/7.
 */

public class AppConfig {

    public static final String testHttpUrl = "http://192.168.1.41:10200";

    public static final String releaseHttpUrl = "https://shop.payadd.cn/ai-service";

    public static String getServerInterface() {

        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/majia";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.41:10019/majia";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/majia";
        }
        return null;
    }

    public static String getNewServerInterface(){
        if (EnvironmentConfig.isDevel()) {
            return "http://wx.payadd.cn/tms/xyb/app";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.41:8081/tms/xyb/app";
        } else if (EnvironmentConfig.isRelease()) {
            return "http://tms.payadd.cn/xyb/app";
        }
        return null;
    }

    public static String getInstallmentUploadInterface() {

        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/installment/upload";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/installment/upload";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/installment/upload";
        }
        return null;
    }

    public static String getInstallmentFileInterface() {

        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/installment/getFilePath";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/installment/getFilePath";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/installment/getFilePath";
        }
        return null;
    }

    public static String getInstallmentDeleteContract() {

        if (EnvironmentConfig.isDevel()) {
            return "http://168.192.1.41:38080/mpos/installment/delete/contractContentPic";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/installment/delete/contractContentPic";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/installment/delete/contractContentPic";
        }
        return null;
    }

    public static String getInstallmentDeleteConsumeVoucher() {

        if (EnvironmentConfig.isDevel()) {
            return "http://168.192.1.41:38080/mpos/installment/delete/consumeVoucherPic";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/installment/delete/consumeVoucherPic";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/installment/delete/consumeVoucherPic";
        }
        return null;
    }

    public static String getInstallmentSaveContract() {

        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/installment/submit";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/installment/submit";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/installment/submit";
        }
        return null;
    }

    public static String getH5Statistics() {

        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/html/account1.html";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/html/account1.html";
        } else if (EnvironmentConfig.isRelease()) {
            return "http://mpos.payadd.cn/html/account1.html";
        }
        return null;
    }

    public static String getH5Account() {

        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/html/count.html?username=%s&sessionToken=%s&platform=android&loginToken=%s";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/html/count.html?username=%s&sessionToken=%s&platform=android&loginToken=%s";
        } else if (EnvironmentConfig.isRelease()) {
            return "http://mpos.payadd.cn/html/count.html?username=%s&sessionToken=%s&platform=android&loginToken=%s";
        }
        return null;
    }

    public static String getViewCode() {
        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/verifyCode";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/verifyCode";
        } else if (EnvironmentConfig.isRelease()) {
            return "http://mpos.payadd.cn/html/mpos/verifyCode";
        }
        return null;
    }

    public static String getInstallmentSignImageInterface() {

        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/installment/uploadSignaturePic";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/installment/uploadSignaturePic";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/installment/uploadSignaturePic";
        }
        return null;
    }

    public static String getInstallmentContractInterface(String orderNo, Context context) {
        if (EnvironmentConfig.isDevel()) {
            return String.format("http://web-test.payadd.cn/app_h5/html/personSignContract.html?orderNo=%s&sessionToken=%s", new Object[]{orderNo, Utilities.getSessionToKen(context)});
        } else if (EnvironmentConfig.isTest()) {
            return String.format("http://web-test.payadd.cn/app_h5/html/personSignContract.html?orderNo=%s&sessionToken=%s", new Object[]{orderNo, Utilities.getSessionToKen(context)});
        } else if (EnvironmentConfig.isRelease()) {
            return String.format("http://mjh5.payadd.cn/html/personSignContract.html?orderNo=%s&sessionToken=%s", new Object[]{orderNo, Utilities.getSessionToKen(context)});
        }
        return null;
    }

    public static String getRegisterInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "http://web-test.payadd.cn/app_h5/register.html";
        } else if (EnvironmentConfig.isTest()) {
            return "http://web-test.payadd.cn/app_h5/register.html";
        } else if (EnvironmentConfig.isRelease()) {
            return "http://mjh5.payadd.cn/register.html";
        }
        return null;
    }

    public static String getForgetPasswordInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "http://web-test.payadd.cn/app_h5/resetPwd-Phone.html";
        } else if (EnvironmentConfig.isTest()) {
            return "http://web-test.payadd.cn/app_h5/resetPwd-Phone.html";
        } else if (EnvironmentConfig.isRelease()) {
            return "http://mjh5.payadd.cn/resetPwd-Phone.html";
        }
        return null;
    }
    ///installment/getShareUrl
    public static String getShareContractInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/installment/getShareUrl";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/installment/getShareUrl";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/installment/getShareUrl";
        }
        return null;
    }

    ///installment/getShareUrl
    public static String getFixedQrcodeInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "http://dev-lm.payadd.cn/mpos/common/getQrcode";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/common/getQrcode";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/common/getQrcode";
        }
        return null;
    }

    public static String getMerchantPwdInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
//            return "http://192.168.1.128:8083/tms/xyb/app";
            return "http://192.168.1.41:8081/tms/xyb/app";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://tms.payadd.cn/xyb/app";
        }
        return null;
    }

    public static String getImageInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
//            return "http://192.168.1.128:8083/image/view?refNo=%&width=$";
            return "http://192.168.1.41:8081/image/view?refNo=%&width=$";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://file.payadd.cn/image/view?refNo=%&width=$";
        }
        return null;
    }

    public static String getRefundInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.123:10014/majia";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://mpos.payadd.cn/majia";
        }
        return null;
    }

    public static String getMerchantPwdCodeInterface() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return "http://192.168.1.128:8081/ums/verify/genMsgCode";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://ums.payadd.cn/verify/genMsgCode";
        }
        return null;
    }

    public static String getIMAccountAndSig() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
//            return "http://192.168.1.201:10100/ai-service/tls/accountImportAndReturnSig";
            return StringUtil.append(testHttpUrl, "/tls/accountImportAndReturnSig");
        } else if (EnvironmentConfig.isRelease()) {
//            return "https://ai.payadd.cn/tls/accountImportAndReturnSig";
            return StringUtil.append(releaseHttpUrl, "/tls/accountImportAndReturnSig");
        }
        return null;
    }

    /**
     * 获取商户周边用户
     * @return
     */
    public static String getRelativePosition() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return StringUtil.append(testHttpUrl, "/aiRadar/getRelativePosition");
        } else if (EnvironmentConfig.isRelease()) {
            return StringUtil.append(releaseHttpUrl, "/aiRadar/getRelativePosition");
        }
        return null;
    }

    /**
     * 获取商户店铺人数
     * @return
     */
    public static String getPeopleNumber() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return StringUtil.append(testHttpUrl, "/aiRadar/getPeopleNumber");
        } else if (EnvironmentConfig.isRelease()) {
            return StringUtil.append(releaseHttpUrl, "/aiRadar/getPeopleNumber");
        }
        return null;
    }

    /**
     * 获取用户行为记录
     * @return
     */
    public static String getActionRecord() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return StringUtil.append(testHttpUrl, "/aiRadar/getActionRecord");
        } else if (EnvironmentConfig.isRelease()) {
            return StringUtil.append(releaseHttpUrl, "/aiRadar/getActionRecord");
        }
        return null;
    }

    /**
     * 获取客户列表
     * @return
     */
    public static String getCustomerList() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return StringUtil.append(testHttpUrl, "/aiCustomerRoster/getCustomerList");
        } else if (EnvironmentConfig.isRelease()) {
            return StringUtil.append(releaseHttpUrl, "/aiCustomerRoster/getCustomerList");
        }
        return null;
    }

    /**
     * 获取客户行为列表
     * @return
     */
    public static String getCustomerInfoList() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return StringUtil.append(testHttpUrl, "/userActionCount/getVisitorDynamic");
        } else if (EnvironmentConfig.isRelease()) {
            return StringUtil.append(releaseHttpUrl, "/userActionCount/getVisitorDynamic");
        }
        return null;
    }

    /**
     * 数据同步接口
     * @return
     */
    public static String synchMerData() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
//            return "http://192.168.1.41:10200/DataSynch/SynchMerData";
            return StringUtil.append(testHttpUrl, "/DataSynch/SynchMerData");
        } else if (EnvironmentConfig.isRelease()) {
//            return "https://ai.payadd.cn/DataSynch/SynchMerData";
            return StringUtil.append(releaseHttpUrl, "/DataSynch/SynchMerData");
        }
        return null;
    }

    /**
     * 用户详情接口
     * @return
     */
    public static String getCustomerInfo() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return StringUtil.append(testHttpUrl, "/aiCustomerInfo/getCustomerInfo");
        } else if (EnvironmentConfig.isRelease()) {
            return StringUtil.append(releaseHttpUrl, "/aiCustomerInfo/getCustomerInfo");
        }
        return null;
    }

    /**
     * 客户筛选列表
     * @return
     */
    public static String getClientScreenList() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return StringUtil.append(testHttpUrl, "/aiCustomerRoster/getCustomerScreen");
        } else if (EnvironmentConfig.isRelease()) {
            return StringUtil.append(releaseHttpUrl, "/aiCustomerRoster/getCustomerScreen");
        }
        return null;
    }

    /**
     * 获取小程序access_token
     * @return
     */
    public static String getAccessToken() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return "https://api.weixin.qq.com/cgi-bin/token";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://api.weixin.qq.com/cgi-bin/token";
        }
        return null;
    }

    /**
     * 获取小程序二维码
     * @return
     */
    public static String getMiniProPic() {
        if (EnvironmentConfig.isDevel()) {
            return "";
        } else if (EnvironmentConfig.isTest()) {
            return "https://api.weixin.qq.com/wxa/getwxacode?access_token=&&";
        } else if (EnvironmentConfig.isRelease()) {
            return "https://api.weixin.qq.com/wxa/getwxacode?access_token=&&";
        }
        return null;
    }

}
