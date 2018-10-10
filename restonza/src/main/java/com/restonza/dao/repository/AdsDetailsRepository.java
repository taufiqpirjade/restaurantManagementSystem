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

import com.restonza.dao.AdsDetails;

/**
 * @author flex-grow developers
 *
 */
public interface AdsDetailsRepository extends CrudRepository<AdsDetails, Integer> {
	/*@Query("select a from AdsDetails a where a.hotel_id=:hotel_id")
	List<AdsDetails> getAds(@Param("hotel_id")int hotel_id);*/
	
	@Query("select a from AdsDetails a")
	List<AdsDetails> getAds();
	
	/*@Query("select a from AdsDetails a where a.adname=:adname and a.hotel_id=:hotel_id")
	AdsDetails isAdAvailable(@Param("adname")String adname, @Param("hotel_id")int hotel_id);
	*/
	//For super admin
	@Query("select a from AdsDetails a where a.status=:status")
	List<AdsDetails> getAllAds(@Param("status")boolean status);
	
	@Modifying
	@Transactional
	@Query(value = "update AdsDetails set status=:status where id=:id")
	void approveAd(@Param("id") int id, @Param("status")boolean status);
	
}
