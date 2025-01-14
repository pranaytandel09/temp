package com.purplebits.emrd2.controller;


import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
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
import com.purplebits.emrd2.dto.PaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupWithMembersRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupWithMembersResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupWithUsersResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupsRequestResponseDTO;
import com.purplebits.emrd2.entity.UserGroupDetails;
import com.purplebits.emrd2.exceptions.AdminDisplayNameChangeNotAllowedException;
import com.purplebits.emrd2.exceptions.AdminRoleChangeNotAllowedException;
import com.purplebits.emrd2.exceptions.AdminRoleDeleteNotAllowedException;
import com.purplebits.emrd2.exceptions.AdministratorUserGroupDeleteException;
import com.purplebits.emrd2.exceptions.DuplicateGroupException;
import com.purplebits.emrd2.exceptions.RoleAlreadyExistException;
import com.purplebits.emrd2.exceptions.RoleNotFoundException;
import com.purplebits.emrd2.exceptions.UserGroupDeleteException;
import com.purplebits.emrd2.exceptions.UserGroupStatusTypeException;
import com.purplebits.emrd2.exceptions.UserGroupsNotFoundException;
import com.purplebits.emrd2.exceptions.UserNotFoundException;
import com.purplebits.emrd2.rsql.UserGroupRoleSearchRsqlVisitor;
import com.purplebits.emrd2.service.UserGroupsService;
import com.purplebits.emrd2.util.ResponseHandler;
import com.purplebits.emrd2.util.ResponseMessages;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

@RestController
@RequestMapping("/userGroups")
public class UserGroupsController {
	 private final String className = UserGroupsController.class.getSimpleName();
		private static final Logger logger = LogManager.getLogger(UserGroupsController.class);
		
	@Autowired
	Environment environment;
	@Autowired
	private UserGroupsService userGroupsService;
	
	@PostMapping("/addNewUserGroup")
    public ResponseEntity<Response<UserGroupsRequestResponseDTO>> addNewUserGroup(@Valid
            @RequestBody Request<UserGroupsRequestResponseDTO> request) {

        Response<UserGroupsRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());

        UserGroupsDTO userGroupsDTO = request.getQuery().getUserGroupsDTO();
        
        try {
        	
        userGroupsDTO = userGroupsService.addNewUserGroups(userGroupsDTO);
        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.CREATE_USER_GROUP_OK));
        response.setResult(new UserGroupsRequestResponseDTO(userGroupsDTO));

        return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(DuplicateGroupException e) {
        	response.setCode(ResponseCode.BAD_REQUEST);
        	response.setMessage(e.getMessage());
        	  logger.error(className + " addNewUserGroup() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        } 
        
        catch(RoleNotFoundException e) {
        	
        	response.setCode(ResponseCode.BAD_REQUEST);
        	response.setMessage(e.getMessage());
        	 logger.error(className + " addNewUserGroup() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        } 
        catch(RoleAlreadyExistException e) {
        	
        	response.setCode(ResponseCode.BAD_REQUEST);
        	response.setMessage(e.getMessage());
        	 logger.error(className + " addNewUserGroup() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        } 
    }
	
	@PutMapping("/updateUserGroup")
    public ResponseEntity<Response<UserGroupsRequestResponseDTO>> updateUserGroup(@Valid
            @RequestBody Request<UserGroupsRequestResponseDTO> request) {
        
        Response<UserGroupsRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());

        UserGroupsDTO userGroupsDTO = request.getQuery().getUserGroupsDTO();
       
        try {
            userGroupsDTO = userGroupsService.updateUserGroups(userGroupsDTO);
            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.UPDATE_USER_GROUP_OK));
            response.setResult(new UserGroupsRequestResponseDTO(userGroupsDTO));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch ( AdminDisplayNameChangeNotAllowedException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (UserGroupStatusTypeException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( AdminRoleChangeNotAllowedException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( DuplicateGroupException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( RoleAlreadyExistException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( UserGroupsNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (RoleNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("/getUserGroupById")
    public ResponseEntity<Response<UserGroupsRequestResponseDTO>> getUserGroupById(
            @RequestBody Request<UserGroupsRequestResponseDTO> request) {

        Response<UserGroupsRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());

        UserGroupsDTO userGroupsDTO;

        try {
            userGroupsDTO = userGroupsService.getUserGroupsById(request.getQuery().getGroupId());
            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.FETCH_USER_GROUP_OK));
            response.setResult(new UserGroupsRequestResponseDTO(userGroupsDTO));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserGroupsNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " getUserGroupById() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
	
	@PostMapping("/getAllUserGroups")
    public ResponseEntity<Response<PaginationResponseDTO<UserGroupsRequestResponseDTO>>> getAllUserGroups(@RequestBody Request<PaginationRequestDTO> request) {
        Response<PaginationResponseDTO<UserGroupsRequestResponseDTO>> response = new Response<>();

        PaginationRequestDTO paginationRequest = request.getQuery();
        Integer pageNumber = 0;
        Integer pageSize = 1000;
        String sortBy = "groupName";
        String sortType = "asc";
        if (paginationRequest != null) {	        
            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
        }
        

        PaginationResponseDTO<UserGroupsRequestResponseDTO> allUserGroups = 
            userGroupsService.getAllUserGroups(pageNumber, pageSize, sortBy, sortType);
        
        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.FETCH_USER_GROUP_OK));
        response.setResult(allUserGroups);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	

	@DeleteMapping("/deleteUserGroup")
    public ResponseEntity<Response<UserGroupsRequestResponseDTO>> deleteUserGroup(@RequestBody Request<UserGroupsRequestResponseDTO> request) {
        Response<UserGroupsRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());

        Integer groupId = request.getQuery().getGroupId();

        try {
            UserGroupsDTO userGroupsDTO = userGroupsService.deleteUserGroups(groupId);
            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.DELETE_USER_GROUP_OK));
            response.setResult(new UserGroupsRequestResponseDTO(userGroupsDTO));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserGroupsNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " deleteUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (AdminRoleDeleteNotAllowedException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " deleteUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
       
    }
	
	

	@DeleteMapping("/deleteUserGroupWithMembers")
    public ResponseEntity<Response<UserGroupsRequestResponseDTO>> deleteUserGroupWithMembers(@RequestBody Request<UserGroupsRequestResponseDTO> request) {
        Response<UserGroupsRequestResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());

        Integer groupId = request.getQuery().getGroupId();

        try {
            UserGroupsDTO userGroupsDTO = userGroupsService.deleteUserGroupWithMembers(groupId);
            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.DELETE_USER_GROUP_OK));
            response.setResult(new UserGroupsRequestResponseDTO(userGroupsDTO));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserGroupsNotFoundException  e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " deleteUserGroupWithMembers() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (UserGroupDeleteException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " deleteUserGroupWithMembers() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch (AdministratorUserGroupDeleteException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " deleteUserGroupWithMembers() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
    }
	
	
	
	@PostMapping("/getAllUserGroupWithMembersAndRole")
	public ResponseEntity<Response<PaginationResponseDTO<UserGroupWithUsersResponseDTO>>> getAllUserGroupWithMembersAndRole(
	        @RequestBody Request<PaginationRequestDTO> request) {

	    Response<PaginationResponseDTO<UserGroupWithUsersResponseDTO>> response = new Response<>();
	    PaginationRequestDTO paginationRequest = request.getQuery();

	 
	    Integer pageNumber = 0;
        Integer pageSize = 1000;
        String sortBy = "groupId";
        String sortType = "asc";
        if (paginationRequest != null) {	        
            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
        }
	    
	    try {
	         PaginationResponseDTO<UserGroupWithUsersResponseDTO> allUserGroupWithMembersAndRole = userGroupsService.getAllUserGroupWithMembersAndRole(
	                pageNumber, pageSize, sortBy, sortType);

	        response.setCode(ResponseCode.OK);
	        response.setMessage(environment.getProperty(ResponseMessages.FETCH_USER_GROUP_OK));
	        response.setResult(allUserGroupWithMembersAndRole);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	    catch(UserGroupsNotFoundException e) {
	    	 response.setCode(ResponseCode.BAD_REQUEST);
	    	 response.setMessage(e.getMessage());
		        logger.error(className + " getAllUserGroupWithMembersAndRole() invoked for: "+e.getMessage());
		        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    	
	    }
	}

	

	@PostMapping("/addNewUserGroupWithMembers")
    public ResponseEntity<Response<UserGroupWithMembersResponseDTO>> addNewUserGroupWithMembers(
            @RequestBody Request<UserGroupWithMembersRequestResponseDTO> request) {

        Response<UserGroupWithMembersResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());
        UserGroupsRequestResponseDTO userGroupsRequestResponseDTO = request.getQuery().getUserGroup();
        UserGroupsDTO userGroupsDTO = userGroupsRequestResponseDTO.getUserGroupsDTO();
        
        try {
        	
        	UserGroupWithMembersResponseDTO  userGroupWithMembers = userGroupsService.
        			addNewUserGroupWithMembers(userGroupsDTO,request.getQuery().getUsers());
        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.CREATE_USER_GROUP_OK));
        response.setResult(userGroupWithMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(DuplicateGroupException e) {
        	response.setCode(ResponseCode.BAD_REQUEST);
        	response.setMessage(e.getMessage());
        	logger.error(className + " addNewUserGroupWithMembers() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        } 
        
        catch(RoleNotFoundException e) {
        	
        	response.setCode(ResponseCode.BAD_REQUEST);
        	response.setMessage(e.getMessage());
        	logger.error(className + " addNewUserGroupWithMembers() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        } 
        catch(RoleAlreadyExistException e) {
        	
        	response.setCode(ResponseCode.BAD_REQUEST);
        	response.setMessage(e.getMessage());
        	logger.error(className + " addNewUserGroupWithMembers() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        catch(UserNotFoundException e) {
        	
        	response.setCode(ResponseCode.BAD_REQUEST);
        	response.setMessage(e.getMessage());
        	logger.error(className + " addNewUserGroupWithMembers() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
       
	
    }
	
	@PostMapping("/updateUserGroupWithMembers")
    public ResponseEntity<Response<UserGroupWithMembersResponseDTO>> updateUserGroupWithMembers(
            @RequestBody Request<UserGroupWithMembersRequestResponseDTO> request) {

        Response<UserGroupWithMembersResponseDTO> response = new Response<>();
        response.setRequestId(request.getRequestId());
        UserGroupsRequestResponseDTO userGroupsRequestResponseDTO = request.getQuery().getUserGroup();
        UserGroupsDTO userGroupsDTO = userGroupsRequestResponseDTO.getUserGroupsDTO();
        
        try {
        	
        	UserGroupWithMembersResponseDTO  userGroupWithMembers = userGroupsService.
        			updateUserGroupWithMembers(userGroupsDTO,request.getQuery().getUsers());
        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.UPDATE_USER_GROUP_OK));
        response.setResult(userGroupWithMembers);

        return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoleNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());        
            logger.error(className + " updateUserGroupWithMembers() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }catch ( AdminDisplayNameChangeNotAllowedException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch (UserGroupStatusTypeException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( AdminRoleChangeNotAllowedException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( DuplicateGroupException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( RoleAlreadyExistException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch ( UserGroupsNotFoundException e) {
            response.setCode(ResponseCode.BAD_REQUEST);
            response.setMessage(e.getMessage());
            logger.error(className + " updateUserGroup() invoked for: "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
	
    }
	
	@PostMapping("/getAllUserGroupWithMembersAndRoleFilterdBased")
	public ResponseEntity<Object> getAllUserGroupWithMembersAndRoleByFilterCriteria(
	        @RequestBody Request<PaginationWithSearchRequestDTO> request) {

	    PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
	    String search = request.getQuery().getSearch();
	    String requestId = request.getRequestId();
	   
        Integer pageNumber = 0;
        Integer pageSize = 1000;
        String sortBy = "groupId";
        String sortType = "desc";
        if (paginationRequest != null) {	        
            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
        }
	    
		search=userGroupsService.createQuery(search);
		
		Node rootNode = new RSQLParser().parse(search);
		Specification<UserGroupDetails> spec = rootNode.accept(
				new UserGroupRoleSearchRsqlVisitor<UserGroupDetails>());
		
		PaginationResponseDTO<UserGroupWithUsersResponseDTO> allUserGroupWithMembersAndRole = userGroupsService.getAllUserGroupWithMembersAndRoleByFilterCriteria(
                pageNumber, pageSize, sortBy, sortType, spec);
		
		 return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK,
				 allUserGroupWithMembersAndRole, requestId);
	}
	
	
}
