package com.sunyard.aps.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @auther zhanghui
 * @date 2017/8/18 14:23
 * @desc
 */
public class Md5Utils {
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String md5(String str) {
        return  getMD5ofStr(str);
        /*
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
        */
    }

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String getMD5ofStr(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static String md5Digest(byte[] src) {
        MessageDigest alg;
        try {
            // MD5 is 32 bit message digest
            alg = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return byteArrayToHexString(alg.digest(src));
    }

    public static void main(String[] args) {
        //zhanghui  输出  c593cc79d6afc605cec121e6f268fdfc
        System.out.println(md5("zhanghui"));

        //f974cb9917d2d2e9501afa9394b86ca2
        String s1="account_class=0&account_level=2&account_name=张三&account_property=1&action_range=0&bank_address=330000&bank_card=0&bank_cityno=330100&currency=CNY&exit_feemark=0&id_no=330101198809281010&id_type=1&industry=2A&interest_mode=2&interest_toaccount=1&mobile=13738195800&open_date=2017-10-10&open_teller=00000000&open_tradetime=2017-10-10 08:08:08&pre_bank_card=0&query_pwd=123456&rate_float=0&rate_no=123&serial_no=13738195800&sms_amount=0&sms_flag=0&subject_no=123&trade_pwd=123456&key=SUNYARD.COM";
        System.out.println(Md5Utils.md5(s1));
    }
}
