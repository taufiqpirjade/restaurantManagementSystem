package com.restonza.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "hotel_ingredients")
public class Ingredient {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int ingredient_id;
	
	private String ingredients_name;
	
	private int ingredient_hotel;
	
	public int getIngredient_hotel() {
		return ingredient_hotel;
	}

	public void setIngredient_hotel(int ingredient_hotel) {
		this.ingredient_hotel = ingredient_hotel;
	}
	
/*	private int ingredient_dish;
	
	
	
	private String ingredient_category;*/
	
	private boolean status;
	
	//private String ingredient_dish_name;


	public int getIngredient_id() {
		return ingredient_id;
	}

	public void setIngredient_id(int ingredient_id) {
		this.ingredient_id = ingredient_id;
	}

	public String getIngredients_name() {
		return ingredients_name;
	}

	public void setIngredients_name(String ingredients_name) {
		this.ingredients_name = ingredients_name;
	}

/*	public int getIngredient_dish() {
		return ingredient_dish;
	}

	public void setIngredient_dish(int ingredient_dish) {
		this.ingredient_dish = ingredient_dish;
	}

	public int getIngredient_hotel() {
		return ingredient_hotel;
	}

	public void setIngredient_hotel(int ingredient_hotel) {
		this.ingredient_hotel = ingredient_hotel;
	}

	public String getIngredient_category() {
		return ingredient_category;
	}

	public void setIngredient_category(String ingredient_category) {
		this.ingredient_category = ingredient_category;
	}
*/
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
}
