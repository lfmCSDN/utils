package com.os.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 产生随机唯一字符串工具类
 */
public final class UuidUtil {

	/**
	 * 不允许new出工具类
	 */
	private UuidUtil(){}

	/**
	 * 产生随机唯一字符串
	 */
	public static String getUuid(){
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 产生订单号
	 */
	public static int getOrderOID(){
		SimpleDateFormat format = new SimpleDateFormat("HHmmssSSS");
		String str = format.format(new Date());
		return Integer.parseInt(str);
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		System.out.println(getOrderOID());
	}
	
}
