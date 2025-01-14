package com.purplebits.emrd2.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purplebits.emrd2.dto.CollectionPrProofDTO;
import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.entity.CollectionPrProof;
import com.purplebits.emrd2.entity.RecordEntity;
import com.purplebits.emrd2.exceptions.CustomIOException;
import com.purplebits.emrd2.exceptions.RecordNotFoundException;
import com.purplebits.emrd2.repositories.CollectionPrProofRepository;
import com.purplebits.emrd2.service.CollectionPrProofService;
import com.purplebits.emrd2.service.ProjectService;
import com.purplebits.emrd2.service.RecordsService;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class CollectionPrProofServiceImpl implements CollectionPrProofService {

	private final Logger logger = LogManager.getLogger(CollectionPrProofServiceImpl.class);
	private final String className = CollectionPrProofServiceImpl.class.getSimpleName();
	
	@Autowired
	private CollectionPrProofRepository collectionPrProofRepository;
	@Autowired
	private RecordsService recordsService;

	@Autowired
	Environment environment;
	@Autowired
	private ProjectService projectService;
	
	
	@Value("${collectionOcrFolder}")
	private String collectionOcrFolder;
	
	@Value("${ocr.api.url}")
	private String OCR_ENDPOINT;
	
	@Value("${ocr.refinement.api.url}")
	private String OCR_REFINEMENT_ENDPOINT;
	
	
	//private final String OCR_ENDPOINT = "http://127.0.0.1:8000/docs/extract-text/";

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public CollectionPrProofDTO uploadOcrRecord(MultipartFile file) {
		logger.info("method to upload ocr record invoked.");
		logger.debug(className + " uploadOcrRecord()");
		
		 String originalFilename = file.getOriginalFilename();
         
         String bearcode;
         int lastIndexOfDot = originalFilename.lastIndexOf(".");
         if (lastIndexOfDot == -1) {
         	bearcode= originalFilename;
         }
         bearcode= originalFilename.substring(0, lastIndexOfDot);
     
          RecordDTO recordDTO = recordsService.fetchRecordById(bearcode);
          
          String batchId = bearcode.substring(0, bearcode.indexOf('D'));

			String eId = bearcode.substring(1, bearcode.indexOf('P'));
			String pId = bearcode.substring(bearcode.indexOf('P') + 1, bearcode.indexOf('B'));

			// Create for Encryption Folder
			String folderStructure = eId + File.separator + pId + File.separator + batchId;
			String ocrCollectionDir = collectionOcrFolder +File.separator + folderStructure;
          
		  Path destinationPath = Paths.get(ocrCollectionDir, originalFilename).normalize().toAbsolutePath();
         
		 try {
			 Files.createDirectories(destinationPath.getParent());
			java.nio.file.Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.error("Error while uploading files", e);
	        throw new CustomIOException(environment.getProperty(ResponseMessages.IO_EXCEPTION_MESSAGE));
		}

			CollectionPrProofDTO collectionPrProofDTO = new CollectionPrProofDTO();
			collectionPrProofDTO.setRecord(recordDTO);
			collectionPrProofDTO.setFileName(originalFilename);
			collectionPrProofDTO.setFilePath(folderStructure+File.separator+originalFilename);
			
			CollectionPrProof collectionPrProof = ObjectMapperUtils.map(collectionPrProofDTO, CollectionPrProof.class);
			collectionPrProof =collectionPrProofRepository.saveAndFlush(collectionPrProof);
			

     return ObjectMapperUtils.map(collectionPrProof, CollectionPrProofDTO.class);
	}
	
	@Override
	public void processFileForOcr(Integer prProofId) {
		
		logger.info("method to extract ocr content from record invoked.");
		logger.debug(className + " processFileForOcr()");
		CollectionPrProof record = collectionPrProofRepository.findById(prProofId).get();
		
		 Path destinationPath = Paths.get(collectionOcrFolder, record.getFilePath()).normalize().toAbsolutePath();
	        File pdfFile = new File(destinationPath.toString());
	        if (!pdfFile.exists()) {
	            throw new RuntimeException("File not found at path: " + record.getFilePath());
	        }

	        // Make OCR API call
	        String extractedText = performOcrApiCall(pdfFile);
	        CollectionPrProof collectionPrProof = ObjectMapperUtils.map(record, CollectionPrProof.class);
	        // Update entity
	        collectionPrProof.setOcrContent(extractedText);
	        collectionPrProof.setOcrDone(true);
	        collectionPrProofRepository.save(collectionPrProof);
	}

	@Override
    public void processFileForOcr(CollectionPrProofDTO record){
		logger.info("method to extract ocr content from record invoked.");
		logger.debug(className + " processFileForOcr()");
        // Prepare file and metadata
        Path destinationPath = Paths.get(collectionOcrFolder, record.getFilePath()).normalize().toAbsolutePath();
        File pdfFile = new File(destinationPath.toString());
        if (!pdfFile.exists()) {
            throw new RuntimeException("File not found at path: " + record.getFilePath());
        }

        // Make OCR API call
        String extractedText = performOcrApiCall(pdfFile);
        CollectionPrProof collectionPrProof = ObjectMapperUtils.map(record, CollectionPrProof.class);
        // Update entity
        collectionPrProof.setOcrContent(extractedText);
        collectionPrProof.setOcrDone(true);
        collectionPrProofRepository.save(collectionPrProof);
    }

    private String performOcrApiCall(File file){
    	logger.info("method to call ocr application for ocr content extraction of record invoked.");
		logger.debug(className + " performOcrApiCall()");
        // Create a multi-part request
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("pdf", new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate= new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(OCR_ENDPOINT, requestEntity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, String> responseBody = response.getBody();
            return responseBody.get("extracted_text");
        } else {
            throw new RuntimeException("Failed to extract OCR content. Status: " + response.getStatusCode());
        }
    }
    
    @Override
    public void refineOcrDataAndUpdateTables(CollectionPrProofDTO record) {
    	logger.info("method to get refined ocr content of record invoked.");
		logger.debug(className + " refineOcrDataAndUpdateTables()");
        // Call OCR API and get the response
    	//String recordId, String content, List<String> requestedAttributes, String fileName
    	 RecordDTO recordDTO = recordsService.fetchRecordById(record.getRecord().getRecordId());
    	 Map<String, List<String>> projectTagsById = projectService.getProjectTagsById(recordDTO.getProject().getProjectId());
    	 List<String> requestedAttributes = projectTagsById.get("otherFields");
        Map<String, String> extractedInfo = callOcrApi(record.getOcrContent(), requestedAttributes, record.getRecord().getRecordId());

        // Update records and collection_pr_proof tables
        updateTables(record.getRecord().getRecordId(), extractedInfo);
    }
    
    private Map<String, String> callOcrApi(String content, List<String> requestedAttributes, String fileName) {
    	logger.info("method to call ocr application for refined ocr content extraction of record invoked.");
		logger.debug(className + " callOcrApi()");
        // Prepare the API request payload
        Map<String, Object> requestPayload = new HashMap<>();
        requestPayload.put("content", content);
        requestPayload.put("requested_attributes", requestedAttributes);
        requestPayload.put("file_name", fileName);

        // OCR Refinement API URL from properties
        String ocrApiUrl = OCR_REFINEMENT_ENDPOINT;
        if (ocrApiUrl == null) {
            throw new RuntimeException("OCR Refinement API URL not configured in properties.");
        }

        // API call to refine OCR data
        ResponseEntity<Map<String, Object>> responseEntity;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);
            RestTemplate restTemplate= new RestTemplate();
            responseEntity = restTemplate.exchange(
                    ocrApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while calling OCR refinement API", e);
        }

        // Process the response
        Map<String, Object> responseBody = responseEntity.getBody();
        if (responseBody == null || !responseBody.containsKey("extracted_info")) {
            throw new RuntimeException("Invalid response from OCR refinement API");
        }

        @SuppressWarnings("unchecked")
        Map<String, String> extractedInfo = (Map<String, String>) responseBody.get("extracted_info");
        return extractedInfo;
    }
    
    private void updateTables(String recordId, Map<String, String> extractedInfo) {
    	logger.info("method to update record against refined ocr data invoked.");
		logger.debug(className + " updateTables()");
        // Fetch record by ID
        RecordDTO record = recordsService.fetchRecordById(recordId);

        // Convert extracted info to JSON
        String additionalTagsJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            additionalTagsJson = objectMapper.writeValueAsString(extractedInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while converting extracted info to JSON", e);
        }

        // Update the record
        record.setAdditionalTags(additionalTagsJson);
        record.setOcrStatus(1); // Assume 1 indicates OCR is complete
        recordsService.updateRecord(record);

        // Update collection_pr_proof
        CollectionPrProof proof = collectionPrProofRepository.findByRecordRecordId(record.getRecordId())
                .orElseThrow(() -> new RuntimeException("CollectionPrProof not found for Record ID: " + record.getRecordId()));

        proof.setDataExtractionDone(true);
        collectionPrProofRepository.saveAndFlush(proof);
    }

	@Override
	public List<CollectionPrProofDTO> getRecordsForOcr() {
		logger.info("method to get records for ocr content extraction invoked.");
		logger.debug(className + " getRecordsForOcr()");
		List<CollectionPrProof> collectionPrProof=collectionPrProofRepository.findByIsOcrDone(false);
		return ObjectMapperUtils.mapAll(collectionPrProof, CollectionPrProofDTO.class);
		
	}

	@Override
	public List<CollectionPrProofDTO> getRecordsForOcrDataRefinement() {
		logger.info("method to get records for refined data from ocr content invoked.");
		logger.debug(className + " getRecordsForOcrDataRefinement()");
		List<CollectionPrProof> collectionPrProof=collectionPrProofRepository.findByIsOcrDoneAndIsDataExtractionDone(true,false);
		return ObjectMapperUtils.mapAll(collectionPrProof, CollectionPrProofDTO.class);
	}

	

}
