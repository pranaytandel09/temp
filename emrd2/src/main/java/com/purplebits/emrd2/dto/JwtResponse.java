package com.purplebits.emrd2.dto;

import java.util.List;

import com.purplebits.emrd2.util.JsonUtils;

public class JwtResponse {
	
	private  UsersDTO logedInUser;
	private  String token;
	private String refershToken;
	List<PermissionsDTO>permissions;

	public String getRefershToken() {
		return refershToken;
	}
	public void setRefershToken(String refershToken) {
		this.refershToken = refershToken;
	}
	public UsersDTO getLogedInUser() {
		return logedInUser;
	}
	public void setLogedInUser(UsersDTO logedInUser) {
		this.logedInUser = logedInUser;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	

	public List<PermissionsDTO> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<PermissionsDTO> permissions) {
		this.permissions = permissions;
	}
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
