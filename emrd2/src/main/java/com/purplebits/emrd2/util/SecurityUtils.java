package com.purplebits.emrd2.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityUtils {

	public static List<String> getUserRoles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ResponseMessages.UNAUTHORIZED);
		}
		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

	}

	public static Authentication getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ResponseMessages.UNAUTHORIZED);
		}
		return authentication;
	}
}
