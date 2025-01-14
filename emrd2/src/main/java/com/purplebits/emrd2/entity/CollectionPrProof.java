package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "collection_pr_proof")
public class CollectionPrProof {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pr_proof_id")
	private Integer prProofId;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "record_id", nullable = false)
	private RecordEntity record;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "file_path")
	private String filePath;
	@Column(name = "is_ocr_done",columnDefinition = "bit default 0")
	private boolean isOcrDone;
	@Lob
    @Column(name = "ocr_content",columnDefinition = "nText")
    private String ocrContent;
	@Column(name = "is_data_extraction_done",columnDefinition = "bit default 0")
	private boolean isDataExtractionDone;
	
	public Integer getPrProofId() {
		return prProofId;
	}
	public void setPrProofId(Integer prProofId) {
		this.prProofId = prProofId;
	}
	
	public RecordEntity getRecord() {
		return record;
	}
	public void setRecord(RecordEntity record) {
		this.record = record;
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
	
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
