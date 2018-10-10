package com.restonza.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.CustomerCallDAO;

public interface CustomerCallRepository extends CrudRepository<CustomerCallDAO, Integer> {
	
	@Query(value = "select c from CustomerCallDAO c where c.cust_call_hotel_id=:cust_call_hotel_id")
	List<CustomerCallDAO> getTables(@Param("cust_call_hotel_id") int cust_call_hotel_id);

	@Query(value = "select c from CustomerCallDAO c where c.cust_call_hotel_id=:cust_call_hotel_id and c.call_type=:call_type")
	List<CustomerCallDAO> getWaterWaiter(@Param("cust_call_hotel_id") int cust_call_hotel_id, @Param("call_type") String call_type);
	
	@Query(value = "select count(*) from CustomerCallDAO where cust_call_hotel_id=:cust_call_hotel_id and status != 'approved'")
	String getCustomerCallCount(@Param("cust_call_hotel_id") int cust_call_hotel_id);

}
