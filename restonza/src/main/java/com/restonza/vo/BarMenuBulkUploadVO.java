/**
 * 
 */
package com.restonza.vo;


/**
 * @author flex-grow developers
 *
 */
public class BarMenuBulkUploadVO {
	private String bar_item_name;
	private int category_id;
	private String sub_category;
	private boolean servered_in_qty;
	private int hotel_id;
	private String qty;
	private double price;
	private boolean status;
	private String barimg;
	private int sequence;
	public String getBar_item_name() {
		return bar_item_name;
	}
	public void setBar_item_name(String bar_item_name) {
		this.bar_item_name = bar_item_name;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public String getSub_category() {
		return sub_category;
	}
	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}
	public boolean isServered_in_qty() {
		return servered_in_qty;
	}
	public void setServered_in_qty(boolean servered_in_qty) {
		this.servered_in_qty = servered_in_qty;
	}
	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getBarimg() {
		return barimg;
	}
	public void setBarimg(String barimg) {
		this.barimg = barimg;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
}
