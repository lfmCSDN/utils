package com.os.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author 易宝在线支付
 * 易宝在线支付提供的支付加密工具
 */
public final class DigestUtil {
	
	/**
	 * 加密源数据
	 * @see 这是针对多条字符串（即数组）进行加密的方法。它会把数组元素拼成新字符串，然后再加密
	 * @see 本文暂未用到该方法
	 * @param aValue 加密的原文，即源数据
	 * @param aKey   密钥
	 */
	public static String getHmac(String[] args, String key) {
		if (args == null || args.length == 0) {
			return (null);
		}
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]);
		}
		return (hmacSign(str.toString(), key));
	}
	
	/**
	 * 加密源数据
	 * @see 这是针对一条字符串进行加密的方法
	 * @param aValue 加密的原文，即源数据
	 * @param aKey   密钥
	 */
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes("UTF-8");
			value = aValue.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String toHex(byte input[]) {
		if (input == null){
			return null;
		}
		StringBuffer output = new StringBuffer(input.length * 2);
		for(int i=0; i<input.length; i++){
			int current = input[i] & 0xff;
			if (current<16){
				output.append("0");
			}
			output.append(Integer.toString(current, 16));
		}
		return output.toString();
	}

}
