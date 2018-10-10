/**
 * 
 */
package com.restonza.vo;

import java.util.Arrays;

/**
 * @author flex-grow developers
 *
 */
public class TrendingDishes {
	String hotel_id;
	String dishname;
	String dishimage;
	int dishcount;
	
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getDishname() {
		return dishname;
	}
	public void setDishname(String dishname) {
		this.dishname = dishname;
	}
	public String getDishimage() {
		return dishimage;
	}
	public void setDishimage(String dishimage) {
		this.dishimage = dishimage;
	}
	public int getDishcount() {
		return dishcount;
	}
	public void setDishcount(int dishcount) {
		this.dishcount = dishcount;
	}
	@Override
	public String toString() {
		return "TrendingDishes [hotel_id=" + hotel_id + ", dishname="
				+ dishname + ", dishimage=" + dishimage
				+ ", dishcount=" + dishcount + "]";
	}
}
