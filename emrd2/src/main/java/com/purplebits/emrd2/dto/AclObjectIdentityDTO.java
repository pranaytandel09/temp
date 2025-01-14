package com.purplebits.emrd2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AclObjectIdentityDTO {

	private Long id;
	private Integer objectIdIdentity; // Object ID (e.g., File ID)
	private AclClassDTO aclClass; // Refers to acl_class
	private AclSidDTO ownerSid; // Refers to acl_sid (Owner)
	private Boolean entriesInheriting = true;
	private Long parentObject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getObjectIdIdentity() {
		return objectIdIdentity;
	}

	public void setObjectIdIdentity(Integer id) {
		this.objectIdIdentity = id;
	}

	public Boolean getEntriesInheriting() {
		return entriesInheriting;
	}

	public void setEntriesInheriting(Boolean entriesInheriting) {
		this.entriesInheriting = entriesInheriting;
	}

	public AclClassDTO getAclClass() {
		return aclClass;
	}

	public void setAclClass(AclClassDTO aclClass) {
		this.aclClass = aclClass;
	}

	public AclSidDTO getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(AclSidDTO ownerSid) {
		this.ownerSid = ownerSid;
	}

	public Long getParentObject() {
		return parentObject;
	}

	public void setParentObject(Long parentObject) {
		this.parentObject = parentObject;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
