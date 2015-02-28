package com.rails.ecommerce.admin.api.trainNumber;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.rails.ecommerce.admin.api.form.TrainNumberForm;
import com.rails.ecommerce.admin.webservice.client.ClientImpl;
import com.rails.ecommerce.core.common.domain.TrainNumberView;

/**
 * 
 *  Class Name: TrainNumberApi.java
 *  Function:查询列车接口
 *  
 *  Modifications:   
 *  
 *  @author allen  DateTime 2015-2-6 下午4:01:31    
 *  @version 1.0
 */
@Component
@Path("trainNumber")
public class TrainNumberApi {
	@Path(value = "findTrainNumber")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public TrainNumberForm getUserLogin(@Context HttpServletRequest request) {
		TrainNumberForm trainNumberApiForm = new TrainNumberForm();
		String loginName = request.getParameter("loginName");
		List<TrainNumberView> trainNumberList = ClientImpl.getTrainNumber(loginName);
		if(trainNumberList.size()!=0){
			trainNumberApiForm.setStatus("0");
			trainNumberApiForm.setMsg("查询成功");
			trainNumberApiForm.setReturnList(trainNumberList);
		}else{
			trainNumberApiForm.setStatus("1");
			trainNumberApiForm.setMsg("无数据");
			trainNumberApiForm.setReturnList(trainNumberList);
		}
		return trainNumberApiForm;
    }
}
