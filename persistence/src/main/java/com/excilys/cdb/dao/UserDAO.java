package com.excilys.cdb.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.beans.UserBean;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.mapper.MapperDTOdb;
import com.excilys.cdb.model.User;

@Repository
public class UserDAO {
	
	private final static String getUserByName = "FROM UserBean as u WHERE u.username= :username";
	
	private Database db;
	private MapperDTOdb mapDb;
	private SessionFactory factory;
	
	private static Logger logger = LoggerFactory.getLogger(UserDAO.class);
	
	public UserDAO(Database db, MapperDTOdb mapDb, SessionFactory factory) {
		
		this.db = db;
		this.mapDb = mapDb;
		this.factory = factory;
		
	}
	
	
	public void addUser(User user) {
		
		UserBean uBean = mapDb.mapUserModelToDTOdb(user);
		
		try( Session session = factory.openSession() ) {
			
			session.save(uBean);
			
		} catch (HibernateException e) {
			
			logger.error(e.getMessage());
			e.printStackTrace();
			
		}
		
		
	}
	
	public User getUserByName(String name) throws NotFoundException {
		
		UserBean uBean;
		System.out.println(name);
	    
    	try ( Session session = factory.openSession() ){
    		
    		uBean = session.createQuery(getUserByName, UserBean.class)
    										.setParameter("username", name)
    										.getSingleResult();
    		
    		System.out.println(uBean);
    	
    	} catch( HibernateException e ) {
    		
    		logger.error(e.getMessage());
    		e.printStackTrace();
    		throw new NotFoundException("User not found");
    		
    	}
    	
    	User user = mapDb.mapDAOBeanToUser(uBean);
		
		return(user);
		
	}
	
	

}
