/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restonza.vo.AdsDetailsVO;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "ads_details")
public class AdsDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private String adname;
	@NotNull
	private String addetails;
	private double price;
	@Lob
	private byte[] adimg;
	private boolean status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAdname() {
		return adname;
	}
	public void setAdname(String adname) {
		this.adname = adname;
	}
	public String getAddetails() {
		return addetails;
	}
	public void setAddetails(String addetails) {
		this.addetails = addetails;
	}
	public byte[] getAdimg() {
		return adimg;
	}
	public void setAdimg(byte[] adimg) {
		this.adimg = adimg;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public AdsDetails prepareAdDetails(AdsDetails aAdsDetails, AdsDetailsVO aAdsDetailsVO ) throws Exception {
		aAdsDetails.setAdname(aAdsDetailsVO.getAdname());
		aAdsDetails.setAddetails(aAdsDetailsVO.getDescription());
		if (aAdsDetailsVO.getAdimg() !=null) {
			aAdsDetails.setAdimg(aAdsDetailsVO.getAdimg());	
		}
		aAdsDetails.setStatus(true);
		if (aAdsDetailsVO.getId() != null && !aAdsDetailsVO.getId().equalsIgnoreCase("")) {
			aAdsDetails.setId(Integer.valueOf(aAdsDetailsVO.getId()));
		}
		aAdsDetails.setPrice(aAdsDetailsVO.getPrice());
		return aAdsDetails;
	}
}
