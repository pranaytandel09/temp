package com.purplebits.emrd2.dto.request_response;

import java.util.List;

import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.util.JsonUtils;


public class UserMappedUserGroupRequestResponseDTO {

	 private UsersDTO user; 
	    private List<UserGroupsDTO> userGroups; 

	    public UserMappedUserGroupRequestResponseDTO() {}

	    public UserMappedUserGroupRequestResponseDTO(UsersDTO user, List<UserGroupsDTO> userGroups) {
	        this.user = user;
	        this.userGroups = userGroups;
	    }

	    public UsersDTO getUser() {
	        return user;
	    }

	    public void setUser(UsersDTO user) {
	        this.user = user;
	    }

	    public List<UserGroupsDTO> getUserGroups() {
	        return userGroups;
	    }

	    public void setUserGroups(List<UserGroupsDTO> userGroups) {
	        this.userGroups = userGroups;
	    }

	    @Override
	    public String toString() {
	        return JsonUtils.createGson().toJson(this);
	    }
}