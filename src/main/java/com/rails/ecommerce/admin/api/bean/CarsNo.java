package com.rails.ecommerce.admin.api.bean;

import java.io.Serializable;

 /**
 *  Class Name: CarsNo.java
 *  Function:订单表
 *  
 *  Modifications:   
 *  
 *  @author gxl  DateTime 2015-1-9 上午10:08:46    
 *  @version 1.0 
 */
public class CarsNo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String JLCBH;
	
	private String CNBH;

	private String YT;

	public String getJLCBH() {
		return JLCBH;
	}

	public void setJLCBH(String jLCBH) {
		JLCBH = jLCBH;
	}

	public String getCNBH() {
		return CNBH;
	}

	public void setCNBH(String cNBH) {
		CNBH = cNBH;
	}

	public String getYT() {
		return YT;
	}

	public void setYT(String yT) {
		YT = yT;
	}
	
	
}
