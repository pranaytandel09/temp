package com.purplebits.emrd2.dto.request_response;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.types.EntityType;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class EntitiesRequestResponseDTO {

	private Integer entityId;
	private String entityName;
	private String entityDisplayName;
	@Enumerated(EnumType.STRING)
	private EntityType entityType;
	private String entityDetails;
	private Byte spokeId;
	private Integer parentEntity;
	private boolean isSuperEntity;
	@Enumerated(EnumType.STRING)
	private Status status;

	public EntitiesRequestResponseDTO() {
		super();
	}

	public EntitiesRequestResponseDTO(EntitiesDTO entity) {
		super();
		this.entityId = entity.getEntityId();
		this.entityName = entity.getEntityName();
		this.entityDisplayName = entity.getEntityDisplayName();
		this.entityType = entity.getEntityType();
		this.entityDetails = entity.getEntityDetails();
		this.spokeId = entity.getSpokeId();
		this.parentEntity = entity.getParentEntity().getEntityId();
		this.isSuperEntity = entity.getIsSuperEntity();
		this.status=entity.getStatus();
	}

	@JsonIgnore
	public EntitiesDTO getEntityDTO() {
		EntitiesDTO entityDTO = new EntitiesDTO();
		entityDTO.setEntityDetails(entityDetails);
		entityDTO.setEntityDisplayName(entityDisplayName);
		entityDTO.setEntityId(entityId);
		entityDTO.setEntityName(entityName);
		entityDTO.setEntityType(entityType);
		entityDTO.setIsSuperEntity(isSuperEntity);
		entityDTO.setSpokeId(spokeId);
		entityDTO.setStatus(status);

		EntitiesDTO parentEntityDTO = new EntitiesDTO();
		parentEntityDTO.setEntityId(parentEntity);
		entityDTO.setParentEntity(parentEntityDTO);

		return entityDTO;
	}

	public static List<EntitiesRequestResponseDTO> getEntityRequestResponseDTO(List<EntitiesDTO> entityDTOS) {
		List<EntitiesRequestResponseDTO> res = new ArrayList<>();
		for (EntitiesDTO entity : entityDTOS) {
			res.add(new EntitiesRequestResponseDTO(entity));
		}
		return res;
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

	public Integer getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(Integer parentEntity) {
		this.parentEntity = parentEntity;
	}

	public boolean isSuperEntity() {
		return isSuperEntity;
	}

	public void setSuperEntity(boolean isSuperEntity) {
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
