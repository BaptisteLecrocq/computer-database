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
import com.excilys.cdb.controller.ControllerCentral;
import com.excilys.cdb.model.Company;

@WebServlet(name = "AddComputer", urlPatterns = "/add")
public class AddComputer extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private ControllerCentral control = new ControllerCentral();

	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		ArrayList<CompanyBean> list = control.listCompanyBean();
		request.setAttribute("companyList", list);
		
		request.setAttribute("validationFront", "true");
	
		this.getServletContext().getRequestDispatcher( "/WEB-INF/addComputer.jsp" ).forward( request, response );
	}
	
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		boolean validationFront = false;
		
		String buffer = request.getParameter("validationFront");
		if(buffer == null) {
			System.out.println("Validation attribute null");
		}
		else {
			
			System.out.println(buffer);
			validationFront = Boolean.parseBoolean(buffer);
			
		}
		
		
		if(validationFront) {
		
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
			} else {
				nbean.setCompany("0");
			}
			
			ArrayList<String> errors = control.addComputerBean(nbean);
			request.setAttribute("errors", errors);
			
		}
		
		this.getServletContext().getRequestDispatcher( "/WEB-INF/addComputer.jsp" ).forward( request, response );
	}
	
}
