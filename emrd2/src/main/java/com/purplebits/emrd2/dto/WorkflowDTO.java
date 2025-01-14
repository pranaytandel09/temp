package com.purplebits.emrd2.dto;

import java.sql.Timestamp;

import com.purplebits.emrd2.util.JsonUtils;


public class WorkflowDTO {

	private Integer workflowId;
    private String name;
    private String description;
    private UsersDTO createdBy;
    private Timestamp createdAt;

	// No-argument constructor
	public WorkflowDTO() {}

	// All-arguments constructor
	public WorkflowDTO(Integer workflowId, String name) {
		this.workflowId = workflowId;
		this.name = name;
	}
    public Integer getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UsersDTO getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UsersDTO createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
    
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
