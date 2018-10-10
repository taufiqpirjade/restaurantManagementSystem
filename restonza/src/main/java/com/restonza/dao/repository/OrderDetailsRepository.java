/**
 * 
 */
package com.restonza.dao.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.restonza.dao.OrderDetails;

/**
 * @author flex-grow developers
 *
 */
public interface OrderDetailsRepository extends CrudRepository<OrderDetails, Integer> {
	//Custom query declaration for order_details table
	@Query(value = "select o from OrderDetails o where o.hotel_id=:hotel_id and o.table_id=:table_id and o.status=:status")
	List<OrderDetails>  findOrders(@Param("hotel_id") int hotel_id, @Param("table_id") int table_id, @Param("status") String status);
	
	@Query(value = "select o from OrderDetails o where o.hotel_id=:hotel_id and o.status=:status")
	List<OrderDetails> findAllOrdersByStatus(@Param("hotel_id") int hotel_id, @Param("status") String status);
	
	@Query(value = "select o from OrderDetails o where o.hotel_id=:hotel_id and o.customer_id=:customer_id")
	List<OrderDetails> getOrderHistoryList(@Param("hotel_id") int hotel_id,@Param("customer_id") String customer_id);
	
	@Modifying
	@Transactional
	@Query(value = "update OrderDetails set status=:status where id=:order_id")
	void cancelOrder(@Param("status") String status, @Param("order_id") int order_id);
	
	@Query(value = "select o from OrderDetails o where o.hotel_id=:hotel_id and  o.status=:status and o.status != 'cancel_order'")
	List<OrderDetails> getAllOrders(@Param("hotel_id") int hotel_id,@Param("status") String status);
	
	@Query(value = "select o from OrderDetails o where o.hotel_id=:hotel_id and  o.status in ('confirm_order', 'ready_order') and o.status != 'cancel_order'")
	List<OrderDetails> getConfirmAndReadyOrders(@Param("hotel_id") int hotel_id);
	
	@Modifying
	@Transactional
	@Query(value = "update OrderDetails set status=:status, updated_on=:updated_on where id=:order_id and hotel_id=:hotel_id")
	void updateOrderStatus(@Param("hotel_id") int hotel_id,@Param("order_id") int order_id,@Param("status") String status, @Param("updated_on")Date updated_on);
	
	@Query(value = "select o.order_summary from OrderDetails o where o.id=:order_id")
	String  findOrderSummary(@Param("order_id") int order_id);
	
	 @Modifying
	 @Transactional
	 @Query(value = "update OrderDetails set order_summary=:order_summary, amount=:amount where id=:order_id")
	 void updateOrder(@Param("order_summary") String order_summary, @Param("amount")double amount, @Param("order_id") int order_id);
	 
	 @Modifying
	 @Transactional
	 @Query(value = "update OrderDetails set amount=:amount where id=:order_id")
	 void billOrder(@Param("amount")int amount, @Param("order_id") int order_id);
	 
	 @Query(value = "select o from OrderDetails o where id=:id")
	 OrderDetails getOrderDetail(@Param("id") int id);
	 
	 @Query(value = "select o.status from OrderDetails o where hotel_id=:hotel_id and table_id=:table_id and customer_id=:customer_id and status NOT IN (:status, 'cancel_order')")
	 List<String> getOrderStatus(@Param("hotel_id") int hotel_id, @Param("table_id") int table_id, @Param("customer_id") String customer_id, @Param("status") String status);
	 
	//update orderRating
	 @Modifying
	 @Transactional
	 @Query(value = "update HotelAnalyze set feedback=:feedback where orderids=:orderids")
	 void updateRating(@Param("feedback")int feedback, @Param("orderids") String orderids);
	 
	 @Query(value = "select count(*) from OrderDetails where hotel_id=:hotel_id and status = 'new_order'")
	 String getNewOrderCount(@Param("hotel_id") int hotel_id);
	 
	 @Query(value = "select count(*) from OrderDetails o where o.status!=:status")
	 Long getOrderCount(@Param("status") String status);
}
