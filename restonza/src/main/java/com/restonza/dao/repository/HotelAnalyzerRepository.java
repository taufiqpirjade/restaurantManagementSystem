/**
 * 
 */
package com.restonza.dao.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.restonza.dao.BarCategory;
import com.restonza.dao.BarItems;
import com.restonza.dao.DishCategories;
import com.restonza.dao.Dishes;
import com.restonza.dao.HotelAnalyze;
import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaPushNotificationService;
import com.restonza.util.service.RestonzaUtility;
import com.restonza.vo.AllOrderVO;
import com.restonza.vo.BarMenuBulkUploadVO;
import com.restonza.vo.BulkMenuUpload;
import com.restonza.vo.FeedBackVO;
import com.restonza.vo.GeneralOrderVO;
import com.restonza.vo.GenerateBillVO;
import com.restonza.vo.HotelAnalysisVO;
import com.restonza.vo.HotelDishMenuPdf;
import com.restonza.vo.HotelReportVO;
import com.restonza.vo.OrderDetailsVO;
import com.restonza.vo.OrderHistoryDetailsVO;
import com.restonza.vo.PlacedOrderDetailsVO;
import com.restonza.vo.TableVO;
import com.restonza.vo.TrendingDishes;

/**
 * @author flex-grow developers
 *
 */
@Repository
public class HotelAnalyzerRepository {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private HotelDishesRepositories hotelDishesRepositories;
	
	@Autowired
	private BarItemsRepository barItemsRepository;
	
	private final static Logger logger = LoggerFactory.getLogger(RestonzaPushNotificationService.class);
	
	@PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
	
	public int[][] bulkSave(List<HotelAnalyze> hotelAnalyzes) {
        String sql = "insert into hotel_analyzer (orderids, hotel_id, customer_id,order_dish_name,feedback,qty) values (?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.batchUpdate(sql, hotelAnalyzes, hotelAnalyzes.size(),
        		new ParameterizedPreparedStatementSetter<HotelAnalyze>() {
					@Override
					public void setValues(PreparedStatement ps, HotelAnalyze hotelAnalyze) throws SQLException {
						ps.setString(1, hotelAnalyze.getOrderids());
						ps.setInt(2, hotelAnalyze.getHotel_id());
						ps.setString(3, hotelAnalyze.getCustomer_id());
						ps.setString(4, hotelAnalyze.getOrder_dish_name());
						ps.setInt(5, hotelAnalyze.getFeedback());
						ps.setInt(6, hotelAnalyze.getQty());
					}
				});
	};
	
	public int[][] bulkUpdate(List<String> orderIDs) {
        String sql = "update order_details set status='billed' where id=?";

        return jdbcTemplate.batchUpdate(sql, orderIDs, orderIDs.size(),
        		new ParameterizedPreparedStatementSetter<String>() {
					@Override
					public void setValues(PreparedStatement ps, String hotelAnalyze) throws SQLException {
						ps.setInt(1, Integer.parseInt(hotelAnalyze));
					}
				});
	};
	
	public void bulkUpdateBarOrders(int orderIDs) {
        String sql = "update bar_order_parent_table set order_status='billed' where id=?";
        jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, orderIDs);
			}
		});
        
        String sql1 = "update bar_order_details set order_status='billed' where parent_order_id=? and order_status != ?";
        jdbcTemplate.update(sql1, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, orderIDs);
				ps.setString(2, RESTONZACONSTANTS.cancel_order.toString());
			}
		});
	};
	
	public List<AllOrderVO> getOrderList(int hotelid) {
		 String sql = "SELECT customer_id,table_id,GROUP_CONCAT(id SEPARATOR ',') as orderids, "
		 		+ "GROUP_CONCAT(order_summary SEPARATOR '') as ordersummaries, sum(amount)"
		 		+ " as sum FROM order_details where hotel_id=? and status!='cancel_order' and status!='billed'  GROUP BY customer_id,table_id";
		 List<AllOrderVO> listObject = new ArrayList<AllOrderVO>();
		 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
		 for (Map<String, Object> map : orders) {
			AllOrderVO allOrderVO = new AllOrderVO();
			allOrderVO.setCustomerid((String) (map.get("customer_id")));
			allOrderVO.setTableid(String.valueOf((Integer)(map.get("table_id"))));
			allOrderVO.setOrderids((String) map.get("orderids"));
			String ordersummaries = (String) map.get("ordersummaries");
			allOrderVO.setOrdersummaries(ordersummaries);
			allOrderVO.setFormatOrderSummary(RestonzaUtility.formatOrderSummaries(ordersummaries));
			Double sum = (Double)map.get("sum");
			allOrderVO.setSum(sum);
			listObject.add(allOrderVO);
		}
		 System.out.println(listObject);
		 return listObject;
	}
	
	public List<HotelAnalysisVO> getHotelAnalysis(int hotelid) {
		 String sql = "SELECT CONCAT(MONTHNAME(billed_date), ' ', YEAR(billed_date)) AS Month, sum(bill_amount) as sum FROM billing_history where hotel_id= ? GROUP BY Month";
		 List<HotelAnalysisVO> listObject = new ArrayList<HotelAnalysisVO>();
		 final String[] colorcodes = {"#FF0F00","#FF6600","#FF9E01", "#FCD202","#F8FF01","#B0DE09","#04D215","#0D8ECF","#0D52D1","#2A0CD0","#8A0CCF"};
		 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
		 int i=1;
		 for (Map<String, Object> map : orders) {
			 HotelAnalysisVO feedBackVO = new HotelAnalysisVO();
			 feedBackVO.setMonth((String)(map.get("Month")));
			 double sum = (Double)map.get("sum");
			 feedBackVO.setTotal_earnings(sum);
			 feedBackVO.setColorcode(colorcodes[i%12]);
			 listObject.add(feedBackVO);
			 i++;
		}
		 System.out.println(listObject);
		 return listObject;
	}
	
	public List<TrendingDishes> getTrendingDishes(int hotelid) {
		 String sql = "select order_dish_name, count(order_dish_name) as sold from hotel_analyzer where hotel_id=? group by order_dish_name order by sold desc limit 0,5";
		 List<TrendingDishes> listObject = new ArrayList<TrendingDishes>();
		 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
		 for (Map<String, Object> map : orders) {
			 TrendingDishes feedBackVO = new TrendingDishes();
			 String dishname = (String)(map.get("order_dish_name"));
			 feedBackVO.setDishname(dishname);
			 String dishimage = hotelDishesRepositories.getDishImage(dishname, hotelid);
			 feedBackVO.setDishimage(dishimage);
			 listObject.add(feedBackVO);
		}
		 System.out.println(listObject);
		 return listObject;
	}
	
	public List<FeedBackVO> getFeedback(int hotelid) {
		 String sql = "select customer_name, feedback_comments, point from feedback where hotel_id=? and feedback_comments != '' and feedback_customer_id != 'admin' order by rand() limit 0,5";
		 List<FeedBackVO> listObject = new ArrayList<FeedBackVO>();
		 List<Map<String, Object>> feedbacks = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
		 for (Map<String, Object> map : feedbacks) {
			 FeedBackVO feedBackVO = new FeedBackVO();
			 String customername = (String)(map.get("customer_name"));
			 feedBackVO.setCustomer_name(customername);
			 String comments = (String)(map.get("feedback_comments"));
			 feedBackVO.setComments(comments);
			 int point = (int)(map.get("point"));
			 feedBackVO.setRating(String.valueOf(point));
			 listObject.add(feedBackVO);
		}
		 return listObject;
	}
	
	public List<HotelAnalysisVO> getFeedbackCount(int hotelid) {
		final String[] colorcodes = {"#FF0F00","#FF6600","#FF9E01", "#FCD202","#F8FF01","#B0DE09","#04D215","#0D8ECF","#0D52D1","#2A0CD0","#8A0CCF"};
		 String sql = "select point,count(point) as points from feedback where hotel_id=? and feedback_customer_id != 'admin' group by point";
		 List<HotelAnalysisVO> listObject = new ArrayList<HotelAnalysisVO>();
		 List<Map<String, Object>> feedbacks = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
		 int i=1;
		 for (Map<String, Object> map : feedbacks) {
			 HotelAnalysisVO feedBackVO = new HotelAnalysisVO();
			 int pointCategory = (int)(map.get("point"));
			 feedBackVO.setMonth(String.valueOf(pointCategory));
			 long point = (long)(map.get("points"));
			 feedBackVO.setTotal_earnings(Integer.parseInt(String.valueOf(point)));
			 feedBackVO.setColorcode(colorcodes[i%12]);
			 listObject.add(feedBackVO);
			 i++;
		}
		 return listObject;
	}
	
	//get occupied table list
	public List<TableVO> getOccupiedTableList(int hotelid) {
		 String sql = "select distinct table_id from order_details where hotel_id=? and status != 'billed' and status != 'cancel_order' and updated_on > DATE_SUB(CURDATE(), INTERVAL 1 DAY) order by table_id";
		 List<TableVO> listOfTables = new ArrayList<TableVO>();
		 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
		 for (Map<String, Object> map : orders) {
			 TableVO tablevo = new TableVO();
			int table_id = (int)(map.get("table_id"));
			tablevo.setTable_id(String.valueOf(table_id));
			listOfTables.add(tablevo);
		}
		 return listOfTables;
	}
	
	//get occupied table list
	public List<TableVO> getOccupiedTableList1(int hotelid) {
		 String sqlCheckForFood = "select distinct od.table_id,td.people_sitting_currently from order_details od INNER JOIN table_details td ON od.table_id=td.table_id where od.hotel_id=? and td.hotel_id=? and od.status != 'billed' and \r\n" + 
		 		"od.status != 'cancel_order' and od.updated_on > DATE_SUB(CURDATE(), INTERVAL 1 DAY) order by od.table_id";
		 String sqlCheckForBar = "select distinct od.table_id,td.people_sitting_currently from bar_order_parent_table od INNER JOIN table_details td ON od.table_id=td.table_id where od.hotel_id=? and td.hotel_id=? and od.order_status != 'billed' and \r\n" + 
			 		"od.order_status != 'cancel_order' and od.updated_time > DATE_SUB(CURDATE(), INTERVAL 1 DAY) order by od.table_id";
		 List<TableVO> listOfTables = new ArrayList<TableVO>();
		 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sqlCheckForFood, new Object[] { hotelid,hotelid });
		 List<Map<String, Object>> orders2 = jdbcTemplate.queryForList(sqlCheckForBar, new Object[] { hotelid,hotelid });
		 orders.removeAll(new HashSet<>(orders2));
		 orders2.addAll(orders);
		 for (Map<String, Object> map : orders2) {
			TableVO tablevo = new TableVO();
			int table_id = (int)(map.get("table_id"));
			int noOfPeopleSitting =0;
			if (map.get("people_sitting_currently") != null) {
				noOfPeopleSitting =(int) map.get("people_sitting_currently");
			}
			tablevo.setTable_id(String.valueOf(table_id));
			tablevo.setNumberOfPeopleSitting(noOfPeopleSitting);
			listOfTables.add(tablevo);
		}
		 return listOfTables;
	}
	
	public Map<String, Object> getCurrentTableStructure(int hotelid) {
		Map<String, Object> response = new HashMap<String, Object>();
		int totalActiveUsers = 0;
		//String sql = "select hotel_table_count from hotel_details where id=?";
		String sql = "select table_id as tableid, status,  (select sum(people_sitting_currently) from table_details where hotel_id=?) as totalActiveUsers  from table_details where hotel_id=?";
		String sql1 = "select sum(bill_amount) as sum from billing_history where hotel_id=? and billed_date > NOW() - INTERVAL 1 DAY";
		List<Map<String, Object>> tableCount = jdbcTemplate.queryForList(sql, new Object[] { hotelid,hotelid });
		List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql1, new Object[] { hotelid });
		Map<Integer,String> listTableCountMap = new HashMap<Integer,String>();
		for (Map<String, Object> map : tableCount) {
			if ("true".equalsIgnoreCase(map.get("status").toString())) {
				int tablecount = Integer.valueOf(map.get("tableid").toString());
				listTableCountMap.put(tablecount,map.get("status").toString());	
			}
			totalActiveUsers = Integer.valueOf((Integer)map.get("totalActiveUsers"));
			
		}
		 for (Map<String, Object> map : orders) {
			 if (map.get("sum") !=null) {
				 double bill_amount = (Double)(map.get("sum"));
				 response.put("todaysEarning",bill_amount);
			 }
		}
		List<TableVO> tableList = getOccupiedTableList1(hotelid);
		response.put("occupiedTableList",tableList);
		response.put("tableCount",tableCount.size());
		response.put("activetables",listTableCountMap);
		response.put("totalActiveUsers",totalActiveUsers);
		return response;
	}
	
	//get todays earning
		public List<HotelAnalysisVO> getTodaysEarning(int hotelid) {
			List<HotelAnalysisVO> list = new ArrayList<HotelAnalysisVO>();
			 HotelAnalysisVO feedBackVO = new HotelAnalysisVO();
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			 String sql = "select sum(bill_amount) as sum from billing_history where hotel_id=? and billed_date > NOW() - INTERVAL 1 DAY";
			try {
				 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
				 for (Map<String, Object> map : orders) {
					 if (map !=null) {
						 double bill_amount = (Double)(map.get("sum"));
						 feedBackVO.setMonth(sdf.format(new Date()));
						 if (bill_amount > 0) {
							 feedBackVO.setTotal_earnings(bill_amount);
						} else {
							feedBackVO.setTotal_earnings(0);
						}
					 feedBackVO.setColorcode("#F8FF01");
					 list.add(feedBackVO);
					 }
				}
			} catch(Exception e){
				logger.error("Error occured in getting todays earning"+e);
				return null;
			}
			return list;
		}
		
		//report analysis
		public Map<String, Object> getReportGenerate(HotelReportVO hotelReportVO, String toDate, String fromDate) throws ParseException {
			int hotelid = Integer.parseInt(hotelReportVO.getHotelname());
			//select distinct order_ids from billing_history where billed_date BETWEEN '2017-03-29' AND '2017-03-30 23:59:59'
			String sql = "select distinct order_ids as orderids from billing_history where hotel_id=? and billed_date BETWEEN '" +fromDate+ "' AND '" + toDate + "'";
			List<String> orderids = new ArrayList<String>();
			 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
			 for (Map<String, Object> map : orders) {
				 orderids.add((String) map.get("orderids"));
			 }
			 Map<String, Object> general_Report = new HashMap<>();
			 String sql2 = "select order_dish_name, sum(qty) as count from hotel_analyzer where orderids = ? group by order_dish_name order by order_dish_name";
			 for (String orderid : orderids) {
				 List<Map<String, Object>> dishnameCount = jdbcTemplate.queryForList(sql2, new Object[] { orderid });
				 for (Map<String, Object> map : dishnameCount) {
					 String dishname = (String) map.get("order_dish_name");
					 BigDecimal count = (BigDecimal) map.get("count");
					 if (general_Report.containsKey(dishname)) {
						 Long updateValue = (Long)general_Report.get(dishname) + count.longValue();
						 general_Report.put(dishname, updateValue);
					} else {
						general_Report.put(dishname, count.longValue());
					}
				}
			}
			 return general_Report;
		}

		public double getTotalEarningForReport(HotelReportVO hotelReportVO, String toDate, String fromDate) {
			int hotelid = Integer.parseInt(hotelReportVO.getHotelname());
			double bill_amount = 0;
			String sql = "select sum(bill_amount) as sum from billing_history where hotel_id=? and billed_date BETWEEN '" +fromDate+ "' AND '" + toDate + "'";
			List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid });
			 for (Map<String, Object> map : orders) {
				 if (map.get("sum") != null && !map.get("sum").equals("null")) {
					 bill_amount = ((Number)(map.get("sum"))).doubleValue();
				 }
			 }
			 if (bill_amount == 0) {
				return 0;
			} else {
				return bill_amount;
			}
		}
		
		public Map<String, Object> getCustomerCall(int hotel_id) {
			String sql = "select call_type,table_no from customer_call where cust_call_hotel_id = ? and status='applied'";
			Map<String, Object> output = new HashMap<String, Object>();
			List<Map<String, Object>> customercalls = jdbcTemplate.queryForList(sql, new Object[] { hotel_id });
			 for (Map<String, Object> map : customercalls) {
				 String calltype = (String)(map.get("call_type"));
				 String tableNo = (String)(map.get("table_no"));
				 output.put(calltype, tableNo);
			 }
			updateAllCalls(hotel_id);
			output.putAll(getOrderCount(hotel_id));
			//output.putAll(getBarOrderCount(hotel_id));
			return output;
		}
		/**
		 * Make it served after they make a call,so they wont appear.
		 * @param hotel_id
		 */
		private void updateAllCalls(int hotel_id) {
		 String sql1 = "update customer_call set status='served' where cust_call_hotel_id=?";
	        jdbcTemplate.update(sql1, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, hotel_id);
				}
			});
		}
		//select status, count(status) from order_details where hotel_id = 1 group by status;
		public Map<String, Long> getOrderCount(int hotel_id) {
			String sql = "select status, count(status) as statuscount from order_details where hotel_id = ? group by status";
			Map<String, Long> output = new HashMap<String, Long>();
			List<Map<String, Object>> customercalls = jdbcTemplate.queryForList(sql, new Object[] { hotel_id });
			 for (Map<String, Object> map : customercalls) {
				 String calltype = (String)(map.get("status"));
				 Long callCount = (Long)(map.get("statuscount"));
				 output.put(calltype, callCount);
			 }
			return output;
		}
		
		public Map<String, Long> getBarOrderCount(int hotel_id) {
			String sql = "select order_status as bar_order_status, count(order_status) as statuscount from bar_order_parent_table where hotel_id = ? group by order_status";
			Map<String, Long> output = new HashMap<String, Long>();
			List<Map<String, Object>> customercalls = jdbcTemplate.queryForList(sql, new Object[] { hotel_id });
			 for (Map<String, Object> map : customercalls) {
				 String calltype = (String)(map.get("barstatus"));
				 Long callCount = (Long)(map.get("statuscount"));
				 output.put(calltype, callCount);
			 }
			return output;
		}
		//batch scheduler 
		//SELECT * FROM users WHERE created >= CURDATE();
		public List<Integer> getExpiredHotelId() {
			String sql = "SELECT id FROM hotel_details WHERE hotel_end_date <= CURDATE() and hotel_enabled=true and hotel_expire=false";
			List<Integer> hotelidlist = new ArrayList<Integer>();
			List<Map<String, Object>> hotelids = jdbcTemplate.queryForList(sql);
			 for (Map<String, Object> map : hotelids) {
				 int hotelid = (int)(map.get("id"));
				 hotelidlist.add(hotelid);
			 }
			return hotelidlist;
		}
		
		public int[][] bulkStatusUpdate(List<Integer> hotelids) {
	        String sql = "update user_details set status=false where hotel_id=?";

	        return jdbcTemplate.batchUpdate(sql, hotelids, hotelids.size(),
	        		new ParameterizedPreparedStatementSetter<Integer>() {
						@Override
						public void setValues(PreparedStatement ps, Integer hotelAnalyze) throws SQLException {
							ps.setInt(1, hotelAnalyze);
						}
					});
		};
		
		public int[][] bulkHotelStatusUpdate(List<Integer> hotelids) {
	        String sql = "update hotel_details set hotel_expire=true where hotel_id=?";

	        return jdbcTemplate.batchUpdate(sql, hotelids, hotelids.size(),
	        		new ParameterizedPreparedStatementSetter<Integer>() {
						@Override
						public void setValues(PreparedStatement ps, Integer hotelAnalyze) throws SQLException {
							ps.setInt(1, hotelAnalyze);
						}
					});
		};
		
		//bulk menu save
		public int[][] bulkMenuUpload(List<BulkMenuUpload> bulkMenuUploads){
	        String sql = "insert into hotel_dishes (hotel, dish_name, dish_category,dish_description,dish_price,calories,discount,avg_cookingtime,hot,dish_ingredients,sequence) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        return jdbcTemplate.batchUpdate(sql, bulkMenuUploads, bulkMenuUploads.size(),
	        		new ParameterizedPreparedStatementSetter<BulkMenuUpload>() {
						@Override
						public void setValues(PreparedStatement ps, BulkMenuUpload bulkMenuUpload) throws SQLException {
							if (!"".equalsIgnoreCase(bulkMenuUpload.getHotel_id()) && bulkMenuUpload.getHotel_id() !=null) {
								ps.setInt(1, Integer.parseInt(bulkMenuUpload.getHotel_id()));
								ps.setString(2, bulkMenuUpload.getDish_name());
								ps.setString(3, bulkMenuUpload.getDish_category());
								ps.setString(4, bulkMenuUpload.getDish_description());
								ps.setInt(5, Integer.parseInt(bulkMenuUpload.getDish_price()));
								ps.setString(6, bulkMenuUpload.getCalories());
								ps.setString(7, bulkMenuUpload.getDiscount());
								ps.setString(8, bulkMenuUpload.getAvg_cooking_time());
								ps.setBoolean(9, bulkMenuUpload.getHot().equals("1") ? true : false);
								List<String> ingrediantsList = Arrays.asList(bulkMenuUpload.getIngedients().split("-"));
								ps.setString(10, ingrediantsList.toString());
								ps.setInt(11, bulkMenuUpload.getSeuence());
							}
						}
					});
		};
		
		//used android billing history
		public List<OrderHistoryDetailsVO> getBillingHistoryByCustomer(String customer_id) {
			String sql = "select b.order_ids,b.bar_order_ids,b.food_order_ids,b.order_summary,round(b.bill_amount,2) as bill_amount,round(IFNULL(b.bill_discount, 0),2) as bill_discount, round(IFNULL(b.bill_tax,0),2) as bill_tax, round(IFNULL(b.liquor_tax,0),2) as liqour_tax, h.hotel_name,DATE_FORMAT(b.billed_date, '%d-%m-%Y') billing_date from billing_history b,hotel_details h where customer_id=? and b.hotel_id = h.id  order by billed_date desc LIMIT 5";
			 List<Map<String, Object>> orderidlists = jdbcTemplate.queryForList(sql, new Object[] { customer_id });
			 List<OrderHistoryDetailsVO> listofAllOrderVOs = new ArrayList<OrderHistoryDetailsVO>();
			 try {
				 for (Map<String, Object> map : orderidlists) {
					 String orderid = (String)map.get("order_ids");
					 String ordersummaries = (String)map.get("order_summary");
					 List<Object> order = new ArrayList<Object>();
					 String orderSumm[] = ordersummaries.split("\\|");
					 for (String orderdetails :orderSumm) {
						 GeneralOrderVO genralOrderVo = new GeneralOrderVO();
						 String entity[] = orderdetails.split("\\,");
						 String dishname = entity[0].split("\\=")[1];
						 String qty = entity[1].split("\\=")[1];
						 genralOrderVo.setItemName(dishname);
						 genralOrderVo.setOrderqty(qty);
						 order.add(genralOrderVo);
					 }
					 double sum = (Double) map.get("bill_amount");
					 String hotelname = (String)map.get("hotel_name");
					 double discount = (Double) map.get("bill_discount");
					 double tax = (Double) map.get("bill_tax");
					 String barorderids = (String) map.get("bar_order_ids");
					 String foodorderids = (String) map.get("food_order_ids");
					 OrderHistoryDetailsVO allOrderVO = new OrderHistoryDetailsVO("", "", orderid, order, "", sum, hotelname,discount,tax,barorderids,foodorderids);
					 allOrderVO.setBilleddate((String)map.get("billing_date"));
					 allOrderVO.setLiqourtax((Double) map.get("liqour_tax"));
					 listofAllOrderVOs.add(allOrderVO);
				}
			 } catch(Exception e) {
				 return null;
			 }
			 
			 return listofAllOrderVOs;
		}
		
		//sales report
		public Map<String, LinkedHashMap<String, Object>> getSalesReport(HotelReportVO hotelReportVO, String toDate, String fromDate) throws ParseException {
			int hotelid = Integer.parseInt(hotelReportVO.getHotelname());
			//select distinct order_ids from billing_history where billed_date BETWEEN '2017-03-29' AND '2017-03-30 23:59:59'
			Map<String, Object> dailyOrderCounter = new LinkedHashMap<>(); 
			 Map<String, LinkedHashMap<String, Object>> general_Report = new LinkedHashMap<>();
			 Map<String, Object> dailyearning = new LinkedHashMap<>();
			 //String sql2 = "select GROUP_CONCAT(order_ids SEPARATOR ',') as id, CONCAT(day(billed_date), '/', month(billed_date), '/' , year(billed_date)) AS Month, sum(bill_amount) as totalamount from billing_history where hotel_id=? and billed_date BETWEEN '" +fromDate+ "' AND '" + toDate + "' group by Month";
			 String sql3 = "select GROUP_CONCAT(order_ids SEPARATOR ',') as id, DATE_FORMAT(billed_date, '%d-%m-%Y') AS billed_date_val, sum(bill_amount) as totalamount from billing_history where hotel_id=? and billed_date BETWEEN '" +fromDate+ "' AND '" + toDate + "' group by billed_date_val order by billed_date";
			 List<Map<String, Object>> dailyBillCounter = jdbcTemplate.queryForList(sql3, new Object[] { hotelid });
			 for (Map<String, Object> map : dailyBillCounter) {
				 String singleDate = (String) map.get("billed_date_val");
				 double totalamount = ((Number) map.get("totalamount")).doubleValue();
				 String orderids  = (String) map.get("id");
				 int orderCounter = 0;
				 if (orderids != null && !orderids.equals("")) {
					 orderCounter = orderids.split(",").length;
				}
				 dailyearning.put(singleDate, totalamount);
				 dailyOrderCounter.put(singleDate, orderCounter);
			}
			 general_Report.put("dailyOrderCounter",(LinkedHashMap<String, Object>) dailyOrderCounter);
			 general_Report.put("dailyearning",(LinkedHashMap<String, Object>) dailyearning);
			 return general_Report;
		}
		
		public List<Map<String, Object>> reportForSales(int id, String toDate, String fromDate) {
			String sql3 = "select GROUP_CONCAT(order_ids SEPARATOR ',') as id, DATE_FORMAT(billed_date, '%d-%m-%Y') AS billed_date_val, sum(bill_amount) as totalamount from billing_history where hotel_id=? and billed_date BETWEEN '" +fromDate+ "' AND '" + toDate + "' group by billed_date_val order by billed_date";
			 List<Map<String, Object>> dailyBillCounter = jdbcTemplate.queryForList(sql3, new Object[] { id });
			 return dailyBillCounter;
		}
		
		//getBillDetails based on customer id, table id, hotel id 05/08/2017-----------------------------
		public GenerateBillVO getBillDetailsForMobileBillApi(int hotelid, int tableid, String customerid) {
			String sql = "SELECT b1.customer_id,\r\n" + 
					"b1.table_id,\r\n" + 
					"GROUP_CONCAT(b1.orderids SEPARATOR ',') as orderids,\r\n" + 
					"GROUP_CONCAT(b1.foodordersummaries SEPARATOR '') as foodordersummaries,\r\n" + 
					"GROUP_CONCAT(b1.barordersummaries SEPARATOR '') as barordersummaries,\r\n" + 
					"sum(b1.sum) sum,\r\n" + 
					"GROUP_CONCAT(b1.foodorder SEPARATOR '') foodorder,\r\n" + 
					"GROUP_CONCAT(b1.barorder SEPARATOR '') barorder, \r\n" + 
					"sum(barordercount) barordercount,\r\n" + 
					"sum(foodbill) foodbill,\r\n" + 
					"sum(barbill) barbill\r\n" + 
					"FROM (SELECT customer_id,\r\n" + 
					"	table_id,GROUP_CONCAT(id SEPARATOR ',') as orderids,\r\n" + 
					"	GROUP_CONCAT(order_summary SEPARATOR '') as foodordersummaries, \r\n" + 
					"    null as barordersummaries,\r\n" + 
					"	sum(amount) as sum,GROUP_CONCAT(id SEPARATOR ',') as foodorder,\r\n" + 
					"    null  barorder,\r\n" + 
					"	null barordercount,\r\n" + 
					"	sum(amount) foodbill,\r\n" + 
					"	null barbill \r\n" + 
					"	FROM order_details\r\n" + 
					"	where hotel_id=? and status!='cancel_order' \r\n" + 
					"	and status!='billed' \r\n" + 
					"	and online_payment=false \r\n" + 
					"	and customer_id=? \r\n" + 
					"	and table_id=? \r\n" + 
					"	GROUP BY customer_id,table_id \r\n" + 
					"    union all SELECT al.customer_id customer_id, al.table_id table_id, al.id orderids,\r\n" + 
					"			null as placeholder,\r\n" + 
					"            GROUP_CONCAT(concat (al.itemname, '-', al.qty)SEPARATOR ',') barordersummaries,\r\n" + 
					"			sum(al.qty*al.price) sum,\r\n" + 
					"			null foodorder,al.id barorder,\r\n" + 
					"			count(al.itemname) barordercount,\r\n" + 
					"			null foodbill,sum(al.qty*al.price) barbill \r\n" + 
					"			FROM (\r\n" + 
					"				select bpt.customer_id customer_id,bpt.table_id table_id,bpt.id id,bi.bar_item_name itemname,\r\n" + 
					"				sum(bd.qty) qty,bd.price price \r\n" + 
					"				FROM bar_order_parent_table bpt INNER JOIN bar_order_details bd ON bpt.id = bd.parent_order_id \r\n" + 
					"                INNER JOIN bar_items bi ON bi.id = bd.bar_item_id where bpt.order_status !='billed' and bpt.order_status !='cancel_order' \r\n" + 
					"				and bpt.online_payment=false \r\n" + 
					"				and bpt.hotel_id=? and bpt.customer_id=? and bpt.table_id=?\r\n" + 
					"				group by bi.bar_item_name, bd.price,\r\n" + 
					"				bpt.customer_id, bpt.table_id, bpt.id) al \r\n" + 
					"				group by al.customer_id, \r\n" + 
					"				al.table_id, al.id) b1 \r\n" + 
					"				group by b1.customer_id, b1.table_id";
			 List<GenerateBillVO> listObject = new ArrayList<GenerateBillVO>();
			 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid,customerid,tableid, hotelid,Long.parseLong(customerid),tableid });
			 GenerateBillVO allOrderVO = new GenerateBillVO();
			 for (Map<String, Object> map : orders) {
				allOrderVO.setCustomerid((String) (map.get("customer_id")));
				allOrderVO.setTableid(String.valueOf((Integer)(map.get("table_id"))));
				allOrderVO.setOrderids((String) map.get("orderids"));
				String foodordersummaries = (String) map.get("foodordersummaries");
				String barordersummaries = (String) map.get("barordersummaries");
				allOrderVO.setFoodordersummaries(foodordersummaries);
				allOrderVO.setBarordersummaries(barordersummaries);
				allOrderVO.setFormatOrderSummary(RestonzaUtility.formatOrderSummariesForCombineBill(foodordersummaries));
				allOrderVO.setFormatBarOrderSummary(barordersummaries);
				Double sum = (Double)map.get("sum");
				allOrderVO.setSum(sum);
				allOrderVO.setFood_order_ids((String)map.get("foodorder"));
				allOrderVO.setBar_order_ids((String)map.get("barorder"));
				allOrderVO.setBarordercount(String.valueOf((BigDecimal)map.get("barordercount")));
				allOrderVO.setFoodbillamt(String.valueOf((Double)map.get("foodbill")));
				allOrderVO.setBarbillamt(String.valueOf((Double)map.get("barbill")));
				listObject.add(allOrderVO);
			}
			 return allOrderVO;
		}
		
		//getDishByHotelIdin HashMap<DishName, price>---------------------------------------------------------------------
				
		public GenerateBillVO getBillDetailsForMobileBillApiHotelWise(int hotelid, int tableid, String customerid) {
			String sql = "SELECT b1.customer_id,b1.table_id,GROUP_CONCAT(b1.orderids SEPARATOR ',') as orderids,GROUP_CONCAT(b1.ordersummaries SEPARATOR '') as ordersummaries,sum(b1.sum) sum,GROUP_CONCAT(b1.foodorder SEPARATOR '') foodorder,GROUP_CONCAT(b1.barorder SEPARATOR '') barorder, sum(barordercount) barordercount,sum(foodbill) foodbill,sum(barbill) barbill FROM (SELECT customer_id,table_id,GROUP_CONCAT(id SEPARATOR ',') as orderids, GROUP_CONCAT(order_summary SEPARATOR '') as ordersummaries, 	sum(amount) as sum,GROUP_CONCAT(id SEPARATOR ',') as foodorder,null  barorder, null barordercount,sum(amount) foodbill,null barbill FROM order_details where hotel_id=? and status!='cancel_order' and status!='billed'  and customer_id=? GROUP BY customer_id,table_id union all SELECT al.customer_id customer_id, al.table_id table_id, al.id orderids, GROUP_CONCAT(concat (al.itemname, '-', al.qty)SEPARATOR ',') ordersummaries,sum(al.qty*al.price) sum,null foodorder,al.id barorder, count(al.itemname) barordercount,null foodbill,sum(al.qty*al.price) barbill FROM (select bpt.customer_id customer_id,bpt.table_id table_id,bpt.id id,bi.bar_item_name itemname,sum(bd.qty) qty,bd.price price FROM bar_order_parent_table bpt INNER JOIN bar_order_details bd ON bpt.id = bd.parent_order_id INNER JOIN bar_items bi ON bi.id = bd.bar_item_id where bpt.order_status !='billed' and bpt.order_status !='cancel_order' and bpt.hotel_id=? and bpt.customer_id=?  group by bi.bar_item_name, bd.price, bpt.customer_id, bpt.table_id, bpt.id) al group by al.customer_id, al.table_id, al.id) b1 group by b1.customer_id, b1.table_id";
			 List<GenerateBillVO> listObject = new ArrayList<GenerateBillVO>();
			 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid,customerid, hotelid,Integer.parseInt(customerid) });
			 GenerateBillVO allOrderVO = new GenerateBillVO();
			 for (Map<String, Object> map : orders) {
				allOrderVO.setCustomerid((String) (map.get("customer_id")));
				allOrderVO.setTableid(String.valueOf((Integer)(map.get("table_id"))));
				allOrderVO.setOrderids((String) map.get("orderids"));
				String ordersummaries = (String) map.get("ordersummaries");
				allOrderVO.setOrdersummaries(ordersummaries);
				allOrderVO.setFormatOrderSummary(RestonzaUtility.formatOrderSummariesForCombineBill(ordersummaries));
				Double sum = (Double)map.get("sum");
				allOrderVO.setSum(sum);
				allOrderVO.setFood_order_ids((String)map.get("foodorder"));
				allOrderVO.setBar_order_ids((String)map.get("barorder"));
				allOrderVO.setBarordercount(String.valueOf((BigDecimal)map.get("barordercount")));
				allOrderVO.setFoodbillamt(String.valueOf((Double)map.get("foodbill")));
				allOrderVO.setBarbillamt(String.valueOf((Double)map.get("barbill")));
				listObject.add(allOrderVO);
			}
			/*StringBuilder sqlQueryBuilder = new StringBuilder();
			sqlQueryBuilder.append("SELECT customer_id,table_id,GROUP_CONCAT(id SEPARATOR ',') as orderids, ");
			sqlQueryBuilder.append("GROUP_CONCAT(order_summary SEPARATOR '') as ordersummaries, sum(amount)");
			sqlQueryBuilder.append("as sum FROM order_details where hotel_id=? ");
			sqlQueryBuilder.append(" and status!='cancel_order' and status!='billed' ");
			sqlQueryBuilder.append(" and table_id=");
			sqlQueryBuilder.append(tableid);
			sqlQueryBuilder.append(" and customer_id='");
			sqlQueryBuilder.append(customerid);
			sqlQueryBuilder.append("' GROUP BY customer_id,table_id");
			AllOrderVO allOrderVO = null;
			 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sqlQueryBuilder.toString(), new Object[] { hotelid });
			 for (Map<String, Object> map : orders) {
				allOrderVO = new AllOrderVO();
				allOrderVO.setCustomerid((String) (map.get("customer_id")));
				allOrderVO.setTableid(String.valueOf((Integer)(map.get("table_id"))));
				allOrderVO.setOrderids((String) map.get("orderids"));
				String ordersummaries = (String) map.get("ordersummaries");
				allOrderVO.setOrdersummaries(ordersummaries);
				allOrderVO.setFormatOrderSummary(RestonzaUtility.formatOrderSummaries(ordersummaries));
				BigDecimal sum = (BigDecimal)map.get("sum");
				allOrderVO.setSum(sum.intValue());
			}*/
			 return allOrderVO;
		}
		
public HashMap<String, Double> getDishAndPriceMapping(int hotelId) {
			String sql = "select dish_name, dish_price from hotel_dishes where hotel = ? union select bar_item_name dish_name, price dish_price FROM bar_items where hotel_id=?";
			HashMap<String, Double> dishDetails = new HashMap<>();
			List<Map<String, Object>> dishDetailsMap = jdbcTemplate.queryForList(sql, new Object[] { hotelId, hotelId });
			 for (Map<String, Object> map : dishDetailsMap) {
				 String dishname = (String)map.get("dish_name");
				 Double price = (Double)map.get("dish_price");
				 dishDetails.put(dishname, price);
			}
			return dishDetails;
		}


		public HashMap<String, String> getDishAndImgMapping(int hotelId) {
			String sql = "select dish_name, img_uri image from hotel_dishes where hotel = ? union select bar_item_name dish_name, barimg image FROM bar_items where hotel_id=?";
			HashMap<String, String> dishDetails = new HashMap<>();
			List<Map<String, Object>> dishDetailsMap = jdbcTemplate.queryForList(sql, new Object[] { hotelId, hotelId });
			 for (Map<String, Object> map : dishDetailsMap) {
				 String dishname = (String)map.get("dish_name");
				 String price = (String)map.get("image");
				 dishDetails.put(dishname, price);
			}
			return dishDetails;
		}
		
		/**
		 * 
		 */
		public List<HotelDishMenuPdf> genrateMenu(int hotelId) {
			String sql="select hotel_name,dish_name,dish_category,dish_price from hotel_dishes Inner JOIN hotel_details ON hotel_dishes.hotel = hotel_details.id where hotel_details.id = ? order by dish_category";
			HashMap<String, Integer> dishMenu = new HashMap<>();
			List<Map<String, Object>> dishDetailsMap = jdbcTemplate.queryForList(sql, new Object[] { hotelId });
			List<HotelDishMenuPdf> list = new ArrayList<HotelDishMenuPdf>();
			for (Map<String, Object> map : dishDetailsMap) {
				HotelDishMenuPdf pdfMenu = new HotelDishMenuPdf();
				pdfMenu.setDish_name((String)map.get("dish_name"));
				pdfMenu.setDish_category((String)map.get("dish_category"));
				pdfMenu.setDish_price((Double)map.get("dish_price"));
				pdfMenu.setHotel_name((String)map.get("hotel_name"));
				list.add(pdfMenu);
			}
			return list;
		}
		
		/**
		 * Bulk Save Bar Menu
		 */
		public int[][] bulkBarMenuUploadSave(List<BarMenuBulkUploadVO> barMenuBulkUploadVOs) {
			int count  = barItemsRepository.count(barMenuBulkUploadVOs.get(0).getHotel_id());
			for (BarMenuBulkUploadVO vo: barMenuBulkUploadVOs) {
				vo.setSequence(count+1);
				count++;
			}
	        String sql = "insert into bar_items (bar_item_name,category_id,sub_category,servered_in_qty,hotel_id,qty,price,status,barimg,sequence) values (?, ?, ?, ?, ?, ?, ? ,?,?,?)";
	        try {
	        	 return jdbcTemplate.batchUpdate(sql, barMenuBulkUploadVOs, barMenuBulkUploadVOs.size(),
	 	        		new ParameterizedPreparedStatementSetter<BarMenuBulkUploadVO>() {
	 						@Override
	 						public void setValues(PreparedStatement ps, BarMenuBulkUploadVO barMenuBulkUploadVO) throws SQLException {
	 							ps.setString(1, barMenuBulkUploadVO.getBar_item_name());
	 							ps.setInt(2, barMenuBulkUploadVO.getCategory_id());
	 							ps.setString(3, barMenuBulkUploadVO.getSub_category());
	 							ps.setBoolean(4, barMenuBulkUploadVO.isServered_in_qty());
	 							ps.setInt(5, barMenuBulkUploadVO.getHotel_id());
	 							ps.setString(6, barMenuBulkUploadVO.getQty());
	 							ps.setDouble(7, barMenuBulkUploadVO.getPrice());
	 							ps.setBoolean(8, barMenuBulkUploadVO.isStatus());
	 							ps.setString(9, barMenuBulkUploadVO.getBarimg());
	 							ps.setInt(10, barMenuBulkUploadVO.getSequence());
	 						}
	 					});
	        } catch (Exception e) {
	        	return null;
	        }
		};
		
		public int addbarMenu(List<BarMenuBulkUploadVO> list) {
			BarMenuBulkUploadVO barMenuBulkUploadVO = list.get(0);
	        String sql = "insert into bar_items (bar_item_name,category_id,sub_category,servered_in_qty,hotel_id,qty,price,status) values (?, ?, ?, ?, ?, ?, ? ,?)";
	        	KeyHolder holder = new GeneratedKeyHolder();
	        	  jdbcTemplate.update(new PreparedStatementCreator() {           
	                 @Override
	                 public PreparedStatement createPreparedStatement(Connection connection)
	                         throws SQLException {
	                     PreparedStatement ps = connection.prepareStatement(sql.toString(),
	                         Statement.RETURN_GENERATED_KEYS); 
	                     	ps.setString(1, barMenuBulkUploadVO.getBar_item_name());
							ps.setInt(2, barMenuBulkUploadVO.getCategory_id());
							ps.setString(3, barMenuBulkUploadVO.getSub_category());
							ps.setBoolean(4, barMenuBulkUploadVO.isServered_in_qty());
							ps.setInt(5, barMenuBulkUploadVO.getHotel_id());
							ps.setString(6, barMenuBulkUploadVO.getQty());
							ps.setDouble(7, barMenuBulkUploadVO.getPrice());
							ps.setBoolean(8, barMenuBulkUploadVO.isStatus());
	                     return ps;
	                 }
	             }, holder);
	        	return holder.getKey().intValue();
		}
		
		public List<Map<String, Object>> getBarItems(int hotel_id) {
			String sql = "select bi.id, bi.bar_item_name,bi.sequence, bc.bar_category_name, bi.sub_category, bi.price,bi.qty, bi.status, bi.barimg from bar_items bi INNER JOIN bar_category bc on bi.category_id = bc.id where bi.hotel_id = ? order by bi.sequence";
			List<Map<String, Object>> barItemsDetails = jdbcTemplate.queryForList(sql, new Object[] { hotel_id });
			return barItemsDetails;
		}
		
		public List<Map<String, Object>> getBarCategoryItems(int hotel_id,int categoryId) {
			String sql = "select bi.id, bi.bar_item_name, bi.sub_category, bi.price,bi.qty, bi.status, bi.barimg from bar_items bi where bi.hotel_id = ? and bi.category_id =?";
			List<Map<String, Object>> barItemsDetails = jdbcTemplate.queryForList(sql, new Object[] { hotel_id,categoryId });
			return barItemsDetails;
		}
		
		public List<GenerateBillVO> getCombineBillOrder(int hotelid) {
			 String sql = "SELECT b1.customer_id,b1.table_id, GROUP_CONCAT(b1.orderids SEPARATOR ',') as orderids, GROUP_CONCAT(b1.ordersummaries SEPARATOR '') as ordersummaries, sum(b1.sum) sum, GROUP_CONCAT(b1.foodorder SEPARATOR '') foodorder, GROUP_CONCAT(b1.barorder SEPARATOR '') barorder, sum(barordercount) barordercount, sum(foodbill) foodbill, sum(barbill) barbill , b1.online_payment FROM (SELECT customer_id, table_id, GROUP_CONCAT(id SEPARATOR ',') as orderids, GROUP_CONCAT(order_summary SEPARATOR '') as ordersummaries, sum(amount) as sum,GROUP_CONCAT(id SEPARATOR ',') as foodorder,  null barorder, null barordercount, sum(amount) foodbill,  null barbill, online_payment FROM order_details where hotel_id=? and status!='cancel_order' and status!='billed' GROUP BY customer_id,table_id,online_payment union select base_data.customer_id customer_id, base_data.table_id table_id, GROUP_CONCAT(base_data.orderids) orderids, GROUP_CONCAT(base_data.ordersummaries) ordersummaries, sum(base_data.sum) sum, null foodorder, GROUP_CONCAT(base_data.barorder) barorder, sum(base_data.barordercount) barordercount, null foodbill, sum(base_data.barbill) barbill, base_data.online_payment online_payment FROM (SELECT al.customer_id customer_id, al.table_id table_id,  al.id orderids, GROUP_CONCAT(concat (al.itemname, '-', al.qty)SEPARATOR ',') ordersummaries, sum(al.qty*al.price) sum,null foodorder,al.id barorder, count(al.itemname) barordercount,  null foodbill, sum(al.qty*al.price) barbill,al.online_payment as online_payment FROM (select bpt.customer_id customer_id,  bpt.table_id table_id,bpt.id id,bi.bar_item_name itemname, sum(bd.qty) qty,bd.price price,bpt.online_payment FROM bar_order_parent_table bpt INNER JOIN bar_order_details bd ON bpt.id = bd.parent_order_id INNER JOIN bar_items bi ON bi.id = bd.bar_item_id where bpt.order_status !='billed' and bpt.order_status !='cancel_order' and bpt.hotel_id=? group by bi.bar_item_name, bd.price, bpt.customer_id, bpt.table_id, bpt.id) al group by al.customer_id, al.table_id, al.id, al.online_payment)base_data group by base_data.customer_id, base_data.table_id, base_data.online_payment) b1 group by b1.customer_id, b1.table_id,b1.online_payment";
			 List<GenerateBillVO> listObject = new ArrayList<GenerateBillVO>();
			 List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, new Object[] { hotelid, hotelid });
			 for (Map<String, Object> map : orders) {
				GenerateBillVO allOrderVO = new GenerateBillVO();
				allOrderVO.setCustomerid((String) (map.get("customer_id")));
				allOrderVO.setTableid(String.valueOf((Integer)(map.get("table_id"))));
				allOrderVO.setOrderids((String) map.get("orderids"));
				String ordersummaries = (String) map.get("ordersummaries");
				allOrderVO.setOrdersummaries(ordersummaries);
				allOrderVO.setFormatOrderSummary(RestonzaUtility.formatOrderSummariesForCombineBill(ordersummaries));
				Double sum = (Double)map.get("sum");
				allOrderVO.setSum(sum);
				allOrderVO.setFood_order_ids((String)map.get("foodorder"));
				allOrderVO.setBar_order_ids((String)map.get("barorder"));
				allOrderVO.setBarordercount(String.valueOf((BigDecimal)map.get("barordercount")));
				allOrderVO.setFoodbillamt(String.valueOf((Double)map.get("foodbill")));
				allOrderVO.setBarbillamt(String.valueOf((Double)map.get("barbill")));
				allOrderVO.setOnlinePayment(String.valueOf((Integer)map.get("online_payment")));
				listObject.add(allOrderVO);
			}
			 return listObject;
		}
		
		public List<Map<String, Object>> getHotelDishesforCombineBill(int hotel_id) {
			String sql = "select a1.dish_name, a1.price FROM (select hd.dish_name dish_name, hd.dish_price price from hotel_dishes hd where hd.hotel = ? union all select bi.bar_item_name dish_name, bi.price price from bar_items bi where bi.hotel_id = ?) a1";
			List<Map<String, Object>> barItemsDetails = jdbcTemplate.queryForList(sql, new Object[] { hotel_id,hotel_id });
			return barItemsDetails;
		}
		
		/**
		 * Update Bulk sequncing for Dishes
		 */
		public int[][] bulkUpdateForSequnceDishes(List<Dishes> dishList) {
	        String sql = "update hotel_dishes set sequence=? where dish_id=?";
	        return jdbcTemplate.batchUpdate(sql, dishList, dishList.size(),
	        		new ParameterizedPreparedStatementSetter<Dishes>() {
						@Override
						public void setValues(PreparedStatement ps, Dishes dishes) throws SQLException {
							ps.setInt(1, dishes.getSequence());
							ps.setInt(2, dishes.getDish_id());
						}
					});
		};
		
		/**
		 * Update Bulk sequncing for Categories
		 */
		public int[][] bulkUpdateForSequnceCategories(List<DishCategories> catList) {
	        String sql = "update dish_categories set sequence=? where id=?";
	        return jdbcTemplate.batchUpdate(sql, catList, catList.size(),
	        		new ParameterizedPreparedStatementSetter<DishCategories>() {
						@Override
						public void setValues(PreparedStatement ps, DishCategories dishes) throws SQLException {
							ps.setInt(1, dishes.getSequence());
							ps.setInt(2, dishes.getId());
						}
					});
		};
		
		
		/**
		 * Update Bulk sequncing for Bar Categories
		 */
		public int[][] bulkUpdateForSequnceBarCategories(List<BarCategory> catList) {
	        String sql = "update bar_category set sequence=? where id=?";
	        return jdbcTemplate.batchUpdate(sql, catList, catList.size(),
	        		new ParameterizedPreparedStatementSetter<BarCategory>() {
						@Override
						public void setValues(PreparedStatement ps, BarCategory dishes) throws SQLException {
							ps.setInt(1, dishes.getSequence());
							ps.setInt(2, dishes.getId());
						}
					});
		};
		
		/**
		 * Update Bulk sequncing for Bar Categories
		 */
		public int[][] bulkUpdateForSequnceBarItems(List<BarItems> catList) {
	        String sql = "update bar_items set sequence=? where id=?";
	        return jdbcTemplate.batchUpdate(sql, catList, catList.size(),
	        		new ParameterizedPreparedStatementSetter<BarItems>() {
						@Override
						public void setValues(PreparedStatement ps, BarItems dishes) throws SQLException {
							ps.setInt(1, dishes.getSequence());
							ps.setInt(2, dishes.getId());
						}
					});
		};
		
		/**
		 * Get Hotel Menu in Pdf 
		 * @param hotelReportVO
		 * @param toDate
		 * @param fromDate
		 * @return
		 * @throws ParseException
		 */
		public List<Map<String, Object>> getHotelMenuForPdf(String hotelId, String type) throws ParseException {
			int hotelid = Integer.parseInt(hotelId);
			String sql3 = "";
			List<Map<String, Object>> foodMenuItemsDetails = null;
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			if (type.equalsIgnoreCase("bar")) {
				 sql3 = "select distinct(select bar_category_name from bar_category bc where bc.hotel_id=? and bc.id=category_id) as dish_category,\r\n" + 
					 		"bar_item_name as dish_name,\r\n" + 
					 		"price as dish_price\r\n" + 
					 		"from bar_items bi where bi.hotel_id=? order by dish_category";
				 foodMenuItemsDetails = jdbcTemplate.queryForList(sql3, new Object[] { hotelid, hotelid });
			} else {
				sql3 = "select distinct(dish_category),dish_name,dish_price from hotel_dishes where hotel=? order by dish_category";
				foodMenuItemsDetails = jdbcTemplate.queryForList(sql3, new Object[] { hotelid });
			}
			 
			 
			 
			 mapList.addAll(foodMenuItemsDetails);
			 return mapList;
		}
		
		/**
		 * Update the image URI path in db for Bar and Dish
		 * @param id
		 * @param path
		 * @param type
		 */
		public void updateCategoryPicture(int id,String path,String type) {
			String updateAddressStmt = "";
			if (type.equalsIgnoreCase("dish")){
				 updateAddressStmt = "update dish_categories set img_uri=? where id=?";
			} else if(type.equalsIgnoreCase("bar")) {
				 updateAddressStmt = "update bar_category set img_uri=? where id=?";
			} else if(type.equalsIgnoreCase("dishimg")) {
				updateAddressStmt = "update hotel_dishes set img_uri=? where dish_id=?";
			} else if(type.equalsIgnoreCase("barimg")) {
				updateAddressStmt = "update bar_items set barimg=? where id=?";
			}
			jdbcTemplate.update(updateAddressStmt, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement prepStmt) throws SQLException {
					prepStmt.setString(1, path);
					prepStmt.setLong(2, id);
				}
			});
		}
		
		public String getDeviceIdBasedOnOrderId(Integer orderId) {
			try {
			 String customer_id = "select od.customer_id FROM order_details od where od.id = ?";
			 String customerId = jdbcTemplate.queryForObject(customer_id, new Object[] { orderId }, String.class);
			 String device_id = "select c.deviceid FROM customer c where c.customer_id = ?";
			 String deviceId = jdbcTemplate.queryForObject(device_id, new Object[] { customerId }, String.class);
			 return deviceId;
			} catch (Exception e) {
				return null;
			}
		}
		
		public String getDeviceIdBasedOnBarOrderId(Integer orderId) {
			try {
			 String customer_id = "select bopt.customer_id FROM bar_order_parent_table bopt where bopt.id = ?";
			 Integer customerId = jdbcTemplate.queryForObject(customer_id, new Object[] { orderId }, Integer.class);
			 String device_id = "select c.deviceid FROM customer c where c.customer_id = ?";
			 String deviceId = jdbcTemplate.queryForObject(device_id, new Object[] { String.valueOf(customerId) }, String.class);
			 return deviceId;
			} catch (Exception e) {
				return null;
			}
		}
		
		public void markOnlinePaymentStatus(String hotelId,String tableId,String customerid,String foodorderids,String barorderids) {
			// Update order details table using order_details
			String sql = "update order_details set online_payment=true where table_id=? and hotel_id=? and customer_id=? and status !='cancel_order'";
			jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1 ,Integer.parseInt(tableId));
					ps.setInt(2, Integer.parseInt(hotelId));
					ps.setString(3, customerid);
				}
			});
			
		if (barorderids !=null && !barorderids.isEmpty()) {
			// Update order details table using order_details
			String sql1 = "update bar_order_details set online_payment=true where parent_order_id=?";
			jdbcTemplate.update(sql1, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1 ,Integer.parseInt(barorderids));
				}
			});
		}
		
		if (barorderids !=null && !barorderids.isEmpty()) {
			// Update order details table using order_details
			String sql1 = "update bar_order_parent_table set online_payment=true where table_id=? and hotel_id=? and customer_id=?";
			jdbcTemplate.update(sql1, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1 ,Integer.parseInt(tableId));
					ps.setInt(2, Integer.parseInt(hotelId));
					ps.setString(3, customerid);
				}
			});
		}
		}
		
		public List<Map<String,Object>> getPaymentByHotel(String fromDate, String toDate) {
			try {
			 String paymentSql = "select (select hotel_name from hotel_details where id=od.hotel_id) hotelname, sum(od.amount) as amount from order_details od where online_payment='1' and ordered_on between ? and ?  group by hotel_id;";
			 List<Map<String,Object>> list = jdbcTemplate.queryForList(paymentSql, new Object[] { fromDate,toDate });
			 return list;
			} catch (Exception e) {
				return null;
			}
		}
		
		public List<GeneralOrderVO> getFoodOrderDetailsByOrderId(String orderids) {
			String sql = "select order_summary from order_details where id IN ("+orderids+")";
			 List<Map<String, Object>> orderidlists = jdbcTemplate.queryForList(sql);
			 List<GeneralOrderVO> order =  new ArrayList<GeneralOrderVO>();
			 try {
				
				 for (Map<String, Object> map : orderidlists) {
					 String ordersummaries = (String)map.get("order_summary");
					 String orderSumm[] = ordersummaries.split("\\|");
					 for (String orderdetails :orderSumm) {
						 GeneralOrderVO genralOrderVo = new GeneralOrderVO();
						 String entity[] = orderdetails.split("\\,");
						 String dishname = entity[0].split("\\=")[1];
						 String qty = entity[1].split("\\=")[1];
						 genralOrderVo.setItemName(dishname);
						 genralOrderVo.setOrderqty(qty);
						 order.add(genralOrderVo);
					 }
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			 return order;  
		}
		
		public void insertBackKitchenOrderDetails(OrderDetailsVO orderDetailsVO, Integer id) {
			String sql = "insert into backkitchen_order_details (food_order_id, dish_id, qty,status,ordered_on,updated_on) values (?, ?, ?, ?, ?, ?)";
	        jdbcTemplate.batchUpdate(sql, orderDetailsVO.getOrderDetails(), orderDetailsVO.getOrderDetails().size(),
	        		new ParameterizedPreparedStatementSetter<PlacedOrderDetailsVO>() {
						@Override
						public void setValues(PreparedStatement ps, PlacedOrderDetailsVO placedOrderDetailsVO) throws SQLException {
							ps.setLong(1, id);
							ps.setLong(2, placedOrderDetailsVO.getId());
							ps.setLong(3, Long.parseLong(placedOrderDetailsVO.getQty()));
							ps.setString(4, RESTONZACONSTANTS.new_order.toString());
							ps.setTimestamp(5, RestonzaUtility.getSystime());
							ps.setTimestamp(6, RestonzaUtility.getSystime());
					}
			});
		}
		/**
		 * To delete the order if its cancelled by hotel Admin.
		 */
		public void deleteBackKitchenOrderDetails(int foodOrderId) {
			String deleteQuery = "delete from backkitchen_order_details where food_order_id = "+foodOrderId;
			jdbcTemplate.update(deleteQuery);
		}
		
		
		public List<Map<String, Object>> getBackKitchenPendingOrderDetails(Integer hotelId, String orderStatus) {
			String getBackKitchenPendingOrderDetailsSql=  "select od.id orderid, group_concat(concat(hd.dish_name, '-', bod.qty)) orderdetails, group_concat(concat(bod.dish_id, '-', hd.dish_name, '-', bod.qty)) order_details, hd.dish_category dishcategory, od.customer_id customer_id, od.table_id table_id, od.instruction instruction\r\n" + 
					"from backkitchen_order_details bod \r\n" + 
					"inner join order_details od on bod.food_order_id = od.id\r\n" + 
					"left outer join hotel_dishes hd on bod.dish_id = hd.dish_id\r\n" + 
					"where od.status = ? and bod.status = 'new_order' and od.hotel_id = ?\r\n" + 
					"group by od.id, hd.dish_category, od.customer_id, od.table_id";
			return jdbcTemplate.queryForList(getBackKitchenPendingOrderDetailsSql, new Object[] {orderStatus, hotelId});
			
		}
		
		public void updateBackKitchenOrder(OrderDetailsVO orderDetailsVO) {
			int dish_id = Integer.parseInt(orderDetailsVO.getOrder_id());
			int order_id = Integer.parseInt(orderDetailsVO.getHotel_id());
			String backKitchenOrderSql = "update backkitchen_order_details set status=? where food_order_id=? and dish_id=?";
			jdbcTemplate.update(backKitchenOrderSql, new Object[] {RESTONZACONSTANTS.ready_order.toString(), order_id, dish_id});
			String getCountSql= "select count(*) from backkitchen_order_details bod where bod.food_order_id = ? and bod.status ='new_order'";
			Integer orderPendingCount = jdbcTemplate.queryForObject(getCountSql, new Object[] {order_id}, Integer.class);
			if (orderPendingCount < 1) {
				String updateMainOrderSql = "update order_details set status =? where id=?";
				jdbcTemplate.update(updateMainOrderSql, new Object[] {RESTONZACONSTANTS.ready_order.toString(), order_id});
			}
		}
		
		public int checkTableValid(int hoteId, int tableid) {
			String sql = "select id from table_details where table_id='"+tableid+"' and hotel_id='"+hoteId+"' and status=true";
			int id = 0;
			try {
				id = jdbcTemplate.queryForObject(sql, Integer.class);
			} catch(EmptyResultDataAccessException e) {
				return 0;
			}
			return id;
		}
		
		public void updatePeopleSttingOnTable(int numberOfPeopleSitting ,String hotelId, String tableId) {
			String sql  = "update table_details set people_sitting_currently = ? where hotel_id=? and table_id=? ";
			jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, numberOfPeopleSitting);
					ps.setInt(2, Integer.parseInt(hotelId));
					ps.setInt(3, Integer.parseInt(tableId));
				}
			});
		}
}
