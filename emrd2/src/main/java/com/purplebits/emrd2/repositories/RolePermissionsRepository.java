package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Permissions;
import com.purplebits.emrd2.entity.RolePermissions;
import com.purplebits.emrd2.types.Status;


public interface RolePermissionsRepository extends JpaRepository<RolePermissions, Integer> {

	@Modifying
    @Query("UPDATE RolePermissions rp SET rp.status = :status WHERE rp.roles.roleId = :roleId")
	int deleteAllByRoleId(@Param("roleId")Integer roleId,@Param("status") Status status);

	@Query("SELECT r FROM RolePermissions r WHERE r.permissions.permissionId = :permissionId AND r.roles.roleId = :roleId")
	Optional<RolePermissions> findByPermissionIdAndRoleId(@Param("permissionId")Integer permissionId, @Param("roleId") Integer roleId);

	@Query("SELECT COUNT(r) FROM RolePermissions r WHERE r.roles.roleId = :roleId AND r.status = :status")
    Integer findCountByRoleIdAndStatus(@Param("roleId")Integer roleId,@Param("status") Status status);

	@Query("SELECT r FROM RolePermissions r WHERE r.roles.roleId = :roleId AND r.status = :status")
    Optional<RolePermissions> findByRoleIdAndStatus(@Param("roleId")Integer roleId,@Param("status") Status status);

	@Query("SELECT rp FROM RolePermissions rp WHERE rp.roles.roleId = :roleId AND rp.status = :status")
    List<RolePermissions> findAllByRoleIdAndStatus(@Param("roleId")Integer roleId,@Param("status") Status active);
	@Query("select rp from RolePermissions rp WHERE rp.rolePermissionId = :rolePermissionId AND rp.status = :status ")
	Optional<RolePermissions> findByRolePermissionIdAndStatus(@Param("rolePermissionId") Integer rolePermissionId,@Param("status") Status status);

	Page<RolePermissions> findAllByStatus(Status active, Pageable pageable);

	@Query("SELECT p FROM Permissions p WHERE p.permissionId IN (SELECT DISTINCT (rp.permissions.permissionId) FROM RolePermissions rp WHERE rp.roles.roleId = :roleId AND rp.status = :status)")
    List<Permissions> findDistinctPermissionsByRoleIdAndStatus(@Param("roleId") Integer roleId,@Param("status") Status status);
    
}
