package com.purplebits.emrd2.dto.request_response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class RolesRequestResponseDTO {
	private Integer roleId;
	@NotBlank
	private String roleName;
	private boolean isSystemRole;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	@Enumerated(EnumType.STRING)
	private Status status;

	public RolesRequestResponseDTO() {
		super();
	}

	public RolesRequestResponseDTO(RolesDTO rolesDTO) {
		super();
		this.roleId = rolesDTO.getRoleId();
		this.roleName = rolesDTO.getRoleName();
		this.status = rolesDTO.getStatus();
		this.isSystemRole = rolesDTO.isSystemRole();
		this.createdAt = rolesDTO.getCreatedAt();
		this.updatedAt = rolesDTO.getUpdatedAt();

	}

	@JsonIgnore
	public RolesDTO getRolesDTO() {
		RolesDTO rolesDTO = new RolesDTO();
		rolesDTO.setRoleName(roleName);
		rolesDTO.setRoleId(roleId);
		rolesDTO.setStatus(status);
		rolesDTO.setSystemRole(isSystemRole);
		rolesDTO.setCreatedAt(createdAt);
		rolesDTO.setUpdatedAt(updatedAt);
		return rolesDTO;
	}

	public static List<RolesRequestResponseDTO> getRolesRequestResponseDTO(List<RolesDTO> rolesDTOs) {
		List<RolesRequestResponseDTO> res = new ArrayList<>();
		for (RolesDTO rolesDTO : rolesDTOs) {
			res.add(new RolesRequestResponseDTO(rolesDTO));
		}
		return res;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isSystemRole() {
		return isSystemRole;
	}

	public void setSystemRole(boolean systemRole) {
		isSystemRole = systemRole;
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

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
