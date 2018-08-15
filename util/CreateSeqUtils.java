package com.sunyard.aps.common.utils;

/**
 * Created by mhy on 2017/6/22.
 */
public class CreateSeqUtils {
    public static String createNumberSeq(int number,int needLength){
        String numberStr = number+"";
        if(numberStr.length()<=needLength){
            String result = number+"";
            for(int i=0;i<needLength-numberStr.length();i++){
                result = 0+result;
            }
            return result;
        }else {
            return numberStr;
        }
    }
}
