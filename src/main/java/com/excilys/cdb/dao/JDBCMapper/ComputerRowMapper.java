package com.excilys.cdb.dao.JDBCMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.ComputerBeanDb;

@Component
public class ComputerRowMapper implements RowMapper<ComputerBeanDb> {
	
	public ComputerBeanDb mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		ComputerBeanDb cBean = new ComputerBeanDb();
		
		cBean.setId(rs.getInt("computer.id"));
		cBean.setName((rs.getString("computer.name")));
		cBean.setIntroduced(rs.getString("computer.introduced"));
		cBean.setDiscontinued(rs.getString("computer.discontinued"));
		cBean.setCompanyId(rs.getInt("computer.company_id"));
		cBean.setCompanyName((rs.getString("company.name")));
		
		return cBean;
	}

}
