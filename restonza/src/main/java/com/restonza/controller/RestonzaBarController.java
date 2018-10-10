/**
 * 
 */
package com.restonza.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.BarCategory;
import com.restonza.dao.BarItems;
import com.restonza.dao.repository.BarCategoryRepository;
import com.restonza.dao.repository.BarItemsRepository;
import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.util.service.RestonzaCustomException;
import com.restonza.vo.BarMenuBulkUploadVO;
import com.restonza.vo.BarMenuRequestVO;
import com.restonza.vo.RestonzaRestResponseVO;
import com.restonza.vo.SubBarServingMannerVO;

/**
 * @author flex-grow developers
 *
 */
@RestController
@RequestMapping("/restonza")
public class RestonzaBarController {
	@Autowired
	private BarCategoryRepository barCategoryRepository;
	
	@Autowired
	private BarItemsRepository barItemsRepository;
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	@PostMapping("/getBarCategories/{hotel_id}")
	public @ResponseBody RestonzaRestResponseVO executeGetBarCategory(@PathVariable("hotel_id")String hotel_id) throws Exception {
		int hotelId = Integer.parseInt(hotel_id);
		List<BarCategory> allBarCategories = barCategoryRepository.getAllCategory(hotelId);
		return new RestonzaRestResponseVO("success", allBarCategories);
	}
	
	// MObile
	@PostMapping("/getActiveBarCategories/{hotel_id}")
	public @ResponseBody RestonzaRestResponseVO executeGetActiveBarCategory(@PathVariable("hotel_id")String hotel_id) throws Exception {
		int hotelId = Integer.parseInt(hotel_id);
		List<BarCategory> allBarCategories = barCategoryRepository.getAllActiveCategory(hotelId,true);
		return new RestonzaRestResponseVO("success", allBarCategories);
	}
	
	@PostMapping("/addBarCategory")
	public @ResponseBody RestonzaRestResponseVO executeAddBarCategory(@Validated(BarMenuRequestVO.CategoryRequest.class)@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		String barCategoryName = barMenuRequestVO.getBar_category_name();
		int hotelId = barMenuRequestVO.getHotel_id();
		Integer id = barCategoryRepository.isCategoryExists(barCategoryName, hotelId);
		if (null != id)
			throw new RestonzaCustomException("Category Already exists");
		int count = barCategoryRepository.count(hotelId);
		BarCategory barCategory = new BarCategory();
		barCategory.setBar_category_name(barCategoryName);
		barCategory.setBar_sub_category(String.join(",", barMenuRequestVO.getBar_sub_category()));
		barCategory.setHotel_id(hotelId);
		barCategory.setStatus(barMenuRequestVO.isStatus());
		barCategory.setImg_uri(barMenuRequestVO.getImage());
		barCategory.setSequence(count+1);
		BarCategory cat = barCategoryRepository.save(barCategory);
		return new RestonzaRestResponseVO("success", cat);
		
	}
	
	@PostMapping("/updateBarCategory")
	public @ResponseBody RestonzaRestResponseVO executeUpdateBarCategory(@Validated(BarMenuRequestVO.CategoryUpdate.class)@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		String barCategoryName = barMenuRequestVO.getBar_category_name();
		int hotelId = barMenuRequestVO.getHotel_id();
		int barCategoryId = barMenuRequestVO.getBar_category_id();
		Integer id = barCategoryRepository.isUpdatedCategoryExists(barCategoryName, hotelId, barCategoryId);
		if (null != id) 
			throw new RestonzaCustomException("Update Category Already exists");
		BarCategory barCategory = new BarCategory();
		barCategory.setId(barCategoryId);
		barCategory.setBar_category_name(barCategoryName);
		//barCategory.setBar_sub_category(String.join(",", barMenuRequestVO.getBar_sub_category()));
		barCategory.setHotel_id(hotelId);
		barCategory.setStatus(barMenuRequestVO.isStatus());
		barCategory.setImg_uri(barMenuRequestVO.getImage());
		barCategory.setSequence(barMenuRequestVO.getSequence());
		barCategoryRepository.save(barCategory);
		return new RestonzaRestResponseVO("success", "Category updated Successfully");
		
	}
	
	@PostMapping("/deleteBarCategory/{id}")
	public @ResponseBody RestonzaRestResponseVO executeDeleteBarCategory(@PathVariable("id")String bar_category_id) throws Exception {
		int barCategoryId = Integer.parseInt(bar_category_id);
		barCategoryRepository.delete(barCategoryId);
		return new RestonzaRestResponseVO("success", "Category removed successfully");
	}
	
	//TODO needs to be edited.
	@PostMapping("/getBarItems/{hotel_id}")
	public @ResponseBody RestonzaRestResponseVO executeGetBarItems(@PathVariable("hotel_id")String hotel_id) throws Exception {
		int hotelId = Integer.parseInt(hotel_id);
		List<Map<String,Object>> allBarItems = hotelAnalyzerRepository.getBarItems(hotelId);
		return new RestonzaRestResponseVO("success", allBarItems);
	}
	
	// Mobile
	@PostMapping("/getBarItemNameList/{hotel_id}")
	public @ResponseBody RestonzaRestResponseVO executeGetBarItemsNameList(@PathVariable("hotel_id")String hotel_id) throws Exception {
		int hotelId = Integer.parseInt(hotel_id);
		List<BarItems> barMenuItemNames = barItemsRepository.getActiveBarMenuNames(hotelId,true);
		return new RestonzaRestResponseVO("success", barMenuItemNames);
	}
	
	@PostMapping("/addBarItem")
	public @ResponseBody RestonzaRestResponseVO executeAddBarItem(@Validated(BarMenuRequestVO.BarItemRequest.class)@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		String barCategoryName = barMenuRequestVO.getBar_category_name();
		String barItemName = barMenuRequestVO.getBar_item_name();
		int hotelId = barMenuRequestVO.getHotel_id();
		Integer categoryId = barCategoryRepository.isCategoryExists(barCategoryName, hotelId);
		Integer barItemId = barItemsRepository.isItemExists(barItemName, hotelId);
		if (null != barItemId) 
			throw new RestonzaCustomException("Item alreay exists");
		if (null == categoryId) 
			throw new RestonzaCustomException("Category does not exists");
		List<BarMenuBulkUploadVO> barmenubulkuploadVOs = new ArrayList<BarMenuBulkUploadVO>();
		List<SubBarServingMannerVO> servingMannerVOs = barMenuRequestVO.getServing_manner_vo();
		if (servingMannerVOs == null) {
			BarMenuBulkUploadVO barmenubulkupload = new BarMenuBulkUploadVO();
			barmenubulkupload.setBar_item_name(barItemName);
			barmenubulkupload.setCategory_id(categoryId);
			barmenubulkupload.setSub_category(barMenuRequestVO.getBar_sub_category_selected());
			barmenubulkupload.setServered_in_qty(barMenuRequestVO.isServered_in_qty());
			barmenubulkupload.setHotel_id(hotelId);
			barmenubulkupload.setPrice(Double.parseDouble(barMenuRequestVO.getBarmenuprice()));
			barmenubulkupload.setStatus(barMenuRequestVO.isStatus());
			barmenubulkupload.setBarimg(barMenuRequestVO.getImage());
			barmenubulkuploadVOs.add(barmenubulkupload);
		}else {
			for (SubBarServingMannerVO subBarServingMannerVO : servingMannerVOs) {
				BarMenuBulkUploadVO barmenubulkupload = new BarMenuBulkUploadVO();
				String qty = subBarServingMannerVO.getQty();
				StringBuilder baritemNamesb = new StringBuilder();
				baritemNamesb.append(barItemName);
				baritemNamesb.append("(");
				baritemNamesb.append(qty);
				baritemNamesb.append(")");
				barmenubulkupload.setBar_item_name(baritemNamesb.toString());
				barmenubulkupload.setCategory_id(categoryId);
				barmenubulkupload.setSub_category(barMenuRequestVO.getBar_sub_category_selected());
				barmenubulkupload.setServered_in_qty(barMenuRequestVO.isServered_in_qty());
				barmenubulkupload.setHotel_id(hotelId);
				barmenubulkupload.setQty(qty);
				barmenubulkupload.setPrice(Double.parseDouble(subBarServingMannerVO.getPrice()));
				barmenubulkupload.setStatus(barMenuRequestVO.isStatus());
				barmenubulkupload.setBarimg(barMenuRequestVO.getImage());
				barmenubulkuploadVOs.add(barmenubulkupload);
			}
		}
		int[][] intresult = hotelAnalyzerRepository.bulkBarMenuUploadSave(barmenubulkuploadVOs);
		//int intresult = hotelAnalyzerRepository.addbarMenu(barmenubulkuploadVOs);
		return new RestonzaRestResponseVO("success", intresult);
		
	}
	
	/*@PostMapping("/updateBarItem")
	public @ResponseBody RestonzaRestResponseVO executeUpdateBarItem(@Validated(BarMenuRequestVO.BarItemUpdate.class)@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		String barItem = barMenuRequestVO.getBar_category_name();
		int hotelId = barMenuRequestVO.getHotel_id();
		int barCategoryId = barMenuRequestVO.getBar_category_id();
		Integer id = barCategoryRepository.isUpdatedCategoryExists(barCategoryName, hotelId, barCategoryId);
		if (null != id) 
			throw new RestonzaCustomException("Update Category Already exists");
		BarCategory barCategory = new BarCategory();
		barCategory.setId(barCategoryId);
		barCategory.setBar_category_name(barCategoryName);
		barCategory.setBar_sub_category(String.join(",", barMenuRequestVO.getBar_sub_category()));
		barCategory.setHotel_id(hotelId);
		barCategory.setStatus(barMenuRequestVO.isStatus());
		barCategoryRepository.save(barCategory);
		return new RestonzaRestResponseVO("success", "Category updated Successfully");
		
	}*/
	
	@PostMapping("/deleteBarItem/{id}")
	public @ResponseBody RestonzaRestResponseVO executeDeleteBarItem(@PathVariable("id")String bar_item_id) throws Exception {
		int barItemId = Integer.parseInt(bar_item_id);
		barItemsRepository.delete(barItemId);
		return new RestonzaRestResponseVO("success", "Item Deleted successfully");
	}
	
	@PostMapping("/updateBarItem")
	public @ResponseBody RestonzaRestResponseVO executeUpdateBarItem(@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		int id = Integer.valueOf(barMenuRequestVO.getBar_item_id());
		double price = Double.valueOf(barMenuRequestVO.getBarmenuprice());
		boolean status  = barMenuRequestVO.isStatus();
		String name = barMenuRequestVO.getBar_item_name();
		barItemsRepository.updateBarItem(name, price, status, id,barMenuRequestVO.getImage());
		return new RestonzaRestResponseVO("success", "Item Updated successfully");
	}
		
}
