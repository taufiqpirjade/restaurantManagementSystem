package com.restonza.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "hotel_dishes")
public class Dishes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int dish_id;
	
	@NotNull
	private String dish_name;
	
	@NotNull
	private String dish_category;
	
	@NotNull
	private String dish_description;
	
	@NotNull
	private double dish_price;
	
	private String calories;
	
	private String discount;
	
	private String avg_cookingtime;
	
	private String unit;
	
	private boolean hot;
	
	private boolean status;
	
	private int hotel;
	
	private String dish_ingredients;
	
	private int sequence;
	
	private String img_uri;

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

	public String getDish_category() {
		return dish_category;
	}

	public void setDish_category(String dish_category) {
		this.dish_category = dish_category;
	}

	public String getDish_description() {
		return dish_description;
	}

	public void setDish_description(String dish_description) {
		this.dish_description = dish_description;
	}

	public double getDish_price() {
		return dish_price;
	}

	public void setDish_price(double dish_price) {
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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getHotel() {
		return hotel;
	}

	public void setHotel(int hotel) {
		this.hotel = hotel;
	}

	public String getDish_ingredients() {
		return dish_ingredients;
	}

	public void setDish_ingredients(String dish_ingredients) {
		this.dish_ingredients = dish_ingredients;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getImg_uri() {
		return img_uri;
	}

	public void setImg_uri(String img_uri) {
		this.img_uri = img_uri;
	}
	
}
