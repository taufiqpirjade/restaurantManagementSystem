/**
 * 
 */
package com.restonza.vo;

import java.util.List;
import java.util.Map;

/**
 * @author flex-grow developers
 *
 */
public class BillManagementVO {
	List<GenerateBillVO> allorderVO;
	Map<String,Double> taxes;
	Map<String,Double> ltaxes;
	
	public BillManagementVO() {
		super();
	}
	
	/**
	 * @param allorderVO
	 * @param tax
	 */
	public BillManagementVO(List<GenerateBillVO> allorderVO, Map<String,Double> taxes, Map<String, Double> ltaxes) {
		this.allorderVO = allorderVO;
		this.taxes = taxes;
		this.ltaxes = ltaxes;
	}

	public List<GenerateBillVO> getAllorderVO() {
		return allorderVO;
	}
	public void setAllorderVO(List<GenerateBillVO> allorderVO) {
		this.allorderVO = allorderVO;
	}

	public Map<String, Double> getTaxes() {
		return taxes;
	}

	public void setTaxes(Map<String, Double> taxes) {
		this.taxes = taxes;
	}
	public Map<String, Double> getLtaxes() {
		return ltaxes;
	}

	public void setLtaxes(Map<String, Double> ltaxes) {
		this.ltaxes = ltaxes;
	}
}
