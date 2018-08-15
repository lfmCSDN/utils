package com.sunyard.aps.common.utils;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chenjia on 2017/9/8.
 */
public class StreamUtil {
    /**
     * 流转化为字符串
     *
     * @param inputStream
     * @return
     */
    public static String streamToStr(InputStream inputStream) {
        if (inputStream != null) {
            try {
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer tStringBuffer = new StringBuffer();
                String sTempOneLine = new String("");
                while ((sTempOneLine = tBufferedReader.readLine()) != null) {
                    tStringBuffer.append(sTempOneLine);
                }
                return tStringBuffer.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字符串转化为流
     *
     * @param str
     * @return
     */
    public static ByteArrayInputStream strToStream(String str) {
        if (StringUtils.isNotBlank(str)) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(str.getBytes());
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
