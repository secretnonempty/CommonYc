package com.rails.ecommerce.admin.api.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.rails.ecommerce.core.publish.domain.PublishApi;
/**
 * 
 *  Class Name: ApiFrom.java
 *  Function:
 *  
 *  Modifications:   
 *  
 *  @author allen  DateTime 2015-2-4 下午2:21:00    
 *  @version 1.0 
 *  @param <T>
 */
@XmlRootElement
public class ApiFrom implements Serializable {
	private static final long serialVersionUID = -7570418543059865561L;
	
	private String status;
	private String msg;

	private List<PublishApi> returnList = new ArrayList<PublishApi>();
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;	
	}
	public List<PublishApi> getReturnList() {
		return returnList;
	}
	public void setReturnList(List<PublishApi> returnList) {
		this.returnList = returnList;
	}
	
	
}
