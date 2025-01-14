package com.purplebits.emrd2.dto;

import java.sql.Timestamp;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupMembershipDTO {

	private Integer membershipId;
	private UsersDTO users;
	private UserGroupsDTO groups;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private OperationOrganizationDTO operationOrganization;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Integer getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public UsersDTO getUsers() {
		return users;
	}

	public void setUsers(UsersDTO users) {
		this.users = users;
	}

	public UserGroupsDTO getGroups() {
		return groups;
	}

	public void setGroups(UserGroupsDTO groups) {
		this.groups = groups;
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
