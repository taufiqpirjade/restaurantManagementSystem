package com.restonza.vo;


public class CustomerCallVO {
	private String call_type;
	
	private String called_customer_id;
	
	private String qrcode;

	public String getCall_type() {
		return call_type;
	}

	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}

	public String getCalled_customer_id() {
		return called_customer_id;
	}

	public void setCalled_customer_id(String called_customer_id) {
		this.called_customer_id = called_customer_id;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
}
