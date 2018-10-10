/**
 * 
 */
package com.restonza.vo;

import java.util.List;

/**
 * @author flex-grow developers
 *
 */
public class OrderDetailsVO {
	private String hotel_id;
	private String order_id;
	private String table_id;
	private String customer_id;
	private String estimated_time;
	private String instruction;
	private double amt;
	private String status;
	private String food_order_ids;
	private String bar_order_ids;
	private List<PlacedOrderDetailsVO> orderDetails; 
	private String bar_order_count;
	private String food_bill_amt;
	private String bar_bill_amt;
	private Double totalAmountWOTaxDiscount;
	private String emailContent;
	private int numberOfPeopleSittingOnTable;
	
	
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
	public String getEstimated_time() {
		return estimated_time;
	}
	public void setEstimated_time(String estimated_time) {
		this.estimated_time = estimated_time;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public List<PlacedOrderDetailsVO> getOrderDetails() {
		return orderDetails;
	}
	public void setOrderDetails(List<PlacedOrderDetailsVO> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public String iterateOrderDetails(List<PlacedOrderDetailsVO> orderDetails) {
		StringBuilder iterateOrderDetails = new StringBuilder();
		for (PlacedOrderDetailsVO placedOrderDetailsVO : orderDetails) {
			iterateOrderDetails.append(placedOrderDetailsVO.toString());
			iterateOrderDetails.append("|");
		}
		return iterateOrderDetails.toString();
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getAmt() {
		return amt;
	}
	public void setAmt(double amt) {
		this.amt = amt;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
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
	public String getBar_order_count() {
		return bar_order_count;
	}
	public void setBar_order_count(String bar_order_count) {
		this.bar_order_count = bar_order_count;
	}
	public String getFood_bill_amt() {
		return food_bill_amt;
	}
	public void setFood_bill_amt(String food_bill_amt) {
		this.food_bill_amt = food_bill_amt;
	}
	public String getBar_bill_amt() {
		return bar_bill_amt;
	}
	public void setBar_bill_amt(String bar_bill_amt) {
		this.bar_bill_amt = bar_bill_amt;
	}
	public Double getTotalAmountWOTaxDiscount() {
		return totalAmountWOTaxDiscount;
	}
	public void setTotalAmountWOTaxDiscount(Double totalAmountWOTaxDiscount) {
		this.totalAmountWOTaxDiscount = totalAmountWOTaxDiscount;
	}
	
	public String getEmailContent() {
		return emailContent;
	}
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}
	
	public int getNumberOfPeopleSittingOnTable() {
		return numberOfPeopleSittingOnTable;
	}
	public void setNumberOfPeopleSittingOnTable(int numberOfPeopleSittingOnTable) {
		this.numberOfPeopleSittingOnTable = numberOfPeopleSittingOnTable;
	}
	@Override
	public String toString() {
		return "OrderDetailsVO [hotel_id=" + hotel_id + ", order_id="
				+ order_id + ", table_id=" + table_id + ", customer_id="
				+ customer_id + ", estimated_time=" + estimated_time
				+ ", instruction=" + instruction + ", amt=" + amt + ", status="
				+ status + ", orderDetails=" + orderDetails + "]";
	}
	
	
}
