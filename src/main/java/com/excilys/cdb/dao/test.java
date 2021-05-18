package com.excilys.cdb.dao;

import java.util.Date; 

import com.excilys.cdb.model.*;

public class test {

	public static void main(String[] args) {
		
		
		DAO test = DAO.getInstance();
		System.out.println(test.listComputer());
		/*test.addComputer(new Computer
				.ComputerBuilder("testadd")
				.withManufacturer(3)
				.build());
				*/
		//System.out.println(test.listComputer());
		//System.out.println(test.findComputer("test1234"));
		//test.updateComputer(test.findComputer("testa").getId(),new Computer("testb",null,null,new Company(26)));
		//System.out.println("ici");
		//System.out.println(test.findComputer("testb"));
		//test.deleteComputer(test.findComputer("testa").getId());
		//test.findComputer("testa");
		
		
		test.stop();		

	}
}
