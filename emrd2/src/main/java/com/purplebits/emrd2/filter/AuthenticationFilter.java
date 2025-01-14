package com.purplebits.emrd2.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.purplebits.emrd2.util.TokenUtil;



public class AuthenticationFilter implements Filter {

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	private static final String REFRESH_TOKEN = "refreshToken";
	private static final String LOGOUT_REQUEST = "logout";
	private static final Logger logger = LogManager.getLogger(AuthenticationFilter.class);
	private List<String> allowedUrls = new ArrayList<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		allowedUrls.add("/login");
		allowedUrls.add("/swagger-ui.html");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

		// Request Path
		String requestPath = httpRequest.getServletPath();

		boolean validRequest = false;
		for (String url : allowedUrls) {
			if (url.equals(requestPath)) {
				validRequest = true;
			}
		}

		if (requestPath.contains("api-docs") || requestPath.endsWith(".css") || requestPath.endsWith(".js")
				|| requestPath.matches("(.*).woff(.?)") || requestPath.endsWith("websocket")) {
			validRequest = true;
		}

		if (!validRequest) {

			// Fetch Authorization header from headers
			String authorization = httpRequest.getHeader(AUTHORIZATION_PROPERTY);
			if (authorization == null) {
				authorization = servletRequest.getParameter("token");
				if (authorization == null) {
					String referer = httpRequest.getHeader("Referer");
					if (referer != null) {
						int tokenStartIndex = referer.indexOf('=');
						authorization = URLDecoder.decode(referer.substring(tokenStartIndex + 1), "UTF-8");
					}
				}
			}

			if (authorization == null) {
				httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
				PrintWriter output = httpResponse.getWriter();
				output.write("Your Session has Expired Login Again.");
				output.close();
				return;
			} else {
				TokenUtil tokenUtil = new TokenUtil();
				// Extract token for validation
				final String token = authorization.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
				// Validate User
				Map<String, Object> validatedMap = tokenUtil.isValid(token);
				if (validatedMap == null) {
					httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
					PrintWriter output = httpResponse.getWriter();
					output.write("Your Session has Expired Login Again.");
					output.close();
					return;
				} else if (!LOGOUT_REQUEST.equals(requestPath)) {
//					logger.debug("Logged in User : " + validatedMap.get("userLogin"));
					tokenUtil.generateRefreshToken(validatedMap, httpResponse);

					if (REFRESH_TOKEN.equals(requestPath)) {
						httpResponse.setStatus(HttpStatus.OK.value());
						PrintWriter output = httpResponse.getWriter();
						output.write("Refresh Token generated Successfully.");
						output.close();
						return;
					}
				} else if (LOGOUT_REQUEST.equals(requestPath)) {
					httpResponse.setStatus(HttpStatus.OK.value());
					PrintWriter output = httpResponse.getWriter();
					output.write("Logout Successfully.");
					output.close();
					return;
				} else {
					httpResponse.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "This Service is not available.");
					return;
				}
			}

		}

		httpResponse.addHeader("Access-Control-Expose-Headers", AUTHORIZATION_PROPERTY);
		chain.doFilter(httpRequest, httpResponse);

	}

	
	@Override
	public void destroy() {
		// Noncompliant - method is empty
	}
}
