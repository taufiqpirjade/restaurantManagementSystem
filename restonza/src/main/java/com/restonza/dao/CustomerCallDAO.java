package com.restonza.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "customer_call")
public class CustomerCallDAO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int call_id;
	
	@NotNull
	private String call_type;
	
	@NotNull
	private String called_customer_id;
	
	@NotNull
	private String table_no;
	
	@NotNull
	private int cust_call_hotel_id;
	
	private String status;

	public int getCall_id() {
		return call_id;
	}

	public void setCall_id(int call_id) {
		this.call_id = call_id;
	}

	public String getCall_type() {
		return call_type;
	}

	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}

	public String getCalled_customer_id() {
		return called_customer_id;
	}

	public void setCalled_customer_id(String called_customer_id) {
		this.called_customer_id = called_customer_id;
	}

	public String getTable_no() {
		return table_no;
	}

	public void setTable_no(String table_no) {
		this.table_no = table_no;
	}

	public int getCust_call_hotel_id() {
		return cust_call_hotel_id;
	}

	public void setCust_call_hotel_id(int cust_call_hotel_id) {
		this.cust_call_hotel_id = cust_call_hotel_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
