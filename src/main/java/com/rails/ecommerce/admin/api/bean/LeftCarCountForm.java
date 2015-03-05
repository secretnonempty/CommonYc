package com.rails.ecommerce.admin.api.bean;

import java.io.Serializable;
import java.util.List;

 /**
 *  Class Name: LeftCarCountForm.java
 *  Function:订单表
 *  
 *  Modifications:   
 *  
 *  @author gxl  DateTime 2015-1-9 上午10:08:46    
 *  @version 1.0 
 */
public class LeftCarCountForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<XnsdList> XnsdList;
	
	private List<CarDate> YyrqList;
	
	private List<UiDataItem> UIDatas;

	public List<XnsdList> getXnsdList() {
		return XnsdList;
	}

	public void setXnsdList(List<XnsdList> xnsdList) {
		XnsdList = xnsdList;
	}

	public List<CarDate> getYyrqList() {
		return YyrqList;
	}

	public void setYyrqList(List<CarDate> yyrqList) {
		YyrqList = yyrqList;
	}

	public List<UiDataItem> getUIDatas() {
		return UIDatas;
	}

	public void setUIDatas(List<UiDataItem> uIDatas) {
		UIDatas = uIDatas;
	}

	
}
