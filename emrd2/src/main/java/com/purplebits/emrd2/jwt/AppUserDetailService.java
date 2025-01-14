package com.purplebits.emrd2.jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.purplebits.emrd2.dto.JwtResponse;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.entity.CustomUserDetails;
import com.purplebits.emrd2.entity.Roles;
import com.purplebits.emrd2.entity.Users;
import com.purplebits.emrd2.exceptions.LoginPasswordException;
import com.purplebits.emrd2.exceptions.UserNotFoundException;
import com.purplebits.emrd2.repositories.AppUserRepository;
import com.purplebits.emrd2.service.RolesService;
import com.purplebits.emrd2.service.UserGroupMembershipService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class AppUserDetailService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AppUserRepository userRepository;
	
	@Autowired
	private UserGroupMembershipService userGroupMembershipService;
	@Autowired
	private RolesService rolesService;

	@Autowired
	private Environment environment;

	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		List<Status> statuses = Arrays.asList(Status.ACTIVE, Status.PRE_APPROVED, Status.DELETED);
		Users loggedInUser = userRepository.findByEmailAndStatus(email, statuses);
		if (loggedInUser == null)
			return null;
		List<GrantedAuthority> authorities = new ArrayList<>();
		List<Roles> roles = userGroupMembershipService.findRolesByUserId(loggedInUser.getUserId());
		for (Roles role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		return new CustomUserDetails(loggedInUser, authorities);

	}

	public JwtResponse authenticateSubject(UserDetails details, Request<UsersDTO> auth, String remoteAddr,
			String loggedInUser, Integer loggedInUserID, String loggedInUserType) {

		UsersDTO jwt = auth.getQuery();
		JwtResponse jwtResponse = new JwtResponse();

		if (jwt.getPassword() == null || jwt.getPassword().isEmpty())
			throw new LoginPasswordException(environment.getProperty(ResponseMessages.ACCOUNT_LOGIN_ERROR));

		// Check if details is an instance of CustomUserDetails
		if (details instanceof CustomUserDetails) {
			CustomUserDetails customDetails = (CustomUserDetails) details;
			Users user = customDetails.getUser();
			if (user.getStatus() == Status.DELETED)
				throw new UserNotFoundException(environment.getProperty(ResponseMessages.ACCOUNT_EXIST_ERROR));
			// Password matching logic
			if (passwordEncoder.matches(jwt.getPassword(), details.getPassword())) {

				final String token = jwtTokenUtil.generateToken(details, loggedInUserType).substring(7);
				final String refreshToken = jwtTokenUtil.generateRefreshToken(details, loggedInUserType).substring(7);
				UsersDTO mappedUserDTO = ObjectMapperUtils.map(user, UsersDTO.class);
				
				jwtResponse.setToken(token);
				jwtResponse.setRefershToken(refreshToken);
				 jwtResponse.setPermissions(rolesService.getPermissionsDetails(mappedUserDTO.getUserId()));
				// Map Users to UsersDTO for the response
				
				jwtResponse.setLogedInUser(mappedUserDTO);
				// jwtResponse.setPermissions(rolesService.getPermissionsDetails(mappedUserDTO.getUserId()));

				return jwtResponse;
			} else {
				throw new LoginPasswordException(environment.getProperty(ResponseMessages.ACCOUNT_LOGIN_ERROR));
			}
		}

		return null;
	}

	public String logoutSubject(String token, Request<UsersDTO> auth, String ipAddress, String loggedInUser,
			Integer loggedInUserID, String loggedInUserType) {
		if (token != null && token.startsWith("Bearer ")) {
			String jwtToken = token.substring(7);

			return "Logged out successfully";
		}
		return null;
	}
}
