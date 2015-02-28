package com.rails.ecommerce.admin.api.bean;

public class Result<T> implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private T data;

	private int code;

	private String message = "";

	public T getData() {
		return data;
	}
			
	public void setData(T data) {
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
