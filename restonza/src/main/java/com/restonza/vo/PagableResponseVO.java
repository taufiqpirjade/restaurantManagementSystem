package com.restonza.vo;

import java.util.List;

import com.restonza.dao.Ingredient;

public class PagableResponseVO {
	Integer totalCount;
	List<Ingredient> ingredient;
	
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public List<Ingredient> getIngredient() {
		return ingredient;
	}
	public void setIngredient(List<Ingredient> ingredient) {
		this.ingredient = ingredient;
	}
}
