package com.utils;


import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 * <p>
 * 1.MD5加密
 * 2.SHA加密
 * Created by Brioal on 2016/8/1.
 */

public class EncodeUtil {

    /**
     * 加密数据
     *
     * @param data
     * @return
     */
    public static String Encrypt(String data) {
        return Encrypt(data, "LENGJIAXING");
    }

    /**
     * 解密数据
     *
     * @param data
     * @return
     */
    public static String Decrypt(String data) {
        return Decrypt(data, "LENGJIAXING");
    }

    /**
     * 加密数据
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String Encrypt(String message, String key) {

        try {
            message = java.net.URLEncoder.encode(message, "utf-8").toLowerCase();//转码
            key = getMD5Code(key, true).substring(0, 8);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(message.getBytes("UTF-8"));

            //byte转16进制String
            StringBuffer hexString = new StringBuffer();
            for (byte b : bytes) {
                String plainText = Integer.toHexString(0xff & b);
                if (plainText.length() < 2)
                    plainText = "0" + plainText;
                hexString.append(plainText.toUpperCase());
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 解密数据
     *
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String Decrypt(String message, String key) {
        try {
            key = getMD5Code(key, true).substring(0, 8);
            //String转16进制Byte
            byte[] bytesrc = new byte[message.length() / 2];
            for (int i = 0; i < bytesrc.length; i++) {
                String byteString = message.substring(2 * i, 2 * i + 2);
                int byteValue = Integer.parseInt(byteString, 16);
                bytesrc[i] = (byte) byteValue;
            }
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] retByte = cipher.doFinal(bytesrc);
            return java.net.URLDecoder.decode(new String(retByte), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    //MD5加密
    public static String getMD5Code(String data, boolean status) {
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            byte[] bytes = md.digest(data.getBytes());
            for (byte b : bytes) {
                String plainText = Integer.toHexString(0xff & b);
                if (plainText.length() < 2)
                    plainText = "0" + plainText;
                hexString.append(status ? plainText.toUpperCase() : plainText.toLowerCase());
            }
        } catch (NoSuchAlgorithmException ex) {
            return data;
        }
        return hexString.toString();
    }

    //SHA加密
    public static String encryptSHA(String data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        return new BigInteger(sha.digest(data.getBytes())).toString(32);
    }

    /**
     * 中文转URLEncoder
     *
     * @param data
     * @return
     */
    public static String setURLEncoder(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                String str = URLEncoder.encode(data, "utf-8");
                LogUtil.info("中文转URLEncoder", str);
                return str;
            } catch (UnsupportedEncodingException e) {
                LogUtil.writeInfoToSDcard("中文转URLEncoder", e.getMessage());
                return data;
            }
        }
        return data;
    }

    /**
     * URLEncoder转中文
     *
     * @param data
     * @return
     */
    public static String getURLEncoder(String data) {
        try {
            String str = URLDecoder.decode(data, "utf-8");
            LogUtil.info("URLEncoder转中文", str);
            return str;
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeInfoToSDcard("URLEncoder转中文", e.getMessage());
            return data;
        }
    }

    private static final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish


    /**
     * 加密
     *
     * @param key
     * @param data
     * @return
     */
    public static String encryptMode(String key, String data) {
        try {
            //生成密钥
            DESedeKeySpec keySpec = new DESedeKeySpec(key.getBytes());
            SecretKey srtKey = SecretKeyFactory.getInstance(Algorithm).generateSecret(keySpec);
            //加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, srtKey);
            byte[] b = c1.doFinal(data.getBytes());//在单一方面的加密或解密
            return Base64.encodeToString(b, Base64.NO_WRAP);//Base64.DEFAULT 多了\n 使用NO_WRAP
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param key
     * @param data
     * @return
     */
    public static String decryptMode(String key, String data) {
        try {
            //生成密钥
            DESedeKeySpec keySpec = new DESedeKeySpec(key.getBytes());
            SecretKey srtKey = SecretKeyFactory.getInstance(Algorithm).generateSecret(keySpec);
//            SecretKey deskey = new SecretKeySpec(key.getBytes(), Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, srtKey);
            byte[] b = c1.doFinal(Base64.decode(data.getBytes(), Base64.NO_WRAP));//在单一方面的加密或解密
            return new String(b, "utf-8");
        } catch (java.security.NoSuchAlgorithmException e1) {
            LogUtil.writeInfoToSDcard("NoSuchAlgorithmException", e1.getMessage());
        } catch (javax.crypto.NoSuchPaddingException e2) {
            LogUtil.writeInfoToSDcard("NoSuchPaddingException", e2.getMessage());
        } catch (java.lang.Exception e3) {
            LogUtil.writeInfoToSDcard("Exception", e3.getMessage());
        }
        return data;
    }


}
