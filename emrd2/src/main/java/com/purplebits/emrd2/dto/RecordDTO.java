package com.purplebits.emrd2.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordDTO {

	private String recordId;
    private ProjectDTO project;
    private EntitiesDTO entity;
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
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public ProjectDTO getProject() {
		return project;
	}
	public void setProject(ProjectDTO project) {
		this.project = project;
	}
	public EntitiesDTO getEntity() {
		return entity;
	}
	public void setEntity(EntitiesDTO entity) {
		this.entity = entity;
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
		this.barcode = barcode;
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
