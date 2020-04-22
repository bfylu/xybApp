package com.fdsj.credittreasure.Interface.iActivities;

import java.util.Map;

/**
 * Created by BXND on 2017-01-06.
 */

public interface ILogin {
    //    void downloadKey();

    /**
     * @param loginToken
     * @param username
     * @param usernameForLogin
     * @param passWord
     * @param mobilePhone
     * @param nickName
     * @param shopName
     * @param Welcome
     * @param sessionToken
     * @param mposKey
     * @param showMerName
     * @param email
     * @param appSecurity 商户管理密码
     */
    void userLogin(String loginToken, String username, String usernameForLogin, String passWord,
                   String mobilePhone, String nickName, String shopName, String Welcome, String
                           sessionToken, String mposKey, String showMerName, String email, String
                           agentFlag, String isAuth,String ismtNoticeCount,String merCode,String goodSelectionUrl, String appSecurity);

    /**
     * 登录失败
     *
     * @param message
     */
    void userLoginError(String message, Map<String, String> respData);
}
