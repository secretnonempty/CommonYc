package com.rails.ecommerce.admin.api.bean;

import java.io.Serializable;

 /**
 *  Class Name: UiDataItem.java
 *  Function:订单表
 *  
 *  Modifications:   
 *  
 *  @author gxl  DateTime 2015-1-9 上午10:08:46    
 *  @version 1.0 
 */
public class UiDataItem implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String Yyrq;
	
	private String Xnsd;
	
	private int SL;
	
	private boolean IsBpked;
	
	private String YyClInfo;

	public String getYyrq() {
		return Yyrq;
	}

	public void setYyrq(String yyrq) {
		Yyrq = yyrq;
	}

	public String getXnsd() {
		return Xnsd;
	}

	public void setXnsd(String xnsd) {
		Xnsd = xnsd;
	}

	public int getSL() {
		return SL;
	}

	public void setSL(int sL) {
		SL = sL;
	}

	public boolean isIsBpked() {
		return IsBpked;
	}

	public void setIsBpked(boolean isBpked) {
		IsBpked = isBpked;
	}

	public String getYyClInfo() {
		return YyClInfo;
	}

	public void setYyClInfo(String yyClInfo) {
		YyClInfo = yyClInfo;
	}

	
}
