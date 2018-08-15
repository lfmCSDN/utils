package com.sunyard.frameworkset.plugin.tsp.manager.utils;

import com.sunyard.frameworkset.plugin.tsp.manager.entity.jobconfig.JobConfig;
import org.dom4j.Element;


public class ElementToJobConfig {
   public static JobConfig getJobConfigByElement(Element element,String type) throws Exception{
	  
	   JobConfig jobConfig=JobConfig.getJobConfigClassByType(type).newInstance();
		
		String name=element.attribute("name").getValue();
		String runParams=element.attribute("runParams").getValue();
		String ignoreErr=element.attribute("ignoreErr").getValue();
		Integer retryCnt=Integer.parseInt(element.attribute("retryCnt").getValue());
		Integer retrySec=Integer.parseInt(element.attribute("retrySec").getValue());
		String createTime=DateUtil.getCurrDate("yyyy-MM-dd HH:mm:ss");
		String timeFormat=element.attribute("timeFormat").getValue();
		
		jobConfig.setName(name);
		jobConfig.setRunParams(runParams);
		jobConfig.setIgnoreErr(ignoreErr);
		jobConfig.setRetryCnt(retryCnt);
		jobConfig.setRetrySec(retrySec);
		jobConfig.setCreateTime(createTime);
		jobConfig.setTimeFormat(timeFormat);
	   return jobConfig;
   }
}
