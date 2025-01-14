package com.purplebits.emrd2.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.ActiveUserDetailsDTO;
import com.purplebits.emrd2.dto.JwtResponse;
import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.UserDetailsDTO;
import com.purplebits.emrd2.dto.PaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.dto.request_response.UserMappedUserGroupRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.UsersRequestResponseDTO;
import com.purplebits.emrd2.entity.UserDetailsView;
import com.purplebits.emrd2.exceptions.DuplicateGroupException;
import com.purplebits.emrd2.exceptions.EmailSendingException;
import com.purplebits.emrd2.exceptions.LoginPasswordException;
import com.purplebits.emrd2.exceptions.UserAlreadyExistsException;
import com.purplebits.emrd2.exceptions.UserGroupsNotAssignedException;
import com.purplebits.emrd2.exceptions.UserGroupsNotFoundException;
import com.purplebits.emrd2.exceptions.UserNotFoundException;
import com.purplebits.emrd2.jwt.AppUserDetailService;
import com.purplebits.emrd2.jwt.JwtTokenUtil;
import com.purplebits.emrd2.jwt.UnifiedUserDetailsService;
import com.purplebits.emrd2.rsql.UserSearchRsqlVisitor;
import com.purplebits.emrd2.service.UserService;
import com.purplebits.emrd2.util.ResponseHandler;
import com.purplebits.emrd2.util.ResponseMessages;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

@RestController
public class UserController {

	private final String className = UserController.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UnifiedUserDetailsService unifiedUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AppUserDetailService userDetailService;

	@Autowired
	private Environment environment;

	@PostMapping("/addUser")
	public ResponseEntity<Response<UsersDTO>> addUser(@RequestBody Request<UsersDTO> request) {

		Response<UsersDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		try {
			UsersDTO user = userService.addUser(request.getQuery());
			response.setResult(user);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_USER_OK));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserAlreadyExistsException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Response<JwtResponse>> createAuthenticationToken(
			@RequestBody Request<UsersDTO> authenticationRequest, HttpServletRequest servletRequest) {

		Response<JwtResponse> response = new Response<>();
		UsersDTO jwt = authenticationRequest.getQuery();

		response.setRequestId(authenticationRequest.getRequestId());

		JwtResponse jwtResponse = null;
		UserDetails details = null;
		details = unifiedUserDetailsService.loadUserByUsername(jwt.getEmail());
		if (details != null) {

			try {
				jwtResponse = userDetailService.authenticateSubject(details, authenticationRequest,
						servletRequest.getRemoteAddr(), authenticationRequest.getLoggedInUser(),
						authenticationRequest.getLoggedInUserID(), authenticationRequest.getLoggedInUserType());
				response.setCode(ResponseCode.OK);
				response.setResult(jwtResponse);
				response.setMessage(environment.getProperty(ResponseMessages.ACCOUNT_LOGIN_OK));

				return new ResponseEntity<>(response, HttpStatus.OK);
			} catch (LoginPasswordException e) {
				response.setCode(ResponseCode.BAD_REQUEST);
				response.setMessage(e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			catch (UserNotFoundException e) {
				response.setCode(ResponseCode.BAD_REQUEST);
				response.setMessage(e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

		}

		response.setMessage(environment.getProperty(ResponseMessages.ACCOUNT_EXIST_ERROR));
		response.setCode(ResponseCode.BAD_REQUEST);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@PostMapping("/logout")
	public Response<?> logoutOfSystem(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody Request<UsersDTO> authenticationRequest, HttpServletRequest servletRequest) throws Exception {

		UsersDTO jwt = authenticationRequest.getQuery();
		Response<?> response = new Response<>();
		response.setRequestId(authenticationRequest.getRequestId());

		UserDetails details = unifiedUserDetailsService.loadUserByUsername(jwt.getEmail());

		if (details != null) {

			String logoutSubject = this.userDetailService.logoutSubject(authorizationHeader, authenticationRequest,
					servletRequest.getRemoteAddr(), authenticationRequest.getLoggedInUser(),
					authenticationRequest.getLoggedInUserID(), authenticationRequest.getLoggedInUserType());

			if (logoutSubject != null) {
				response.setCode(ResponseCode.ACCEPTED);
				response.setMessage(logoutSubject);
				return response;
			}

		}
		response.setCode(ResponseCode.BAD_REQUEST);
		response.setMessage(environment.getProperty(ResponseMessages.ACCOUNT_LOGOUT_ERROR));
		return response;

	}

	@PostMapping("/refresh")
	public Response<?> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody Request<UsersDTO> authenticationRequest) {
		String refreshToken = authorizationHeader;
		Response<?> response = new Response<>();

		try {
			String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
			UserDetails userDetails = unifiedUserDetailsService.loadUserByUsername(username);

			// Validate the refresh token
			if (!jwtTokenUtil.validateToken(refreshToken, userDetails)) {
				response.setCode(ResponseCode.BAD_REQUEST);
				response.setMessage(environment.getProperty(ResponseMessages.ACCOUNT_REFRESH_TOKEN_ERROR));
				return response;

			}

			// Generate a new access token
			String newAccessToken = jwtTokenUtil.generateToken(userDetails,
					authenticationRequest.getLoggedInUserType());

			if (newAccessToken != null) {
				response.setCode(ResponseCode.ACCEPTED);
				response.setMessage(newAccessToken);
				return response;
			}
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(environment.getProperty(ResponseMessages.ACCOUNT_REFRESH_TOKEN_ERROR));
			return response;

		} catch (Exception e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(environment.getProperty(ResponseMessages.ACCOUNT_REFRESH_TOKEN_ERROR));
			logger.error(className + " refreshAccessToken() invoked : " + e);
			return response;
		}
	}
	
	@PostMapping("/findUserById")
	public ResponseEntity<Response<UsersDTO>> findUserById(@RequestBody Request<UsersDTO> request) {
		Response<UsersDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());

		// Get the current class name

		logger.info(className + " findUserById() invoked with requestId: " + request.getRequestId());

		try {
			UsersDTO user = userService.findUserById(request.getQuery().getUserId());

			response.setResult(user);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.USER_FOUND));

			logger.info(className + " findUserById() succeeded for userId: " + request.getQuery().getUserId());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			logger.error(className + " findUserById() error: " + e.getMessage());
			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/updateUser")
	public ResponseEntity<Response<UsersDTO>> updateUser(@RequestBody Request<UsersDTO> request) {
		Response<UsersDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		try {
			UsersDTO updatedUser = userService.updateUser(request.getQuery());
			response.setResult(updatedUser);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_USER_OK));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			logger.error(className + " updateUser() invoked : " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
	}

	@PostMapping("/mapUserToGroups")
	public ResponseEntity<Response<UsersDTO>> mapUserToGroups(
			@Valid @RequestBody Request<UserMappedUserGroupRequestResponseDTO> request) {

		Response<UsersDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());

		try {
			UsersDTO userDTO = request.getQuery().getUser();
			List<UserGroupsDTO> userGroupsDTO = request.getQuery().getUserGroups();

			UsersDTO result = userService.mapUsersToGroups(userDTO, userGroupsDTO);

			response.setResult(result);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.USER_MAPPED_TO_GROUP_SUCCESS));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (UserAlreadyExistsException e) {
			logger.error(className + " mapUserToGroups() invoked : " + e.getMessage());
			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (UserGroupsNotFoundException e) {
			logger.error(className + " mapUserToGroups() invoked : " + e.getMessage());
			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (DuplicateGroupException e) {
			logger.error(className + " mapUserToGroups() invoked : " + e.getMessage());
			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (EmailSendingException e) {
			logger.error(className + " mapUserToGroups() invoked : " + e.getMessage());
			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/mapUsersToGroupsUpdate")
	public ResponseEntity<Response<UserMappedUserGroupRequestResponseDTO>> mapUsersToGroupsUpdate(
			@RequestBody Request<UserMappedUserGroupRequestResponseDTO> request) {

		Response<UserMappedUserGroupRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());

		try {
			UsersDTO userDTO = request.getQuery().getUser();
			List<UserGroupsDTO> userGroupsDTO = request.getQuery().getUserGroups();

			UserMappedUserGroupRequestResponseDTO result = userService.mapUsersToGroupsUpdate(userDTO, userGroupsDTO);

			response.setResult(result);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.USER_MAPPED_TO_GROUP_SUCCESS));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserGroupsNotFoundException e) {

			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			logger.error(className + " mapUsersToGroupsUpdate() invoked : " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (UserNotFoundException e) {

			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			logger.error(className + " mapUsersToGroupsUpdate() invoked : " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		 catch (UserGroupsNotAssignedException e) {

				response.setMessage(e.getMessage());
				response.setCode(ResponseCode.BAD_REQUEST);
				logger.error(className + " mapUsersToGroupsUpdate() invoked : " + e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
	}

	@DeleteMapping("/deleteUserById")
	public ResponseEntity<Response<UsersDTO>> deleteUserById(@RequestBody Request<UsersDTO> request) {
		Response<UsersDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		logger.info(className + " deleteUserById() invoked with requestId: " + request.getRequestId());
		try {
			UsersDTO user = userService.deleteUserById(request.getQuery().getUserId());
			response.setResult(user);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.DELETE_USER_OK));

			logger.info(className + " findUserById() succeeded for userId: " + request.getQuery().getUserId());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			logger.error(className + " deleteUserById() invoked : " + e.getMessage());
			response.setMessage(e.getMessage());
			response.setCode(ResponseCode.BAD_REQUEST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/getActiveUsers")
	public ResponseEntity<Response<List<UsersRequestResponseDTO>>> getActiveUsers() throws IOException {
		Response<List<UsersRequestResponseDTO>> response = new Response<>();

		List<UsersRequestResponseDTO> users = userService.getActiveUsers();
		response.setCode(ResponseCode.OK);
		response.setMessage(environment.getProperty(ResponseMessages.USER_FOUND));
		response.setResult(users);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/getUsersSearchBased") // not mention in list
	public ResponseEntity<Object> getUsersSearchBased(@RequestBody Request<PaginationWithSearchRequestDTO> req) {
		String requestId = req.getRequestId();

		PaginationWithSearchRequestDTO paginationWithSearchRequestDTO = req.getQuery();
		PaginationRequestDTO paginationRequest = paginationWithSearchRequestDTO.getPaginationRequest();
		String search = paginationWithSearchRequestDTO.getSearch();
		Integer pageNumber = 0;
		Integer pageSize = 1000;
		String sortBy = "fullName";
		String sortType = "asc";
		if (paginationRequest != null) {
			if (paginationRequest.getPageNumber() != null)
				pageNumber = paginationRequest.getPageNumber();
			if (paginationRequest.getPageSize() != null)
				pageSize = paginationRequest.getPageSize();
			if (paginationRequest.getSortBy() != null)
				sortBy = paginationRequest.getSortBy();
			if (paginationRequest.getSortType() != null)
				sortType = paginationRequest.getSortType();
		}

		search=userService.createQueryForUserSearch(search);
		
		Node rootNode = new RSQLParser().parse(search);
		Specification<UserDetailsView> spec = rootNode.accept(new UserSearchRsqlVisitor<UserDetailsView>());

		PaginationResponseDTO<ActiveUserDetailsDTO> paginationResponseDTO = this.userService
				.getUserInfoBySpecifications(pageNumber, pageSize, sortBy, sortType, spec);

		return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, paginationResponseDTO,
				requestId);

	}

	@PostMapping("/getActiveUserDetailsSearchBased")
	public ResponseEntity<Object> getActiveUserDetailsSearchBased(
			@RequestBody Request<PaginationWithSearchRequestDTO> request) {
		String requestId = request.getRequestId();

		PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
		String search = request.getQuery().getSearch();

		Integer pageNumber = 0;
		Integer pageSize = 1000;
		String sortBy = "userId";
		String sortType = "desc";
		if (paginationRequest != null) {
			if (paginationRequest.getPageNumber() != null)
				pageNumber = paginationRequest.getPageNumber();
			if (paginationRequest.getPageSize() != null)
				pageSize = paginationRequest.getPageSize();
			if (paginationRequest.getSortBy() != null)
				sortBy = paginationRequest.getSortBy();
			if (paginationRequest.getSortType() != null)
				sortType = paginationRequest.getSortType();
		}
		
		search=userService.createQueryForActiveUserSearch(search);

		Node rootNode = new RSQLParser().parse(search);
		Specification<UserDetailsView> spec = rootNode.accept(new UserSearchRsqlVisitor<UserDetailsView>());

		PaginationResponseDTO<UserDetailsDTO> users = userService.getActiveUserDetailsBysearch(pageNumber, pageSize,
				sortBy, sortType, spec);

		return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, users, requestId);

	}
	
	@PostMapping("/getAllUsersWithinTeam") // not mention in list
	public ResponseEntity<Object> getUsersOfSameTeams(@RequestBody Request<PaginationWithSearchRequestDTO> req) {
		String requestId = req.getRequestId();

		PaginationWithSearchRequestDTO paginationWithSearchRequestDTO = req.getQuery();
		PaginationRequestDTO paginationRequest = paginationWithSearchRequestDTO.getPaginationRequest();
		String search = paginationWithSearchRequestDTO.getSearch();
		Integer pageNumber = 0;
		Integer pageSize = 10000;
		String sortBy = "fullName";
		String sortType = "asc";
		if (paginationRequest != null) {
			if (paginationRequest.getPageNumber() != null)
				pageNumber = paginationRequest.getPageNumber();
			if (paginationRequest.getPageSize() != null)
				pageSize = paginationRequest.getPageSize();
			if (paginationRequest.getSortBy() != null)
				sortBy = paginationRequest.getSortBy();
			if (paginationRequest.getSortType() != null)
				sortType = paginationRequest.getSortType();
		}


		PaginationResponseDTO<UsersRequestResponseDTO> paginationResponseDTO = this.userService
				.getUsersOfSameTeams(pageNumber, pageSize, sortBy, sortType,req.getLoggedInUserID());

		return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, paginationResponseDTO,
				requestId);

	}
}
