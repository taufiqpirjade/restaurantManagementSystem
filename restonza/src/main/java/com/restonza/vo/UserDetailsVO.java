/**
 * 
 */
package com.restonza.vo;

import java.util.Arrays;


/**
 * @author flex-grow developers
 *
 */
public class UserDetailsVO {
	private String id;
	private String hotel_id;
	private String username;
	private String password;
	private String user_role;
	private String fullname;
	private String email;
	private String address;
	private String phonenumber;
	private String dateofjoining;
	private String salary;
	private byte[] emp_photo;
	private String status;
	
	
	public UserDetailsVO() {
		super();
	}

	public UserDetailsVO(String id, String hotel_id, String username,
			String password, String user_role, String fullname, String email,
			String address, String phonenumber,
			String dateofjoining, String salary, byte[] emp_photo, String status) {
		this.id = id;
		this.hotel_id = hotel_id;
		this.username = username;
		this.password = password;
		this.user_role = user_role;
		this.fullname = fullname;
		this.email = email;
		this.address = address;
		this.phonenumber = phonenumber;
		this.dateofjoining = dateofjoining;
		this.salary = salary;
		this.emp_photo = emp_photo;
		this.status = status;
	}
	
	public UserDetailsVO(String fullname, String email,
			String address, String phonenumber) {
		this.fullname = fullname;
		this.email = email;
		this.address = address;
		this.phonenumber = phonenumber;
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
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
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
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getDateofjoining() {
		return dateofjoining;
	}
	public void setDateofjoining(String dateofjoining) {
		this.dateofjoining = dateofjoining;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public byte[] getEmp_photo() {
		return emp_photo;
	}
	public void setEmp_photo(byte[] emp_photo) {
		this.emp_photo = emp_photo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserDetailsVO [id=" + id + ", hotel_id=" + hotel_id
				+ ", username=" + username + ", password=" + password
				+ ", user_role=" + user_role + ", fullname=" + fullname
				+ ", email=" + email + ", address=" + address
				+ ", phonenumber=" + phonenumber
				+ ", dateofjoining=" + dateofjoining + ", salary=" + salary
				+ ", emp_photo=" + Arrays.toString(emp_photo) + ", status="
				+ status + "]";
	}
}
