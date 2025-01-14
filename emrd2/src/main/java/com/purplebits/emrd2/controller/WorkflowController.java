package com.purplebits.emrd2.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.WorkflowDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.WorkflowRequestResponseDTO;
import com.purplebits.emrd2.exceptions.WorkflowNotFoundException;
import com.purplebits.emrd2.service.WorkflowService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

	@Autowired
	Environment environment;

	@Autowired
	private WorkflowService workflowService;
	
	@PostMapping("/addNewWorkflow")
	public ResponseEntity<Response<WorkflowRequestResponseDTO>> addNewWorkflow(
			@Valid @RequestBody Request<WorkflowRequestResponseDTO> request) {

		Response<WorkflowRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		WorkflowDTO workflowDTO = request.getQuery().getWorkflowDTO();
		
			workflowDTO = workflowService.createNewWorkFlow(workflowDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_WORKFLOW_OK));
			response.setResult(new WorkflowRequestResponseDTO(workflowDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		

	}

	@PostMapping("/updateWorkflow")
	public ResponseEntity<Response<WorkflowRequestResponseDTO>> updateWorkflow(
			@Valid @RequestBody Request<WorkflowRequestResponseDTO> request) {

		Response<WorkflowRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		WorkflowDTO workflowDTO = request.getQuery().getWorkflowDTO();
		try {
			
			workflowDTO = workflowService.updateWorkFlow(workflowDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_WORKFLOW_OK));
			response.setResult(new WorkflowRequestResponseDTO(workflowDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (WorkflowNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getWorkflowById")
	public ResponseEntity<Response<WorkflowRequestResponseDTO>> getWorkflowById(
			@Valid @RequestBody Request<WorkflowRequestResponseDTO> request) {

		Response<WorkflowRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		WorkflowDTO workflowDTO = request.getQuery().getWorkflowDTO();
		try {
			
			 workflowDTO =  workflowService.getWorkFlowById(workflowDTO.getWorkflowId());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_WORKFLOW_OK));
			response.setResult(new WorkflowRequestResponseDTO(workflowDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (WorkflowNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

}
