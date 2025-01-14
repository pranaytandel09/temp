package com.purplebits.emrd2.service;


import org.springframework.data.jpa.domain.Specification;

import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.request_response.EntitiesRequestResponseDTO;
import com.purplebits.emrd2.entity.Entities;

public interface EntitiesService {

	EntitiesDTO addNewEntity(EntitiesDTO entity);
	EntitiesDTO updateEntity(EntitiesDTO entity);
	EntitiesDTO getEntityById(Integer entityId);
	PaginationResponseDTO<EntitiesRequestResponseDTO> getALLEntities(Integer pageNumber, Integer pageSize, String sortBy, String sortType);
	EntitiesDTO deleteEntity(EntitiesDTO entity);
	
	public PaginationResponseDTO<EntitiesRequestResponseDTO> getAllEntitiesByFilterCriteria(
			Integer pageNumber, Integer pageSize, String sortBy, String sortType, Specification<Entities> spec);
	public String createQuery(String search);
}
