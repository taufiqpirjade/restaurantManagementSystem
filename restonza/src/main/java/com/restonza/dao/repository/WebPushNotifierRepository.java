package com.restonza.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.PushNotifierDAO;

public interface WebPushNotifierRepository extends CrudRepository<PushNotifierDAO, Integer> {
	
	@Query("select m.playerid  from PushNotifierDAO m where hotelid=:hotelid")
	List<String> getUserByHotelId(@Param("hotelid") int hotelid);
	
}
