package com.sunyard.frameworkset.plugin.tsp.manager.utils;


import com.sunyard.frameworkset.plugin.tsp.manager.core.annotation.JobDesc;
import com.sunyard.frameworkset.plugin.tsp.manager.entity.jobconfig.JobConfig;
import com.sunyard.frameworkset.plugin.tsp.spi.exception.TaskSchedulingPlatformException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class JobPropertyIsNullAble {
	
   public static void jobPropertyIsNullAble(JobConfig jobConfig,String type) throws Exception{
	   
	   //获取子类的所有的属性
	   Class clazzSub=JobConfig.getJobConfigClassByType(type);
	   Field[] fieldsSub = clazzSub.getDeclaredFields();
	   for(Field field:fieldsSub){
		   JobDesc job = field.getAnnotation(JobDesc.class);
		   if(job!=null){
				field.setAccessible(true);
				boolean  flag=job.isNullAble();
				PropertyDescriptor pd=new PropertyDescriptor(field.getName(),clazzSub);
				Method getMethod =pd.getReadMethod();
				Object o=getMethod.invoke(jobConfig);
				if(flag==false){
					if(o==null||"".equals(o)){
					TaskSchedulingPlatformException tspe=new TaskSchedulingPlatformException();
					 tspe.setSeriousness(TaskSchedulingPlatformException.ERROR);
					 tspe.setBriefDescription("配置"+type+"类型作业时出错"+"作业类型中参数《"+field.getName()+"》不能为空");
					 throw tspe;
					}
				}
		   }
	   }
	   
	   //获取父类中所有的属性
	   Class clazzSuper=clazzSub.getSuperclass();
	   Field[] fieldsSuper = clazzSuper.getDeclaredFields();
	   for(Field field:fieldsSuper){
		   JobDesc job = field.getAnnotation(JobDesc.class); 
		   if(job!=null){
				field.setAccessible(true);
				boolean  flag=job.isNullAble();
				String str=field.getName();
				PropertyDescriptor pd=new PropertyDescriptor(str,clazzSuper);
				Method getMethod =pd.getReadMethod();
				Object o=getMethod.invoke(jobConfig);
				if(flag==false){
					if(o==null||"".equals(o)){
						TaskSchedulingPlatformException tspe=new TaskSchedulingPlatformException();
						  tspe.setSeriousness(TaskSchedulingPlatformException.ERROR);
						  tspe.setBriefDescription("配置"+type+"类型作业时出错"+"作业类型中参数《"+field.getName()+"》不能为空");
						  throw tspe;
					}
				}
		   }
	   }
	   
   }
}
