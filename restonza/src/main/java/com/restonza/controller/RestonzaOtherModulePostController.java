/**
 * 
 */
package com.restonza.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.AdsDetails;
import com.restonza.dao.CampaignData;
import com.restonza.dao.CustomerDAO;
import com.restonza.dao.FeedBack;
import com.restonza.dao.GreetingsDetails;
import com.restonza.dao.TaxSetting;
import com.restonza.dao.repository.AdsDetailsRepository;
import com.restonza.dao.repository.CampaignDataRepository;
import com.restonza.dao.repository.CustomerRepository;
import com.restonza.dao.repository.FeedBackRepository;
import com.restonza.dao.repository.GreetingDetailsRepository;
import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.dao.repository.OrderDetailsRepository;
import com.restonza.dao.repository.TaxSettingRepository;
import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaPushNotificationService;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.AdsDetailsVO;
import com.restonza.vo.CampaignDataVO;
import com.restonza.vo.CustomerVO;
import com.restonza.vo.FeedBackVO;
import com.restonza.vo.GreetingsDetailsVO;
import com.restonza.vo.HotelAnalysisVO;
import com.restonza.vo.OrderDetailsVO;
import com.restonza.vo.RestonzaRestResponseVO;
import com.restonza.vo.TableVO;
import com.restonza.vo.TaxSettingVO;
import com.restonza.vo.TrendingDishes;

/**
 * @author flex-grow developers
 * used for handling other modules request
 */
@RestController
@RequestMapping("/restonza")
public class RestonzaOtherModulePostController {
	@Autowired
	private AdsDetailsRepository adsDetailsRepository;
	
	@Autowired
	private GreetingDetailsRepository greetingDetailsRepository;
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	@Autowired
	private FeedBackRepository feedBackRepository;
	
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	
	@Autowired
	private TaxSettingRepository taxSettingRepository;
	
	@Autowired
	private CampaignDataRepository campaignDataRepository;
	
	@Autowired
	private RestonzaPushNotificationService restonzaPushNotificationService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	//Ad management
	/**
	 * post request for adding ads details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/addAdsDetails")
	public @ResponseBody RestonzaRestResponseVO executeAddAdsDetails(@RequestBody AdsDetailsVO adsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		AdsDetails adsDetails = null;
		try {
			/*adsDetails = adsDetailsRepository.isAdAvailable(adsDetailsVO.getAdname(), Integer.parseInt(adsDetailsVO.getHotel_id()));
			if (adsDetails == null) {
				
			}*/
			adsDetails = new AdsDetails();
			adsDetails = adsDetails.prepareAdDetails(adsDetails, adsDetailsVO);
			adsDetailsRepository.save(adsDetails);
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully added new advertisment");
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occured while adding new advertisment");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting ads details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/getAdsDetails")
	public @ResponseBody RestonzaRestResponseVO executeGetAdsDetails(){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		try {
			List<AdsDetails> listOfAds = adsDetailsRepository.getAllAds(true);
			List<AdsDetailsVO> adsList = new ArrayList<AdsDetailsVO>();
			AdsDetailsVO adDetails = null;
			for (AdsDetails ad : listOfAds) {
				adDetails = new AdsDetailsVO(String.valueOf(ad.getId()), ad.getAdname(), ad.getAddetails(), ad.getAdimg(), RestonzaUtility.getStatus(ad.isStatus()),ad.getPrice());
				adsList.add(adDetails);
			}
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", adsList);
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occured while adding new advertisment");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting ads details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/deleteAd")
	public @ResponseBody RestonzaRestResponseVO executeDeleteAdsDetails(@RequestBody AdsDetailsVO adsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		adsDetailsRepository.delete(Integer.parseInt(adsDetailsVO.getId()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully deleted advertisment");
		return restonzaRestResponseVO;
	}
	
	//greeting management
	/**
	 * post request for adding ads details
	 * @param greetingsDetailsVO
	 * @return
	 */
	@PostMapping("/addGreetingDetails")
	public @ResponseBody RestonzaRestResponseVO executeAddGreetingsDetails(@RequestBody GreetingsDetailsVO greetingsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		GreetingsDetails greetingsDetails = null;
		try {
			greetingsDetails = greetingDetailsRepository.isGreetingAvailable(greetingsDetailsVO.getGreetingname());
			if (greetingsDetails == null) {
				greetingsDetails = new GreetingsDetails();
			}
			greetingsDetails = greetingsDetails.prepareGreetingDetails(greetingsDetails, greetingsDetailsVO);
			greetingDetailsRepository.save(greetingsDetails);
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully added/updated advertisment");
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occured while adding new advertisment");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting ads details
	 * @param greetingsDetailsVO
	 * @return
	 */
	@PostMapping("/getGreetingDetails")
	public @ResponseBody RestonzaRestResponseVO executeGetGreetingDetails(@RequestBody GreetingsDetailsVO greetingsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			List<GreetingsDetails> listofGreetings = greetingDetailsRepository.getGreetings();
			List<GreetingsDetailsVO> greetingList = new ArrayList<GreetingsDetailsVO>();
			GreetingsDetailsVO greetingDetails = null;
			for (GreetingsDetails greeting : listofGreetings) {
				greetingDetails = new GreetingsDetailsVO(String.valueOf(greeting.getId()), greeting.getGreetingname(), greeting.getGreetingdetails(), greeting.getGreetingimg(), sdf.format(greeting.getGreetingdate()) ,RestonzaUtility.getStatus(greeting.isStatus()));
				greetingList.add(greetingDetails);
			}
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", greetingList);
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error occured while adding new advertisment");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting ads details
	 * @param greetingsDetailsVO
	 * @return
	 */
	@PostMapping("/deleteGreeting")
	public @ResponseBody RestonzaRestResponseVO executeDeleteGreetingDetails(@RequestBody GreetingsDetailsVO greetingsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		greetingDetailsRepository.delete(Integer.parseInt(greetingsDetailsVO.getId()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully deleted advertisment");
		return restonzaRestResponseVO;
	}
	
	//For super admin user (ad and greeting management)
	
	/**
	 * post request for approval management(greeting) details
	 * @param greetingsDetailsVO
	 * @return
	 */
	@PostMapping("/getActiveGreetings")
	public @ResponseBody RestonzaRestResponseVO executeGetAllads(@RequestBody GreetingsDetailsVO greetingsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<GreetingsDetails> getAllGreetings = greetingDetailsRepository.getAllGreetings(false);
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", getAllGreetings);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for approval management(Ad) details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/getPublishedAds")
	public @ResponseBody RestonzaRestResponseVO executeGetAllgreetings(@RequestBody AdsDetailsVO adsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<AdsDetails> getAllAds = adsDetailsRepository.getAllAds(true);
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", getAllAds);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for approval management(Ad) details
	 * @param adsDetailsVO
	 * @return
	 * Requesting a camping from admin to superadmin.
	 */
	@PostMapping("/requestCampaign")
	public @ResponseBody RestonzaRestResponseVO executeRequestCampaign(@RequestBody CampaignDataVO campaignDataVo){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		
		List<CampaignData> dataCheck = campaignDataRepository.checkCampaignEnrollment(campaignDataVo.getHotel_id());
		if (dataCheck.size() > 0) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("Error", "You have already enrolled for Campaign");
		} else {
			CampaignData data = new CampaignData();
			data.setHotel_id(campaignDataVo.getHotel_id());
			data.setCampaign_id(campaignDataVo.getCampaign_id());
			data.setCampaign_name(campaignDataVo.getCampaign_name());
			data.setDescription(campaignDataVo.getDescription());
			data.setPrice(campaignDataVo.getPrice());
			data.setStatus(false);
			campaignDataRepository.save(data);
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully Requested Campaign");
		}
		
		return restonzaRestResponseVO;
	}
	
	/**
	 * For Super Admin : to get all campaigns data requested by user.
	 * @param campaignDataVo
	 * @return
	 */
	@PostMapping("/getApprovalCampaigns")
	public @ResponseBody RestonzaRestResponseVO executeGetApprovalCampaign(@RequestBody CampaignDataVO campaignDataVo){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<CampaignData> capmaingnList = campaignDataRepository.getAllCampaigns();
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", capmaingnList);
		return restonzaRestResponseVO;
	}
	
	/**
	 * For Super Admin : Super admin will approve campaigns requested by admin.
	 * @param campaignDataVo
	 * @return
	 */
	@PostMapping("/approveCampaign")
	public @ResponseBody RestonzaRestResponseVO executeApproveCampaign(@RequestBody CampaignDataVO campaignDataVo){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		try {
			campaignDataRepository.approveCampaign(campaignDataVo.getHotel_id(),campaignDataVo.getCampaign_id(), true);
			CampaignData approvedCampaingedPushNotification = campaignDataRepository.findOne(campaignDataVo.getCampaign_id());
			List<String> listOfCustomers = customerRepository.getAllCustomerDeviceIds();
			for (String customerDeviceId : listOfCustomers) {
				if (customerDeviceId != null && !customerDeviceId.equals("")) {
					restonzaPushNotificationService.sendPushNotification(customerDeviceId, approvedCampaingedPushNotification.getCampaign_name(), approvedCampaingedPushNotification.getDescription());
				}
			}
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Campaign Approved");
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "ERROR While Approving Campaign");
		}
		
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/myCampaigns")
	public @ResponseBody RestonzaRestResponseVO executeGetMyCampaign(@RequestBody CampaignDataVO campaignDataVo){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<CampaignData> myCampinsList = campaignDataRepository.getMyCampaigns(campaignDataVo.getHotel_id());
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", myCampinsList);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting total monthly income
	 * @param HotelAnalysisVO
	 * @return
	 */
	@PostMapping("/getTotalEarningReport")
	public @ResponseBody RestonzaRestResponseVO executeGetTotalEarningReport(@RequestBody HotelAnalysisVO hotelAnalysisVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<HotelAnalysisVO> listofObjects = hotelAnalyzerRepository.getHotelAnalysis(Integer.parseInt(hotelAnalysisVO.getHotel_id()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", listofObjects);
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for getting trending dish details
	 * @param trendingDishes
	 * @return
	 */
	@PostMapping("/trendingdishesgenerator")
	public @ResponseBody RestonzaRestResponseVO executeGetTrendingDish(@RequestBody TrendingDishes trendingDishes){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<TrendingDishes> listofObjects = hotelAnalyzerRepository.getTrendingDishes(Integer.parseInt(trendingDishes.getHotel_id()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", listofObjects);
		return restonzaRestResponseVO;
	}
	
	
	/**
	 * post request for feedback details
	 * @param feedBackVO
	 * @return
	 */
	@PostMapping("/registerFeedBack")
	public @ResponseBody RestonzaRestResponseVO executeRegisterFeedBack(@RequestBody FeedBackVO feedBackVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		try {
			int ratingpoints = Integer.parseInt(feedBackVO.getRating());
			orderDetailsRepository.updateRating(ratingpoints,feedBackVO.getOrderids());
			FeedBack feedBack = new FeedBack();
			feedBack = feedBack.prepareFeedBack(feedBack, feedBackVO);
			feedBackRepository.save(feedBack);
			restonzaRestResponseVO = new RestonzaRestResponseVO("success","Thank you! Visit Again");
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error","Something went wrong! Please call administrator");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for feedback details
	 * @param feedBackVO
	 * @return
	 */
	@PostMapping("/getFeedBackAnalysis")
	public @ResponseBody RestonzaRestResponseVO executeFeedBackAnalysis(@RequestBody FeedBackVO feedBackVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		Map<String, Object> feedbackresponseVO = new HashMap<String, Object>();
		int hotel_id= Integer.parseInt(feedBackVO.getHotel_id());
		List<FeedBackVO> feedbackComments = hotelAnalyzerRepository.getFeedback(hotel_id);
		List<HotelAnalysisVO> feedbackCounts = hotelAnalyzerRepository.getFeedbackCount(hotel_id);
		feedbackresponseVO.put("feedbackComments", feedbackComments);
		feedbackresponseVO.put("feedbackCounts", feedbackCounts);
		try {
			restonzaRestResponseVO = new RestonzaRestResponseVO("success",feedbackresponseVO);
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error","Something went wrong! Please call administrator");
		}
		return restonzaRestResponseVO;
	}
	
	
	@PostMapping("/getDashboard")
	public @ResponseBody RestonzaRestResponseVO executeDashBoard(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		Map<String, Object> feedbackresponseVO = new HashMap<String, Object>();
		int hotel_id= Integer.parseInt(orderDetailsVO.getHotel_id());
		List<TableVO> listofoccupiedtables = hotelAnalyzerRepository.getOccupiedTableList(hotel_id);
		List<HotelAnalysisVO> todaysEarning = hotelAnalyzerRepository.getTodaysEarning(hotel_id);
		feedbackresponseVO.put("tablelist", listofoccupiedtables);
		feedbackresponseVO.put("earning", todaysEarning);
		try {
			restonzaRestResponseVO = new RestonzaRestResponseVO("success",feedbackresponseVO);
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error","Something went wrong! Please call administrator");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getOccupiedTableList")
	public @ResponseBody RestonzaRestResponseVO executeGetOccupiedTableList(@RequestBody OrderDetailsVO orderDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		int hotel_id= Integer.parseInt(orderDetailsVO.getHotel_id());
		List<TableVO> listofoccupiedtables = hotelAnalyzerRepository.getOccupiedTableList(hotel_id);
		List<String> tables = new ArrayList<>();
		for (TableVO table : listofoccupiedtables) {
			tables.add(table.getTable_id());
		}
		try {
			restonzaRestResponseVO = new RestonzaRestResponseVO("success",tables);
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error","Something went wrong! Please call administrator");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getTaxsetting")
	public @ResponseBody RestonzaRestResponseVO executeGetTaxSetting(@RequestBody TaxSettingVO taxSettingVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<TaxSetting> taxSetting = taxSettingRepository.getTaxSetting(Integer.parseInt(taxSettingVO.getHotel_id()));
		if (taxSetting != null) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", taxSetting);
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("Error", "Error in fetching tax settings.");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * Add tax setting as per hotel in a db.
	 * @param taxSettingVO
	 * @return
	 */
	@PostMapping("/saveTax")
	public @ResponseBody RestonzaRestResponseVO executeaddTaxSetting(@RequestBody TaxSettingVO taxSettingVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (taxSettingVO.getHotel_id()!=null && taxSettingVO.getPercentage() !=null &&
				taxSettingVO.getTaxname() !=null && taxSettingVO.getStatus() !=null) {
			double percentage = Double.valueOf(taxSettingVO.getPercentage());
			boolean result = taxSettingVO.getStatus().equalsIgnoreCase("true") ? true : false;
			boolean isLiquor = taxSettingVO.getIsliquor().equalsIgnoreCase("true") ? true : false;
			TaxSetting taxSetting = new TaxSetting();
			taxSetting.setHotel_id(Integer.valueOf(taxSettingVO.getHotel_id()));
			taxSetting.setTaxname(taxSettingVO.getTaxname());
			taxSetting.setPercentage(Double.valueOf(taxSettingVO.getPercentage()));
			taxSetting.setStatus(result);
			taxSetting.setIsliquortax(isLiquor);
			taxSettingRepository.save(taxSetting);
			restonzaRestResponseVO = new RestonzaRestResponseVO("Success", "Tax added successfully");
		} else { 
			restonzaRestResponseVO = new RestonzaRestResponseVO("Error", "Error in tax adding");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * Update tax setting as per hotel in a db.
	 * @param taxSettingVO
	 * @return
	 */
	@PostMapping("/updateTaxsetting")
	public @ResponseBody RestonzaRestResponseVO executeUpdateTaxSetting(@RequestBody TaxSettingVO taxSettingVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (taxSettingVO.getHotel_id()!=null && taxSettingVO.getId() !=null && 
				taxSettingVO.getPercentage() !=null && taxSettingVO.getTaxname() !=null && taxSettingVO.getStatus() !=null) {
			double percentage = Double.valueOf(taxSettingVO.getPercentage());
			boolean result = taxSettingVO.getStatus().equalsIgnoreCase("true") ? true : false;
			boolean isLiquor = taxSettingVO.getIsliquor().equalsIgnoreCase("true") ? true : false;
			taxSettingRepository.updateTaxStatus(taxSettingVO.getTaxname(), percentage, isLiquor, result,Integer.valueOf(taxSettingVO.getId()));
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Tax Updated Settings Successfully");
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("Error", "Error in sending parameters");
		}
		return restonzaRestResponseVO;
	}
	
	//get order count
	@PostMapping("/getTotalOrderCount")
	public @ResponseBody RestonzaRestResponseVO executeGetTotalOrderCount() {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		Long orderCount = orderDetailsRepository.getOrderCount(RESTONZACONSTANTS.cancel_order.toString());
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", orderCount);
		return restonzaRestResponseVO;
	}
	
	/***
	 * For deleting tax. 
	 * 
	 */
	@PostMapping("/deleteTax")
	public @ResponseBody RestonzaRestResponseVO executeDeleteTax(@RequestBody TaxSettingVO taxSettingVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (taxSettingVO != null && !taxSettingVO.getId().isEmpty()) {
			taxSettingRepository.delete(Integer.valueOf(taxSettingVO.getId()));
			restonzaRestResponseVO = new RestonzaRestResponseVO("Success", "Tax Deleted Successfully");
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("Error", "Error in deleting tax");
		}
		return restonzaRestResponseVO;
	}
	
	
	/***
	 * For fetching all Customer details.
	 * 
	 */
	@PostMapping("/getAllCustomerDetails")
	public @ResponseBody RestonzaRestResponseVO getAllCustomerDetails() {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		try {
			List<CustomerDAO> listOfCustomer = customerRepository.getAllCustomerDetails();
			List<CustomerVO> customerList = new ArrayList<CustomerVO>();
			/*CustomerVO customerDetails = null;
			for (CustomerDAO cd : listOfCustomer) {
				customerDetails = new CustomerVO(String.valueOf(cd.getCustomer_id()), cd.getFirst_name(),
						cd.getLast_name(), cd.getDeviceid());
				customerList.add(customerDetails);
			}*/
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", listOfCustomer);
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error",
					"Error occured while getting customer details");
		}
		return restonzaRestResponseVO;
	}
	
	/***
	 * To update Customer details.
	 * 
	 */
	@PostMapping("/updateCustomerDetails")
	public @ResponseBody RestonzaRestResponseVO updateCustomerDetails(@RequestBody CustomerVO customerVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (customerVO.getDeviceid() != null && customerVO.getPhonenumber() != null) {
			try {
				int row = customerRepository.updateCustomerDetails(customerVO.getFirst_name(),
						customerVO.getLast_name(), customerVO.getDeviceid(),
						customerVO.getCustomer_id(),customerVO.getEmailid(),customerVO.getPhonenumber());
				if (row == 0) {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Customer record found");
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("success",
							"Updated Customer details successfully");
				}

			} catch (Exception e) {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error",
						"Error occured while updating customer details");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error",
					"Error Customer id or first name cannot be null");
		}

		return restonzaRestResponseVO;
	}
	

}
