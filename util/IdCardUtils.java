/*
 * Youlema.com Inc.
 * Copyright (c) 2011-2013 All Rights Reserved
 */

package com.sunyard.aps.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Write class comments here
 * <p>
 * User:jiaw.j
 * Date:2018/1/8 0008上午 10:48
 * version $Id:IdCardUtils.java,v 0.1 Exp $
 */
public class IdCardUtils {
    //区域码
    final static Map<Integer,String> zoneNum = new HashMap<Integer,String>();

    static{
        zoneNum.put(11,"北京");
        zoneNum.put(12,"天津");
        zoneNum.put(13,"河北");
        zoneNum.put(14,"山西");
        zoneNum.put(15,"内蒙古");
        zoneNum.put(21,"辽宁");
        zoneNum.put(22,"吉林");
        zoneNum.put(23,"黑龙江");
        zoneNum.put(31,"上海");
        zoneNum.put(32,"江苏");
        zoneNum.put(33,"浙江");
        zoneNum.put(34,"安徽");
        zoneNum.put(35,"福建");
        zoneNum.put(36,"江西");
        zoneNum.put(37,"山东");
        zoneNum.put(41,"河南");
        zoneNum.put(42,"湖北");
        zoneNum.put(43,"湖南");
        zoneNum.put(44,"广东");
        zoneNum.put(45,"广西");
        zoneNum.put(46,"海南");
        zoneNum.put(50,"重庆");
        zoneNum.put(51,"四川");
        zoneNum.put(52,"贵州");
        zoneNum.put(53,"云南");
        zoneNum.put(54,"西藏");
        zoneNum.put(61,"陕西");
        zoneNum.put(62,"甘肃");
        zoneNum.put(63,"青海");
        zoneNum.put(64,"新疆");
        zoneNum.put(71,"台湾");
        zoneNum.put(81,"香港");
        zoneNum.put(82,"澳门");
        zoneNum.put(91,"外国");
    }

    //校验码
    final static int[] PARITYBIT = {'1','0','X','9','8','7','6','5','4','3','2'};
    //随机因子
    final static int[] POWEER_LIST = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};

    /**
     *身份证号码验证
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard){
        if(idCard == null || (idCard.length() != 15 && idCard.length() != 18 )){
            return false;
        }

        final char[] idCardArr = idCard.toUpperCase().toCharArray();
        int power = 0;
        //求出随机数（1-14位或1-17位与相应随机因子的乘积和）
        for(int i = 0;i < idCardArr.length;i++){
            if(i == idCardArr.length - 1 && idCardArr[i] == 'X'){
                break;
            }
            if(idCardArr[i] < '0' || idCardArr[i] > '9'){
                return false;
            }
            if(i < idCardArr.length - 1){
                power += (idCardArr[i] - '0') * POWEER_LIST[i];
            }
        }

        //校验区域码
        if(!zoneNum.containsKey(Integer.valueOf((idCard.substring(0,2))))){
            return false;
        }

        //校验年份
        String year = idCard.length() == 15 ? getIdCardCalendar(idCard) : idCard.substring(6,10);
        final int iYear = Integer.parseInt(year);
        if(iYear < 1900 || iYear > Calendar.getInstance().get(Calendar.YEAR)){
            return false;
        }

        //校验月份
        String month = idCard.length() == 15 ? idCard.substring(8,10) : idCard.substring(10,12);
        final int iMonth = Integer.parseInt(month);
        if(iMonth < 1 || iMonth > 12){
            return false;
        }

        //校验天数
        String day = idCard.length() == 15 ? idCard.substring(10,12) : idCard.substring(12,14);
        final int iDay = Integer.parseInt(day);
        if(iDay < 1 || iDay > 31){
            return false;
        }

        //校验“校验码”
        if(idCard.length() == 15){
            return true;
        }
        return idCardArr[idCardArr.length - 1] == PARITYBIT[power%11];

    }

    //获取15位身份证的出生年份
    private static String getIdCardCalendar(String idCard){
        String birthday = idCard.substring(6,12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        Date birthdate = null;

        try{
            birthdate = sdf.parse(birthday);
        }catch (ParseException e){
            e.printStackTrace();
        }
        Calendar cDay = Calendar.getInstance();
        cDay.setTime(birthdate);
        String year = String.valueOf(cDay.get(Calendar.YEAR));

        return year;
    }


}
