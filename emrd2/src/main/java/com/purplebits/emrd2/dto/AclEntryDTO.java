package com.purplebits.emrd2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AclEntryDTO {

	private Long id;
	private AclObjectIdentityDTO aclObjectIdentity;
	private Integer aceOrder;
	private AclSidDTO sid;
	private Integer mask;
	private Boolean granting;
	private Boolean auditSuccess;
	private Boolean auditFailure;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAceOrder() {
		return aceOrder;
	}

	public void setAceOrder(Integer aceOrder) {
		this.aceOrder = aceOrder;
	}

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public Boolean getGranting() {
		return granting;
	}

	public void setGranting(Boolean granting) {
		this.granting = granting;
	}

	public Boolean getAuditSuccess() {
		return auditSuccess;
	}

	public void setAuditSuccess(Boolean auditSuccess) {
		this.auditSuccess = auditSuccess;
	}

	public Boolean getAuditFailure() {
		return auditFailure;
	}

	public void setAuditFailure(Boolean auditFailure) {
		this.auditFailure = auditFailure;
	}

	public AclObjectIdentityDTO getAclObjectIdentity() {
		return aclObjectIdentity;
	}

	public void setAclObjectIdentity(AclObjectIdentityDTO aclObjectIdentity) {
		this.aclObjectIdentity = aclObjectIdentity;
	}

	public AclSidDTO getSid() {
		return sid;
	}

	public void setSid(AclSidDTO sid) {
		this.sid = sid;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
