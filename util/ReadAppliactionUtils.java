package com.sunyard.aps.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by chenjia on 2017/7/17.
 *
 * 章辉注释:
 * 本类原始是 陈佳创建
 * 这个类的用法明显是错误的，这里暂时只是 从 原aps-common-config 项目把类迁移过来
 * 这种读取配置的方式  可以采用@Value的方式 本方式太原始
 * 未来aps-common-config 项目即将删除
 */
public class ReadAppliactionUtils {
    public static String ReadOssServerUrl() {
        ReadAppliactionUtils readAppliactionUtils = new ReadAppliactionUtils();
        InputStream in = readAppliactionUtils.getClass().getResourceAsStream("/application.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            return prop.getProperty("oss.server.url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}

