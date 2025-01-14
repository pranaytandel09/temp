package com.purplebits.emrd2.dto.request_response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.purplebits.emrd2.dto.OperationOrganizationDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class UserGroupsRequestResponseDTO {

	private Integer groupId;

	private String groupName;
	private Integer roleId;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	@Enumerated(EnumType.STRING)
	private Status status;
	private String displayName;
	private Integer operationOrganizationId;

	public UserGroupsRequestResponseDTO() {
		super();
	}

	public UserGroupsRequestResponseDTO(UserGroupsDTO userGroupsDTO) {
		super();
		this.groupId = userGroupsDTO.getGroupId();
		this.groupName = userGroupsDTO.getGroupName();
		this.roleId = userGroupsDTO.getRoles().getRoleId();
		this.createdAt = userGroupsDTO.getCreatedAt();
		this.updatedAt = userGroupsDTO.getUpdatedAt();
		this.status = userGroupsDTO.getStatus();
		this.displayName = userGroupsDTO.getDisplayName();
		this.operationOrganizationId = userGroupsDTO.getOperationOrganization().getOperationOrganizationId();
	}

	@JsonIgnore
	public UserGroupsDTO getUserGroupsDTO() {
		UserGroupsDTO userGroupsDTO = new UserGroupsDTO();
		userGroupsDTO.setGroupId(groupId);
		userGroupsDTO.setGroupName(groupName);
		userGroupsDTO.setDisplayName(displayName);
		RolesDTO rolesDTO = new RolesDTO();
		rolesDTO.setRoleId(roleId);
		userGroupsDTO.setRoles(rolesDTO);

		userGroupsDTO.setCreatedAt(createdAt);
		userGroupsDTO.setUpdatedAt(updatedAt);
		userGroupsDTO.setStatus(status);

		OperationOrganizationDTO operationOrganizationDTO = new OperationOrganizationDTO();
		operationOrganizationDTO.setOperationOrganizationId(operationOrganizationId);
		userGroupsDTO.setOperationOrganization(operationOrganizationDTO);
		return userGroupsDTO;
	}

	public static List<UserGroupsRequestResponseDTO> getUserGroupsRequestResponseDTO(
			List<UserGroupsDTO> userGroupsDTOs) {
		List<UserGroupsRequestResponseDTO> res = new ArrayList<>();
		for (UserGroupsDTO userGroupsDTO : userGroupsDTOs) {
			res.add(new UserGroupsRequestResponseDTO(userGroupsDTO));
		}
		return res;
	}

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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
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

	public Integer getOperationOrganizationId() {
		return operationOrganizationId;
	}

	public void setOperationOrganizationId(Integer operationOrganizationId) {
		this.operationOrganizationId = operationOrganizationId;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
