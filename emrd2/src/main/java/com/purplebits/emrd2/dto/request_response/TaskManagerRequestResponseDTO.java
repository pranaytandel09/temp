package com.purplebits.emrd2.dto.request_response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.dto.ContractDTO;
import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.StepDTO;
import com.purplebits.emrd2.dto.TaskManagerDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.dto.WorkflowDTO;
import com.purplebits.emrd2.types.TaskPriority;
import com.purplebits.emrd2.types.TaskStatus;
import com.purplebits.emrd2.util.JsonUtils;
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskManagerRequestResponseDTO {

	private Integer taskManagerId;
	private Integer workflowId;
	private Integer assignedToUserId;
	private Integer createdById;
	private Integer entityId;
	private Integer projectId;
	private Integer contractId;
	private Integer stepId;
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	@Enumerated(EnumType.STRING)
	private TaskPriority priority;
	private Date startedAt;
	private Date completedAt;
	private String comments;
	private Integer numberOfFiles;
	private Date dueDate;

	public TaskManagerRequestResponseDTO() {
		super();
	}

	public TaskManagerRequestResponseDTO(TaskManagerDTO taskManagerDTO) {
		super();
		this.taskManagerId = taskManagerDTO.getTaskManagerId();
		this.workflowId = taskManagerDTO.getWorkflow().getWorkflowId();
		this.assignedToUserId = taskManagerDTO.getAssignedToUser().getUserId();
		this.createdById = taskManagerDTO.getCreatedBy().getUserId();
		this.entityId = taskManagerDTO.getEntity().getEntityId();
		this.projectId = taskManagerDTO.getProject().getProjectId();
		this.contractId = taskManagerDTO.getContract().getContractId();
		this.status = taskManagerDTO.getStatus();
		this.priority = taskManagerDTO.getPriority();
		this.startedAt = taskManagerDTO.getStartedAt();
		this.completedAt = taskManagerDTO.getCompletedAt();
		this.comments = taskManagerDTO.getComments();
		this.numberOfFiles = taskManagerDTO.getNumberOfFiles();
		this.stepId = taskManagerDTO.getStep().getStepId();
		this.dueDate=taskManagerDTO.getDueDate();
	}



	@JsonIgnore
	public TaskManagerDTO getTaskManagerDTO() {
		TaskManagerDTO taskManagerDTO = new TaskManagerDTO();
		taskManagerDTO.setTaskManagerId(taskManagerId);
		taskManagerDTO.setStatus(status);
		taskManagerDTO.setPriority(priority);
		taskManagerDTO.setStartedAt(startedAt);
		taskManagerDTO.setCompletedAt(completedAt);
		taskManagerDTO.setComments(comments);
		taskManagerDTO.setNumberOfFiles(numberOfFiles);
		taskManagerDTO.setDueDate(dueDate);

		WorkflowDTO workflowDTO = new WorkflowDTO();
		workflowDTO.setWorkflowId(workflowId);
		taskManagerDTO.setWorkflow(workflowDTO);

		UsersDTO assignedToUsersDTO = new UsersDTO();
		assignedToUsersDTO.setUserId(assignedToUserId);
		taskManagerDTO.setAssignedToUser(assignedToUsersDTO);

		UsersDTO createdByUsersDTO = new UsersDTO();
		createdByUsersDTO.setUserId(createdById);
		taskManagerDTO.setCreatedBy(createdByUsersDTO);

		EntitiesDTO entitiesDTO = new EntitiesDTO();
		entitiesDTO.setEntityId(entityId);
		taskManagerDTO.setEntity(entitiesDTO);

		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setProjectId(projectId);
		taskManagerDTO.setProject(projectDTO);

		ContractDTO contractDTO = new ContractDTO();
		contractDTO.setContractId(contractId);
		taskManagerDTO.setContract(contractDTO);

		StepDTO stepDTO = new StepDTO();
		stepDTO.setStepId(stepId);
		taskManagerDTO.setStep(stepDTO);
		return taskManagerDTO;
	}

	public static List<TaskManagerRequestResponseDTO> getProjectRequestResponseDTO(
			List<TaskManagerDTO> taskManagerDTOs) {
		List<TaskManagerRequestResponseDTO> res = new ArrayList<>();
		for (TaskManagerDTO taskManagerDTO : taskManagerDTOs) {
			res.add(new TaskManagerRequestResponseDTO(taskManagerDTO));
		}
		return res;
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

	public Integer getAssignedToUserId() {
		return assignedToUserId;
	}

	public void setAssignedToUserId(Integer assignedToUserId) {
		this.assignedToUserId = assignedToUserId;
	}

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
	}

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

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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

	public Integer getStepId() {
		return stepId;
	}

	public void setStepId(Integer stepId) {
		this.stepId = stepId;
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
