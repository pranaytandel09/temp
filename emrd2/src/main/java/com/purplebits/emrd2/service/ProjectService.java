package com.purplebits.emrd2.service;

import java.util.List;
import java.util.Map;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.dto.request_response.ProjectsRequestResponseDTO;

public interface ProjectService {

	ProjectDTO addNewProject(ProjectDTO projectDTO);
	ProjectDTO updateProject(ProjectDTO projectDTO);
	ProjectDTO deleteProject(ProjectDTO projectDTO);
	ProjectDTO getProjectById(Integer projectId);
	PaginationResponseDTO<ProjectsRequestResponseDTO> getAllProjects(Integer pageNumber, Integer pageSize, String sortBy, String sortType);
	PaginationResponseDTO<ProjectsRequestResponseDTO> getAllProjectsByEntityId(Integer pageNumber, Integer pageSize, String sortBy, String sortType,Integer entityId);
	Map<String, List<String>> getProjectTagsById(Integer projectId);
}
