package com.restonza.vo;


public class IngredientVO {

	private String ingredient_name;
	
	private String hotel_id;
	
	private String status;
	
	private int old_ingredient_id;

	public int getOld_ingredient_id() {
		return old_ingredient_id;
	}

	public void setOld_ingredient_id(int old_ingredient_id) {
		this.old_ingredient_id = old_ingredient_id;
	}

	public String getIngredient_name() {
		return ingredient_name;
	}

	public void setIngredient_name(String ingredient_name) {
		this.ingredient_name = ingredient_name;
	}

	public String getHotel_id() {
		return hotel_id;
	}

	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
