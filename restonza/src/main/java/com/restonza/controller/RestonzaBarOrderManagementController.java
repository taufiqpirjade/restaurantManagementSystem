/**
 * 
 */
package com.restonza.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.repository.BarOrderManagementRepository;
import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaCommonAsynHandler;
import com.restonza.vo.BarOrderManagementVO;
import com.restonza.vo.PlacedOrderDetailsVO;
import com.restonza.vo.RestonzaRestResponseVO;

/**
 * @author flex-grow developers
 *
 */
@RestController
@RequestMapping("/restonza")
public class RestonzaBarOrderManagementController {
	@Autowired
	private BarOrderManagementRepository barOrderManagementRepository;
	
	@Autowired
	private RestonzaCommonAsynHandler restonzaCommonAsynHandler;
	
	// WEB
	@PostMapping("/addBarOrder")
	public @ResponseBody RestonzaRestResponseVO executeAddBarOrder(@RequestBody BarOrderManagementVO barOrderManagementVO) throws Exception {
		boolean previousOrderExist = false;
		Long parent_id = barOrderManagementRepository.isOrderRunning(barOrderManagementVO);
		if (parent_id != null) {
			previousOrderExist = true;
		}
		List<PlacedOrderDetailsVO> placedOrderDetailsVOs = barOrderManagementVO.getOrderDetails();
		HashMap<String, String> barOrderMap = new HashMap<>();
		for (PlacedOrderDetailsVO placedOrderDetailsVO : placedOrderDetailsVOs) {
			barOrderMap.put(placedOrderDetailsVO.getDishname(), placedOrderDetailsVO.getQty());
		}
		barOrderManagementRepository.addBarOrder(barOrderManagementVO, barOrderMap,previousOrderExist, parent_id);
		return new RestonzaRestResponseVO("success", "Bar Order Added Successfully");
	}
	
	
	@PostMapping("/getBarOrderBilling/{hotel_id}")
	public @ResponseBody RestonzaRestResponseVO executeGetBarOrderBilling(@PathVariable("hotel_id")String hotel_id) throws Exception {
		int hotelId = Integer.parseInt(hotel_id);
		List<Map<String, Object>> runningBarOrders  = barOrderManagementRepository.getBarOrders(hotelId);
		return new RestonzaRestResponseVO("success", runningBarOrders);
	}
	
	@PostMapping("/getBarOrder/{hotel_id}/{status}")
	public @ResponseBody RestonzaRestResponseVO getBarOrderStatuswise(@PathVariable("hotel_id")String hotel_id, @PathVariable("status")String status) throws Exception {
		int hotelId = Integer.parseInt(hotel_id);
		List<Map<String, Object>> runningBarOrders = null;
		if (RESTONZACONSTANTS.delivered_order.toString().equals(status)) {
			runningBarOrders  = barOrderManagementRepository.getBarOrderStatusWise(hotelId, RESTONZACONSTANTS.confirm_order.toString(), RESTONZACONSTANTS.ready_order.toString());
		} else {
			runningBarOrders = barOrderManagementRepository.getBarOrderStatusWise(hotelId,status);
		}
		return new RestonzaRestResponseVO("success", runningBarOrders);
	}
	
	@PostMapping("/updateBarOrderStatus")
	public @ResponseBody RestonzaRestResponseVO executeUpdateBarOrderStatuswise(@RequestBody BarOrderManagementVO barOrderManagementVO) throws Exception {
		int order_id = barOrderManagementVO.getParent_order_id();
		String newStatus = barOrderManagementVO.getNew_status();
		String oldStatus = barOrderManagementVO.getOld_status();
		if (RESTONZACONSTANTS.cancel_order.toString().equals(newStatus)) {
			barOrderManagementRepository.cancelBarOrder(order_id, newStatus, oldStatus);
		}else {
			barOrderManagementRepository.updateBarOrder(order_id, newStatus, oldStatus);
		}
		if (RESTONZACONSTANTS.cancel_order.toString().equals(newStatus)) {
			newStatus = "Canceled";
		} else if (RESTONZACONSTANTS.confirm_order.toString().equals(newStatus)) {
			newStatus = "Confirmed";
		} else if (RESTONZACONSTANTS.delivered_order.toString().equals(newStatus)) {
			newStatus = "Delivered";
		}
		restonzaCommonAsynHandler.sendBarPushNotification(order_id, newStatus, "Bar Order Status Change", "Your Order has been " + newStatus);
		
		return new RestonzaRestResponseVO("success", "Bar Order Updated Successfully");
	}
}
