package com.EmployeeSystem.Employee_Management_System.Services;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.EmployeeSystem.Employee_Management_System.Repositories.UserRepository;




@Service
public class UserDetailsImp implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public UserDetailsImp(UserRepository userRepository) {
		this.userRepository=userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("User not found"));
	}
      
	
}
