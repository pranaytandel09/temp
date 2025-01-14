package com.purplebits.emrd2.dto.request_response;

import java.util.Map;

public class OcrDataRequestResponseDTO {

	private String barcodeId;
    private Map<String, String> ocrExtractedData;
    
	public String getBarcodeId() {
		return barcodeId;
	}
	public void setBarcodeId(String barcodeId) {
		this.barcodeId = barcodeId;
	}
	public Map<String, String> getOcrExtractedData() {
		return ocrExtractedData;
	}
	public void setOcrExtractedData(Map<String, String> ocrExtractedData) {
		this.ocrExtractedData = ocrExtractedData;
	}
    
    
    
}
