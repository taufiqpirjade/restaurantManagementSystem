/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class PlacedOrderDetailsVO {
	private int id;
	private String dishname;
	private String qty;
	private String price;
	private String dishimg;
	
	
	public String getDishimg() {
		return dishimg;
	}
	public void setDishimg(String dishimg) {
		this.dishimg = dishimg;
	}
	public String getDishname() {
		return dishname;
	}
	public void setDishname(String dishname) {
		this.dishname = dishname;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	@Override
	public String toString() {
		return " dishname=" + dishname + ",qty=" + qty;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
