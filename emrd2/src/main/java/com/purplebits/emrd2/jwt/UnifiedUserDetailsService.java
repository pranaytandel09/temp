package com.purplebits.emrd2.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UnifiedUserDetailsService implements UserDetailsService {

	@Autowired
	private AppUserDetailService userDetailService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return userDetailService.loadUserByUsername(email);
	}
}
