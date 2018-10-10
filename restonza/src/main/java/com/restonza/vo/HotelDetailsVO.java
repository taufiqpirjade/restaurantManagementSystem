/**
 * 
 */
package com.restonza.vo;

import java.util.Arrays;

/**
 * @author flex-grow developers
 *
 */
public class HotelDetailsVO {
	private String id;
	private String hotelname;
	private String hoteltype;
	private String currencyType;
	private String hoteladminname;
	private String username;
	private String password;
	private String email;
	private String address;
	private String hoteladminphone;
	private String hotelsubscription;
	private String startdate;
	private String enddate;
	private byte[] hoteladminphoto;
	private String status;
	private String barPriviledege;
	
	
	public HotelDetailsVO() {
		super();
	}
	
	public HotelDetailsVO(String id, String hotelname, String hoteltype,
			String startdate, String enddate, String hotelsubscription, String status) {
		this.id = id;
		this.hotelname = hotelname;
		this.hoteltype = hoteltype;
		this.startdate = startdate;
		this.enddate = enddate;
		this.hotelsubscription = hotelsubscription;
		this.status = status;
	}
	
	public HotelDetailsVO(String id, String hotelname, String hoteltype,
			String startdate, String enddate, String hotelsubscription, String status, String barPriviledege) {
		this.id = id;
		this.hotelname = hotelname;
		this.hoteltype = hoteltype;
		this.startdate = startdate;
		this.enddate = enddate;
		this.hotelsubscription = hotelsubscription;
		this.status = status;
		this.barPriviledege = barPriviledege;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public String getHoteltype() {
		return hoteltype;
	}
	public void setHoteltype(String hoteltype) {
		this.hoteltype = hoteltype;
	}
	public String getHoteladminname() {
		return hoteladminname;
	}
	public void setHoteladminname(String hoteladminname) {
		this.hoteladminname = hoteladminname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHoteladminphone() {
		return hoteladminphone;
	}
	public void setHoteladminphone(String hoteladminphone) {
		this.hoteladminphone = hoteladminphone;
	}
	public String getHotelsubscription() {
		return hotelsubscription;
	}
	public void setHotelsubscription(String hotelsubscription) {
		this.hotelsubscription = hotelsubscription;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	
	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public byte[] getHoteladminphoto() {
		return hoteladminphoto;
	}
	public void setHoteladminphoto(byte[] hoteladminphoto) {
		this.hoteladminphoto = hoteladminphoto;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	public String getBarPriviledege() {
		return barPriviledege;
	}

	public void setBarPriviledege(String barPriviledege) {
		this.barPriviledege = barPriviledege;
	}

	@Override
	public String toString() {
		return "HotelDetailsVO [id=" + id + ", hotelname=" + hotelname
				+ ", hoteltype=" + hoteltype + ", hoteladminname="
				+ hoteladminname + ", username=" + username + ", password="
				+ password + ", email=" + email + ", address=" + address
				+ ", hoteladminphone=" + hoteladminphone
				+ ", hotelsubscription=" + hotelsubscription + ", startdate="
				+ startdate + ", hoteladminphoto="
				+ Arrays.toString(hoteladminphoto) + ", status=" + status + "]";
	}
	
}
