/**
 * 
 */
package com.restonza.vo;

import java.util.Arrays;

/**
 * @author flex-grow developers
 *
 */
public class AdsDetailsVO {
	private String id;
	private String adname;
	private String description;
	private byte[] adimg;
	private String status;
	private double price;
	
	public AdsDetailsVO() {
		super();
	}
	
	public AdsDetailsVO(String id, String adname, String description,
			byte[] adimg, String status,double price) {
		this.id = id;
		this.adname = adname;
		this.description = description;
		this.adimg = adimg;
		this.status = status;
		this.price = price;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAdname() {
		return adname;
	}
	public void setAdname(String adname) {
		this.adname = adname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getAdimg() {
		return adimg;
	}
	public void setAdimg(byte[] adimg) {
		this.adimg = adimg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "AdsDetailsVO [id=" + id + ", price=" + price
				+ ", adname=" + adname + ", description=" + description
				+ ", adimg=" + Arrays.toString(adimg) + ", status=" + status
				+ "]";
	}
	
}
