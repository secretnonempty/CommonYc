package com.rails.ecommerce.admin.api.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.rails.ecommerce.core.common.domain.TrainNumberView;
import com.rails.ecommerce.core.user.domain.User;

/**
 * 
 *  Class Name: UserApiForm.java
 *  Function:
 *  
 *  Modifications:   
 *  
 *  @author allen  DateTime 2015-2-4 下午3:37:47    
 *  @version 1.0
 */
@XmlRootElement
public class UserApiForm implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1948786892837679470L;
	private String status;
	private String msg;

	private List<User> returnList = new ArrayList<User>();
	private List<TrainNumberView> returnList1 = new ArrayList<TrainNumberView>();
	
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
	public List<User> getReturnList() {
		return returnList;
	}
	public void setReturnList(List<User> returnList) {
		this.returnList = returnList;
	}
	public List<TrainNumberView> getReturnList1() {
		return returnList1;
	}
	public void setReturnList1(List<TrainNumberView> returnList1) {
		this.returnList1 = returnList1;
	}
	
}
