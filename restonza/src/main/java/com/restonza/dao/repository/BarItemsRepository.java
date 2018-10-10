/**
 * 
 */
package com.restonza.dao.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.BarItems;

/**
 * @author flex-grow developers
 *
 */
public interface BarItemsRepository extends CrudRepository<BarItems, Integer> {
	@Query("select bi.id from BarItems bi where bi.bar_item_name=:bar_item_name and bi.hotel_id=:hotel_id")
	Integer isItemExists(@Param("bar_item_name")String bar_item_name, @Param("hotel_id")int hotel_id);
	
	@Query("select bi from BarItems bi where bi.hotel_id=:hotel_id and bi.status=:status")
	List<BarItems> getActiveBarMenuNames(@Param("hotel_id")int hotel_id, @Param("status") boolean status);
	
	@Query("select count(d) from BarItems d WHERE d.hotel_id=:hotel_id")
	int count(@Param("hotel_id")int hotel_id);
	
	@Modifying
	@Transactional
	@Query("update BarItems set bar_item_name=:bar_item_name, price=:price, barimg=:barimg, status=:status where id=:id")
	void updateBarItem(@Param("bar_item_name")String bar_item_name,
			@Param("price")double price,
			@Param("status") boolean status,
			@Param("id") int id,
			@Param("barimg") String barimg);
}
