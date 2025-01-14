package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.purplebits.emrd2.types.EntityType;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "table_entities")
public class Entities {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "entity_id")
	private Integer entityId;

	@Column(name = "entity_name", nullable = false, unique = true)
	private String entityName;

	@Column(name = "entity_disp_name", nullable = true, unique = true)
	private String entityDisplayName;

	@Enumerated(EnumType.STRING)
	@Column(name = "entity_type", nullable = false)
	private EntityType entityType;

	@Column(name = "entity_details")
	private String entityDetails;

	@Column(name = "spoke_id")
	private Byte spokeId;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "parent_entity_id")
	private Entities parentEntity;

	@Column(name = "is_super_entity", nullable = false)
	private boolean isSuperEntity;

	@Enumerated(EnumType.STRING)
	private Status status;

	public Entities(Integer entityId) {
	}

	public Entities() {

	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityDisplayName() {
		return entityDisplayName;
	}

	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public String getEntityDetails() {
		return entityDetails;
	}

	public void setEntityDetails(String entityDetails) {
		this.entityDetails = entityDetails;
	}

	public Byte getSpokeId() {
		return spokeId;
	}

	public void setSpokeId(Byte spokeId) {
		this.spokeId = spokeId;
	}

	public Entities getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(Entities parentEntity) {
		this.parentEntity = parentEntity;
	}

	public boolean getIsSuperEntity() {
		return isSuperEntity;
	}

	public void setIsSuperEntity(boolean isSuperEntity) {
		this.isSuperEntity = isSuperEntity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
