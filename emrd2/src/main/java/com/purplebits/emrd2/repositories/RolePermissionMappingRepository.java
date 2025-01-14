package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.RolePermissionMapping;
import com.purplebits.emrd2.types.Status;


public interface RolePermissionMappingRepository extends JpaRepository<RolePermissionMapping, Integer> {

	@Query("SELECT rp FROM RolePermissionMapping rp WHERE rp.roles.roleId = :roleId AND rp.permissions.permissionId = :permissionId")
	Optional<RolePermissionMapping> findByRoleIdAndPermissionId(@Param("roleId") Integer roleId,@Param("permissionId") Integer permissionId);

	@Query("SELECT DISTINCT(rp.permissions.permissionId) FROM RolePermissionMapping rp WHERE rp.roles.roleId IN :roleId AND rp.status = :status")
	List<Integer> getAllDistinctPermissionsByRoleIds(@Param("roleId")List<Integer> roleIds,@Param("status")Status status);

}
