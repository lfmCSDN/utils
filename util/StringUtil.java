package com.sunyard.frameworkset.plugin.tsp.manager.utils;

import java.util.UUID;

public class StringUtil {
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}


