package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "acl_object_identity")
public class AclObjectIdentity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer objectIdIdentity; // Object ID (e.g., File ID)

	@ManyToOne
	@JoinColumn(name = "object_id_class", nullable = false)
	private AclClass aclClass; // Refers to acl_class

	@ManyToOne
	@JoinColumn(name = "owner_sid")
	private AclSid ownerSid; // Refers to acl_sid (Owner)

	@Column(nullable = false)
	private Boolean entriesInheriting = true;

	@Column(name = "parent_object", nullable = true)
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

	public AclClass getAclClass() {
		return aclClass;
	}

	public void setAclClass(AclClass aclClass) {
		this.aclClass = aclClass;
	}

	public AclSid getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(AclSid ownerSid) {
		this.ownerSid = ownerSid;
	}

	public Boolean getEntriesInheriting() {
		return entriesInheriting;
	}

	public void setEntriesInheriting(Boolean entriesInheriting) {
		this.entriesInheriting = entriesInheriting;
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
