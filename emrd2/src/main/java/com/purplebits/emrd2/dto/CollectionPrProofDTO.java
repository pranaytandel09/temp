package com.purplebits.emrd2.dto;

import com.purplebits.emrd2.util.JsonUtils;

public class CollectionPrProofDTO {

	private Integer prProofId;
	private RecordDTO record;
	private String fileName;
	private String filePath;
	private boolean isOcrDone;
	private String ocrContent;
	private boolean isDataExtractionDone;
	public Integer getPrProofId() {
		return prProofId;
	}
	public void setPrProofId(Integer prProofId) {
		this.prProofId = prProofId;
	}
	
	public RecordDTO getRecord() {
		return record;
	}
	public void setRecord(RecordDTO record) {
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
