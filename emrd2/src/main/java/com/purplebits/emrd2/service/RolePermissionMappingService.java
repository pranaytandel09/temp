package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.RolePermissionMappingDTO;


public interface RolePermissionMappingService {

	List<RolePermissionMappingDTO> addAllNewRolePermission(List<RolePermissionMappingDTO> rolePermissionsDTOs);

	List<Integer> getAllDistinctPermissionsByRoleIds(List<Integer> roleIds);

}
