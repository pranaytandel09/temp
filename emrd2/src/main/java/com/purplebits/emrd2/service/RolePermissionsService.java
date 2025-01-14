package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.PaginationResponseDTO;
import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.dto.RolePermissionsDTO;
import com.purplebits.emrd2.dto.request_response.RolePermissionsRequestResponseDTO;


public interface RolePermissionsService {

    public RolePermissionsDTO addNewRolePermissions(RolePermissionsDTO rolePermissionsDTO);
    public RolePermissionsDTO updateRolePermissions(RolePermissionsDTO rolePermissionsDTO);
    public RolePermissionsDTO getRolePermissionsById(Integer rolePermissionId);
    public PaginationResponseDTO<RolePermissionsRequestResponseDTO> getAllRolePermissions(Integer pageNumber, Integer pageSize, String sortBy, String sortType);
    public RolePermissionsDTO deleteRolePermissions(Integer rolePermissionId);
	public void deleteAllRolesByRoleId(Integer roleId);
	public List<RolePermissionsDTO> addOrUpdateRoleWithPermissions(List<RolePermissionsDTO> rolesWithPermissionsDTOs);
	public List<RolePermissionsDTO> getAllRolePermissionsById(Integer roleId);
	public List<PermissionsDTO> findDistinctPermissionsByRoleIdAndStatus(Integer roleId);
}
