package com.purplebits.emrd2.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "records")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordEntity {

    @Id
    @Column(name = "record_id")
    private String recordId;
    
    @Column(name = "barcode", unique = true, nullable = false)
	private String barcode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_id", nullable = false)
    private Entities entity;

    @Column(name = "scanned_status")
    private int scannedStatus;

    @Column(name = "ocr_status")
    private int ocrStatus;

    @Column(name = "upload_status")
    private int uploadStatus;

    @Column(name = "page_count")
    private int pageCount;
    
    @Column(name = "upload_location")
    private String uploadLocation;

    
    @Column(name = "sequence_number")
    private int sequenceNumber;

    @Column(name = "physical_status")
    private int physicalStatus;

    @Column(name = "box_id")
    private int boxId;

    @Column(name = "name_tag")
    private String nameTag;

    @Column(name = "doa_tag")
    private Date doaTag;

    @Column(name = "ipd_number_tag")
    private String ipdNumberTag;

    @Lob
    @Column(name = "additional_tags",columnDefinition = "nText")
    private String additionalTags;
    
    @Column(name = "performe_ocr", columnDefinition = "bit default 1")
	private boolean performeOCR;
    
    @Column(name = "performe_encryption", columnDefinition = "bit default 1")
	private boolean performeEncryption;

    @Column(name = "file_type", columnDefinition = "TEXT")
	private String fileType;
    
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Entities getEntity() {
		return entity;
	}

	public void setEntity(Entities entity) {
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

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


}
