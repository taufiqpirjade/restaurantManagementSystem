/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 * 1-12-2017 Added changes for generic tax setting
 *
 */
public class TaxSettingVO {
	String id;
	String hotel_id;
	String taxname;
	String percentage;
	String status;
	String isliquor;
	
	public String getIsliquor() {
		return isliquor;
	}
	public void setIsliquor(String isliquor) {
		this.isliquor = isliquor;
	}
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaxname() {
		return taxname;
	}
	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "TaxSettingVO [id=" + id + ", hotel_id=" + hotel_id
				+ ", taxname=" + taxname + ", percentage=" + percentage + "]";
	}
	
}
