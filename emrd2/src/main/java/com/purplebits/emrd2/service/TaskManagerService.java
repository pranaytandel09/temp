package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.*;
import com.purplebits.emrd2.entity.TaskManagerView;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public interface TaskManagerService {

//    List<TaskManagerDTO> getTasksByUser(Long id, String status, String priority, String startDateFrom, String startDateTo, Integer userId, int size);

//	TaskManagerDTO getTaskManagerById(Integer taskManagerId);
	TaskManagerDTO deleteTask(TaskManagerDTO taskManagerDTO);


	TaskManagerDTO createNewTasks(TaskManagerDTO taskManagerDTO, Integer loggedInUserId);

	String createSearchQuery(TaskManagerPaginationWithSearchRequestDTO query);

	PaginationResponseDTO<TaskManagerViewDTO> getAllTaskSearchBased(Integer pageNumber, Integer pageSize, String sortBy,
			String sortType, Specification<TaskManagerView> spec);


	TaskManagerDTO updateTask(TaskManagerDTO taskManagerDTO, Integer loggedInUserId);
}
