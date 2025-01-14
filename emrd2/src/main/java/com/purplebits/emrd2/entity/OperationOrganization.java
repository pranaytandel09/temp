package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.purplebits.emrd2.types.OrganizationType;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "operation_organization")
public class OperationOrganization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_organization_id")
    private Integer operationOrganizationId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "organisation_type", nullable = false)
    private OrganizationType organisationType;

    @Column(name = "location")
    private String location;

    @Column(name = "contact_info")
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
