package com.purplebits.emrd2.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.purplebits.emrd2.types.EntityType;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntitiesDTO {

    private Integer entityId;
    private String entityName;
    private String entityDisplayName;
    @Enumerated(EnumType.STRING)
    private EntityType entityType;
    private String entityDetails;
    private Byte spokeId;
    private EntitiesDTO parentEntity;
    private boolean isSuperEntity;
    @Enumerated(EnumType.STRING)
	private Status status;

	// No-argument constructor
	public EntitiesDTO() {}

	// All-arguments constructor
	public EntitiesDTO(Integer entityId, String entityName) {
		this.entityId = entityId;
		this.entityName = entityName;
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

	public EntitiesDTO getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(EntitiesDTO parentEntity) {
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
