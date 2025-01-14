package com.purplebits.emrd2.dto;

import java.util.Date;

import com.purplebits.emrd2.util.JsonUtils;


public class RecordsViewDTO {

	private String recordId;
	private String barcode;
	private Integer scannedStatus;
	private Integer ocrStatus;
	private Integer uploadStatus;
	private Integer pageCount;
	private Integer sequenceNumber;
	private Integer physicalStatus;
	private Integer boxId;
	private String nameTag;
	private Date doaTag;
	private String ipdNumberTag;
	private String additionalTags;
	private Integer projectId;
	private String projectName;
	private String projectDisplayName;
	private Integer entityId;
	private String entityName;
	private String entityDisplayName;
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getScannedStatus() {
		return scannedStatus;
	}
	public void setScannedStatus(Integer scannedStatus) {
		this.scannedStatus = scannedStatus;
	}
	public Integer getOcrStatus() {
		return ocrStatus;
	}
	public void setOcrStatus(Integer ocrStatus) {
		this.ocrStatus = ocrStatus;
	}
	public Integer getUploadStatus() {
		return uploadStatus;
	}
	public void setUploadStatus(Integer uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public Integer getPhysicalStatus() {
		return physicalStatus;
	}
	public void setPhysicalStatus(Integer physicalStatus) {
		this.physicalStatus = physicalStatus;
	}
	public Integer getBoxId() {
		return boxId;
	}
	public void setBoxId(Integer boxId) {
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
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectDisplayName() {
		return projectDisplayName;
	}
	public void setProjectDisplayName(String projectDisplayName) {
		this.projectDisplayName = projectDisplayName;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityDisplayName() {
		return entityDisplayName;
	}
	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}
	
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
