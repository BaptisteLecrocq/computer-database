package com.excilys.cdb.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.controller.Controller;
import com.excilys.cdb.model.Computer;

@WebServlet(name = "Dashboard", urlPatterns = "/app")
public class Dashboard extends HttpServlet {

	private final int pageSize = 12;
	
	private static final long serialVersionUID = 1L;
	private static final String[] columnName = { "Computer name", "Introduced date", "Discontinued date", "Company"};
	private Controller control = new Controller();
	

	//@SuppressWarnings("unused")
	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		/*
		String paramAuteur = request.getParameter( "auteur" );
		String message = "Transmission de variables : OK ! " + paramAuteur;
		request.setAttribute( "test", message );
		
		TestBean bean = new TestBean();
		
		bean.setNom("Gates");
		bean.setPrenom("Bill");		
		request.setAttribute("bean", bean);
		*/		
		
		
		HttpSession session = request.getSession();
		
		
		Optional<String> paramPageNumber = Optional.ofNullable(request.getParameter("pageNumber"));		
		ArrayList<ComputerBean> page = new ArrayList<ComputerBean>();
		String searchTerm;
		int order = -1;
		int count = 0;
		int choice = 0;
		
		Object buffer;
		String read;
		
		//pageNumber
		if( paramPageNumber.isPresent()) {
			request.setAttribute("pageNumber", paramPageNumber.get());
		}
		else {
			request.setAttribute("pageNumber", 0);
		}
		
		//searchTerm
		read = request.getParameter("search");
		if(read == null || read.length() < 1) {
			buffer = session.getAttribute("previousSearch");
			if(buffer != null) {
				searchTerm = buffer.toString();
			
			} else {
				searchTerm = null;
			}
		
		} else {
			searchTerm = read;
		}
		
		//order
		read = request.getParameter("order");
		if( read == null) {
			order = 0;
		
		} else { 
			order = Integer.parseInt(read);
		}
		
		//choice
		read = request.getParameter("choice");
		if( read == null) {
			choice = 0;
		
		} else { 
			choice = Integer.parseInt(read);
		}
		
		String refresh = request.getParameter("refresh");
		if(refresh != null) {
			System.out.println(refresh);
			session.invalidate();
			//session.setAttribute("previousSearch","");
		}
		else {
			session.setAttribute("previousSearch", searchTerm);
		}
		
		RequestParameterBean parameters = new RequestParameterBean();
		parameters.setSearchTerm(searchTerm);
		parameters.setOrder(order);
		parameters.setChoice(choice);
		control.initPage(Computer.class, pageSize, Integer.parseInt(request.getAttribute("pageNumber").toString()), parameters);
		
		page = control.pageComputerBean();
		count = control.getPage().getCount();		
		
		request.setAttribute("columnName", columnName);
		request.setAttribute("order", order);
		request.setAttribute("choice", choice);
		request.setAttribute("count", count);					
		request.setAttribute("page", page);
		
		
		
		this.getServletContext().getRequestDispatcher( "/WEB-INF/dashboard.jsp" ).forward( request, response );
	}
	
	public void doPost( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String test = request.getParameter("selection");
		control.deleteList(test);
		
		this.getServletContext().getRequestDispatcher( "/WEB-INF/editComputer.jsp" ).forward( request, response );
	}
	
}

