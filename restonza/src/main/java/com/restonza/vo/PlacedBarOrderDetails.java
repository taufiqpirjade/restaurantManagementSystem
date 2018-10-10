/**
 * 
 */
package com.restonza.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author flex-grow developers
 *
 */
public class PlacedBarOrderDetails {
	private int bar_item_id;
	private String bar_item_name;
	private int qty;
	private double price;
	private String barimg;
	public int getBar_item_id() {
		return bar_item_id;
	}
	public void setBar_item_id(int bar_item_id) {
		this.bar_item_id = bar_item_id;
	}
	public String getBar_item_name() {
		return bar_item_name;
	}
	public void setBar_item_name(String bar_item_name) {
		this.bar_item_name = bar_item_name;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getBarimg() {
		return barimg;
	}
	public void setBarimg(String barimg) {
		this.barimg = barimg;
	}
	public static List<PlacedBarOrderDetails> getPlaceBarOrderDetailsObject(List<Map<String, Object>>barItemsDetails,  HashMap<String, String> barOrderMap) {
		List<PlacedBarOrderDetails> placeOrderDetailsList = new ArrayList<PlacedBarOrderDetails>();
		for (Map<String, Object> map : barItemsDetails) {
			PlacedBarOrderDetails placedBarOrderDetails = new PlacedBarOrderDetails();
			String baritemname = (String)map.get("bar_item_name");
			placedBarOrderDetails.setBar_item_id((int)map.get("id"));
			placedBarOrderDetails.setBar_item_name(baritemname);
			placedBarOrderDetails.setPrice((double)map.get("price"));
			placedBarOrderDetails.setQty(Integer.parseInt(barOrderMap.get(baritemname)));
			placeOrderDetailsList.add(placedBarOrderDetails);
		}
		return placeOrderDetailsList;
	}
	
}
