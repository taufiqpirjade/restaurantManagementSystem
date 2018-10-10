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

import com.restonza.vo.TableVO;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "table_details")
public class TableDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	
	@NotNull
	private int table_id;
	
	@NotNull
	private int hotel_id;
	
	private String description;
	
	@NotNull
	private boolean status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTable_id() {
		return table_id;
	}

	public void setTable_id(int table_id) {
		this.table_id = table_id;
	}

	public int getHotel_id() {
		return hotel_id;
	}

	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public TableDetails prepareTableDetails(TableDetails aTableDetails, TableVO aTableVO) throws Exception {
		aTableDetails.setHotel_id(Integer.parseInt(aTableVO.getHotel_id()));
		aTableDetails.setDescription(aTableVO.getDescription());
		aTableDetails.setTable_id(Integer.parseInt(aTableVO.getTable_id()));
		String status = aTableVO.getStatus();
		if (status.equals("active")) {
			aTableDetails.setStatus(true);
		} else {
			aTableDetails.setStatus(false);
		}
		return aTableDetails;
	}
}
