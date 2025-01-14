package com.purplebits.emrd2.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


	@Autowired
	private UnifiedUserDetailsService unifiedUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		final String className = JwtRequestFilter.class.getSimpleName();
		final Logger logger = LogManager.getLogger(JwtRequestFilter.class);

		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String userType = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);

			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				//userType = jwtTokenUtil.getUserTypeFromToken(jwtToken);
			} catch (IllegalArgumentException | ExpiredJwtException e) {

				logger.error(className+"doFilterInternal()"+e.getMessage());
			} catch (Exception e) {
				logger.error(className+"doFilterInternal()"+e.getMessage());
				
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.unifiedUserDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

				HttpServletResponse httpServletResponse = (HttpServletResponse) response;

				httpServletResponse.setHeader("Authorization",
						jwtTokenUtil.generateRefreshToken(userDetails, userType).substring(7));
				httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");

			}
		}
		chain.doFilter(request, response);
	}

}
