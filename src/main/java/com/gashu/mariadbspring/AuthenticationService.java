package com.gashu.mariadbspring;
import java.util.Arrays;

import com.gashu.mariadbspring.model.UserInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class AuthenticationService implements UserDetailsService {
	@Autowired
	private UserDAO userDAO;
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserInfo userInfo = userDAO.getUserInfo(username);
		GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRole());
		UserDetails userDetails = (UserDetails)new User(userInfo.getUsername(), 
				userInfo.getPassword(), Arrays.asList(authority));
		return userDetails;
	}
} 
