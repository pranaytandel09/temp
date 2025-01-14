package com.purplebits.emrd2.dto;

import com.purplebits.emrd2.util.JsonUtils;

public class UserDetailsViewDTO {

	private Integer userId;
	private String username;
	private String email;
	private String fullName;
	private String groupIds;
	private String groupNames;
	private String groupDisplayNames;
	private String roleIds;
	private String roleNames;
	private String detailedGroupRoleMappings;
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
