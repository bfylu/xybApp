package com.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2015/8/14.
 */
public class Utilities {


    public static Map<String, String> fromHashMap(String json) {
        Map<String, String> hashMap = new HashMap<>();
        try {
            String[] temp = json.split("\\&");
            for (String s : temp) {
                if (!TextUtils.isEmpty(s)) {
                    String[] Key_Values = s.split("\\=");
                    if (Key_Values != null && Key_Values.length > 1) {
                        hashMap.put(Key_Values[0], URLDecoder.decode(Key_Values[1], "UTF-8"));
                    }
                }
            }
            return hashMap;
        } catch (Exception e) {
            hashMap.put("respCode", Enums.apiState.异常.getString());
            hashMap.put("respDesc", "数据解析失败");
            return hashMap;
        }
    }

    public static String getSessionToKen(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "CreditTreasure",
                "SessionToKenChery");
    }

    public static void setSessionToKen(Context context, String value) {
        SharedPreferenceTool.getInstance().SaveShare(context, "CreditTreasure",
                "SessionToKenChery", value);
    }

    public static String getMerchantKey(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "CreditTreasure",
                "MerchantChery");
    }

    public static void setMerchantKey(Context context, String value) {
        SharedPreferenceTool.getInstance().SaveShare(context, "CreditTreasure", "MerchantChery",
                value);
    }

    public static void setIsAuth(Context context, boolean value) {
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "isAuth", value);
    }

    public static void setOrderNoSet(Context context, String value) {
        SharedPreferenceTool.getInstance().SaveShareSet(context, "UserInfo", "orderNo", value);
    }

    public static Set<String> getOrderNoSet(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShareSet(context, "UserInfo", "orderNo");
    }

    /**
     * 用户名
     *
     * @param context
     * @return
     */
    public static String getUsernameForLogin(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "OverUser",
                "usernameForLogin", "");
    }

    public static String getUsername(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo", "username", "");
    }

    public static String getShowMerName(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "OverUser",
                "showMerName", "");
    }

    public static String getEmail(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo", "email", "");
    }

    public static String getPassWord(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "User", "passWord", "");
    }

    /**
     * 用户昵称
     *
     * @param context
     * @return
     */
    public static String getNickName(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo", "nickName", "");
    }

    public static boolean isAgent(Context context) {
        String agentFlag = SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo",
                "agentFlag", "");
        if ("Y".equals(agentFlag)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAuth(Context context) {
        boolean agentFlag = SharedPreferenceTool.getInstance().GetSaveShareBoolean(context, "UserInfo",
                "isAuth");

            return agentFlag;

    }

    /**
     * 店铺名称
     *
     * @param context
     * @return
     */
    public static String getShopName(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo", "shopName", "");
    }

    /**
     * 欢迎语句
     *
     * @param context
     * @return
     */
    public static String getWelCome(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo", "welcome");
    }

//    public static String getLoginTime(Context context) {
//        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo", "time");
//    }

    public static String getLoginToken(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo", "loginToken");
    }

    public static String getMobilePhone(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "UserInfo",
                "mobilePhone", "");
    }


    public static String getDownloadKey(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context, "OverUser",
                "downloadKey", "");
    }

    public static void setDownloadKey(Context context, String downloadKey) {
        SharedPreferenceTool.getInstance().SaveShare(context, "OverUser", "downloadKey",
                !TextUtils.isEmpty(downloadKey) ? downloadKey : "");
    }

    public static boolean getCheckBox(Context context) {
        return SharedPreferenceTool.getInstance().GetSaveShareBoolean(context, "OverUser",
                "checkbox");
    }

    public static boolean getGuideIsFirst(Context context) {
        return SharedPreferenceTool.getInstance().GetSaveShareBoolean(context, "UserInfo",
                "guideIsFirst");
    }

    public static void setGuideIsFirst(boolean verCode ,Context context){
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo",
                "guideIsFirst", verCode);
    }

    public static void saveMerPwd(String merPwd, Context context) {
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "appSecurity"
                , !TextUtils.isEmpty(merPwd) ? merPwd : "");
    }

    /**
     * 重新登录，更新登录时间，更新用户资料
     *
     * @param context
     * @param loginToken
     * @param username
     * @param usernameForLogin
     * @param passWord
     * @param mobilePhone
     * @param nickName
     * @param shopName
     * @param welcome
     * @param sessionToken
     * @param mposKey
     * @param showMerName
     * @param status
     * @param email
     */
    public static void setUserInfo(Context context, String loginToken, String username, String usernameForLogin
                                    , String passWord, String mobilePhone, String nickName, String shopName
                                    , String welcome, String sessionToken, String mposKey, String showMerName
                                    , boolean status, String email, String isAgent, String isAuth
                                    , String ismtNoticeCount, String merCode, String goodsSelectionUrl, String appSecurity) {
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "nickName", !TextUtils
                .isEmpty(nickName) ? nickName : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "shopName", !TextUtils
                .isEmpty(shopName) ? shopName : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "welcome", !TextUtils
                .isEmpty(welcome) ? welcome : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "mobilePhone",
                !TextUtils.isEmpty(mobilePhone) ? mobilePhone : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "loginToken", loginToken);
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "username", !TextUtils
                .isEmpty(username) ? username : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "usernameForLogin",
                !TextUtils.isEmpty(usernameForLogin) ? usernameForLogin : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "OverUser", "usernameForLogin",
                !TextUtils.isEmpty(usernameForLogin) ? usernameForLogin : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "OverUser", "showMerName",
                !TextUtils.isEmpty(showMerName) ? showMerName : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "OverUser", "checkbox", status);
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "email", !TextUtils
                .isEmpty(email) ? email : "");
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "agentFlag", isAgent);
        if ("Y".equals(isAuth)) {
            setIsAuth(context, true);
        } else {
            setIsAuth(context, false);
        }
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "merCode", merCode);
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "goodsSelectionUrl", goodsSelectionUrl);
        int count = Integer.valueOf(ismtNoticeCount);
        setIsmtNoticeCount(context,count);

        setSessionToKen(context, sessionToken);
        setMerchantKey(context, mposKey);
        if (status) {
            SharedPreferenceTool.getInstance().SaveShare(context, "User", "passWord", !TextUtils
                    .isEmpty(passWord) ? passWord : "");
        }

        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "appSecurity"
                , !TextUtils.isEmpty(appSecurity) ? appSecurity : "");
    }

//    /**
//     * 记住密码登录，更新用户资料
//     *
//     * @param context
//     * @param userName
//     * @param nickName
//     * @param shopName
//     * @param welcome
//     * @param newTime
//     */
//    public static void setUserInfo(Context context, String loginToken, String userName, String
// passWord, String mobilePhone, String nickName, String shopName, String welcome, String newTime) {
//        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "userName",
// !TextUtils.isEmpty(userName) ? userName : "");
//        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "passWord",
// !TextUtils.isEmpty(passWord) ? passWord : "");
//        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "nickName",
// !TextUtils.isEmpty(nickName) ? nickName : "");
//        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "shopName",
// !TextUtils.isEmpty(shopName) ? shopName : "");
//        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "welcome", !TextUtils
// .isEmpty(welcome) ? welcome : "");
//        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "mobilePhone",
// !TextUtils.isEmpty(mobilePhone) ? mobilePhone : "");
//        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo", "time", newTime);
//        SharedPreferenceTool.getInstance().SaveShare(context, "User", "loginToken", loginToken);
//    }

    /**
     * 删除用户信息
     *
     * @param context
     */
    public static void delUserInfo(Context context) {
        SharedPreferenceTool.getInstance().removeObjectPre(context, "UserInfo");
    }

    public static void delOverInfo(Context context) {
        SharedPreferenceTool.getInstance().removeObjectPre(context, "OverUser");
    }


    public static void delUser(Context context) {
        SharedPreferenceTool.getInstance().removeObjectPre(context, "User");
    }

//    /**
//     * 删除密钥
//     *
//     * @param context
//     */
//    public static void delMerchantKey(Context context) {
//        SharedPreferenceTool.getInstance().removeObjectPre(context, "CreditTreasure");
//    }

    /**
     * 单条移除搜索历史
     *
     * @param context
     */
    public static void RemoveHistory(Context context, String key, String Text) {
        String HHistorys = "";
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        String History = sharedPreferences.getString(key, null);
        if (!TextUtils.isEmpty(History)) {
            String[] HistoryItem = History.split(",");
            for (String s : HistoryItem) {
                if (!Text.equals(s)) {
                    HHistorys += s + ",";
                }
            }
            if (!TextUtils.isEmpty(HHistorys)) {
                if (HHistorys.substring(HHistorys.length() - 1, HHistorys.length()).equals(",")) {
                    HHistorys = HHistorys.substring(0, HHistorys.length() - 1);
                }
            }
        }
        editor.putString(key, HHistorys);
        editor.commit();
    }

    /**
     * 清空搜索历史
     *
     * @param context
     */
    public static void ClearHistory(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        editor.clear();
        editor.commit();
    }

    /**
     * 添加搜索历史
     *
     * @param context
     * @param key
     * @param text
     */
    public static void SaveHistory(Context context, String key, String text) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        String History = sharedPreferences.getString(key, null);
        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
        if (History != null) {
            String[] HistoryItem = History.split(",");
            if (HistoryItem.length >= 3) {
                History = History.substring(0, History.length() - 1 - HistoryItem[2].length());
            }
            if (History.indexOf(text) < 0)
                editor.putString(key, text + "," + History);
        } else {
            editor.putString(key, text);
        }
        editor.commit();
    }

    /**
     * 得到搜索历史
     *
     * @param context
     * @return
     */
    public static List<String> GetHistory(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(key, Context
                .MODE_PRIVATE);
        String History = sharedPreferences.getString(key, null);
        if (!TextUtils.isEmpty(History)) {
            List list = Arrays.asList(History.split(","));
            List arrayList = new ArrayList(list);
            return arrayList;
        } else {
            return null;
        }
    }


    public final static String format = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将时间转化成毫秒
     *
     * @param time   2016-03-04
     * @param format "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Long timeStrToSecond(String time, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Long second = simpleDateFormat.parse(time).getTime();
            return second;
        } catch (Exception e) {
            return Long.parseLong("1471859160094");
        }
    }

    public static int getTimeDifference(String format, String time1, String time2) {
        try {
            DateFormat df = new SimpleDateFormat(format);
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d2.getTime() - d1.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return (int) days;
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 返回当前时间
     *
     * @param format
     * @return
     */
    public static String getNewDate(String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(new Date());
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 返回时间
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static String getStandardDate(String timeStr, String format) {
        Calendar calendar = Calendar.getInstance();
        Long currentMillisecond = calendar.getTimeInMillis();
        Long millisecond = timeStrToSecond(timeStr, format);
        //间隔秒
        Long spaceSecond = (currentMillisecond - millisecond) / 1000;

        //一分钟之内
        if (spaceSecond >= 0 && spaceSecond < 60) {
            return "刚刚";
        }
        //一小时之内
        else if (spaceSecond / 60 > 0 && spaceSecond / 60 < 60) {
            return spaceSecond / 60 + "分钟前";
        }
        //一天之内
        else if (spaceSecond / (60 * 60) > 0 && spaceSecond / (60 * 60) < 24) {
            return spaceSecond / (60 * 60) + "小时前";
        }
        //3天之内
        else if (spaceSecond / (60 * 60 * 24) > 0 && spaceSecond / (60 * 60 * 24) < 3) {
            return spaceSecond / (60 * 60 * 24) + "天前";
        } else {
            return getDateTime(millisecond, format);
        }
    }


    /**
     * 将毫秒转换为时间
     *
     * @param millisecond
     * @param format      "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getDateTime(Long millisecond, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    /**
     * 格式化时间
     *
     * @param time    时间字符串
     * @param format1 当前字符串格式
     * @param format2 需要的格式
     * @return
     */
    public static String setFormatTime(String time, String format1, String format2) {
        if (!TextUtils.isEmpty(time)) {
            SimpleDateFormat formatter = new SimpleDateFormat(format1);
            formatter.setLenient(false);
            try {
                Date newDate = formatter.parse(time);
                formatter = new SimpleDateFormat(format2);
                return formatter.format(newDate);
            } catch (ParseException e) {
                return time;
            }
        }
        return time;
    }

    private static MediaPlayer mediaPlayer1;

    public static void playerPayMusic(Context context, int Path) {

        try {
            if (null == mediaPlayer1) {
                mediaPlayer1 = MediaPlayer.create(context, Path);
            }
            mediaPlayer1.start();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
    }

    private static MediaPlayer mediaPlayer2;

    public static void playerMusic(Context context, int Path) {

        try {
            if (null == mediaPlayer2) {
                mediaPlayer2 = MediaPlayer.create(context, Path);
            }
            mediaPlayer2.start();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

        }
    }

    /**
     * 格式化时间
     *
     * @param date   时间
     * @param format 格式
     * @return
     */
    public static String setFormatTime(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }


    /**
     * 返回EditText中的值
     */
    public static String getEditText(EditText editText) {
        if (editText != null && editText.getText() != null && !(editText.getText().toString()
                .trim().equals(""))) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }


    /**
     * 設置View的寬度（像素），若設置爲自適應則應該傳入MarginLayoutParams.WRAP_CONTENT
     *
     * @param view
     * @param width
     */
    public static void setLayoutWidth(View view, int width) {
        if (view.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.width = width;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        }
    }

    /**
     * 設置View的高度（像素），若設置爲自適應則應該傳入MarginLayoutParams.WRAP_CONTENT
     *
     * @param view
     * @param height
     */
    public static void setLayoutHeight(View view, int height) {
        if (view.getParent() instanceof FrameLayout) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof RelativeLayout) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        } else if (view.getParent() instanceof LinearLayout) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.height = height;
            view.setLayoutParams(lp);
            //view.setX(x);
            view.requestLayout();
        }
    }


    /**
     * 计算adapter的高度，设置在listview中
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    //生成图片名称
    public static String getPhotoFileNames() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG1'_yyyyMMdd_HHmmss");
        return "DCIM/Camera/" + dateFormat.format(date) + ".jpg";
    }


    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);//参数100表示不压缩
        byte[] bytes = baos.toByteArray();
        result = Base64.encodeToString(bytes, Base64.DEFAULT);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 移除尾部多余,s字符
     *
     * @param str
     * @return
     */
    public static String replaceStrEnd(String str, String c) {
        Pattern pattern = Pattern.compile("" + c + "+$");
        Matcher matcher = pattern.matcher(str);
        String text = matcher.replaceAll("");
        return text;
    }

    /**
     * 移除尾部多余,字符
     *
     * @param str
     * @return
     */
    public static String replaceStr(String str) {
        Pattern pattern = Pattern.compile("^\\&?");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll("");
    }

    /**
     * 保留小数点后两位
     *
     * @param number
     * @return
     */
    public static String getDoubleTwo(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.00");
        return decimalFormat.format(number);
    }


    /**
     * 不能输入某字符串
     *
     * @param str
     * @return
     */
    public static InputFilter getInputFilter(final String str) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int
                    dstart, int dend) {
                if (source.toString().equals(str))
                    return "";
                else
                    return null;
            }
        };
        return filter;
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static void setIsFirstUserFundAuth(Context context, boolean isFirst) {
        SharedPreferenceTool.getInstance().SaveShare(context, "ProductUser",
                "isFirstUserFundAuth", isFirst);
    }

    public static boolean getIsFirstUserFundAuth(Context context) {
        return SharedPreferenceTool.getInstance().GetSaveShareBoolean(context, "ProductUser",
                "isFirstUserFundAuth", true);
    }
    public static void setIsmtNoticeCount(Context context, int ismtNoticeCount) {
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo",
                "ismtNoticeCount", ismtNoticeCount);
    }

    public static int getIsmtNoticeCount(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShareInt(context,"UserInfo","ismtNoticeCount");
    }

    public static String getGoodsSelectionUrl(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context,"UserInfo","goodsSelectionUrl");
    }
    public static void setGoodsSelectionUrl(Context context,String goodsSelectionUrl) {
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo",
                "goodsSelectionUrl", goodsSelectionUrl);
    }
    public static void setMerCode(Context context,String merCode) {
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo",
                "merCode", merCode);
    }
    public static String getMerCode(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context,"UserInfo","merCode");
    }

    public static String getMerPwd(Context context) {
        return SharedPreferenceTool.getInstance().getSaveShare(context,"UserInfo","appSecurity");
    }

    public static void setResourceInfo(String verCode,String verName,Context context){
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo",
                "verCode", verCode);
        SharedPreferenceTool.getInstance().SaveShare(context, "UserInfo",
                "verName", verName);
    }

    public static String getResourceVerName(Context context){
        return SharedPreferenceTool.getInstance().getSaveShare(context,"UserInfo","verName");
    }
    public static String getResourceVerCode(Context context){
        return SharedPreferenceTool.getInstance().getSaveShare(context,"UserInfo","verCode");
    }

}
