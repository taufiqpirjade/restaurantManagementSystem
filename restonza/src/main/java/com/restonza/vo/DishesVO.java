package com.restonza.vo;

import java.util.List;


public class DishesVO {

	private int dish_id;
	
	private String dish_name;
	
	private List<String> dish_category;
	
	private String dish_description;
	
	private String dish_price;
	
	private String calories;
	
	private String discount;
	
	private String avg_cookingtime;
	
	private byte[] image;
	
	private String imageURI;
	
	private String unit;
	
	private boolean hot;
	
	private String vegNonVeg;
	
	private String status;
	
	private int hotel;
	
	private List<String> updatedSequence;
	
	private List<String> ingredients;

	public int getDish_id() {
		return dish_id;
	}

	public void setDish_id(int dish_id) {
		this.dish_id = dish_id;
	}

	public String getDish_name() {
		return dish_name;
	}

	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}

	public List<String> getDish_category() {
		return dish_category;
	}

	public void setDish_category(List<String> dish_category) {
		this.dish_category = dish_category;
	}

	public String getDish_description() {
		return dish_description;
	}

	public void setDish_description(String dish_description) {
		this.dish_description = dish_description;
	}

	public String getDish_price() {
		return dish_price;
	}

	public void setDish_price(String dish_price) {
		this.dish_price = dish_price;
	}

	public String getCalories() {
		return calories;
	}

	public void setCalories(String calories) {
		this.calories = calories;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getAvg_cookingtime() {
		return avg_cookingtime;
	}

	public void setAvg_cookingtime(String avg_cookingtime) {
		this.avg_cookingtime = avg_cookingtime;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean isHot() {
		return hot;
	}

	public void setHot(boolean hot) {
		this.hot = hot;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getHotel() {
		return hotel;
	}

	public void setHotel(int hotel) {
		this.hotel = hotel;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
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

	public String getVegNonVeg() {
		return vegNonVeg;
	}

	public void setVegNonVeg(String vegNonVeg) {
		this.vegNonVeg = vegNonVeg;
	}
}
