package com.restonza.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.restonza.dao.Dishes;

public interface HotelDishesRepositories extends CrudRepository<Dishes, Integer> {
	
	@Query(value = "select d from Dishes d where d.hotel=:hotel order by sequence")
	List<Dishes> findDishes(@Param("hotel") int hotel);
	
	@Query(value = "select d from Dishes d where d.hotel=:hotel and d.status=true order by sequence")
	List<Dishes> findDishes2(@Param("hotel") int hotel);
	
	@Query(value = "select d from Dishes d where d.hotel=:hotel and d.dish_category=:dish_category and d.dish_name=:dish_name")
	Dishes findDishForIngredient(@Param("hotel") int hotel,@Param("dish_category") String dish_category,@Param("dish_name") String dish_name);
	
	@Query(value = "select d from Dishes d where d.hotel=:hotel and d.dish_category=:dish_category and d.status=true order by d.sequence asc")
	List<Dishes> getDishes(@Param("hotel") int hotel,@Param("dish_category") String dish_category);
	
	@Query(value = "select d.img_uri from Dishes d where d.dish_name=:dish_name and d.hotel=:hotel")
	String getDishImage(@Param("dish_name")String dish_name ,@Param("hotel") int hotel);
	
	@Query(value = "select d from Dishes d where d.hotel=:hotel and d.dish_id=:dish_id")
	Dishes findDish(@Param("hotel") int hotel,@Param("dish_id") int dish_id);
	
	@Query("select count(d) from Dishes d WHERE d.dish_name=:dish_name and d.hotel=:hotel")
	long isExist(@Param("dish_name")String dish_name, @Param("hotel")int hotel);
	
	@Query("select count(d) from Dishes d WHERE d.hotel=:hotel")
	int count(@Param("hotel")int hotel);
	
	@Query("select d.dish_price from Dishes d WHERE d.hotel=:hotel and dish_name=:dish_name")
	double getDishPrice(@Param("hotel")int hotel,@Param("dish_name")String dish_name);
	
	@Modifying
	@Transactional
	@Query(value = "update Dishes SET dish_name=:dish_name, dish_description=:dish_description, dish_category=:dish_category, dish_price=:dish_price, discount=:discount ,status=:status,hot=:hot, dish_ingredients=:dish_ingredients,img_uri=:img_uri where hotel=:hotel and dish_id=:dish_id")
	void update(@Param("hotel") int hotel,
			@Param("dish_id") int dish_id,
			@Param("dish_name") String dish_name,
			@Param("dish_description") String dish_description,
			@Param("dish_category") String dish_category,
			@Param("dish_price") double dish_price,
			@Param("discount") String discount,
			@Param("status") boolean status,
			@Param("hot") boolean hot,
			@Param("dish_ingredients") String dish_ingredients,
			@Param("img_uri") String img_uri);
	
}
