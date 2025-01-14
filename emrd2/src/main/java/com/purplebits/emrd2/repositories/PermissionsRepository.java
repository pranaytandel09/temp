package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Permissions;
import com.purplebits.emrd2.types.Status;

public interface PermissionsRepository extends JpaRepository<Permissions, Integer> {
	List<Permissions> findByStatus(Status status);

	boolean existsByPermissionName(String permissionName);

	Optional<Permissions> findByPermissionIdAndStatus(Integer permissionId, Status status);

	Optional<Permissions> findByPermissionName(String permissionName);

	@Query("SELECT p FROM Permissions p WHERE p.permissionId IN :permissionIds")
	List<Permissions> findAllByPermissionIdIn(@Param("permissionIds") List<Integer> permissionIds);

	Optional<Permissions> findByPermissionNameAndStatus(String permissionName, Status active);

	Optional<Permissions> findByDisplayNameAndStatus(String displayName, Status active);

	@Query("SELECT MAX(p.mask) FROM Permissions p")
	Integer findMaxMask();

	@Query("SELECT p FROM Permissions p WHERE p.mask =:mask")
	Optional<Permissions> findByMask(@Param("mask") Integer mask);

	@Query("SELECT p FROM Permissions p WHERE p.mask IN :mask")
	List<Permissions> findAllByPermissionMaskIn(@Param("mask") List<Integer> maskIds);

	@Query("SELECT p FROM Permissions p WHERE p.mask <= :mask")
	List<Permissions> findAllByPermissionMaskLessThan(int mask);

}
