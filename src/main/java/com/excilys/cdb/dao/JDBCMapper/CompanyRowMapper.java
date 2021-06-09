package com.excilys.cdb.dao.JDBCMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.excilys.cdb.beans.CompanyBeanDb;

public class CompanyRowMapper implements RowMapper<CompanyBeanDb> {

	@Override
	public CompanyBeanDb mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		CompanyBeanDb cBean = new CompanyBeanDb();
		
		cBean.setId(rs.getInt("company.id"));
		cBean.setName(rs.getString("company.name"));
		
		return(cBean);
		
	}

}
