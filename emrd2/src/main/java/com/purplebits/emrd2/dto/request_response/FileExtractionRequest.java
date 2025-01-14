package com.purplebits.emrd2.dto.request_response;

import java.util.List;

public class FileExtractionRequest {
    private Integer entityId;
    private List<String> fileNames;

    // Getters and setters
    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }
}