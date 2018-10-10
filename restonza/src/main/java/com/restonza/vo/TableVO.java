/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class TableVO {
	String table_id;
	String hotel_id;
	String description;
	String status;
	byte[] qrcodeimg;
	int numberOfPeopleSitting;
	
	public TableVO() {
		super();
	}
	
	/**
	 * @param table_id
	 * @param hotel_id
	 * @param qrcodeimg
	 */
	public TableVO(String table_id, String hotel_id, byte[] qrcodeimg) {
		super();
		this.table_id = table_id;
		this.hotel_id = hotel_id;
		this.qrcodeimg = qrcodeimg;
	}

	public TableVO(String table_id, String description,
			String status) {
		this.table_id = table_id;
		this.description = description;
		this.status = status;
	}
	
	public byte[] getQrcodeimg() {
		return qrcodeimg;
	}

	public void setQrcodeimg(byte[] qrcodeimg) {
		this.qrcodeimg = qrcodeimg;
	}

	public String getTable_id() {
		return table_id;
	}
	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}
	public String getHotel_id() {
		return hotel_id;
	}
	public void setHotel_id(String hotel_id) {
		this.hotel_id = hotel_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getNumberOfPeopleSitting() {
		return numberOfPeopleSitting;
	}

	public void setNumberOfPeopleSitting(int numberOfPeopleSitting) {
		this.numberOfPeopleSitting = numberOfPeopleSitting;
	}

	@Override
	public String toString() {
		return "TableVO [table_id=" + table_id + ", hotel_id=" + hotel_id
				+ ", description=" + description + ", status=" + status + "]";
	}
}
