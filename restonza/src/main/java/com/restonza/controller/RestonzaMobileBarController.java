package com.restonza.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.restonza.dao.BarCategory;
import com.restonza.dao.repository.BarCategoryRepository;
import com.restonza.dao.repository.BarItemsRepository;
import com.restonza.dao.repository.HotelAnalyzerRepository;
import com.restonza.vo.BarItemServingQty;
import com.restonza.vo.BarMenuRequestVO;
import com.restonza.vo.BatItemsResponseVO;
import com.restonza.vo.RestonzaRestResponseVO;

@RestController
@RequestMapping("/restonza/mobile")
public class RestonzaMobileBarController {
	
	@Autowired
	private BarCategoryRepository barCategoryRepository;
	
	@Autowired
	private BarItemsRepository barItemsRepository;
	
	@Autowired
	private HotelAnalyzerRepository hotelAnalyzerRepository;
	
	/**
	 * It will fetch all the categories in added by hotel
	 * @param barMenuRequestVO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/getBarCategories")
	public @ResponseBody RestonzaRestResponseVO executeGetBarCategory(@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		int hotelId = barMenuRequestVO.getHotel_id();
		List<BarCategory> allBarCategories = barCategoryRepository.getActiveCategory(hotelId);
		return new RestonzaRestResponseVO("success", allBarCategories);
	}
	
	/**
	 * It will fetch all item names under provided categories
	 * @param barMenuRequestVO
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/getCategoryItems")
	public @ResponseBody RestonzaRestResponseVO executeGetBarItems(@Validated(BarMenuRequestVO.GetCategoryItems.class)@RequestBody BarMenuRequestVO barMenuRequestVO) throws Exception {
		int hotelId = barMenuRequestVO.getHotel_id();
		int barCatID = barMenuRequestVO.getBar_category_id();
		List<Map<String,Object>> allBarItems = hotelAnalyzerRepository.getBarCategoryItems(hotelId,barCatID);
		List<BatItemsResponseVO> responseVO   = barIndependentElements(allBarItems);
		return new RestonzaRestResponseVO("success", responseVO);
	}
	
	private List<BatItemsResponseVO> barIndependentElements(List<Map<String,Object>> allBarItems) {
		List<BatItemsResponseVO> list =  new ArrayList<BatItemsResponseVO>();
		for (Map<String,Object> mp:allBarItems) {
			BatItemsResponseVO item = new BatItemsResponseVO();
			String itemName = ((String) mp.get("bar_item_name")).split("\\(")[0];
			
			boolean flag = doesItExist(list,itemName);
			if (!flag) {
				List<BarItemServingQty> listOfRelatedItems = getRelatedItems(itemName,allBarItems);
				//item.setId((Integer) mp.get("id"));
				item.setBarimg((String) mp.get("barimg"));
				item.setBar_item_name(itemName);
				item.setSub_category((String) mp.get("sub_category"));
				item.setList(listOfRelatedItems);
				list.add(item);
			} else {
				List<BarItemServingQty> listOfRelatedItems = null;
				item.setBarimg((String) mp.get("barimg"));
				item.setBar_item_name(itemName);
				item.setSub_category((String) mp.get("sub_category"));
				item.setList(listOfRelatedItems);
			}
		}
		return list;
	}
	
	private List<BarItemServingQty> getRelatedItems(String itemName, List<Map<String,Object>> allBarItems) {
		List<BarItemServingQty> list = new ArrayList<BarItemServingQty>();
		for (Map<String,Object> mp:allBarItems) {
			String mapItem = ((String) mp.get("bar_item_name")).split("\\(")[0];
			if (itemName.equals(mapItem)) {
				BarItemServingQty barItem = new BarItemServingQty();
				barItem.setName((String) mp.get("bar_item_name"));
				barItem.setPrice((Double)mp.get("price"));;
				barItem.setQty((String)mp.get("qty"));
				barItem.setStatus((Boolean)mp.get("status"));
				barItem.setId((Integer)mp.get("id"));
				list.add(barItem);
			}
		}
		return list;
	}
	
	private boolean doesItExist(List<BatItemsResponseVO> list, String itemName) {
		for (BatItemsResponseVO vo:list) {
			if(vo.getBar_item_name().equals(itemName)){
				return true;
			}
		}
		return false;
	}
}
