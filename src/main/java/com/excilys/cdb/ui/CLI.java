package com.excilys.cdb.ui;

import java.util.Optional;
import java.util.Scanner; 

import com.excilys.cdb.controller.*;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class CLI {
	
		
	private final String[] menu =  {"1) List computers", "2) List companies", "3) Show one computer", "4) Create a computer", "5) Update a computer", "6) Delete a computer", "7) Exit"};
	private final String[] commande = {"Computer List :", "Company List :", "Computer found :", "Computer created", "Computer Updated", "Computer Deleted"};
	private final String[] failure = {"Couldn't access Computer list", "Couldn't access Company list", "Computer not found", "Computer not created", "Computer not found", "Computer not found"};
	private final String[] order = {"Enter the following computer attributes :", "-Id ( -1 for no input ) :", "-Name :", "-Introduction date ( yyyy-mm-dd | n if null ) :", "-Discontinuation Date ( yyyy-mm-dd | n if null ) :", "-Company Id ( 0 if null ) :"};
	
	private MenuChoice status;
	Scanner sc;
	private Controller control;
	
	public CLI() {
		status = MenuChoice.MENU;
		control = new Controller();
		this.init();
	}
	
	public void init() {
		
		sc = new Scanner(System.in);
		
		while (status.getNumber() >= 0) {			
			
			switch (status) {
			
			case COMPUTER_LIST:
				
				System.out.println("Enter the Page length :" + "\n");		
				int taille1 = sc.nextInt();
				
				Optional<String> testPage1 = control.initPage(Computer.class, taille1);
				if (!testPage1.isPresent()) {
					
					gestionPage();
					
				} else {
					System.out.println(testPage1.get());
				}
							
				status = MenuChoice.MENU;				
				break;
			
			case COMPANY_LIST:
				
				System.out.println("Enter the Page length :" + "\n");
				int taille2 = sc.nextInt();	
				
				Optional<String> testPage2 = control.initPage(Company.class, taille2);
				if (!testPage2.isPresent()) {
					
					gestionPage();
					
				} else {
					System.out.println(testPage2.get());
				}
							
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
				
				demande();
				retour(control.addComputer());
				
				sc.next();				
				status = MenuChoice.MENU;
				
				break;
			
			case UPDATE_COMPUTER:
				
				demande();			
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
	
	public void demande() {
		
		control.initComputer();
		
		int i = 0;
		while (i < order.length) {
			System.out.println(order[i] + "\n");
			
			switch (i) {
	
			//Waits for computer id
			case 1:
				control.setId(sc.nextInt());
				break;
				
			//Waits for computer name
			case 2:
				boolean nameTest = control.setName(sc.next());				
				
				while (!nameTest) {
					System.out.println("Name can't be null" + "\n");
					nameTest = control.setName(sc.next());
				}
				break;
				
			//Waits and check for introduction date
			case 3:
				boolean startTest = control.setStart(sc.next());
				
				while (!startTest) {
					System.out.println("Wrong date format" + "\n");
					startTest = control.setStart(sc.next());
				}
				break;	
				
			//Waits and check for discontinuation date
			case 4:
				boolean endTest = control.setEnd(sc.next());
				
				while (!endTest) {
					System.out.println("Wrong date format or Discontunation Date higher than Introduction Date" + "\n");
					System.out.println(order[i] + "\n");
					System.out.println("Reminder, Introduction Date is :" + control.getStart() + "\n");
					endTest = control.setEnd(sc.next());
				}
				break;
				
			//Waits for company id
			case 5:
				control.setCompanyId(sc.nextInt());
				break;
			default:
			}
			i++;
		}
		control.buildComputer();
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
