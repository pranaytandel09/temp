package com.purplebits.emrd2.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.util.JsonUtils;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersRoleDetailsDTO {

	private RolesDTO roles;
	private List<LocationCategoryPermissionsDTO>locationDetails;

	
	public RolesDTO getRoles() {
		return roles;
	}
	public void setRoles(RolesDTO roles) {
		this.roles = roles;
	}
	public List<LocationCategoryPermissionsDTO> getLocationDetails() {
		return locationDetails;
	}
	public void setLocationDetails(List<LocationCategoryPermissionsDTO> locationDetails) {
		this.locationDetails = locationDetails;
	}
	
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
