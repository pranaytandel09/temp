package com.purplebits.emrd2.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CORSFilter implements Filter {

	private static final String AUTHORIZATION_PROPERTY = "Authorization";

	public CORSFilter() {
		// Noncompliant - method is empty
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT,DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Authorization, Role, Origin, Content-Type, Accept, X-Requested-With, remember-me");

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.addHeader("Access-Control-Expose-Headers", AUTHORIZATION_PROPERTY);
			return;
		}
		chain.doFilter(req, res);

	}

	
	@Override
	public void init(FilterConfig filterConfig){
		// Noncompliant - method is empty
	}

	
	@Override
	public void destroy() {
		// Noncompliant - method is empty
	}

}