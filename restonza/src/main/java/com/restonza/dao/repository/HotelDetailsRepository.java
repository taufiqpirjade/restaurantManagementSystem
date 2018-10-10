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

import com.restonza.dao.HotelDetails;

/**
 * @author flex-grow developers
 *
 */
public interface HotelDetailsRepository extends CrudRepository<HotelDetails, Integer> {
	@Query(value = "select h from HotelDetails h where h.hotel_name=:hotel_name")
	List<HotelDetails> findName(@Param("hotel_name") String hotel_name);
	
	@Query(value = "select h.hotel_table_count from HotelDetails h where h.id=:hotel_id")
	int getTableCount(@Param("hotel_id") int hotel_id);
	
	@Query(value = "select h.hotel_name from HotelDetails h where h.hotel_enabled=:status and h.hotel_expire=false")
	String[] getHotelName(@Param("status") boolean status);
	
	@Query(value = "select h from HotelDetails h where h.hotel_enabled=:status and h.hotel_expire=false")
	List<HotelDetails> getHotelDetails(@Param("status") boolean status);
	
	@Query(value = "select h from HotelDetails h where h.id=:id")
	HotelDetails getHotel(@Param("id") int id);
	
	@Query(value = "select h from HotelDetails h where h.id!=1")
	List<HotelDetails> findAllHotelsForSuperAdmin();
	
	@Modifying
	@Transactional
	@Query(value = "update HotelDetails set hotel_table_count=hotel_table_count + 1 where id=:hotel_id")
	void addTable(@Param("hotel_id") int hotel_id);
	
	@Modifying
	@Transactional
	@Query(value = "update HotelDetails set hotel_table_count=hotel_table_count - 1 where id=:hotel_id")
	void removeTable(@Param("hotel_id") int hotel_id);
}
