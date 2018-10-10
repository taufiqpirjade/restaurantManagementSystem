/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class FeedBackVO {
	String orderids;
	String hotel_id;
	String customer_name;
	String comments;
	String rating;
	String servicerating;
	String ambiencerating;
	String foodrating;
	
	public String getOrderids() {
		return orderids;
	}
	public void setOrderids(String orderids) {
		this.orderids = orderids;
	}
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getServicerating() {
		return servicerating;
	}
	public void setServicerating(String servicerating) {
		this.servicerating = servicerating;
	}
	public String getAmbiencerating() {
		return ambiencerating;
	}
	public void setAmbiencerating(String ambiencerating) {
		this.ambiencerating = ambiencerating;
	}
	public String getFoodrating() {
		return foodrating;
	}
	public void setFoodrating(String foodrating) {
		this.foodrating = foodrating;
	}
	@Override
	public String toString() {
		return "FeedBackVO [orderids=" + orderids + ", hotel_id=" + hotel_id
				+ ", customer_name=" + customer_name + ", comments=" + comments
				+ ", rating=" + rating + ", servicerating=" + servicerating
				+ ", ambiencerating=" + ambiencerating + "]";
	}
}
