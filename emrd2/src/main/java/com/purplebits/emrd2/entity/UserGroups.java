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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "user_groups")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroups {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer groupId;

	@Column(name = "group_name", nullable = false, unique = true)
	private String groupName;

	@Column(name = "display_name", nullable = false, unique = true)
	private String displayName;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "role_id")
	private Roles roles;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "operation_organization_id")
	private OperationOrganization operationOrganization;

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

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
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

	public OperationOrganization getOperationOrganization() {
		return operationOrganization;
	}

	public void setOperationOrganization(OperationOrganization operationOrganization) {
		this.operationOrganization = operationOrganization;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
