package com.excilys.dao;

import java.util.Date;

import com.excilys.model.*;

public class test {

	public static void main(String[] args) {
		
		
		Connect test = new Connect();
		System.out.println(test.listComputer());
		test.addComputer(new Computer("testadd",null,null,3));
		System.out.println(test.listComputer());
		//System.out.println(test.findComputer("test1234"));
		//test.updateComputer(test.findComputer("testa").getId(),new Computer("testb",null,null,new Company(26)));
		//System.out.println("ici");
		//System.out.println(test.findComputer("testb"));
		//test.deleteComputer(test.findComputer("testa").getId());
		//test.findComputer("testa");
		
		
		test.stop();		

	}
}
