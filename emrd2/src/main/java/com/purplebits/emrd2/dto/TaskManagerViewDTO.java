package com.purplebits.emrd2.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.purplebits.emrd2.types.TaskStatus;
import com.purplebits.emrd2.util.JsonUtils;


public class TaskManagerViewDTO {

//	 private Long uniqueId;
	    private Integer taskManagerId;
	    private Integer workflowId;
	    private String workflowName;
	    private Integer assignedToUserId;
	    private String assignedToUsername;
	    private String assignedToEmail;
	    private String assignedToFullName;
	    private Integer createdById;
	    private String createdByUsername;
	    private String createdByEmail;
	    private String createdByFullName;
	    private Integer entityId;
	    private String entityName;
	    private String entityDisplayName;
	    private Integer projectId;
	    private String projectName;
	    private String projectDisplayName;
	    private Integer contractId;
	    private String contractName;
	    private Integer stepId;
	    private String stepName;
	    @Enumerated(EnumType.STRING)
		private TaskStatus status;
	    private String priority;
	    private Date startedAt;
	    private Date completedAt;
	    private String comments;
	    private Integer numberOfFiles;
	    private int priorityNumeric;
	    private int statusSortOrder;
	    private Date dueDate;
	    private List<PermissionsDTO> permissions;
//		public Long getUniqueId() {
//			return uniqueId;
//		}
//		public void setUniqueId(Long uniqueId) {
//			this.uniqueId = uniqueId;
//		}
		public Integer getTaskManagerId() {
			return taskManagerId;
		}
		public void setTaskManagerId(Integer taskManagerId) {
			this.taskManagerId = taskManagerId;
		}
		
		public Integer getWorkflowId() {
			return workflowId;
		}
		public void setWorkflowId(Integer workflowId) {
			this.workflowId = workflowId;
		}
		public String getWorkflowName() {
			return workflowName;
		}
		public void setWorkflowName(String workflowName) {
			this.workflowName = workflowName;
		}
		public Integer getAssignedToUserId() {
			return assignedToUserId;
		}
		public void setAssignedToUserId(Integer assignedToUserId) {
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
		public Integer getCreatedById() {
			return createdById;
		}
		public void setCreatedById(Integer createdById) {
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
		public Integer getContractId() {
			return contractId;
		}
		public void setContractId(Integer contractId) {
			this.contractId = contractId;
		}
		public String getContractName() {
			return contractName;
		}
		public void setContractName(String contractName) {
			this.contractName = contractName;
		}
		public Integer getStepId() {
			return stepId;
		}
		public void setStepId(Integer stepId) {
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
		public Date getStartedAt() {
			return startedAt;
		}
		public void setStartedAt(Date startedAt) {
			this.startedAt = startedAt;
		}
		public Date getCompletedAt() {
			return completedAt;
		}
		public void setCompletedAt(Date completedAt) {
			this.completedAt = completedAt;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public Integer getNumberOfFiles() {
			return numberOfFiles;
		}
		public void setNumberOfFiles(Integer numberOfFiles) {
			this.numberOfFiles = numberOfFiles;
		}
		public int getPriorityNumeric() {
			return priorityNumeric;
		}
		public void setPriorityNumeric(int priorityNumeric) {
			this.priorityNumeric = priorityNumeric;
		}
		public int getStatusSortOrder() {
			return statusSortOrder;
		}
		public void setStatusSortOrder(int statusSortOrder) {
			this.statusSortOrder = statusSortOrder;
		}
		public Date getDueDate() {
			return dueDate;
		}
		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}
		public List<PermissionsDTO> getPermissions() {
			return permissions;
		}
		public void setPermissions(List<PermissionsDTO> permissions) {
			this.permissions = permissions;
		}
		@Override
		public String toString() {
			return JsonUtils.createGson().toJson(this);
		}
}
