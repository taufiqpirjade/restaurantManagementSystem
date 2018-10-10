/**
 * 
 */
package com.restonza.vo;

import java.util.Date;

/**
 * @author flex-grow developers
 *
 */
public class AllOrderVO {
	String tableid;
	String customerid;
	String orderids;
	String ordersummaries;
	String formatOrderSummary;
	double sum;
	String hotelname;
	String billeddate;
	String foodordersummaries;
	String barordersummaries;
	String formatBarOrderSummary;
	/**
	 * 
	 */
	public AllOrderVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param tableid
	 * @param customerid
	 * @param orderids
	 * @param ordersummaries
	 * @param formatOrderSummary
	 * @param sum
	 * @param hotelname
	 */
	public AllOrderVO(String tableid, String customerid, String orderids,
			String ordersummaries, String formatOrderSummary, double sum,
			String hotelname) {
		this.tableid = tableid;
		this.customerid = customerid;
		this.orderids = orderids;
		this.ordersummaries = ordersummaries;
		this.formatOrderSummary = formatOrderSummary;
		this.sum = sum;
		this.hotelname = hotelname;
	}


	/**
	 * @param orderids
	 * @param ordersummaries
	 * @param sum
	 * @param hotelname
	 */
	public AllOrderVO(String orderids, String ordersummaries, int sum,
			String hotelname) {
		this.orderids = orderids;
		this.ordersummaries = ordersummaries;
		this.sum = sum;
		this.hotelname = hotelname;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	public String getOrderids() {
		return orderids;
	}
	public void setOrderids(String orderids) {
		this.orderids = orderids;
	}
	public String getOrdersummaries() {
		return ordersummaries;
	}
	public void setOrdersummaries(String ordersummaries) {
		this.ordersummaries = ordersummaries;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	public String getFormatOrderSummary() {
		return formatOrderSummary;
	}
	public void setFormatOrderSummary(String formatOrderSummary) {
		this.formatOrderSummary = formatOrderSummary;
	}
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public String getBilleddate() {
		return billeddate;
	}

	public String getFoodordersummaries() {
		return foodordersummaries;
	}

	public void setFoodordersummaries(String foodordersummaries) {
		this.foodordersummaries = foodordersummaries;
	}

	public String getBarordersummaries() {
		return barordersummaries;
	}

	public void setBarordersummaries(String barordersummaries) {
		this.barordersummaries = barordersummaries;
	}

	public void setBilleddate(String billeddate) {
		this.billeddate = billeddate;
	}

	public String getFormatBarOrderSummary() {
		return formatBarOrderSummary;
	}

	public void setFormatBarOrderSummary(String formatBarOrderSummary) {
		this.formatBarOrderSummary = formatBarOrderSummary;
	}

	@Override
	public String toString() {
		return "AllOrderVO [tableid=" + tableid + ", customerid=" + customerid
				+ ", orderids=" + orderids + ", ordersummaries="
				+ ordersummaries + ", formatOrderSummary=" + formatOrderSummary
				+ ", sum=" + sum + ", hotelname=" + hotelname + "]";
	}
}
