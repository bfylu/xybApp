package cn.payadd.majia.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengzhen.wang on 2017/6/19.
 */

public class Validator {

    public static boolean checkIdcard(String idcard) {

        String regex = "[1-9]\\d{5}(19\\d{2}|[2-9]\\d{3})((0\\d)|(1[0-2]))(([0-2]\\d)|3[0-1])(\\d{4}|\\d{3}X)";
        Pattern p = Pattern.compile(regex);
        return p.matcher(idcard).matches();
    }
    public static boolean checkNumber(String number){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(number);
            if( !isNum.matches() ){
                return false;
            }
            return true;
        }

}
