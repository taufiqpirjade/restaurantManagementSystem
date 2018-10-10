/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.OrderDetailsVO;

/**
 * @author flex-grow developers
 * hbm mapping for order_details table
 * used to store order details
 */
@Entity
@Table(name = "order_details")
public class OrderDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private int hotel_id;
	@NotNull
	private int table_id;
	@NotNull
	private String customer_id;
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date ordered_on;
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_on;
	@NotNull
	private int estimated_time;
	@NotNull
	@Column(name = "actual_time", columnDefinition = "int default 0")
	private int actual_time;
	@NotNull
	private String status;
	@NotNull
	private String order_summary;
	@NotNull
	private double amount;
	@NotNull
	@Column(name = "instruction", columnDefinition = "VARCHAR(255) default none")
	private String instruction;
	
	@Transient
	private List<Object> orderItemList;
	
	@Transient
	private String customerName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}
	public int getTable_id() {
		return table_id;
	}
	public void setTable_id(int table_id) {
		this.table_id = table_id;
	}
	public Date getOrdered_on() {
		return ordered_on;
	}
	public void setOrdered_on(Date ordered_on) {
		this.ordered_on = ordered_on;
	}
	public Date getUpdated_on() {
		return updated_on;
	}
	public void setUpdated_on(Date updated_on) {
		this.updated_on = updated_on;
	}
	public int getEstimated_time() {
		return estimated_time;
	}
	public void setEstimated_time(int estimated_time) {
		this.estimated_time = estimated_time;
	}
	public int getActual_time() {
		return actual_time;
	}
	public void setActual_time(int actual_time) {
		this.actual_time = actual_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getOrder_summary() {
		return order_summary;
	}
	public void setOrder_summary(String order_summary) {
		this.order_summary = order_summary;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
	public List<Object> getList() {
		return orderItemList;
	}
	public void setList(List<Object> list) {
		this.orderItemList = list;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public OrderDetails prepareOrderDetails(OrderDetails aOrderDetails, OrderDetailsVO aOrderDetailsVO ) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		aOrderDetails.setHotel_id(Integer.parseInt(aOrderDetailsVO.getHotel_id()));
		aOrderDetails.setTable_id(Integer.parseInt(aOrderDetailsVO.getTable_id()));
		aOrderDetails.setCustomer_id(aOrderDetailsVO.getCustomer_id());
		aOrderDetails.setOrdered_on(sdf.parse(sdf.format(new Date())));
		aOrderDetails.setUpdated_on(sdf.parse(sdf.format(new Date())));
		aOrderDetails.setEstimated_time(Integer.parseInt(aOrderDetailsVO.getEstimated_time()));
		aOrderDetails.setOrder_summary(aOrderDetailsVO.iterateOrderDetails(aOrderDetailsVO.getOrderDetails()));
		aOrderDetails.setInstruction(aOrderDetailsVO.getInstruction());
		aOrderDetails.setAmount(aOrderDetailsVO.getAmt());
		aOrderDetails.setStatus(RESTONZACONSTANTS.new_order.toString());
		return aOrderDetails;
	}
}
