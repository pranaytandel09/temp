package com.purplebits.emrd2.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.RecordDTO;
import com.purplebits.emrd2.dto.RecordsPaginationWithSearchRequestDTO;
import com.purplebits.emrd2.dto.RecordsViewDTO;
import com.purplebits.emrd2.entity.RecordEntity;
import com.purplebits.emrd2.entity.RecordsView;
import com.purplebits.emrd2.exceptions.CustomIOException;
import com.purplebits.emrd2.exceptions.CustomJsonProcessingException;
import com.purplebits.emrd2.exceptions.RecordNotFoundException;
import com.purplebits.emrd2.repositories.RecordsRepository;
import com.purplebits.emrd2.repositories.RecordsViewRepository;
import com.purplebits.emrd2.service.ProjectService;
import com.purplebits.emrd2.service.RecordsService;
import com.purplebits.emrd2.util.Encryptor;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class RecordsServiceimpl implements RecordsService {

	private final Logger logger = LogManager.getLogger(RecordsServiceimpl.class);
	private final String className = RecordsServiceimpl.class.getSimpleName();
	
	@Autowired
	Environment environment;

	@Autowired
	private RecordsRepository recordsRepository;
	
	@Autowired
	private RecordsViewRepository recordsViewRepository;
	@Autowired
	private ProjectService projectService;
	
	@Value("${uploadFolder}")
	private String uploadFolder;
	
	@Value("${encryptionFolder}")
	private String encrptionHomeDir;
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public RecordDTO updateRecord(RecordDTO recordDTO) {
		logger.info("method to update record invoked." + recordDTO);
		logger.debug(className + " updateRecord()");
		
		Optional<RecordEntity> recordsOp = recordsRepository.findById(recordDTO.getRecordId());
		if(!recordsOp.isPresent())throw new RecordNotFoundException(
				environment.getProperty(ResponseMessages.RECORD_NOT_EXIST));
		
		RecordEntity records = recordsOp.get();
		records = ObjectMapperUtils.map(recordDTO, RecordEntity.class);
		records=recordsRepository.saveAndFlush(records);
		return ObjectMapperUtils.map(records, RecordDTO.class);
	}

	@Override
	public RecordDTO fetchRecordById(String recordId) {
		logger.info("method to fetch record invoked. " + recordId);
		logger.debug(className + " fetchRecordById()");
		
		Optional<RecordEntity> recordsOp = recordsRepository.findById(recordId);
		if(!recordsOp.isPresent())throw new RecordNotFoundException(
				environment.getProperty(ResponseMessages.RECORD_NOT_EXIST));
		
		RecordEntity records = recordsOp.get();
		return ObjectMapperUtils.map(records, RecordDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public List<RecordDTO> uploadRecords(List<MultipartFile> files) {
	    logger.info("Method to upload records invoked.");
	    logger.debug(className + " uploadRecords()");

	    List<RecordDTO> result = new ArrayList<>();
	    
	    try {
	        for (MultipartFile file : files) {
	        	
	            String originalFilename = file.getOriginalFilename();
	            
	            String bearcode;
	            int lastIndexOfDot = originalFilename.lastIndexOf(".");
	            if (lastIndexOfDot == -1) {
	            	bearcode= originalFilename;
	            }
	            bearcode= originalFilename.substring(0, lastIndexOfDot);
	        
	            Optional<RecordEntity> recordsOp = recordsRepository.findById(bearcode);
	            if (!recordsOp.isPresent()) {
	                throw new RecordNotFoundException(
	                    environment.getProperty(ResponseMessages.RECORD_NOT_EXIST));
	            }

	            RecordEntity records = recordsOp.get();
				Path destinationPath = Paths.get(uploadFolder, originalFilename).normalize().toAbsolutePath();
	            Files.createDirectories(destinationPath.getParent());
				java.nio.file.Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

				records.setUploadStatus(1);	
				records.setUploadLocation(originalFilename);
				records.setFileType(file.getContentType());
				recordsRepository.save(records);
	            result.add(ObjectMapperUtils.map(records, RecordDTO.class));
	        }

	        return result;
	    } catch (IOException e) {
	        logger.error("Error while uploading files", e);
	        throw new CustomIOException(environment.getProperty(ResponseMessages.IO_EXCEPTION_MESSAGE));
	    }
	}


	private String getFileExtension(String filename) {
	    int lastIndex = filename.lastIndexOf('.');
	    return (lastIndex == -1) ? "" : filename.substring(lastIndex + 1);
	}
	
	@Override
	public PaginationResponseDTO<RecordsViewDTO> getAllRecordsSearchBased(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType, Specification<RecordsView> spec) {
		 logger.info("Method to get all records search based invoked.");
		    logger.debug(className + " getAllRecordsSearchBased()");
	    Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<RecordsViewDTO> response= new PaginationResponseDTO<>();
	    
	    Page<RecordsView> recordsPage = recordsViewRepository.findAll(spec,pageable);
	    List<RecordsView> recordsList = recordsPage.getContent();
	    
	    List<RecordsViewDTO> result = ObjectMapperUtils.mapAll(recordsList, RecordsViewDTO.class);
	    
	    PaginationDTO paginationDTO = new PaginationDTO();
	    paginationDTO.setPage(recordsPage.getNumber());
	    paginationDTO.setLimit(recordsPage.getSize());
	    paginationDTO.setTotalPages(recordsPage.getTotalPages());
	    paginationDTO.setLastPage(recordsPage.isLast());
	    paginationDTO.setTotalCounts(recordsPage.getTotalElements());
	    response.setPagination(paginationDTO);
	    response.setData(result);
	    return response;
	}
	
	@Override
	public List<RecordDTO> fetchRecordsForEncryptionProcessing() {
		logger.info("Method to get records for encryption invoked.");
	    logger.debug(className + " fetchRecordsForEncryptionProcessing()");
	    
	    List<RecordEntity> records = recordsRepository.findByUploadStatusAndPhysicalStatusAndPerformeEncryption(1, 0, true);
	    return ObjectMapperUtils.mapAll(records, RecordDTO.class);
	    
	}

	@Override
	public List<RecordDTO> fetchRecordsForOcrProcessing() {
		logger.info("Method to get records for ocr invoked.");
	    logger.debug(className + " fetchRecordsForOcrProcessing()");
	    
	 // For records with uploadStatus = 1, ocrStatus = 0, performeOCR = true
	    List<RecordEntity> records = recordsRepository.findByUploadStatusAndOcrStatusAndPerformeOCR(1, 0, true);
	    return ObjectMapperUtils.mapAll(records, RecordDTO.class);
	}
	
	@Override
	public RecordDTO uploadExtractedData(String barcodeId, Map<String, String> ocrExtractedData) {
		logger.info("Method to upload ocr data invoked.");
	    logger.debug(className + " uploadExtractedData()");

	    Optional<RecordEntity> recordsOp = recordsRepository.findById(barcodeId);
        if (!recordsOp.isPresent()) {
            throw new RecordNotFoundException(
                environment.getProperty(ResponseMessages.RECORD_NOT_EXIST));
        }
        RecordEntity recordEntity = recordsOp.get();
        
        ObjectMapper mapper = new ObjectMapper();
        String additionalTagsJson;
        try {
            additionalTagsJson = mapper.writeValueAsString(ocrExtractedData);
        } catch (JsonProcessingException e) {
            throw new CustomJsonProcessingException(environment.getProperty(ResponseMessages.JSON_PROCESSING_EXCEPTION));
        }
        
        recordEntity.setAdditionalTags(additionalTagsJson);
        recordEntity =recordsRepository.saveAndFlush(recordEntity);
		return ObjectMapperUtils.map(recordEntity, RecordDTO.class);
	}

	@Override
	public String createSearchQuery(RecordsPaginationWithSearchRequestDTO query) {
		StringBuilder actualQuery = new StringBuilder();
		String result;
		boolean entityInfo=false;
		String searchParam = query.getEntityName();
		if (searchParam != null && !searchParam.isEmpty()) {
			entityInfo=true;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("entityName ==" + "'" + searchParamList.get(i)).append("'").append(";");
					} else {
						actualQuery.append("entityName ==" + "'" + searchParamList.get(i)).append("'").append(",");
					}

				}

			} else {

				actualQuery.append("entityName ==" + "'" + searchParam + "'").append(";");
			}

		}

		 searchParam = query.getEntityDisplayName();
		if (searchParam != null && !searchParam.isEmpty()) {
			entityInfo=true;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("entityDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("entityDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("entityDisplayName ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getProjectName();
		if (searchParam != null && !searchParam.isEmpty()) {
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("projectName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("projectName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("projectName ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getProjectDisplayName();
		if (searchParam != null && !searchParam.isEmpty()) {
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("projectDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("projectDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("projectDisplayName ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getNameTag();
		if (searchParam != null && !searchParam.isEmpty()) {
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("nameTag ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("nameTag ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("nameTag ==" + "'*" + searchParam + "*'").append(";");
			}

		}
		searchParam = query.getIpdNumberTag();
		if (searchParam != null && !searchParam.isEmpty()) {
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("ipdNumberTag ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("ipdNumberTag ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("ipdNumberTag ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getBarcode();
		if (searchParam != null && !searchParam.isEmpty()) {
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("barcode ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("barcode ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("barcode ==" + "'*" + searchParam + "*'").append(";");
			}

		}
		
		String recordId = query.getRecordId();
		if (recordId != null && !recordId.isEmpty()) {
			if (recordId.contains(",")) {
				List<String> recordIdList = Arrays.asList(recordId.split("\\s*,\\s*"));

				for (int i = 0; i < recordIdList.size(); i++) {
					if (i == recordIdList.size() - 1) {
						actualQuery.append("recordId ==" + Integer.parseInt(recordIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("recordId ==" + Integer.parseInt(recordIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("recordId ==" + Integer.parseInt(recordId)).append(";");
			}

		}
		
		String boxId = query.getBoxId();
		if (boxId != null && !boxId.isEmpty()) {
			if (boxId.contains(",")) {
				List<String> boxIdList = Arrays.asList(boxId.split("\\s*,\\s*"));

				for (int i = 0; i < boxIdList.size(); i++) {
					if (i == boxIdList.size() - 1) {
						actualQuery.append("boxId ==" + Integer.parseInt(boxIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("boxId ==" + Integer.parseInt(boxIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("boxId ==" + Integer.parseInt(boxId)).append(";");
			}

		}
		
		String scannedStatus = query.getScannedStatus();
		if (scannedStatus != null && !scannedStatus.isEmpty()) {
			if (scannedStatus.contains(",")) {
				List<String> scannedStatusList = Arrays.asList(scannedStatus.split("\\s*,\\s*"));

				for (int i = 0; i < scannedStatusList.size(); i++) {
					if (i == scannedStatusList.size() - 1) {
						actualQuery.append("scannedStatus ==" + Integer.parseInt(scannedStatusList.get(i)))
								.append(";");
					} else {
						actualQuery.append("scannedStatus ==" + Integer.parseInt(scannedStatusList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("scannedStatus ==" + Integer.parseInt(scannedStatus)).append(";");
			}

		}
		
		String uploadStatus = query.getUploadStatus();
		if (uploadStatus != null && !uploadStatus.isEmpty()) {
			if (uploadStatus.contains(",")) {
				List<String> uploadStatusList = Arrays.asList(uploadStatus.split("\\s*,\\s*"));

				for (int i = 0; i < uploadStatusList.size(); i++) {
					if (i == uploadStatusList.size() - 1) {
						actualQuery.append("uploadStatus ==" + Integer.parseInt(uploadStatusList.get(i)))
								.append(";");
					} else {
						actualQuery.append("uploadStatus ==" + Integer.parseInt(uploadStatusList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("uploadStatus ==" + Integer.parseInt(uploadStatus)).append(";");
			}

		}
		
		String entityId = query.getEntityId();
		if (entityId != null && !entityId.isEmpty()) {
			entityInfo=true;
			if (entityId.contains(",")) {
				List<String> entityIdList = Arrays.asList(entityId.split("\\s*,\\s*"));

				for (int i = 0; i < entityIdList.size(); i++) {
					if (i == entityIdList.size() - 1) {
						actualQuery.append("entityId ==" + Integer.parseInt(entityIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("entityId ==" + Integer.parseInt(entityIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("entityId ==" + Integer.parseInt(entityId)).append(";");
			}

		}
		
		String projectId = query.getProjectId();
		if (projectId != null && !projectId.isEmpty()) {
			if (projectId.contains(",")) {
				List<String> projectIdList = Arrays.asList(projectId.split("\\s*,\\s*"));

				for (int i = 0; i < projectIdList.size(); i++) {
					if (i == projectIdList.size() - 1) {
						actualQuery.append("projectId ==" + Integer.parseInt(projectIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("projectId ==" + Integer.parseInt(projectIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("projectId ==" + Integer.parseInt(projectId)).append(";");
			}

		}
		
		String ocrStatus = query.getOcrStatus();
		if (ocrStatus != null && !ocrStatus.isEmpty()) {
			if (ocrStatus.contains(",")) {
				List<String> ocrStatusList = Arrays.asList(ocrStatus.split("\\s*,\\s*"));

				for (int i = 0; i < ocrStatusList.size(); i++) {
					if (i == ocrStatusList.size() - 1) {
						actualQuery.append("ocrStatus ==" + Integer.parseInt(ocrStatusList.get(i)))
								.append(";");
					} else {
						actualQuery.append("ocrStatus ==" + Integer.parseInt(ocrStatusList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("ocrStatus ==" + Integer.parseInt(ocrStatus)).append(";");
			}

		}
		
		String startDate = query.getStartDate();
		if (startDate != null && !startDate.isEmpty()) {
			if (startDate.contains(",")) {
				List<String> startDateList = Arrays.asList(startDate.split("\\s*,\\s*"));

				for (int i = 0; i < startDateList.size(); i++) {
					if (i == startDateList.size() - 1) {
						actualQuery.append("doaTag =ge= " + startDateList.get(i)).append("*").append(";");
					} else {
						actualQuery.append("doaTag =ge= " + startDateList.get(i)).append("*").append(",");
					}

				}

			} else {

				actualQuery.append("doaTag =ge=" + "'" + startDate + "'").append(";");
			}

		}

		String endDate = query.getStartDate();
		if (endDate != null && !endDate.isEmpty()) {
			if (endDate.contains(",")) {
				List<String> startDateList = Arrays.asList(endDate.split("\\s*,\\s*"));

				for (int i = 0; i < startDateList.size(); i++) {
					if (i == startDateList.size() - 1) {
						actualQuery.append("doaTag =le= " + startDateList.get(i)).append("*").append(";");
					} else {
						actualQuery.append("doaTag =le= " + startDateList.get(i)).append("*").append(",");
					}

				}

			} else {

				actualQuery.append("doaTag =le=" + "'" + startDate + "'").append(";");
			}

		}
		
		if(!entityInfo) {
			return null;
		}
		

		if (actualQuery.charAt(actualQuery.length() - 1) == ';'
				|| actualQuery.charAt(actualQuery.length() - 1) == ',') {
			result = actualQuery.substring(0, actualQuery.length() - 1);
		} else {
			result = actualQuery.toString();
		}
		return result;
	}

	@Override
	public Resource downloadRecordById(String recordId, int loggedInUserId) {
	    logger.info("Method to download record invoked.");
	    logger.debug(className + " downloadRecordById()");

	    // File type constants
	    String applicationPDF = "application/pdf";
	    String pdfExtension = "pdf";
	   

	    // Fetch record details
	    Optional<RecordEntity> recordOp = recordsRepository.findById(recordId);
	    if (!recordOp.isPresent()) {
	        throw new RecordNotFoundException(environment.getProperty(ResponseMessages.RECORD_NOT_EXIST));
	    }

	    RecordEntity record = recordOp.get();
	    String input = record.getUploadLocation(); // Encrypted file location
	    Path inputPath = Paths.get(encrptionHomeDir, input); // Parse the input path
	    String originalFileName = inputPath.getFileName().toString(); // Extract file name

	    // Determine decrypted file name
	    String fileName;
	    if (originalFileName.endsWith("_e.pdf")) {
	        fileName = originalFileName.replace("_e.pdf", ".pdf");
	       // oldEncryptedFiles=true;
	    }
//	    else if (originalFileName.endsWith("_e2.pdf")) {
//	        fileName = originalFileName.replace("_e2.pdf", ".pdf");
//	    }
	    else {
	        fileName = originalFileName;
	    }

	    // Paths for decryption
	    String src = encrptionHomeDir + File.separator + input;
	    String decryptDirPath = encrptionHomeDir + File.separator + "decrypt";
	    String dest = decryptDirPath + File.separator + fileName;

	    // Ensure the decrypt directory exists
	    File decryptDir = new File(decryptDirPath);
	    if (!decryptDir.exists()) {
	        if (!decryptDir.mkdirs()) {
	            throw new RuntimeException("Failed to create decrypt directory: " + decryptDirPath);
	        }
	    }

	    // Decrypt the file
	    ProjectDTO projectDTO = projectService.getProjectById(record.getProject().getProjectId());
	    String encryptPassword = projectDTO.getProjectPassword();
	    Encryptor encryptor = new Encryptor();
	    String decryptPassword = encryptor.decryptPassword(encryptPassword);

	    if (applicationPDF.equalsIgnoreCase(record.getFileType()) || pdfExtension.equalsIgnoreCase(record.getFileType())) {
	        encryptor.decryptPdf(src, dest, decryptPassword);
	    } else {
	        encryptor.decryptFile(src, dest, decryptPassword);
	    }
	    
//	    boolean oldEncryptedFiles=false;
//	    if (oldEncryptedFiles) {
//	        encryptor.decryptPdf(src, dest, decryptPassword);
//	    } else {
//	        encryptor.decryptFile(src, dest, decryptPassword);
//	    }

	    // Verify if the decrypted file exists
	    Path filePath = Paths.get(dest).normalize().toAbsolutePath();
	    File file = filePath.toFile();

	    if (!file.exists() || !file.isFile()) {
	        throw new RecordNotFoundException(environment.getProperty(ResponseMessages.RECORD_NOT_EXIST));
	    }
	    
	    return new FileSystemResource(file);

	}

	


}
