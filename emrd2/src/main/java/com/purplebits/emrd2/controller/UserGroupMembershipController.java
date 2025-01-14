package com.purplebits.emrd2.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.UserGroupMembershipDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupMembershipRequestResponseDTO;
import com.purplebits.emrd2.exceptions.UserGroupMembershipNotFoundException;
import com.purplebits.emrd2.exceptions.UserGroupsNotFoundException;
import com.purplebits.emrd2.service.UserGroupMembershipService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/userGroupMembership")
public class UserGroupMembershipController {
	  private final String className = UserGroupMembershipController.class.getSimpleName();
		private static final Logger logger = LogManager.getLogger(UserGroupMembershipController.class);
		@Autowired
		Environment environment;
		
    @Autowired
    private UserGroupMembershipService userGroupMembershipService;

    @PostMapping("/addUserMembership")
    public ResponseEntity<Response<UserGroupMembershipRequestResponseDTO>> addUserMembership(
            @Valid @RequestBody Request<UserGroupMembershipRequestResponseDTO> request) {

    	 
        Response<UserGroupMembershipRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());
        
       
        try {
        UserGroupMembershipDTO userGroupMembershipDTO = request.getQuery().getUserGroupMembershipDTO();
        userGroupMembershipDTO = userGroupMembershipService.addUserMembership(userGroupMembershipDTO);

        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.CREATE_USER_GROUP_MEMBERSHIP_OK));
        response.setResult(new UserGroupMembershipRequestResponseDTO(userGroupMembershipDTO));

        return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(UserGroupsNotFoundException t) {
          	 response.setCode(ResponseCode.BAD_REQUEST);
          	 response.setMessage(t.getMessage());
               logger.error(className + " addUserMembership() invoked for: "+t.getMessage());
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
          }
        
    }

    @PutMapping("/updateUserMembership")
    public ResponseEntity<Response<UserGroupMembershipRequestResponseDTO>> updateUserMembership(
            @Valid @RequestBody Request<UserGroupMembershipRequestResponseDTO> request) {

        Response<UserGroupMembershipRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());
        
        UserGroupMembershipDTO userGroupMembershipDTO = request.getQuery().getUserGroupMembershipDTO();

        try {
        	
           userGroupMembershipDTO = userGroupMembershipService.updateUserMembership(userGroupMembershipDTO);

            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.UPDATE_USER_GROUP_MEMBERSHIP_OK));
            response.setResult(new UserGroupMembershipRequestResponseDTO(userGroupMembershipDTO));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (UserGroupMembershipNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserMembership() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getUserMembershipById")
    public ResponseEntity<Response<UserGroupMembershipRequestResponseDTO>> getUserMembershipById(
            @RequestBody Request<UserGroupMembershipRequestResponseDTO> request) {

        Response<UserGroupMembershipRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());
        
        UserGroupMembershipDTO userGroupMembershipDTO;

        try {
          userGroupMembershipDTO = userGroupMembershipService
                    .getUserMembershipById(request.getQuery().getMembershipId());

            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.FETCH_USER_GROUP_MEMBERSHIP_OK));
            response.setResult(new UserGroupMembershipRequestResponseDTO(userGroupMembershipDTO));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (UserGroupMembershipNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " getUserMembershipById() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/getAllUserMemberships")
    public ResponseEntity<Response<PaginationResponseDTO<UserGroupMembershipRequestResponseDTO>>> getAllUserMemberships(
            @RequestBody Request<PaginationRequestDTO> request) {

        Response<PaginationResponseDTO<UserGroupMembershipRequestResponseDTO>> response = new Response<>();
        PaginationRequestDTO paginationRequest = request.getQuery();
        Integer pageNumber = 0;
        Integer pageSize = 1000;
        String sortBy = "membershipId";
        String sortType = "asc";
        if (paginationRequest != null) {	        
            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
        }

        PaginationResponseDTO<UserGroupMembershipRequestResponseDTO> memberships = userGroupMembershipService.getAllUserGroupMemberShip(
                pageNumber, pageSize, sortBy, sortType);

        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.FETCH_USER_GROUP_MEMBERSHIP_OK));
        response.setResult(memberships);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserMembershipById")
    public ResponseEntity<Response<UserGroupMembershipRequestResponseDTO>> deleteUserMembershipById(
            @RequestBody Request<UserGroupMembershipRequestResponseDTO> request) {

        Response<UserGroupMembershipRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());

        try {
        	UserGroupMembershipDTO userGroupMembershipDTO =  userGroupMembershipService.deleteUserGroupMembershipById(request.getQuery().getMembershipId());

            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.DELETE_USER_GROUP_MEMBERSHIP_OK));
            response.setResult(new UserGroupMembershipRequestResponseDTO(userGroupMembershipDTO));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (UserGroupMembershipNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " deleteUserMembershipById() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
 
   


}