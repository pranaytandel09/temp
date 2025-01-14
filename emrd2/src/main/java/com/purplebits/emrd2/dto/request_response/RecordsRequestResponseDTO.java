package com.purplebits.emrd2.dto.request_response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.RecordDTO;

public class RecordsRequestResponseDTO {

	private String recordId;
	private Integer projectId;
	private Integer entityId;
	private int scannedStatus;
	private int ocrStatus;
	private int uploadStatus;
	private int pageCount;
	private int sequenceNumber;
	private int physicalStatus;
	private int boxId;
	private String nameTag;
	private Date doaTag;
	private String ipdNumberTag;
	private String additionalTags;
	private String barcode;
	private String uploadLocation;
	private boolean performeOCR;
	private boolean performeEncryption;
	private String fileType;

	public RecordsRequestResponseDTO() {
		super();
	}

	public RecordsRequestResponseDTO(RecordDTO recordDTO) {
		super();
		this.recordId = recordDTO.getRecordId();
		this.projectId = recordDTO.getProject().getProjectId();
		this.entityId = recordDTO.getEntity().getEntityId();
		this.scannedStatus = recordDTO.getScannedStatus();
		this.ocrStatus = recordDTO.getOcrStatus();
		this.uploadStatus = recordDTO.getUploadStatus();
		this.pageCount = recordDTO.getPageCount();
		this.sequenceNumber = recordDTO.getSequenceNumber();
		this.physicalStatus = recordDTO.getPhysicalStatus();
		this.boxId = recordDTO.getBoxId();
		this.nameTag = recordDTO.getNameTag();
		this.doaTag = recordDTO.getDoaTag();
		this.ipdNumberTag = recordDTO.getIpdNumberTag();
		this.additionalTags = recordDTO.getAdditionalTags();
		this.barcode = recordDTO.getBarcode();
		this.uploadLocation = recordDTO.getUploadLocation();
		this.performeOCR = recordDTO.isPerformeOCR();
		this.performeEncryption = recordDTO.isPerformeEncryption();
	}

	@JsonIgnore
	public RecordDTO getRecordDTO() {
		RecordDTO recordDTO = new RecordDTO();
		recordDTO.setRecordId(recordId);
		recordDTO.setScannedStatus(scannedStatus);
		recordDTO.setOcrStatus(ocrStatus);
		recordDTO.setUploadStatus(uploadStatus);
		recordDTO.setPageCount(pageCount);
		recordDTO.setSequenceNumber(sequenceNumber);
		recordDTO.setPhysicalStatus(physicalStatus);
		recordDTO.setBoxId(boxId);
		recordDTO.setNameTag(nameTag);
		recordDTO.setDoaTag(doaTag);
		recordDTO.setIpdNumberTag(ipdNumberTag);
		recordDTO.setAdditionalTags(additionalTags);
		recordDTO.setBarcode(barcode);
		recordDTO.setUploadLocation(uploadLocation);
		recordDTO.setPerformeOCR(performeOCR);
		recordDTO.setPerformeEncryption(performeEncryption);

		EntitiesDTO entitiesDTO = new EntitiesDTO();
		entitiesDTO.setEntityId(entityId);
		recordDTO.setEntity(entitiesDTO);

		ProjectDTO projectsDTO = new ProjectDTO();
		projectsDTO.setProjectId(projectId);
		recordDTO.setProject(projectsDTO);
		return recordDTO;
	}

	public static List<RecordsRequestResponseDTO> getRecordsRequestResponseDTO(List<RecordDTO> recordDTOs) {
		List<RecordsRequestResponseDTO> res = new ArrayList<>();
		for (RecordDTO record : recordDTOs) {
			res.add(new RecordsRequestResponseDTO(record));
		}
		return res;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public int getScannedStatus() {
		return scannedStatus;
	}

	public void setScannedStatus(int scannedStatus) {
		this.scannedStatus = scannedStatus;
	}

	public int getOcrStatus() {
		return ocrStatus;
	}

	public void setOcrStatus(int ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	public int getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(int uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getPhysicalStatus() {
		return physicalStatus;
	}

	public void setPhysicalStatus(int physicalStatus) {
		this.physicalStatus = physicalStatus;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public String getNameTag() {
		return nameTag;
	}

	public void setNameTag(String nameTag) {
		this.nameTag = nameTag;
	}

	public Date getDoaTag() {
		return doaTag;
	}

	public void setDoaTag(Date doaTag) {
		this.doaTag = doaTag;
	}

	public String getIpdNumberTag() {
		return ipdNumberTag;
	}

	public void setIpdNumberTag(String ipdNumberTag) {
		this.ipdNumberTag = ipdNumberTag;
	}

	public String getAdditionalTags() {
		return additionalTags;
	}

	public void setAdditionalTags(String additionalTags) {
		this.additionalTags = additionalTags;
	}

	public String getBarcode() {
		return barcode;
	}

	public String getUploadLocation() {
		return uploadLocation;
	}

	public void setUploadLocation(String uploadLocation) {
		this.uploadLocation = uploadLocation;
	}

	public void setBarcode(String barcode) {
		barcode = barcode;
	}

	public boolean isPerformeOCR() {
		return performeOCR;
	}

	public void setPerformeOCR(boolean performeOCR) {
		this.performeOCR = performeOCR;
	}

	public boolean isPerformeEncryption() {
		return performeEncryption;
	}

	public void setPerformeEncryption(boolean performeEncryption) {
		this.performeEncryption = performeEncryption;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}
