/**
 * 
 */
package com.restonza.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.BarCategory;
import com.restonza.dao.BarItems;
import com.restonza.dao.DishCategories;
import com.restonza.dao.Dishes;
import com.restonza.dao.HotelDetails;
import com.restonza.dao.Ingredient;
import com.restonza.dao.PushNotifierDAO;
import com.restonza.dao.UserDetails;
import com.restonza.dao.repository.DishCategoriesRepository;
import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.dao.repository.HotelDetailsRepository;
import com.restonza.dao.repository.HotelDishesRepositories;
import com.restonza.dao.repository.IngredientRepository;
import com.restonza.dao.repository.OrderDetailsRepository;
import com.restonza.dao.repository.UserDetailsRepository;
import com.restonza.dao.repository.WebPushNotifierRepository;
import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaResponseConstants;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.AddDishesModalResponse;
import com.restonza.vo.AdsDetailsVO;
import com.restonza.vo.DishCategoriesVO;
import com.restonza.vo.DishesVO;
import com.restonza.vo.HotelDetailsVO;
import com.restonza.vo.IngredientVO;
import com.restonza.vo.PagableResponseVO;
import com.restonza.vo.RestonzaRestResponseVO;
import com.restonza.vo.SessionVO;
import com.restonza.vo.StaticImageListingVO;
import com.restonza.vo.UserDetailsVO;

/**
 * @author flex-grow developers
 *
 */
@RestController
@RequestMapping("/restonza")
public class RestonzaPostController {
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private OrderDetailsRepository orderDetailsRepository;
	
	@Autowired
	private HotelDetailsRepository hotelDetailsRepository;
	
	@Autowired
	private DishCategoriesRepository dishCategoriesRepository;
	
	@Autowired
	private HotelDishesRepositories dishesRepository;
	
	@Autowired
	private IngredientRepository ingredientRepository;
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	@Autowired
	private WebPushNotifierRepository webPushNotifierRepository;
	
	@Value("${imageuploaddirectory}")
	private String imageuploaddirectory; 
	
	/**
	 * post request for authentication and authorization
	 * @param orderDetailsVO
	 * @return
	 */
	@PostMapping("/doAuthorize")
	public @ResponseBody RestonzaRestResponseVO executeAuthentication(@RequestBody UserDetailsVO userDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<UserDetails> userDetails = userDetailsRepository.authenticateUser(userDetailsVO.getUsername(), userDetailsVO.getPassword(), true);
		int tableCount = 0;
		int hotel_id = 0;
		int empid = 0;
		String user_role= "";
		String totalTableCount = "";
		try {
			if (!userDetails.isEmpty()) {
				for (UserDetails aUserDetails : userDetails) {
					tableCount = hotelDetailsRepository.getTableCount(aUserDetails.getHotel_id());
					hotel_id = aUserDetails.getHotel_id();
					user_role = aUserDetails.getUser_role();
					empid = aUserDetails.getId();
				}
				if (user_role.equals("superadmin")) {
					totalTableCount = String.valueOf(orderDetailsRepository.getOrderCount(RESTONZACONSTANTS.cancel_order.toString()));
				}
				SessionVO sessionVO = new SessionVO(hotel_id, empid, tableCount, user_role,totalTableCount);
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", sessionVO);
			} else{
				throw new Exception();
			}
		} catch (Exception e) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Something went wrong! Please call administrator");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for fetching hotel information
	 * TODO need to be modified later
	 * @param hotelDetailsVO
	 * @return
	 */
	@PostMapping("/getHotelDetails")
	public @ResponseBody RestonzaRestResponseVO executeGetHotelDetails(@RequestBody HotelDetailsVO hotelDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<HotelDetails> hotelDetails = hotelDetailsRepository.findName(hotelDetailsVO.getHotelname());
		if (hotelDetails != null && !hotelDetails.isEmpty()) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", hotelDetails);
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No hotel information available");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * This post request will fetch all categories with perticular hotel.
	 * @param dishCategoriesVO
	 * @return
	 */
	@PostMapping("/getDishCategories")
	public @ResponseBody RestonzaRestResponseVO executeGetDishCategories(@RequestBody DishCategoriesVO dishCategoriesVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		List<DishCategories> categories = dishCategoriesRepository.findCategories(Integer.parseInt(dishCategoriesVO.getHotel_id()));
		List<DishCategoriesVO> categoriesList = new ArrayList<DishCategoriesVO>();
		DishCategoriesVO dishCategorieVO = null;
		for (DishCategories dishCategories : categories) {
			if (dishCategories.isStatus()) {
				dishCategorieVO = new DishCategoriesVO(String.valueOf(dishCategories.getId()), dishCategories.getCategory_name(), 
						"true", String.valueOf(dishCategories.getHotel_id()),String.valueOf(dishCategories.getSequence()),dishCategories.getImg_uri());
			} else {
				dishCategorieVO = new DishCategoriesVO(String.valueOf(dishCategories.getId()), dishCategories.getCategory_name(),
						"false", String.valueOf(dishCategories.getHotel_id()),String.valueOf(dishCategories.getSequence()),dishCategories.getImg_uri());
			}
			categoriesList.add(dishCategorieVO);
		}
		if (dishCategoriesVO.getHotel_id() != null && !(dishCategoriesVO.getHotel_id()).isEmpty()) {
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", categoriesList);
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Category information available");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * This post request will insert hotel dish category
	 * @param dishCategoriesVO
	 * @return
	 */
	@PostMapping("/addDishCategory")
	public @ResponseBody RestonzaRestResponseVO executeAddDishCategories(@RequestBody DishCategoriesVO dishCategoriesVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (dishCategoriesVO != null) {
			if (dishCategoriesVO.getHotel_id() != null && !(dishCategoriesVO.getHotel_id()).isEmpty()) {
				if (dishCategoriesRepository.isExist(dishCategoriesVO.getCategory_name(), Integer.parseInt(dishCategoriesVO.getHotel_id())) < 1) {
					int count = dishCategoriesRepository.countCategories(Integer.parseInt(dishCategoriesVO.getHotel_id()));
					DishCategories dishCategories = populateDishCategory(dishCategoriesVO,count);
					DishCategories dishCat = dishCategoriesRepository.save(dishCategories);
					restonzaRestResponseVO = new RestonzaRestResponseVO("success", dishCat);
				} else {
					restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Category already exists");
				}
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Insertion error");
			}
			return restonzaRestResponseVO;
			
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Insertion error");
			return restonzaRestResponseVO;
		}
	}
	
	/**
	 * Preparing request object to inject into crud repository
	 * @param dishCategoriesVO
	 * @return
	 */
	private DishCategories populateDishCategory(DishCategoriesVO dishCategoriesVO,int count) {
		DishCategories category = new DishCategories();
		category.setCategory_name(dishCategoriesVO.getCategory_name());
		if (dishCategoriesVO.getStatus().equals("enable")) {
			category.setStatus(true);
		} else {
			category.setStatus(false);
		}
		category.setImg_uri(dishCategoriesVO.getImageURI());
		category.setSequence(count+1);
		category.setHotel_id(Integer.parseInt(dishCategoriesVO.getHotel_id()));
		return category;
	}
	
	
	/**
	 * This post request will fetch all dishes for perticular hotel.
	 * @param dishesVO
	 * @return
	 */
	@PostMapping("/getDishes")
	public @ResponseBody RestonzaRestResponseVO executeGetDishes(@RequestBody DishesVO dishesVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (dishesVO.getHotel() > 0) {
			List<Dishes> dishes = dishesRepository.findDishes(dishesVO.getHotel());
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", dishes);
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Category information available");
		}
		return restonzaRestResponseVO;
	}
	
	@PostMapping("/getDishes2")
	public @ResponseBody RestonzaRestResponseVO executeGetDishes2(@RequestBody DishesVO dishesVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (dishesVO.getHotel() > 0) {
			List<Dishes> dishes = dishesRepository.findDishes2(dishesVO.getHotel());
			restonzaRestResponseVO = new RestonzaRestResponseVO("success", dishes);
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "No Category information available");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * THis post methos will add dishes into db
	 * @param dishesVO
	 * @return
	 */
	@PostMapping("/addDishes")
	public @ResponseBody RestonzaRestResponseVO executeaddDishes(@RequestBody DishesVO dishesVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		Dishes dish = null;
		if (dishesVO.getHotel() > 0) {
			if (dishesRepository.isExist(dishesVO.getDish_name(), dishesVO.getHotel() ) < 1) {
				int count = dishesRepository.count(dishesVO.getHotel());
				for (String category:dishesVO.getDish_category()) {
					Dishes dishes = populateDishes(dishesVO,category,count+1);
					if (dishes != null) {
						dish = dishesRepository.save(dishes);
					} else {
						restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error Inserting dishes");
					}
				}
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", dish);
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Dish already exists");
			}
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Error Inserting dish");
		}
		return restonzaRestResponseVO;
	}
	
	//TODO add logic for populating for multiple categories
		private Dishes populateDishes(DishesVO dishesVO,String category,int seqNumber) {
			Dishes dishes = new Dishes();
			
			if (dishesVO.getDish_name() != null && dishesVO.getDish_category() != null) {
				dishes.setDish_ingredients(dishesVO.getIngredients().toString());
				dishes.setDish_name(dishesVO.getDish_name());
				dishes.setDish_category(category);
				dishes.setDish_price(Double.parseDouble(dishesVO.getDish_price()));
				//dishes.setHot(true);
				//dishes.setStatus(true);
				dishes.setDish_description(dishesVO.getDish_description());
				dishes.setAvg_cookingtime(dishesVO.getAvg_cookingtime());
				dishes.setCalories(dishesVO.getCalories());
				dishes.setDiscount(dishesVO.getDiscount());
				dishes.setHotel(dishesVO.getHotel());
				dishes.setUnit("1");
				dishes.setImg_uri(dishesVO.getImageURI());
				if (dishesVO.getVegNonVeg().equalsIgnoreCase("false")) {
					dishes.setHot(false);
				} else {
					dishes.setHot(true);
				}
				
				if (dishesVO.getStatus().equalsIgnoreCase("false")) {
					dishes.setStatus(false);
				} else {
					dishes.setStatus(true);
				}
				/*dishes.setImage(dishesVO.getImage());*/
				dishes.setSequence(seqNumber);
				return dishes;
			}
			return null;
		}
	
	/**
	 * Get ingredients table based on page request
	 * @param ingredientVO
	 * @return pagePagableResponseVO
	 */
	@SuppressWarnings("unused")
	@PostMapping("/getIngredientsTable/{page}/{size}")
	public @ResponseBody RestonzaRestResponseVO executeGetIngredients(@RequestBody IngredientVO ingredientVO,@PathVariable Integer page, @PathVariable Integer size){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		String hotelId = ingredientVO.getHotel_id();
		if (RestonzaUtility.isNullOrEmpty(hotelId)) {
			/*List<Ingredient> ingredient = ingredientRepository.findIngredient(Integer.parseInt(hotelId), new PageRequest(page-1, size));*/
			List<Ingredient> ingredient = ingredientRepository.getAllIngredients(Integer.parseInt(hotelId));
			if (ingredient!=null) {
				//Pagination code--------------------------------------------------------------
				PagableResponseVO pagePagableResponseVO = new PagableResponseVO();
				pagePagableResponseVO.setIngredient(ingredient);
				//Integer totalRecordCount = ingredientRepository.getTotalCount(Integer.parseInt(hotelId));
				//pagePagableResponseVO.setTotalCount(totalRecordCount);
				//--------------------------------------------------------------------------------
				return restonzaRestResponseVO = new RestonzaRestResponseVO("success", pagePagableResponseVO);
			} else {
				return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Ingredient not available");
			}
		}
		return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Unexpected Error Occured! Please call administrator");
	}
	
	/**
	 * This method will add ingredients in db
	 */
	@SuppressWarnings("unused")
	@PostMapping("/addIngredients")
	public @ResponseBody RestonzaRestResponseVO executeaddIngredients(@RequestBody IngredientVO ingredientVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (ingredientVO != null) {
			if (ingredientRepository.isExist(ingredientVO.getIngredient_name(), Integer.parseInt(ingredientVO.getHotel_id())) < 1) {
				Ingredient ingredient =new Ingredient();
				 ingredient.setIngredients_name(ingredientVO.getIngredient_name());
				 if (ingredientVO.getStatus().equals("enable")) {
					 ingredient.setStatus(true);
				} else {
					ingredient.setStatus(false);
				}
				 
				 ingredient.setIngredient_hotel(Integer.parseInt(ingredientVO.getHotel_id()));
				 ingredientRepository.save(ingredient);
				 return restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Ingredient added successfully");
			} else {
				return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Ingredient already exists");
			}
			
		} else {
			return restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Inserting dishes");
		}
	}
	/**
	 * This will be called by add dish modal popup for fetching current ingredients and categories
	 * @param ingredientVO
	 * @return
	 */
	@SuppressWarnings("unused")
	@PostMapping("/getCategoriesAndIngredients")
	public @ResponseBody RestonzaRestResponseVO executeGetCategoriesAndIngredients(@RequestBody DishesVO dishesVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (dishesVO != null && dishesVO.getHotel() > 0) {
			List<String> categoriesList = dishCategoriesRepository.getCategories(dishesVO.getHotel());
			List<String> ingredientsList = ingredientRepository.getIngredients(dishesVO.getHotel());
			AddDishesModalResponse modalresponse = new AddDishesModalResponse();
			modalresponse.setCategories(categoriesList);
			modalresponse.setIngredients(ingredientsList);
			return restonzaRestResponseVO = new RestonzaRestResponseVO("success", modalresponse);
		} else {
			return restonzaRestResponseVO  = new RestonzaRestResponseVO("error", "Error Inserting dishes");
		}
	}
	
	/**
	 * Update the dish categories |dish category management
	 */
	@PostMapping("/updateDishCategory")
	public @ResponseBody RestonzaRestResponseVO exectuteUpdateCategory(@RequestBody DishCategoriesVO dishCategoriesVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (dishCategoriesVO != null) {
			if (dishCategoriesVO.getHotel_id() != null && !(dishCategoriesVO.getHotel_id()).isEmpty()) {
				// TODO add status variable 
				boolean status =false;
				if (dishCategoriesVO.getStatus().equalsIgnoreCase("true")) {
					 status =true;
				}
				dishCategoriesRepository.update(Integer.parseInt(dishCategoriesVO.getHotel_id()),
						dishCategoriesVO.getCategory_name(),
						Integer.parseInt(dishCategoriesVO.getCategory_id()),dishCategoriesVO.getImageURI(),status);
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Category is Updated successfully");
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Updation error");
			}
			return restonzaRestResponseVO;
			
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Updation error");
		}
		return restonzaRestResponseVO;
	}
	
	
	/**
	 * Update the dish  |dish category management
	 */
	@PostMapping("/updateDish")
	public @ResponseBody RestonzaRestResponseVO executeUpdatDish(@RequestBody DishesVO dishesVO) {
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (dishesVO != null && dishesVO.getDish_id() > 0) {
			if (RestonzaUtility.isNullOrEmpty(dishesVO.getDish_name()) 
					&& RestonzaUtility.isNullOrEmpty(dishesVO.getDish_description())
					&& RestonzaUtility.isNullOrEmpty(dishesVO.getDish_price())
					&& RestonzaUtility.isNullOrEmpty(dishesVO.getStatus())) {
				boolean status = false;
				boolean nonVeg = false;
				if (dishesVO.getStatus().equalsIgnoreCase("true")) {
					status = true;
				}
				if (dishesVO.getVegNonVeg().equalsIgnoreCase("true")) {
					nonVeg = true;
				}
				try {
					dishesRepository.update(
							dishesVO.getHotel(),dishesVO.getDish_id(),dishesVO.getDish_name(),
							dishesVO.getDish_description(),dishesVO.getDish_category().get(0),
							Double.parseDouble(dishesVO.getDish_price()), dishesVO.getDiscount(),
							status,nonVeg,dishesVO.getIngredients().toString(),dishesVO.getImageURI());
				} catch (Exception e) {
					return restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Updation error");
				}
				restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Dish is Updated successfully");
			} else {
				restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Updation error");
			}
			return restonzaRestResponseVO;
			
		} else {
			restonzaRestResponseVO = new RestonzaRestResponseVO("error", "Updation error");
		}
		return restonzaRestResponseVO;
	}
	
	/**
	 * Sequncing updatees for dish
	 * @param dishesVO
	 * @return
	 */
	@PostMapping("/updatedDishSequence")
	public @ResponseBody RestonzaRestResponseVO executeUpdatDishSequence(@RequestBody DishesVO dishesVO) {
		RestonzaRestResponseVO responseVO;
		List<String> sequnceList = dishesVO.getUpdatedSequence();
		List<Dishes> dishList = new ArrayList<Dishes>();
		for (String list:sequnceList) {
			Dishes dish = new Dishes();
			String[] array = list.split(" - ");
			dish.setSequence(Integer.valueOf(array[1]));
			dish.setDish_id(Integer.valueOf(array[0]));
			dishList.add(dish);
		}
		hotelAnalyzerRepository.bulkUpdateForSequnceDishes(dishList);
		responseVO = new RestonzaRestResponseVO("success","Sequnce Updated Sucessfully !");
		return responseVO;
	}
	
	/**
	 * 
	 */
	@PostMapping("/updatedCategorySequence")
	public @ResponseBody RestonzaRestResponseVO executeUpdatCatSequence(@RequestBody DishCategoriesVO vo) {
		RestonzaRestResponseVO responseVO;
		List<String> sequnceList = vo.getUpdatedSequence();
		List<DishCategories> catList = new ArrayList<DishCategories>();
		for (String list:sequnceList) {
			DishCategories cat = new DishCategories();
			String[] array = list.split(" - ");
			cat.setSequence(Integer.valueOf(array[1]));
			cat.setId(Integer.valueOf(array[0]));
			catList.add(cat);
		}
		hotelAnalyzerRepository.bulkUpdateForSequnceCategories(catList);
		return new RestonzaRestResponseVO("success","Sequnce Updated Sucessfully !");
	}
	
	/**
	 * Sequence update for the bar categories.
	 */
	@PostMapping("/updatedBarCategorySequence")
	public @ResponseBody RestonzaRestResponseVO executeUpdatBarCatSequence(@RequestBody DishCategoriesVO vo) {
		RestonzaRestResponseVO responseVO;
		List<String> sequnceList = vo.getUpdatedSequence();
		List<BarCategory> catList = new ArrayList<BarCategory>();
		for (String list:sequnceList) {
			BarCategory cat = new BarCategory();
			String[] array = list.split(" - ");
			cat.setSequence(Integer.valueOf(array[1]));
			cat.setId(Integer.valueOf(array[0]));
			catList.add(cat);
		}
		hotelAnalyzerRepository.bulkUpdateForSequnceBarCategories(catList);
		return new RestonzaRestResponseVO("success","Sequnce Updated Sucessfully !");
	}
	
	/**
	 * Sequence update for the bar items.
	 */
	@PostMapping("/updatedBarItemSequence")
	public @ResponseBody RestonzaRestResponseVO executeUpdateBarItemSequence(@RequestBody DishCategoriesVO vo) {
		RestonzaRestResponseVO responseVO;
		List<String> sequnceList = vo.getUpdatedSequence();
		List<BarItems> catList = new ArrayList<BarItems>();
		for (String list:sequnceList) {
			BarItems cat = new BarItems();
			String[] array = list.split(" - ");
			cat.setSequence(Integer.valueOf(array[1]));
			cat.setId(Integer.valueOf(array[0]));
			catList.add(cat);
		}
		hotelAnalyzerRepository.bulkUpdateForSequnceBarItems(catList);
		return new RestonzaRestResponseVO("success","Sequnce Updated Sucessfully !");
	}
	
	/**
	 * post request for getting ads details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/deleteDish")
	public @ResponseBody RestonzaRestResponseVO executeDeleteDish(@RequestBody AdsDetailsVO adsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		dishesRepository.delete(Integer.parseInt(adsDetailsVO.getId()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully deleted dish");
		return restonzaRestResponseVO;
	}
	

	/**
	 * post request for getting ads details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/deleteIngredient")
	public @ResponseBody RestonzaRestResponseVO executeDeleteIngredient(@RequestBody AdsDetailsVO adsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		ingredientRepository.delete(Integer.parseInt(adsDetailsVO.getId()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully deleted dish");
		return restonzaRestResponseVO;
	}
	/**
	 * Registering player id in db for post notification
	 * @param id
	 * @param playerId
	 */
	@RequestMapping(value = "/registerOneSignalUserId/{id}/{playerId}", method = RequestMethod.GET)
	public void executeRegisterOneSignalPlayer(@PathVariable("id") String id, @PathVariable("playerId") String playerId){
		if (id != null && playerId != null) {
			PushNotifierDAO dao = new PushNotifierDAO();
			dao.setHotelid(Integer.valueOf(id));
			dao.setPlayerid(playerId);
			webPushNotifierRepository.save(dao);
		}
	}
	/**
	 * post request for getting ads details
	 * @param adsDetailsVO
	 * @return
	 */
	@PostMapping("/deleteCategory")
	public @ResponseBody RestonzaRestResponseVO executeDeleteCategory(@RequestBody AdsDetailsVO adsDetailsVO){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		dishCategoriesRepository.delete(Integer.parseInt(adsDetailsVO.getId()));
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", "Successfully deleted dish");
		return restonzaRestResponseVO;
	}
	
	/**
	 * post request for updating ingredient + update ingredient which is added at dish level
	 * @param ingredientVO
	 * @return restonzaRestResponseVO
	 */
	@SuppressWarnings("unused")
	@PostMapping("/updateIngredient")
	public @ResponseBody RestonzaRestResponseVO executeUpdateIngredient(@RequestBody IngredientVO ingredientVO) throws Exception{
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		if (!RestonzaUtility.isObjectFieldsForNullOREmpty(ingredientVO)) {
			String oldIngredientName = ingredientRepository.getIngrdientName(ingredientVO.getOld_ingredient_id());
			//update ingredient table record-----------------------------------------------------------------------------
			int ingredientID = ingredientVO.getOld_ingredient_id();
			boolean status = Boolean.valueOf(ingredientVO.getStatus());
			String newIngredientName = ingredientVO.getIngredient_name();
			int hoteId = Integer.parseInt(ingredientVO.getHotel_id());
			ingredientRepository.updateIngredientNameOrStatusById(status, newIngredientName, ingredientID);
			return restonzaRestResponseVO = new RestonzaRestResponseVO("success", RestonzaResponseConstants.SUCCESSFULLY_CHANGED);
		}
		return restonzaRestResponseVO = new RestonzaRestResponseVO("error", RestonzaResponseConstants.INPUT_PARAMTER_MISSING);
	}
	
	@PostMapping("/getHotelItems/{hotel_id}")
	public @ResponseBody RestonzaRestResponseVO executeGetDishes(@PathVariable("hotel_id") String hotel_id){
		RestonzaRestResponseVO restonzaRestResponseVO = null;
		Integer hotelId = Integer.parseInt(hotel_id);
		List<Map<String, Object>> dishes = hotelAnalyzerRepository.getHotelDishesforCombineBill(hotelId);
		restonzaRestResponseVO = new RestonzaRestResponseVO("success", dishes);
		return restonzaRestResponseVO;
	}
	
	
	@PostMapping("/getIngredientsTable")
	public @ResponseBody RestonzaRestResponseVO executeGetAllIngredients(@RequestBody IngredientVO ingredientVO){
		String hotelId = ingredientVO.getHotel_id();
		if (RestonzaUtility.isNullOrEmpty(hotelId)) {
			List<Ingredient> ingredient = ingredientRepository.getAllIngredients(Integer.parseInt(hotelId));
			if (ingredient!=null) {
				PagableResponseVO pagePagableResponseVO = new PagableResponseVO();
				pagePagableResponseVO.setIngredient(ingredient);
				return new RestonzaRestResponseVO("success", pagePagableResponseVO);
			} else {
				return new RestonzaRestResponseVO("error", "Ingredient not available");
			}
		}
		return new RestonzaRestResponseVO("error", "Unexpected Error Occured! Please call administrator");
	}
	
	@PostMapping("/getStaticImageListing/{type}")
	public @ResponseBody RestonzaRestResponseVO executeGetAllStaticImages(@PathVariable("type") String type){
		String path = null;
		if (type.equalsIgnoreCase("bar")) {
			 path = imageuploaddirectory + "/barimg";
		} else if (type.equalsIgnoreCase("dish")) {
			 path = imageuploaddirectory + "/dishimg";
		}
        File directory = new File(path);
        File[] fList = directory.listFiles();
        List<StaticImageListingVO> list = new ArrayList<StaticImageListingVO>();
        
        for (int i=0;i<fList.length;i+=4) {
        	StaticImageListingVO listingObject = new StaticImageListingVO();
        	String prefix = null;
	    	if (type.equalsIgnoreCase("bar")) {
	    		prefix = "http://173.255.250.117:8888/img/barimg/";
	   		} else if (type.equalsIgnoreCase("dish")) {
	   			prefix = "http://173.255.250.117:8888/img/dishimg/";
	   		} 
	    	if (i<fList.length) {
	    		listingObject.setImg1(prefix+fList[i].getName());
	    	}
			if (i+1<fList.length) {
				listingObject.setImg2(prefix+fList[i+1].getName());  		
			}
			if (i+2<fList.length) {
				listingObject.setImg3(prefix+fList[i+2].getName());
			}
			if (i+3<fList.length) {
				listingObject.setImg4(prefix+fList[i+3].getName());
			}
			list.add(listingObject);
        }
        return new RestonzaRestResponseVO("success", list);
	}
}
