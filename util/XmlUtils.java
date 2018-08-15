package com.sunyard.aps.common.utils;


import com.sunyard.aps.common.exception.BizException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Writer;

/**
 * Created by zhangxin on 2017/8/29.
 */
@Component
public class XmlUtils {
	
	private static Logger log = LoggerFactory.getLogger(XmlUtils.class);

	public static <T> String toXml(T obj) throws BizException {
		String xml = null;
		try {
			//XStream xstream =  new XStream(new DomDriver("UTF-8"));
			//XStream双下划线问题解决与CDATA标记同时的方案 zhanghui
			XStream xstream =new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_")));
			xstream.processAnnotations(obj.getClass());
			xml = xstream.toXML(obj);
			xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+System.getProperty("line.separator", "\n")+xml;
		} catch (Exception e) {
			throw new BizException("编码服务器数据错误",e);
		}
		log.info(xml);
		return xml;
	}

	public static  <T> T toBean(String xmlStr, Class<T> cls) throws BizException {
		log.info(xmlStr);
		try {
			XStream xstream = new XStream();
			xstream.ignoreUnknownElements();
			xstream.processAnnotations(cls);
			return (T) xstream.fromXML(xmlStr);
		} catch (Exception e) {
			throw new BizException("解析服务器数据错误",e);
		}
	}

	public static <T>Boolean isXmlStr(String xmlStr,Class<T> clazz){
		try {
			XStream xstream = new XStream();
			xstream.ignoreUnknownElements();
			xstream.processAnnotations(clazz);
			xstream.fromXML(xmlStr);
			return true;
		}catch (Exception e){
			return false;
		}
	}
}
