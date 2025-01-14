package com.purplebits.emrd2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.purplebits.emrd2.dto.GlobalPaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.StepDTO;
import com.purplebits.emrd2.service.StepsService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/steps")
public class StepsController {
	@Autowired
	Environment environment;

	@Autowired
	private StepsService stepsService;
	
	@PostMapping("/getStepsByContractId")
	public ResponseEntity<Response<PaginationResponseDTO<StepDTO>>> getStepsByContractId(
			@RequestBody Request<GlobalPaginationRequestDTO> request) {
		
		Response<PaginationResponseDTO<StepDTO>> response = new Response<>();
		PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
		int pageNumber = 0;
		int pageSize = 1000;
		String sortBy = "name";
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
			PaginationResponseDTO<StepDTO> StepsDTO =  stepsService.getStepsByContractId(request.getQuery().getContractId(),
					pageNumber, pageSize, sortBy, sortType);
			

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_STEPS_OK));
			response.setResult(StepsDTO);

			return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
