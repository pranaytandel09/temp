package com.purplebits.emrd2.service.impl;

import java.util.ArrayList;
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
import org.springframework.security.acls.model.AclCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.AclSidDTO;
import com.purplebits.emrd2.dto.PaginationDTO;
import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.dto.RolePermissionsDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UserGroupDTO;
import com.purplebits.emrd2.dto.UserGroupsDTO;
import com.purplebits.emrd2.dto.UsersRoleDetailsDTO;
import com.purplebits.emrd2.dto.request_response.RolePermissionsRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RolesRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RolesWithPermissionRequestRequestDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupsResponseDTO;
import com.purplebits.emrd2.entity.Roles;
import com.purplebits.emrd2.entity.UserGroupDetails;
import com.purplebits.emrd2.exceptions.AdminRoleDeleteNotAllowedException;
import com.purplebits.emrd2.exceptions.DuplicateRoleException;
import com.purplebits.emrd2.exceptions.PermissionNotFoundException;
import com.purplebits.emrd2.exceptions.RoleDeletionException;
import com.purplebits.emrd2.exceptions.RoleNotFoundException;
import com.purplebits.emrd2.repositories.RolesRepository;
import com.purplebits.emrd2.repositories.UserGroupsDetailsRepository;
import com.purplebits.emrd2.service.AclEntryService;
import com.purplebits.emrd2.service.AclSidService;
import com.purplebits.emrd2.service.PermissionsService;
import com.purplebits.emrd2.service.RolePermissionMappingService;
import com.purplebits.emrd2.service.RolePermissionsService;
import com.purplebits.emrd2.service.RolesService;
import com.purplebits.emrd2.service.UserGroupMembershipService;
import com.purplebits.emrd2.service.UserGroupsService;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;


@Service
public class RolesServiceImpl implements RolesService {
	private static final Logger logger = LogManager.getLogger(RolesServiceImpl.class);
	private final String className = RolesServiceImpl.class.getSimpleName();
	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private RolePermissionsService rolePermissionsService;

	@Autowired
	private UserGroupsService userGroupsService;

	@Autowired
	private PermissionsService permissionsService;

	@Autowired
	private RolePermissionMappingService rolePermissionMappingService;

	@Autowired
	private UserGroupMembershipService userGroupMembershipService;

	@Autowired
	private UserGroupsDetailsRepository userGroupsDetailsRepository;

	@Autowired
	private AclSidService aclSidService;

	@Autowired
	private AclEntryService aclEntryService;

	@Autowired
	Environment environment;

	@Autowired
	private AclCache aclCache;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public RolesDTO addNewRole(RolesDTO rolesDTO) {
		logger.info("method to add new role invoked."); // Log the invocation
		logger.debug(className + " addNewRole()  ");
		Optional<Roles> roleOp = rolesRepository.findByRoleName(rolesDTO.getRoleName());
		if (roleOp.isPresent() && roleOp.get().getStatus().equals(Status.ACTIVE)) {
			throw new DuplicateRoleException(environment.getProperty(ResponseMessages.ROLE_ALREADY_EXISTS));
		} else if (roleOp.isPresent() && roleOp.get().getStatus().equals(Status.DELETED)) {
			rolesDTO.setRoleId(roleOp.get().getRoleId());
		}

		Roles roles = ObjectMapperUtils.map(rolesDTO, Roles.class);
		roles.setStatus(Status.ACTIVE);
		Roles savedRole = rolesRepository.saveAndFlush(roles);

		// aclSid Table entry for that Role:
		AclSidDTO aclSid = aclSidService.findBySid(savedRole.getRoleName());
		if (aclSid == null) {
			aclSid = new AclSidDTO();
			aclSid.setPrincipal(false); // False because this is a role, not a user
			aclSid.setSid(savedRole.getRoleName()); // Set the role name as SID
			aclSidService.addNewPrincipleObject(aclSid);
		}

		return ObjectMapperUtils.map(savedRole, RolesDTO.class);
	}

	@Override
	public List<RolesDTO> getAllRoles() {
		logger.info("method to get all roles invoked.");
		logger.debug(className + " getAllRoles()");
		List<Roles> rolesList = rolesRepository.findByStatus(Status.ACTIVE);
		List<RolesDTO> rolesDTOList = ObjectMapperUtils.mapAll(rolesList, RolesDTO.class);
		return rolesDTOList;
	}

	@Override
	public RolesDTO getRoleById(Integer roleId) {
		logger.info("method to get role by roleId invoked.");
		logger.debug(className + "getRoleById() ");

		Roles role = rolesRepository.findByIdAndStatus(roleId, Status.ACTIVE).orElseThrow(() -> {
			return new RoleNotFoundException(environment.getProperty(ResponseMessages.ROLES_NOT_FOUND_ERROR));
		});
		return ObjectMapperUtils.map(role, RolesDTO.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public RolesDTO updateRole(RolesDTO rolesDTO) {
		logger.info("method to update role invoked.");
		logger.debug(className + "updateRole() : ");
		Roles existingRole = rolesRepository.findById(rolesDTO.getRoleId()).orElseThrow(() -> {
			return new RoleNotFoundException(environment.getProperty(ResponseMessages.ROLES_NOT_FOUND_ERROR));
		});

		Optional<Roles> roleOp = rolesRepository.findByRoleName(rolesDTO.getRoleName());
		if (roleOp.isPresent() && roleOp.get().getStatus().equals(Status.ACTIVE)
				&& roleOp.get().getRoleId() != rolesDTO.getRoleId()) {
			throw new DuplicateRoleException(environment.getProperty(ResponseMessages.ROLE_ALREADY_EXISTS));
		} else if (roleOp.isPresent() && roleOp.get().getStatus().equals(Status.DELETED)) {
			rolesRepository.deleteById(roleOp.get().getRoleId());
		}

		AclSidDTO aclSid = aclSidService.findBySid(existingRole.getRoleName());
		if (aclSid == null) {
			aclSid = new AclSidDTO();
			aclSid.setPrincipal(false); // False because this is a role, not a user
			aclSid.setSid(rolesDTO.getRoleName()); // Set the role name as SID

			aclSidService.addNewPrincipleObject(aclSid);
		} else {
			aclSid.setSid(rolesDTO.getRoleName());
			aclSidService.addNewPrincipleObject(aclSid);
		}

		existingRole.setRoleName(rolesDTO.getRoleName());
		existingRole.setSystemRole(rolesDTO.isSystemRole());
		existingRole.setStatus(Status.ACTIVE);
		Roles updatedRole = rolesRepository.saveAndFlush(existingRole);
		return ObjectMapperUtils.map(updatedRole, RolesDTO.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	public RolesDTO deleteRole(Integer roleId) {
		logger.info("method to delete role invoked.");
		logger.debug(className + "deleteRole() : ");
		Roles role = rolesRepository.findByIdAndStatus(roleId, Status.ACTIVE).orElseThrow(
				() -> new RoleNotFoundException(environment.getProperty(ResponseMessages.ROLES_NOT_FOUND_ERROR)));

		UserGroupsDTO userGroupsByRoleId = userGroupsService.getUserGroupsByRoleId(roleId);
		if (userGroupsByRoleId != null) {
			throw new RoleDeletionException(environment.getProperty(ResponseMessages.ROLE_DELETION_ERROR));
		}
		if (role.isSystemRole() == true) {
			throw new AdminRoleDeleteNotAllowedException(
					environment.getProperty(ResponseMessages.ADMIN_ROLE_DELETION_ERROR));

		}
		rolePermissionsService.deleteAllRolesByRoleId(roleId);
		
//		AclSidDTO aclSid = aclSidService.findBySid(role.getRoleName());
//		aclEntryService.deleteALLByAclSid(aclSid.getId());
		role.setStatus(Status.DELETED);
		Roles updatedRole = rolesRepository.saveAndFlush(role);
//		aclCache.clearCache();
		return ObjectMapperUtils.map(updatedRole, RolesDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public RolesWithPermissionRequestRequestDTO addOrUpdateRole(RolesDTO rolesDTO,
			List<RolePermissionsRequestResponseDTO> permissions) {
		logger.info("method to add or update role invoked."); // Log invocation
		logger.info(className + "addOrUpdateRole() ");

		RolesWithPermissionRequestRequestDTO response = new RolesWithPermissionRequestRequestDTO();
		RolesDTO role = null;

		if (rolesDTO.getRoleId() != null) {
			// update case:
			role = updateRole(rolesDTO);
		} else {
			// add case:
			if (permissions == null || permissions.isEmpty())
				throw new PermissionNotFoundException(
						environment.getProperty(ResponseMessages.ROLE_PERMISSIONS_NOT_FOUND_ERROR));
			role = addNewRole(rolesDTO);

		}

		List<RolePermissionsDTO> rolesWithPermissionsDTOs = new ArrayList<>();
		for (RolePermissionsRequestResponseDTO rolePermissions : permissions) {
			rolePermissions.setRoleId(role.getRoleId());
			rolesWithPermissionsDTOs.add(rolePermissions.getRolePermissionsDTO());
		}

		rolesWithPermissionsDTOs = rolePermissionsService.addOrUpdateRoleWithPermissions(rolesWithPermissionsDTOs);
		response.setRoles(new RolesRequestResponseDTO(role));
		response.setPermissions(
				RolePermissionsRequestResponseDTO.getRolePermissionsRequestResponseDTO(rolesWithPermissionsDTOs));
		return response;

	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public PaginationResponseDTO<UserGroupsResponseDTO> getUsersGroups(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType) {
		logger.info(className + "getUsersGroups() invoked ");

		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		PaginationResponseDTO<UserGroupsResponseDTO> paginationResponse = new PaginationResponseDTO<>();
		List<UserGroupsResponseDTO> userGroupsResponseDTOList = new ArrayList<>();

		Page<Roles> rolesPage = rolesRepository.findAllByStatus(Status.ACTIVE, pageable);
		List<Roles> roles = rolesPage.getContent();

		for (Roles role : roles) {
			UserGroupsResponseDTO userGroupsResponseDTO = new UserGroupsResponseDTO();
			RolesDTO rolesDTO = ObjectMapperUtils.map(role, RolesDTO.class);
			userGroupsResponseDTO.setRolesDTO(rolesDTO);
			UserGroupsDTO userGroup = userGroupsService.getUserGroupsByRoleId(role.getRoleId());
			if (userGroup != null) {
				UserGroupDTO userGroupDTO = new UserGroupDTO();
				userGroupDTO.setGroupId(userGroup.getGroupId());
				userGroupDTO.setGroupName(userGroup.getGroupName());
				userGroupDTO.setCreatedAt(userGroup.getCreatedAt());
				userGroupDTO.setUpdatedAt(userGroup.getUpdatedAt());
				userGroupDTO.setStatus(userGroup.getStatus());
				userGroupDTO.setDisplayName(userGroup.getDisplayName());
				userGroupsResponseDTO.setUserGroupDTO(userGroupDTO);
			}
			userGroupsResponseDTOList.add(userGroupsResponseDTO);
		}
		paginationResponse.setData(userGroupsResponseDTOList);
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(rolesPage.getNumber());
		paginationDTO.setLimit(rolesPage.getSize());
		paginationDTO.setTotalPages(rolesPage.getTotalPages());
		paginationDTO.setLastPage(rolesPage.isLast());
		paginationDTO.setTotalCounts(rolesPage.getTotalElements());
		paginationResponse.setPagination(paginationDTO);

		return paginationResponse;
	}

	@Override
	public RolesWithPermissionRequestRequestDTO getRoleDetailsWithPermissions(Integer roleId) {
		logger.info("method to get role details with permissions invoked.");
		logger.debug(className + "RolesWithPermissionRequestRequestDTO() ");
		RolesWithPermissionRequestRequestDTO response = new RolesWithPermissionRequestRequestDTO();
		Optional<Roles> roleOp = rolesRepository.findById(roleId);
		if (!roleOp.isPresent())
			throw new RoleNotFoundException(environment.getProperty(ResponseMessages.ROLES_NOT_FOUND_ERROR));

		RolesDTO roleDTO = ObjectMapperUtils.map(roleOp.get(), RolesDTO.class);
		List<RolePermissionsDTO> rolePermissionsDTOs = rolePermissionsService.getAllRolePermissionsById(roleId);

		response.setRoles(new RolesRequestResponseDTO(roleDTO));
		response.setPermissions(
				RolePermissionsRequestResponseDTO.getRolePermissionsRequestResponseDTO(rolePermissionsDTOs));
		return response;
	}

	@Override
	public RolesDTO getRoleByIdAndStatus(Integer roleId) {
		logger.info("method to get role by roleId and status invoked.");
		logger.debug(className + "getRoleByIdAndStatus() invoked");
		Optional<Roles> roleOp = rolesRepository.findById(roleId);
		return ObjectMapperUtils.map(roleOp.get(), RolesDTO.class);
	}

	@Override
	public List<RolesDTO> findAllByRoleIdInAndStatus(List<Integer> roleIds) {
		logger.info("method to get all role invoked.");
		logger.debug(className + "findAllByRoleIdInAndStatus() invoked");
		List<Roles> roles = rolesRepository.findAllByRoleIdInAndStatus(roleIds, Status.ACTIVE);
		return ObjectMapperUtils.mapAll(roles, RolesDTO.class);
	}

//	@Override
//	public List<UsersRoleDetailsDTO> getUsersRoleDetails(Integer userId) {
//		logger.info(environment.getProperty(ResponseMessages.GET_USERS_ROLE_DETAILS_LOG));
//		logger.debug(className + "getUsersRoleDetails()  ");
//
//		List<Integer> groupIds = userGroupMembershipService.findGroupIdsByUserId(userId);
//		List<Integer> roleIds = userGroupsService.getAllUserGroupsById(groupIds);
//
//		List<Roles> roles = rolesRepository.getAllByRoleIds(roleIds, Status.ACTIVE);
//		List<UsersRoleDetailsDTO> result = new ArrayList<>();
//		for (Roles role : roles) {
//			List<LocationCategoryPermissionsDTO> locationDetails = rolePermissionsService
//					.getLocationCategoriesPermissionsByRoleId(role.getRoleId());
//			UsersRoleDetailsDTO usersRoleDetailsDTO = new UsersRoleDetailsDTO();
//			usersRoleDetailsDTO.setRoles(ObjectMapperUtils.map(role, RolesDTO.class));
//			usersRoleDetailsDTO.setLocationDetails(locationDetails);
//			result.add(usersRoleDetailsDTO);
//		}
//
//		return result;
//
//	}

	@Override
	public List<RolesDTO> getSysetmRoles() {
		logger.info("method to get system role invoked.");
		logger.debug(className + "getSysetmRoles()");
		List<Roles> roles = rolesRepository.getByIsSystemRoleAndStatus(true, Status.ACTIVE);
		return ObjectMapperUtils.mapAll(roles, RolesDTO.class);
	}

//	@Override
//	public List<PermissionsDTO> getPermissionsDetails(Integer userId) {
//		logger.info(environment.getProperty(ResponseMessages.GET_PERMISSIONS_DETAILS_LOG));
//		logger.debug(className + "getUsersRoleDetails()");
//
//		List<Integer> groupIds = userGroupMembershipService.findGroupIdsByUserId(userId);
//		List<Integer> roleIds = userGroupsService.getAllUserGroupsById(groupIds);
//
//		List<Integer> allDistinctPermissionsByRoleIds = rolePermissionMappingService
//				.getAllDistinctPermissionsByRoleIds(roleIds);
//		return permissionsService.getPermissionsByPermissionIds(allDistinctPermissionsByRoleIds);
//
//	}

	@Override
	public PaginationResponseDTO<UserGroupsResponseDTO> getRoleDetailsSearchBased(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType, Specification<UserGroupDetails> spec) {
		logger.info(className + "getRoleDetailsSearchBased() invoked ");

		Sort sort = sortType.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		PaginationResponseDTO<UserGroupsResponseDTO> paginationResponse = new PaginationResponseDTO<>();
		List<UserGroupsResponseDTO> userGroupsResponseDTOList = new ArrayList<>();
		Page<UserGroupDetails> rolesPage = userGroupsDetailsRepository.findAll(spec, pageable);
		List<UserGroupDetails> userGroups = rolesPage.getContent();

		for (UserGroupDetails userGroupDetails : userGroups) {
			UserGroupsResponseDTO userGroupsResponseDTO = new UserGroupsResponseDTO();
			UserGroupDTO userGroupsDTO = null;
			if (userGroupDetails.getGroupId() != null) {
				userGroupsDTO = new UserGroupDTO();
				userGroupsDTO.setDisplayName(userGroupDetails.getDisplayName());
				userGroupsDTO.setGroupId(userGroupDetails.getGroupId());
				userGroupsDTO.setGroupName(userGroupDetails.getGroupName());
			}

			RolesDTO rolesDTO = new RolesDTO();
			rolesDTO.setRoleId(userGroupDetails.getRoleId());
			rolesDTO.setRoleName(userGroupDetails.getRoleName());
			rolesDTO.setSystemRole(userGroupDetails.isSystemRole());

			userGroupsResponseDTO.setRolesDTO(rolesDTO);
			userGroupsResponseDTO.setUserGroupDTO(userGroupsDTO);
			userGroupsResponseDTOList.add(userGroupsResponseDTO);
		}

		paginationResponse.setData(userGroupsResponseDTOList);
		PaginationDTO paginationDTO = new PaginationDTO();
		paginationDTO.setPage(rolesPage.getNumber());
		paginationDTO.setLimit(rolesPage.getSize());
		paginationDTO.setTotalPages(rolesPage.getTotalPages());
		paginationDTO.setLastPage(rolesPage.isLast());
		paginationDTO.setTotalCounts(rolesPage.getTotalElements());
		paginationResponse.setPagination(paginationDTO);

		return paginationResponse;
	}

	@Override
	public List<PermissionsDTO> getPermissionsDetails(Integer userId) {
		logger.info(environment.getProperty(ResponseMessages.GET_PERMISSIONS_DETAILS_LOG));
		logger.debug(className + "getUsersRoleDetails()");

		List<Integer> groupIds = userGroupMembershipService.findGroupIdsByUserId(userId);
		List<Integer> roleIds = userGroupsService.getAllUserGroupsById(groupIds);

		List<Integer> allDistinctPermissionsByRoleIds = rolePermissionMappingService
				.getAllDistinctPermissionsByRoleIds(roleIds);
		return permissionsService.getPermissionsByPermissionIds(allDistinctPermissionsByRoleIds);
	}

}
