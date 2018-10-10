/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class HotelAnalysisVO {
	String hotel_id;
	String month;
	double total_earnings;
	String colorcode;
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public double getTotal_earnings() {
		return total_earnings;
	}
	public void setTotal_earnings(double total_earnings) {
		this.total_earnings = total_earnings;
	}
	public String getColorcode() {
		return colorcode;
	}
	public void setColorcode(String colorcode) {
		this.colorcode = colorcode;
	}
	@Override
	public String toString() {
		return "HotelAnalysisVO [hotel_id=" + hotel_id + ", month=" + month
				+ ", total_earnings=" + total_earnings + ", colorcode="
				+ colorcode + "]";
	}
}
