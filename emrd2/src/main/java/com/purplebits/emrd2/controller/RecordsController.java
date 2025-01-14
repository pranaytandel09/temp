package com.purplebits.emrd2.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.purplebits.emrd2.dto.PaginationRequestDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.dto.RecordsPaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.RecordsViewDTO;
import com.purplebits.emrd2.dto.Request;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.OcrDataRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RecordsRequestResponseDTO;
import com.purplebits.emrd2.entity.RecordsView;
import com.purplebits.emrd2.exceptions.CustomIOException;
import com.purplebits.emrd2.exceptions.RecordNotFoundException;
import com.purplebits.emrd2.rsql.RecordsSearchRsqlVisitor;
import com.purplebits.emrd2.service.RecordsService;
import com.purplebits.emrd2.util.ResponseHandler;
import com.purplebits.emrd2.util.ResponseMessages;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

@RestController
@RequestMapping("/records")
public class RecordsController {

	@Autowired
	Environment environment;

	@Autowired
	private RecordsService recordsService;
	
	@PostMapping("/updateRecord")
	public ResponseEntity<Response<RecordsRequestResponseDTO>> updateRecord(
			@RequestBody Request<RecordsRequestResponseDTO> request) {

		Response<RecordsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		RecordDTO recordDTO = request.getQuery().getRecordDTO();
		try {
			
			recordDTO = recordsService.updateRecord(recordDTO);

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPDATE_RECORD_OK));
			response.setResult(new RecordsRequestResponseDTO(recordDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/fetchRecordById")
	public ResponseEntity<Response<RecordsRequestResponseDTO>> fetchRecordById(
			@RequestBody Request<RecordsRequestResponseDTO> request) {

		Response<RecordsRequestResponseDTO> response = new Response<>();
		response.setRequestId(request.getRequestId());
		RecordDTO recordDTO =null;
		try {
			
			recordDTO = recordsService.fetchRecordById(request.getQuery().getRecordId());

			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.FETCH_RECORD_OK));
			response.setResult(new RecordsRequestResponseDTO(recordDTO));

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PostMapping("/uploadRecords")
	public ResponseEntity<Response<List<RecordsRequestResponseDTO>>> uploadRecords(@RequestParam("files") List<MultipartFile> files)
			{
		Response<List<RecordsRequestResponseDTO>> response = new Response<>();

		try {


			List<RecordDTO> filesDTO = recordsService.uploadRecords(files);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPLOAD_RECORD_OK));
			response.setResult(RecordsRequestResponseDTO.getRecordsRequestResponseDTO(filesDTO));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RecordNotFoundException  e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		} catch (CustomIOException e) {
			response.setCode(ResponseCode.INTERNAL_SERVER_ERROR);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/getAllRecordsSearchBased")
	public ResponseEntity<Object> getAllRecordsSearchBased(
			@RequestBody Request<RecordsPaginationWithSearchRequestDTO> request) {
		String requestId = request.getRequestId();

		PaginationRequestDTO paginationRequest = request.getQuery().getPaginationRequest();
		String search;

		Integer pageNumber = 0;
		Integer pageSize = 1000;
		String sortBy = "recordId";
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

		search=recordsService.createSearchQuery(request.getQuery());
		if(search==null) {
			return ResponseHandler.generateResponse("select the project!", HttpStatus.NOT_FOUND, null, requestId);
		}
		Node rootNode = new RSQLParser().parse(search);
		Specification<RecordsView> spec = rootNode.accept(new RecordsSearchRsqlVisitor<RecordsView>());

		PaginationResponseDTO<RecordsViewDTO> records = recordsService.getAllRecordsSearchBased(pageNumber, pageSize,
				sortBy, sortType, spec);

		return ResponseHandler.generateResponse("Successfully retrieved data!", HttpStatus.OK, records, requestId);

	}
	
	@GetMapping("/downloadRecordById/{recordId}/{loggedInUserId}")
	public ResponseEntity<Resource> downloadRecordById(@PathVariable("recordId") String recordId,
			@PathVariable("loggedInUserId") int loggedInUserId) throws IOException {
		try {
			Resource resource = recordsService.downloadRecordById(recordId, loggedInUserId);

			String fileName = resource.getFilename();

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.contentLength(resource.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(resource);
		} catch (RecordNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}
	
	@PostMapping("/uploadExtractedData")
	public ResponseEntity<Response<RecordsRequestResponseDTO>> uploadExtractedData(@RequestBody Request<OcrDataRequestResponseDTO> request)
			{
		Response<RecordsRequestResponseDTO> response = new Response<>();
		try {


			RecordDTO recordDTO = recordsService.uploadExtractedData(request.getQuery().getBarcodeId(), request.getQuery().getOcrExtractedData());
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPLOAD_RECORD_OK));
			response.setResult(new RecordsRequestResponseDTO(recordDTO));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RecordNotFoundException  e) {
			response.setCode(ResponseCode.BAD_REQUEST);
			response.setMessage(e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
