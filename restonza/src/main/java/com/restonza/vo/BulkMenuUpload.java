/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class BulkMenuUpload {
	String hotel_id;
	String dish_name;
	String dish_category;
	String dish_description;
	String dish_price;
	String calories;
	String discount;
	String avg_cooking_time;
	String hot;
	String ingedients;
	int sequence;
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
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
	public String getAvg_cooking_time() {
		return avg_cooking_time;
	}
	public void setAvg_cooking_time(String avg_cooking_time) {
		this.avg_cooking_time = avg_cooking_time;
	}
	public String getHot() {
		return hot;
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	public String getIngedients() {
		return ingedients;
	}
	public void setIngedients(String ingedients) {
		this.ingedients = ingedients;
	}
	public int getSeuence() {
		return sequence;
	}
	public void setSeuence(int seuence) {
		this.sequence = seuence;
	}
	@Override
	public String toString() {
		return "BulkMenuUpload [hotel_id=" + hotel_id + ", dish_name="
				+ dish_name + ", dish_category=" + dish_category
				+ ", dish_description=" + dish_description + ", dish_price="
				+ dish_price + ", calories=" + calories + ", discount="
				+ discount + ", avg_cooking_time=" + avg_cooking_time
				+ ", hot=" + hot + ", ingedients=" + ingedients + "]";
	}
}
