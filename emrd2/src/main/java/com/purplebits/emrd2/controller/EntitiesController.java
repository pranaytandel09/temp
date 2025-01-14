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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.PaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.EntitiesRequestResponseDTO;
import com.purplebits.emrd2.entity.Entities;
import com.purplebits.emrd2.entity.UserGroupDetails;
import com.purplebits.emrd2.exceptions.EntityNameAlreadyExistException;
import com.purplebits.emrd2.exceptions.EntityNotFoundException;
import com.purplebits.emrd2.exceptions.UserGroupMembershipNotFoundException;
import com.purplebits.emrd2.exceptions.UserGroupsNotFoundException;
import com.purplebits.emrd2.rsql.EntitiesSearchRsqlVisitor;
import com.purplebits.emrd2.rsql.UserGroupRoleSearchRsqlVisitor;
import com.purplebits.emrd2.service.EntitiesService;
import com.purplebits.emrd2.util.ResponseHandler;
import com.purplebits.emrd2.util.ResponseMessages;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

@RestController
@RequestMapping("/entities")
public class EntitiesController {

	private final String className = UserGroupMembershipController.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(UserGroupMembershipController.class);
	@Autowired
	Environment environment;

	@Autowired
	private EntitiesService entitiesService;

	@PostMapping("/addNewEntity")
	public ResponseEntity<Response<EntitiesRequestResponseDTO>> addNewEntity(
			@Valid @RequestBody Request<EntitiesRequestResponseDTO> request) {

		Response<EntitiesRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		EntitiesDTO entityDTO = request.getQuery().getEntityDTO();
		try {
			entityDTO = entitiesService.addNewEntity(entityDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_ENTITY_OK));
			response.setResult(new EntitiesRequestResponseDTO(entityDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (EntityNameAlreadyExistException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/updateEntity")
	public ResponseEntity<Response<EntitiesRequestResponseDTO>> updateEntity(
			@Valid @RequestBody Request<EntitiesRequestResponseDTO> request) {

		Response<EntitiesRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		EntitiesDTO entityDTO = request.getQuery().getEntityDTO();
		try {
			
			entityDTO = entitiesService.updateEntity(entityDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_ENTITY_OK));
			response.setResult(new EntitiesRequestResponseDTO(entityDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (EntityNotFoundException  e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		catch (EntityNameAlreadyExistException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getEntityById")
	public ResponseEntity<Response<EntitiesRequestResponseDTO>> getEntityById(
			@Valid @RequestBody Request<EntitiesRequestResponseDTO> request) {

		Response<EntitiesRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		
		try {
			
			EntitiesDTO entityDTO =  entitiesService.getEntityById(request.getQuery().getEntityId());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_ENTITY_OK));
			response.setResult(new EntitiesRequestResponseDTO(entityDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getAllEntities")
	public ResponseEntity<Response<PaginationResponseDTO<EntitiesRequestResponseDTO>>> getAllEntities(
			@RequestBody Request<PaginationRequestDTO> request) {

		Response<PaginationResponseDTO<EntitiesRequestResponseDTO>> response = new Response<>();
		PaginationRequestDTO paginationRequest = request.getQuery();
		Integer pageNumber = 0;
		Integer pageSize = 1000;
		String sortBy = "entityId";
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

		PaginationResponseDTO<EntitiesRequestResponseDTO> memberships = entitiesService
				.getALLEntities(pageNumber, pageSize, sortBy, sortType);

		response.setCode(ResponseCode.OK);
		response.setMessage(environment.getProperty(ResponseMessages.FETCH_ENTITY_OK));
		response.setResult(memberships);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/deleteEntity")
	public ResponseEntity<Response<EntitiesRequestResponseDTO>> deleteEntity(
			@Valid @RequestBody Request<EntitiesRequestResponseDTO> request) {

		Response<EntitiesRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		
		try {
			
			EntitiesDTO entityDTO =  entitiesService.deleteEntity(request.getQuery().getEntityDTO());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.DELETE_ENTITY_OK));
			response.setResult(new EntitiesRequestResponseDTO(entityDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/getAllEntitiesFilterdBased")
	public ResponseEntity<Object> getAllEntitiesFilterdBased(
	        @RequestBody Request<PaginationWithSearchRequestDTO> request) {

	    PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
	    String search = request.getQuery().getSearch();
	    String requestId = request.getRequestId();
	   
        Integer pageNumber = 0;
        Integer pageSize = 1000;
        String sortBy = "entityDisplayName";
        String sortType = "asc";
        if (paginationRequest != null) {	        
            if (paginationRequest.getPageNumber() != null) pageNumber = paginationRequest.getPageNumber();
            if (paginationRequest.getPageSize() != null) pageSize = paginationRequest.getPageSize();
            if (paginationRequest.getSortBy() != null) sortBy = paginationRequest.getSortBy();
            if (paginationRequest.getSortType() != null) sortType = paginationRequest.getSortType();
        }
	    
		search=entitiesService.createQuery(search);
		System.out.println("search query-------->"+ search);
		Node rootNode = new RSQLParser().parse(search);
		Specification<Entities> spec = rootNode.accept(
				new EntitiesSearchRsqlVisitor<Entities>());
		
		PaginationResponseDTO<EntitiesRequestResponseDTO> allActiveEntities = entitiesService.getAllEntitiesByFilterCriteria(
                pageNumber, pageSize, sortBy, sortType, spec);
		
		 return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK,
				 allActiveEntities, requestId);
	}
}
