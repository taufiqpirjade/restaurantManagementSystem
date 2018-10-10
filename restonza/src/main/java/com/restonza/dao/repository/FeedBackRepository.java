/**
 * 
 */
package com.restonza.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.FeedBack;

/**
 * @author flex-grow developers
 *
 */
public interface FeedBackRepository extends CrudRepository<FeedBack, Integer> {
	
	@Query(value = "select f from FeedBack f where feedback_customer_id=:feedback_customer_id")
	List<FeedBack> getFeedbackHistory(@Param("feedback_customer_id") String feedback_customer_id);
}
