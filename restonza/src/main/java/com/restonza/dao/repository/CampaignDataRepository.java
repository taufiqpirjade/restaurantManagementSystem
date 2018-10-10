package com.restonza.dao.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.CampaignData;

public interface CampaignDataRepository extends
		CrudRepository<CampaignData, Integer> {
	
	@Query("select a from CampaignData a where status=false")
	List<CampaignData> getAllCampaigns();
	
	@Modifying
	@Transactional
	@Query("update CampaignData set status=:status where hotel_id=:hotel_id AND id=:id")
	void approveCampaign(@Param("hotel_id") int hotel_id,@Param("id") int id,
			@Param("status")boolean status);
	
	@Query("select a from CampaignData a where hotel_id=:hotel_id")
	List<CampaignData> getMyCampaigns(@Param("hotel_id") int hotel_id);
	
	@Query("select a from CampaignData a where hotel_id=:hotel_id")
	List<CampaignData> checkCampaignEnrollment(@Param("hotel_id") int hotel_id);

}
