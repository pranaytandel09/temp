package com.purplebits.emrd2.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "records_view")
public class RecordsView {

    @Id
    @Column(name = "record_id")
    private String recordId;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "scanned_status")
    private Integer scannedStatus;

    @Column(name = "ocr_status")
    private Integer ocrStatus;

    @Column(name = "upload_status")
    private Integer uploadStatus;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;

    @Column(name = "physical_status")
    private Integer physicalStatus;

    @Column(name = "box_id")
    private Integer boxId;

    @Column(name = "name_tag")
    private String nameTag;

    @Column(name = "doa_tag")
    private Date doaTag;

    @Column(name = "ipd_number_tag")
    private String ipdNumberTag;

    @Column(name = "additional_tags")
    private String additionalTags;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_disp_name")
    private String projectDisplayName;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_disp_name")
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
