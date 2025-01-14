package com.purplebits.emrd2.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UserGroupMembershipDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupMembershipRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupWithMembersResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupWithUsersResponseDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupsRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.UsersRequestResponseDTO;
import com.purplebits.emrd2.entity.Roles;
import com.purplebits.emrd2.entity.UserGroupDetails;
import com.purplebits.emrd2.entity.UserGroupMembership;
import com.purplebits.emrd2.entity.UserGroups;
import com.purplebits.emrd2.exceptions.AdminDisplayNameChangeNotAllowedException;
import com.purplebits.emrd2.exceptions.AdminRoleChangeNotAllowedException;
import com.purplebits.emrd2.exceptions.AdminRoleDeleteNotAllowedException;
import com.purplebits.emrd2.exceptions.AdministratorUserGroupDeleteException;
import com.purplebits.emrd2.exceptions.DuplicateGroupException;
import com.purplebits.emrd2.exceptions.RoleAlreadyExistException;
import com.purplebits.emrd2.exceptions.RoleNotFoundException;
import com.purplebits.emrd2.exceptions.UserGroupDeleteException;
import com.purplebits.emrd2.exceptions.UserGroupsNotFoundException;
import com.purplebits.emrd2.exceptions.UserNotFoundException;
import com.purplebits.emrd2.repositories.RolesRepository;
import com.purplebits.emrd2.repositories.UserGroupMembershipRepository;
import com.purplebits.emrd2.repositories.UserGroupsDetailsRepository;
import com.purplebits.emrd2.repositories.UserGroupsRepository;
import com.purplebits.emrd2.service.UserGroupMembershipService;
import com.purplebits.emrd2.service.UserGroupsService;
import com.purplebits.emrd2.service.UserService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Service
public class UserGroupsServiceImpl implements UserGroupsService {

	private final Logger logger = LogManager.getLogger(UserGroupsServiceImpl.class);
	private final String className = UserGroupsServiceImpl.class.getSimpleName();

	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupsRepository userGroupsRepository;

	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
    private UserGroupMembershipService userGroupMembershipService;

	@Autowired
	private UserGroupMembershipRepository userGroupMembershipRepository;
	@Autowired
	Environment environment;
	@Autowired
	private UserGroupsDetailsRepository userGroupsDetailsRepository;
	

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupsDTO addNewUserGroups(UserGroupsDTO userGroupsDTO) {
		logger.info(className + " addNewUserGroups() invoked for " + userGroupsDTO);

		Optional<UserGroups> groupOp = userGroupsRepository.findByGroupNameOrDisplayName(userGroupsDTO.getDisplayName());
		if (groupOp.isPresent() && groupOp.get().getStatus().equals(Status.ACTIVE)) {
			throw new DuplicateGroupException(environment.getProperty(ResponseMessages.DUPLICATE_GROUP_ERROR));
		}else if(groupOp.isPresent() && groupOp.get().getStatus().equals(Status.DELETED)) {
			userGroupMembershipRepository.deleteAllUserGroupMembershipsByGroupId(groupOp.get().getGroupId());
			userGroupsRepository.deleteById(groupOp.get().getGroupId());
		}
		
		
		Integer roleId = userGroupsDTO.getRoles().getRoleId();
		Optional<Roles> roleOptional = rolesRepository.findByIdAndStatus(roleId,Status.ACTIVE);

		if (!roleOptional.isPresent()) {
			throw new RoleNotFoundException(environment.getProperty(ResponseMessages.ROLES_NOT_FOUND_ERROR));
		}
		Optional<UserGroups>  userGroupsOp= userGroupsRepository.findByRoleIdAndStatus(roleId,Status.ACTIVE);
		if(userGroupsOp.isPresent()) {
			throw new RoleAlreadyExistException(environment.getProperty(ResponseMessages.ROLEID_ALREADY_FOUND_ERROR));
		}
		
		Roles role = roleOptional.get();
		userGroupsDTO.setGroupName(userGroupsDTO.getDisplayName());

		UserGroups userGroup = ObjectMapperUtils.map(userGroupsDTO, UserGroups.class);
		userGroup.setRoles(role);
		userGroup.setStatus(Status.ACTIVE);
		

		userGroup = userGroupsRepository.saveAndFlush(userGroup);

		UserGroupsDTO resultDTO = ObjectMapperUtils.map(userGroup, UserGroupsDTO.class);

		RolesDTO rolesDTO = ObjectMapperUtils.map(role, RolesDTO.class);
		resultDTO.setRoles(rolesDTO);

		return resultDTO;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupsDTO updateUserGroups(UserGroupsDTO userGroupsDTO) {
	    logger.info(className + " updateUserGroups() invoked for " + userGroupsDTO);

	    Optional<UserGroups> groupOp = userGroupsRepository.findByGroupIdAndStatus(userGroupsDTO.getGroupId(),
	            Status.ACTIVE);
	    
	    Integer roleId=userGroupsDTO.getRoles().getRoleId();

	    if (!groupOp.isPresent()) {
	        throw new UserGroupsNotFoundException(environment.getProperty(ResponseMessages.USER_GROUP_NOT_FOUND_ERROR));
	    }
	   
	   if((groupOp.get().getRoles().isSystemRole()==true)&&(groupOp.get().getRoles().getRoleId()!=roleId)) {
		   throw new AdminRoleChangeNotAllowedException(environment.getProperty(ResponseMessages.ADMIN_ROLE_CHANGE_NOT_ALLOWED_ERROR));
	   }
	  
	   
	  
	  
	    if(groupOp.get().getRoles().getRoleId()!=roleId) {
	    Optional<UserGroups>  userGroupsOp= userGroupsRepository.findByRoleIdAndStatus(roleId,Status.ACTIVE);
		if(userGroupsOp.isPresent()) {
			throw new RoleAlreadyExistException(environment.getProperty(ResponseMessages.ROLEID_ALREADY_FOUND_ERROR));
		}
	    }
	    
		Optional<UserGroups> userGroupOp=userGroupsRepository.findByGroupNameOrDisplayName(userGroupsDTO.getDisplayName());
	
		if (userGroupOp.isPresent()) {			
		    UserGroups existingGroup = userGroupOp.get();
		    if (existingGroup.getStatus().equals(Status.ACTIVE) 
		            && !existingGroup.getGroupId().equals(userGroupsDTO.getGroupId())) {
		        throw new DuplicateGroupException(environment.getProperty(ResponseMessages.DUPLICATE_GROUP_ERROR));
		    }
		    if (existingGroup.getStatus().equals(Status.DELETED)) {
		        logger.info("Deleted UserGroup with ID: " + existingGroup.getGroupId());
		        userGroupsRepository.deleteByGroupId(existingGroup.getGroupId());
		    }
		}

	    UserGroups userGroup = groupOp.get();

	    if (userGroupsDTO.getRoles() != null && userGroupsDTO.getRoles().getRoleId() != null) {
	         roleId = userGroupsDTO.getRoles().getRoleId();
	        Optional<Roles> roleOptional = rolesRepository.findById(roleId);

	        if (!roleOptional.isPresent()) {
	            throw new RoleNotFoundException(environment.getProperty(ResponseMessages.ROLES_NOT_FOUND_ERROR));
	        }

	        Roles role = roleOptional.get();
	        userGroup.setRoles(role);
	    }
	    if ((groupOp.get().getRoles().isSystemRole() == true) &&
	    	    (!groupOp.get().getDisplayName().trim().equalsIgnoreCase(userGroupsDTO.getDisplayName().trim()))) {
	    	    throw new AdminDisplayNameChangeNotAllowedException(environment.getProperty(ResponseMessages.ADMIN_DISPLAY_NAME_CHANGE_NOT_ALLOWED_ERROR));
	    	}

	     if(userGroupsDTO.getDisplayName() !=null) {
	    	 userGroup.setDisplayName(userGroupsDTO.getDisplayName());
	     }


	    if (userGroupsDTO.getStatus() != null) {
	        userGroup.setStatus(userGroupsDTO.getStatus());
	    } else {
	        userGroup.setStatus(Status.ACTIVE); 
	    }

	    userGroup = userGroupsRepository.saveAndFlush(userGroup);

	    UserGroupsDTO resultDTO = ObjectMapperUtils.map(userGroup, UserGroupsDTO.class);

	    if (userGroup.getRoles() != null) {
	        RolesDTO rolesDTO = ObjectMapperUtils.map(userGroup.getRoles(), RolesDTO.class);
	        resultDTO.setRoles(rolesDTO);
	    }

	    return resultDTO;
	}

	@Override
	public UserGroupsDTO getUserGroupsById(Integer groupId) {

		logger.info(className + " getUserGroupsById() invoked for id: " + groupId);

		Optional<UserGroups> groupOp = userGroupsRepository.findByGroupIdAndStatus(groupId, Status.ACTIVE);
		if (!groupOp.isPresent()) {
			throw new UserGroupsNotFoundException(environment.getProperty(ResponseMessages.USER_GROUP_NOT_FOUND_ERROR));
		}

		return ObjectMapperUtils.map(groupOp.get(), UserGroupsDTO.class);
	}

	@Override
	public PaginationResponseDTO<UserGroupsRequestResponseDTO> getAllUserGroups(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType) {
	    logger.info("method to get all user groups invoked.");
		logger.info(className + " getAllUserGroups()");

		Sort sort = (sortType.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		PaginationResponseDTO<UserGroupsRequestResponseDTO> pageResponse = new PaginationResponseDTO<>();

		Page<UserGroups> userGroupPage = userGroupsRepository.findAllByStatus(Status.ACTIVE, pageable);
		List<UserGroups> userGroups = userGroupPage.getContent();
		List<UserGroupsDTO> userGroupsDTO = ObjectMapperUtils.mapAll(userGroups, UserGroupsDTO.class);

		pageResponse.setData(UserGroupsRequestResponseDTO.getUserGroupsRequestResponseDTO(userGroupsDTO));

		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(userGroupPage.getNumber());
		paginationDTO.setLimit(userGroupPage.getSize());
		paginationDTO.setTotalPages(userGroupPage.getTotalPages());
		paginationDTO.setLastPage(userGroupPage.isLast());
		paginationDTO.setTotalCounts(userGroupPage.getTotalElements());

		pageResponse.setPagination(paginationDTO);
		return pageResponse;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupsDTO deleteUserGroups(Integer groupId) {
		logger.info(className + " deleteUserGroups() invoked for id " + groupId);

		Optional<UserGroups> groupOp = userGroupsRepository.findByGroupIdAndStatus(groupId, Status.ACTIVE);
		if (!groupOp.isPresent()) {
			throw new UserGroupsNotFoundException(environment.getProperty(ResponseMessages.USER_GROUP_NOT_FOUND_ERROR));
		}

		UserGroups userGroup = groupOp.get();
		if(userGroup.getRoles().isSystemRole()==true) {
			throw new AdminRoleDeleteNotAllowedException(environment.getProperty(ResponseMessages.ADMIN_ROLE_DELETE_NOT_ALLOWED_ERROR));
		}
		userGroup.setStatus(Status.DELETED);
		userGroup = userGroupsRepository.saveAndFlush(userGroup);

		return ObjectMapperUtils.map(userGroup, UserGroupsDTO.class);
	}


	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupsDTO deleteUserGroupWithMembers(Integer groupId) {
		 logger.info("method to delete userGroup invoked.");
		    logger.debug(className + " deleteUserGroupWithMembers() for groupId: " + groupId);

	    // Step 1: Find the UserGroup by groupId and ensure it is active
	    Optional<UserGroups> groupOp = userGroupsRepository.findByGroupIdAndStatus(groupId, Status.ACTIVE);
	    if (!groupOp.isPresent()) {
	        throw new UserGroupsNotFoundException(environment.getProperty(ResponseMessages.USER_GROUP_NOT_FOUND_ERROR));
	    }
	    if(groupOp.get().getRoles().isSystemRole()==true) {
	    	 throw new AdministratorUserGroupDeleteException(environment.getProperty(ResponseMessages.ADMINISTRATOR_USER_GROUP_DELETE_ERROR));

	    }

	    // Step 2: Update the status of all UserGroupMemberships to 'DELETED'
	 
	  List<UserGroupMembership> groupMemberships = userGroupMembershipRepository.findAllMembersByGroupIdAndStatus(groupId, Status.ACTIVE);
	  if(!groupMemberships.isEmpty()) {
		  throw new UserGroupDeleteException(environment.getProperty(ResponseMessages.USER_GROUP_DELETE_ERROR));
	  }

	    // Step 3: Update the status of the UserGroup to 'DELETED'
	    UserGroups userGroup = groupOp.get();
	    userGroup.setStatus(Status.DELETED);
	    userGroup = userGroupsRepository.saveAndFlush(userGroup);

	    // Step 4: Return the updated UserGroup as DTO
	    return ObjectMapperUtils.map(userGroup, UserGroupsDTO.class);
	}
	
	
	
	
	@Override
	public PaginationResponseDTO<UserGroupWithUsersResponseDTO> getAllUserGroupWithMembersAndRole(
	    int pageNumber, int pageSize, String sortBy, String sortType) {
		 logger.info("method to get all userGroup with users and role details invoked.");
		logger.debug("getAllUserGroupWithMembersAndRole()  ");
	    Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<UserGroupWithUsersResponseDTO> response= new PaginationResponseDTO<>();
	    List<UserGroupWithUsersResponseDTO> userGroupWithUsersResponseDTOs = new ArrayList<>();
	    
	    Page<UserGroupDetails> userGroupsPage2 = userGroupsDetailsRepository.findAll(pageable);
	    List<UserGroupDetails> userGroups2 = userGroupsPage2.getContent();
	    
	    for(UserGroupDetails userGroupDetails : userGroups2) {
	    	UserGroupWithUsersResponseDTO userGroupWithUsersResponseDTO2 = new UserGroupWithUsersResponseDTO();
	    	UserGroupsDTO userGroupsDTO2 = new UserGroupsDTO();
	    	userGroupsDTO2.setDisplayName(userGroupDetails.getDisplayName());
	    	userGroupsDTO2.setGroupId(userGroupDetails.getGroupId());
	    	userGroupsDTO2.setGroupName(userGroupDetails.getGroupName());
	    	
		    RolesDTO rolesDTO = new RolesDTO();
		    rolesDTO.setRoleId(userGroupDetails.getRoleId());
		    rolesDTO.setRoleName(userGroupDetails.getRoleName());
		    rolesDTO.setSystemRole(userGroupDetails.isSystemRole());
		    userGroupsDTO2.setRoles(rolesDTO);
		    
		    List<UsersDTO> usersDTO2 = parseUsersMappings(userGroupDetails.getDetailedUserMappings());
		    userGroupWithUsersResponseDTO2.setUserGroup(userGroupsDTO2);
		    userGroupWithUsersResponseDTO2.setUsers(usersDTO2);
		    
		    userGroupWithUsersResponseDTOs.add(userGroupWithUsersResponseDTO2);
	    }
	    
	    PaginationDTO paginationDTO = new PaginationDTO();
	    paginationDTO.setPage(userGroupsPage2.getNumber());
	    paginationDTO.setLimit(userGroupsPage2.getSize());
	    paginationDTO.setTotalPages(userGroupsPage2.getTotalPages());
	    paginationDTO.setLastPage(userGroupsPage2.isLast());
	    paginationDTO.setTotalCounts(userGroupsPage2.getTotalElements());
	    response.setPagination(paginationDTO);
	    response.setData(userGroupWithUsersResponseDTOs);
return response;
	    
	}

	


	public void deleteAllUserGroupsRolesByRoleId(Integer roleId) {
	    List<UserGroups> userGroupsList = userGroupsRepository.findByRolesId(roleId);
	    Roles unassignedRole = new Roles();
	    unassignedRole.setRoleId(-1);
	    for (UserGroups userGroup : userGroupsList) {
	        userGroup.setRoles(unassignedRole);
	    }
	    userGroupsRepository.saveAll(userGroupsList);
	}

	@Override
	public UserGroupsDTO getUserGroupsByRoleId(Integer roleId) {
	    logger.info(className + " getUserGroupsById() invoked for id: " + roleId);
	    Optional<UserGroups> groupOp = userGroupsRepository.getUserGroupsByRoleId(roleId, Status.ACTIVE);
	    if (!groupOp.isPresent()) {
	        return null;
	    }
	    return ObjectMapperUtils.map(groupOp.get(), UserGroupsDTO.class);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupWithMembersResponseDTO addNewUserGroupWithMembers(UserGroupsDTO userGroupsDTO,
			List<UsersRequestResponseDTO> users) {
		 logger.info("method to add userGroup with members invoked.");
		   		logger.debug(className + " addNewUserGroupWithMembers() invoked for " + userGroupsDTO);
		UserGroupWithMembersResponseDTO response = new UserGroupWithMembersResponseDTO();
		UserGroupsDTO addedGroup = addNewUserGroups(userGroupsDTO);
		response.setUserGroup(new UserGroupsRequestResponseDTO(addedGroup));
		
		List<UserGroupMembershipDTO> userGroupMembershipDTOs = new ArrayList<>();
		
		for (UsersRequestResponseDTO user : users) {
			
			Optional<UserGroupMembership> userMembershipOp = userGroupMembershipService.
					findByUserIdAndGroupIdAndStatus(user.getUserId(), addedGroup.getGroupId(), Status.ACTIVE);
			if(userMembershipOp.isPresent())continue;
			userMembershipOp = userGroupMembershipService.
					findByUserIdAndGroupIdAndStatus(user.getUserId(), addedGroup.getGroupId(), Status.DELETED);
			if(userMembershipOp.isPresent()) {
				UserGroupMembership userGroupMembership = userMembershipOp.get();
				userGroupMembership.setStatus(Status.ACTIVE);
				userGroupMembershipDTOs.add(userGroupMembershipService.addUserMembership(ObjectMapperUtils.map(userGroupMembership, UserGroupMembershipDTO.class)));
			}else {
				
				UsersDTO usersDTO =userService.findUserById(user.getUserId());
				if(usersDTO==null) {
					throw new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
				}		
				UserGroupMembershipDTO userGroupMembershipDTO = new UserGroupMembershipDTO();
				usersDTO.setUserId(user.getUserId());
				userGroupMembershipDTO.setUsers(usersDTO);
				userGroupMembershipDTO.setGroups(addedGroup);
				userGroupMembershipDTOs.add(userGroupMembershipService.addUserMembership(userGroupMembershipDTO));
			}
			
		}
		response.setUsersGroupMembership(UserGroupMembershipRequestResponseDTO.getUserGroupMembershipRequestResponseDTO(userGroupMembershipDTOs));
		return response;
		
	}
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public UserGroupWithMembersResponseDTO updateUserGroupWithMembers(UserGroupsDTO userGroupsDTO,
			List<UsersRequestResponseDTO> users) {
		 logger.info("method to update userGroup with members invoked.");
		 logger.debug(className + " updateUserGroupWithMembers() ");
		UserGroupWithMembersResponseDTO response = new UserGroupWithMembersResponseDTO();
		UserGroupsDTO addedGroup = updateUserGroups(userGroupsDTO);
		
		response.setUserGroup(new UserGroupsRequestResponseDTO(addedGroup));
		
		userGroupMembershipService.deleteAllUserGroupMembershipsByGroupId(addedGroup.getGroupId());
List<UserGroupMembershipDTO> userGroupMembershipDTOs = new ArrayList<>();
		
		for (UsersRequestResponseDTO user : users) {
			
			Optional<UserGroupMembership> userMembershipOp = userGroupMembershipService.
					findByUserIdAndGroupIdAndStatus(user.getUserId(), addedGroup.getGroupId(), Status.ACTIVE);
			if(userMembershipOp.isPresent())continue;
			userMembershipOp = userGroupMembershipService.
					findByUserIdAndGroupIdAndStatus(user.getUserId(), addedGroup.getGroupId(), Status.DELETED);
			 if  (userMembershipOp.isPresent()) {
				UserGroupMembership userGroupMembership = userMembershipOp.get();
				userGroupMembership.setStatus(Status.ACTIVE);
				userGroupMembershipDTOs.add(userGroupMembershipService.addUserMembership(ObjectMapperUtils.map(userGroupMembership, UserGroupMembershipDTO.class)));
			}else {
				
				UserGroupMembershipDTO userGroupMembershipDTO = new UserGroupMembershipDTO();
				UsersDTO usersDTO =userService.findUserById(user.getUserId());
				if(usersDTO==null) {
					throw new UserNotFoundException(environment.getProperty(ResponseMessages.USER_NOT_FOUND));
				}	
				 usersDTO = new UsersDTO();
				usersDTO.setUserId(user.getUserId());
				userGroupMembershipDTO.setUsers(usersDTO);
				userGroupMembershipDTO.setGroups(addedGroup);
				userGroupMembershipDTOs.add(userGroupMembershipService.addUserMembership(userGroupMembershipDTO));
			}
			
		}
		response.setUsersGroupMembership(UserGroupMembershipRequestResponseDTO.getUserGroupMembershipRequestResponseDTO(userGroupMembershipDTOs));
		return response;
	}

	@Override
	public List<Integer> getAllUserGroupsById(List<Integer> groupIds) {
		return userGroupsRepository.getAllRoleId(groupIds, Status.ACTIVE);
	}

	@Override
	public PaginationResponseDTO<UserGroupWithUsersResponseDTO> getAllUserGroupWithMembersAndRoleByFilterCriteria(
			Integer pageNumber, Integer pageSize, String sortBy, String sortType,
			Specification<UserGroupDetails> spec) {
		
		logger.info("getAllUserGroupWithMembersAndRoleByFilterCriteria() invoked: ");
	    Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	    
	    PaginationResponseDTO<UserGroupWithUsersResponseDTO> response= new PaginationResponseDTO<>();
	    List<UserGroupWithUsersResponseDTO> userGroupWithUsersResponseDTOs = new ArrayList<>();
	    
	    Page<UserGroupDetails> userGroupsPage = userGroupsDetailsRepository.findAll(spec,pageable);
	    List<UserGroupDetails> userGroups = userGroupsPage.getContent();
	    
	    for(UserGroupDetails userGroupDetails : userGroups) {
	    	UserGroupWithUsersResponseDTO userGroupWithUsersResponseDTO = new UserGroupWithUsersResponseDTO();
	    	UserGroupsDTO userGroupsDTO = new UserGroupsDTO();
	    	userGroupsDTO.setDisplayName(userGroupDetails.getDisplayName());
	    	userGroupsDTO.setGroupId(userGroupDetails.getGroupId());
	    	userGroupsDTO.setGroupName(userGroupDetails.getGroupName());
	    	
		    RolesDTO rolesDTO = new RolesDTO();
		    rolesDTO.setRoleId(userGroupDetails.getRoleId());
		    rolesDTO.setRoleName(userGroupDetails.getRoleName());
		    rolesDTO.setSystemRole(userGroupDetails.isSystemRole());
		    userGroupsDTO.setRoles(rolesDTO);
		    
		    List<UsersDTO> usersDTO = parseUsersMappings(userGroupDetails.getDetailedUserMappings());
		    userGroupWithUsersResponseDTO.setUserGroup(userGroupsDTO);
		    userGroupWithUsersResponseDTO.setUsers(usersDTO);
		    
		    userGroupWithUsersResponseDTOs.add(userGroupWithUsersResponseDTO);
	    }

	    PaginationDTO paginationDTO = new PaginationDTO();
	    paginationDTO.setPage(userGroupsPage.getNumber());
	    paginationDTO.setLimit(userGroupsPage.getSize());
	    paginationDTO.setTotalPages(userGroupsPage.getTotalPages());
	    paginationDTO.setLastPage(userGroupsPage.isLast());
	    paginationDTO.setTotalCounts(userGroupsPage.getTotalElements());
	    response.setPagination(paginationDTO);
	    response.setData(userGroupWithUsersResponseDTOs);
	    return response;
	}	
	
	 public static List<UsersDTO> parseUsersMappings(String input) {
	        List<UsersDTO> mappings = new ArrayList<>();
	        if(input==null) return mappings;
	        // Regex to match each group-role mapping
	        String regex = "\\(userId - (\\d+) and fullName - ([^\\-]+) and email - ([\\w\\.-]+@[\\w\\.-]+)\\)";
	        
	        Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(input);
	        while (matcher.find()) {
	            // Extract values from the match
	            Integer userId = Integer.parseInt(matcher.group(1).trim());
	            String fullName = matcher.group(2).trim();
	            String email = matcher.group(3).trim();
	            
	            UsersDTO usersDTO = new UsersDTO();
	            usersDTO.setEmail(email);
	            usersDTO.setUserId(userId);
	            usersDTO.setFullName(fullName);

	            mappings.add(usersDTO);
	        }

	        return mappings;
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
							actualQuery.append("displayName ==" + "'*" + userInputList.get(i)).append("*'").append(")").append(",");
						} else {
							actualQuery.append("displayName ==" + "'*" + userInputList.get(i)).append("*'").append(",");
						}

					}
					
					actualQuery.append("(");
					for (int i = 0; i < userInputList.size(); i++) {
						if (i == userInputList.size() - 1) {
							actualQuery.append("roleName ==" + "'*" + userInputList.get(i)).append("*'").append(")").append(",");
						} else {
							actualQuery.append("roleName ==" + "'*" + userInputList.get(i)).append("*'").append(",");
						}

					}
					
					actualQuery.append("(");
					for (int i = 0; i < userInputList.size(); i++) {
						if (i == userInputList.size() - 1) {
							actualQuery.append("userFullNames ==" + "'*" + userInputList.get(i)).append("*'").append(")").append(";");
						} else {
							actualQuery.append("userFullNames ==" + "'*" + userInputList.get(i)).append("*'").append(",");
						}

					}

				} else {
					actualQuery.append("(displayName ==" + "'*" + search + "*'").append(")").append(",");
					actualQuery.append("(roleName ==" + "'*" + search + "*'").append(")").append(",");
					actualQuery.append("(userFullNames ==" + "'*" + search + "*'").append(")").append(";");
				}
		}
	        actualQuery.append("groupStatus ==" + "'*ACTIVE*'").append(";");
			if (actualQuery.charAt(actualQuery.length() - 1) == ';'
					|| actualQuery.charAt(actualQuery.length() - 1) == ',') {
				result = actualQuery.substring(0, actualQuery.length() - 1);
			} else {
				result = actualQuery.toString();
			}
			
			return result;
	}
	 
}
