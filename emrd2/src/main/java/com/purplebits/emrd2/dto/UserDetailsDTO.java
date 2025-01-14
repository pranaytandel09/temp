package com.purplebits.emrd2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.util.JsonUtils;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetailsDTO {
	private UsersDTO user;
	private List<UserGroupRoleDTO> userGroupRoles;
	public UsersDTO getUser() {
		return user;
	}
	public void setUser(UsersDTO user) {
		this.user = user;
	}
	public List<UserGroupRoleDTO> getUserGroupRoles() {
		return userGroupRoles;
	}
	public void setUserGroupRoles(List<UserGroupRoleDTO> userGroupRoles) {
		this.userGroupRoles = userGroupRoles;
	}
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
