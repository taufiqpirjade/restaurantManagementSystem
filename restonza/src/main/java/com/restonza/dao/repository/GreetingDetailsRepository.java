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

import com.restonza.dao.GreetingsDetails;

/**
 * @author flex-grow developers
 *
 */
public interface GreetingDetailsRepository extends CrudRepository<GreetingsDetails, Integer> {
	@Query("select g from GreetingsDetails g")
	List<GreetingsDetails> getGreetings();
	
	@Query("select g from GreetingsDetails g where g.greetingname=:greetingname")
	GreetingsDetails isGreetingAvailable(@Param("greetingname")String greetingname);
	
	//For admin
	@Query("select a from GreetingsDetails a where a.status=:status")
	List<GreetingsDetails> getAllGreetings(@Param("status")boolean status);
	
	@Modifying
	@Transactional
	@Query(value = "update GreetingsDetails set status=:status where id=:id")
	void approveGreeting(@Param("id") int id, @Param("status")boolean status);
}
