package com.purplebits.emrd2.dto.request_response;

import java.util.Map;

public class FileProcessingResponse {

    private String originalFileName;
    private String renamedFileName;
    private Map<String, String> metadata;

    // Constructor, Getters, and Setters


    public FileProcessingResponse(String originalFileName, String renamedFileName, Map<String, String> metadata) {
        this.originalFileName = originalFileName;
        this.renamedFileName = renamedFileName;
        this.metadata = metadata;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getRenamedFileName() {
        return renamedFileName;
    }

    public void setRenamedFileName(String renamedFileName) {
        this.renamedFileName = renamedFileName;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
