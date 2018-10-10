package com.restonza.vo;

import java.util.List;

public class BatItemsResponseVO {
	
	private String bar_item_name;
	private String sub_category;
	private String barimg;
	List<BarItemServingQty> list;
	
	public String getBar_item_name() {
		return bar_item_name;
	}
	public void setBar_item_name(String bar_item_name) {
		this.bar_item_name = bar_item_name;
	}
	public String getSub_category() {
		return sub_category;
	}
	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}
	public String getBarimg() {
		return barimg;
	}
	public void setBarimg(String barimg) {
		this.barimg = barimg;
	}
	public List<BarItemServingQty> getList() {
		return list;
	}
	public void setList(List<BarItemServingQty> list) {
		this.list = list;
	}

}
