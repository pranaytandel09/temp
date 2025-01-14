package com.purplebits.emrd2.dto;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.purplebits.emrd2.types.TaskStatus;
import com.purplebits.emrd2.util.JsonUtils;

public class TaskManagerPaginationWithSearchRequestDTO {

	private PaginationRequestDTO paginationRequest;
	private String taskManagerId;
    private String workflowId;
    private String workflowName;
    private String assignedToUserId;
    private String assignedToUsername;
    private String assignedToEmail;
    private String assignedToFullName;
    private String createdById;
    private String createdByUsername;
    private String createdByEmail;
    private String createdByFullName;
    private String entityId;
    private String entityName;
    private String entityDisplayName;
    private String projectId;
    private String projectName;
    private String projectDisplayName;
    private String contractId;
    private String contractName;
    private String stepId;
    private String stepName;
    @Enumerated(EnumType.STRING)
	private TaskStatus status;
    private String priority;
    private String startedAt;
    private String completedAt;
    private String comments;
    private String numberOfFiles;
    private String priorityNumeric;
    private String startDate;
    private String endDate;

	public TaskManagerPaginationWithSearchRequestDTO() {
		super();
	}

	public PaginationRequestDTO getPaginationRequest() {
		return paginationRequest;
	}

	public void setPaginationRequest(PaginationRequestDTO paginationRequest) {
		this.paginationRequest = paginationRequest;
	}

	
	public String getTaskManagerId() {
		return taskManagerId;
	}

	public void setTaskManagerId(String taskManagerId) {
		this.taskManagerId = taskManagerId;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getAssignedToUserId() {
		return assignedToUserId;
	}

	public void setAssignedToUserId(String assignedToUserId) {
		this.assignedToUserId = assignedToUserId;
	}

	public String getAssignedToUsername() {
		return assignedToUsername;
	}

	public void setAssignedToUsername(String assignedToUsername) {
		this.assignedToUsername = assignedToUsername;
	}

	public String getAssignedToEmail() {
		return assignedToEmail;
	}

	public void setAssignedToEmail(String assignedToEmail) {
		this.assignedToEmail = assignedToEmail;
	}

	public String getAssignedToFullName() {
		return assignedToFullName;
	}

	public void setAssignedToFullName(String assignedToFullName) {
		this.assignedToFullName = assignedToFullName;
	}

	public String getCreatedById() {
		return createdById;
	}

	public void setCreatedById(String createdById) {
		this.createdById = createdById;
	}

	public String getCreatedByUsername() {
		return createdByUsername;
	}

	public void setCreatedByUsername(String createdByUsername) {
		this.createdByUsername = createdByUsername;
	}

	public String getCreatedByEmail() {
		return createdByEmail;
	}

	public void setCreatedByEmail(String createdByEmail) {
		this.createdByEmail = createdByEmail;
	}

	public String getCreatedByFullName() {
		return createdByFullName;
	}

	public void setCreatedByFullName(String createdByFullName) {
		this.createdByFullName = createdByFullName;
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

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(String startedAt) {
		this.startedAt = startedAt;
	}

	public String getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(String completedAt) {
		this.completedAt = completedAt;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getNumberOfFiles() {
		return numberOfFiles;
	}

	public void setNumberOfFiles(String numberOfFiles) {
		this.numberOfFiles = numberOfFiles;
	}

	public String getPriorityNumeric() {
		return priorityNumeric;
	}

	public void setPriorityNumeric(String priorityNumeric) {
		this.priorityNumeric = priorityNumeric;
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

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
