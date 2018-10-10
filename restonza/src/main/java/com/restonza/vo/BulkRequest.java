/**
 * 
 */
package com.restonza.vo;

import java.util.List;

/**
 * @author flex-grow developers
 *
 */
public class BulkRequest {
	List<BulkMenuUpload> bulkMenu;

	public List<BulkMenuUpload> getBulkMenu() {
		return bulkMenu;
	}

	public void setBulkMenu(List<BulkMenuUpload> bulkMenu) {
		this.bulkMenu = bulkMenu;
	}

	@Override
	public String toString() {
		return "BulkRequest [bulkMenu=" + bulkMenu + "]";
	}
	
}
