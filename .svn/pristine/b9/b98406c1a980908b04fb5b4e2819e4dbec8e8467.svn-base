package cn.payadd.majia.util;

/**
 * Created by df on 2017/9/21.
 */

public class BankCardUtil {
    /**
     * 格式化银行卡号，每四个数字隔空格
     *
     * @param cardNo
     * @return
     */
    public static String bankCardFormat(String cardNo) {
        char[] chs = cardNo.toCharArray();
        char[] newArr = cardNo.toCharArray();
        int count = 0;
        for (int i = 0; i < chs.length; i++) {
            if ((i + 1) % 4 == 0) {
                newArr = insert(newArr, i+1+count, ' ');
                count++;
            }
        }
        return String.valueOf(newArr);
    }

    /**
     * 数组指定位置插入元素
     *
     * @param arr        原数组
     * @param x          位置
     * @param insertData 插入元素
     * @return char[]
     */
    public static char[] insert(char[] arr, int x, char insertData) {
        char[] chs = new char[(arr.length) + 1];
        for (int i = 0; i < x; i++) {
            chs[i] = arr[i];
        }
        chs[x] = insertData;
        for (int i = x + 1; i <= arr.length; i++) {
            chs[i] = arr[i - 1];
        }
        return chs;
    }
}
