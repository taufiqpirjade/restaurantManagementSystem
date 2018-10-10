/**
 * 
 */
package com.restonza.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * @author flex-grow developers
 *
 */
public class BarMenuRequestVO {
	@NotNull(message= "Hotel id can not be null", groups= {GetCategoryItems.class})
	private int hotel_id;
	@NotNull(message= "Bar Category is not selected", groups= {CategoryUpdate.class,GetCategoryItems.class})
	private int bar_category_id;
	@NotNull(message= "Bar item id not selected", groups= {BarItemUpdate.class})
	private int bar_item_id;
	@NotNull(message= "Bar category can not be null", groups= {CategoryRequest.class, BarItemRequest.class, BarItemUpdate.class, CategoryUpdate.class})
	private String bar_category_name;
	private List<String> bar_sub_category;
	private String bar_sub_category_selected;
	@NotNull(message= "bar item name can not be null", groups ={BarItemRequest.class})
	private String bar_item_name;
	@NotNull(message= "status can not be null", groups= {BarItemRequest.class, CategoryRequest.class, BarItemUpdate.class, CategoryUpdate.class})
	private boolean status;
	private boolean servered_in_qty;
	private String image;
	private List<SubBarServingMannerVO> serving_manner_vo;
	private String barmenuprice;
	private int sequence;
	
	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}
	public int getBar_category_id() {
		return bar_category_id;
	}
	public void setBar_category_id(int bar_category_id) {
		this.bar_category_id = bar_category_id;
	}
	public int getBar_item_id() {
		return bar_item_id;
	}
	public void setBar_item_id(int bar_item_id) {
		this.bar_item_id = bar_item_id;
	}
	public String getBar_category_name() {
		return bar_category_name;
	}
	public void setBar_category_name(String bar_category_name) {
		this.bar_category_name = bar_category_name;
	}
	public List<String> getBar_sub_category() {
		return bar_sub_category;
	}
	public void setBar_sub_category(List<String> bar_sub_category) {
		this.bar_sub_category = bar_sub_category;
	}
	public String getBar_item_name() {
		return bar_item_name;
	}
	public void setBar_item_name(String bar_item_name) {
		this.bar_item_name = bar_item_name;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isServered_in_qty() {
		return servered_in_qty;
	}
	public void setServered_in_qty(boolean servered_in_qty) {
		this.servered_in_qty = servered_in_qty;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<SubBarServingMannerVO> getServing_manner_vo() {
		return serving_manner_vo;
	}
	public void setServing_manner_vo(List<SubBarServingMannerVO> serving_manner_vo) {
		this.serving_manner_vo = serving_manner_vo;
	}
	public String getBarmenuprice() {
		return barmenuprice;
	}
	public void setBarmenuprice(String barmenuprice) {
		this.barmenuprice = barmenuprice;
	}
	public String getBar_sub_category_selected() {
		return bar_sub_category_selected;
	}
	public void setBar_sub_category_selected(String bar_sub_category_selected) {
		this.bar_sub_category_selected = bar_sub_category_selected;
	}
	
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public interface CategoryRequest{
	}
	
	public interface CategoryUpdate {
	}
	
	public interface BarItemRequest {
	}
	public interface BarItemUpdate {
	}
	public interface GetCategoryItems {
	}
}
