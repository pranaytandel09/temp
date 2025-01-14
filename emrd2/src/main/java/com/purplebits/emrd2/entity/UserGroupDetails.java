package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "user_group_details")
public class UserGroupDetails {

	@Id
	@Column(name = "detail_id")
	private Integer detailId;
	@Column(name = "group_id")
	private Integer groupId;

	@Column(name = "group_name")
	private String groupName;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "group_status")
	private String groupStatus;

	@Column(name = "user_ids")
	private String userIds;

	@Column(name = "usernames")
	private String usernames;

	@Column(name = "user_full_names")
	private String userFullNames;

	@Column(name = "detailed_user_mappings")
	private String detailedUserMappings;

	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "role_status")
	private String roleStatus;

	@Column(name = "is_system_role")
	private boolean isSystemRole;

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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUsernames() {
		return usernames;
	}

	public void setUsernames(String usernames) {
		this.usernames = usernames;
	}

	public String getUserFullNames() {
		return userFullNames;
	}

	public void setUserFullNames(String userFullNames) {
		this.userFullNames = userFullNames;
	}

	public String getDetailedUserMappings() {
		return detailedUserMappings;
	}

	public void setDetailedUserMappings(String detailedUserMappings) {
		this.detailedUserMappings = detailedUserMappings;
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

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

	public boolean isSystemRole() {
		return isSystemRole;
	}

	public void setSystemRole(boolean isSystemRole) {
		this.isSystemRole = isSystemRole;
	}

	public Integer getDetailId() {
		return detailId;
	}

	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
