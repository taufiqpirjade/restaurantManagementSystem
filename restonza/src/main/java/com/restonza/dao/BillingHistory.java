/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restonza.vo.OrderDetailsVO;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "billing_history")
public class BillingHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private int hotel_id;
	@NotNull
	private String order_ids;
	@NotNull
	private Date billed_date;
	@NotNull
	private double bill_amount;
	@NotNull
	private String customer_id;
	@NotNull
	private String order_summary;
	private Double bill_tax;
	private Double liquor_tax;
	private Double bill_discount;
	
	private String bar_order_ids;
	
	private String food_order_ids;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(int hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getOrder_ids() {
		return order_ids;
	}
	public void setOrder_ids(String order_ids) {
		this.order_ids = order_ids;
	}
	public Date getBilled_date() {
		return billed_date;
	}
	public void setBilled_date(Date billed_date) {
		this.billed_date = billed_date;
	}
	public double getBill_amount() {
		return bill_amount;
	}
	public void setBill_amount(double bill_amount) {
		this.bill_amount = bill_amount;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getOrder_summary() {
		return order_summary;
	}
	public void setOrder_summary(String order_summary) {
		this.order_summary = order_summary;
	}
	
	public Double getBill_tax() {
		return bill_tax;
	}
	public void setBill_tax(Double bill_tax) {
		this.bill_tax = bill_tax;
	}
	public Double getBill_discount() {
		return bill_discount;
	}
	public void setBill_discount(Double bill_discount) {
		this.bill_discount = bill_discount;
	}
	
	public String getBar_order_ids() {
		return bar_order_ids;
	}
	public void setBar_order_ids(String bar_order_ids) {
		this.bar_order_ids = bar_order_ids;
	}
	public String getFood_order_ids() {
		return food_order_ids;
	}
	public void setFood_order_ids(String food_order_ids) {
		this.food_order_ids = food_order_ids;
	}
	
	public BillingHistory prepareBillingHistory(BillingHistory aBillingHistory, OrderDetailsVO aOrderDetailsVO, Double taxAmount,Double liquorTax) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		aBillingHistory.setHotel_id(Integer.parseInt(aOrderDetailsVO.getHotel_id()));
		aBillingHistory.setOrder_ids(aOrderDetailsVO.getOrder_id());
		aBillingHistory.setBar_order_ids(aOrderDetailsVO.getBar_order_ids());
		aBillingHistory.setFood_order_ids(aOrderDetailsVO.getFood_order_ids());
		aBillingHistory.setBill_amount(aOrderDetailsVO.getAmt());
		aBillingHistory.setBilled_date(sdf.parse(sdf.format(new Date())));
		aBillingHistory.setCustomer_id(aOrderDetailsVO.getCustomer_id());
		aBillingHistory.setOrder_summary(aOrderDetailsVO.iterateOrderDetails(aOrderDetailsVO.getOrderDetails()));
		//Added tax details and discount details
		String foodAmount = aOrderDetailsVO.getFood_bill_amt().equals("null") ? "0": aOrderDetailsVO.getFood_bill_amt();
		String barAmount = aOrderDetailsVO.getBar_bill_amt().equals("null") ? "0" : aOrderDetailsVO.getBar_bill_amt(); 
		Double tax = Double.parseDouble(foodAmount)*taxAmount/100;
		Double liquortax = Double.parseDouble(barAmount)*liquorTax/100;
		Double discount = aOrderDetailsVO.getTotalAmountWOTaxDiscount() - aOrderDetailsVO.getAmt();
		aBillingHistory.setBill_discount(discount);
		aBillingHistory.setBill_tax(tax);
		aBillingHistory.setLiquor_tax(liquortax);
		return aBillingHistory;
	}
	
	public Double getLiquor_tax() {
		return liquor_tax;
	}
	public void setLiquor_tax(Double liquor_tax) {
		this.liquor_tax = liquor_tax;
	}
}
