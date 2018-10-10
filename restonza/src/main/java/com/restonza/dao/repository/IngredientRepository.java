package com.restonza.dao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.restonza.dao.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer>{

	 @Query(value = "select ingredients_name from Ingredient i where i.ingredient_hotel=:ingredient_hotel and i.status=true")
	 List<String> getIngredients(@Param("ingredient_hotel") int ingredient_hotel);
	 
	 @Query("select count(i) from Ingredient i WHERE i.ingredients_name=:ingredients_name and i.ingredient_hotel=:ingredient_hotel")
	 long isExist(@Param("ingredients_name")String ingredients_name, @Param("ingredient_hotel") int ingredient_hotel);
	
	 //Query used for pagenation------------------------------------------------------------------------------------
	 @Query(value = "select i from Ingredient i where i.ingredient_hotel=:ingredient_hotel")
	 List<Ingredient> findIngredient(@Param("ingredient_hotel") int ingredient_hotel, Pageable page);
	 
	 /**
	  * Without pagination
	  * @param ingredient_hotel
	  * @param page
	  * @return
	  */
	 @Query(value = "select i from Ingredient i where i.ingredient_hotel=:ingredient_hotel")
	 List<Ingredient> getAllIngredients(@Param("ingredient_hotel") int ingredient_hotel);
	 
	 @Query(value = "select count(i) from Ingredient i where i.ingredient_hotel=:ingredient_hotel")
	 Integer getTotalCount(@Param("ingredient_hotel") int ingredient_hotel);
	 //------------------------------------------------------------------------------------------------------------
	 
	 @Query(value = "select i.ingredients_name from Ingredient i where i.ingredient_id=:ingredient_id")
	 String getIngrdientName(@Param("ingredient_id") int ingredient_id);
	 //update ingredient
	 @Modifying
	 @Transactional
	 @Query(value = "update Ingredient SET ingredients_name=:ingredients_name, status=:status where ingredient_id=:ingredient_id")
	 void updateIngredientNameOrStatusById(@Param("status") boolean status,@Param("ingredients_name") String ingredients_name,@Param("ingredient_id") int ingredient_id);
}
