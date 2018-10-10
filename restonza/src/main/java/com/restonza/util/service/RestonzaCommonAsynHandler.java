/**
 * 
 */
package com.restonza.util.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.restonza.dao.repository.HotelAnalyzerRepository;

/**
 * @author flex-grow developers
 *
 */
@EnableAsync
@Service
public class RestonzaCommonAsynHandler {
	@Autowired
	HotelAnalyzerRepository hotelAnalyzerRepository;
	
	@Autowired
	RestonzaPushNotificationService restonzaPushNotificationService;
	
	@Async
	public void sendPushNotification(Integer order_id, String status, String messagetitle, String messageBody) {
		try {
			String deviceId = hotelAnalyzerRepository.getDeviceIdBasedOnOrderId(order_id);
			if (null != deviceId) {
				restonzaPushNotificationService.sendPushNotification(deviceId, messagetitle, messageBody);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Async
	public void sendBarPushNotification(Integer order_id, String status, String messagetitle, String messageBody) {
		try {
			String deviceId = hotelAnalyzerRepository.getDeviceIdBasedOnBarOrderId(order_id);
			if (null != deviceId) {
				restonzaPushNotificationService.sendPushNotification(deviceId, messagetitle, messageBody);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
