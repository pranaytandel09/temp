package com.purplebits.emrd2.service;


import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.dto.UsersRoleDetailsDTO;
import com.purplebits.emrd2.dto.request_response.RolePermissionsRequestResponseDTO;
import com.purplebits.emrd2.dto.request_response.RolesWithPermissionRequestRequestDTO;
import com.purplebits.emrd2.dto.request_response.UserGroupsResponseDTO;
import com.purplebits.emrd2.entity.UserGroupDetails;

public interface RolesService {
    RolesDTO addNewRole(RolesDTO rolesDTO);

    List<RolesDTO> getAllRoles();

    RolesDTO getRoleById(Integer roleId);

    RolesDTO updateRole(RolesDTO rolesDTO);

    RolesDTO deleteRole(Integer roleId);

    RolesWithPermissionRequestRequestDTO addOrUpdateRole(RolesDTO rolesDTO, List<RolePermissionsRequestResponseDTO> permissions);

      PaginationResponseDTO<UserGroupsResponseDTO> getUsersGroups(Integer pageNumber, Integer pageSize, String sortBy, String sortType);

	RolesWithPermissionRequestRequestDTO getRoleDetailsWithPermissions(Integer roleId);

	RolesDTO getRoleByIdAndStatus(Integer roleId);

	List<RolesDTO> findAllByRoleIdInAndStatus(List<Integer> roleIds);

//	List<UsersRoleDetailsDTO> getUsersRoleDetails(Integer userId);

	List<RolesDTO> getSysetmRoles();

//	List<PermissionsDTO> getPermissionsDetails(Integer userId);

	PaginationResponseDTO<UserGroupsResponseDTO> getRoleDetailsSearchBased(Integer pageNumber, Integer pageSize,
			String sortBy, String sortType, Specification<UserGroupDetails> spec);

	List<PermissionsDTO> getPermissionsDetails(Integer userId);
	 
}
