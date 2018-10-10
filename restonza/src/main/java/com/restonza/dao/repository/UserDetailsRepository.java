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

import com.restonza.dao.UserDetails;

/**
 * @author flex-grow developers
 *
 */
public interface UserDetailsRepository extends CrudRepository<UserDetails, Integer> {
	@Query("select u from UserDetails u where username=:username and password=:password and status=:status")
	List<UserDetails> authenticateUser(@Param("username")String username, @Param("password")String password, @Param("status")boolean status);
	
	@Query("select u from UserDetails u where u.id=:id")
	UserDetails getUserDetails(@Param("id")int id);
	
	@Query("select u from UserDetails u where u.username=:username and u.hotel_id=:hotel_id")
	UserDetails getAdminUserDetails(@Param("username")String username, @Param("hotel_id")int hotel_id);
	
	@Query("select count(u) from UserDetails u WHERE u.username=:username")
	long isExist(@Param("username")String username);
	
	@Modifying
	@Transactional
	@Query(value = "update UserDetails u set u.status=:status where u.hotel_id=:hotel_id")
	void updateUserStatus(@Param("status") boolean status, @Param("hotel_id") int hotel_id);
	
	@Modifying
	@Transactional
	@Query(value = "update UserDetails u set u.user_role=:user_role where u.hotel_id=:hotel_id")
	void updateUserBarPriviledge(@Param("user_role") String user_role, @Param("hotel_id") int hotel_id);
	
	@Modifying
	@Transactional
	@Query(value = "delete from UserDetails u where u.hotel_id=:hotel_id")
	void removeUser(@Param("hotel_id") int hotel_id);
	
	//manage user db update
	@Query("select u from UserDetails u where u.hotel_id=:hotel_id and u.id !=:id")
	List<UserDetails> getAllUsersDetails(@Param("id")int id , @Param("hotel_id")int hotel_id);
	
	@Modifying
	@Transactional
	@Query(value = "update UserDetails u set u.password=:password, u.address=:address, u.salary=:salary, u.phonenumber=:phonenumber, u.email=:email, u.user_role=:user_role, u.status=:status where u.id=:id")
	void updateUser(@Param("id") int id, @Param("password") String password,
			@Param("address") String address, @Param("salary") double salary,
			@Param("phonenumber") String phonenumber,
			@Param("email") String email,
			@Param("user_role") String user_role,
			@Param("status") boolean status);
	
}
