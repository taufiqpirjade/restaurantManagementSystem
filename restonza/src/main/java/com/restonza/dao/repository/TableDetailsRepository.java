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

import com.restonza.dao.TableDetails;

/**
 * @author flex-grow developers
 *
 */
public interface TableDetailsRepository extends CrudRepository<TableDetails, Integer> {
	@Query(value = "select t from TableDetails t where t.hotel_id=:hotel_id")
	List<TableDetails> getTableDetails(@Param("hotel_id") int hotel_id);
	
	@Query(value = "select t.table_id from TableDetails t where t.hotel_id=:hotel_id and status=:status")
	int[] getActiveTableDetails(@Param("hotel_id") int hotel_id, @Param("status")boolean status);
	
	@Modifying
	@Transactional
	@Query(value = "update TableDetails set status=:status, description=:description where table_id=:table_id and hotel_id=:hotel_id")
	void updateTableStatus(@Param("table_id") int table_id,@Param("hotel_id") int hotel_id, @Param("description")String description, @Param("status")boolean status);
}
