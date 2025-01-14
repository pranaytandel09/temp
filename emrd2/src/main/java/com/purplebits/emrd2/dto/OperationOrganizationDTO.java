package com.purplebits.emrd2.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.purplebits.emrd2.types.OrganizationType;
import com.purplebits.emrd2.util.JsonUtils;

public class OperationOrganizationDTO {

	private Integer operationOrganizationId;
    private String name;
    @Enumerated(EnumType.STRING)
    private OrganizationType organisationType;
    private String location;
    private String contactInfo;
	public Integer getOperationOrganizationId() {
		return operationOrganizationId;
	}
	public void setOperationOrganizationId(Integer operationOrganizationId) {
		this.operationOrganizationId = operationOrganizationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public OrganizationType getOrganisationType() {
		return organisationType;
	}
	public void setOrganisationType(OrganizationType organisationType) {
		this.organisationType = organisationType;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
    
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
