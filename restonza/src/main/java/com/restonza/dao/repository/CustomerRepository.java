package com.restonza.dao.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.CustomerDAO;

public interface CustomerRepository extends CrudRepository<CustomerDAO, String> {
	@Query("select c.deviceid from CustomerDAO c")
	List<String> getAllCustomerDeviceIds();
	
	@Query("select c from CustomerDAO c where phonenumber=:phonenumber")
	CustomerDAO getLoginDetails(@Param(value = "phonenumber") String phonenumber);
	
	@Query("select c from CustomerDAO c")
	List<CustomerDAO> getAllCustomerDetails();
	
	@Query("select c from CustomerDAO c where customer_id=:customer_id")
	CustomerDAO getCustomerDetailsUsingId(@Param("customer_id")String customer_id);
	
	@Modifying
	@Transactional
	@Query(value = "update CustomerDAO set first_name=:first_name , last_name=:last_name, deviceid=:deviceid, emailid=:emailid, phonenumber=:phonenumber where customer_id=:customer_id AND phonenumber=:phonenumber")
	int updateCustomerDetails(@Param("first_name") String first_name,
			@Param("last_name")String last_name,
			@Param("deviceid")String deviceid,
			@Param("customer_id")String customer_id,
			@Param("emailid")String emailid,
			@Param("phonenumber")String phonenumber);
	
	@Query("select c from CustomerDAO c where phonenumber=:phonenumber")
	List<CustomerDAO> isCustomerExist(@Param("phonenumber")String phonenumber);
	
	@Query("select c from CustomerDAO c where customer_id=:customer_id AND phonenumber=:phonenumber")
	List<CustomerDAO> isDeviceCustomerIdExist(@Param("customer_id")String customer_id,@Param("phonenumber")String phonenumber);
	
	@Modifying
	@Transactional
	@Query(value = "update CustomerDAO set deviceid=:deviceid where customer_id=:customer_id")
	int updateDeviceId(@Param("deviceid") String deviceid,
							@Param("customer_id")String customer_id);
	
	@Query("select c from CustomerDAO c where customer_id=:customer_id AND emailid=:emailid")
	List<CustomerDAO> isCustomerIdExist(@Param("customer_id")String customer_id,@Param("emailid")String emailid);
	
}
