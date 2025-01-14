package com.purplebits.emrd2.dto.request_response;

import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UserGroupDTO;
import com.purplebits.emrd2.util.JsonUtils;

public class UserGroupsResponseDTO {

	private RolesDTO rolesDTO;
	private UserGroupDTO userGroupDTO;

	public RolesDTO getRolesDTO() {
		return rolesDTO;
	}

	public void setRolesDTO(RolesDTO rolesDTO) {
		this.rolesDTO = rolesDTO;
	}

	public UserGroupDTO getUserGroupDTO() {
		return userGroupDTO;
	}


	public void setUserGroupDTO(UserGroupDTO userGroupDTO) {
		this.userGroupDTO = userGroupDTO;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}	
}
