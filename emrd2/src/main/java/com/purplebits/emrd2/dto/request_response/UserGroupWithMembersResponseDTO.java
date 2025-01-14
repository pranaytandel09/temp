package com.purplebits.emrd2.dto.request_response;

import java.util.List;

import com.purplebits.emrd2.util.JsonUtils;

public class UserGroupWithMembersResponseDTO {

	private UserGroupsRequestResponseDTO userGroup;
	private List<UserGroupMembershipRequestResponseDTO> usersGroupMembership;

	public UserGroupsRequestResponseDTO getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroupsRequestResponseDTO userGroup) {
		this.userGroup = userGroup;
	}

	public List<UserGroupMembershipRequestResponseDTO> getUsersGroupMembership() {
		return usersGroupMembership;
	}

	public void setUsersGroupMembership(List<UserGroupMembershipRequestResponseDTO> usersGroupMembership) {
		this.usersGroupMembership = usersGroupMembership;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
