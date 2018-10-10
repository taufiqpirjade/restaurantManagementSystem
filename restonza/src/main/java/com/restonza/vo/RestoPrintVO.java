package com.restonza.vo;

import java.util.List;
import java.util.Map;

public class RestoPrintVO {
	private String hotel_id;
	private String table_id;
	private String order_id;
	private String amt;
	private Map<String,Double> taxes;
	private Map<String,Double> ltaxes;
	private List<PlacedOrderDetailsVO> orderDetails;
	private String foodbillamt;
	private String barbillamt;
	private String barordercount;
	
	public String getFoodbillamt() {
		return foodbillamt;
	}
	public void setFoodbillamt(String foodbillamt) {
		this.foodbillamt = foodbillamt;
	}
	public String getBarbillamt() {
		return barbillamt;
	}
	public void setBarbillamt(String barbillamt) {
		this.barbillamt = barbillamt;
	}
	public String getBarordercount() {
		return barordercount;
	}
	public void setBarordercount(String barordercount) {
		this.barordercount = barordercount;
	}
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getTable_id() {
		return table_id;
	}
	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public List<PlacedOrderDetailsVO> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<PlacedOrderDetailsVO> orderDetails) {
		this.orderDetails = orderDetails;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public Map<String, Double> getTaxes() {
		return taxes;
	}
	public void setTaxes(Map<String, Double> taxes) {
		this.taxes = taxes;
	}
	public Map<String,Double> getLtaxes() {
		return ltaxes;
	}
	public void setLtaxes(Map<String,Double> ltaxes) {
		this.ltaxes = ltaxes;
	}
}
