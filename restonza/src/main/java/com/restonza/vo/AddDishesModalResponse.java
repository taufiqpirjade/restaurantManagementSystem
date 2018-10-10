package com.restonza.vo;

import java.util.List;

public class AddDishesModalResponse {
	
	private List<String> categories;
	
	private List<String> ingredients;

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<String> ingredients) {
		this.ingredients = ingredients;
	}
	
}
