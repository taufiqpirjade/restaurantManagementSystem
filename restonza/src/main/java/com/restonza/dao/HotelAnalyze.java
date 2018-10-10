/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "hotel_analyzer")
public class HotelAnalyze implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private String orderids;
	@NotNull
	private int hotel_id;
	@NotNull
	private String customer_id;
	@NotNull
	private String order_dish_name;
	@NotNull
	private int feedback;
	@NotNull
	private int qty;
	
	/**
	 * 
	 */
	public HotelAnalyze() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HotelAnalyze(String orderids, int hotel_id, String customer_id, String order_dish_name, int feedback, int qty) {
		this.orderids = orderids;
		this.hotel_id = hotel_id;
		this.customer_id = customer_id;
		this.order_dish_name = order_dish_name;
		this.feedback = feedback;
		this.qty = qty;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getOrderids() {
		return orderids;
	}

	public void setOrderids(String orderids) {
		this.orderids = orderids;
	}

	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getOrder_dish_name() {
		return order_dish_name;
	}
	public void setOrder_dish_name(String order_dish_name) {
		this.order_dish_name = order_dish_name;
	}
	public int getFeedback() {
		return feedback;
	}
	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
}
