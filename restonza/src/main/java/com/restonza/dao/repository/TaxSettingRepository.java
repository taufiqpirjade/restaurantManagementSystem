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

import com.restonza.dao.TaxSetting;

/**
 * @author flex-grow developers
 *
 */
public interface TaxSettingRepository extends CrudRepository<TaxSetting, Integer> {
	@Query(value = "select t from TaxSetting t where t.hotel_id=:hotel_id")
	List<TaxSetting> getTaxSetting(@Param("hotel_id") int hotel_id);
	
	@Query(value = "select t from TaxSetting t where t.hotel_id=:hotel_id and t.status=:status and t.isliquortax=:isliquor")
	List<TaxSetting> getTax(@Param("hotel_id") int hotel_id,@Param("status") boolean status, @Param("isliquor") boolean isliquor);
	
	@Modifying
	@Transactional
	@Query(value = "update TaxSetting set taxname=:taxname, percentage=:percentage,status=:status,isliquortax=:isliquor where id=:id")
	void updateTaxStatus(@Param("taxname") String taxname,
							@Param("percentage") double percentage,
							@Param("isliquor")boolean isliquor,
							@Param("status") boolean status,
							@Param("id") int id);
	
}
