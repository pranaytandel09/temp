package com.purplebits.emrd2.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.UserGroups;
import com.purplebits.emrd2.types.Status;
public interface UserGroupsRepository extends JpaRepository<UserGroups, Integer> {
	
	Page<UserGroups> findAllByStatus(Status active, Pageable pageable);
	List<UserGroups> findAllByStatus(Status active);
	
	@Query("SELECT u FROM UserGroups u WHERE u.groupName = :groupName")
	Optional<UserGroups> findByGroupName(@Param("groupName")String groupName);
	
	@Query("SELECT u FROM UserGroups u WHERE u.groupId = :groupId AND u.status = :status")
	Optional<UserGroups> findByGroupIdAndStatus(@Param("groupId") Integer groupId, @Param("status") Status status);
	@Query("SELECT ug.id FROM UserGroups ug WHERE ug.roles.roleId = :roleId")
    List<Integer> findUserGroupsIdsByRoleId(@Param("roleId") Integer roleId);
	
	  @Query("SELECT ug FROM UserGroups ug WHERE ug.roles.id = :roleId")
	    List<UserGroups> findByRolesId(@Param("roleId") Integer roleId);
	  
	  @Query("SELECT ug FROM UserGroups ug WHERE ug.roles.id = :roleId AND ug.status = :status")
	    List<UserGroups> findByRolesIdAndStatus(@Param("roleId") Integer roleId,@Param("status") Status status);
	  
	  @Query("SELECT u FROM UserGroups u WHERE u.roles.roleId = :roleId AND u.status = :status")
	  Optional<UserGroups> getUserGroupsByRoleId(@Param("roleId") Integer roleId, @Param("status") Status status);

	@Query("SELECT u FROM UserGroups u WHERE u.groupName = :groupName AND u.status = :status")
	Optional<UserGroups> findByGroupNameAndStatus(@Param("groupName") String groupName, @Param("status") Status status);
	
	 List<UserGroups> findByGroupNameIn(List<String> groupNames);
	 
	 @Query("SELECT ug FROM UserGroups ug WHERE ug.groupId = :groupId  AND ug.status = :status")
	    Optional<UserGroups> findByGroupIdAndGroupNameAndStatus(@Param("groupId") Integer groupId,	                                                           
	                                                            @Param("status") Status status);
 
	 @Query("SELECT ug FROM UserGroups ug WHERE ug.roles.id = :roleId AND ug.status = :status")
	 Optional<UserGroups> findByRoleIdAndStatus(@Param("roleId") Integer roleId, @Param("status") Status status);
	 
	 @Query("SELECT u FROM UserGroups u WHERE u.groupName = :displayName or u.displayName =:displayName")
		Optional<UserGroups> findByGroupNameOrDisplayName(@Param("displayName")String displayName);
		
	 @Query("SELECT u FROM UserGroups u WHERE (u.groupName = :displayName OR u.displayName = :displayName) AND u.groupId <> :groupId")
	 Optional<UserGroups> findByGroupNameOrDisplayNameAndSkipGroupId(
	     @Param("displayName") String displayName,
	     @Param("groupId") Integer groupId
	 );

	 @Modifying
	 @Query("DELETE FROM UserGroups u WHERE u.groupId = :groupId")
	 void deleteByGroupId(@Param("groupId") Integer groupId);
	 
	 @Query("SELECT ug.roles.roleId FROM UserGroups ug WHERE ug.groupId IN :groupIds AND ug.status = :status")
	List<Integer> getAllRoleId(@Param("groupIds")List<Integer> groupIds,@Param("status") Status status);

	 

}
