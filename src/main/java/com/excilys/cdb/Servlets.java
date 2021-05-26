package com.excilys.cdb;

import java.io.IOException; 

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.beans.TestBean;

@WebServlet(name = "Servlets", urlPatterns="/test")
public class Servlets extends HttpServlet {

	/**.
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String paramAuteur = request.getParameter( "auteur" );
		String message = "Transmission de variables : OK ! " + paramAuteur;
		request.setAttribute( "test", message );
		
		TestBean bean = new TestBean();
		
		bean.setNom("Gates");
		bean.setPrenom("Bill");		
		request.setAttribute("bean", bean);
		
		this.getServletContext().getRequestDispatcher( "/WEB-INF/test.jsp" ).forward( request, response );
	}
	
}

