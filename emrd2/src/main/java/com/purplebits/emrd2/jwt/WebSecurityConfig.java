package com.purplebits.emrd2.jwt;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UnifiedUserDetailsService unifiedUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	public static String[] publicUrls = { "/login", "/authenticate", "/swagger-resources", "/swagger-resources/**",
			"/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**",
			"/v2/api-docs/**", "/api/public/**", "/api/public/authenticate", "/actuator/*", "/swagger-ui/**", "/records/uploadExtractedData", "/**"};

//	public static String[] publicUrls = { "/login", "/authenticate", "/swagger-resources", "/swagger-resources/**",
//	"/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/v3/api-docs/**",
//	"/v2/api-docs/**", "/api/public/**", "/api/public/authenticate", "/actuator/*", "/swagger-ui/**","/siteMembers/login" };

	// @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/*
	 * public void configureGlobal(AuthenticationManagerBuilder auth) throws
	 * Exception { auth.userDetailsService(jwtUserDetailsService).passwordEncoder(
	 * passwordEncoder()); // auth.userDetailsService(jwtUserDetailsService); }
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(unifiedUserDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/* .csrf().disable()cors().disable() */
		
		http.csrf().disable().cors().disable().authorizeHttpRequests().anyRequest().authenticated().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
		.authenticationEntryPoint(this.jwtAuthenticationEntryPoint);

// Your existing security configuration for non-development profiles
//http.authorizeRequests().anyRequest().authenticated().and().exceptionHandling()
//		.authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Ignore spring security in these paths
		web.ignoring().antMatchers(publicUrls);
	}

//	@Bean
//	public CorsFilter corsFilter() {
//		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		final CorsConfiguration config = new CorsConfiguration();
//		config.setAllowCredentials(true);
//		// Don't do this in production, use a proper list of allowed origins
//		config.setAllowedOrigins(Collections.singletonList("*"));
//		config.addAllowedOrigin("*");
//		config.setAllowedOriginPatterns(Collections.singletonList("*"));
//		config.setAllowedHeaders(Arrays.asList("Authorization", "Access-Control-Expose-Headers"));
//		config.setExposedHeaders(Arrays.asList("Authorization"));
//
//		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
//		source.registerCorsConfiguration("/**", config);
//		return new CorsFilter(source);
//	}

}
