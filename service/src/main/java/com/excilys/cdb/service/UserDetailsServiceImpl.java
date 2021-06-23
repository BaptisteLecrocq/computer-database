package com.excilys.cdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.UserDAO;
import com.excilys.cdb.exception.NotFoundException;
import com.excilys.cdb.model.MyUserDetails;
import com.excilys.cdb.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserDAO userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user;
		
		try {
			
			user = userDao.getUserByName(username);
			
		} catch (NotFoundException e) {
			
			e.printStackTrace();
			throw new UsernameNotFoundException(e.getMessage());
			
		}
		
		return new MyUserDetails(user);
	}

}
