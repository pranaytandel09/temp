package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "user_details_view")
public class UserDetailsView {

	@Id
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "username")
	private String username;

	@Column(name = "email")
	private String email;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "group_ids")
	private String groupIds;

	@Column(name = "group_names")
	private String groupNames;

	@Column(name = "group_display_names")
	private String groupDisplayNames;

	@Column(name = "role_ids")
	private String roleIds;

	@Column(name = "role_names")
	private String roleNames;

	@Column(name = "detailed_group_role_mappings")
	private String detailedGroupRoleMappings;

	@Column(name = "status")
	private String status;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public String getGroupDisplayNames() {
		return groupDisplayNames;
	}

	public void setGroupDisplayNames(String groupDisplayNames) {
		this.groupDisplayNames = groupDisplayNames;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getDetailedGroupRoleMappings() {
		return detailedGroupRoleMappings;
	}

	public void setDetailedGroupRoleMappings(String detailedGroupRoleMappings) {
		this.detailedGroupRoleMappings = detailedGroupRoleMappings;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
