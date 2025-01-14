package com.purplebits.emrd2.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
import com.purplebits.emrd2.dto.RolePermissionsDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.request_response.RolePermissionsRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RolesRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RolesWithPermissionRequestRequestDTO;
import com.purplebits.emrd2.exceptions.DuplicateRoleException;
import com.purplebits.emrd2.exceptions.PermissionNotFoundException;
import com.purplebits.emrd2.exceptions.RoleDeletionException;
import com.purplebits.emrd2.exceptions.RoleNotFoundException;
import com.purplebits.emrd2.exceptions.RolePermissionNotFoundException;
import com.purplebits.emrd2.service.RolePermissionsService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/rolePermission")
public class RolePermissionsController {
	  private final String className = RolePermissionsController.class.getSimpleName();
			private static final Logger logger = LogManager.getLogger(RolePermissionsController.class);
			
	@Autowired
	RolePermissionsService rolePermissionsService;
	@Autowired
	private Environment environment;
	@PostMapping("/addNewRolePermission")
    public ResponseEntity<Response<RolePermissionsRequestResponseDTO>> addNewRolePermission(
            @RequestBody Request<RolePermissionsRequestResponseDTO> request) {
		 Response<RolePermissionsRequestResponseDTO> response = new Response<>();
	        response.setRequestId(request.getRequestId());

	        RolePermissionsDTO rolePermissionsDTO = request.getQuery().getRolePermissionsDTO();
	        
	        try {
	        	
	        rolePermissionsDTO = rolePermissionsService.addNewRolePermissions(rolePermissionsDTO);
	        response.setCode(ResponseCode.OK);
	        response.setMessage(environment.getProperty(ResponseMessages.CREATE_ROLE_PERMISSION_OK));
	        response.setResult(new RolePermissionsRequestResponseDTO(rolePermissionsDTO));

	        return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch(RoleNotFoundException e) {
	        	response.setCode(ResponseCode.BAD_REQUEST);
	        	  response.setMessage(e.getMessage());
	        	logger.error(className + " addNewRolePermission() invoked for: "+e.getMessage());
	        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	        }
			   catch(PermissionNotFoundException e) {
		      	response.setCode(ResponseCode.BAD_REQUEST);
		      	  response.setMessage(e.getMessage());
		      	logger.error(className + " addNewRolePermission() invoked for: "+e.getMessage());
		      	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		      }     
    }
	
	@PutMapping("/updateRolePermissions")
    public ResponseEntity<Response<RolePermissionsRequestResponseDTO>> updateRolePermissions(
            @RequestBody Request<RolePermissionsRequestResponseDTO> request) {
		 Response<RolePermissionsRequestResponseDTO> response = new Response<>();
	        response.setRequestId(request.getRequestId());

	        RolePermissionsDTO rolePermissionsDTO = request.getQuery().getRolePermissionsDTO();
	        
	        try {
	        	
	        rolePermissionsDTO = rolePermissionsService.updateRolePermissions(rolePermissionsDTO);
	        response.setCode(ResponseCode.OK);
	        response.setMessage(environment.getProperty(ResponseMessages.UPDATE_ROLE_PERMISSION_OK));
	        response.setResult(new RolePermissionsRequestResponseDTO(rolePermissionsDTO));

	        return new ResponseEntity<>(response, HttpStatus.OK);
	        } catch(RoleNotFoundException e) {
	        	response.setCode(ResponseCode.BAD_REQUEST);
	        	  response.setMessage(e.getMessage());
	        	logger.error(className + " updateRolePermissions() invoked for: "+e.getMessage());
	        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	        }
			   catch(PermissionNotFoundException e) {
		      	response.setCode(ResponseCode.BAD_REQUEST);
		      	  response.setMessage(e.getMessage());
		      	logger.error(className + " updateRolePermissions() invoked for: "+e.getMessage());
		      	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		      }   
	        catch(RolePermissionNotFoundException e) {
				response.setCode(ResponseCode.BAD_REQUEST);
		      	  response.setMessage(e.getMessage());
		      	logger.error(className + " updateRolePermissions() invoked for: "+e.getMessage());
		      	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		 }
	        catch( RoleDeletionException e) {
				response.setCode(ResponseCode.BAD_REQUEST);
		      	  response.setMessage(e.getMessage());
		      	logger.error(className + " updateRolePermissions() invoked for: "+e.getMessage());
		      	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		 }
	       
    }
	
	
	
	@PostMapping("/getRolePermissionById")
	  public ResponseEntity<Response<RolePermissionsRequestResponseDTO>> getRolePermissionById(
	            @RequestBody Request<RolePermissionsRequestResponseDTO> request) {
			 Response<RolePermissionsRequestResponseDTO> response = new Response<>();
			 response.setRequestId(request.getRequestId());
			 try {
				 RolePermissionsDTO rolePermissionsDTO=rolePermissionsService.getRolePermissionsById(request.getQuery().getRolePermissionId());
				 response.setResult(new RolePermissionsRequestResponseDTO(rolePermissionsDTO));
				 response.setCode(ResponseCode.OK);
				 response.setMessage(environment.getProperty(ResponseMessages.FETCH_ROLE_PERMISSION_OK));
				 return new ResponseEntity<>(response, HttpStatus.OK);
			 }
			 catch(RolePermissionNotFoundException e) {
					response.setCode(ResponseCode.BAD_REQUEST);
			      	  response.setMessage(e.getMessage());
			      	logger.error(className + " getRolePermissionById() invoked for: "+e.getMessage());
			      	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
			 }
	  }
	@DeleteMapping("/deleteRolePermissionById")
	  public ResponseEntity<Response<RolePermissionsRequestResponseDTO>> deleteRolePermissionById(
	            @RequestBody Request<RolePermissionsRequestResponseDTO> request) {
			 Response<RolePermissionsRequestResponseDTO> response = new Response<>();
			 response.setRequestId(request.getRequestId());
			 try {
				 RolePermissionsDTO rolePermissionsDTO=rolePermissionsService.deleteRolePermissions(request.getQuery().getRolePermissionId());
				 response.setResult(new RolePermissionsRequestResponseDTO(rolePermissionsDTO));
				 response.setCode(ResponseCode.OK);
				 response.setMessage(environment.getProperty(ResponseMessages.DELETE_ROLE_PERMISSION_OK));
				 return new ResponseEntity<>(response, HttpStatus.OK);
			 }
			 catch(RolePermissionNotFoundException e) {
					response.setCode(ResponseCode.BAD_REQUEST);
			      	  response.setMessage(e.getMessage());
			      	logger.error(className + " deleteRolePermissionById() invoked for: "+e.getMessage());
			      	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
			 }
	  }
	@PostMapping("/getAllRolePermissionsByRoleId")
	  public ResponseEntity<Response<List<RolePermissionsRequestResponseDTO>>> getAllRolePermissionsByRoleId(
	            @RequestBody Request<RolePermissionsRequestResponseDTO> request) {
			 Response<List<RolePermissionsRequestResponseDTO>> response = new Response<>();
			 response.setRequestId(request.getRequestId());
			 try {
				 List<RolePermissionsDTO> rolePermissionsDTOs=rolePermissionsService.getAllRolePermissionsById(request.getQuery().getRoleId());
				 List<RolePermissionsRequestResponseDTO> rolePermissionsResponseDTOs = 
			                RolePermissionsRequestResponseDTO.getRolePermissionsRequestResponseDTO(rolePermissionsDTOs);
				 response.setResult(rolePermissionsResponseDTOs);
				 response.setCode(ResponseCode.OK);
				 response.setMessage(environment.getProperty(ResponseMessages.FETCH_ROLE_PERMISSION_OK));
				 return new ResponseEntity<>(response, HttpStatus.OK);
			 }
			 catch(RoleNotFoundException e) {
					response.setCode(ResponseCode.BAD_REQUEST);
			      	  response.setMessage(e.getMessage());
			      	logger.error(className + " getRolePermissionById() invoked for: "+e.getMessage());
			      	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
			 }
	  }
	
	@PostMapping("/getAllRolePermissions")
	public ResponseEntity<Response<PaginationResponseDTO<RolePermissionsRequestResponseDTO>>> getAllRolePermissions(@RequestBody Request<PaginationRequestDTO> request) {
        Response<PaginationResponseDTO<RolePermissionsRequestResponseDTO>> response = new Response<>();
        PaginationRequestDTO paginationRequest = request.getQuery();
        Integer pageNumber = 0;
        Integer pageSize = 100;
        String sortBy = "status";
        String sortType = "asc";
        if (paginationRequest != null) {	        
            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
        }
        PaginationResponseDTO<RolePermissionsRequestResponseDTO> allRolePermissions = 
            rolePermissionsService.getAllRolePermissions(pageNumber, pageSize, sortBy, sortType);     
        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.FETCH_ROLE_PERMISSION_OK));
        response.setResult(allRolePermissions);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@PostMapping("/addOrUpdateRoleWithPermissions")
	public ResponseEntity<Response<List<RolePermissionsRequestResponseDTO>>> addOrUpdateRoleWithPermissions(
	        @Valid @RequestBody Request<List<RolePermissionsRequestResponseDTO>> request) {
	    Response<List<RolePermissionsRequestResponseDTO>> response = new Response<>();

	    try {
	        List<RolePermissionsDTO> rolePermissionsDTOs = new ArrayList();
	        for (RolePermissionsRequestResponseDTO requestDTO : request.getQuery()) {
	            rolePermissionsDTOs.add(requestDTO.getRolePermissionsDTO());
	        }
	        List<RolePermissionsDTO> updatedRolePermissionsDTOs = rolePermissionsService.addOrUpdateRoleWithPermissions(rolePermissionsDTOs);
	        List<RolePermissionsRequestResponseDTO> responseDTOs = RolePermissionsRequestResponseDTO
	                .getRolePermissionsRequestResponseDTO(updatedRolePermissionsDTOs);

	        response.setCode(ResponseCode.OK);
	        response.setMessage(environment.getProperty(ResponseMessages.CREATE_ROLES_PERMISSIONS_OK));
	        response.setResult(responseDTOs);

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } catch (DuplicateRoleException | PermissionNotFoundException | RoleDeletionException e) {
	        response.setCode(ResponseCode.BAD_REQUEST);
	        response.setMessage(e.getMessage());
	        logger.error(className + " addOrUpdateRoleWithPermissions() error: " + e.getMessage());
	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    } 
	}

	
	
}
