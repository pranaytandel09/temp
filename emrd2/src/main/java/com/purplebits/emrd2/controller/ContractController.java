package com.purplebits.emrd2.controller;

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

import com.purplebits.emrd2.dto.ContractDTO;
import com.purplebits.emrd2.dto.GlobalPaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.ContractRequestResponseDTO;
import com.purplebits.emrd2.exceptions.ContractNotFoundException;
import com.purplebits.emrd2.service.ContractService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	Environment environment;

	@Autowired
	private ContractService contractService;
	
	@PostMapping("/addNewContract")
	public ResponseEntity<Response<ContractRequestResponseDTO>> addNewContract(
			@Valid @RequestBody Request<ContractRequestResponseDTO> request) {

		Response<ContractRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		ContractDTO contractDTO = request.getQuery().getContractDTO();
		
			contractDTO = contractService.addNewContract(contractDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.CREATE_CONTRACT_OK));
			response.setResult(new ContractRequestResponseDTO(contractDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);

	}

	@PostMapping("/updateContract")
	public ResponseEntity<Response<ContractRequestResponseDTO>> updateContract(
			@Valid @RequestBody Request<ContractRequestResponseDTO> request) {

		Response<ContractRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		ContractDTO contractDTO = request.getQuery().getContractDTO();
		try {
			
			contractDTO = contractService.updateContract(contractDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_CONTRACT_OK));
			response.setResult(new ContractRequestResponseDTO(contractDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ContractNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getContractById")
	public ResponseEntity<Response<ContractRequestResponseDTO>> getContractById(
			@Valid @RequestBody Request<ContractRequestResponseDTO> request) {

		Response<ContractRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		
		try {
			
			ContractDTO contractDTO =  contractService.getContractById(request.getQuery().getContractId());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_CONTRACT_OK));
			response.setResult(new ContractRequestResponseDTO(contractDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ContractNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/getAllContracts")
	public ResponseEntity<Response<PaginationResponseDTO<ContractRequestResponseDTO>>> getAllContracts(
			@RequestBody Request<PaginationRequestDTO> request) {

		Response<PaginationResponseDTO<ContractRequestResponseDTO>> response = new Response<>();
		PaginationRequestDTO paginationRequest = request.getQuery();
		Integer pageNumber = 0;
		Integer pageSize = 1000;
		String sortBy = "contractId";
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

		PaginationResponseDTO<ContractRequestResponseDTO> memberships = contractService
				.getAllContract(pageNumber, pageSize, sortBy, sortType);

		response.setCode(ResponseCode.OK);
		response.setMessage(environment.getProperty(ResponseMessages.FETCH_CONTRACT_OK));
		response.setResult(memberships);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping("/deleteContract")
	public ResponseEntity<Response<ContractRequestResponseDTO>> deleteContract(
			@Valid @RequestBody Request<ContractRequestResponseDTO> request) {

		Response<ContractRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		
		try {
			
			ContractDTO contractDTO =  contractService.deleteContract(request.getQuery().getContractDTO());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.DELETE_CONTRACT_OK));
			response.setResult(new ContractRequestResponseDTO(contractDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (ContractNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/getAllContractsByProjectId")
	public ResponseEntity<Object> getAllContractsByProjectId(
	        @RequestBody Request<GlobalPaginationRequestDTO> request) {

		Response<PaginationResponseDTO<ContractRequestResponseDTO>> response = new Response<>();
		PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
		Integer pageNumber = 0;
		Integer pageSize = 10000;
		String sortBy = "contractId";
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

		PaginationResponseDTO<ContractRequestResponseDTO> memberships = contractService
				.getAllContractsByProjectId(pageNumber, pageSize, sortBy, sortType,request.getQuery().getProjectId());

		response.setCode(ResponseCode.OK);
		response.setMessage(environment.getProperty(ResponseMessages.FETCH_CONTRACT_OK));
		response.setResult(memberships);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
