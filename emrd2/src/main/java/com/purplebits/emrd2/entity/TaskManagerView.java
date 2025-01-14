package com.purplebits.emrd2.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.purplebits.emrd2.types.TaskStatus;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "task_manager_view")
public class TaskManagerView {

//    @Id
//    @Column(name = "unique_id")
//    private Long uniqueId;

	@Id
    @Column(name = "task_manager_id")
    private Integer taskManagerId;

    @Column(name = "workflow_id")
    private Integer workflowId;

    @Column(name = "workflow_name")
    private String workflowName;

    @Column(name = "assigned_to_user_id")
    private Integer assignedToUserId;

    @Column(name = "assigned_to_username")
    private String assignedToUsername;

    @Column(name = "assigned_to_email")
    private String assignedToEmail;

    @Column(name = "assigned_to_full_name")
    private String assignedToFullName;

    @Column(name = "created_by_id")
    private Integer createdById;

    @Column(name = "created_by_username")
    private String createdByUsername;

    @Column(name = "created_by_email")
    private String createdByEmail;

    @Column(name = "created_by_full_name")
    private String createdByFullName;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_display_name")
    private String entityDisplayName;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_display_name")
    private String projectDisplayName;

    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "contract_name")
    private String contractName;

    @Column(name = "step_id")
    private Integer stepId;

    @Column(name = "step_name")
    private String stepName;

    @Enumerated(EnumType.STRING)
	private TaskStatus status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "started_at")
    private Date startedAt;

    @Column(name = "completed_at")
    private Date completedAt;

    @Column(name = "comments")
    private String comments;

    @Column(name = "number_of_files")
    private Integer numberOfFiles;
    
    @Column(name = "priority_numeric")
    private int priorityNumeric;
    
    @Column(name = "status_sort_order")
    private int statusSortOrder;
    
    @Column(name = "due_date")
    private Date dueDate;

//	public Long getUniqueId() {
//		return uniqueId;
//	}
//
//	public void setUniqueId(Long uniqueId) {
//		this.uniqueId = uniqueId;
//	}

	

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

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

}
