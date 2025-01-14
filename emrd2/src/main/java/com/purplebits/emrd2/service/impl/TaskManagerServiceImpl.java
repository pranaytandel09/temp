package com.purplebits.emrd2.service.impl;

import com.purplebits.emrd2.dto.*;
import com.purplebits.emrd2.entity.*;
import com.purplebits.emrd2.exceptions.ContractUpdateException;
import com.purplebits.emrd2.exceptions.ContractWorkFlowNotFound;

import com.purplebits.emrd2.exceptions.EntityNotFoundException;
import com.purplebits.emrd2.exceptions.EntityUpdateException;
import com.purplebits.emrd2.exceptions.FileNumberUpdateException;
import com.purplebits.emrd2.exceptions.ProjectUpdateException;
import com.purplebits.emrd2.exceptions.TaskManagerNotFoundException;
import com.purplebits.emrd2.exceptions.TaskStatusNotValidException;
import com.purplebits.emrd2.exceptions.WorkFlowStepNotFound;
import com.purplebits.emrd2.repositories.TaskManagerViewRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.repositories.TaskManagerRepository;
import com.purplebits.emrd2.service.AclClassService;
import com.purplebits.emrd2.service.AclEntryService;
import com.purplebits.emrd2.service.AclObjectIdentityService;
import com.purplebits.emrd2.service.AclSidService;
import com.purplebits.emrd2.service.ContractWorkflowBindingService;
import com.purplebits.emrd2.service.PermissionsService;
import com.purplebits.emrd2.service.RolePermissionsService;
import com.purplebits.emrd2.service.RolesService;
import com.purplebits.emrd2.service.TaskManagerService;
import com.purplebits.emrd2.service.UserGroupMembershipService;
import com.purplebits.emrd2.service.WorkflowDefinitionService;
import com.purplebits.emrd2.types.DependencyType;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.types.TaskStatus;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;
import com.purplebits.emrd2.util.SecurityUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskManagerServiceImpl implements TaskManagerService {

	private final Logger logger = LogManager.getLogger(TaskManagerServiceImpl.class);
	private final String className = TaskManagerServiceImpl.class.getSimpleName();


	@Autowired
	public TaskManagerServiceImpl(TaskManagerRepository taskManagerRepository) {
		this.taskManagerRepository = taskManagerRepository;
	}
	
	@Autowired
	private  TaskManagerRepository taskManagerRepository;
	@Autowired
	private ContractWorkflowBindingService contractWorkflowBindingService;
	@Autowired
	private WorkflowDefinitionService workflowDefinitionService;
	@Autowired
	private TaskManagerViewRepository taskManagerViewRepository;
	
	@Autowired
	private RolePermissionsService rolePermissionsService;
	@Autowired
	private RolesService rolesService;
	@Autowired
	private AclClassService aclClassService;
	@Autowired
	private PermissionsService permissionsService;
	
	@Autowired
	private AclEntryService aclEntryService;
	@Autowired
	private AclObjectIdentityService aclObjectIdentityService;
	@Autowired
	private AclSidService aclSidService;

	@Autowired
	private AclPermissionEvaluator aclPermissionEvaluator;
	@Autowired
	private  UserGroupMembershipService userGroupMembershipService;
	
	@Autowired
    private AclCache aclCache;

	@Autowired
    Environment environment;
	
	@Value("${task.completion.interval}")
	private int taskCompletionInterval;

	@PostAuthorize("hasPermission(#taskManagerDTO.taskManagerId, 'com.purplebits.emrd2.entity.TaskManager', 'DELETE')")
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public TaskManagerDTO deleteTask(TaskManagerDTO taskManagerDTO) {
		logger.info("method to delete a task. " + taskManagerDTO);
		logger.debug(className + " deleteTask()");
		Integer taskManagerId = taskManagerDTO.getTaskManagerId();

		// Check if the task exists
		TaskManager task = taskManagerRepository.findById(taskManagerId)
				.orElseThrow(() -> new TaskManagerNotFoundException(
						environment.getProperty(ResponseMessages.TASK_ID_NOT_FOUND)));

		// Perform the delete operation
		taskManagerRepository.deleteById(task.getTaskManagerId());
		AclClassDTO aclClassDTO = aclClassService.findByClassName(TaskManager.class.getName());
		AclObjectIdentityDTO aclObjectIdentity = aclObjectIdentityService.getAclObjectIdentityByObjectIdIdentityAndAclClass(task.getTaskManagerId(), aclClassDTO.getId());
		if(aclObjectIdentity!=null) {
			aclEntryService.deleteByAclObjectIdentity(aclObjectIdentity.getId());
			Integer records = aclEntryService.findByAclObjectIdentity(aclObjectIdentity.getId());
    		if(records==0) {
    			aclObjectIdentityService.deleteByAclObjectIdentityId(aclObjectIdentity.getId());
    		}
		}
		return taskManagerDTO;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public TaskManagerDTO createNewTasks(TaskManagerDTO taskManagerDTO,Integer loggedInUserId) {
		logger.info("method to create a new task manager invoked. " + taskManagerDTO);
		logger.debug(className + " getTaskManagerById()");
		
		
		WorkflowDTO workFlowDTO = contractWorkflowBindingService.
				findWorkflowByContractId(taskManagerDTO.getContract().getContractId());
		if(workFlowDTO==null)throw new ContractWorkFlowNotFound(environment.getProperty(ResponseMessages.CONTRACT_WORKFLOW_NOT_FOUND));
		
		taskManagerDTO.setWorkflow(workFlowDTO);
		
		StepDTO stepDTO = workflowDefinitionService.
				getStepByWorkFlowId(taskManagerDTO.getWorkflow().getWorkflowId(), DependencyType.Independent);
		if(stepDTO==null)throw new WorkFlowStepNotFound(environment.getProperty(ResponseMessages.WORKFLOW_STEP_NOT_FOUND));
	
		taskManagerDTO.setStep(stepDTO);
		
		TaskManager taskManager = ObjectMapperUtils.map(taskManagerDTO, TaskManager.class);
		taskManager.setStatus(TaskStatus.Pending);
		taskManager.setStartedAt(new Date());
		
		  Date currentDate = new Date();
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(currentDate);
		    calendar.add(Calendar.DATE, taskCompletionInterval);
		    Date updatedDate = calendar.getTime();
		    taskManager.setDueDate(updatedDate);
		    
		    	taskManager=taskManagerRepository.saveAndFlush(taskManager);
		
		addAclEntriesForTaskManager(taskManager, taskManager.getAssignedToUser().getUserId(),taskManager.getCreatedBy().getUserId());
		
		return ObjectMapperUtils.map(taskManager, TaskManagerDTO.class);
	}

	@Override
	public PaginationResponseDTO<TaskManagerViewDTO> getAllTaskSearchBased(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType, Specification<TaskManagerView> spec) {

		logger.info("method to get all Task search based invoked.");
		logger.debug(className + " getAllTaskSearchBased()");
		
		  Sort sort;
		if (sortBy == null || sortBy.isEmpty()) {
		    // Default sorting by statusSortOrder and priorityNumeric
		    sort = Sort.by(
		            Sort.Order.asc("statusSortOrder"),
		            Sort.Order.asc("priorityNumeric"),
		            Sort.Order.asc("taskManagerId") // Tie-breaker
		    );
		} else {
		    // User-specified sorting
			if ("status".equalsIgnoreCase(sortBy)) {
		        sortBy = "statusSortOrder"; // Map status to statusSortOrder
		    } else if ("priority".equalsIgnoreCase(sortBy)) {
		        sortBy = "priorityNumeric"; // Map priority to priorityNumeric
		    }
		    sort = sortType.equalsIgnoreCase("asc") 
		            ? Sort.by(sortBy).ascending() 
		            : Sort.by(sortBy).descending();
		}


	    PaginationResponseDTO<TaskManagerViewDTO> pageResponse= new PaginationResponseDTO<>();

	    List<TaskManagerView> taskList = taskManagerViewRepository.findAll(spec,sort);
	    
	    List<String> userRoles = SecurityUtils.getUserRoles();
	    
	    if (SecurityUtils.getAuthentication() != null) {
	    	taskList = taskList.stream()
					.filter(task -> aclPermissionEvaluator.hasPermission(SecurityUtils.getAuthentication(),
							task.getTaskManagerId(), // The identifier for the ACL-controlled entity
							"com.purplebits.emrd2.entity.TaskManager", // The ACL domain class
							"VIEW" // Permission to check
					)).collect(Collectors.toList());
		}
	    
	    int totalElements = taskList.size();
		int fromIndex = Math.min(pageNumber * pageSize, totalElements);
		int toIndex = Math.min((pageNumber + 1) * pageSize, totalElements);

		List<TaskManagerView> subList = taskList.subList(fromIndex, toIndex);
	    
	    List<TaskManagerViewDTO> taskManagerViewDTO = ObjectMapperUtils.mapAll(subList, TaskManagerViewDTO.class);
	    List<TaskManagerViewDTO> resultList= new ArrayList<>();
	    for(TaskManagerViewDTO taskManagers: taskManagerViewDTO) {
	    	List<Integer> maskLists = aclEntryService.findDistinctMaskByObjectIdIdentityAndClassNameAndPrincipalAndSid(
	    			taskManagers.getTaskManagerId(), TaskManager.class.getName(), false,
					SecurityUtils.getUserRoles());
			List<PermissionsDTO> permissions = permissionsService.findAllByPermissionIdIn(maskLists);
			taskManagers.setPermissions(permissions);
			resultList.add(taskManagers);
	    }
	    
	    pageResponse.setData(resultList);

		PaginationDTO pgDto = new PaginationDTO();
		pgDto.setPage(pageNumber);
		pgDto.setLimit(pageSize);
		pgDto.setTotalPages((int) Math.ceil((double) totalElements / pageSize));
		pgDto.setLastPage((pageNumber + 1) * pageSize >= totalElements);
		pgDto.setTotalCounts(totalElements);
		pageResponse.setPagination(pgDto);

		pageResponse.setPagination(pgDto);
		return pageResponse;
	}

	@PreAuthorize("hasPermission(#taskManagerDTO.taskManagerId, 'com.purplebits.emrd2.entity.TaskManager', 'UPLOAD')")
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public TaskManagerDTO updateTask(TaskManagerDTO taskManagerDTO,Integer loggedInUserId) {
		logger.info("method to update task manager invoked. " + taskManagerDTO);
		logger.debug(className + " updateTask()");
		
		 Optional<TaskManager> taskManagerOp = taskManagerRepository.findById(taskManagerDTO.getTaskManagerId());
		 if(!taskManagerOp.isPresent()) {
			 throw new TaskManagerNotFoundException
			 (environment.getProperty(ResponseMessages.TASK_ID_NOT_FOUND));
		 }
					
		 TaskManager taskManager = taskManagerOp.get();
		 
		 if(taskManagerDTO.getEntity().getEntityId()!=null && taskManager.getEntity().getEntityId()!=taskManagerDTO.getEntity().getEntityId()) {
			 throw new EntityUpdateException(environment.getProperty(ResponseMessages.ENTITY_UPDATE_ERROR));
		 }
		 if(taskManagerDTO.getProject().getProjectId()!=null && taskManager.getProject().getProjectId()!=taskManagerDTO.getProject().getProjectId()) {
			 throw new ProjectUpdateException(environment.getProperty(ResponseMessages.PROJECT_UPDATE_ERROR));
		 }
		 if(taskManagerDTO.getContract().getContractId()!=null && taskManager.getContract().getContractId()!=taskManagerDTO.getContract().getContractId()) {
			 throw new ContractUpdateException(environment.getProperty(ResponseMessages.CONTRACT_UPDATE_ERROR));
		 }
		 if(taskManagerDTO.getNumberOfFiles()!=null && taskManager.getNumberOfFiles()!=taskManagerDTO.getNumberOfFiles()) {
			 throw new FileNumberUpdateException(environment.getProperty(ResponseMessages.FILE_NUMBER_UPDATE_ERROR));
		 }
		 
		 if(taskManagerDTO.getNumberOfFiles()!=null && taskManager.getNumberOfFiles()!=taskManagerDTO.getNumberOfFiles()) {
			 throw new FileNumberUpdateException(environment.getProperty(ResponseMessages.FILE_NUMBER_UPDATE_ERROR));
		 }
		 
		 if(taskManagerDTO.getStatus()==null || (taskManagerDTO.getStatus().equals(TaskStatus.Pending) && taskManager.getStatus().equals(TaskStatus.In_Progress))) {
			 throw new TaskStatusNotValidException(environment.getProperty(ResponseMessages.TASK_STATUS_NOT_VALID));
		 }
		 
		 if(taskManagerDTO.getStatus().equals(TaskStatus.Completed)) {
			 List<StepDTO> stepsDTOs = workflowDefinitionService.getNextStepByWorkflowIdAndDependentOn(taskManager.getWorkflow().getWorkflowId(),
					 taskManager.getStep().getStepId());
			 
			 if(!stepsDTOs.isEmpty()) {
				 StepDTO stepDTO = stepsDTOs.get(0);
				 taskManagerDTO.setStep(stepDTO);
				 taskManagerDTO.setStatus(TaskStatus.Pending);
				 taskManagerDTO.setAssignedToUser(taskManagerDTO.getCreatedBy());
			}
			 else {
				 taskManagerDTO.setCompletedAt(new Date());
			 }
		 }
		 
		 
		 
		 taskManager = ObjectMapperUtils.map(taskManagerDTO, TaskManager.class);
		 taskManager=taskManagerRepository.saveAndFlush(taskManager);
		 
			AclClassDTO aclClassDTO = aclClassService.findByClassName(TaskManager.class.getName());
			AclObjectIdentityDTO aclObjectIdentity = aclObjectIdentityService.getAclObjectIdentityByObjectIdIdentityAndAclClass(taskManager.getTaskManagerId(), aclClassDTO.getId());
			if(aclObjectIdentity!=null) {
				aclEntryService.deleteByAclObjectIdentity(aclObjectIdentity.getId());
				Integer records = aclEntryService.findByAclObjectIdentity(aclObjectIdentity.getId());
        		if(records==0) {
        			aclObjectIdentityService.deleteByAclObjectIdentityId(aclObjectIdentity.getId());
        		}
			}
			
			addAclEntriesForTaskManager(taskManager, taskManager.getAssignedToUser().getUserId(), taskManager.getCreatedBy().getUserId());
			aclCache.clearCache();
			return ObjectMapperUtils.map(taskManager, TaskManagerDTO.class);
	}


	@Override
	public String createSearchQuery(TaskManagerPaginationWithSearchRequestDTO query) {

		StringBuilder actualQuery = new StringBuilder();
		String result;
		int count=0;
		String searchParam = query.getEntityName();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("entityName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("entityName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("entityName ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		 searchParam = query.getEntityDisplayName();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("entityDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("entityDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("entityDisplayName ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getProjectName();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("projectName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("projectName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("projectName ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getProjectDisplayName();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("projectDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("projectDisplayName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("projectDisplayName ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getAssignedToFullName();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("assignedToUsername ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("assignedToUsername ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("assignedToUsername ==" + "'*" + searchParam + "*'").append(";");
			}

		}
		searchParam = query.getAssignedToEmail();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("assignedToEmail ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("assignedToEmail ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("assignedToEmail ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getAssignedToUsername();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("assignedToUsername ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("assignedToUsername ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("assignedToUsername ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getCreatedByFullName();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("createdByFullName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("createdByFullName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("createdByFullName ==" + "'*" + searchParam + "*'").append(";");
			}

		}
		searchParam = query.getCreatedByUsername();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("createdByUsername ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("createdByUsername ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("createdByUsername ==" + "'*" + searchParam + "*'").append(";");
			}

		}
		searchParam = query.getCreatedByEmail();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("createdByEmail ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("createdByEmail ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("createdByEmail ==" + "'*" + searchParam + "*'").append(";");
			}

		}

		searchParam = query.getStepName();
		if (searchParam != null && !searchParam.isEmpty()) {
			count++;
			if (searchParam.contains(",")) {
				List<String> searchParamList = Arrays.asList(searchParam.split("\\s*,\\s*"));

				for (int i = 0; i < searchParamList.size(); i++) {
					if (i == searchParamList.size() - 1) {
						actualQuery.append("stepName ==" + "'*" + searchParamList.get(i)).append("*'").append(";");
					} else {
						actualQuery.append("stepName ==" + "'*" + searchParamList.get(i)).append("*'").append(",");
					}

				}

			} else {

				actualQuery.append("stepName ==" + "'*" + searchParam + "*'").append(";");
			}

		}
		
		String taskManagerId = query.getTaskManagerId();
		if (taskManagerId != null && !taskManagerId.isEmpty()) {
			if (taskManagerId.contains(",")) {
				List<String> staskManagerIdList = Arrays.asList(taskManagerId.split("\\s*,\\s*"));

				for (int i = 0; i < staskManagerIdList.size(); i++) {
					if (i == staskManagerIdList.size() - 1) {
						actualQuery.append("taskManagerId ==" + Integer.parseInt(staskManagerIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("taskManagerId ==" + Integer.parseInt(staskManagerIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("taskManagerId ==" + Integer.parseInt(taskManagerId)).append(";");
			}

		}
		
		String assignedToUserId = query.getAssignedToUserId();
		if (assignedToUserId != null && !assignedToUserId.isEmpty()) {
			if (assignedToUserId.contains(",")) {
				List<String> assignedToUserIdList = Arrays.asList(assignedToUserId.split("\\s*,\\s*"));

				for (int i = 0; i < assignedToUserIdList.size(); i++) {
					if (i == assignedToUserIdList.size() - 1) {
						actualQuery.append("assignedToUserId ==" + Integer.parseInt(assignedToUserIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("assignedToUserId ==" + Integer.parseInt(assignedToUserIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("assignedToUserId ==" + Integer.parseInt(assignedToUserId)).append(";");
			}

		}
		
		String createdById = query.getCreatedById();
		if (createdById != null && !createdById.isEmpty()) {
			if (createdById.contains(",")) {
				List<String> createdByIdList = Arrays.asList(createdById.split("\\s*,\\s*"));

				for (int i = 0; i < createdByIdList.size(); i++) {
					if (i == createdByIdList.size() - 1) {
						actualQuery.append("createdById ==" + Integer.parseInt(createdByIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("createdById ==" + Integer.parseInt(createdByIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("createdById ==" + Integer.parseInt(createdById)).append(";");
			}

		}
		
		String entityId = query.getEntityId();
		if (entityId != null && !entityId.isEmpty()) {
			if (entityId.contains(",")) {
				List<String> entityIdList = Arrays.asList(entityId.split("\\s*,\\s*"));

				for (int i = 0; i < entityIdList.size(); i++) {
					if (i == entityIdList.size() - 1) {
						actualQuery.append("entityId ==" + Integer.parseInt(entityIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("entityId ==" + Integer.parseInt(entityIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("entityId ==" + Integer.parseInt(entityId)).append(";");
			}

		}
		
		String projectId = query.getProjectId();
		if (projectId != null && !projectId.isEmpty()) {
			if (projectId.contains(",")) {
				List<String> projectIdList = Arrays.asList(projectId.split("\\s*,\\s*"));

				for (int i = 0; i < projectIdList.size(); i++) {
					if (i == projectIdList.size() - 1) {
						actualQuery.append("projectId ==" + Integer.parseInt(projectIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("projectId ==" + Integer.parseInt(projectIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("projectId ==" + Integer.parseInt(projectId)).append(";");
			}

		}
		
		String contractId = query.getContractId();
		if (contractId != null && !contractId.isEmpty()) {
			if (contractId.contains(",")) {
				List<String> contractIdList = Arrays.asList(contractId.split("\\s*,\\s*"));

				for (int i = 0; i < contractIdList.size(); i++) {
					if (i == contractIdList.size() - 1) {
						actualQuery.append("contractId ==" + Integer.parseInt(contractIdList.get(i)))
								.append(";");
					} else {
						actualQuery.append("contractId ==" + Integer.parseInt(contractIdList.get(i)))
								.append(",");
					}

				}

			} else {
				actualQuery.append("contractId ==" + Integer.parseInt(contractId)).append(";");
			}

		}
		
		String startDate = query.getStartDate();
		if (startDate != null && !startDate.isEmpty()) {
			if (startDate.contains(",")) {
				List<String> startDateList = Arrays.asList(startDate.split("\\s*,\\s*"));

				for (int i = 0; i < startDateList.size(); i++) {
					if (i == startDateList.size() - 1) {
						actualQuery.append("dueDate =ge= " + startDateList.get(i)).append("*").append(";");
					} else {
						actualQuery.append("dueDate =ge= " + startDateList.get(i)).append("*").append(",");
					}

				}

			} else {

				actualQuery.append("dueDate =ge=" + "'" + startDate + "'").append(";");
			}

		}

		String endDate = query.getStartDate();
		if (endDate != null && !endDate.isEmpty()) {
			if (endDate.contains(",")) {
				List<String> startDateList = Arrays.asList(endDate.split("\\s*,\\s*"));

				for (int i = 0; i < startDateList.size(); i++) {
					if (i == startDateList.size() - 1) {
						actualQuery.append("dueDate =le= " + startDateList.get(i)).append("*").append(";");
					} else {
						actualQuery.append("dueDate =le= " + startDateList.get(i)).append("*").append(",");
					}

				}

			} else {

				actualQuery.append("dueDate =le=" + "'" + startDate + "'").append(";");
			}

		}
		
		TaskStatus taskStatus = query.getStatus();

		if (taskStatus != null) {
			count++;
			actualQuery.append("status ==" + taskStatus).append(";");

		}

		if(count==0) {
			actualQuery.append("status ==" + TaskStatus.In_Progress).append(",");
			actualQuery.append("status ==" + TaskStatus.Pending).append(";");
		}

		if (actualQuery.charAt(actualQuery.length() - 1) == ';'
				|| actualQuery.charAt(actualQuery.length() - 1) == ',') {
			result = actualQuery.substring(0, actualQuery.length() - 1);
		} else {
			result = actualQuery.toString();
		}
		return result;
	}
	
	private void addAclEntriesForTaskManager(TaskManager taskManager, Integer assignedToUserId, Integer loggedInUserId) {
		AclClassDTO aclClassDTO = aclClassService.findByClassName(TaskManager.class.getName());
		if (aclClassDTO == null) {
			aclClassDTO = new AclClassDTO();
			aclClassDTO.setClassName(TaskManager.class.getName());
			aclClassDTO = aclClassService.addNewAclClass(aclClassDTO);
		}

		List<RolesDTO> roles = userGroupMembershipService.findRolesByLoggedInUserId(loggedInUserId);
		roles.addAll(userGroupMembershipService.findRolesByLoggedInUserId(assignedToUserId));
		roles.addAll(rolesService.getSysetmRoles());
		
		for (RolesDTO role : roles) {

			AclSidDTO aclSid = aclSidService.findBySid(role.getRoleName());

			if (aclSid == null) {
				aclSid = new AclSidDTO();
				aclSid.setPrincipal(false); // False because this is a role, not a user
				aclSid.setSid(role.getRoleName()); // Set the role name as SID

				aclSid = aclSidService.addNewPrincipleObject(aclSid);
			}

			// adding AclObjectIdentity
			AclObjectIdentityDTO aclObjectIdentity = aclObjectIdentityService
					.getAclObjectIdentityByObjectIdIdentityAndAclClass(taskManager.getTaskManagerId(),
							aclClassDTO.getId());
			if (aclObjectIdentity == null) {
				aclObjectIdentity = new AclObjectIdentityDTO();
				aclObjectIdentity.setObjectIdIdentity(taskManager.getTaskManagerId());
				aclObjectIdentity.setAclClass(aclClassDTO);
				aclObjectIdentity.setOwnerSid(aclSid);
				aclObjectIdentity.setEntriesInheriting(true);

				aclObjectIdentity = aclObjectIdentityService.addNewAclObjectIdentity(aclObjectIdentity);
			}

			List<PermissionsDTO> distinctPermissions = rolePermissionsService
					.findDistinctPermissionsByRoleIdAndStatus(role.getRoleId());
			
			for (PermissionsDTO permission : distinctPermissions) {
				// Grant permissions to the role

				AclEntryDTO aclEntry = new AclEntryDTO();
				aclEntry.setSid(aclSid); // The role or user sid
				aclEntry.setAclObjectIdentity(aclObjectIdentity);
				aclEntry.setMask(permission.getMask()); // Save the permission mask
				aclEntry.setGranting(true); // True if granting permission
				aclEntry.setAuditSuccess(true);
				aclEntry.setAuditFailure(true);
				aclEntry.setAceOrder(0);
				aclEntryService.addNewAclEntry(aclEntry);
			}

		}
	}

	}

