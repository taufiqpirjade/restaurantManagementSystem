/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restonza.vo.FeedBackVO;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "feedback")
public class FeedBack implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private int hotel_id;
	@NotNull
	private String customer_name;
	@NotNull
	private String feedback_comments;
	@NotNull
	private int point;
	@NotNull
	@Column(name = "feedback_customer_id", columnDefinition = "VARCHAR(255) default admin")
	private String feedback_customer_id;
	private int service_rating;
	private int ambience_rating;
	private int food_rating;
	
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
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public String getFeedback_comments() {
		return feedback_comments;
	}
	public void setFeedback_comments(String feedback_comments) {
		this.feedback_comments = feedback_comments;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getFeedback_customer_id() {
		return feedback_customer_id;
	}
	public void setFeedback_customer_id(String feedback_customer_id) {
		this.feedback_customer_id = feedback_customer_id;
	}
	
	public int getService_rating() {
		return service_rating;
	}
	public void setService_rating(int service_rating) {
		this.service_rating = service_rating;
	}
	public int getAmbience_rating() {
		return ambience_rating;
	}
	public void setAmbience_rating(int ambience_rating) {
		this.ambience_rating = ambience_rating;
	}
	public int getFood_rating() {
		return food_rating;
	}
	public void setFood_rating(int food_rating) {
		this.food_rating = food_rating;
	}
	public FeedBack prepareFeedBack(FeedBack aFeedBack, FeedBackVO aFeedBackVO) throws Exception {
		aFeedBack.setHotel_id(Integer.parseInt(aFeedBackVO.getHotel_id()));
		aFeedBack.setCustomer_name(aFeedBackVO.getCustomer_name());
		aFeedBack.setFeedback_comments(aFeedBackVO.getComments());
		aFeedBack.setPoint(Integer.parseInt(aFeedBackVO.getRating()));
		aFeedBack.setFeedback_customer_id("admin");
		aFeedBack.setAmbience_rating(Integer.parseInt(aFeedBackVO.getAmbiencerating()));
		aFeedBack.setService_rating(Integer.parseInt(aFeedBackVO.getServicerating()));
		String foodrating = aFeedBackVO.getFoodrating() != null  ? aFeedBackVO.getFoodrating() : "0";
		aFeedBack.setFood_rating(Integer.parseInt(foodrating));
		return aFeedBack;
	}
	
}
