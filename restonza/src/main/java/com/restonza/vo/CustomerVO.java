package com.restonza.vo;


public class CustomerVO {
	
	private String customer_id;
	
	private String first_name;
	
	private String last_name;
	
	private String deviceid;
	
	private String emailid;
	
	private String phonenumber;
	
	private String password;

	public CustomerVO(){
		
	}
	
	
	public CustomerVO(String customer_id, String first_name, String last_name, String deviceid) {
		this.customer_id = customer_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.deviceid = deviceid;
	}

	
	
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
