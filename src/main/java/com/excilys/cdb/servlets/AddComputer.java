package com.excilys.cdb.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.beans.CompanyBean;
import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.controller.Controller;
import com.excilys.cdb.model.Company;

@WebServlet(name = "AddComputer", urlPatterns = "/add")
public class AddComputer extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Controller control = new Controller();

	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		ArrayList<CompanyBean> list = control.listCompanyBean();
		request.setAttribute("companyList", list);
	
		this.getServletContext().getRequestDispatcher( "/WEB-INF/addComputer.jsp" ).forward( request, response );
	}
	
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		ComputerBean nbean = new ComputerBean();
		
		nbean.setName(request.getParameter("computerName"));
		
		Optional<String> introducedBean = Optional.ofNullable(request.getParameter("introduced"));
		if(introducedBean.isPresent()) {
			nbean.setIntroduced(introducedBean.get());
		} else {
			nbean.setIntroduced("n");
		}
		
		Optional<String> discontinuedBean = Optional.ofNullable(request.getParameter("discontinued"));
		if(discontinuedBean.isPresent()) {
			nbean.setDiscontinued(discontinuedBean.get());
		} else {
			nbean.setDiscontinued("n");
		}
		
		Optional<String> companyIdBean = Optional.ofNullable(request.getParameter("companyId"));
		if(companyIdBean.isPresent()) {
			nbean.setCompany(companyIdBean.get());
			System.out.println("CompanyId is " + companyIdBean.get());
		} else {
			nbean.setCompany("0");
			System.out.println("CompanyId is 0");
		}
		
		control.addComputerBean(nbean);
		
		this.getServletContext().getRequestDispatcher( "/WEB-INF/addComputer.jsp" ).forward( request, response );
	}
	
}
