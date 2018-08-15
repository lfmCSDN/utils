package com.sunyard.aps.common.utils;

import com.sunyard.aps.common.feeUtil.Interpret;
import com.sunyard.aps.common.feeUtil.Money;

import java.math.BigDecimal;

/**
 * Created by zhangxin on 2017/11/1.
 */
public class FeeCaLcUtils {
    /**
     *  通过手续费计算公式及交易金额计算手续费金额
     * @param calMode
     * @param transAmt
     * @return
     * @throws IllegalArgumentException
     */
    public static BigDecimal getFeeAmt(String calMode, BigDecimal transAmt) throws IllegalArgumentException {
        Money amt = new Money(transAmt);
        Interpret interpret = new Interpret();
        String feeAmt = interpret.getFeeAmt(calMode, amt.toString());
        if (feeAmt == null) {
            throw new IllegalArgumentException("手续费计算异常");
        }
        return new Money(feeAmt).getAmount();
    }
}
