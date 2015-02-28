package com.rails.ecommerce.admin.api.bean;

import java.io.Serializable;

 /**
 *  Class Name: OrderInfo.java
 *  Function:订单表
 *  
 *  Modifications:   
 *  
 *  @author gxl  DateTime 2015-1-9 上午10:08:46    
 *  @version 1.0 
 */
public class StudentInfoFormNode implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private StudentInfoForm data = new StudentInfoForm();
	
	private int code;
	
	private String message;

	public StudentInfoForm getData() {
		return data;
	}

	public void setData(StudentInfoForm data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
