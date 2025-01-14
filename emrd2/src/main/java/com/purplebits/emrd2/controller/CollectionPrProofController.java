package com.purplebits.emrd2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.purplebits.emrd2.dto.CollectionPrProofDTO;
import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.dto.Response;
import com.purplebits.emrd2.dto.ResponseCode;
import com.purplebits.emrd2.dto.request_response.CollectionPrProofRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RecordsRequestResponseDTO;
import com.purplebits.emrd2.exceptions.CustomIOException;
import com.purplebits.emrd2.exceptions.RecordNotFoundException;
import com.purplebits.emrd2.service.CollectionPrProofService;
import com.purplebits.emrd2.util.ResponseMessages;

@RestController
@RequestMapping("/ocrCollection")
public class CollectionPrProofController {

	@Autowired
	private CollectionPrProofService collectionPrProofService;
	@Autowired
	Environment environment;
	
	@PostMapping("/uploadOcrRecord")
	public ResponseEntity<Response<CollectionPrProofRequestResponseDTO>> uploadOcrRecord(@RequestParam("files") MultipartFile file)
			{
		Response<CollectionPrProofRequestResponseDTO> response = new Response<>();

		try {


			CollectionPrProofDTO filesDTO = collectionPrProofService.uploadOcrRecord(file);
			response.setCode(ResponseCode.OK);
			response.setMessage(environment.getProperty(ResponseMessages.UPLOAD_RECORD_OK));
			response.setResult(new CollectionPrProofRequestResponseDTO(filesDTO));
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
	
	@PostMapping("/{id}/process-ocr")
    public ResponseEntity<String> processOcr(@PathVariable("id") Integer prProofId) {
        try {
        	collectionPrProofService.processFileForOcr(prProofId);
            return ResponseEntity.ok("OCR processing completed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
	
}
