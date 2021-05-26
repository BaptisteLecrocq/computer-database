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

import com.excilys.cdb.beans.ComputerBean;
import com.excilys.cdb.controller.Controller;
import com.excilys.cdb.model.Computer;

@WebServlet(name = "Dashboard", urlPatterns = "/app")
public class Dashboard extends HttpServlet {

	private final int pageSize = 12;
	
	private static final long serialVersionUID = 1L;
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
		
		Optional<String> paramPageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
		
		if( paramPageNumber.isPresent()) {
			request.setAttribute("pageNumber", paramPageNumber.get());
		}
		else {
			request.setAttribute("pageNumber", 0);
		}

		
		control.initPage(Computer.class, pageSize, Integer.parseInt(request.getAttribute("pageNumber").toString()));
		
		request.setAttribute("count", control.getPage().getCount());
		
		
		ArrayList<ComputerBean> page = new ArrayList<ComputerBean>();
		
		Optional<LocalDate> introducedBuffer = Optional.empty();
		Optional<LocalDate> discontinuedBuffer = Optional.empty();
		
		for(Computer c:((ArrayList<Computer>)(control.getPage().getElements()))) {
			
			ComputerBean cBean = new ComputerBean();
			cBean.setName(c.getName());
			//System.out.println(cBean.getName());
			cBean.setCompany(c.getManufacturer().getName());
			
			introducedBuffer = Optional.ofNullable(c.getStart());
			if(introducedBuffer.isPresent()) {
				cBean.setIntroduced(introducedBuffer.get().toString());
			}
			
			discontinuedBuffer = Optional.ofNullable(c.getEnd());
			if(discontinuedBuffer.isPresent()) {
				cBean.setDiscontinued(discontinuedBuffer.get().toString());
			}
			
			
			page.add(cBean);
			
		}		
		request.setAttribute("page", page);
		
		
		
		
		this.getServletContext().getRequestDispatcher( "/WEB-INF/dashboard.jsp" ).forward( request, response );
	}
	
}

