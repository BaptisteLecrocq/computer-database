package com.excilys.cdb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.beans.ComputerBeanDb;
import com.excilys.cdb.beans.RequestParameterBeanDb;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.MapperDTOdb;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.model.RequestParameter;

@Repository
public class ComputerDAO {
	
	
	private static final String getComputer = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name ";
	private static final String countComputer = "SELECT COUNT(id) FROM ComputerBeanDb as cp WHERE cp.name LIKE :searchTerm";
	private static final String getLastComputerId = "SELECT max(id) FROM ComputerBeanDb;";
	
	private static final String getOneComputer = "FROM ComputerBeanDb as cp LEFT JOIN FETCH cp.company WHERE cp.id= :id";
	private static final String updateComputer = "UPDATE FROM ComputerBeanDb SET name = :name ,introduced = :introduced, discontinued = :discontinued, company_id = :companyId WHERE id=:id";
	private static final String deleteComputer = "DELETE FROM ComputerBeanDb WHERE id= :id";
	private static final String deleteComputers = "DELETE FROM ComputerBeanDb WHERE id IN(";
	
	private static final String orderBy = "ORDER BY ";
	private static final String[] order = { "ASC ", "DESC "};
	private static final String[] columnComputer = { "cp.id ", "cp.name ", "cp.introduced ", "cp.discontinued ", "cy.name " };
	
	private static final String computerAsk = "FROM ComputerBeanDb as cp LEFT JOIN FETCH cp.company AS cy WHERE cp.name LIKE :searchTerm ";

	private Database db;
	private MapperDTOdb mapDb;
	private SessionFactory factory;
	
	private static Logger logger = LoggerFactory.getLogger(ComputerDAO.class);
	
	public ComputerDAO(Database db, MapperDTOdb mapDb, SessionFactory factory) {
		
		this.db = db;
		this.mapDb = mapDb;
		this.factory = factory;
		
	}
	
	/*            Simple Requests             */
	
	public int getLastComputerId() {
 		
 		int id = 0;
 		
 		try( Session session = factory.openSession() ) {
 			
 			id = session.createQuery(getLastComputerId,Long.class)
 							.getSingleResult()
 							.intValue();
 			
 		} catch (HibernateException e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}


		return(id);
	}
	
	/*            CRUD Requests             */	
	
	public Computer findComputer(int id) throws NotFoundException{		
		
	    ComputerBeanDb cBean;
	    
	    	try ( Session session = factory.openSession() ){
	    		
	    		cBean = session.createQuery(getOneComputer, ComputerBeanDb.class)
	    										.setParameter("id", id)
	    										.getSingleResult();  
	    		
	    		System.out.println(cBean);
	    	
	    	} catch( HibernateException e ) {
	    		
	    		logger.error(e.getMessage());
	    		e.printStackTrace();
	    		throw new NotFoundException("Computer not found");
	    		
	    	}
	    	
	    	Computer computer = mapDb.mapDAOBeanToComputer(cBean);
			
			return(computer);
			
		}
		
	public boolean addComputer(Computer computer){
		
		ComputerBeanDb cBean = mapDb.mapComputerModelToDTOdb(computer);
		
		try( Session session = factory.openSession() ) {
			
			session.save(cBean);
			
		} catch (HibernateException e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}
		
		return true;
		
	}

	public boolean updateComputer(Computer computer){

		ComputerBeanDb cBean = mapDb.mapComputerModelToDTOdb(computer);
		
		try ( Session session = factory.openSession() ) {
			
			session.beginTransaction();
		
			try {
				
				session.createQuery(updateComputer)
						.setParameter("name",cBean.getName())
						.setParameter("introduced", cBean.getIntroduced())
						.setParameter("discontinued", cBean.getDiscontinued())
						.setParameter("companyId", cBean.getCompany().getId())
						.setParameter("id", cBean.getId())
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
		
		return true;
	}
		
	public boolean deleteComputer(int id){

		try ( Session session = factory.openSession() ) {
			
			session.beginTransaction();
			
			try  {
				
				session.createQuery(deleteComputer)
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
		
		return true;
		
	}

	public void deleteComputerList(String list) {
		
		String[] buffer = list.split(",");
		String query = deleteComputers;
		
		for(int i=0; i < (buffer.length - 1); i++) {
			
			query+=":id"+i+", ";			
			
		}
		query +=" :id"+(buffer.length - 1)+" )";
		
		
		try( Session session = factory.openSession() ) {
			
			Query test = session.createQuery(query);
			
			for(int i=0; i < buffer.length; i++) {
				
				test.setParameter("id"+i, Integer.parseInt(buffer[i]));
				
			}
			
			test.executeUpdate();
			
		} catch( HibernateException e ) {
    		
    		logger.error(e.getMessage());
    		e.printStackTrace();
    		
    	}
		
	}
	
	/*            Composed Requests             */	
	
	public ArrayList<Computer> listComputer(RequestParameter parameters){
		
		List<ComputerBeanDb> listBean = new ArrayList<ComputerBeanDb>();
		String query = computerAsk+orderBy+columnComputer[parameters.getChoice()]+order[parameters.getOrder()];

		
		try( Session session = factory.openSession() ) {
					
			listBean = session.createQuery(query, ComputerBeanDb.class)
													.setParameter("searchTerm", "%"+parameters.getSearchTerm()+"%")
													.list();
			
		} catch( Exception e ) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}
		
		ArrayList<Computer> computerList = (ArrayList<Computer>) listBean.stream()
				.map(s -> mapDb.mapDAOBeanToComputer(s))
				.collect(Collectors.toList());					
		
		return(computerList);
		
	}		
	
	public int countComputer(RequestParameter parameters) {
		
		String query = countComputer;
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
	
	public ArrayList<Computer> pageComputer(Page page){
		
		RequestParameterBeanDb parameters = mapDb.mapParametersToDTOdb(page.getParameters());
		List<ComputerBeanDb> listBean = new ArrayList<ComputerBeanDb>();
		String query = computerAsk+orderBy+columnComputer[parameters.getChoice()]+order[parameters.getOrder()];

		
		try( Session session = factory.openSession() ) {
			
			listBean = session.createQuery(query,ComputerBeanDb.class)
								.setParameter("searchTerm", "%"+parameters.getSearchTerm()+"%")
								.setFirstResult(page.getStart())
								.setMaxResults(page.getTaille())
								.list();
			
			
		} catch( Exception e ) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}
		
		ArrayList<Computer> computerList = (ArrayList<Computer>) listBean.stream()
				.map(s -> mapDb.mapDAOBeanToComputer(s))
				.collect(Collectors.toList());		
		
		return(computerList);

	}		
	
	/*            Utility             */
	
	public Database getDb() {
		return db;
	}
	
}
