package com.purplebits.emrd2.dto.request_response;

import java.util.List;

import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.util.JsonUtils;


public class UserGroupWithUsersResponseDTO {
	
	private UserGroupsDTO userGroup;
    private List<UsersDTO> users;

    public UserGroupsDTO getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupsDTO userGroup) {
        this.userGroup = userGroup;
    }

    public List<UsersDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UsersDTO> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return JsonUtils.createGson().toJson(this);
    }

}
