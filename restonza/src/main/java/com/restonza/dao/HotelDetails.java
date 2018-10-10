/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.vo.HotelDetailsVO;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "hotel_details")
public class HotelDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private String hotel_name;
	@NotNull
	private String hotel_address;
	@NotNull
	private int hotel_table_count;
	@NotNull
	private String hotel_subscription_plan;
	@NotNull
	private Date hotel_start_date;
	@NotNull
	private Date hotel_end_date;
	@NotNull
	private String hotel_reg_admin_name;
	@NotNull
	private String hotel_type;
	@NotNull
	private boolean hotel_enabled;
	@NotNull
	private boolean hotel_expire;
	
	private String hotel_logo_url;
	
	private String hotel_banner_url;
	
	private String hotel_currency;
	
	private boolean isbar;
	
	/**
	 * 
	 */
	public HotelDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * @param id
	 * @param hotel_name
	 * @param hotel_address
	 * @param hotel_table_count
	 * @param hotel_subscription_plan
	 * @param hotel_start_date
	 * @param hotel_end_date
	 * @param hotel_reg_admin_name
	 * @param hotel_type
	 * @param hotel_enabled
	 */
	public HotelDetails(int id, String hotel_name, String hotel_address,
			int hotel_table_count, String hotel_subscription_plan,
			Date hotel_start_date, Date hotel_end_date,
			String hotel_reg_admin_name, String hotel_type,
			boolean hotel_enabled, boolean hotel_expire, String hotel_currency) {
		this.id = id;
		this.hotel_name = hotel_name;
		this.hotel_address = hotel_address;
		this.hotel_table_count = hotel_table_count;
		this.hotel_subscription_plan = hotel_subscription_plan;
		this.hotel_start_date = hotel_start_date;
		this.hotel_end_date = hotel_end_date;
		this.hotel_reg_admin_name = hotel_reg_admin_name;
		this.hotel_type = hotel_type;
		this.hotel_enabled = hotel_enabled;
		this.hotel_expire = hotel_expire;
		this.hotel_currency = hotel_currency;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHotel_name() {
		return hotel_name;
	}
	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
	}
	public String getHotel_address() {
		return hotel_address;
	}
	public void setHotel_address(String hotel_address) {
		this.hotel_address = hotel_address;
	}
	public int getHotel_table_count() {
		return hotel_table_count;
	}
	public void setHotel_table_count(int hotel_table_count) {
		this.hotel_table_count = hotel_table_count;
	}
	public String getHotel_subscription_plan() {
		return hotel_subscription_plan;
	}
	public void setHotel_subscription_plan(String hotel_subscription_plan) {
		this.hotel_subscription_plan = hotel_subscription_plan;
	}
	public Date getHotel_start_date() {
		return hotel_start_date;
	}
	public void setHotel_start_date(Date hotel_start_date) {
		this.hotel_start_date = hotel_start_date;
	}
	public Date getHotel_end_date() {
		return hotel_end_date;
	}
	public void setHotel_end_date(Date hotel_end_date) {
		this.hotel_end_date = hotel_end_date;
	}
	public String getHotel_reg_admin_name() {
		return hotel_reg_admin_name;
	}
	public void setHotel_reg_admin_name(String hotel_reg_admin_name) {
		this.hotel_reg_admin_name = hotel_reg_admin_name;
	}
	public String getHotel_type() {
		return hotel_type;
	}
	public void setHotel_type(String hotel_type) {
		this.hotel_type = hotel_type;
	}
	public boolean isHotel_enabled() {
		return hotel_enabled;
	}
	public void setHotel_enabled(boolean hotel_enabled) {
		this.hotel_enabled = hotel_enabled;
	}
	public boolean isHotel_expire() {
		return hotel_expire;
	}
	public void setHotel_expire(boolean hotel_expire) {
		this.hotel_expire = hotel_expire;
	}
	
	public String getHotel_currency() {
		return hotel_currency;
	}

	public void setHotel_currency(String hotel_currency) {
		this.hotel_currency = hotel_currency;
	}

	public String getHotel_logo_url() {
		return hotel_logo_url;
	}

	public void setHotel_logo_url(String hotel_logo_url) {
		this.hotel_logo_url = hotel_logo_url;
	}

	public String getHotel_banner_url() {
		return hotel_banner_url;
	}

	public void setHotel_banner_url(String hotel_banner_url) {
		this.hotel_banner_url = hotel_banner_url;
	}

	public boolean isIsbar() {
		return isbar;
	}

	public void setIsbar(boolean isbar) {
		this.isbar = isbar;
	}


	public HotelDetails prepareHotelDetails(HotelDetails aHotelDetails, HotelDetailsVO aHotelDetailsVO,int offset) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		aHotelDetails.setHotel_name(aHotelDetailsVO.getHotelname());
		aHotelDetails.setHotel_address(aHotelDetailsVO.getAddress());
		aHotelDetails.setHotel_type(aHotelDetailsVO.getHoteltype());
		aHotelDetails.setHotel_currency(aHotelDetailsVO.getCurrencyType());
		aHotelDetails.setHotel_reg_admin_name(aHotelDetailsVO.getUsername());
		String barPrivilege = aHotelDetailsVO.getBarPriviledege();
		if (barPrivilege.equals("active")) {
			aHotelDetails.setIsbar(true);
		} else {
			aHotelDetails.setIsbar(false);
		}
		Date date = sdf.parse(aHotelDetailsVO.getStartdate());
		aHotelDetails.setHotel_start_date(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, offset);
		date = cal.getTime();
		aHotelDetails.setHotel_end_date(date);
		aHotelDetails.setHotel_subscription_plan(aHotelDetailsVO.getHotelsubscription());
		aHotelDetails.setHotel_table_count(0);
		String status = aHotelDetailsVO.getStatus();
		aHotelDetails.setHotel_expire(false);
		if (status.equals("active")) {
			aHotelDetails.setHotel_enabled(true);
		} else {
			aHotelDetails.setHotel_enabled(false);
		}
		return aHotelDetails;
	}
}
