
package com.restonza.mobile.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.BarCategory;
import com.restonza.dao.CustomerCallDAO;
import com.restonza.dao.CustomerDAO;
import com.restonza.dao.DishCategories;
import com.restonza.dao.Dishes;
import com.restonza.dao.FeedBack;
import com.restonza.dao.HotelDetails;
import com.restonza.dao.OrderDetails;
import com.restonza.dao.TaxSetting;
import com.restonza.dao.repository.BarCategoryRepository;
import com.restonza.dao.repository.BarOrderManagementRepository;
import com.restonza.dao.repository.CustomerCallRepository;
import com.restonza.dao.repository.CustomerRepository;
import com.restonza.dao.repository.DishCategoriesRepository;
import com.restonza.dao.repository.FeedBackRepository;
import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.dao.repository.HotelDetailsRepository;
import com.restonza.dao.repository.HotelDishesRepositories;
import com.restonza.dao.repository.OrderDetailsRepository;
import com.restonza.dao.repository.TaxSettingRepository;
import com.restonza.dao.repository.WebPushNotifierRepository;
import com.restonza.util.service.MailSenderUtility;
import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaCustomException;
import com.restonza.util.service.RestonzaPushNotificationService;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.AdsDetailsVO;
import com.restonza.vo.BarMenuRequestVO;
import com.restonza.vo.BarOrderManagementVO;
import com.restonza.vo.BarOrderResponse;
import com.restonza.vo.CustomerCallVO;
import com.restonza.vo.CustomerVO;
import com.restonza.vo.DishesVO;
import com.restonza.vo.GeneralOrderVO;
import com.restonza.vo.GenerateBillVO;
import com.restonza.vo.MobileRequestVO;
import com.restonza.vo.OrderDetailsVO;
import com.restonza.vo.OrderHistoryDetailsVO;
import com.restonza.vo.PlacedBarOrderDetails;
import com.restonza.vo.PlacedOrderDetailsVO;
import com.restonza.vo.PrintVO;
import com.restonza.vo.RestonzaRestResponseVO;

/**
 * @author flex-grow developers
 *
 */
@RestController
@RequestMapping("/restonza/mobile")
public class RestonzaMobilePostController {
	
	@Autowired
	private DishCategoriesRepository dishCategoriesRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private HotelDishesRepositories hotelDishesRepositories;
	
	
	@Autowired
	private CustomerCallRepository customerCallRepository;
	
	@Autowired
	private FeedBackRepository feedbackRepository;
	
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	
	@Autowired
	private HotelDetailsRepository hotelDetailsRepository;
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	@Autowired
	private RestonzaPushNotificationService restonzaPushNotificationService;
	
	@Autowired
	private TaxSettingRepository taxSettingRepository;
	
	@Autowired
	private WebPushNotifierRepository webPushNotifierRepository;
	
	@Autowired
	private BarCategoryRepository barCategoryRepository;
	
	@Autowired
	private BarOrderManagementRepository barOrderManagementRepository;
	
	@Autowired
	private MailSenderUtility mailSenderUtility;
	
	/**
	 * Customer login (solely used by mobile application)
	 * @param mobileRequestVO
	 * @return
	 */
	@PostMapping("/customerLogin")
	public @ResponseBody RestonzaRestResponseVO executeLogin(@RequestBody CustomerVO customerVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(customerVO.getPhonenumber()) &&
				RestonzaUtility.isNullOrEmpty(customerVO.getPassword())) {
			CustomerDAO daoResponse = customerRepository.getLoginDetails(customerVO.getPhonenumber());
			if(daoResponse !=null && daoResponse.getPassword().equals(customerVO.getPassword())) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", daoResponse);
				if (RestonzaUtility.isNullOrEmpty(customerVO.getDeviceid())) {
					customerRepository.updateDeviceId(customerVO.getDeviceid(),daoResponse.getCustomer_id());
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Userid Password mismatch");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Userid Password mismatch");
		}
		return restonzaRestResponseVO;
	}
	
	
	/**
	 * Customer login (solely used by mobile application)
	 * @param mobileRequestVO
	 * @return
	 */
	@PostMapping("/loginwithfacebook")
	public @ResponseBody RestonzaRestResponseVO executefacebookLogin(@RequestBody CustomerVO customerVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(customerVO.getCustomer_id()) 
				&& RestonzaUtility.isNullOrEmpty(customerVO.getFirst_name())
				&& RestonzaUtility.isNullOrEmpty(customerVO.getDeviceid())
				&& RestonzaUtility.isNullOrEmpty(customerVO.getEmailid())) {
			List<CustomerDAO> cust = customerRepository.isCustomerIdExist(customerVO.getCustomer_id(),customerVO.getEmailid());
			if (cust != null && !cust.isEmpty()) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("SUCCESS", cust.get(0));
				customerRepository.updateDeviceId(customerVO.getDeviceid(),customerVO.getCustomer_id());
			} else {
				// Add Customer
				CustomerDAO customer = new CustomerDAO();
				customer.setCustomer_id(customerVO.getCustomer_id());
				customer.setFirst_name(customerVO.getFirst_name());
				customer.setLast_name(customerVO.getLast_name() != null ? customerVO.getLast_name() : "");
				customer.setDeviceid(customerVO.getDeviceid());
				customer.setPassword(customerVO.getPassword() != null ? customerVO.getPassword() : "");
				customer.setPhonenumber(customerVO.getPhonenumber() != null ? customerVO.getPhonenumber() : null);
				customer.setEmailid(customerVO.getEmailid());
				try {
					customerRepository.save(customer);
					restonzaRestResponseVO = new RestonzaRestResponseVO("success", customer);
				} catch (Exception e) {
					if (e instanceof DataIntegrityViolationException) {
						restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Records already present!");
					} else {
						restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error in adding records!");
					}
				}
			}
			} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Please Provice all data");
		}
		return restonzaRestResponseVO;
	}
	
	
	/**
	 * Customer forgot password (solely used by mobile application)
	 * @param mobileRequestVO
	 * @return
	 */
	@PostMapping("/forgotPassword")
	public @ResponseBody RestonzaRestResponseVO executeForgotPassword(@RequestBody CustomerVO customerVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(customerVO.getPhonenumber())){
			CustomerDAO daoResponse = customerRepository.getLoginDetails(customerVO.getPhonenumber());
			if (daoResponse !=null) {
				if (daoResponse.getEmailid()!=null || daoResponse.getEmailid().equalsIgnoreCase("")) {
					mailSenderUtility.asyncSend(daoResponse.getEmailid(), "Restonza: Forgot password!", "Your password for restonza account is:"+daoResponse.getPassword());
					restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Password has been sent to registered Email Id");
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Email id is not configured !");
				}
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Userid not found");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * Add customer in db (solely used by mobile application)
	 * @param mobileRequestVO
	 * @return
	 */
	@PostMapping("/addCustomer")
	public @ResponseBody RestonzaRestResponseVO executeAddCustomer(@RequestBody CustomerVO customerVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(customerVO.getCustomer_id()) 
				&& RestonzaUtility.isNullOrEmpty(customerVO.getDeviceid())) {
			// Check if customer is already present
			List<CustomerDAO> cust = customerRepository.isDeviceCustomerIdExist(customerVO.getCustomer_id(),customerVO.getPhonenumber());
			if (cust != null && !cust.isEmpty()) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", cust.get(0));
			} else {
				CustomerDAO customer = new CustomerDAO();
				customer.setCustomer_id(customerVO.getCustomer_id());
				customer.setFirst_name(customerVO.getFirst_name());
				customer.setLast_name(customerVO.getLast_name() != null ? customerVO.getLast_name() : "");
				customer.setDeviceid(customerVO.getDeviceid());
				customer.setPassword(customerVO.getPassword() != null ? customerVO.getPassword() : "");
				customer.setPhonenumber(customerVO.getPhonenumber() != null ? customerVO.getPhonenumber() : "");
				customer.setEmailid(customerVO.getEmailid());
				try {
					customerRepository.save(customer);
					restonzaRestResponseVO = new RestonzaRestResponseVO("success", customer);
				} catch (Exception e) {
					if (e instanceof DataIntegrityViolationException) {
						restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Records already present!");
					} else {
						restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error in adding records!");
					}
				}
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error in adding records!");
		}
	return restonzaRestResponseVO;
	}
	
	/**
	 * TO call waiter and water by customer(solely used by mobile application)
	 * @param mobileRequestVO
	 * @return
	 */
	@PostMapping("/callWaiterWater")
	public @ResponseBody RestonzaRestResponseVO executeCustomerCall(@RequestBody CustomerCallVO customerCallVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(customerCallVO.getQrcode()) 
				&& RestonzaUtility.isNullOrEmpty(customerCallVO.getCalled_customer_id())
				&& RestonzaUtility.isNullOrEmpty(customerCallVO.getCall_type())) {
			CustomerCallDAO call = new CustomerCallDAO();
			call.setCall_type(customerCallVO.getCall_type().toUpperCase());
			call.setCalled_customer_id(customerCallVO.getCalled_customer_id());
			String[] output = customerCallVO.getQrcode().split("-");
			String hotelId= output[0];
			String tableno = output[1];
			call.setCust_call_hotel_id(Integer.parseInt(hotelId));
			sendPush(hotelId,customerCallVO.getCall_type().toUpperCase(),tableno);
			call.setTable_no(tableno);
			call.setStatus("applied");
			try {
				customerCallRepository.save(call);
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Success!");
			} catch (Exception e){
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * Sends Web push notification to Admin
	 * @param hotelid
	 * @param type
	 * @param tableid
	 */
	public void sendPush(String hotelid,String type,String tableid){
		List<String> hotelListIds = webPushNotifierRepository.getUserByHotelId(Integer.valueOf(hotelid));
		String message = null;
		if (hotelListIds!=null && !hotelListIds.isEmpty()) {
			List<String> send = new ArrayList<String>();
			for (String s :hotelListIds) {
				send.add('"'+s+'"');
			}
			if (type.equalsIgnoreCase(RestonzaUtility.WAITER)) {
				message = RestonzaUtility.WAITER_MSG + tableid; 
			} else if (type.equalsIgnoreCase(RestonzaUtility.WATER)) {
				message = RestonzaUtility.WATER_MSG + tableid; 
			} else if (type.equalsIgnoreCase(RestonzaUtility.PLACE_ORDER)) {
				message = RestonzaUtility.PLACE_ORDER_MSG + tableid; 
			}
			RestonzaUtility.sendWebPush(send,"Type",'"'+message+'"');
		}
	}
	/**
	 * To Fetch all categories in hotel
	 * @param mobileRequestVO
	 * @return
	 */
	@PostMapping("/getCategories")
	public @ResponseBody RestonzaRestResponseVO executeGetMobileDishCategories(@RequestBody MobileRequestVO mobileRequestVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (mobileRequestVO!=null) {
			
			if (mobileRequestVO.getQrcode()!=null && !mobileRequestVO.getQrcode().isEmpty()) {
				// QR-CODE 1001-10/1001- hotelid and 10-table id
				List<DishCategories> categories = null;
				try {
					StringTokenizer qrcodeToken = new StringTokenizer(mobileRequestVO.getQrcode());
					String hotelId = qrcodeToken.nextToken("-");
					categories = dishCategoriesRepository.findCategories(Integer.parseInt(hotelId));
					if (categories.isEmpty()) {
						return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Category information available");
					}
				} catch (Exception e) {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Category information available");
				}
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", categories);
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Category information available");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * To Fetch all dishes
	 * @param mobileRequestVO
	 * @return
	 */
	@PostMapping("/getGetMenuItems")
	public @ResponseBody RestonzaRestResponseVO executeGetMenuItems(@RequestBody MobileRequestVO mobileRequestVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		
		if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCategoryName()) &&
				RestonzaUtility.isNullOrEmpty(mobileRequestVO.getQrcode())) {
			String[] output = mobileRequestVO.getQrcode().split("-");
			String hotelId= output[0];
			//String tableno = output[1];
			try {
				List<Dishes> dishesList = hotelDishesRepositories.getDishes(Integer.parseInt(hotelId), mobileRequestVO.getCategoryName());
				/*for (Dishes dishes : dishesList) {
					dishes.setImage(new byte[0]);
				}*/
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", dishesList);
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Dish information available");
		}
		return restonzaRestResponseVO;
	}
	
	
	/**
	 * To Fetch a dish
	 * @param DishesVO
	 * @return
	 */
	@PostMapping("/getGetMenu")
	public @ResponseBody RestonzaRestResponseVO executeGetMenuDetail(@RequestBody DishesVO dishesVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (dishesVO.getDish_id() > -1 && dishesVO.getHotel() > -1) {
			try {
				Dishes dishes= hotelDishesRepositories.findDish(dishesVO.getHotel(), dishesVO.getDish_id());
				if (dishes != null){
					restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", dishes);
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Dish information available");
				}
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Dish information available");
		}
		return restonzaRestResponseVO;
	}
	
	
	/**
	 * To post feedback in db
	 * @param 
	 * @return
	 */
	@PostMapping("/postFeedback")
	public @ResponseBody RestonzaRestResponseVO executePostFeedback(@RequestBody MobileRequestVO mobileRequestVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (mobileRequestVO!=null) {
			
			if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getQrcode()) 
					&& RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCustomerName())
					&& RestonzaUtility.isNullOrEmpty(mobileRequestVO.getFeedbackdescription())
					&& RestonzaUtility.isNullOrEmpty(mobileRequestVO.getRating())
					&& RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCutomerid())) {
				try {
					StringTokenizer qrcodeToken = new StringTokenizer(mobileRequestVO.getQrcode());
					String hotelId = qrcodeToken.nextToken("-");
					FeedBack feedback = new FeedBack();
					feedback.setCustomer_name(mobileRequestVO.getCustomerName());
					feedback.setFeedback_comments(mobileRequestVO.getFeedbackdescription());
					feedback.setHotel_id(Integer.parseInt(hotelId));
					feedback.setPoint(Integer.parseInt(mobileRequestVO.getRating()));
					feedback.setFeedback_customer_id(mobileRequestVO.getCutomerid());
					feedback.setFood_rating(Integer.parseInt(mobileRequestVO.getFood_rating() != null ? mobileRequestVO.getFood_rating() : "0"));
					feedback.setService_rating(Integer.parseInt(mobileRequestVO.getService_rating() != null ? mobileRequestVO.getService_rating() : "0"));
					feedback.setAmbience_rating(Integer.parseInt(mobileRequestVO.getAmbience_rating() != null ? mobileRequestVO.getAmbience_rating() : "0"));
					feedbackRepository.save(feedback);
				} catch (Exception e) {
					e.printStackTrace();
					return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Problem In Saving Feedback");
				}
				return restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Feedback received");
			}
		} else {
			return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Problem In Saving Feedback in correct parameters");
		}
		return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Problem In Saving Feedback in correct parameters");
	}
	
	
	/**
	 * This method will check the if payment is done or not.
	 * @param 
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/paymentStatus")
	public @ResponseBody RestonzaRestResponseVO executeCheckPaymentStatus(@RequestBody MobileRequestVO mobileRequestVO) throws Exception{
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCutomerid()) &&
				RestonzaUtility.isNullOrEmpty(mobileRequestVO.getQrcode())) {
			String[] output = mobileRequestVO.getQrcode().split("-");
			int hotelId= Integer.parseInt(output[0]);
			int tableno = Integer.parseInt(output[1]);
			String customerId = mobileRequestVO.getCutomerid();
			List<String> orderStatus = orderDetailsRepository.getOrderStatus(hotelId, tableno, customerId,"billed");
			List<Map<String, Object>> barOrderStatus = barOrderManagementRepository.getBarOrderPaymentStatus(String.valueOf(hotelId), String.valueOf(tableno), customerId,"billed");
			String paymentstatus = "Paid";
			if ((orderStatus != null && orderStatus.size() > 0) || (barOrderStatus != null && barOrderStatus.size() > 0)) {
				paymentstatus = "Unpaid";
			}
			try {
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", paymentstatus);
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * This method will fetch feedback history for single customer.
	 * @param 
	 * @return
	 */
	@PostMapping("/getFeedbackHistory")
	public @ResponseBody RestonzaRestResponseVO executeGetFeedbackHistory(@RequestBody MobileRequestVO mobileRequestVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCutomerid())) {
			try {
				List<FeedBack> feedbackList = feedbackRepository.getFeedbackHistory(mobileRequestVO.getCutomerid());
				if (!feedbackList.isEmpty()) {
				 restonzaRestResponseVO = new RestonzaRestResponseVO("success", feedbackList);
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Feedbacks available");
				}
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Feedbacks available");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Feedbacks available");
		}
		return restonzaRestResponseVO;
	}
	
	
	/**
	 * This method will fetch particular order history for single customer.
	 * @param 
	 * @return
	 */
	@PostMapping("/getOrderHistoryDetails")
	public @ResponseBody RestonzaRestResponseVO executeGetOrderHistoryDetails(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (!RestonzaUtility.isNullOrEmpty(orderDetailsVO.getOrder_id())) {
			try {
				List<Object> foodOrderDetails = new ArrayList<Object>();
				List<Object> barOrderDetails = new ArrayList<Object>();
				String[] barorderIds = orderDetailsVO.getBar_order_ids().split(",");
				Map<String, Object> output = new HashMap<>();
				if (orderDetailsVO.getFood_order_ids() != null && !orderDetailsVO.getFood_order_ids().isEmpty()) {
						String[] foodorderIds = orderDetailsVO.getFood_order_ids().split(",");
					for (String foodOrderId : foodorderIds) {
						List<Object> order = new ArrayList<Object>();
						OrderDetails foodOrdeDetail = orderDetailsRepository.getOrderDetail(Integer.parseInt(foodOrderId));
						String orderSummary = foodOrdeDetail.getOrder_summary();
						String orderSumm[] = orderSummary.split("\\|");
						 for (String orderdetails :orderSumm) {
							 GeneralOrderVO genralOrderVo = new GeneralOrderVO();
							 String entity[] = orderdetails.split("\\,");
							 String dishname = entity[0].split("\\=")[1];
							 double price = hotelDishesRepositories.getDishPrice(foodOrdeDetail.getHotel_id(), dishname);
							 genralOrderVo.setPrice(price);
							 String qty = entity[1].split("\\=")[1];
							 genralOrderVo.setItemName(dishname);
							 genralOrderVo.setOrderqty(qty);
							 order.add(genralOrderVo);
						 }
						 foodOrdeDetail.setList(order);
						foodOrderDetails.add(foodOrdeDetail);
					}
				}
				if (orderDetailsVO.getBar_order_ids() != null && !orderDetailsVO.getBar_order_ids().isEmpty()) {
					for (String barOrderId : barorderIds) {
						List<Map<String, Object>> barOrdeDetail = barOrderManagementRepository.getItemById(barOrderId);
						for (Map<String, Object> singleOrder: barOrdeDetail) {
							String barItemName = (String) singleOrder.get("bar_item_name");
							String id = singleOrder.get("id").toString();
							String qty = singleOrder.get("qty").toString();
							double price = (Double) singleOrder.get("price");
							String orderStatus = (String) singleOrder.get("order_status");
							String updatedTime =  singleOrder.get("updated_time").toString();
							BarOrderResponse barOrderres = new BarOrderResponse(barItemName,id,qty,price,orderStatus,updatedTime);
							barOrderDetails.add(barOrderres);
						}
					}
				}
				output.put("foodOrderHistory", foodOrderDetails);
				output.put("barOrderHistory", barOrderDetails);
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", output);
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order detail available");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order detail available");
		}
		return restonzaRestResponseVO;
	}
	

	/**
	 * This method will order list history on single customer .
	 * @param 
	 * @return
	 */
	@PostMapping("/getOrderHistoryDetailsList")
	public @ResponseBody RestonzaRestResponseVO executeGetOrderHistoryDetails(@RequestBody MobileRequestVO mobileRequestVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCutomerid())) {
			//StringTokenizer qrcodeToken = new StringTokenizer(mobileRequestVO.getQrcode());
			try {
				 List<OrderHistoryDetailsVO> listAllOrderVOs = hotelAnalyzerRepository.getBillingHistoryByCustomer(mobileRequestVO.getCutomerid());
				 //List<OrderDetails> orderDetailsList = orderDetailsRepository.getOrderHistoryList(Integer.parseInt(hotelId),mobileRequestVO.getCutomerid());
				if (!listAllOrderVOs.isEmpty()) {
				 restonzaRestResponseVO = new RestonzaRestResponseVO("success", listAllOrderVOs);
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order history available");
				}
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order history available");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order history available");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * This method will fetch hotel details.
	 * @param 
	 * @return
	 */
	@PostMapping("/getHotelInfo")
	public @ResponseBody RestonzaRestResponseVO executeGetHotelInfo(@RequestBody MobileRequestVO mobileRequestVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getQrcode())) {
			StringTokenizer qrcodeToken = new StringTokenizer(mobileRequestVO.getQrcode());
			String hotelId = qrcodeToken.nextToken("-");
			try {
				 HotelDetails hoteldetails = hotelDetailsRepository.getHotel(Integer.parseInt(hotelId));
				if (hoteldetails != null) {
				 restonzaRestResponseVO = new RestonzaRestResponseVO("success", hoteldetails);
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", new HotelDetails(0, "", "", 0, "", new Date(0), new Date(0), "", "", false, false, ""));
				}
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error",  new HotelDetails(0, "", "", 0, "", new Date(0), new Date(0), "", "", false, false, ""));
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error",  new HotelDetails(0, "", "", 0, "", new Date(0), new Date(0), "", "", false, false, ""));
		}
		return restonzaRestResponseVO;
	}
	
	
	

	/**
	 * post request for adding new order
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/addOrder")
	public @ResponseBody RestonzaRestResponseVO executeAddOrder(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		OrderDetails orderDetails = new OrderDetails();
		try {
			if (orderDetailsVO != null) {
				boolean flag = validateHotelTable(Integer.parseInt(orderDetailsVO.getHotel_id()), Integer.parseInt(orderDetailsVO.getTable_id()));
				if (!flag) {
					return new RestonzaRestResponseVO("error", "Selected Table Does't Exist !");
				}
				orderDetails.prepareOrderDetails(orderDetails, orderDetailsVO);
				orderDetails = orderDetailsRepository.save(orderDetails);
				hotelAnalyzerRepository.insertBackKitchenOrderDetails(orderDetailsVO, orderDetails.getId());
				hotelAnalyzerRepository.updatePeopleSttingOnTable(orderDetailsVO.getNumberOfPeopleSittingOnTable(),
						orderDetailsVO.getHotel_id(),orderDetailsVO.getTable_id());
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", orderDetails.getId());
				sendPush(String.valueOf(orderDetails.getHotel_id()), RestonzaUtility.PLACE_ORDER,String.valueOf(orderDetails.getTable_id()));
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong! Please call administrator");
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong! Please call administrator");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request to update the placed order
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/updateOrderDetails")
	public @ResponseBody RestonzaRestResponseVO executeUpdateOrderDetails(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String checkNullFields[] = {"order_id", "orderDetails", "amt"};
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(orderDetailsVO, checkNullFields)) {
				int order_id = Integer.parseInt(orderDetailsVO.getOrder_id());
				String order_summary = orderDetailsVO.iterateOrderDetails(orderDetailsVO.getOrderDetails()).toString();
				orderDetailsRepository.updateOrder(order_summary, orderDetailsVO.getAmt(), order_id);
				restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", "successfully updated order" + order_id);
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Something went wrong");
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "No records found");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request to cancel the placed request
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/cancelOrder")
	public @ResponseBody RestonzaRestResponseVO executeCancelOrder(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String checkNullFields[] = {"order_id"};
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(orderDetailsVO, checkNullFields)) {
				int order_id = Integer.parseInt(orderDetailsVO.getOrder_id());
				orderDetailsRepository.cancelOrder(RESTONZACONSTANTS.cancel_order.toString(), order_id);
				restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", "sucessfully deleted");
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Something went wrong");
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "No records found");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting ads details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/approveCall")
	public @ResponseBody RestonzaRestResponseVO executeApproveCall(@RequestBody AdsDetailsVO adsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		customerCallRepository.delete(Integer.parseInt(adsDetailsVO.getId()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully approved customer call");
		return restonzaRestResponseVO;
	}
	
	/**
	 * Dummy test url for checking push notification : campaign
	 * @return
	 */
	@PostMapping("/campaignPushNotification")
	public @ResponseBody RestonzaRestResponseVO executeCampaignPushNotification(){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<String> listOfCustomers = customerRepository.getAllCustomerDeviceIds();
		try {
			for (String customerDeviceId : listOfCustomers) {
				if (customerDeviceId != null) {
					restonzaPushNotificationService.sendPushNotification(customerDeviceId, "Campaign Title", "Campaign Description");
				}
			}
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully sent push notification");
		} catch (IOException e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "While Approving Campaign");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/addBarCategory")
	public @ResponseBody RestonzaRestResponseVO executeAddBarCategory(@Validated(BarMenuRequestVO.CategoryRequest.class)@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		String barCategoryName = barMenuRequestVO.getBar_category_name();
		int hotelId = barMenuRequestVO.getHotel_id();
		Integer id = barCategoryRepository.isCategoryExists(barCategoryName, hotelId);
		if (null != id) 
			throw new RestonzaCustomException("Category Already exists");
		BarCategory barCategory = new BarCategory();
		barCategory.setBar_category_name(barCategoryName);
		barCategory.setBar_sub_category(String.join(",", barMenuRequestVO.getBar_sub_category()));
		barCategory.setHotel_id(hotelId);
		barCategory.setStatus(barMenuRequestVO.isStatus());
		BarCategory cat = barCategoryRepository.save(barCategory);
		return new RestonzaRestResponseVO("success", cat);
		
	}
	
	@PostMapping("/addBarOrder")
	public @ResponseBody RestonzaRestResponseVO executeAddBarOrder(@RequestBody BarOrderManagementVO barOrderManagementVO) throws Exception {
		boolean previousOrderExist = false;
		String [] qrcode = barOrderManagementVO.getQrcode().split("-");
		boolean flag = validateHotelTable(Integer.parseInt(qrcode[0]), Integer.parseInt(qrcode[1]));
		if (!flag) {
			return  new RestonzaRestResponseVO("error", "Selected Table Does't Exist !");
		}
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
		hotelAnalyzerRepository.updatePeopleSttingOnTable(barOrderManagementVO.getNumberOfPeopleSittingOnTable(),
				qrcode[0], qrcode[1]);
		return new RestonzaRestResponseVO("success", "Bar Order Added Successfully");
	}
	
	
	/**
	 * billApi 05/08/2017
	 */
	@PostMapping("/getBill")
	public @ResponseBody RestonzaRestResponseVO executeGetBillApi(@RequestBody MobileRequestVO mobileRequestVO){
		if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getQrcode()) && RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCutomerid())) {
			String[] output = mobileRequestVO.getQrcode().split("-");
			int hotelId= Integer.parseInt(output[0]);
			int tableno = Integer.parseInt(output[1]);
			String customerId = mobileRequestVO.getCutomerid();
			GenerateBillVO billDetails = hotelAnalyzerRepository.getBillDetailsForMobileBillApi(hotelId, tableno, customerId);
			if (null != billDetails) {
				//get hotel dishes--------------------------------------------------------------------------
				HashMap<String, Double> dishNames = hotelAnalyzerRepository.getDishAndPriceMapping(hotelId);
				//get tax details---------------------------------------------------------------------------
				List<TaxSetting> taxSetting = taxSettingRepository.getTax(hotelId, true,false); 
				Map<String,Double> taxNameValue = new HashMap<>();
				double taxPercentage=0;
				if (taxSetting != null && !taxSetting.isEmpty()) {
					for (TaxSetting tax: taxSetting) {
						taxPercentage += tax.getPercentage();
						taxNameValue.put(tax.getTaxname(), tax.getPercentage());
					}
				}
				double ltaxPercentage=0;
				List<TaxSetting> ltaxSetting = taxSettingRepository.getTax(hotelId, true, true);
				Map<String,Double> ltaxNameValue = new HashMap<>();
				if (ltaxSetting != null && !ltaxSetting.isEmpty()) {
					for (TaxSetting tax: ltaxSetting) {
						ltaxPercentage += tax.getPercentage();
						ltaxNameValue.put(tax.getTaxname(), tax.getPercentage());
					}
				}
				
				//get hotel dishes images--------------------------------------------------------------------------
				HashMap<String, String> dishImages = hotelAnalyzerRepository.getDishAndImgMapping(hotelId);
				//build output------------------------------------------------------------------------------
				long final_amt =0;
				double taxAmount = 0;
				double btaxAmount = 0;
				/*double bill_amt = billDetails.getFoodbillamt();
				if (taxPercentage > 0) {
					taxAmount = (bill_amt * taxPercentage)/100;
					final_amt = Math.round(bill_amt + taxAmount);
				} else  {
					 final_amt = Math.round(bill_amt);
				}*/
				String foodamount = billDetails.getFoodbillamt().equals("null") ? "0" : billDetails.getFoodbillamt();
				String baramount = billDetails.getBarbillamt().equals("null") ? "0" : billDetails.getBarbillamt();
				taxAmount = (Double.parseDouble(foodamount)* taxPercentage)/100;
				Long foodAmt = Math.round(Double.parseDouble(foodamount) + taxAmount);
				btaxAmount = (Double.parseDouble(baramount)* ltaxPercentage)/100;
				Long barAmt = Math.round(Double.parseDouble(baramount) + btaxAmount);
				final_amt = foodAmt + barAmt;
				PrintVO printvodetails = new PrintVO();
				printvodetails.setHotel_id(String.valueOf(hotelId));
				printvodetails.setTable_id(String.valueOf(tableno));
				printvodetails.setOrder_id(billDetails.getOrderids());
				printvodetails.setAmt(String.valueOf(final_amt));
				printvodetails.setTaxes(taxNameValue);
				printvodetails.setLtaxes(ltaxNameValue);
				printvodetails.setFood_order_ids(billDetails.getFood_order_ids());
				printvodetails.setBar_order_ids(billDetails.getBar_order_ids());
				
				
				List<PlacedOrderDetailsVO> dishDetails = new ArrayList<PlacedOrderDetailsVO>();
				List<PlacedBarOrderDetails> barDetails = new ArrayList<PlacedBarOrderDetails>();
				if(RestonzaUtility.isNullOrEmpty(billDetails.getFormatOrderSummary())) {
					String orderSummary[] = billDetails.getFormatOrderSummary().split(",");
					for (String singleOrder : orderSummary) {
						PlacedOrderDetailsVO placeOrderDetailsVO = new PlacedOrderDetailsVO();
						String[] dishnameqty = singleOrder.split("-");
						placeOrderDetailsVO.setDishname(dishnameqty[0]);
						placeOrderDetailsVO.setQty(dishnameqty[1]);
						placeOrderDetailsVO.setPrice(String.valueOf(dishNames.get(dishnameqty[0])));
						placeOrderDetailsVO.setDishimg(dishImages.get(dishnameqty[0]));
						dishDetails.add(placeOrderDetailsVO);
					}
				}
				
				if(RestonzaUtility.isNullOrEmpty(billDetails.getFormatBarOrderSummary())) {
					String foodOrderSummary[] = billDetails.getFormatBarOrderSummary().split(",");
					for (String singleOrder : foodOrderSummary) {
						PlacedBarOrderDetails placeBarOrderDetailsVO = new PlacedBarOrderDetails();
						String[] dishnameqty = singleOrder.split("-");
						placeBarOrderDetailsVO.setBar_item_name(dishnameqty[0]);
						placeBarOrderDetailsVO.setQty(Integer.parseInt(dishnameqty[1]));
						placeBarOrderDetailsVO.setBarimg(dishImages.get(dishnameqty[0]));
						placeBarOrderDetailsVO.setPrice(Double.valueOf(dishNames.get(dishnameqty[0])));
						barDetails.add(placeBarOrderDetailsVO);
					}
				}
				
				printvodetails.setOrderDetails(dishDetails);
				printvodetails.setBarOrderDetail(barDetails);
				//end
				return new RestonzaRestResponseVO("SUCCESS", printvodetails);
			} else {
				return new RestonzaRestResponseVO("FAIL", "Bill Not Available");
			}
		} else {
			return new RestonzaRestResponseVO("FAIL", "Invalid Request. Please contact administrator");
		}
	}
	
	
	@PostMapping("/getBarOrderDetails")
	public @ResponseBody RestonzaRestResponseVO executeGetBarOrderDetails(@RequestBody BarOrderManagementVO barOrderManagementVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		Integer barOrderId = barOrderManagementVO.getParent_order_id();
		if (null != barOrderId) {
			try {
				List<Map<String, Object>> listAllOrderVOs = barOrderManagementRepository.getBarOrdersById(barOrderId);
				 //List<OrderDetails> orderDetailsList = orderDetailsRepository.getOrderHistoryList(Integer.parseInt(hotelId),mobileRequestVO.getCutomerid());
				if (!listAllOrderVOs.isEmpty()) {
				 restonzaRestResponseVO = new RestonzaRestResponseVO("success", listAllOrderVOs);
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order history available");
				}
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order history available");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Order history available");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getHotelWiseOrderHistory")
	public @ResponseBody RestonzaRestResponseVO executeHotelWiseHistory(@RequestBody MobileRequestVO mobileRequestVO){
		if (RestonzaUtility.isNullOrEmpty(mobileRequestVO.getQrcode()) && RestonzaUtility.isNullOrEmpty(mobileRequestVO.getCutomerid())) {
			String[] output = mobileRequestVO.getQrcode().split("-");
			int hotelId= Integer.parseInt(output[0]);
			int tableno = Integer.parseInt(output[1]);
			String customerId = mobileRequestVO.getCutomerid();
			GenerateBillVO billDetails = hotelAnalyzerRepository.getBillDetailsForMobileBillApi(hotelId, tableno, customerId);
			if (null != billDetails) {
				//get hotel dishes--------------------------------------------------------------------------
				HashMap<String, Double> dishNames = hotelAnalyzerRepository.getDishAndPriceMapping(hotelId);
				//get tax details---------------------------------------------------------------------------
				List<TaxSetting> taxSetting = taxSettingRepository.getTax(hotelId, true, false); //TODO:
				Map<String,Double> taxNameValue = new HashMap<>();
				double taxPercentage=0;
				if (taxSetting != null && !taxSetting.isEmpty()) {
					for (TaxSetting tax: taxSetting) {
						taxPercentage += tax.getPercentage();
						taxNameValue.put(tax.getTaxname(), tax.getPercentage());
					}
				}
				
				double ltaxPercentage=0;
				List<TaxSetting> ltaxSetting = taxSettingRepository.getTax(hotelId, true, true); //TODO:
				Map<String,Double> ltaxNameValue = new HashMap<>();
				if (ltaxSetting != null && !ltaxSetting.isEmpty()) {
					for (TaxSetting tax: ltaxSetting) {
						ltaxPercentage += tax.getPercentage();
						ltaxNameValue.put(tax.getTaxname(), tax.getPercentage());
					}
				}
				//get hotel dishes images--------------------------------------------------------------------------
				HashMap<String, String> dishImages = hotelAnalyzerRepository.getDishAndImgMapping(hotelId);
				
				//build output------------------------------------------------------------------------------
				long final_amt =0;
				double taxAmount = 0;
				double bill_amt = billDetails.getSum();
				if (taxPercentage > 0) {
					taxAmount = (bill_amt * taxPercentage)/100;
					final_amt = Math.round(bill_amt + taxAmount);
				} else  {
					 final_amt = Math.round(bill_amt);
				}
				
				PrintVO printvodetails = new PrintVO();
				printvodetails.setHotel_id(String.valueOf(hotelId));
				printvodetails.setTable_id(String.valueOf(tableno));
				printvodetails.setOrder_id(billDetails.getOrderids());
				printvodetails.setAmt(String.valueOf(final_amt));
				printvodetails.setFood_order_ids(billDetails.getFood_order_ids());
				printvodetails.setBar_order_ids(billDetails.getBar_order_ids());
				printvodetails.setTaxes(taxNameValue);;
				String orderSummary[] = billDetails.getFormatOrderSummary().split(",");
				List<PlacedOrderDetailsVO> dishDetails = new ArrayList<PlacedOrderDetailsVO>();
				for (String singleOrder : orderSummary) {
					PlacedOrderDetailsVO placeOrderDetailsVO = new PlacedOrderDetailsVO();
					String[] dishnameqty = singleOrder.split("-");
					placeOrderDetailsVO.setDishname(dishnameqty[0]);
					placeOrderDetailsVO.setQty(dishnameqty[1]);
					placeOrderDetailsVO.setPrice(String.valueOf(dishNames.get(dishnameqty[0])));
					placeOrderDetailsVO.setDishimg(dishImages.get(dishnameqty[0]));
					dishDetails.add(placeOrderDetailsVO);
				}
				printvodetails.setOrderDetails(dishDetails);
				//end
				return new RestonzaRestResponseVO("SUCCESS", printvodetails);
			} else {
				return new RestonzaRestResponseVO("FAIL", "Bill Not Available");
			}
		} else {
			return new RestonzaRestResponseVO("FAIL", "Invalid Request. Please contact administrator");
		}
	}
	
	@PostMapping("/payonline")
	public @ResponseBody RestonzaRestResponseVO executePayOnline(@RequestBody OrderDetailsVO orderdetailsvo) {
		String foodorderids = orderdetailsvo.getFood_order_ids();
		String barorderids = null;
		if (orderdetailsvo.getBar_order_ids() != null) {
			barorderids = orderdetailsvo.getBar_order_ids();
		}
		String hotelId = orderdetailsvo.getHotel_id(); 
		String tableId = orderdetailsvo.getTable_id();
		String customerIds = orderdetailsvo.getCustomer_id();
		try {
			hotelAnalyzerRepository.markOnlinePaymentStatus(hotelId,tableId,customerIds,foodorderids,barorderids);
		} catch (Exception e) {
			return new RestonzaRestResponseVO("FAIL", "Please Contact Hotel Admin!");
		}
		return new RestonzaRestResponseVO("SUCCESS", "Payment completed!");
	}
	
	@PostMapping("/isQRCodeValid")
	public @ResponseBody RestonzaRestResponseVO isQRValid(@RequestBody MobileRequestVO mobileRequestVO) {
		String [] qrcode = mobileRequestVO.getQrcode().split("-");
		boolean flag = validateHotelTable(Integer.parseInt(qrcode[0]), Integer.parseInt(qrcode[1]));
		if (!flag) {
			return  new RestonzaRestResponseVO("error", "Selected Table Doesn't Exist !");
		}
		return new RestonzaRestResponseVO("Success", "Selected Table is Valid !");
	}
	
	public boolean validateHotelTable(int hotelId, int tableId) {
		int id = hotelAnalyzerRepository.checkTableValid(hotelId,tableId);
		if (id > 0) {
			return true;
		}
		return false;
	}
}
