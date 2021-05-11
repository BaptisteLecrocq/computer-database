package com.excilys.ui;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import com.excilys.model.*;
import com.excilys.service.*;

public class CLI {
	
	private static String[] menu =  {"1) List computers","2) List companies","3) Show one computer","4) Create a computer","5) Update a computer","6) Delete a computer","7) Exit"};
	private static String[] commande = {"Computer List :","Company List :","Computer found :","Computer created","Computer Updated","Computer Deleted"};
	private static String[] failure = {"Couldn't access Computer list","Couldn't access Company list","Computer not found","Computer not created","Computer not found","Computer not found"};
	private static String[] order = {"Enter the following computer attributes :","-Id ( -1 for no input ) :","-Name :","-Introduction date ( dd/mm/yyy | n if null ) :","-Discontinuation Date ( dd/mm/yyy | n if null ) :","-Company Id ( 0 if null ) :"};
	
	private int status;
	Scanner sc;
	CRUD service;
	
	public CLI(){
		status = 0;
		service = new CRUD();
		this.init();
	}
	
	public void init() {
		
		sc = new Scanner(System.in);
		
		while(status >= 0) {
			switch(status){
			//Display computer list
			case 1:
				System.out.println(commande[status-1]+"\n");			
				System.out.println(service.getComputerList().toString());
				
				sc.next();				
				status = 0;
				
				break;
			//Displays company list
			case 2:
				System.out.println(commande[status-1]+"\n");
				System.out.println(service.getCompanyList().toString());
				
				sc.next();				
				status = 0;
				
				break;
			//Asks and shows one computer
			case 3:			
				
				System.out.println(order[0]+"\n");
				System.out.println(order[1]+"\n");
				
				int id = sc.nextInt();
				Computer result = service.getComputerById(id);
				
				
				retour(result!=null);
				System.out.println(result);
				
				sc.next();				
				status = 0;
				
				break;
			//Asks and create a computer	
			case 4:
				
				Computer addComputer = demande();
				retour(service.add(addComputer));
				
				sc.next();				
				status = 0;
				
				break;
			//Asks and update a computer
			case 5:
				
				Computer updateComputer = demande();				
				retour(service.update(updateComputer));
				
				sc.next();				
				status = 0;
				
				break;
			//Exits the loop
			case 7:
				clean();
				System.out.println("Fin du programme");
				status = -1;
				break;
			//Menu
			default:
				clean();
				
				for(String m:menu) {
					System.out.println(m+"\n");
				}
				status = sc.nextInt();
				
				clean();
				break;
			}
		}
		
		sc.close();
		service.CRUDstop();
	}
	
	public void clean() {
		for(int i=0;i<20;i++){
			System.out.println("\n");;
		}
	}
	
	public void retour(boolean state) {
		if(state) {
			System.out.println(commande[status-1]+"\n");
		}
		else {
			System.out.println(failure[status-1]+"\n");
		}
	}
	
	public Computer demande() {
		int id = -1;
		String name = null;
		Date introduced = null;
		Date discontinued = null;
		int company_id = -1;
		
		Computer computer = null;
		
		int i=0;
		while(i<order.length) {
			System.out.println(order[i]+"\n");
			switch(i) {
			case 1:
				id = sc.nextInt();
				break;
			case 2:
				name = sc.next();
				break;
			case 3:
				String date1 = sc.next();
				System.out.println(date1);
				if(date1.equals("n")) {
					introduced = null;
				}
				else {
					try {
						introduced = new SimpleDateFormat("dd/MM/yyyy").parse(date1);
					} catch (ParseException e) {
						System.out.println("Wrong date format"+"\n");
						i--;
					} 
				}
				break;				
			case 4:
				String date2 = sc.next();
				if(date2.equals("n")) {
					introduced = null;
				}
				else {
					try {
						introduced = new SimpleDateFormat("dd/MM/yyyy").parse(date2);
					} catch (ParseException e) {
						System.out.println("Wrong date format"+"\n");
						i--;
					} 
				}
				break;
			case 5:
				company_id = sc.nextInt();
				break;
			default:
			}
			i++;
		}
		
		if(id<0) {
			computer = new Computer(name,introduced,discontinued,company_id);
		}
		else {
			computer = new Computer(id,name,introduced,discontinued,company_id);
		}
		
		return(computer);
	}

}
