package com.restonza.vo;

public class BarOrderResponse {
	
	String barItemName;
	String id;
	String qty;
	double price;
	String orderStatus;
	String updatedTime;
	
	public BarOrderResponse(String barItemName, String id, String qty,
			double price, String orderStatus, String updatedTime) {
		super();
		this.barItemName = barItemName;
		this.id = id;
		this.qty = qty;
		this.price = price;
		this.orderStatus = orderStatus;
		this.updatedTime = updatedTime;
	}
	
	public BarOrderResponse() {
	}
	
	public String getBarItemName() {
		return barItemName;
	}
	public void setBarItemName(String barItemName) {
		this.barItemName = barItemName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

}
