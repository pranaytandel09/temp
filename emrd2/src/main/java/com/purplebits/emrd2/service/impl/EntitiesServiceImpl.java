package com.purplebits.emrd2.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.request_response.EntitiesRequestResponseDTO;
import com.purplebits.emrd2.entity.Entities;
import com.purplebits.emrd2.exceptions.EntityNameAlreadyExistException;
import com.purplebits.emrd2.exceptions.EntityNotFoundException;
import com.purplebits.emrd2.repositories.EntitiesRepository;
import com.purplebits.emrd2.service.EntitiesService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class EntitiesServiceImpl implements EntitiesService {
	private final Logger logger = LogManager.getLogger(EntitiesServiceImpl.class);
	private final String className = EntitiesServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private EntitiesRepository entitiesRepository;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public EntitiesDTO addNewEntity(EntitiesDTO entityDTO) {
		logger.info("method to add new entity invoked." + entityDTO);
		logger.debug(className + " addNewEntity()");
		
		Optional<Entities> entityOp = entitiesRepository.getByEntityName(entityDTO.getEntityName());
		if(entityOp.isPresent() && entityOp.get().getStatus().equals(Status.ACTIVE))
			throw new EntityNameAlreadyExistException(environment.getProperty(ResponseMessages.ENTITY_NAME_EXIST));
		if(entityOp.isPresent() && entityOp.get().getStatus().equals(Status.DELETED))
			entityDTO.setEntityId(entityOp.get().getEntityId());
		
		Entities entity = ObjectMapperUtils.map(entityDTO, Entities.class);
		entity.setStatus(Status.ACTIVE);
		entity=entitiesRepository.saveAndFlush(entity);
		return ObjectMapperUtils.map(entity, EntitiesDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public EntitiesDTO updateEntity(EntitiesDTO entityDTO) {
		logger.info("method to update entity invoked.");
		logger.debug(className + " updateEntity()");
		Optional<Entities> entityOp = entitiesRepository.findById(entityDTO.getEntityId());
		if(!entityOp.isPresent())throw new EntityNotFoundException(environment.getProperty(ResponseMessages.ENTITY_NOT_EXIST));
		
		entityOp = entitiesRepository.getByEntityName(entityDTO.getEntityName());
		if(entityOp.isPresent() && entityOp.get().getStatus().equals(Status.ACTIVE) && entityOp.get().getEntityId()!=entityDTO.getEntityId())
			throw new EntityNameAlreadyExistException(environment.getProperty(ResponseMessages.ENTITY_NAME_EXIST));
		
		if(entityOp.isPresent() && entityOp.get().getStatus().equals(Status.DELETED) && entityOp.get().getEntityId()!=entityDTO.getEntityId())
			entitiesRepository.deleteById(entityOp.get().getEntityId());
		
		Entities entity = ObjectMapperUtils.map(entityDTO, Entities.class);
		entity=entitiesRepository.saveAndFlush(entity);
		return ObjectMapperUtils.map(entity, EntitiesDTO.class);
	}

	@Override
	public EntitiesDTO getEntityById(Integer entityId) {
		logger.info("method to get entity by id invoked.");
		logger.debug(className + " getEntityById()");
		Optional<Entities> entityOp = entitiesRepository.findById(entityId);
		if(!entityOp.isPresent() || entityOp.get().getStatus().equals(Status.DELETED))throw new EntityNotFoundException(environment.getProperty(ResponseMessages.ENTITY_NOT_EXIST));
		
		Entities entity = entityOp.get();
		return ObjectMapperUtils.map(entity, EntitiesDTO.class);
	}

	@Override
	public PaginationResponseDTO<EntitiesRequestResponseDTO> getALLEntities(Integer pageNumber, Integer pageSize, String sortBy, String sortType) {
		logger.info("method to get all entities invoked.");
		logger.debug(className + " getALLEntities()");
		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<EntitiesRequestResponseDTO> pageResponse= new PaginationResponseDTO<>();
	    
		Page<Entities> entitiesPage = entitiesRepository.findByStatus(Status.ACTIVE,pageable);
		List<Entities> entity = entitiesPage.getContent();
	    List<EntitiesDTO> entitiesDTO = ObjectMapperUtils.mapAll(entity, EntitiesDTO.class);
	    pageResponse.setData(EntitiesRequestResponseDTO.getEntityRequestResponseDTO(entitiesDTO) );
	    PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(entitiesPage.getNumber());
		paginationDTO.setLimit(entitiesPage.getSize());
		paginationDTO.setTotalPages(entitiesPage.getTotalPages());
		paginationDTO.setLastPage(entitiesPage.isLast());
		paginationDTO.setTotalCounts(entitiesPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public EntitiesDTO deleteEntity(EntitiesDTO entityDTO) {
		logger.info("method to delete entity invoked.");
		logger.debug(className + " deleteEntity()");
		
		Optional<Entities> entityOp = entitiesRepository.findById(entityDTO.getEntityId());
		if(!entityOp.isPresent() || entityOp.get().getStatus().equals(Status.DELETED))throw new EntityNotFoundException(environment.getProperty(ResponseMessages.ENTITY_NOT_EXIST));
		
		Entities entity = ObjectMapperUtils.map(entityDTO, Entities.class);
		entity.setStatus(Status.DELETED);
		entity=entitiesRepository.saveAndFlush(entity);
		return ObjectMapperUtils.map(entity, EntitiesDTO.class);
	}

	@Override
	public PaginationResponseDTO<EntitiesRequestResponseDTO> getAllEntitiesByFilterCriteria(
			Integer pageNumber, Integer pageSize, String sortBy, String sortType, Specification<Entities> spec) {
		logger.info("method to get all entities search based invoked.");
		logger.debug(className + " getAllEntitiesByFilterCriteria()");
		
	    Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<EntitiesRequestResponseDTO> pageResponse= new PaginationResponseDTO<>();
	    
	    Page<Entities> entitiesPage = entitiesRepository.findAll(spec,pageable);
	    List<Entities> entity = entitiesPage.getContent();
	    List<EntitiesDTO> entitiesDTO = ObjectMapperUtils.mapAll(entity, EntitiesDTO.class);
	    pageResponse.setData(EntitiesRequestResponseDTO.getEntityRequestResponseDTO(entitiesDTO) );
	    PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(entitiesPage.getNumber());
		paginationDTO.setLimit(entitiesPage.getSize());
		paginationDTO.setTotalPages(entitiesPage.getTotalPages());
		paginationDTO.setLastPage(entitiesPage.isLast());
		paginationDTO.setTotalCounts(entitiesPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
	}
	
	@Override
	public String createQuery(String search) {
		String result;
		 StringBuilder actualQuery = new StringBuilder();
	        
	        if (search != null && !search.isEmpty()) {
				
				if (search.contains(",")) {
					List<String> userInputList = Arrays.asList(search.split("\\s*,\\s*"));
					
					actualQuery.append("(");
					for (int i = 0; i < userInputList.size(); i++) {
						if (i == userInputList.size() - 1) {
							actualQuery.append("entityDisplayName ==" + "'*" + userInputList.get(i)).append("*'").append(")").append(";");
						} else {
							actualQuery.append("entityDisplayName ==" + "'*" + userInputList.get(i)).append("*'").append(",");
						}

					}

				} else {
					actualQuery.append("(entityDisplayName ==" + "'*" + search + "*'").append(")").append(";");
				}
		}
	        actualQuery.append("status ==" + Status.ACTIVE).append(";");
			if (actualQuery.charAt(actualQuery.length() - 1) == ';'
					|| actualQuery.charAt(actualQuery.length() - 1) == ',') {
				result = actualQuery.substring(0, actualQuery.length() - 1);
			} else {
				result = actualQuery.toString();
			}
			
			return result;
	}
	
	
}
