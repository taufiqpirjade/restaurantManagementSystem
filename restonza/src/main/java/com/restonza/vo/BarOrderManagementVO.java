/**
 * 
 */
package com.restonza.vo;

import java.util.List;

/**
 * @author flex-grow developers
 *
 */
public class BarOrderManagementVO {
	private int parent_order_id;
	private int hotel_id;
	private long customer_id;
	private int table_id;
	private String instruction;
	private String new_status;
	private String old_status;
	private String qrcode;
	private List<PlacedOrderDetailsVO> orderDetails;
	private int numberOfPeopleSittingOnTable;
	
	public int getParent_order_id() {
		return parent_order_id;
	}
	public void setParent_order_id(int parent_order_id) {
		this.parent_order_id = parent_order_id;
	}
	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}
	public long getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}
	public int getTable_id() {
		return table_id;
	}
	public void setTable_id(int table_id) {
		this.table_id = table_id;
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
	public String getNew_status() {
		return new_status;
	}
	public void setNew_status(String new_status) {
		this.new_status = new_status;
	}
	public String getOld_status() {
		return old_status;
	}
	public void setOld_status(String old_status) {
		this.old_status = old_status;
	}
	public String getQrcode() {
		return qrcode;
	}
	
	public int getNumberOfPeopleSittingOnTable() {
		return numberOfPeopleSittingOnTable;
	}
	public void setNumberOfPeopleSittingOnTable(int numberOfPeopleSittingOnTable) {
		this.numberOfPeopleSittingOnTable = numberOfPeopleSittingOnTable;
	}
	public void setQrcode(String qrcode) {
		String[] output = qrcode.split("-");
		String hotelId= output[0];
		String tableno = output[1];
		this.table_id = Integer.valueOf(tableno);
		this.hotel_id = Integer.valueOf(hotelId);
		this.qrcode = qrcode;
	}
	
}
