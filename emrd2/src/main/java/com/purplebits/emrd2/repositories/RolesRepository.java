package com.purplebits.emrd2.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.purplebits.emrd2.entity.Roles;
import com.purplebits.emrd2.types.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Integer> {
	
	 @Query("SELECT r FROM Roles r WHERE r.roleName = :roleName")
	Optional<Roles> findByRoleName(@Param("roleName")String roleName);
    List<Roles> findByStatus(Status status);
    boolean existsByRoleName(String roleName);
    
    @Query("SELECT r FROM Roles r WHERE r.roleId = :roleId AND r.status = :status")
    Optional<Roles> findByIdAndStatus(@Param("roleId") Integer roleId, @Param("status") Status status);
//    @Query("SELECT r FROM Roles r WHERE  r.status = :status")
//    Optional<Roles> findByStatus( @Param("status") Status status);

    Optional<Roles> findByRoleNameAndStatus(String roleName, Status status);
	@Query("SELECT r FROM Roles r WHERE r.roleId IN (:roleIds) AND r.status = :status")
	List<Roles> findAllByRoleIdInAndStatus(@Param("roleIds")List<Integer> roleIds,@Param("status") Status active);
	
	@Query("SELECT r FROM Roles r WHERE r.roleId IN :roleIds AND r.status = :status")
	List<Roles> getAllByRoleIds(@Param("roleIds")List<Integer> roleIds, @Param("status")Status status);

	@Query("SELECT r FROM Roles r WHERE r.isSystemRole = :isSystemRole AND r.status = :status")
	List<Roles> getByIsSystemRoleAndStatus(@Param("isSystemRole")boolean isSystemRole, @Param("status")Status status);
	@Query("SELECT r FROM Roles r WHERE r.status = :status")
	Page<Roles> findAllByStatus(@Param("status")Status status, Pageable pageable);



}
