package com.purplebits.emrd2.entity;

import java.sql.Timestamp;

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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "action_permissions_mapping", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"role_id", "permission_id"})
	})
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolePermissionMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rolePermissionMappingId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="role_id")
	private Roles roles;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="permission_id")
	private Permissions permissions;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt;
	
	@Enumerated(EnumType.STRING)
	private Status status;

	public Integer getRolePermissionMappingId() {
		return rolePermissionMappingId;
	}

	public void setRolePermissionMappingId(Integer rolePermissionMappingId) {
		this.rolePermissionMappingId = rolePermissionMappingId;
	}

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}

	public Permissions getPermissions() {
		return permissions;
	}

	public void setPermissions(Permissions permissions) {
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

