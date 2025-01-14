package com.purplebits.emrd2.dto.request_response;

import java.util.List;

import com.purplebits.emrd2.util.JsonUtils;


public class RolesWithPermissionRequestRequestDTO {

	private RolesRequestResponseDTO roles;
	private List<RolePermissionsRequestResponseDTO> permissions ;
	
	public RolesRequestResponseDTO getRoles() {
		return roles;
	}
	public void setRoles(RolesRequestResponseDTO roles) {
		this.roles = roles;
	}
	public List<RolePermissionsRequestResponseDTO> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<RolePermissionsRequestResponseDTO> permissions) {
		this.permissions = permissions;
	}
	
	 @Override
	    public String toString() {
	        return JsonUtils.createGson().toJson(this);
	    }
}
