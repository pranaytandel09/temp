package com.purplebits.emrd2.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RolePermissionMappingDTO {

	private Integer rolePermissionMappingId;
	private RolesDTO roles;
	private PermissionsDTO permissions;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Status status;

	public Integer getRolePermissionMappingId() {
		return rolePermissionMappingId;
	}

	public void setRolePermissionMappingId(Integer rolePermissionMappingId) {
		this.rolePermissionMappingId = rolePermissionMappingId;
	}

	public RolesDTO getRoles() {
		return roles;
	}

	public void setRoles(RolesDTO roles) {
		this.roles = roles;
	}

	public PermissionsDTO getPermissions() {
		return permissions;
	}

	public void setPermissions(PermissionsDTO permissions) {
		this.permissions = permissions;
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
	
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
