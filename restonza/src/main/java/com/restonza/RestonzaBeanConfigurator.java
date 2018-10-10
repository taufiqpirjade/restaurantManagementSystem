/**
 * 
 */
package com.restonza;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.restonza.controller.RestonzaAspect;
import com.restonza.dao.repository.HotelAnalyzerRepository;

/**
 * @author flex-grow developers
 *
 */
@Configuration
@EnableScheduling
public class RestonzaBeanConfigurator {
	@Autowired
	HotelAnalyzerRepository hotelAnalyzerRepository;
	
	private final static Logger logger = LoggerFactory.getLogger(RestonzaAspect.class);
	
	@Scheduled(cron = "0 0 6 * * *")
	public void CheckSubscriptionStatus() {
		logger.info("Batch running started");
		List<Integer> hotelids = hotelAnalyzerRepository.getExpiredHotelId();
		logger.info("Expired Hotel List: " + hotelids);
		hotelAnalyzerRepository.bulkHotelStatusUpdate(hotelids);
		hotelAnalyzerRepository.bulkStatusUpdate(hotelids);
		logger.info("Batch Successfully ended");
	}
}
