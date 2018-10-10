package com.restonza.vo;

public class MobileRequestVO {
	
	private String cutomerid;
	
	private String customerName;
	
	private String qrcode;
	
	private String rating;
	
	private String feedbackdescription;
	
	private String categoryName;
	
	private String service_rating;
	
	private String ambience_rating;
	
	private String food_rating;
	
	
	public String getFeedbackdescription() {
		return feedbackdescription;
	}

	public void setFeedbackdescription(String feedbackdescription) {
		this.feedbackdescription = feedbackdescription;
	}
	
	public String getCutomerid() {
		return cutomerid;
	}

	public void setCutomerid(String cutomerid) {
		this.cutomerid = cutomerid;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryname) {
		this.categoryName = categoryname;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getService_rating() {
		return service_rating;
	}

	public void setService_rating(String service_rating) {
		this.service_rating = service_rating;
	}

	public String getAmbience_rating() {
		return ambience_rating;
	}

	public void setAmbience_rating(String ambience_rating) {
		this.ambience_rating = ambience_rating;
	}

	public String getFood_rating() {
		return food_rating;
	}

	public void setFood_rating(String food_rating) {
		this.food_rating = food_rating;
	}
	
}
