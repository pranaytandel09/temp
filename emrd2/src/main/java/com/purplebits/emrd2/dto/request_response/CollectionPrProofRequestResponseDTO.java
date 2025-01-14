package com.purplebits.emrd2.dto.request_response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purplebits.emrd2.dto.CollectionPrProofDTO;
import com.purplebits.emrd2.dto.RecordDTO;

public class CollectionPrProofRequestResponseDTO {

	private Integer prProofId;
	private String recordId;
	private String fileName;
	private String filePath;
	private boolean isOcrDone;
	private String ocrContent;
	private boolean isDataExtractionDone;
	
	public CollectionPrProofRequestResponseDTO() {
		super();
	}

	public CollectionPrProofRequestResponseDTO(CollectionPrProofDTO collectionPrProofDTO) {
		super();
		this.prProofId = collectionPrProofDTO.getPrProofId();
		this.recordId = collectionPrProofDTO.getRecord().getRecordId();
		this.fileName = collectionPrProofDTO.getFileName();
		this.filePath = collectionPrProofDTO.getFilePath();
		this.isOcrDone = collectionPrProofDTO.isOcrDone();
		this.ocrContent = collectionPrProofDTO.getOcrContent();
		this.isDataExtractionDone = collectionPrProofDTO.isDataExtractionDone();
	}

	@JsonIgnore
	public CollectionPrProofDTO getCollectionPrProofDTO() {
		CollectionPrProofDTO collectionPrProofDTO = new CollectionPrProofDTO();
		collectionPrProofDTO.setPrProofId(prProofId);
		collectionPrProofDTO.setFileName(fileName);
		collectionPrProofDTO.setFilePath(filePath);
		collectionPrProofDTO.setOcrContent(ocrContent);
		collectionPrProofDTO.setOcrDone(isOcrDone);
		collectionPrProofDTO.setDataExtractionDone(isDataExtractionDone);
		
		RecordDTO recordDTO = new RecordDTO();
		recordDTO.setRecordId(recordId);
		collectionPrProofDTO.setRecord(recordDTO);
		return collectionPrProofDTO;
	}

	public static List<CollectionPrProofRequestResponseDTO> getCollectionPrProofRequestResponseDTO(
			List<CollectionPrProofDTO> collectionPrProofDTOs) {
		List<CollectionPrProofRequestResponseDTO> res = new ArrayList<>();
		for (CollectionPrProofDTO collectionPrProofDTO : collectionPrProofDTOs) {
			res.add(new CollectionPrProofRequestResponseDTO(collectionPrProofDTO));
		}
		return res;
	}
	
	public Integer getPrProofId() {
		return prProofId;
	}
	public void setPrProofId(Integer prProofId) {
		this.prProofId = prProofId;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public boolean isOcrDone() {
		return isOcrDone;
	}
	public void setOcrDone(boolean isOcrDone) {
		this.isOcrDone = isOcrDone;
	}
	public String getOcrContent() {
		return ocrContent;
	}
	public void setOcrContent(String ocrContent) {
		this.ocrContent = ocrContent;
	}
	public boolean isDataExtractionDone() {
		return isDataExtractionDone;
	}
	public void setDataExtractionDone(boolean isDataExtractionDone) {
		this.isDataExtractionDone = isDataExtractionDone;
	}
	
	

}
