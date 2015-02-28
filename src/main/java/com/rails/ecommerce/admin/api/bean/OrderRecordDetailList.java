package com.rails.ecommerce.admin.api.bean;

import java.io.Serializable;

 /**
 *  Class Name: OrderRecordDetailList.java
 *  Function:订单表
 *  
 *  Modifications:   
 *  
 *  @author gxl  DateTime 2015-1-9 上午10:08:46    
 *  @version 1.0 
 */
public class OrderRecordDetailList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String XYBH;
	
	private String YYRQ;
	
	private String YYSD;

	private String XXZH;
	
	private String KSSJ;
	
	private String JSSJ;
	
	private String SFXL;
	
	private String CNBH;
	
	private String DTNAME;

	private String XNSD;
	
	private String XNSDNAME;
	
	private String JLCBH;

	public String getXYBH() {
		return XYBH;
	}

	public void setXYBH(String xYBH) {
		XYBH = xYBH;
	}

	public String getYYRQ() {
		return YYRQ;
	}

	public void setYYRQ(String yYRQ) {
		YYRQ = yYRQ;
	}

	public String getYYSD() {
		return YYSD;
	}

	public void setYYSD(String yYSD) {
		YYSD = yYSD;
	}

	public String getXXZH() {
		return XXZH;
	}

	public void setXXZH(String xXZH) {
		XXZH = xXZH;
	}

	public String getKSSJ() {
		return KSSJ;
	}

	public void setKSSJ(String kSSJ) {
		KSSJ = kSSJ;
	}

	public String getJSSJ() {
		return JSSJ;
	}

	public void setJSSJ(String jSSJ) {
		JSSJ = jSSJ;
	}

	public String getSFXL() {
		return SFXL;
	}

	public void setSFXL(String sFXL) {
		SFXL = sFXL;
	}

	public String getCNBH() {
		return CNBH;
	}

	public void setCNBH(String cNBH) {
		CNBH = cNBH;
	}

	public String getDTNAME() {
		return DTNAME;
	}

	public void setDTNAME(String dTNAME) {
		DTNAME = dTNAME;
	}

	public String getXNSD() {
		return XNSD;
	}

	public void setXNSD(String xNSD) {
		XNSD = xNSD;
	}

	public String getXNSDNAME() {
		return XNSDNAME;
	}

	public void setXNSDNAME(String xNSDNAME) {
		XNSDNAME = xNSDNAME;
	}

	public String getJLCBH() {
		return JLCBH;
	}

	public void setJLCBH(String jLCBH) {
		JLCBH = jLCBH;
	}
	
}
