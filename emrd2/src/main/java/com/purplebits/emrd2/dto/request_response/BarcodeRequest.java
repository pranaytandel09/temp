package com.purplebits.emrd2.dto.request_response;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class BarcodeRequest {

    @NotNull(message = "Entity ID is required.")
    private Integer entityId;

    @NotNull(message = "Project ID is required.")
    private Integer projectId;

    @NotNull(message = "Count is required.")
    @Min(value = 1, message = "Count must be at least 1.")
    private Integer count;

    // Getters and Setters
    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}