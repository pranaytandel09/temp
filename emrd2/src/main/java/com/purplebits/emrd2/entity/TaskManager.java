package com.purplebits.emrd2.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.TaskPriority;
import com.purplebits.emrd2.types.TaskStatus;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "TaskManager")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskManager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TaskManagerID")
	private Integer taskManagerId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "WorkflowID", nullable = false)
	private Workflow workflow;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "AssignedToUserID", nullable = false)
	private Users assignedToUser;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CreatedBy", nullable = false)
	private Users createdBy;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "EntityID", nullable = false)
	private Entities entity;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ProjectID", nullable = false)
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ContractID", nullable = false)
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "StepId", nullable = true)
	private Step step;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status", nullable = false)
	private TaskStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "Priority", nullable = false)
	private TaskPriority priority;
	
	@CreationTimestamp
	@Column(name = "StartedAt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startedAt;
	
	@Column(name = "CompletedAt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedAt;

	@Column(name = "Comments")
	private String comments;

	@Column(name = "NumberOfFiles")
	private Integer numberOfFiles;
	
	@Column(name = "due_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dueDate;

	public Integer getTaskManagerId() {
		return taskManagerId;
	}

	public void setTaskManagerId(Integer taskManagerId) {
		this.taskManagerId = taskManagerId;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	public Users getAssignedToUser() {
		return assignedToUser;
	}

	public void setAssignedToUser(Users assignedToUser) {
		this.assignedToUser = assignedToUser;
	}

	public Users getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Users createdBy) {
		this.createdBy = createdBy;
	}

	public Entities getEntity() {
		return entity;
	}

	public void setEntity(Entities entity) {
		this.entity = entity;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
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

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
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
