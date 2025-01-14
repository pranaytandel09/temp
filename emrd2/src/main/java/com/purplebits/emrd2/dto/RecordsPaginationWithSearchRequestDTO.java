package com.purplebits.emrd2.dto;
import com.purplebits.emrd2.util.JsonUtils;

public class RecordsPaginationWithSearchRequestDTO {

	private String recordId;
	private String barcode;
	private String scannedStatus;
	private String ocrStatus;
	private String uploadStatus;
	private String pageCount;
	private String sequenceNumber;
	private String physicalStatus;
	private String boxId;
	private String nameTag;
	private String ipdNumberTag;
	private String additionalTags;
	private String projectId;
	private String projectName;
	private String projectDisplayName;
	private String entityId;
	private String entityName;
	private String entityDisplayName;
	private String startDate;
	private String endDate;
	private PaginationRequestDTO paginationRequest;

	public RecordsPaginationWithSearchRequestDTO() {
		super();
	}

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

	public String getScannedStatus() {
		return scannedStatus;
	}

	public void setScannedStatus(String scannedStatus) {
		this.scannedStatus = scannedStatus;
	}

	public String getOcrStatus() {
		return ocrStatus;
	}

	public void setOcrStatus(String ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getPhysicalStatus() {
		return physicalStatus;
	}

	public void setPhysicalStatus(String physicalStatus) {
		this.physicalStatus = physicalStatus;
	}

	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public String getNameTag() {
		return nameTag;
	}

	public void setNameTag(String nameTag) {
		this.nameTag = nameTag;
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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
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

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	

	public PaginationRequestDTO getPaginationRequest() {
		return paginationRequest;
	}

	public void setPaginationRequest(PaginationRequestDTO paginationRequest) {
		this.paginationRequest = paginationRequest;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
