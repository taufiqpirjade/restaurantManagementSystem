/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class SessionVO {
	private int hotel_id;
	private int empid;
	private int table_count;
	private String user_role;
	private String totalOrderCount;
	
	public SessionVO(int hotel_id, int empid, int table_count, String user_role, String totalOrderCount) {
		this.hotel_id = hotel_id;
		this.empid = empid;
		this.table_count = table_count;
		this.user_role = user_role;
		this.totalOrderCount = totalOrderCount;
	}
	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}
	public int getTable_count() {
		return table_count;
	}
	public void setTable_count(int table_count) {
		this.table_count = table_count;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public int getEmpid() {
		return empid;
	}
	public void setEmpid(int empid) {
		this.empid = empid;
	}
	@Override
	public String toString() {
		return "SessionVO [hotel_id=" + hotel_id + ", empid=" + empid
				+ ", table_count=" + table_count + ", user_role=" + user_role
				+ "]";
	}
	public String getTotalOrderCount() {
		return totalOrderCount;
	}
	public void setTotalOrderCount(String totalOrderCount) {
		this.totalOrderCount = totalOrderCount;
	}
	

}
