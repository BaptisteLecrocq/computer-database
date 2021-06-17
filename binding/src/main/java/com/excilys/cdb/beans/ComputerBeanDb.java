package com.excilys.cdb.beans;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;




@Entity
@Table(name = "computer")
public class ComputerBeanDb {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private Date introduced;
	private Date discontinued;
	
	@ManyToOne
	private CompanyBeanDb company;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getIntroduced() {
		return introduced;
	}
	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}
	public Date getDiscontinued() {
		return discontinued;
	}
	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}
	public CompanyBeanDb getCompany() {
		return company;
	}
	public void setCompany(CompanyBeanDb company) {
		this.company = company;
	}
	
	@Override
	public String toString() {
		return("Id : "+id+", Name : "+name+", Introduced : "+introduced+", Discontinued : "+discontinued+", Company : "+company);
	}
}
