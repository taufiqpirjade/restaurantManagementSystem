/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class GetOrderDetailsVO {
	private Integer orderid;
	private String customer_id;
	private String customerName;
	private Integer hotel_id;
	private Integer table_id;
	private String orderdetails;
	private String instruction;
	private String formatorderinstruction;
	
	public GetOrderDetailsVO() {
		super();
	}
	
	public GetOrderDetailsVO(Integer orderid, String customer_id,
			Integer hotel_id, Integer table_id, String orderdetails, String instruction) {
		this.orderid = orderid;
		this.customer_id = customer_id;
		this.hotel_id = hotel_id;
		this.table_id = table_id;
		this.orderdetails = orderdetails;
		this.instruction = instruction;
	}
	
	public GetOrderDetailsVO(Integer orderid, String orderdetails, String formatorderinstruction, String customername) {
		this.orderid = orderid;
		this.orderdetails = orderdetails;
		this.formatorderinstruction = formatorderinstruction;
		this.customerName = customername;
	}

	public GetOrderDetailsVO(Integer orderid, String orderdetails) {
		this.orderid = orderid;
		this.orderdetails = orderdetails;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public Integer getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(Integer hotel_id) {
		this.hotel_id = hotel_id;
	}
	public Integer getTable_id() {
		return table_id;
	}
	public void setTable_id(Integer table_id) {
		this.table_id = table_id;
	}
	public Integer getOrderid() {
		return orderid;
	}
	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}
	public String getOrderdetails() {
		return orderdetails;
	}
	public void setOrderdetails(String orderdetails) {
		this.orderdetails = orderdetails;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public String getFormatorderinstruction() {
		return formatorderinstruction;
	}

	public void setFormatorderinstruction(String formatorderinstruction) {
		this.formatorderinstruction = formatorderinstruction;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Override
	public String toString() {
		return "GetOrderDetailsVO [orderid=" + orderid + ", customer_id="
				+ customer_id + ", hotel_id=" + hotel_id + ", table_id="
				+ table_id + ", orderdetails=" + orderdetails
				+ ", instruction=" + instruction + ", formatorderinstruction="
				+ formatorderinstruction + "]";
	}
}
