package com.purplebits.emrd2.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.purplebits.emrd2.dto.GlobalPaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.ProjectsRequestResponseDTO;
import com.purplebits.emrd2.exceptions.ProjectNameAlreadyExistException;
import com.purplebits.emrd2.exceptions.ProjectNotFoundException;
import com.purplebits.emrd2.service.ProjectService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/projects")
public class ProjectController {

	@Autowired
	Environment environment;

	@Autowired
	private ProjectService projectService;
	
	@PostMapping("/addNewProject")
	public ResponseEntity<Response<ProjectsRequestResponseDTO>> addProject(
			@Valid @RequestBody Request<ProjectsRequestResponseDTO> request) {

		Response<ProjectsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		ProjectDTO projectDTO = request.getQuery().getProjectDTO();
		try {
			projectDTO = projectService.addNewProject(projectDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_PROJECT_OK));
			response.setResult(new ProjectsRequestResponseDTO(projectDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ProjectNameAlreadyExistException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/updateProject")
	public ResponseEntity<Response<ProjectsRequestResponseDTO>> updateExistingProject(
			@Valid @RequestBody Request<ProjectsRequestResponseDTO> request) {

		Response<ProjectsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		ProjectDTO projectDTO = request.getQuery().getProjectDTO();
		try {
			
			projectDTO = projectService.updateProject(projectDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_PROJECT_OK));
			response.setResult(new ProjectsRequestResponseDTO(projectDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ProjectNameAlreadyExistException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		catch (ProjectNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getProjectById")
	public ResponseEntity<Response<ProjectsRequestResponseDTO>> getProjectById(
			@Valid @RequestBody Request<ProjectsRequestResponseDTO> request) {

		Response<ProjectsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		
		try {
			
			ProjectDTO projectDTO =  projectService.getProjectById(request.getQuery().getProjectId());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_PROJECT_OK));
			response.setResult(new ProjectsRequestResponseDTO(projectDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ProjectNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getAllProjects")
	public ResponseEntity<Response<PaginationResponseDTO<ProjectsRequestResponseDTO>>> getAllProjects(
			@RequestBody Request<PaginationRequestDTO> request) {

		Response<PaginationResponseDTO<ProjectsRequestResponseDTO>> response = new Response<>();
		PaginationRequestDTO paginationRequest = request.getQuery();
		Integer pageNumber = 0;
		Integer pageSize = 1000;
		String sortBy = "projectId";
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

		PaginationResponseDTO<ProjectsRequestResponseDTO> memberships = projectService
				.getAllProjects(pageNumber, pageSize, sortBy, sortType);

		response.setCode(ResponseCode.OK);
		response.setMessage(environment.getProperty(ResponseMessages.FETCH_PROJECT_OK));
		response.setResult(memberships);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/deleteProject")
	public ResponseEntity<Response<ProjectsRequestResponseDTO>> deleteProject(
			@Valid @RequestBody Request<ProjectsRequestResponseDTO> request) {

		Response<ProjectsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		
		try {
			
			ProjectDTO projectDTO =  projectService.deleteProject(request.getQuery().getProjectDTO());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.DELETE_PROJECT_OK));
			response.setResult(new ProjectsRequestResponseDTO(projectDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ProjectNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/getAllProjectsByEntityId")
	public ResponseEntity<Object> getAllProjectsByEntityId(
	        @RequestBody Request<GlobalPaginationRequestDTO> request) {

		Response<PaginationResponseDTO<ProjectsRequestResponseDTO>> response = new Response<>();
		PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
		Integer pageNumber = 0;
		Integer pageSize = 10000;
		String sortBy = "projectId";
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

		PaginationResponseDTO<ProjectsRequestResponseDTO> memberships = projectService
				.getAllProjectsByEntityId(pageNumber, pageSize, sortBy, sortType,request.getQuery().getEntityId());

		response.setCode(ResponseCode.OK);
		response.setMessage(environment.getProperty(ResponseMessages.FETCH_PROJECT_OK));
		response.setResult(memberships);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/getProjectTagsByProjectId")
	public ResponseEntity<Response<Map<String, List<String>>>> getProjectTagsById(
			 @RequestBody Request<ProjectsRequestResponseDTO> request) {

		Response<Map<String, List<String>>> response = new Response<>();
		response.setRequestId(request.getRequestId());
		
		try {
			
			Map<String, List<String>> projectTags =  projectService.getProjectTagsById(request.getQuery().getProjectId());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_PROJECT_TAGS_OK));
			response.setResult(projectTags);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ProjectNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}
}
