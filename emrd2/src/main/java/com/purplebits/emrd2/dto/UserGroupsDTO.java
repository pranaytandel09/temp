package com.purplebits.emrd2.dto;

import java.sql.Timestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupsDTO {
	private RolesDTO roles;
	private Integer groupId;
	private String groupName;

	private Timestamp createdAt;
	private Timestamp updatedAt;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String displayName;
	private OperationOrganizationDTO operationOrganization;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public RolesDTO getRoles() {
		return roles;
	}

	public void setRoles(RolesDTO roles) {
		this.roles = roles;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public OperationOrganizationDTO getOperationOrganization() {
		return operationOrganization;
	}

	public void setOperationOrganization(OperationOrganizationDTO operationOrganization) {
		this.operationOrganization = operationOrganization;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
