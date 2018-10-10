/**
 * 
 */
package com.restonza.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restonza.vo.AllOrderVO;

/**
 * @author flex-grow developers
 *
 */
public class OrderMapper implements RowMapper<Object> {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		AllOrderVO allOrderVO = new AllOrderVO();
		allOrderVO.setCustomerid(rs.getString(1));
		allOrderVO.setOrderids(rs.getString(2));
		allOrderVO.setOrdersummaries(rs.getString(3));
		allOrderVO.setSum(rs.getInt(4));
		return allOrderVO;
	}

}
