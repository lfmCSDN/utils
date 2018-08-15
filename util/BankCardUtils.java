package com.sunyard.aps.common.utils;

import org.apache.commons.lang.StringUtils;

/*
 * Created by mhy on 2017/7/12.
 */
public class BankCardUtils {
    public static String cardIdFormat(String cardId) {
        if(StringUtils.isNotBlank(cardId) && cardId.length()>=10){
            cardId =  cardId.replaceAll("\\s*", "");
            String starStr = "";
            for (int i = 0; i < cardId.length() - 10; i++) {
                starStr += "*";
            }
            String str = cardId.substring(0, 6) + starStr + cardId.substring(cardId.length() - 4, cardId.length());
            String newCardId = "";
            for (int i = 0; i < str.length(); i += 4) {
                if (i >= str.length() - 4) {
                    newCardId += str.substring(i, str.length());
                } else {
                    newCardId += str.substring(i, i + 4) + " ";
                }
            }
            return newCardId;
        }else {
            return "";
        }
    }
    public static boolean matchLuhn(String cardNo) {
        cardNo =  cardNo.replaceAll("\\s*", "");
        int[] cardNoArr = new int[cardNo.length()];
        for (int i=0; i<cardNo.length(); i++) {
            cardNoArr[i] = Integer.valueOf(String.valueOf(cardNo.charAt(i)));
        }
        for(int i=cardNoArr.length-2;i>=0;i-=2) {
            cardNoArr[i] <<= 1;
            cardNoArr[i] = cardNoArr[i]/10 + cardNoArr[i]%10;
        }
        int sum = 0;
        for(int i=0;i<cardNoArr.length;i++) {
            sum += cardNoArr[i];
        }
        return sum % 10 == 0;
    }
}