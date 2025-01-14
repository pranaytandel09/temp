package com.purplebits.emrd2.dto.request_response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.purplebits.emrd2.dto.PermissionsDTO;
import com.purplebits.emrd2.dto.RolePermissionsDTO;
import com.purplebits.emrd2.dto.RolesDTO;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class RolePermissionsRequestResponseDTO {

    private Integer rolePermissionId;

    private Integer roleId;
    private Integer permissionId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public RolePermissionsRequestResponseDTO() {
        super();
    }

    public RolePermissionsRequestResponseDTO(RolePermissionsDTO rolePermissionsDTO) {
        super();
        this.rolePermissionId = rolePermissionsDTO.getRolePermissionId();
        this.roleId = rolePermissionsDTO.getRoles().getRoleId(); 
        this.permissionId = rolePermissionsDTO.getPermissions().getPermissionId();
        this.createdAt = rolePermissionsDTO.getCreatedAt();
        this.updatedAt = rolePermissionsDTO.getUpdatedAt();
        this.status = rolePermissionsDTO.getStatus();
    }

    @JsonIgnore
    public RolePermissionsDTO getRolePermissionsDTO() {
        RolePermissionsDTO rolePermissionsDTO = new RolePermissionsDTO();
        rolePermissionsDTO.setRolePermissionId(rolePermissionId);
        
        RolesDTO rolesDTO = new RolesDTO();
        rolesDTO.setRoleId(roleId);
        rolePermissionsDTO.setRoles(rolesDTO);
        
        PermissionsDTO permissionsDTO = new PermissionsDTO();
        permissionsDTO.setPermissionId(permissionId);
        rolePermissionsDTO.setPermissions(permissionsDTO);
        
        rolePermissionsDTO.setCreatedAt(createdAt);
        rolePermissionsDTO.setUpdatedAt(updatedAt);
        rolePermissionsDTO.setStatus(status);
        return rolePermissionsDTO;
    }

    public static List<RolePermissionsRequestResponseDTO> getRolePermissionsRequestResponseDTO(
            List<RolePermissionsDTO> rolePermissionsDTOs) {
        List<RolePermissionsRequestResponseDTO> res = new ArrayList<>();
        for (RolePermissionsDTO rolePermissionsDTO : rolePermissionsDTOs) {
            res.add(new RolePermissionsRequestResponseDTO(rolePermissionsDTO));
        }
        return res;
    }

    public Integer getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(Integer rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JsonUtils.createGson().toJson(this);
    }
}