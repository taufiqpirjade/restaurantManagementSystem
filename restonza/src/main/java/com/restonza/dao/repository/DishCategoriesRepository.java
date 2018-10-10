package com.restonza.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.restonza.dao.DishCategories;

public interface DishCategoriesRepository extends CrudRepository<DishCategories, Integer> {
	
	@Query(value = "select c from DishCategories c where c.hotel_id=:hotel_id order by c.sequence")
	List<DishCategories>  findCategories(@Param("hotel_id") int hotel_id);

	@Query(value = "select count(c) from DishCategories c where c.hotel_id=:hotel_id")
	int countCategories(@Param("hotel_id") int hotel_id);
	
	@Query(value = "select category_name from DishCategories c where c.hotel_id=:hotel_id and c.status=true")
	List<String>  getCategories(@Param("hotel_id") int hotel_id);
	
	@Query("select count(d) from DishCategories d WHERE d.category_name=:category_name and d.hotel_id=:hotel_id")
	long isExist(@Param("category_name")String category_name, @Param("hotel_id")int hotel_id);
	
	/*@Query(value = "select id from DishCategories c where c.hotel_id=:hotel_id and c.category_name=:category_name")
	int isDishExist(@Param("hotel_id") int hotel_id, @Param("category_name") String category_name);
	*/
	@Modifying
	@Transactional
	@Query(value = "update DishCategories SET category_name=:category_name,img_uri=:img_uri,status=:status where hotel_id=:hotel_id and id=:id")
	void update(@Param("hotel_id") int hotel_id,
			@Param("category_name") String category_name,
			@Param("id") int id,
			@Param("img_uri") String img_uri,
			@Param("status") boolean status);
	
}
