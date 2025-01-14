package com.purplebits.emrd2.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Permissions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer permissionId;

	@NotBlank
	@Column(name = "permission_name", unique = true, nullable = false)
	private String permissionName;
	@Column(name = "is_system_permission", columnDefinition = "bit default 0")
	private boolean isSystemPermission;

	@Column(name = "display_name", nullable = true, unique = true)
	private String displayName;

	@CreationTimestamp
	@JsonIgnore
	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(nullable = true)
	private Integer mask;

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

	public void setSystemPermission(boolean isSystemPermission) {
		this.isSystemPermission = isSystemPermission;
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

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
