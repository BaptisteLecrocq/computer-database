package com.excilys.cdb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.beans.CompanyBeanDb;
import com.excilys.cdb.beans.RequestParameterBeanDb;
import com.excilys.cdb.exception.TransactionException;
import com.excilys.cdb.mapper.MapperDTOdb;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.RequestParameter;

@Repository
public class CompanyDAO {
	
	private static final String getCompany = "SELECT id, name ";	
	private static final String countCompany = "SELECT COUNT(id) FROM CompanyBeanDb as cp WHERE cp.name LIKE :searchTerm";
	
	private static final String getLastCompanyId = "SELECT max(id) FROM CompanyBeanDb;";
	private static final String deleteCompany = "DELETE FROM CompanyBeanDb WHERE id= :id";	
	private static final String deleteLinkedComputers ="DELETE FROM ComputerBeanDb WHERE company_id= :id";
	
	private static final String orderBy = "ORDER BY ";
	private static final String[] order = { "ASC ", "DESC "};
	private static final String[] columnCompany = { "id ", "name " };
	
	private static final String companyAsk = "FROM CompanyBeanDb as cp WHERE cp.name LIKE :searchTerm ";

	
	private Database db;
	private MapperDTOdb mapDb;
	private SessionFactory factory;
	
	private static Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	
	public CompanyDAO(Database db, MapperDTOdb mapDb, SessionFactory factory) {
		
		this.db = db;
		this.mapDb = mapDb;
		this.factory = factory;
		
	}
	
	/*            Simple Requests             */
	
	public int getLastCompanyId() { 
		
		
		int id = 0;
 		
 		try( Session session = factory.openSession() ) {
 			
 			id = session.createQuery(getLastCompanyId,Long.class)
 							.getSingleResult()
 							.intValue();
 			
 		} catch (HibernateException e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}

		return(id);
	}
	
	
	/*            CRUD Requests             */		
	
	public void addCompany(Company company) {
		
		CompanyBeanDb cBean = mapDb.mapCompanyModelToDTOdb(company);
		
		try( Session session = factory.openSession() ) {
			
			session.save(cBean);		
		
		} catch (HibernateException e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}
		
	}

	public void deleteCompany(int id) throws TransactionException {
		
		try ( Session session = factory.openSession() ) {
			
			session.beginTransaction();
		
			try {
				
				session.createQuery(deleteLinkedComputers)
						.setParameter("id", id)
						.executeUpdate();
				
				session.createQuery(deleteCompany)
						.setParameter("id", id)
						.executeUpdate();
				
				session.getTransaction().commit();
				
			} catch( HibernateException e ) {
	    		
	    		logger.error(e.getMessage());
	    		e.printStackTrace();
	    		session.getTransaction().rollback();
	    		
	    	}
			
		} catch( HibernateException e ) {
    		
    		logger.error(e.getMessage());
    		e.printStackTrace();
    		
    	}
		
	}
	
	
	/*            Composed Requests             */	
	
	public ArrayList<Company> listCompany(RequestParameter parameters){
		
		List<CompanyBeanDb> searchResult = new ArrayList<CompanyBeanDb>();
		String query = companyAsk+orderBy+columnCompany[parameters.getChoice()]+order[parameters.getOrder()];
		
		try( Session session = factory.openSession() ) {
			
			searchResult = session.createQuery(query, CompanyBeanDb.class)
													.setParameter("searchTerm", "%"+parameters.getSearchTerm()+"%")
													.list();
			
		} catch( Exception e ) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}
		
		ArrayList<Company> companyList = (ArrayList<Company>) searchResult.stream()
					.map(s -> mapDb.mapDAOBeanToCompany(s))
					.collect(Collectors.toList());					
		
		return(companyList);	
	}		
	
	public int countCompany(RequestParameter parameters) {
			
		String query = countCompany;
		int result = 0;
		
		try( Session session = factory.openSession() ) {
			
			result = session.createQuery(query, Long.class)
							.setParameter("searchTerm", "%"+parameters.getSearchTerm()+"%")
							.uniqueResult()
							.intValue();
			
		} catch( Exception e ) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}		
					
		return(result);
	}			
	
	public ArrayList<Company> pageCompany(Page page){
			
		RequestParameterBeanDb parameters = mapDb.mapParametersToDTOdb(page.getParameters());
		List<CompanyBeanDb> searchResult = new ArrayList<CompanyBeanDb>();
		String query = companyAsk+orderBy+columnCompany[parameters.getChoice()]+order[parameters.getOrder()];
		
		try( Session session = factory.openSession() ) {
			
			searchResult = session.createQuery(query, CompanyBeanDb.class)
													.setParameter("searchTerm", "%"+parameters.getSearchTerm()+"%")
													.setFirstResult(page.getStart())
													.setMaxResults(page.getTaille())
													.list();
			
		} catch( Exception e ) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}
		
		ArrayList<Company> companyList = (ArrayList<Company>) searchResult.stream()
					.map(s -> mapDb.mapDAOBeanToCompany(s))
					.collect(Collectors.toList());					
		
		return(companyList);	
	}
	
	
	/*            Utility             */

	public Database getDb() {
		return db;
	}

}
