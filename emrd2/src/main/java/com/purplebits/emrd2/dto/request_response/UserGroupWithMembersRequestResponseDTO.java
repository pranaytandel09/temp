package com.purplebits.emrd2.dto.request_response;

import java.util.List;

import com.purplebits.emrd2.util.JsonUtils;


public class UserGroupWithMembersRequestResponseDTO {

	private UserGroupsRequestResponseDTO userGroup;
	private List<UsersRequestResponseDTO> users;

	public UserGroupsRequestResponseDTO getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroupsRequestResponseDTO userGroup) {
		this.userGroup = userGroup;
	}

	public List<UsersRequestResponseDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UsersRequestResponseDTO> users) {
		this.users = users;
	}
	
	@Override
    public String toString() {
        return JsonUtils.createGson().toJson(this);
    }
	
}
