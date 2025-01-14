package com.purplebits.emrd2.dto.request_response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.dto.WorkflowDTO;
import com.purplebits.emrd2.util.JsonUtils;


public class WorkflowRequestResponseDTO {
	private Integer workflowId;
    private String name;
    private String description;
    private Integer createdById;
    private Timestamp createdAt;
    
    public WorkflowRequestResponseDTO() {
		super();
	}

	public WorkflowRequestResponseDTO(WorkflowDTO workflowDTO) {
		super();
		this.workflowId=workflowDTO.getWorkflowId();
		this.name=workflowDTO.getName();
		this.description=workflowDTO.getDescription();
		this.createdById= workflowDTO.getCreatedBy().getUserId();
		this.createdAt= workflowDTO.getCreatedAt();
	}

	@JsonIgnore
	public WorkflowDTO getWorkflowDTO() {
		WorkflowDTO workflowDTO = new WorkflowDTO();
		workflowDTO.setWorkflowId(workflowId);
		workflowDTO.setName(name);
		workflowDTO.setDescription(description);
		workflowDTO.setCreatedAt(createdAt);
		UsersDTO usersDTO = new UsersDTO();
		usersDTO.setUserId(createdById);
		workflowDTO.setCreatedBy(usersDTO);
		return workflowDTO;
	}

	public static List<WorkflowRequestResponseDTO> getWorkflowRequestResponseDTO(List<WorkflowDTO> workflowDTOs) {
		List<WorkflowRequestResponseDTO> res = new ArrayList<>();
		for (WorkflowDTO workflowDTO : workflowDTOs) {
			res.add(new WorkflowRequestResponseDTO(workflowDTO));
		}
		return res;
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

	public Integer getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Integer createdById) {
		this.createdById = createdById;
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
