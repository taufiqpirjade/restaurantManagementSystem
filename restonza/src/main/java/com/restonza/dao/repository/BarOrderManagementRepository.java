/**
 * 
 */
package com.restonza.dao.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.restonza.util.service.RESTONZACONSTANTS;
import com.restonza.util.service.RestonzaCustomException;
import com.restonza.util.service.RestonzaCustomTransactional;
import com.restonza.vo.BarOrderManagementVO;

/**
 * @author flex-grow developers
 *
 */
@Repository
public class BarOrderManagementRepository {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private static Long parentKey;
	@PostConstruct
    private void postConstruct() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        
    }
	
	@RestonzaCustomTransactional
	public int[][] addBarOrderWeb(BarOrderManagementVO barOrderManagementVO, HashMap<String, String> barOrderMap, boolean orderExists, Long parent_id) throws Exception {
		 parentKey = parent_id;
		if (!orderExists) {
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
			    @Override
			    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			        PreparedStatement statement = con.prepareStatement("insert into bar_order_parent_table (table_id,hotel_id,customer_id,order_status,instruction) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			        statement.setInt(1, barOrderManagementVO.getTable_id());
			        statement.setInt(2, barOrderManagementVO.getHotel_id());
			        statement.setLong(3, barOrderManagementVO.getCustomer_id());
			        statement.setString(4, RESTONZACONSTANTS.new_order.toString());
			        statement.setString(5, barOrderManagementVO.getInstruction());
			        return statement;
			    }
			}, holder);
			parentKey = holder.getKey().longValue();
		}
		 // get the primary key from the parent table
		//insert into child table: user_ref_contact_details
		Set<String> barItemNames = barOrderMap.keySet();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("baritemsnames", barItemNames);
		String selectstmt = "select bi.id, bi.bar_item_name, bi.price from bar_items bi where bi.bar_item_name in (:baritemsnames)";
		List<Map<String, Object>> barItemsDetails = namedParameterJdbcTemplate.queryForList(selectstmt,parameters);
		String stmt = "insert into bar_order_details (parent_order_id,bar_item_id,qty,price,order_status) values(?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(stmt, barItemsDetails, barItemsDetails.size(),
        		new ParameterizedPreparedStatementSetter<Map<String, Object>>() {
					@Override
					public void setValues(PreparedStatement ps, Map<String, Object> placedBarOrderDetail) throws SQLException {
						String baritemname = (String)placedBarOrderDetail.get("bar_item_name");
						ps.setLong(1, parentKey);
						ps.setInt(2, (int)placedBarOrderDetail.get("id"));
						ps.setInt(3, Integer.parseInt(barOrderMap.get(baritemname)));
						ps.setDouble(4, (double)placedBarOrderDetail.get("price"));
						ps.setString(5, RESTONZACONSTANTS.new_order.toString());
					}
				});
	}
	
	
	@RestonzaCustomTransactional
	public int[][] addBarOrder(BarOrderManagementVO barOrderManagementVO, HashMap<String, String> barOrderMap, boolean orderExists, Long parent_id) throws Exception {
		 parentKey = parent_id;
		if (!orderExists) {
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
			    @Override
			    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
			        PreparedStatement statement = con.prepareStatement("insert into bar_order_parent_table (table_id,hotel_id,customer_id,order_status,instruction) values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			        statement.setInt(1, barOrderManagementVO.getTable_id());
			        statement.setInt(2, barOrderManagementVO.getHotel_id());
			        statement.setLong(3, barOrderManagementVO.getCustomer_id());
			        statement.setString(4, RESTONZACONSTANTS.new_order.toString());
			        statement.setString(5, barOrderManagementVO.getInstruction());
			        return statement;
			    }
			}, holder);
			parentKey = holder.getKey().longValue();
		}
		 // get the primary key from the parent table
		//insert into child table: user_ref_contact_details
		Set<String> barItemNames = barOrderMap.keySet();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("baritemsnames", barItemNames);
		parameters.addValue("hotelid", barOrderManagementVO.getHotel_id());
		String selectstmt = "select bi.id, bi.bar_item_name, bi.price from bar_items bi where bi.bar_item_name in (:baritemsnames) and hotel_id=:hotelid";
		List<Map<String, Object>> barItemsDetails = namedParameterJdbcTemplate.queryForList(selectstmt,parameters);
		String stmt = "insert into bar_order_details (parent_order_id,bar_item_id,qty,price,order_status) values(?,?,?,?,?)";
		return jdbcTemplate.batchUpdate(stmt, barItemsDetails, barItemsDetails.size(),
        		new ParameterizedPreparedStatementSetter<Map<String, Object>>() {
					@Override
					public void setValues(PreparedStatement ps, Map<String, Object> placedBarOrderDetail) throws SQLException {
						String baritemname = (String)placedBarOrderDetail.get("bar_item_name");
						ps.setLong(1, parentKey);
						ps.setInt(2, (int)placedBarOrderDetail.get("id"));
						ps.setInt(3, Integer.parseInt(barOrderMap.get(baritemname)));
						ps.setDouble(4, (double)placedBarOrderDetail.get("price"));
						ps.setString(5, RESTONZACONSTANTS.new_order.toString());
					}
				});
	}
	
	public Long isOrderRunning(BarOrderManagementVO barOrderManagementVO) throws Exception{
		String stmt = "select bpt.id from bar_order_parent_table bpt where bpt.table_id=? and bpt.hotel_id=? and bpt.customer_id=? and bpt.order_status=? and bpt.online_payment=?";
		try {
			Long id = jdbcTemplate.queryForObject(stmt, new Object[] {barOrderManagementVO.getTable_id(),barOrderManagementVO.getHotel_id(),barOrderManagementVO.getCustomer_id(), RESTONZACONSTANTS.new_order.toString(),false}, Long.class);
			return id;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	public List<Map<String, Object>> getBarOrders(int hotel_id) throws Exception {
		String sqlstmt = "SELECT al.customer_id, al.table_id, al.id, GROUP_CONCAT(concat (al.itemname, '-', al.qty)SEPARATOR ',') formatOrderSummary,sum(al.qty*al.price) sum FROM (select bpt.customer_id customer_id, bpt.table_id table_id, bpt.id id,bi.bar_item_name itemname,sum(bd.qty) qty, bd.price price FROM bar_order_parent_table bpt INNER JOIN bar_order_details bd ON bpt.id = bd.parent_order_id INNER JOIN bar_items bi ON bi.id = bd.bar_item_id where bpt.order_status !=? and bpt.order_status !=? and bpt.hotel_id=? group by bi.bar_item_name, bd.price, bpt.customer_id, bpt.table_id, bpt.id) al group by al.customer_id, al.table_id, al.id";
		List<Map<String, Object>> runningBarOrders = jdbcTemplate.queryForList(sqlstmt, new Object[] {RESTONZACONSTANTS.cancel_order.toString(), RESTONZACONSTANTS.billed.toString(), hotel_id});
		return runningBarOrders;
	}
	
	public List<Map<String, Object>> getBarOrdersById(int orderId) throws Exception {
		String sqlstmt = "SELECT al.customer_id, al.table_id, al.id, GROUP_CONCAT(concat (al.itemname, '-', al.qty)SEPARATOR ',') formatOrderSummary,sum(al.qty*al.price) sum FROM (select bpt.customer_id customer_id, bpt.table_id table_id, bpt.id id,bi.bar_item_name itemname,sum(bd.qty) qty, bd.price price FROM bar_order_parent_table bpt INNER JOIN bar_order_details bd ON bpt.id = bd.parent_order_id INNER JOIN bar_items bi ON bi.id = bd.bar_item_id where bpt.order_status !=? and bpt.order_status !=? and bpt.id=? group by bi.bar_item_name, bd.price, bpt.customer_id, bpt.table_id, bpt.id) al group by al.customer_id, al.table_id, al.id";
		List<Map<String, Object>> runningBarOrders = jdbcTemplate.queryForList(sqlstmt, new Object[] {RESTONZACONSTANTS.cancel_order.toString(), RESTONZACONSTANTS.billed.toString(), orderId});
		return runningBarOrders;
	}
	
	public List<Map<String, Object>> getBarOrderStatusWise(int hotel_id, String status) throws Exception {
		String sqlstmt = "SELECT al.customer_id, al.table_id, al.id, GROUP_CONCAT(concat (al.itemname, '-', al.qty)SEPARATOR ',') orderdetails ,  sum(al.qty*al.price) sum, al.updated_time, al.status, al.instruction FROM (select bpt.customer_id customer_id, bpt.table_id table_id, bpt.id id, bi.bar_item_name itemname,sum(bd.qty) qty,bd.updated_time updated_time, bd.order_status status, bd.price, bpt.instruction FROM bar_order_parent_table bpt INNER JOIN bar_order_details bd ON bpt.id = bd.parent_order_id and bpt.hotel_id=? and bd.order_status=? and bpt.order_status != ? and bpt.order_status != ? INNER JOIN bar_items bi ON bi.id = bd.bar_item_id group by bi.bar_item_name, bd.price, bpt.customer_id, bpt.table_id, bpt.id) al group by al.customer_id, al.table_id, al.id,al.status";
		List<Map<String, Object>> runningBarOrders = jdbcTemplate.queryForList(sqlstmt, new Object[] {hotel_id, status, RESTONZACONSTANTS.cancel_order.toString(), RESTONZACONSTANTS.billed.toString()});
		return runningBarOrders;
	}
	
	public List<Map<String, Object>> getBarOrderStatusWise(int hotel_id, String status1, String status2) throws Exception {
		String sqlstmt = "SELECT al.customer_id, al.table_id, al.id, GROUP_CONCAT(concat (al.itemname, '-', al.qty)SEPARATOR ',') orderdetails , al.status, sum(al.qty*al.price) sum, al.updated_time, al.status, al.instruction FROM (select bpt.customer_id customer_id, bpt.table_id table_id, bpt.id id, bi.bar_item_name itemname,sum(bd.qty) qty,bd.order_status status, bd.updated_time updated_time, bd.price, bpt.instruction FROM bar_order_parent_table bpt INNER JOIN bar_order_details bd ON bpt.id = bd.parent_order_id and bpt.hotel_id=? and bpt.order_status != ? and bpt.order_status != ? INNER JOIN bar_items bi ON bi.id = bd.bar_item_id where bd.order_status=? or bd.order_status=? group by bi.bar_item_name, bd.price, bpt.customer_id, bpt.table_id, bpt.id) al group by al.customer_id, al.table_id, al.id,al.status";
		List<Map<String, Object>> runningBarOrders = jdbcTemplate.queryForList(sqlstmt, new Object[] {hotel_id, RESTONZACONSTANTS.cancel_order.toString(), RESTONZACONSTANTS.billed.toString(), status1, status2, });
		return runningBarOrders;
	}
	
	public List<Map<String, Object>> getBarOrderPaymentStatus(String hotel_id, String table_id, String customer_id, String status) throws Exception {
		String sqlstmt = "select order_status from bar_order_parent_table  where hotel_id=? and table_id=? and customer_id=? and order_status NOT IN (?,'cancel_order');";
		List<Map<String, Object>> barOrderStatus = jdbcTemplate.queryForList(sqlstmt, new Object[] {hotel_id, table_id, customer_id, status});
		return barOrderStatus;
	}
	
	@RestonzaCustomTransactional
	public void cancelBarOrder(int order_id, String newStatus, String oldStatus) throws Exception {
		String sqlstmt = "update bar_order_details bd set bd.order_status= ? where bd.parent_order_id = ? and bd.order_status= ?";
		jdbcTemplate.update(sqlstmt, new Object[] {newStatus,order_id,oldStatus});
		String stmt = "select count(bd.id) from bar_order_details bd where bd.parent_order_id=? and bd.order_status!= ?";
		try {
			Long id = jdbcTemplate.queryForObject(stmt, new Object[] {order_id, newStatus}, Long.class);
			if (id == 0 ) {
				String parenttableupdate = "update bar_order_parent_table bpt set bpt.order_status= ? where bpt.id = ?";
				jdbcTemplate.update(parenttableupdate, new Object[] {newStatus,order_id});
			}
		} catch (EmptyResultDataAccessException e) {
			throw new RestonzaCustomException("Error occured while cancelling bar order! Please contact support for assisatnce");
		}
	}
	
	public void updateBarOrder(int order_id, String newStatus, String oldStatus) throws Exception {
		String sqlstmt = "update bar_order_details bd set bd.order_status= ? where bd.parent_order_id = ? and bd.order_status= ?";
		jdbcTemplate.update(sqlstmt, new Object[] {newStatus,order_id,oldStatus});
	}
	
	public List<Map<String, Object>> getItemById(String order_id) throws Exception {
		String sqlstmt = "select BI.bar_item_name, BOD.id,BOD.qty,BOD.price,BOD.order_status,BOD.updated_time  from bar_order_details BOD,bar_items BI where BOD.parent_order_id=? and BI.id=BOD.bar_item_id;";
		List<Map<String, Object>> barOrderDetails = jdbcTemplate.queryForList(sqlstmt, new Object[] {order_id});
		return barOrderDetails;
	}
}
