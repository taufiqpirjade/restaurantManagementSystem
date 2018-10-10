/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class HotelDetails {
	private String owner_name;
	private String contact_info;
	private String address;
	public String getOwner_name() {
		return owner_name;
	}
	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}
	public String getContact_info() {
		return contact_info;
	}
	public void setContact_info(String contact_info) {
		this.contact_info = contact_info;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "owner_name=" + owner_name + ", contact_info="
				+ contact_info + ", address=" + address;
	}
}
