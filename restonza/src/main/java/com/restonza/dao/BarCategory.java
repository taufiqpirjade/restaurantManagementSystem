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
 */
@Entity
@Table(name = "bar_category")
public class BarCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private String bar_category_name;
	
	private String bar_sub_category;
	@NotNull
	private int hotel_id;
	@NotNull
	private boolean status;
	private String img_uri;
	
	private int sequence;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBar_category_name() {
		return bar_category_name;
	}
	public void setBar_category_name(String bar_category_name) {
		this.bar_category_name = bar_category_name;
	}
	public String getBar_sub_category() {
		return bar_sub_category;
	}
	public void setBar_sub_category(String bar_sub_category) {
		this.bar_sub_category = bar_sub_category;
	}
	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getImg_uri() {
		return img_uri;
	}
	public void setImg_uri(String img_uri) {
		this.img_uri = img_uri;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
}
