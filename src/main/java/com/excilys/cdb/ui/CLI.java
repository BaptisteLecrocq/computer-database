package com.excilys.cdb.ui;

import java.util.ArrayList; 
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.beans.CompanyBeanCLI;
import com.excilys.cdb.beans.ComputerBeanCLI;
import com.excilys.cdb.beans.RequestParameterBean;
import com.excilys.cdb.controller.ControllerCentral;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageCompany;
import com.excilys.cdb.model.PageComputer;


@Component
public class CLI {
	
		
	private final String[] menu =  {"1) List computers", "2) List companies", "3) Show one computer", "4) Create a computer", "5) Update a computer", "6) Delete a computer", "7) Create a Company", "8) Delete a Company", "9) Exit"};
	private final String[] commande = {"Computer List :", "Company List :", "Computer found :", "Computer created", "Computer Updated", "Computer Deleted"};
	private final String[] failure = {"Couldn't access Computer list", "Couldn't access Company list", "Computer not found", "Computer not created", "Computer not found", "Computer not found"};
	private final String[] order = {"Enter the following computer attributes :", "-Id :", "-Name :", "-Introduction date ( yyyy-mm-dd | n if null ) :", "-Discontinuation Date ( yyyy-mm-dd | n if null ) :", "-Company Id ( 0 if null ) :"};
	private final String[] orderCompany = { "Enter the following company attributes :","-Id :", "-Name :"};
	
	@Autowired
	private ControllerCentral control;
	@Autowired
	private Validation val;
	
	
	private MenuChoice status;
	Scanner sc;
	
	public CLI() {
		status = MenuChoice.MENU;
	}
	
	public void init() {
		
		sc = new Scanner(System.in);
		
		while (status.getNumber() >= 0) {			
			
			switch (status) {
			
			case COMPUTER_LIST:
				
				System.out.println("Enter the Page length :" + "\n");	
				
				int taille1 = sc.nextInt();
				ArrayList<String> taille1Test = val.valTaille(taille1);
				
				while(!taille1Test.isEmpty()) {
					
					System.out.println(taille1Test.toString());
					System.out.println("Enter the Page length :" + "\n");
					
					taille1 = sc.nextInt();
					taille1Test = val.valTaille(taille1);					
				}
				
				control.initPage(PageComputer.class, taille1, new RequestParameterBean());
				gestionPage();
							
				status = MenuChoice.MENU;				
				break;
			
			case COMPANY_LIST:
				
				System.out.println("Enter the Page length :" + "\n");
				
				int taille2 = sc.nextInt();
				ArrayList<String> taille2Test = val.valTaille(taille2);
				
				while(!taille2Test.isEmpty()) {
					
					System.out.println(taille2Test.toString());
					System.out.println("Enter the Page length :" + "\n");
					
					taille2 = sc.nextInt();
					taille2Test = val.valTaille(taille2);					
				}
				
				control.initPage(PageCompany.class, taille2, new RequestParameterBean());
				gestionPage();
							
				status = MenuChoice.MENU;
				
				break;
			
			case FIND_COMPUTER:			
				
				System.out.println(order[0] + "\n");
				System.out.println(order[1] + "\n");
				
				int idFind = sc.nextInt();			
				System.out.println(control.getComputerById(idFind));
				
				sc.next();				
				status = MenuChoice.MENU;
				
				break;
				
			case ADD_COMPUTER:
				
				control.setComputerCLI(askComputer());
				retour(control.addComputer());
				
				sc.next();				
				status = MenuChoice.MENU;
				
				break;
			
			case UPDATE_COMPUTER:
				
				control.setComputerCLI(askComputer());			
				retour(control.updateComputer());
				
				sc.next();				
				status = MenuChoice.MENU;
				
				break;
			
			case DELETE_COMPUTER:
				
				System.out.println(order[0] + "\n");
				System.out.println(order[1] + "\n");
				
				int idDelete = sc.nextInt();
				retour(control.deleteComputer(idDelete));
				
				sc.next();				
				status = MenuChoice.MENU;
				
				break;				
			
			case ADD_COMPANY:
				
				control.setCompanyCLI(askCompany());
				control.addCompany();
				
				sc.next();				
				status = MenuChoice.MENU;
				
				break;
				
			case DELETE_COMPANY:
				
				System.out.println(orderCompany[0] + "\n");
				System.out.println(orderCompany[1] + "\n");
				
				control.deleteCompany(sc.nextInt());
				
				break;
			
			case EXIT:
				
				clean();
				System.out.println("Fin du programme");
				status = MenuChoice.END;
				break;
			
			default:
				
				clean();
				
				for (String m:menu) {
					System.out.println(m + "\n");
				}
				status = MenuChoice.values()[sc.nextInt() + 1];
				
				clean();
				break;
			}
		}
		
		sc.close();
		control.close();
	}


	public void clean() {
		for (int i = 0; i < 20; i++) {
			System.out.println("\n");
		}
	}
	
	public void retour(boolean state) {
		if (state) {
			System.out.println(commande[status.getNumber() - 1] + "\n");
		
		} else {
			System.out.println(failure[status.getNumber() - 1] + "\n");
		}
	}
	
	public ComputerBeanCLI askComputer() {
		
		ComputerBeanCLI cBean = new ComputerBeanCLI();
		
		int i = 0;
		System.out.println(order[i] + "\n");
		
		while (i < order.length) {
			
			switch (i) {
	
			//Waits for computer id
			case 1:
				
				if( status == MenuChoice.UPDATE_COMPUTER ) {
					
					System.out.println(order[i] + "\n");
					cBean.setId((sc.nextInt()));
				}				
				break;
				
			//Waits for computer name
			case 2:
				System.out.println(order[i] + "\n");
				
				String name = sc.next();
				ArrayList<String> nameTest = val.valName(name);	
				
				while (!nameTest.isEmpty()) {
					System.out.println(nameTest.toString());
					name = sc.next();
					nameTest = val.valName(name);
				}
				
				cBean.setName(name);				
				break;
				
			//Waits and check for introduction date
			case 3:
				System.out.println(order[i] + "\n");
				
				String start = sc.next();
				ArrayList<String> startTest = val.valStart(start);	
				
				while (!startTest.isEmpty()) {
					
					System.out.println(startTest.toString());
					System.out.println(order[i] + "\n");
					
					start = sc.next();
					startTest = val.valStart(start);
				}
				
				cBean.setIntroduced(start);
				break;	
				
			//Waits and check for discontinuation date
			case 4:
				System.out.println(order[i] + "\n");
				
				String end = sc.next();
				ArrayList<String> endTest = val.valEnd(cBean.getIntroduced(), end);	
				
				while (!endTest.isEmpty()) {
					
					System.out.println(endTest.toString());
					System.out.println(order[i] + "\n");
					System.out.println("Reminder, Introduction Date is :" + cBean.getIntroduced() + "\n");
					
					end = sc.next();
					endTest = val.valEnd(cBean.getIntroduced(), end);
				}
				
				cBean.setDiscontinued(end);
				break;
				
			//Waits for company id
			case 5:
				System.out.println(order[i] + "\n");
				
				int companyId = sc.nextInt();
				ArrayList<String> companyIdTest = val.valCompanyId(companyId);	
				
				while (!companyIdTest.isEmpty()) {
					
					System.out.println(companyIdTest.toString());
					System.out.println(order[i] + "\n");
					
					companyId = sc.nextInt();
					companyIdTest = val.valCompanyId(companyId);
				}				
				
				cBean.setCompany_id(companyId);
				break;
				
			default:
			}
			i++;
		}
		
		return(cBean);
	}
	
	public CompanyBeanCLI askCompany() {
		
		CompanyBeanCLI cBean = new CompanyBeanCLI();
		
		int i = 0;
		while (i < orderCompany.length) {
			
			switch(i) {
			case 0:
				System.out.println(order[i] + "\n");
				break;
			
			case 1:
				System.out.println(order[i] + "\n");
				
				int companyId = sc.nextInt();
				ArrayList<String> companyIdTest = val.valCompanyId(companyId);	
				
				while (!companyIdTest.isEmpty()) {
					
					System.out.println(companyIdTest.toString());
					System.out.println(order[i] + "\n");
					
					companyId = sc.nextInt();
					companyIdTest = val.valCompanyId(companyId);
				}				
				
				cBean.setId(companyId);
				break;
			
			case 2:
				System.out.println(order[i] + "\n");
				
				cBean.setName(sc.next());
				break;			
			}
			i++;
		}
		
		return(cBean);
		
	}
	
	public void gestionPage() {
		int pageStatus = 0;
		
		while (pageStatus >= 0) {
			switch (pageStatus) {
			//Goes to Next page
			case 1:
				System.out.println(control.nextPage());
				pageStatus = 0;
				break;
			//Goes to Previous page
			case 2:
				System.out.println(control.previousPage());			
				pageStatus = 0;
				break;
			//Exits loop
			case 3:
				pageStatus = -1;
				break;
			//Prints page and waits for next action
			default:
				
				System.out.println(control.getPage().toString());
				System.out.println("\n" + "Enter a number :" + "\n" + "1) Next Page | 2) Previous Page | 3) Exit" + "\n");
				
				pageStatus = sc.nextInt();
				clean();
				break;
			}
		}
	}

}
