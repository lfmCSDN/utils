package com.sunyard.aps.common.utils;

import java.math.BigDecimal;

/**
 * 有关数字操作的常用函数.
 * 判断指定的字符串是否为数字<code>isNumber</code>
 * 分元转换<code>yuanToCent</code>、<code>centToYuan</code>
 * Created by zhangxin on 2017/10/23.
 */
public class NumberUtils {

    /**
     * 判断一个字符串是否为有效的java语言表达的数字.
     * <p>
     * 有效的数字包含16进制表示的经<code>0x</code>开头的或者指定某种类型的例如(123L).
     * <p>
     * <code>Null</code> and 空字符串会返回<code>false</code>.
     *
     * @param str 被检查的字符串.
     * @return <code>true</code> 当字符串是正确格式的数字
     */
    public static final boolean isNumber(final String str) {
        return org.apache.commons.lang.math.NumberUtils.isNumber(str);
    }

    /**
     * 金额元转为分.
     *
     * @param yuanObject 金额元
     * @return 分 Long类型
     */
    public static final long yuanToCent(Object yuanObject) {
        BigDecimal yuan = toDecimal(yuanObject);
        yuan = yuan.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal bg = yuan.multiply(new BigDecimal(100));
        return bg.longValue();
    }

    /**
     * 金额元转为分,并且分以字符串表示
     * @param yuanObject
     * @return 分 字符串
     */
    public static final String yuanToCentString(Object yuanObject) {
        return String.valueOf(yuanToCent(yuanObject));
    }

    /**
     * 金额分转为元.
     *
     * @param cent 金额分
     * @return 元
     */
    public static final BigDecimal centToYuan(String cent) {
        BigDecimal yuan = new BigDecimal(cent).divide(new BigDecimal(100));
        return yuan.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 金额分转为元.
     *
     * @param cent 金额分
     * @return 元
     */
    public static final BigDecimal centToYuan(Object cent) {
        if(null == cent){
            throw new IllegalArgumentException("cent ["+cent+"] should not be null.");
        }
        return centToYuan(cent.toString());
    }

    /**
     * 金额分转为元.
     *
     * @param cent 金额分
     * @return 元
     */
    public static final String centToYuanAsString(String cent) {
        return centToYuan(cent).toString();
    }

    /**
     * 将一个指定的对象转换为 {@link BigDecimal}.
     * @param o 指定的对象，可以是String,Number
     * @return BigDecimal
     */
    public static BigDecimal toDecimal(Object o){
        BigDecimal result = null;
        if (o instanceof String) {
            result = org.springframework.util.NumberUtils.parseNumber((String) o, BigDecimal.class);
        } else if (o instanceof Number) {
            result = org.springframework.util.NumberUtils.convertNumberToTargetClass((Number) o, BigDecimal.class);
        } else {
            throw new IllegalArgumentException("Substract param a  type illegal [" + o + "].");
        }
        return result;
    }
}
