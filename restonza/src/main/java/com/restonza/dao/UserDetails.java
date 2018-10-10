/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.vo.HotelDetailsVO;
import com.restonza.vo.UserDetailsVO;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "user_details")
public class UserDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private int hotel_id;
	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	private String user_role;
	@NotNull
	private boolean status;
	@NotNull
	private String fullname;
	@NotNull
	private String email;
	@NotNull
	private String address;
	@NotNull
	private String phonenumber;
	@NotNull
	private Date dateofjoining;
	@NotNull
	private double salary;
	@Lob
	private byte[] emp_photo;
	
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
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
	public Date getDateofjoining() {
		return dateofjoining;
	}
	public void setDateofjoining(Date dateofjoining) {
		this.dateofjoining = dateofjoining;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public byte[] getEmp_photo() {
		return emp_photo;
	}
	public void setEmp_photo(byte[] emp_photo) {
		this.emp_photo = emp_photo;
	}
	
	public UserDetails prepareUserDetails(UserDetails aUserDetails, UserDetailsVO aUserDetailsVO ) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		aUserDetails.setHotel_id(Integer.parseInt(aUserDetailsVO.getHotel_id()));
		aUserDetails.setUsername(aUserDetailsVO.getUsername());
		aUserDetails.setPassword(aUserDetailsVO.getPassword());
		aUserDetails.setUser_role(aUserDetailsVO.getUser_role());
		aUserDetails.setFullname(aUserDetailsVO.getFullname());
		aUserDetails.setEmail(aUserDetailsVO.getEmail());
		aUserDetails.setAddress(aUserDetailsVO.getAddress());
		aUserDetails.setPhonenumber(aUserDetailsVO.getPhonenumber());
		aUserDetails.setDateofjoining(sdf.parse(aUserDetailsVO.getDateofjoining()));
		aUserDetails.setSalary(Double.parseDouble(aUserDetailsVO.getSalary()));
		aUserDetails.setEmp_photo(aUserDetailsVO.getEmp_photo());
		String status = aUserDetailsVO.getStatus();
		if (status.equals("active")) {
			aUserDetails.setStatus(true);
		} else {
			aUserDetails.setStatus(false);
		}
		return aUserDetails;
	}
	
	public UserDetails prepareAdminUserDetails(UserDetails aUserDetails, HotelDetailsVO aUserDetailsVO, int hotel_id) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		aUserDetails.setHotel_id(hotel_id);
		aUserDetails.setUsername(aUserDetailsVO.getUsername());
		aUserDetails.setPassword(aUserDetailsVO.getPassword());
		String barPrivilege = aUserDetailsVO.getBarPriviledege();
		if (barPrivilege.equals("active")) {
			aUserDetails.setUser_role(RESTONZACONSTANTS.restrobaradmin.toString());
		} else {
			aUserDetails.setUser_role(RESTONZACONSTANTS.admin.toString());
		}
		aUserDetails.setFullname(aUserDetailsVO.getHoteladminname());
		aUserDetails.setEmail(aUserDetailsVO.getEmail());
		aUserDetails.setAddress(aUserDetailsVO.getAddress());
		aUserDetails.setPhonenumber(aUserDetailsVO.getHoteladminphone());
		aUserDetails.setDateofjoining(sdf.parse(aUserDetailsVO.getStartdate()));
		aUserDetails.setSalary(00000);
		aUserDetails.setEmp_photo(aUserDetailsVO.getHoteladminphoto());
		String status = aUserDetailsVO.getStatus();
		if (status.equals("active")) {
			aUserDetails.setStatus(true);
		} else {
			aUserDetails.setStatus(false);
		}
		return aUserDetails;
	}
}
