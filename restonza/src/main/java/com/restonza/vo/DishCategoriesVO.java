package com.restonza.vo;

import java.util.List;

/**
 * @author flex-grow developers
 *
 */

public class DishCategoriesVO {
	
	private String category_id;
	
	private String category_name;
	
	private String status;
	
	private String hotel_id;
	
	private String sequence;
	
	private List<String> updatedSequence;
	
	private String imageURI;
	
	public DishCategoriesVO() {
		super();
	}

	
	public DishCategoriesVO(String category_id, String category_name,
			String status, String hotel_id,String sequnce,String imgURI) {
		this.category_id = category_id;
		this.category_name = category_name;
		this.status = status;
		this.hotel_id = hotel_id;
		this.sequence = sequnce;
		this.imageURI = imgURI;
	}


	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHotel_id() {
		return hotel_id;
	}

	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}


	public List<String> getUpdatedSequence() {
		return updatedSequence;
	}

	public void setUpdatedSequence(List<String> updatedSequence) {
		this.updatedSequence = updatedSequence;
	}


	public String getImageURI() {
		return imageURI;
	}

	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}
	
}
