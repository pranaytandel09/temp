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
@Table(name = "user_group_membership", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "user_id", "group_id" }) })
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupMembership {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer membershipId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private Users users;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "group_id")
	private UserGroups groups;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "operation_organization_id")
	private OperationOrganization operationOrganization;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private Timestamp createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Integer getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public UserGroups getGroups() {
		return groups;
	}

	public void setGroups(UserGroups groups) {
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
