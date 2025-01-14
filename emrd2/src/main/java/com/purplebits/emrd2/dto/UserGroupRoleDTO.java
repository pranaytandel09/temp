package com.purplebits.emrd2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupRoleDTO {
	private UserGroupDTO userGroup;
	private RolesDTO role;
	
	
	public UserGroupDTO getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroupDTO userGroup) {
		this.userGroup = userGroup;
	}

	public RolesDTO getRole() {
		return role;
	}

	public void setRole(RolesDTO role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
