/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class GenerateBillVO extends AllOrderVO {
	private String food_order_ids;
	private String bar_order_ids;
	private String barordercount;
	private String foodbillamt;
	private String barbillamt;
	private String onlinePayment;
	
	public String getBarordercount() {
		return barordercount;
	}
	public void setBarordercount(String barordercount) {
		this.barordercount = barordercount;
	}
	public String getFood_order_ids() {
		return food_order_ids;
	}
	public void setFood_order_ids(String food_order_ids) {
		this.food_order_ids = food_order_ids;
	}
	public String getBar_order_ids() {
		return bar_order_ids;
	}
	public void setBar_order_ids(String bar_order_ids) {
		this.bar_order_ids = bar_order_ids;
	}
	
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
	public String getOnlinePayment() {
		return onlinePayment;
	}
	public void setOnlinePayment(String onlinePayment) {
		this.onlinePayment = onlinePayment;
	}
}
