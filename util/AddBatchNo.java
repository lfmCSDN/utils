package com.sunyard.frameworkset.plugin.tsp.manager.utils;

import com.sunyard.frameworkset.plugin.tsp.manager.entity.PlanInstParam;
import com.sunyard.frameworkset.plugin.tsp.manager.entity.PlanInstance;
import com.sunyard.frameworkset.plugin.tsp.spi.entity.job.inst.JobInst;
import com.sunyard.frameworkset.plugin.tsp.spi.entity.job.inst.StoreProJobInst;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class AddBatchNo {

	/**
	 * 替换参数
	 * 
	 * @param batchNo
	 * @param bji
	 * @param timeFormat
	 * @param planInstance
	 * @return
	 */
	public static JobInst isNeedBatchNo(String batchNo, JobInst bji,String timeFormat, PlanInstance planInstance) {
		String runParams = bji.getRunParams();
		if (runParams != null) {
			 
			if(StringUtils.isEmpty (timeFormat)){
				timeFormat="yyyy-MM-dd";
			}
			//如果批次号为NULL,并且执行存储过程不传入参数（修改人：李冲  时间:2016-12-01)
 			if(StringUtils.isEmpty (batchNo) && (bji instanceof StoreProJobInst)){
				runParams = null;	
			}else{
				String batchNo1="";
 				if(DateUtil.isValidDate(batchNo, "yyyy-MM-dd")){
					batchNo1= DateUtil.format(batchNo, timeFormat);
				}else{
					batchNo1=DateUtil.getCurrDate("yyyy-MM-dd");
					batchNo1= DateUtil.format(batchNo1, timeFormat);
				}
  				runParams = runParams.replace("${workdate}", batchNo1);
			}
			
			List<PlanInstParam> params = planInstance.getParams();
			for (PlanInstParam planInstParam : params) {
				runParams = runParams.replace("${"+planInstParam.getParamName()+"}", planInstParam.getParamValue()==null?"":planInstParam.getParamValue());
			}
			bji.setRunParams(runParams);
		}
		return bji;
	}
}
