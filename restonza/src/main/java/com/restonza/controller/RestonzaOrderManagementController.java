/**
 * 
 */
package com.restonza.controller;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.BillingHistory;
import com.restonza.dao.CustomerCallDAO;
import com.restonza.dao.CustomerDAO;
import com.restonza.dao.HotelAnalyze;
import com.restonza.dao.HotelDetails;
import com.restonza.dao.OrderDetails;
import com.restonza.dao.TaxSetting;
import com.restonza.dao.repository.BillingHistoryRepository;
import com.restonza.dao.repository.CustomerCallRepository;
import com.restonza.dao.repository.CustomerRepository;
import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.dao.repository.HotelDetailsRepository;
import com.restonza.dao.repository.OrderDetailsRepository;
import com.restonza.dao.repository.TaxSettingRepository;
import com.restonza.util.service.MailSenderUtility;
import com.restonza.util.service.PrinterService;
import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaCommonAsynHandler;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.BillManagementVO;
import com.restonza.vo.GenerateBillVO;
import com.restonza.vo.GetOrderDetailsVO;
import com.restonza.vo.HotelDetailsVO;
import com.restonza.vo.OrderDetailsVO;
import com.restonza.vo.PlacedOrderDetailsVO;
import com.restonza.vo.PrintVO;
import com.restonza.vo.RestoPrintVO;
import com.restonza.vo.RestonzaRestResponseVO;

/**
 * @author flex-grow developers
 *
 */
@RestController
@RequestMapping("/restonza")
public class RestonzaOrderManagementController {
	@Autowired
	private HotelDetailsRepository hotelDetailsRepository;
	
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	@Autowired
	private BillingHistoryRepository billingHistoryRepository;
	
	@Autowired
	private CustomerCallRepository customerCallRepository;
	
	@Autowired
	private TaxSettingRepository taxSettingRepository;
	
	@Autowired
	private RestonzaCommonAsynHandler restonzaCommonAsynHandler;
	
	@Autowired
	private MailSenderUtility mailSenderUtility;
	
	@Autowired
	private CustomerRepository customerRepository;
	
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
				orderDetails.prepareOrderDetails(orderDetails, orderDetailsVO);
				orderDetails = orderDetailsRepository.save(orderDetails);
				hotelAnalyzerRepository.insertBackKitchenOrderDetails(orderDetailsVO, orderDetails.getId());
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully placed new order with order id: "+ orderDetails.getId());
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong! Please call administrator");
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong! Please call administrator");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for fetching all new orders associated to hotel and table
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/getOrderDetails")
	public @ResponseBody RestonzaRestResponseVO executeGetOrderDetails(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String checkNullFields[] = {"hotel_id", "table_id"};
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(orderDetailsVO, checkNullFields)) {
				int hotel_id = Integer.parseInt(orderDetailsVO.getHotel_id());
				int table_id = Integer.parseInt(orderDetailsVO.getTable_id());
				List<OrderDetails> getAllOrderidList = orderDetailsRepository.findOrders(hotel_id, table_id, RESTONZACONSTANTS.new_order.toString());
				if (!getAllOrderidList.isEmpty()) {
					List<GetOrderDetailsVO> ordersList = new ArrayList<>();
					for (OrderDetails orderDetails :getAllOrderidList) {
						String customerid = orderDetails.getCustomer_id();
						CustomerDAO custDao = customerRepository.getCustomerDetailsUsingId(customerid);
						if (custDao != null) {
							orderDetails.setCustomerName(custDao.getFirst_name());
						} else {
							orderDetails.setCustomerName("Hotel Admin");
						}
					}
					GetOrderDetailsVO orders = null;
					for (OrderDetails orderdetail : getAllOrderidList) {
						String ordersummary = orderdetail.getOrder_summary();
						orders = new GetOrderDetailsVO(orderdetail.getId(), ordersummary, RestonzaUtility.formatOrderSummaries(ordersummary), orderdetail.getCustomerName());
						ordersList.add(orders);
					}
					restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", ordersList);
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "No records found");
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Something Went Wrong");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "Something Went Wrong");
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
	 * Call this to get the current ordered for particular table 
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/getOrderDetailsForTable")
	public @ResponseBody RestonzaRestResponseVO getOrderDetailsForTable(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		//TODO tryccatch
		if (orderDetailsVO != null ) {
			if (RestonzaUtility.isNullOrEmpty(orderDetailsVO.getHotel_id())
				&& RestonzaUtility.isNullOrEmpty(orderDetailsVO.getStatus())) {
				List<OrderDetails> orderDetailsList = orderDetailsRepository.getAllOrders(Integer.parseInt(orderDetailsVO.getHotel_id()),orderDetailsVO.getStatus());
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", orderDetailsList);
			} else {
				restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Fetching Order Details");
			}
		} else {
			restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Fetching Order Details");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * Call this to get the current ordered for particular table 
	 * @param orderDetailsVO
	 * @return
	 */
	@SuppressWarnings("unused")
	@PostMapping("/getBilledOrder")
	public @ResponseBody RestonzaRestResponseVO executeGetBillDetail(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (orderDetailsVO != null && orderDetailsVO.getHotel_id() != null || !orderDetailsVO.getHotel_id().isEmpty()) {
			List<GenerateBillVO> orderDetailsList = hotelAnalyzerRepository.getCombineBillOrder(Integer.parseInt(orderDetailsVO.getHotel_id()));
			List<TaxSetting> taxSetting = taxSettingRepository.getTax(Integer.parseInt(orderDetailsVO.getHotel_id()), true, false); 
			Map<String,Double> taxNameValue = new HashMap<>();
			if (taxSetting != null && !taxSetting.isEmpty()) {
				for (TaxSetting tax: taxSetting) {
					taxNameValue.put(tax.getTaxname(), tax.getPercentage());
				}
			}
			
			List<TaxSetting> ltaxSetting = taxSettingRepository.getTax(Integer.parseInt(orderDetailsVO.getHotel_id()), true, true); 
			Map<String,Double> ltaxNameValue = new HashMap<>();
			if (ltaxSetting != null && !ltaxSetting.isEmpty()) {
				for (TaxSetting tax: ltaxSetting) {
					ltaxNameValue.put(tax.getTaxname(), tax.getPercentage());
				}
			}
			BillManagementVO billManagementVO = new BillManagementVO(orderDetailsList, taxNameValue,ltaxNameValue);
			return restonzaRestResponseVO = new RestonzaRestResponseVO("success", billManagementVO);
		} else {
			return restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Fetching Order Details");
		}
	}
	
	/**
	 * post request to update the table order from order management
	 * @param orderDetailsVO
	 * @return
	 */
	@SuppressWarnings("unused")
	@PostMapping("/updateOrderStatus")
	public @ResponseBody RestonzaRestResponseVO executeUpdateTableOrder(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (orderDetailsVO != null && orderDetailsVO.getHotel_id() != null && orderDetailsVO.getOrder_id() != null) {
			int order_id = Integer.parseInt(orderDetailsVO.getOrder_id());
			int hotel_id = Integer.parseInt(orderDetailsVO.getHotel_id());
			String status = orderDetailsVO.getStatus();
			orderDetailsRepository.updateOrderStatus(hotel_id,order_id,status,new Date());
			if (RESTONZACONSTANTS.cancel_order.toString().equals(status)) {
				status = "Canceled";
				//hotelAnalyzerRepository.deleteBackKitchenOrderDetails(Integer.valueOf(orderDetailsVO.getOrder_id()));
			} else if (RESTONZACONSTANTS.confirm_order.toString().equals(status)) {
				status = "Confirmed";
			} else if (RESTONZACONSTANTS.delivered_order.toString().equals(status)) {
				status = "Delivered";
			}
			restonzaCommonAsynHandler.sendPushNotification(order_id, status, "Order Status Change", "Your Order has been " + status);
			return restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Order Updated Successfully");
		} else {
			return restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Fetching Order Details");
		}
	}
	
	/**
	 * post request to billing order
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/dobilling")
	public @ResponseBody RestonzaRestResponseVO executeBillingOrder(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (orderDetailsVO != null) {
			if (null != orderDetailsVO.getFood_order_ids()) {
				String[] array = orderDetailsVO.getFood_order_ids().split(",");
				List<String> orderIds = Arrays.asList(array);
				hotelAnalyzerRepository.bulkUpdate(orderIds);
			}
			if (null != orderDetailsVO.getBar_order_ids()) {
				int barOrderIds = Integer.parseInt(orderDetailsVO.getBar_order_ids());
				hotelAnalyzerRepository.bulkUpdateBarOrders(barOrderIds);
			}
			String  barOrderCounterString = orderDetailsVO.getBar_order_count().equals("null") ? "0" : orderDetailsVO.getBar_order_count();
			Integer barOrderCounter = Integer.parseInt(barOrderCounterString);
			String baramount = orderDetailsVO.getBar_bill_amt();
			String foodamount = orderDetailsVO.getFood_bill_amt();
			BillingHistory billingHistory = new BillingHistory();
		try {
			List<HotelAnalyze> listHotelAnalyze = new ArrayList<>();
			for (PlacedOrderDetailsVO placeOrderDetailsVO : orderDetailsVO.getOrderDetails()) {
				HotelAnalyze hotelAnalyze = new HotelAnalyze(orderDetailsVO.getOrder_id(), Integer.parseInt(orderDetailsVO.getHotel_id()),orderDetailsVO.getCustomer_id(),
						placeOrderDetailsVO.getDishname(),0, Integer.parseInt(placeOrderDetailsVO.getQty()));
				listHotelAnalyze.add(hotelAnalyze);
			}
			hotelAnalyzerRepository.bulkSave(listHotelAnalyze);
			//print bill receipt addition-------------------------------------------------------------
			//fetch hotel information
			int hotel_id = Integer.parseInt(orderDetailsVO.getHotel_id());
			HotelDetails hotelDetails = hotelDetailsRepository.findOne(hotel_id);
			String hotelName = hotelDetails.getHotel_name();
			String hotelAddress = hotelDetails.getHotel_address();
			
			//fetch tax details : Food tax :TODO
			List<TaxSetting> taxSetting = taxSettingRepository.getTax(Integer.parseInt(orderDetailsVO.getHotel_id()), true, false); //get food tax details
			Map<String,Double> taxNameValue = new HashMap<>();
			if (taxSetting != null && !taxSetting.isEmpty()) {
				for (TaxSetting tax: taxSetting) {
					taxNameValue.put(tax.getTaxname(), tax.getPercentage());
				}
			}
			
			//fetch tax details : Liquor tax : TODO
			List<TaxSetting> liquorTaxSetting = taxSettingRepository.getTax(Integer.parseInt(orderDetailsVO.getHotel_id()), true, true); //get food tax details
			Map<String,Double> liquorTaxNameValue = new HashMap<>();
			if (liquorTaxSetting != null && !liquorTaxSetting.isEmpty()) {
				for (TaxSetting tax: liquorTaxSetting) {
					liquorTaxNameValue.put(tax.getTaxname(), tax.getPercentage());
				}
			}
			
			StringBuilder builder = new StringBuilder();
			double taxamount = 0;
			for (Map.Entry<String, Double> entry : taxNameValue.entrySet())
			{
				builder.append(entry.getKey()+"+");
				taxamount += entry.getValue();
			}
			
			//bar tax : TODO
			StringBuilder lbuilder = new StringBuilder();
			double liquorTaxAmount = 0;
			for (Map.Entry<String, Double> entry : liquorTaxNameValue.entrySet())
			{
				lbuilder.append(entry.getKey()+"+");
				liquorTaxAmount += entry.getValue();
			}
			
			//dish item creation--------------------------------------------------------------------
			List<PlacedOrderDetailsVO> allDishDetails = orderDetailsVO.getOrderDetails();
			int orderCount = 0;
			Object[][] dishArray = new Object[allDishDetails.size()][1];
			for (PlacedOrderDetailsVO placedOrderDetailsVO : allDishDetails) {
				String[] dishDetail= new String[4];
				dishDetail[0] = placedOrderDetailsVO.getDishname();
				dishDetail[1] = placedOrderDetailsVO.getQty();
				dishDetail[2] = placedOrderDetailsVO.getPrice();
				dishDetail[3] = String.valueOf(Integer.parseInt(dishDetail[1]) * Integer.parseInt(dishDetail[2]));
				dishArray[orderCount] = dishDetail;
				orderCount++;
			}
			//Store the billing data into billing_history
			billingHistory = billingHistory.prepareBillingHistory(billingHistory, orderDetailsVO, taxamount, liquorTaxAmount);
			billingHistoryRepository.save(billingHistory);
			hotelAnalyzerRepository.updatePeopleSttingOnTable(0,orderDetailsVO.getHotel_id(), orderDetailsVO.getTable_id());
			sendEmail(dishArray, hotelDetails,taxNameValue,liquorTaxNameValue,billingHistory,orderDetailsVO.getTable_id(),barOrderCounter);
			PrinterService ps = new PrinterService();
			ps.setItems(dishArray, hotelName, hotelAddress, orderDetailsVO.getOrder_id(), String.valueOf(orderDetailsVO.getAmt()),builder,lbuilder,taxamount, liquorTaxAmount, barOrderCounter, foodamount, baramount);
			PrinterJob pj = PrinterJob.getPrinterJob();
			pj.setPrintable(new PrinterService.MyBarPrintable(), ps.getPageFormat(pj));
			pj.print();
			//end-------------------------------------------------------------------------------------
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully billed order with order no as " + orderDetailsVO.getOrder_id());
		} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Successfully billed order! Please connect the printer.");
		}
	} else {
		restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong! Please call administrator");
	}
		return restonzaRestResponseVO;
  }
	
	private void sendEmail(Object[][] dishArray, HotelDetails hotelDetails,Map<String,Double> taxNameValue,
			Map<String,Double> liquorTaxNameValue,BillingHistory billingHistory, String tableid, Integer barOrderCounter) {
		CustomerDAO customerDetails = customerRepository.getCustomerDetailsUsingId(billingHistory.getCustomer_id());
		//List<GeneralOrderVO> foodlist = hotelAnalyzerRepository.getFoodOrderDetailsByOrderId(billingHistory.getFood_order_ids());
		//PrepareFoodOrderList
		Integer foodCount= dishArray.length - barOrderCounter;
		List<String[]> foodOrderList = new LinkedList<>();
		List<String[]> barOrderList = new LinkedList<>();
		for (int i = 0; i < foodCount; i++) {
			String[] fooddishDetail= new String[4];
			fooddishDetail[0] = (String) dishArray[i][0];
			fooddishDetail[1] = (String) dishArray[i][1];
			fooddishDetail[2] = (String) dishArray[i][2];
			fooddishDetail[3] = (String) dishArray[i][3];
			foodOrderList.add(fooddishDetail);
		}
		for (int j = foodCount; j < dishArray.length; j++) {
			String[] bardishDetail= new String[4];
			bardishDetail[0] = (String) dishArray[j][0];
			bardishDetail[1] = (String) dishArray[j][1];
			bardishDetail[2] = (String) dishArray[j][2];
			bardishDetail[3] = (String) dishArray[j][3];
			barOrderList.add(bardishDetail);
		}
		String tableContent = buildTableContent(foodOrderList, barOrderList,taxNameValue,liquorTaxNameValue, billingHistory);
		String header = getHeaderSectionForTable(hotelDetails,customerDetails,tableid,billingHistory,tableContent);
		if (customerDetails!=null) {
			mailSenderUtility.asyncSend(customerDetails.getEmailid(), "Billy: Innovice", header);
		}
		
	}
	
	
	private String getHeaderSectionForTable(HotelDetails hotelDetails,
			CustomerDAO customerDetails,String tableid,BillingHistory billingHistory,String tableContent) {
		tableContent = tableContent.replace("**hotelname", hotelDetails.getHotel_name());
		tableContent = tableContent.replace("**address", hotelDetails.getHotel_address());
		String usernameandmobileno = "";
		if (customerDetails != null) {
			usernameandmobileno = customerDetails.getPhonenumber();
		}
		tableContent = tableContent.replace("**usernameandmobileno", usernameandmobileno);
		tableContent = tableContent.replace("**customerId", customerDetails.getCustomer_id());
		tableContent = tableContent.replace("**username", customerDetails.getFirst_name()+" "+customerDetails.getLast_name());
		tableContent = tableContent.replace("**tableno", tableid);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		tableContent = tableContent.replace("**inoviceDate",dateFormat.format(billingHistory.getBilled_date()));
		return tableContent;
	}
	
	public String buildTableContent(List<String[]> foodOrders, List<String[]> barOrders,Map<String,Double> foodtaxNameValue,
			Map<String,Double> liquorTaxNameValue,BillingHistory billingHistory) {
		
		//************* Food Row Starts *********************//
		double totalFoodAmount = 0;
		double totalFoodTaxAmount = 0;
		String foodOrderDetailsRow = "";
		String foodTaxDetailsRow= "";
		String foodTotalAmountRowWithTax = "";
		double totalFoodAmountWithTax = 0;
		for (String[] singleFoodOrder : foodOrders) {
			foodOrderDetailsRow += "<tr>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleFoodOrder[0]+"</td>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleFoodOrder[1]+"</td>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleFoodOrder[2]+"</td>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleFoodOrder[3]+"</td>\r\n" + 
					"</tr>";
			totalFoodAmount += Double.valueOf(singleFoodOrder[3]);
		}
		String totalFoodAmountRowWOTax = "<tr>"
				+ "<td colspan='3' style='font-weight:600;text-align:right;border: 1px solid #ddd;padding: 7px;'>Total Amount</td>\r\n" + 
				"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+totalFoodAmount+"</td>\r\n" + 
				"    </tr>";
		
		if (foodOrders.size() > 0) {
			for (Entry e : foodtaxNameValue.entrySet()) {
				foodTaxDetailsRow += "<tr>"
						+ "		<td colspan='2' style='font-weight:600;text-align:right;border: 1px solid #ddd;padding: 7px;'>"+e.getKey()+"</td>\r\n" + 
						"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+e.getValue()+"%</td>\r\n" + 
						"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+(totalFoodAmount*Double.valueOf((Double)e.getValue()))/100+"</td>\r\n" + 
						"    </tr>";
				totalFoodTaxAmount += (totalFoodAmount*Double.valueOf((Double)e.getValue()))/100;
			}
		}
		
		foodTotalAmountRowWithTax = "<tr>\r\n" + 
				"    <td colspan='3' style='font-weight:600;text-align:right;border: 1px solid #ddd;padding: 7px;'>Net Food Amount</td>\r\n" + 
				"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+(totalFoodAmount+totalFoodTaxAmount)+"</td>\r\n" + 
				"    </tr>";
		totalFoodAmountWithTax = totalFoodAmount+totalFoodTaxAmount;
		String foodRows = foodOrderDetailsRow+totalFoodAmountRowWOTax+foodTaxDetailsRow+foodTotalAmountRowWithTax;
		//************* Food Row Ends *********************//
		
		
		//************* BAR Row Starts *********************//
		double totalBarAmount = 0;
		double totalBarTaxAmount = 0;
		String barOrderDetailsRow = "";
		String barTaxDetailsRow= "";
		String barTotalAmountRowWithTax = "";
		double totalBarAmountWithTax = 0;
		
		for (String[] singleBarOrder : barOrders) {
			barOrderDetailsRow += "<tr>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleBarOrder[0]+"</td>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleBarOrder[1]+"</td>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleBarOrder[2]+"</td>\r\n" + 
					"<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+singleBarOrder[3]+"</td>\r\n" + 
					"</tr>";
			totalBarAmount += Double.valueOf(singleBarOrder[3]);
		}
		
		String totalBarAmountRowWOTax = "<tr>"
				+ "<td colspan='3' style='font-weight:600;text-align:right;border: 1px solid #ddd;padding: 7px;'>Total Amount</td>\r\n" + 
				"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+totalBarAmount+"</td>\r\n" + 
				"    </tr>";
		
		if (barOrders.size() > 0) {
			for (Entry e : liquorTaxNameValue.entrySet()) {
				barTaxDetailsRow += "<tr>"
						+ "		<td colspan='2' style='font-weight:600;text-align:right;border: 1px solid #ddd;padding: 7px;'>"+e.getKey()+"</td>\r\n" + 
						"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+e.getValue()+"%</td>\r\n" + 
						"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+(totalBarAmount*Double.valueOf((Double)e.getValue()))/100+"</td>\r\n" + 
						"    </tr>";
				totalBarTaxAmount += (totalBarAmount*Double.valueOf((Double)e.getValue()))/100;
			}
		}

		barTotalAmountRowWithTax = "<tr>\r\n" + 
				"    <td colspan='3' style='font-weight:600;text-align:right;border: 1px solid #ddd;padding: 7px;'>Net Drinks Amount</td>\r\n" + 
				"		<td style='text-align:center;border: 1px solid #ddd;padding: 7px;'>"+(totalBarAmount+totalBarTaxAmount)+"</td>\r\n" + 
				"    </tr>";
		
		totalBarAmountWithTax = totalBarAmount+totalBarTaxAmount;
		String barRows = barOrderDetailsRow+totalBarAmountRowWOTax+barTaxDetailsRow+barTotalAmountRowWithTax;
		//************* BAR Row Ends *********************//
		
		String totalIndex = "    <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
				"    <html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
				"    <head>\r\n" + 
				"    <title>Restonza</title>\r\n" + 
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n" + 
				"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n" + 
				"    <link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.0.12/css/all.css\" integrity=\"sha384-G0fIWCsCzJIMAVNQPfjH08cyYaUtMwjJwqiRKxxE/rx96Uroj1BtIQ6MLJuheaO9\" crossorigin=\"anonymous\">\r\n" + 
				"    <style type=\"text/css\">\r\n" + 
				"    * {\r\n" + 
				"    -ms-text-size-adjust:100%;\r\n" + 
				"    -webkit-text-size-adjust:none;\r\n" + 
				"    -webkit-text-resize:100%;\r\n" + 
				"    text-resize:100%;\r\n" + 
				"    }\r\n" + 
				"    a{\r\n" + 
				"    outline:none;\r\n" + 
				"    color:#40aceb;\r\n" + 
				"    text-decoration:underline;\r\n" + 
				"    }\r\n" + 
				"    a:hover{text-decoration:none !important;}\r\n" + 
				"    .nav a:hover{text-decoration:underline !important;}\r\n" + 
				"    .title a:hover{text-decoration:underline !important;}\r\n" + 
				"    .title{}\r\n" + 
				"    .meet-title{}\r\n" + 
				"    .meet-title h1{font:24px/30px Arial, Helvetica, sans-serif; color:#fff;text-transform:uppercase;margin:0px auto 10px;}\r\n" + 
				"    .meet-title h1 span{font-weight:600;}\r\n" + 
				"    .meet-title h1 .highlight-span{color:#fecd1a;}\r\n" + 
				"    .title-2 a:hover{text-decoration:underline !important;}\r\n" + 
				"    .btn:hover{opacity:0.8;}\r\n" + 
				"    .btn a:hover{text-decoration:none !important;}\r\n" + 
				"    .btn{\r\n" + 
				"    -webkit-transition:all 0.3s ease;\r\n" + 
				"    -moz-transition:all 0.3s ease;\r\n" + 
				"    -ms-transition:all 0.3s ease;\r\n" + 
				"    transition:all 0.3s ease;\r\n" + 
				"    }\r\n" + 
				"    table td {border-collapse: collapse !important;}\r\n" + 
				"    .ExternalClass, .ExternalClass a, .ExternalClass span, .ExternalClass b, .ExternalClass br, .ExternalClass p, .ExternalClass div{line-height:inherit;}\r\n" + 
				"    \r\n" + 
				"    .service-div{width:100%;float:left;position:relative;padding-left:5px;}\r\n" + 
				"    .service-div .service-sub{display:inline-block; vertical-align:middle;text-align:center;width:30%;margin-right: 15px;box-shadow: 0px 1px 3px rgba(0,0,0,0.3);border-radius:10px;padding:20px 0px;text-transform:uppercase;min-height:83px;\r\n" + 
				"    background-color:#fff;}\r\n" + 
				"    .service-div .service-sub .img-div{}\r\n" + 
				"    .service-div .service-sub .img-div img{max-width:100%;display:block;margin:0px auto;max-height:30px;}\r\n" + 
				"    .service-div .service-sub h3{margin:15px auto 0px;color:#000;font:12px/20px Arial, Helvetica, sans-serif;font-weight:600;}\r\n" + 
				"    .service-div .service-sub h3 span{color: #e02d74;font-size:13px;}\r\n" + 
				"    .service-div .service-sub p{margin:0px auto;color:#a8a8a8;font:12px/18px Arial, Helvetica, sans-serif;}\r\n" + 
				"    \r\n" + 
				"    @media only screen and (max-width:500px) {\r\n" + 
				"    table[class=\"flexible\"]{width:100% !important;}\r\n" + 
				"    table[class=\"center\"]{\r\n" + 
				"    float:none !important;\r\n" + 
				"    margin:0 auto !important;\r\n" + 
				"    }\r\n" + 
				"    *[class=\"hide\"]{\r\n" + 
				"    display:none !important;\r\n" + 
				"    width:0 !important;\r\n" + 
				"    height:0 !important;\r\n" + 
				"    padding:0 !important;\r\n" + 
				"    font-size:0 !important;\r\n" + 
				"    line-height:0 !important;\r\n" + 
				"    }\r\n" + 
				"    td[class=\"img-flex\"] img{\r\n" + 
				"    width:100% !important;\r\n" + 
				"    height:auto !important;\r\n" + 
				"    }\r\n" + 
				"    td[class=\"aligncenter\"]{text-align:center !important;}\r\n" + 
				"    th[class=\"flex\"]{\r\n" + 
				"    display:block !important;\r\n" + 
				"    width:100% !important;\r\n" + 
				"    }\r\n" + 
				"    td[class=\"wrapper\"]{padding:0 !important;}\r\n" + 
				"    td[class=\"holder\"]{padding:30px 15px 20px !important;}\r\n" + 
				"    td[class=\"nav\"]{\r\n" + 
				"    padding:20px 0 0 !important;\r\n" + 
				"    text-align:center !important;\r\n" + 
				"    }\r\n" + 
				"    td[class=\"h-auto\"]{height:auto !important;}\r\n" + 
				"    td[class=\"description\"]{padding:30px 20px !important;}\r\n" + 
				"    td[class=\"i-120\"] img{\r\n" + 
				"    width:120px !important;\r\n" + 
				"    height:auto !important;\r\n" + 
				"    }\r\n" + 
				"    td[class=\"footer\"]{padding:5px 20px 20px !important;}\r\n" + 
				"    td[class=\"footer\"] td[class=\"aligncenter\"]{\r\n" + 
				"    line-height:25px !important;\r\n" + 
				"    padding:20px 0 0 !important;\r\n" + 
				"    }\r\n" + 
				"    tr[class=\"table-holder\"]{\r\n" + 
				"    display:table !important;\r\n" + 
				"    width:100% !important;\r\n" + 
				"    }\r\n" + 
				"    th[class=\"thead\"]{display:table-header-group !important; width:100% !important;}\r\n" + 
				"    th[class=\"tfoot\"]{display:table-footer-group !important; width:100% !important;}\r\n" + 
				"    }\r\n" + 
				"    </style>\r\n" + 
				"    </head>\r\n" + 
				"    <body style=\"margin:0; padding:0;\" bgcolor=\"#eaeced\">\r\n" + 
				"    <table style=\"min-width:320px;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#eaeced\">\r\n" + 
				"    <!-- fix for gmail -->\r\n" + 
				"    <tr>\r\n" + 
				"    <td class=\"hide\">\r\n" + 
				"    <table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:600px !important;\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td style=\"min-width:600px; font-size:0; line-height:0;\">&nbsp;</td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <td class=\"wrapper\" style=\"padding:15px 10px;\">\r\n" + 
				"    <!-- module 1 -->\r\n" + 
				"    \r\n" + 
				"    \r\n" + 
				"    <!-- module 2 -->\r\n" + 
				"    <table data-module=\"module-4\" data-thumb=\"thumbnails/04.png\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\r\n" + 
				"    <table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"#ffffff\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <th class=\"flex\" width=\"400\" align=\"left\" style=\"vertical-align:top; padding:0;\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-inner-block-01\" class=\"h-auto\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td class=\"description\" style=\"padding:15px;\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <th width=\"35%\" align=\"left\"><h1 style=\"font:bold 16px/16px Arial, Helvetica, sans-serif; color:#000;margin:0px auto 15px;font-weight:600;\"><i class=\"fas fa-h-square\" style=\"color: #fac011;\"></i>&nbsp;Hotel Name :</h1></th>\r\n" + 
				"    <td align=\"left\" width=\"65%\"><p style=\"font:normal 14px/14px Arial, Helvetica, sans-serif; color:#000;margin:0px auto 20px;\">**hotelname</p></td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <th width=\"35%\" align=\"left\"><h1 style=\"font:bold 16px/16px Arial, Helvetica, sans-serif; color:#000;margin:0px auto 15px;font-weight:600;\"><i class=\"fas fa-map-marker-alt\" style=\"color: #fac011;\"></i>&nbsp;Address :</h1></th>\r\n" + 
				"    <td align=\"left\" width=\"65%\"><p style=\"font:normal 14px/14px Arial, Helvetica, sans-serif; color:#000;margin:0px auto 20px;\">**address</p></td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </th>\r\n" + 
				"    <th class=\"flex\" width=\"200\" align=\"left\" style=\"vertical-align:top; padding:15px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"     <td>\r\n" + 
				"    <h3 style=\"text-align:right;font-weight:normal;font-size:18px;margin:0px 0px 15px;color: #fac011;\">**inoviceDate</h3>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <td>\r\n" + 
				"    <h5 style=\"text-align:right;font-weight:normal;font-size:14px;margin:0px 0px 10px;\">Bill To</h5>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <td>\r\n" + 
				"    <h4 style=\"margin:0px 0px 10px;text-align:right;\"><i class=\"fas fa-user\" style=\"color: #fac011;\"></i> User Name</h4>\r\n" + 
				"    <p style=\"margin:0px 0px 10px;font-weight:normal;text-align:right;color:#333333;\">**username</p>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <td>\r\n" + 
				"    <h4 style=\"margin:0px 0px 10px;text-align:right;\"><i class=\"fas fa-mobile-alt\" style=\"color: #fac011;\"></i> Mobile Number</h4>\r\n" + 
				"    <p style=\"margin:0px 0px 10px;font-weight:normal;text-align:right;color:#333333;\">**usernameandmobileno</p>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </th>\r\n" + 
				"    \r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td>\r\n" + 
				"    <table width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;border: 1px solid #ddd;\">\r\n" + 
				"    <tr>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\"><i class=\"fas fa-clipboard-list\" style=\"color: #fac011;\"></i> Table Number</th>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\"><i class=\"fas fa-user\" style=\"color: #fac011;\"></i> Customer ID</th>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <td style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">**tableno</td>\r\n" + 
				"    <td style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">**customerId</td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px 15px 5px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td style=\"width:3%;\"><img src=\"https://www.prometteursolutions.com/restimg/breakfast.png\" style=\"vertical-align:top;\" width=\"25\" alt=\"\" /></td>\r\n" + 
				"    <td style=\"font:18px/23px Arial, Helvetica, sans-serif; color:#000;width:97%;padding-left:10px;font-weight:600;\">Dishes</td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td>\r\n" + 
				"    <table width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;border: 1px solid #ddd;\">\r\n" + 
				"    <tr>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Dishname</th>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Quantity</th>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Price</th>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Total</th>\r\n" + 
				"    </tr>\r\n" + 
				"    **foodRows\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    \r\n" + 
				"    \r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px 15px 5px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td style=\"width:3%;\"><img src=\"https://www.prometteursolutions.com/restimg/beer.png\" style=\"vertical-align:top;\" width=\"25\" alt=\"\" /></td>\r\n" + 
				"    <td style=\"font:18px/23px Arial, Helvetica, sans-serif; color:#000;width:97%;padding-left:10px;font-weight:600;\">Drinks</td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td>\r\n" + 
				"    <table width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;border: 1px solid #ddd;\">\r\n" + 
				"    <tr>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Dishname</th>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Quantity</th>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Price</th>\r\n" + 
				"    <th style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">Total</th>\r\n" + 
				"    </tr>\r\n" + 
				"    **barRows" +
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    \r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px 15px 5px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td style=\"width:3%;\"><img src=\"https://www.prometteursolutions.com/restimg/water.png\" style=\"vertical-align:top;\" width=\"25\" alt=\"\" /></td>\r\n" + 
				"    <td style=\"font:18px/23px Arial, Helvetica, sans-serif; color:#000;width:97%;padding-left:10px;font-weight:600;\">Amount Payable</td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px;\" bgcolor=\"#ffffff\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td>\r\n" + 
				"    <table width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;border: 1px solid #ddd;\">\r\n" + 
				"    <tr>\r\n" + 
				"    <th style=\"text-align:left;border: 1px solid #ddd;padding: 7px;\">Net Food Amount</th>\r\n" + 
				"    <td colspan=\"3\" style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">"+totalFoodAmountWithTax+"</td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <th style=\"text-align:left;border: 1px solid #ddd;padding: 7px;\">Net Drink Amount</th>\r\n" + 
				"    <td style=\"text-align:center;border: 1px solid #ddd;padding: 7px;\">"+totalBarAmountWithTax+"</td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr>\r\n" + 
				"    <th style=\"text-align:left;border: 1px solid #ddd;padding: 7px;\">Total Amount Payable</th>\r\n" + 
				"    <td style=\"text-align:center;border: 1px solid #ddd;padding: 7px;font-weight:600;\">"+(totalFoodAmountWithTax+totalBarAmountWithTax)+"</td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    \r\n" + 
				"    <!-- module 6 -->\r\n" + 
				"    <table data-module=\"module-6\" data-thumb=\"thumbnails/06.png\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\r\n" + 
				"    <table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-block\" class=\"holder\" style=\"padding:15px;\" bgcolor=\"#fecd1a\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-color=\"title\" data-size=\"size title\" data-min=\"20\" data-max=\"40\" data-link-color=\"link title color\" data-link-style=\"text-decoration:none; color:#000;\" class=\"title\" align=\"center\" style=\"font:30px/33px Arial, Helvetica, sans-serif; color:#fff; padding:0px;position:relative;\">\r\n" + 
				"    Thank You\r\n" + 
				"    <img src=\"https://www.prometteursolutions.com/restimg/smile.png\" style=\"position:absolute;left:0%;top:-20%;\" width=\"50\" alt=\"\" />\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    \r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    <tr><td height=\"15\"></td></tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    <!-- module 7 -->\r\n" + 
				"    <table data-module=\"module-7\" data-thumb=\"thumbnails/07.png\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-bgcolor=\"bg-module\" bgcolor=\"#eaeced\">\r\n" + 
				"    <table class=\"flexible\" width=\"600\" align=\"center\" style=\"margin:0 auto;\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td class=\"footer\" style=\"padding:0 0 10px;\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr class=\"table-holder\">\r\n" + 
				"    <th class=\"tfoot\" width=\"400\" align=\"left\" style=\"vertical-align:top; padding:0;\">\r\n" + 
				"    <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td data-color=\"text\" data-link-color=\"link text color\" data-link-style=\"text-decoration:underline; color:#797c82;\" class=\"aligncenter\" style=\"font:12px/16px Arial, Helvetica, sans-serif; color:#797c82; padding:0 0 10px;\">\r\n" + 
				"    Copyright @ 2018 Prometteur Solutions Pvt. Ltd. All Rights Reserved. <!--<a target=\"_blank\" style=\"text-decoration:underline; color:#797c82;\" href=\"sr_unsubscribe\">Unsubscribe.</a>-->\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </th>\r\n" + 
				"    <th class=\"thead\" width=\"200\" align=\"left\" style=\"vertical-align:top; padding:0;\">\r\n" + 
				"    <table class=\"center\" align=\"right\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"    <tr>\r\n" + 
				"    <td class=\"btn\" valign=\"top\" style=\"line-height:0; padding:3px 0 0;\">\r\n" + 
				"    <a target=\"_blank\" style=\"text-decoration:none;\" href=\"https://www.facebook.com/Prometteur-Solutions-Pvt-Ltd-982676831753173/timeline/\">\r\n" + 
				"    <img src=\"https://www.prometteursolutions.com/restimg/ico-facebook.png\" border=\"0\" style=\"font:12px/15px Arial, Helvetica, sans-serif; color:#797c82;\" align=\"left\" vspace=\"0\" hspace=\"0\" width=\"6\" height=\"13\" alt=\"fb\" />\r\n" + 
				"    </a>\r\n" + 
				"    </td>\r\n" + 
				"    <td width=\"20\"></td>\r\n" + 
				"    <td class=\"btn\" valign=\"top\" style=\"line-height:0; padding:3px 0 0;\">\r\n" + 
				"    <a target=\"_blank\" style=\"text-decoration:none;\" href=\"https://twitter.com/Iamprometteur\">\r\n" + 
				"    <img src=\"https://www.prometteursolutions.com/restimg/ico-twitter.png\" border=\"0\" style=\"font:12px/15px Arial, Helvetica, sans-serif; color:#797c82;\" align=\"left\" vspace=\"0\" hspace=\"0\" width=\"13\" height=\"11\" alt=\"tw\" />\r\n" + 
				"    </a>\r\n" + 
				"    </td>\r\n" + 
				"    <td width=\"19\"></td>\r\n" + 
				"    <td class=\"btn\" valign=\"top\" style=\"line-height:0; padding:3px 0 0;\">\r\n" + 
				"    <a target=\"_blank\" style=\"text-decoration:none;\" href=\"https://plus.google.com/118375854560050713875\">\r\n" + 
				"    <img src=\"https://www.prometteursolutions.com/restimg/ico-google-plus.png\" border=\"0\" style=\"font:12px/15px Arial, Helvetica, sans-serif; color:#797c82;\" align=\"left\" vspace=\"0\" hspace=\"0\" width=\"19\" height=\"15\" alt=\"g+\" />\r\n" + 
				"    </a>\r\n" + 
				"    </td>\r\n" + 
				"    <td width=\"20\"></td>\r\n" + 
				"    <td class=\"btn\" valign=\"top\" style=\"line-height:0; padding:3px 0 0;\">\r\n" + 
				"    <a target=\"_blank\" style=\"text-decoration:none;\" href=\"https://www.linkedin.com/company/prometteur-solutions-pvt--ltd-?trk=top_nav_home\">\r\n" + 
				"    <img src=\"https://www.prometteursolutions.com/restimg/ico-linkedin.png\" border=\"0\" style=\"font:12px/15px Arial, Helvetica, sans-serif; color:#797c82;\" align=\"left\" vspace=\"0\" hspace=\"0\" width=\"13\" height=\"11\" alt=\"in\" />\r\n" + 
				"    </a>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </th>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </td>\r\n" + 
				"    </tr>\r\n" + 
				"    <!-- fix for gmail -->\r\n" + 
				"    <tr>\r\n" + 
				"    <td style=\"line-height:0;\"><div style=\"display:none; white-space:nowrap; font:15px/1px courier;\">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div></td>\r\n" + 
				"    </tr>\r\n" + 
				"    </table>\r\n" + 
				"    </body>\r\n" + 
				"    </html>";
		
		totalIndex = totalIndex.replace("**foodRows", foodRows);
		totalIndex = totalIndex.replace("**barRows", barRows);
		return totalIndex;
	}
	/**
	 * post request for fetching all new orders associated to hotel and table
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/getPendingOrders")
	public @ResponseBody RestonzaRestResponseVO executeGetPendingOrders(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		int hotel_id = Integer.parseInt(orderDetailsVO.getHotel_id());
		List<OrderDetails> getAllOrderidList = orderDetailsRepository.findAllOrdersByStatus(hotel_id, RESTONZACONSTANTS.confirm_order.toString());
		if (!getAllOrderidList.isEmpty()) {
			List<GetOrderDetailsVO> ordersList = new ArrayList<>();
			GetOrderDetailsVO orders = null;
			for (OrderDetails orderdetail : getAllOrderidList) {
				orders = new GetOrderDetailsVO(orderdetail.getId(), orderdetail.getCustomer_id(), orderdetail.getHotel_id(), orderdetail.getTable_id(), orderdetail.getOrder_summary(), orderdetail.getInstruction());
				ordersList.add(orders);
			}
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", ordersList);
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "No records found");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getPendingOrders/v2")
	public @ResponseBody RestonzaRestResponseVO executeGetPendingOrdersV2(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		int hotel_id = Integer.parseInt(orderDetailsVO.getHotel_id());
		List<Map<String, Object>> getAllOrderidList = hotelAnalyzerRepository.getBackKitchenPendingOrderDetails(hotel_id, RESTONZACONSTANTS.confirm_order.toString());
		if (!getAllOrderidList.isEmpty()) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", getAllOrderidList);
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("fail", "No records found");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/markReady")
	public @ResponseBody RestonzaRestResponseVO executeMarkOrderAsReady(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		int order_id = Integer.parseInt(orderDetailsVO.getOrder_id());
		int hotel_id = Integer.parseInt(orderDetailsVO.getHotel_id());
		orderDetailsRepository.updateOrderStatus(hotel_id, order_id, RESTONZACONSTANTS.ready_order.toString(), new Date());
		restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", "Order" + order_id + "is Ready");
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/markReady/v2")
	public @ResponseBody RestonzaRestResponseVO executeMarkOrderAsReadyV2(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		hotelAnalyzerRepository.updateBackKitchenOrder(orderDetailsVO);
		restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", "Order updated successfully");
		return restonzaRestResponseVO;
	}
	
	//TODO: Used for confirm and ready orders
	@PostMapping("/getConfirmAndReady")
	public @ResponseBody RestonzaRestResponseVO executeGetConfirmAndReady(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (orderDetailsVO != null ) {
			if (RestonzaUtility.isNullOrEmpty(orderDetailsVO.getHotel_id())) {
				List<OrderDetails> orderDetailsList = orderDetailsRepository.getConfirmAndReadyOrders(Integer.parseInt(orderDetailsVO.getHotel_id()));
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", orderDetailsList);
			} else {
				restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Fetching Order Details");
			}
		} else {
			restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Fetching Order Details");
		}
		return restonzaRestResponseVO;
	}
	

	@PostMapping("/getWaiterTable")
	public @ResponseBody RestonzaRestResponseVO executeGetWaiterTable(@RequestBody HotelDetailsVO hotelDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (hotelDetailsVO != null) {
			if (RestonzaUtility.isNullOrEmpty(hotelDetailsVO.getId())) {
				try{
					List<CustomerCallDAO> customercallList =  customerCallRepository.getWaterWaiter(Integer.parseInt(hotelDetailsVO.getId()), "WAITER");
					restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", customercallList);
				} catch (Exception e) {
					
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Failed to fetch the stat");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Failed to fetch the stat");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getWaterTable")
	public @ResponseBody RestonzaRestResponseVO executeGetWaterTable(@RequestBody HotelDetailsVO hotelDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (hotelDetailsVO != null) {
			if (RestonzaUtility.isNullOrEmpty(hotelDetailsVO.getId())) {
				try{
					List<CustomerCallDAO> customercallList =  customerCallRepository.getWaterWaiter(Integer.parseInt(hotelDetailsVO.getId()), "WATER");
					restonzaRestResponseVO = new RestonzaRestResponseVO("sucess", customercallList);
				} catch (Exception e) {
					
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Failed to fetch the stat");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Failed to fetch the stat");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getWidgetCall")
	public @ResponseBody RestonzaRestResponseVO executeGetWidgetCall(@RequestBody HotelDetailsVO hotelDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (RestonzaUtility.isNullOrEmpty(hotelDetailsVO.getId())) {
			try {
				String count = orderDetailsRepository.getNewOrderCount(Integer.parseInt(hotelDetailsVO.getId()));
				String callCount = customerCallRepository.getCustomerCallCount(Integer.parseInt(hotelDetailsVO.getId()));
				Map<String,String> map = new HashMap<String, String>();
				map.put("newordercount",count);
				map.put("custcallcount",callCount);
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", map);
			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Failed to fetch the stat");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Failed to fetch the stat");
		}
		return restonzaRestResponseVO;
	}
	
	
	@PostMapping("/doPrintJobService")
	public @ResponseBody RestonzaRestResponseVO executePrintJobService(@RequestBody PrintVO printVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		try {
			if (!RestonzaUtility.isObjectFieldsForNullOREmpty(printVO)) {
				HotelDetails hotelDetails = hotelDetailsRepository.findOne(Integer.parseInt(printVO.getHotel_id()));
				String hotelName = hotelDetails.getHotel_name();
				String hotelAddress = hotelDetails.getHotel_address();
				String orderNo = printVO.getOrder_id();
				String amt = printVO.getAmt();
				Map<String,Double> taxes = printVO.getTaxes();
				StringBuilder builder = new StringBuilder();
				double taxamount = 0;
				for (Map.Entry<String, Double> entry : taxes.entrySet()){
					builder.append(entry.getKey()+"+");
					taxamount += entry.getValue();
				}
				
				List<PlacedOrderDetailsVO> allDishDetails = printVO.getOrderDetails();
				int orderCount = 0;
				Object[][] array = new Object[allDishDetails.size()][1];
				for (PlacedOrderDetailsVO placedOrderDetailsVO : allDishDetails) {
					String[] dishDetail= new String[4];
					dishDetail[0] = placedOrderDetailsVO.getDishname();
					dishDetail[1] = placedOrderDetailsVO.getQty();
					dishDetail[2] = placedOrderDetailsVO.getPrice();
					dishDetail[3] = String.valueOf(Integer.parseInt(dishDetail[1]) * Integer.parseInt(dishDetail[2]));
					array[orderCount] = dishDetail;
					orderCount++;
				}
				PrinterService ps = new PrinterService();
				ps.setItems(array, hotelName, hotelAddress, orderNo, amt, builder,taxamount);
				PrinterJob pj = PrinterJob.getPrinterJob();
				pj.setPrintable(new PrinterService.MyPrintable(), ps.getPageFormat(pj));
				try {
					pj.print();
					restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Print Successful");
				} catch (PrinterException ex) {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error In Printing");
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error In Printing");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error In Printing");
		}
		
		return restonzaRestResponseVO;
	}
	
	
	@PostMapping("/doPrintRestoBarJobService")
	public @ResponseBody RestonzaRestResponseVO executePrintJobService(@RequestBody RestoPrintVO restoPrintVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		try {
				HotelDetails hotelDetails = hotelDetailsRepository.findOne(Integer.parseInt(restoPrintVO.getHotel_id()));
				String hotelName = hotelDetails.getHotel_name();
				String hotelAddress = hotelDetails.getHotel_address();
				String orderNo = restoPrintVO.getOrder_id();
				String amt = restoPrintVO.getAmt();
				Map<String,Double> taxes = restoPrintVO.getTaxes();
				Map<String,Double> ltaxes = restoPrintVO.getLtaxes();
				String  barOrderCounterString = restoPrintVO.getBarordercount().equals("null") ? "0" : restoPrintVO.getBarordercount();
				Integer barOrderCounter = Integer.parseInt(barOrderCounterString);
				String baramount = restoPrintVO.getBarbillamt();
				String foodamount = restoPrintVO.getFoodbillamt();
				StringBuilder builder = new StringBuilder();
				double taxamount = 0;
				for (Map.Entry<String, Double> entry : taxes.entrySet()){
					builder.append(entry.getKey()+"+");
					taxamount += entry.getValue();
				}
				
				StringBuilder lbuilder = new StringBuilder();
				double ltaxamount = 0;
				for (Map.Entry<String, Double> entry : ltaxes.entrySet()){
					lbuilder.append(entry.getKey()+"+");
					ltaxamount += entry.getValue();
				}
				
				List<PlacedOrderDetailsVO> allDishDetails = restoPrintVO.getOrderDetails();
				int orderCount = 0;
				Object[][] array = new Object[allDishDetails.size()][1];
				for (PlacedOrderDetailsVO placedOrderDetailsVO : allDishDetails) {
					String[] dishDetail= new String[4];
					dishDetail[0] = placedOrderDetailsVO.getDishname();
					dishDetail[1] = placedOrderDetailsVO.getQty();
					dishDetail[2] = placedOrderDetailsVO.getPrice();
					dishDetail[3] = String.valueOf(Integer.parseInt(dishDetail[1]) * Integer.parseInt(dishDetail[2]));
					array[orderCount] = dishDetail;
					orderCount++;
				}
				PrinterService ps = new PrinterService();
				ps.setItems(array, hotelName, hotelAddress, orderNo, amt, builder,lbuilder,taxamount, ltaxamount, barOrderCounter, foodamount, baramount);//TODO:
				PrinterJob pj = PrinterJob.getPrinterJob();
				pj.setPrintable(new PrinterService.MyBarPrintable(), ps.getPageFormat(pj));
				try {
					pj.print();
					restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Print Successful");
				} catch (PrinterException ex) {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error In Printing");
				}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error In Printing");
		}
		
		return restonzaRestResponseVO;
	}
	/**
	 * get tax details 05/08/2017
	 */
	@PostMapping("/getTaxDetails")
	public @ResponseBody RestonzaRestResponseVO executeTaxDetails(@RequestBody OrderDetailsVO orderDetailsVO){
		if (orderDetailsVO != null && orderDetailsVO.getHotel_id() != null || !orderDetailsVO.getHotel_id().isEmpty()) {
			List<TaxSetting> taxSetting = taxSettingRepository.getTax(Integer.parseInt(orderDetailsVO.getHotel_id()), true, false); //TODO:
			Map<String,Double> taxNameValue = new HashMap<>();
			if (taxSetting != null && !taxSetting.isEmpty()) {
				for (TaxSetting tax: taxSetting) {
					taxNameValue.put(tax.getTaxname(), tax.getPercentage());
				}
			}
			
			List<TaxSetting> ltaxSetting = taxSettingRepository.getTax(Integer.parseInt(orderDetailsVO.getHotel_id()), true, true); //TODO:
			Map<String,Double> ltaxNameValue = new HashMap<>();
			if (ltaxSetting != null && !ltaxSetting.isEmpty()) {
				for (TaxSetting tax: ltaxSetting) {
					ltaxNameValue.put(tax.getTaxname(), tax.getPercentage());
				}
			}
			
			BillManagementVO billManagementVO = new BillManagementVO(null, taxNameValue,ltaxNameValue);
			return new RestonzaRestResponseVO("success", billManagementVO);
		} else {
			return new RestonzaRestResponseVO("error", "Error Fetching Order Details");
		}
	}
}
