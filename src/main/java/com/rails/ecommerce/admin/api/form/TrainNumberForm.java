package com.rails.ecommerce.admin.api.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.rails.ecommerce.core.common.domain.TrainNumberView;


/**
 * 
 *  Class Name: TrainNumberForm.java
 *  Function:列车
 *  
 *  Modifications:   
 *  
 *  @author allen  DateTime 2015-2-6 下午4:05:11    
 *  @version 1.0
 */
@XmlRootElement
public class TrainNumberForm implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8155086201221751717L;
	private String status;
	private String msg;

	private List<TrainNumberView> returnList = new ArrayList<TrainNumberView>();

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

	public List<TrainNumberView> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<TrainNumberView> returnList) {
		this.returnList = returnList;
	}
	
	
}
