package com.purplebits.emrd2.dto.request_response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class PermissionsRequestResponseDTO {
	private Integer permissionId;
	@NotBlank
	private String permissionName;
	private boolean isSystemPermission;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	@Enumerated(EnumType.STRING)
	private Status status;
	@NotBlank
	private String displayName;

	public PermissionsRequestResponseDTO() {
		super();
	}

	public PermissionsRequestResponseDTO(PermissionsDTO permissionsDTO) {
		super();
		this.permissionId = permissionsDTO.getPermissionId();
		this.permissionName = permissionsDTO.getPermissionName();
		this.isSystemPermission = permissionsDTO.isSystemPermission();
		this.createdAt = permissionsDTO.getCreatedAt();
		this.updatedAt = permissionsDTO.getUpdatedAt();
		this.status = permissionsDTO.getStatus();
		this.displayName = permissionsDTO.getDisplayName();
	}

	@JsonIgnore
	public PermissionsDTO getPermissionsDTO() {
		PermissionsDTO permissionsDTO = new PermissionsDTO();
		permissionsDTO.setPermissionId(permissionId);
		permissionsDTO.setPermissionName(permissionName);
		permissionsDTO.setSystemPermission(isSystemPermission);
		permissionsDTO.setCreatedAt(createdAt);
		permissionsDTO.setUpdatedAt(updatedAt);
		permissionsDTO.setStatus(status);
		permissionsDTO.setDisplayName(displayName);
		return permissionsDTO;
	}

	public static List<PermissionsRequestResponseDTO> getPermissionsRequestResponseDTO(
			List<PermissionsDTO> permissionsDTOS) {
		List<PermissionsRequestResponseDTO> res = new ArrayList<>();
		for (PermissionsDTO permissionsDTO : permissionsDTOS) {
			res.add(new PermissionsRequestResponseDTO(permissionsDTO));
		}
		return res;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public boolean isSystemPermission() {
		return isSystemPermission;
	}

	public void setSystemPermission(boolean systemPermission) {
		isSystemPermission = systemPermission;
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
