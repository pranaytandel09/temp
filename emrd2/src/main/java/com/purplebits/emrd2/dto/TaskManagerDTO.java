package com.purplebits.emrd2.dto;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.entity.TaskManager;
import com.purplebits.emrd2.types.TaskPriority;
import com.purplebits.emrd2.types.TaskStatus;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskManagerDTO {

	private Integer taskManagerId;
	private WorkflowDTO workflow;
	private UsersDTO assignedToUser;
	private UsersDTO createdBy;
	private EntitiesDTO entity;
	private ProjectDTO project;
	private ContractDTO contract;
	private StepDTO step;
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	@Enumerated(EnumType.STRING)
	private TaskPriority priority;
	private Date startedAt;
	private Date completedAt;
	private String comments;
	private Integer numberOfFiles;
	private Date dueDate;

	public Integer getTaskManagerId() {
		return taskManagerId;
	}

	public void setTaskManagerId(Integer taskManagerId) {
		this.taskManagerId = taskManagerId;
	}

	public WorkflowDTO getWorkflow() {
		return workflow;
	}

	public void setWorkflow(WorkflowDTO workflow) {
		this.workflow = workflow;
	}

	public UsersDTO getAssignedToUser() {
		return assignedToUser;
	}

	public void setAssignedToUser(UsersDTO assignedToUser) {
		this.assignedToUser = assignedToUser;
	}

	public UsersDTO getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UsersDTO createdBy) {
		this.createdBy = createdBy;
	}

	public EntitiesDTO getEntity() {
		return entity;
	}

	public void setEntity(EntitiesDTO entity) {
		this.entity = entity;
	}

	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	public ContractDTO getContract() {
		return contract;
	}

	public void setContract(ContractDTO contract) {
		this.contract = contract;
	}

	public StepDTO getStep() {
		return step;
	}

	public void setStep(StepDTO step) {
		this.step = step;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
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
	
	

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
