/**
 * 
 */
package com.restonza.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.HotelSubscriptionPlanDetails;

/**
 * @author flex-grow developers
 *
 */
public interface HotelSubscriptionPlanDetailsRepository extends CrudRepository<HotelSubscriptionPlanDetails, Integer> {
	@Query("select h.offset from HotelSubscriptionPlanDetails h where h.plan_name=:plan_name")
	int getOffset(@Param("plan_name")String plan_name);
}
