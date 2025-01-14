package com.purplebits.emrd2.dto.request_response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.purplebits.emrd2.dto.OperationOrganizationDTO;
import com.purplebits.emrd2.dto.UserGroupMembershipDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class UserGroupMembershipRequestResponseDTO {

	private Integer membershipId;
	private Integer userId;
	private Integer groupId;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Integer operationOrganizationId;

	@Enumerated(EnumType.STRING)
	private Status status;

	public UserGroupMembershipRequestResponseDTO() {
		super();
	}

	public UserGroupMembershipRequestResponseDTO(UserGroupMembershipDTO userGroupMembershipDTO) {
		super();
		this.membershipId = userGroupMembershipDTO.getMembershipId();
		this.userId = userGroupMembershipDTO.getUsers().getUserId();
		this.groupId = userGroupMembershipDTO.getGroups().getGroupId();
		this.createdAt = userGroupMembershipDTO.getCreatedAt();
		this.updatedAt = userGroupMembershipDTO.getUpdatedAt();
		this.status = userGroupMembershipDTO.getStatus();
		this.operationOrganizationId = userGroupMembershipDTO.getOperationOrganization().getOperationOrganizationId();
	}

	public static List<UserGroupMembershipRequestResponseDTO> getUserGroupMembershipRequestResponseDTO(
			List<UserGroupMembershipDTO> userGroupMembershipDTOs) {
		List<UserGroupMembershipRequestResponseDTO> res = new ArrayList<>();
		for (UserGroupMembershipDTO userGroupMembershipDTO : userGroupMembershipDTOs) {
			res.add(new UserGroupMembershipRequestResponseDTO(userGroupMembershipDTO));
		}
		return res;
	}

	@JsonIgnore
	public UserGroupMembershipDTO getUserGroupMembershipDTO() {
		UserGroupMembershipDTO userGroupMembershipDTO = new UserGroupMembershipDTO();
		userGroupMembershipDTO.setMembershipId(membershipId);

		UsersDTO usersDTO = new UsersDTO();
		usersDTO.setUserId(userId);
		userGroupMembershipDTO.setUsers(usersDTO);

		UserGroupsDTO groupsDTO = new UserGroupsDTO();
		groupsDTO.setGroupId(groupId);
		userGroupMembershipDTO.setGroups(groupsDTO);

		OperationOrganizationDTO operationOrganizationDTO = new OperationOrganizationDTO();
		operationOrganizationDTO.setOperationOrganizationId(operationOrganizationId);
		userGroupMembershipDTO.setOperationOrganization(operationOrganizationDTO);

		userGroupMembershipDTO.setCreatedAt(createdAt);
		userGroupMembershipDTO.setUpdatedAt(updatedAt);
		userGroupMembershipDTO.setStatus(status);
		return userGroupMembershipDTO;
	}

	public Integer getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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
