package com.purplebits.emrd2.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.RolePermissionMappingDTO;
import com.purplebits.emrd2.dto.request_response.RolePermissionsMappingRequestResponseDTO;
import com.purplebits.emrd2.exceptions.DuplicateRolePermissionException;
import com.purplebits.emrd2.service.RolePermissionMappingService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/rolePermissionMapping")
public class RolePermissionMappingController {
	private final String className = RolePermissionMappingController.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(RolePermissionMappingController.class);
	@Autowired
	Environment environment;
	
	@Autowired
	private RolePermissionMappingService rolePermissionMappingService;

	@PostMapping("/addAllNewRolePermission")
    public ResponseEntity<Response<List<RolePermissionsMappingRequestResponseDTO>>> addAllNewRolePermission(@Valid
            @RequestBody Request<List<RolePermissionsMappingRequestResponseDTO>> request) {

        Response<List<RolePermissionsMappingRequestResponseDTO>> response = new Response<>();
        response.setRequestId(request.getRequestId());

        List<RolePermissionsMappingRequestResponseDTO> rolePermissionsMappingRequestResponseDTOs = request.getQuery();
        List<RolePermissionMappingDTO>rolePermissionsDTOs = new ArrayList<>();
        for(RolePermissionsMappingRequestResponseDTO rolePermissionsMappingRequestResponseDTO : rolePermissionsMappingRequestResponseDTOs) {
        	rolePermissionsDTOs.add(rolePermissionsMappingRequestResponseDTO.getRolePermissionsDTO());
        }
        
        try {
        	
        	rolePermissionsDTOs = rolePermissionMappingService.addAllNewRolePermission(rolePermissionsDTOs);
        response.setCode(ResponseCode.OK);
        response.setMessage(environment.getProperty(ResponseMessages.CREATE_ROLE_PERMISSION_OK));
        response.setResult(RolePermissionsMappingRequestResponseDTO.getRolePermissionsRequestResponseDTO(rolePermissionsDTOs));

        return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(DuplicateRolePermissionException e) {
        	response.setCode(ResponseCode.BAD_REQUEST);
        	  response.setMessage(e.getMessage());
        	logger.error(className + " addAllNewRolePermission() invoked for: "+e.getMessage());
        	return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }
}
