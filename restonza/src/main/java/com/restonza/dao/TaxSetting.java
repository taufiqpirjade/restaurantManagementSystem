/**
 * 
 */
package com.restonza.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author flex-grow developers
 * 
 * 1-12-2017 Added changes for generic tax setting
 *
 */
@Entity
@Table(name = "tax_setting")
public class TaxSetting {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	
	@NotNull
	private int hotel_id;
	
	@NotNull
	private String taxname;
	
	@NotNull
	private double percentage;
	
	@NotNull
	private boolean status;
	
	@NotNull
	private boolean isliquortax;
	
	
	public boolean isIsliquortax() {
		return isliquortax;
	}

	public void setIsliquortax(boolean isliquortax) {
		this.isliquortax = isliquortax;
	}

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

	public String getTaxname() {
		return taxname;
	}

	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TaxSetting [id=" + id + ", hotel_id=" + hotel_id + ", taxname="
				+ taxname + ", percentage=" + percentage + "]";
	}

}
