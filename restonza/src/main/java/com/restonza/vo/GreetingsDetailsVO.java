/**
 * 
 */
package com.restonza.vo;

import java.util.Arrays;

/**
 * @author flex-grow developers
 *
 */
public class GreetingsDetailsVO {
	private String id;
	private String hotel_id;
	private String greetingname;
	private String description;
	private byte[] greetingimg;
	private String greetingdate;
	private String status;
	
	public GreetingsDetailsVO() {
		super();
	}
	
	public GreetingsDetailsVO(String id, String greetingname,
			String description, byte[] greetingimg, String greetingdate, String status) {
		super();
		this.id = id;
		this.greetingname = greetingname;
		this.description = description;
		this.greetingimg = greetingimg;
		this.greetingdate = greetingdate;
		this.status = status;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getGreetingname() {
		return greetingname;
	}
	public void setGreetingname(String greetingname) {
		this.greetingname = greetingname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getGreetingimg() {
		return greetingimg;
	}
	public void setGreetingimg(byte[] greetingimg) {
		this.greetingimg = greetingimg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getGreetingdate() {
		return greetingdate;
	}

	public void setGreetingdate(String greetingdate) {
		this.greetingdate = greetingdate;
	}

	@Override
	public String toString() {
		return "GreetingsDetailsVO [id=" + id + ", hotel_id=" + hotel_id
				+ ", greetingname=" + greetingname + ", description="
				+ description + ", greetingimg=" + Arrays.toString(greetingimg)
				+ ", greetingdate=" + greetingdate + ", status=" + status + "]";
	}
}
