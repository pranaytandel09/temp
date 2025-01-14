package com.purplebits.emrd2.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.PaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.request_response.RolesRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RolesWithPermissionRequestRequestDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupsResponseDTO;
import com.purplebits.emrd2.entity.UserGroupDetails;
import com.purplebits.emrd2.exceptions.AdminRoleDeleteNotAllowedException;
import com.purplebits.emrd2.exceptions.DuplicateRoleException;
import com.purplebits.emrd2.exceptions.PermissionNotFoundException;
import com.purplebits.emrd2.exceptions.RoleDeletionException;
import com.purplebits.emrd2.exceptions.RoleNotFoundException;
import com.purplebits.emrd2.rsql.UserGroupRoleSearchRsqlVisitor;
import com.purplebits.emrd2.service.RolesService;
import com.purplebits.emrd2.util.ResponseHandler;
import com.purplebits.emrd2.util.ResponseMessages;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;



@RestController
@RequestMapping("/roles")
public class RoleController {
	  private final String className = RoleController.class.getSimpleName();
		private static final Logger logger = LogManager.getLogger(RoleController.class);
	@Autowired
	RolesService rolesService;
	@Autowired
	private Environment environment;
	@PostMapping("/addOrUpdateRole")
    public ResponseEntity<Response<RolesWithPermissionRequestRequestDTO>> addOrUpdateRole(@Valid @RequestBody Request<RolesWithPermissionRequestRequestDTO> request) throws IOException, ParseException {
        Response<RolesWithPermissionRequestRequestDTO> response = new Response<>();
         RolesRequestResponseDTO rolesRequestResponseDTO = request.getQuery().getRoles();
         RolesDTO rolesDTO = rolesRequestResponseDTO.getRolesDTO();

        try {       
        	RolesWithPermissionRequestRequestDTO rolesWithPermissionRequestRequestDTO = rolesService.addOrUpdateRole(rolesDTO, request.getQuery().getPermissions());
            response.setCode(ResponseCode.OK);
            response.setMessage(environment.getProperty(ResponseMessages.CREATE_ROLES_PERMISSIONS_OK));
            response.setResult(rolesWithPermissionRequestRequestDTO);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } 
        catch(PermissionNotFoundException  e) {
        	  response.setMessage(e.getMessage());
        	logger.error(className + " addOrUpdateRole() invoked for: "+e.getMessage());
        	 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        catch(DuplicateRoleException e) {
      	  response.setMessage(e.getMessage());
      	logger.error(className + " addOrUpdateRole() invoked for: "+e.getMessage());
      	 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
      }
            catch(RoleNotFoundException e) {
            	  response.setMessage(e.getMessage());
            	logger.error(className + " addOrUpdateRole() invoked for: "+e.getMessage());
            	 return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
    }
	
	
	 @PostMapping("/getRoleDetailsWithPermissions")
	    public ResponseEntity<Response<RolesWithPermissionRequestRequestDTO>> getRoleDetailsWithPermissions(@RequestBody Request<RolesRequestResponseDTO> request){
	        Response<RolesWithPermissionRequestRequestDTO> response = new Response<>();
	        try {       
	           
	        	RolesWithPermissionRequestRequestDTO roleWithPermissionDetailsDTO = rolesService.
	        			getRoleDetailsWithPermissions(request.getQuery().getRoleId());
	            response.setCode(ResponseCode.OK);
	            response.setMessage(environment.getProperty(ResponseMessages.FETCH_ROLES_OK));
	            response.setResult(roleWithPermissionDetailsDTO);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch (RoleNotFoundException e) {
	            response.setCode(ResponseCode.BAD_REQUEST);
	            response.setMessage(e.getMessage());
	            logger.error(className + " getRoleDetailsWithPermissions() invoked for: "+e.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	    }
	 @DeleteMapping("/deleteRole")
	    public ResponseEntity<Response<RolesRequestResponseDTO>> deleteRole(@RequestBody Request<RolesRequestResponseDTO> request) {
	        Response<RolesRequestResponseDTO> response = new Response<>();
	        response.setRequestId(request.getRequestId());
	        try {
	            Integer roleId = request.getQuery().getRoleId();
	            RolesDTO rolesDTO = rolesService.deleteRole(roleId);
	            response.setCode(ResponseCode.DELETED_OK);
	            response.setMessage(environment.getProperty(ResponseMessages.DELETE_ROLES_OK));
	            response.setResult(new RolesRequestResponseDTO(rolesDTO));
	            return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch (RoleNotFoundException e) {
	            response.setCode(ResponseCode.BAD_REQUEST);
	            response.setMessage(e.getMessage());
	            logger.error(className + " deleteRole() invoked for: "+e.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	        catch (AdminRoleDeleteNotAllowedException e) {
	            response.setCode(ResponseCode.BAD_REQUEST);
	            response.setMessage(e.getMessage());
	            logger.error(className + " deleteRole() invoked for: "+e.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	        catch ( RoleDeletionException e) {
	            response.setCode(ResponseCode.BAD_REQUEST);
	            response.setMessage(e.getMessage());
	            logger.error(className + " deleteRole() invoked for: "+e.getMessage());
	            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	        }
	    }
	 
	 @PostMapping("/getUsersGroups")
	    public ResponseEntity<Response<PaginationResponseDTO<UserGroupsResponseDTO>>> getUsersGroups(
	            @RequestBody Request<PaginationRequestDTO> request){
	        Response<PaginationResponseDTO<UserGroupsResponseDTO>> response = new Response<>();
	        response.setRequestId(request.getRequestId());
	        PaginationRequestDTO paginationRequest = request.getQuery();
	        Integer pageNumber = 0;
	        Integer pageSize = 100;
	        String sortBy = "roleId";
	        String sortType = "desc";
	        if (paginationRequest != null) {	        
	            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
	            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
	            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
	            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
	        }
	        
	            PaginationResponseDTO<UserGroupsResponseDTO> users = rolesService.getUsersGroups(
	                    pageNumber, pageSize, sortBy, sortType);
	                        
	            response.setCode(ResponseCode.OK);
	            response.setMessage(environment.getProperty(ResponseMessages.FETCH_USER_GROUP_OK));
	            response.setResult(users);
	            return new ResponseEntity<>(response, HttpStatus.OK);
	       
	    }
	 
	 @PostMapping("/getRoleDetailsSearchBased")
	    public ResponseEntity<Object> getRoleDetailsByUserSearchBased(
	            @RequestBody Request<PaginationWithSearchRequestDTO> request){
	        
	        String requestId = request.getRequestId();

	        PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
	        String search = request.getQuery().getSearch();
	        
	        StringBuilder actualQuery = new StringBuilder();

	      
	        Integer pageNumber = 0;
	        Integer pageSize = 100;
	        String sortBy = "roleId";
	        String sortType = "desc";
	        if (paginationRequest != null) {	        
	            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
	            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
	            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
	            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
	        }
	              
	        
	        if (search != null && !search.isEmpty()) {
				
				if (search.contains(",")) {
					List<String> userInputList = Arrays.asList(search.split("\\s*,\\s*"));
					
					actualQuery.append("(");
					for (int i = 0; i < userInputList.size(); i++) {
						if (i == userInputList.size() - 1) {
							actualQuery.append("displayName ==" + "'*" + userInputList.get(i)).append("*'").append(")").append(",");
						} else {
							actualQuery.append("displayName ==" + "'*" + userInputList.get(i)).append("*'").append(",");
						}

					}
					
					actualQuery.append("(");
					for (int i = 0; i < userInputList.size(); i++) {
						if (i == userInputList.size() - 1) {
							actualQuery.append("roleName ==" + "'*" + userInputList.get(i)).append("*'").append(")").append(";");
						} else {
							actualQuery.append("roleName ==" + "'*" + userInputList.get(i)).append("*'").append(",");
						}

					}

				} else {
					actualQuery.append("(displayName ==" + "'*" + search + "*'").append(")").append(",");
					actualQuery.append("(roleName ==" + "'*" + search + "*'").append(")").append(";");
				}
		}
//			actualQuery.append("groupStatus ==" + "'*ACTIVE*'").append(";");
			actualQuery.append("roleStatus ==" + "'*ACTIVE*'").append(";");
			if (actualQuery.charAt(actualQuery.length() - 1) == ';'
					|| actualQuery.charAt(actualQuery.length() - 1) == ',') {
				search = actualQuery.substring(0, actualQuery.length() - 1);
			} else {
				search = actualQuery.toString();
			}
			
			Node rootNode = new RSQLParser().parse(search);
			Specification<UserGroupDetails> spec = rootNode.accept(
					new UserGroupRoleSearchRsqlVisitor<UserGroupDetails>());

	        
	            PaginationResponseDTO<UserGroupsResponseDTO> users = rolesService.getRoleDetailsSearchBased(
	                    pageNumber, pageSize, sortBy, sortType, spec);
	                        
	            return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK,
	            		users, requestId);	       
	    }
	
	
}
