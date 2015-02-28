package com.rails.ecommerce.admin.api.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

 /**
 *  Class Name: OrderInfo.java
 *  Function:订单表
 *  
 *  Modifications:   
 *  
 *  @author gxl  DateTime 2015-1-9 上午10:08:46    
 *  @version 1.0 
 */
public class UserInfoForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String userName;
	
	private String phoneNum;
	
	private String nickName;
	
	private String Id;
	
	@JsonIgnore
	private String os;
	
	private String email;
	
	private String password;

	private String authemail;
	
	private String xxzh;
	
	private String jgid;
	
	private String webapiurl;
	
	private String xybh;
	
	private String sfzh;
	
	private String jxcode;
	
	private String schoolpwd;
	
	private String iconpath;
	
	private String username;
	
	private String phonenum;
	
	private String nickname;
	
	private String qquid;
	
	private String sinauid;
	
	private String apiurl;
	
	private String jxmc;
	
	private String xm;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthemail() {
		return authemail;
	}

	public void setAuthemail(String authemail) {
		this.authemail = authemail;
	}

	public String getXxzh() {
		return xxzh;
	}

	public void setXxzh(String xxzh) {
		this.xxzh = xxzh;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getWebapiurl() {
		return webapiurl;
	}

	public void setWebapiurl(String webapiurl) {
		this.webapiurl = webapiurl;
	}

	public String getXybh() {
		return xybh;
	}

	public void setXybh(String xybh) {
		this.xybh = xybh;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getJxcode() {
		return jxcode;
	}

	public void setJxcode(String jxcode) {
		this.jxcode = jxcode;
	}

	public String getSchoolpwd() {
		return schoolpwd;
	}

	public void setSchoolpwd(String schoolpwd) {
		this.schoolpwd = schoolpwd;
	}

	public String getIconpath() {
		return iconpath;
	}

	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getQquid() {
		return qquid;
	}

	public void setQquid(String qquid) {
		this.qquid = qquid;
	}

	public String getSinauid() {
		return sinauid;
	}

	public void setSinauid(String sinauid) {
		this.sinauid = sinauid;
	}

	public String getApiurl() {
		return apiurl;
	}

	public void setApiurl(String apiurl) {
		this.apiurl = apiurl;
	}

	public String getJxmc() {
		return jxmc;
	}

	public void setJxmc(String jxmc) {
		this.jxmc = jxmc;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}


}
