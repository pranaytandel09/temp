package com.purplebits.emrd2.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.request_response.ProjectsRequestResponseDTO;
import com.purplebits.emrd2.entity.Entities;
import com.purplebits.emrd2.entity.Project;
import com.purplebits.emrd2.exceptions.EntityNameAlreadyExistException;
import com.purplebits.emrd2.exceptions.ProjectNameAlreadyExistException;
import com.purplebits.emrd2.exceptions.ProjectNotFoundException;
import com.purplebits.emrd2.repositories.ProjectRepository;
import com.purplebits.emrd2.service.ProjectService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.Encryptor;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class ProjectServiceImpl implements ProjectService {

	private final Logger logger = LogManager.getLogger(ProjectServiceImpl.class);
	private final String className = ProjectServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private ProjectRepository projectRepository;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public ProjectDTO addNewProject(ProjectDTO projectDTO) {
		logger.info("method to add new project invoked." + projectDTO);
		logger.debug(className + " addNewProject()");
		
		Optional<Project> projectOp = projectRepository.getByProjectNameAndEntityId(projectDTO.getProjectName(), projectDTO.getEntity().getEntityId());
		if(projectOp.isPresent())
			throw new ProjectNameAlreadyExistException(environment.getProperty(ResponseMessages.PROJECT_NAME_EXIST));
		
		projectOp = projectRepository.getByProjectDisplayNameAndEntityId(projectDTO.getProjectDisplayName(),projectDTO.getEntity().getEntityId());
		if(projectOp.isPresent())
			throw new ProjectNameAlreadyExistException(environment.getProperty(ResponseMessages.PROJECT_DISPLAY_NAME_EXIST));
		
		Project project = ObjectMapperUtils.map(projectDTO, Project.class);
		Encryptor encryptor= new Encryptor();
		String password = encryptor.generatePassword();
		project.setProjectPassword(encryptor.encryptPassword(password));
		projectRepository.saveAndFlush(project);
		
		return ObjectMapperUtils.map(project, ProjectDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public ProjectDTO updateProject(ProjectDTO projectDTO) {
		logger.info("method to update project invoked." + projectDTO);
		logger.debug(className + " updateProject()");
		
		Optional<Project> projectOp = projectRepository.findById(projectDTO.getProjectId());
		if(!projectOp.isPresent())throw new ProjectNotFoundException(environment.getProperty(ResponseMessages.PROJECT_NOT_EXIST));
		
		projectOp = projectRepository.getByProjectNameAndEntityId(projectDTO.getProjectName(), projectDTO.getEntity().getEntityId());
		if(projectOp.isPresent() && projectOp.get().getProjectId()!=projectDTO.getProjectId())
			throw new ProjectNameAlreadyExistException(environment.getProperty(ResponseMessages.PROJECT_NAME_EXIST));
		
		projectOp = projectRepository.getByProjectDisplayNameAndEntityId(projectDTO.getProjectDisplayName(),projectDTO.getEntity().getEntityId());
		if(projectOp.isPresent() && projectOp.get().getProjectId()!=projectDTO.getProjectId())
			throw new ProjectNameAlreadyExistException(environment.getProperty(ResponseMessages.PROJECT_DISPLAY_NAME_EXIST));
		
		Project project = ObjectMapperUtils.map(projectDTO, Project.class);
		project=projectRepository.saveAndFlush(project);
		return ObjectMapperUtils.map(project, ProjectDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public ProjectDTO deleteProject(ProjectDTO projectDTO) {
		logger.info("method to delete project invoked." + projectDTO);
		logger.debug(className + " deleteProject()");
		Optional<Project> projectOp = projectRepository.findById(projectDTO.getProjectId());
		if(!projectOp.isPresent())throw new ProjectNotFoundException(environment.getProperty(ResponseMessages.PROJECT_NOT_EXIST));
		
		projectRepository.deleteById(projectDTO.getProjectId());
		return projectDTO;
	}

	@Override
	public ProjectDTO getProjectById(Integer projectId) {
		logger.info("method to get project by Id invoked." + projectId);
		logger.debug(className + " deleteProject()");
		Optional<Project> projectOp = projectRepository.findById(projectId);
		if(!projectOp.isPresent())throw new ProjectNotFoundException(environment.getProperty(ResponseMessages.PROJECT_NOT_EXIST));
		Project project = projectOp.get();
		return ObjectMapperUtils.map(project, ProjectDTO.class);
	}

	@Override
	public PaginationResponseDTO<ProjectsRequestResponseDTO> getAllProjects(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType) {
		logger.info("method to get all projects invoked.");
		logger.debug(className + " getALLProjects()");
		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<ProjectsRequestResponseDTO> pageResponse= new PaginationResponseDTO<>();
	    
		Page<Project> projectPage = projectRepository.findAll(pageable);
		List<Project> project = projectPage.getContent();
	    List<ProjectDTO> projectDTO = ObjectMapperUtils.mapAll(project, ProjectDTO.class);
	    pageResponse.setData(ProjectsRequestResponseDTO.getProjectRequestResponseDTO(projectDTO));
	    PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(projectPage.getNumber());
		paginationDTO.setLimit(projectPage.getSize());
		paginationDTO.setTotalPages(projectPage.getTotalPages());
		paginationDTO.setLastPage(projectPage.isLast());
		paginationDTO.setTotalCounts(projectPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
	}

	@Override
	public PaginationResponseDTO<ProjectsRequestResponseDTO> getAllProjectsByEntityId(Integer pageNumber, Integer pageSize, String sortBy, String sortType,Integer entityId) {
		logger.info("method to get all project by enitityId invoked." + entityId);
		logger.debug(className + " getALLProjectsByEntityId()");
		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<ProjectsRequestResponseDTO> pageResponse= new PaginationResponseDTO<>();
	    
		Page<Project> projectPage = projectRepository.findAllByEntityId(entityId,pageable);
		List<Project> project = projectPage.getContent();
	    List<ProjectDTO> projectDTO = ObjectMapperUtils.mapAll(project, ProjectDTO.class);
	    pageResponse.setData(ProjectsRequestResponseDTO.getProjectRequestResponseDTO(projectDTO));
	    PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(projectPage.getNumber());
		paginationDTO.setLimit(projectPage.getSize());
		paginationDTO.setTotalPages(projectPage.getTotalPages());
		paginationDTO.setLastPage(projectPage.isLast());
		paginationDTO.setTotalCounts(projectPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
	}

	@Override
	public Map<String, List<String>> getProjectTagsById(Integer projectId) {
		logger.info("method to get all project related tags by projectId invoked.");
		logger.debug(className + " getProjectTagsById()");
		
		Optional<Project> projectOp = projectRepository.findById(projectId);
		if(!projectOp.isPresent())throw new ProjectNotFoundException(environment.getProperty(ResponseMessages.PROJECT_NOT_EXIST));
		
		 String projectDetailsJson = projectRepository.findProjectDetailsByProjectId(projectId);

	        // Parse the JSON using Jackson or any JSON library
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            // Convert JSON string to Map
	            Map<String, List<String>> fieldsMap = objectMapper.readValue(projectDetailsJson, 
	                new TypeReference<Map<String, List<String>>>() {});
	            return fieldsMap;
	        } catch (JsonProcessingException e) {
	            throw new RuntimeException(environment.getProperty(ResponseMessages.JSON_PARSING_EXCEPTION), e);
	        }
	}

}
