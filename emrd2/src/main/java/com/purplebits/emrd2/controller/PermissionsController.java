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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.PermissionsRequestResponseDTO;
import com.purplebits.emrd2.exceptions.DuplicateDisplayNameException;
import com.purplebits.emrd2.exceptions.DuplicatePermissionsException;
import com.purplebits.emrd2.exceptions.PermissionNotFoundException;
import com.purplebits.emrd2.service.PermissionsService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {
	private final String className = PermissionsController.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(PermissionsController.class);
	@Autowired
	Environment environment;

	@Autowired
	private PermissionsService permissionsService;

	@PostMapping("/addNewPermission")
	public ResponseEntity<Response<PermissionsRequestResponseDTO>> addNewPermission(
			@Valid @RequestBody Request<PermissionsRequestResponseDTO> request) {
		Response<PermissionsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());

		try {
			PermissionsDTO permissionsDTO = request.getQuery().getPermissionsDTO();
			PermissionsDTO createdPermission = permissionsService.addNewPermission(permissionsDTO);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_PERMISSIONS_OK));
			response.setResult(new PermissionsRequestResponseDTO(createdPermission));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DuplicatePermissionsException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " addNewPermission() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (DuplicateDisplayNameException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " addNewPermission() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getAllPermissions")
	public ResponseEntity<Response<List<PermissionsRequestResponseDTO>>> getAllPermissions() {
		Response<List<PermissionsRequestResponseDTO>> response = new Response<>();

		List<PermissionsDTO> permissionsList = permissionsService.getAllPermissions();
		response.setCode(ResponseCode.OK);
		response.setMessage(environment.getProperty(ResponseMessages.FETCH_PERMISSIONS_OK));
		response.setResult(PermissionsRequestResponseDTO.getPermissionsRequestResponseDTO(permissionsList));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/getPermissionById")
	public ResponseEntity<Response<PermissionsRequestResponseDTO>> getPermissionById(
			@RequestBody Request<PermissionsRequestResponseDTO> request) {
		Response<PermissionsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());

		try {
			PermissionsDTO permissionsDTO = permissionsService.getPermissionById(request.getQuery().getPermissionId());
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_PERMISSIONS_OK));
			response.setResult(new PermissionsRequestResponseDTO(permissionsDTO));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (PermissionNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " getPermissionById() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/updatePermission")
	public ResponseEntity<Response<PermissionsRequestResponseDTO>> updatePermission(
			@RequestBody Request<PermissionsRequestResponseDTO> request) {
		Response<PermissionsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());

		try {
			PermissionsDTO permissionsDTO = request.getQuery().getPermissionsDTO();
			PermissionsDTO updatedPermission = permissionsService.updatePermission(permissionsDTO);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_PERMISSIONS_OK));
			response.setResult(new PermissionsRequestResponseDTO(updatedPermission));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (PermissionNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " updatePermission() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (DuplicateDisplayNameException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " addNewPermission() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/deletePermission")
	public ResponseEntity<Response<PermissionsRequestResponseDTO>> deletePermission(
			@RequestBody Request<PermissionsRequestResponseDTO> request) {
		Response<PermissionsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());

		try {
			PermissionsDTO permissionsDTO = permissionsService.deletePermission(request.getQuery().getPermissionId());
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.DELETE_PERMISSIONS_OK));
			response.setResult(new PermissionsRequestResponseDTO(permissionsDTO));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (PermissionNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " deletePermission() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/addAllNewPermission")
	public ResponseEntity<Response<List<PermissionsRequestResponseDTO>>> addAllNewPermission(
			@Valid @RequestBody Request<List<PermissionsRequestResponseDTO>> request) {
		Response<List<PermissionsRequestResponseDTO>> response = new Response<>();
		response.setRequestId(request.getRequestId());

		List<PermissionsDTO> permissionsDTOs = new ArrayList<>();
		List<PermissionsRequestResponseDTO> permissionsRequestResponseDTOs = request.getQuery();

		for (PermissionsRequestResponseDTO permissionsRequestResponseDTO : permissionsRequestResponseDTOs) {
			permissionsDTOs.add(permissionsRequestResponseDTO.getPermissionsDTO());
		}

		try {

			permissionsDTOs = permissionsService.addAllNewPermission(permissionsDTOs);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_PERMISSIONS_OK));
			response.setResult(PermissionsRequestResponseDTO.getPermissionsRequestResponseDTO(permissionsDTOs));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DuplicatePermissionsException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " addAllNewPermission() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (DuplicateDisplayNameException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			logger.error(className + " addAllNewPermission() invoked for: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
