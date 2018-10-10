/**
 * 
 */
package com.restonza.vo;

import java.util.List;


/**
 * @author flex-grow developers
 *
 */
public class OrderHistoryDetailsVO {
	String tableid;
	String customerid;
	String orderids;
	//String ordersummaries;
	String formatOrderSummary;
	double sum;
	String hotelname;
	String billeddate;
	double discount;
	double taxamount;
	String barOrderIds;
	String foodOrderIds;
	double liqourtax;
	List<Object> ordersummaries;
	
	/**
	 * 
	 */
	public OrderHistoryDetailsVO() {
		super();
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
	public OrderHistoryDetailsVO(String tableid, String customerid, String orderids,
			List<Object> ordersummaries, String formatOrderSummary, double sum,
			String hotelname,double discount, double taxamount, String barorderids,String foodorderids) {
		this.tableid = tableid;
		this.customerid = customerid;
		this.orderids = orderids;
		this.ordersummaries = ordersummaries;
		this.formatOrderSummary = formatOrderSummary;
		this.sum = sum;
		this.hotelname = hotelname;
		this.discount = discount;
		this.taxamount = taxamount;
		this.barOrderIds = barorderids;
		this.foodOrderIds = foodorderids;
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
	public List<Object> getOrdersummaries() {
		return ordersummaries;
	}
	public void setOrdersummaries(List<Object> ordersummaries) {
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


	public void setBilleddate(String billeddate) {
		this.billeddate = billeddate;
	}


	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(double taxamount) {
		this.taxamount = taxamount;
	}
	public String getBarOrderIds() {
		return barOrderIds;
	}

	public void setBarOrderIds(String barOrderIds) {
		this.barOrderIds = barOrderIds;
	}

	public String getFoodOrderIds() {
		return foodOrderIds;
	}

	public void setFoodOrderIds(String foodOrderIds) {
		this.foodOrderIds = foodOrderIds;
	}
	
	public double getLiqourtax() {
		return liqourtax;
	}

	public void setLiqourtax(double liqourtax) {
		this.liqourtax = liqourtax;
	}

	@Override
	public String toString() {
		return "AllOrderVO [tableid=" + tableid + ", customerid=" + customerid
				+ ", orderids=" + orderids + ", ordersummaries="
				+ ordersummaries + ", formatOrderSummary=" + formatOrderSummary
				+ ", sum=" + sum + ", hotelname=" + hotelname + "]";
	}
}
