package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.PermissionsDTO;

public interface PermissionsService {
	PermissionsDTO addNewPermission(PermissionsDTO permissionsDTO);

	List<PermissionsDTO> getAllPermissions();

	PermissionsDTO getPermissionById(Integer permissionId);

	PermissionsDTO updatePermission(PermissionsDTO permissionsDTO);

	PermissionsDTO deletePermission(Integer permissionId);

	List<PermissionsDTO> findAllByPermissionIdIn(List<Integer> permissionsIds);

	List<PermissionsDTO> getAllPermissionsLesserThanGivenMask(int mask);

	List<PermissionsDTO> addAllNewPermission(List<PermissionsDTO> permissionsDTOs);

	List<PermissionsDTO> getPermissionsByPermissionIds(List<Integer> allDistinctPermissionsByRoleIds);

	PermissionsDTO getPermissionByName(String name);
}
