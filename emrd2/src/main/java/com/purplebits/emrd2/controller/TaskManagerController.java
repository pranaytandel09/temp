package com.purplebits.emrd2.controller;

import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.PaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.TaskManagerDTO;
import com.purplebits.emrd2.dto.TaskManagerPaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.TaskManagerViewDTO;
import com.purplebits.emrd2.dto.UserDetailsDTO;
import com.purplebits.emrd2.dto.request_response.TaskManagerRequestResponseDTO;
import com.purplebits.emrd2.entity.TaskManagerView;
import com.purplebits.emrd2.entity.UserDetailsView;
import com.purplebits.emrd2.exceptions.ContractUpdateException;
import com.purplebits.emrd2.exceptions.ContractWorkFlowNotFound;
import com.purplebits.emrd2.exceptions.EntityNotFoundException;
import com.purplebits.emrd2.exceptions.EntityUpdateException;
import com.purplebits.emrd2.exceptions.FileNumberUpdateException;
import com.purplebits.emrd2.exceptions.ProjectUpdateException;
import com.purplebits.emrd2.exceptions.TaskManagerNotFoundException;
import com.purplebits.emrd2.exceptions.TaskStatusNotValidException;
import com.purplebits.emrd2.exceptions.WorkFlowStepNotFound;
import com.purplebits.emrd2.rsql.TaskManagerSearchRsqlVisitor;
import com.purplebits.emrd2.rsql.UserSearchRsqlVisitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.purplebits.emrd2.service.TaskManagerService;
import com.purplebits.emrd2.util.ResponseHandler;
import com.purplebits.emrd2.util.ResponseMessages;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/taskManager")
public class TaskManagerController {

	@Autowired
	private TaskManagerService taskManagerService;

	@Autowired
	Environment environment;

	@PostMapping("/createNewTasks")
	public ResponseEntity<Response<TaskManagerRequestResponseDTO>> createNewTasks(
			 @RequestBody Request<TaskManagerRequestResponseDTO> request) {

		Response<TaskManagerRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		TaskManagerDTO taskManagerDTO = request.getQuery().getTaskManagerDTO();
		try {
			
			 taskManagerDTO =  taskManagerService.createNewTasks(taskManagerDTO,request.getLoggedInUserID());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_TASK_MANAGER_OK));
			response.setResult(new TaskManagerRequestResponseDTO(taskManagerDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ContractWorkFlowNotFound e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		catch (WorkFlowStepNotFound e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/getAllTaskSearchBased")
	public ResponseEntity<Object> getAllTaskSearchBased(
			@RequestBody Request<TaskManagerPaginationWithSearchRequestDTO> request) {
		String requestId = request.getRequestId();

		PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
		String search;

		Integer pageNumber = 0;
		Integer pageSize = 1000;
		String sortBy = null;
		String sortType = null;
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

		search=taskManagerService.createSearchQuery(request.getQuery());
		Node rootNode = new RSQLParser().parse(search);
		Specification<TaskManagerView> spec = rootNode.accept(new TaskManagerSearchRsqlVisitor<TaskManagerView>());

		PaginationResponseDTO<TaskManagerViewDTO> tasks = taskManagerService.getAllTaskSearchBased(pageNumber, pageSize,
				sortBy, sortType, spec);

		return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, tasks, requestId);

	}


	@PostMapping("/deleteTask")
	public ResponseEntity<Response<TaskManagerRequestResponseDTO>> deleteTask(
			@RequestBody Request<TaskManagerRequestResponseDTO> request) {

		Response<TaskManagerRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		TaskManagerDTO taskManagerDTO = request.getQuery().getTaskManagerDTO();
		try {
			// Call the service layer to handle task deletion
			taskManagerDTO=taskManagerService.deleteTask(taskManagerDTO);
	

			// On success
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.DELETE_TASK_MANAGER_OK));
			response.setResult(new TaskManagerRequestResponseDTO(taskManagerDTO));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (TaskManagerNotFoundException e) {
			// Task not found
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
	}
	
	@PostMapping("/updateTask")
	public ResponseEntity<Response<TaskManagerRequestResponseDTO>> updateTask(
			@RequestBody Request<TaskManagerRequestResponseDTO> request) {

		Response<TaskManagerRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		TaskManagerDTO taskManagerDTO = request.getQuery().getTaskManagerDTO();
		try {
			taskManagerDTO=taskManagerService.updateTask(taskManagerDTO, request.getLoggedInUserID());
			// On success
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_TASK_MANAGER_OK));
			response.setResult(new TaskManagerRequestResponseDTO(taskManagerDTO));
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (EntityUpdateException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}catch (ProjectUpdateException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}catch (ContractUpdateException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}catch (FileNumberUpdateException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}catch (TaskManagerNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}catch (TaskStatusNotValidException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		}
	}
}


