/**
 * 
 */
package com.restonza.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.BarCategory;

/**
 * @author flex-grow developers
 *
 */
public interface BarCategoryRepository extends CrudRepository<BarCategory, Integer> {
	@Query("select bc.id from BarCategory bc where bc.bar_category_name=:bar_category_name and bc.hotel_id=:hotel_id")
	Integer isCategoryExists(@Param("bar_category_name")String bar_category_name, @Param("hotel_id")int hotel_id);
	
	@Query("select bc from BarCategory bc where bc.hotel_id=:hotel_id order by bc.sequence")
	List<BarCategory> getAllCategory(@Param("hotel_id")int hotel_id);
	
	@Query("select bc from BarCategory bc where bc.hotel_id=:hotel_id and bc.status=true order by bc.sequence")
	List<BarCategory> getActiveCategory(@Param("hotel_id")int hotel_id);
	
	@Query("select bc from BarCategory bc where bc.hotel_id=:hotel_id and bc.status=:status")
	List<BarCategory> getAllActiveCategory(@Param("hotel_id")int hotel_id, @Param("status")boolean status);
	
	@Query("select bc.id from BarCategory bc where bc.bar_category_name=:bar_category_name and bc.hotel_id=:hotel_id and bc.id !=:bar_category_id")
	Integer isUpdatedCategoryExists(@Param("bar_category_name")String bar_category_name, @Param("hotel_id")int hotel_id, @Param("bar_category_id")int bar_category_id);

	@Query("select count(d) from BarCategory d WHERE d.hotel_id=:hotel_id")
	int count(@Param("hotel_id")int hotel_id);
}
