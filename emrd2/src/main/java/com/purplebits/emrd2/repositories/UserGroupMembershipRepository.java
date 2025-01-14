package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Roles;
import com.purplebits.emrd2.entity.UserGroupMembership;
import com.purplebits.emrd2.entity.UserGroups;
import com.purplebits.emrd2.entity.Users;
import com.purplebits.emrd2.types.Status;


public interface UserGroupMembershipRepository extends JpaRepository<UserGroupMembership, Integer> {
	
//	@Query("SELECT u FROM UserGroupMembership u WHERE u.membershipId = :membershipId AND u.status = :status")
//	Optional<UserGroupMembership> findByUserGroupMembershipIdAndStatus(@Param("membershipId") Integer locationCategoryId,@Param("status") Status status);

	@Query("SELECT u FROM UserGroupMembership u  WHERE u.membershipId = :membershipId AND u.status = :status")
	Optional<UserGroupMembership> findByUserGroupMembershipIdAndStatus(@Param("membershipId") Integer membershipId, @Param("status") Status status);

	
	Page<UserGroupMembership> findAllByStatus(Status active, Pageable pageable);
	
//	@Query("SELECT u.fullName FROM UserGroupMembership ugm JOIN ugm.users u WHERE ugm.groups.groupId = :groupId")
	@Query("SELECT u.fullName FROM UserGroupMembership ugm " +
	           "JOIN ugm.users u " +
	           "JOIN ugm.groups g " +
	           "WHERE g.groupId = :groupId AND u.status = 'ACTIVE'")
    List<String> findMembersByGroupId(Integer groupId);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM UserGroupMembership ugm " +
           "WHERE ugm.users.username = :username AND ugm.groups.groupId = :groupId")
    void deleteUserFromGroup(@Param("username") String username, @Param("groupId") Integer groupId);
	
	//by sajid
	  @Query("SELECT m.membershipId FROM UserGroupMembership m WHERE m.groups.groupId = :groupId")
	    List<Integer> findAllMembershipIdsByGroupId(@Param("groupId") Integer groupId);
	  
	  @Query("SELECT ugm.groups.groupId FROM UserGroupMembership ugm WHERE ugm.users.userId = :userId AND ugm.status = :status AND ugm.groups.status = 'ACTIVE'")
	  List<Integer> findGroupIdsByUserId(@Param("userId") Integer userId, @Param("status") Status status);
	  
	  @Query("SELECT ugm.membershipId FROM UserGroupMembership ugm WHERE ugm.users.userId = :userId AND ugm.status=:status")
	  List<Integer> findAllGroupMembershipIdsByUserId(@Param("userId") Integer userId, @Param("status") Status status);

	 
	  // Method to find members by group ID and status
	    @Query("SELECT u FROM UserGroupMembership ugm JOIN ugm.users u WHERE ugm.groups.groupId = :groupId AND u.status = :status")
	    Page<Users> findMembersByGroupIdAndStatus(@Param("groupId") Integer groupId, @Param("status") Status status, Pageable pageable);
	    
	    @Query("SELECT ugm FROM UserGroupMembership ugm WHERE ugm.users = :users AND ugm.groups = :groups AND ugm.status = :status")
	    Optional<UserGroupMembership> findByUsersAndGroupsAndStatus(@Param("users") Users users,
	                                                                @Param("groups") UserGroups groups,
	                                                                @Param("status") Status status);

	 Page<UserGroupMembership> findByUsers_UserIdAndStatus(Integer userId, Status status, Pageable pageable);
	 @Query("SELECT r FROM UserGroupMembership ugm JOIN ugm.groups g JOIN g.roles r WHERE ugm.users.userId = :userId AND ugm.status = :status")
	    Page<Roles> findRolesByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") Status status, Pageable pageable);

	 @Query("SELECT ug FROM UserGroups ug " +
	           "JOIN UserGroupMembership ugm ON ugm.groups.groupId = ug.groupId " +
	           "JOIN ugm.users u " +
	           "WHERE u.userId = :userId AND ugm.status = :status " +
	           "ORDER BY ug.groupId DESC")
	    Page<UserGroups> findUserGroupsByUserIdAndStatus(@Param("userId") Integer userId, 
	                                                     @Param("status") Status status, 
	                                                     Pageable pageable);
	 
	 @Query("SELECT u FROM UserGroupMembership u WHERE u.groups.groupId = :groupId AND u.status = :status")
	 List<UserGroupMembership> findAllMembersByGroupIdAndStatus(@Param("groupId") Integer groupId, @Param("status") Status status);
	 
	 @Query("SELECT ugm FROM UserGroupMembership ugm WHERE ugm.users = :users AND ugm.groups = :groups")
	 Optional<UserGroupMembership> findByUsersAndGroups(@Param("users") Users users, @Param("groups") UserGroups groups);


	 @Query("SELECT u FROM UserGroupMembership u WHERE u.users.userId = :userId AND u.groups.groupId = :groupId AND u.status = :status")
	Optional<UserGroupMembership> findByUserIdAndGroupIdAndStatus(@Param("userId") Integer userId,@Param("groupId") Integer groupId,@Param("status") Status status);

	 @Modifying
	 @Query("DELETE FROM UserGroupMembership ugm WHERE ugm.users.userId = :userId")
	Integer deleteAllByUsersUserId(@Param("userId") Integer userId);

	 @Modifying
	 @Query("DELETE FROM UserGroupMembership ugm WHERE ugm.groups.groupId = :groupId")
	Integer deleteAllUserGroupMembershipsByGroupId(@Param("groupId")Integer groupId);


	 @Query("SELECT ug.roles FROM UserGroupMembership ugm " +
	           "JOIN ugm.groups ug " +
	           "JOIN ug.roles r " +
	           "WHERE ugm.users.userId = :userId")
	    List<Roles> findRolesByUserId(@Param("userId") Integer userId);


	 @Query("SELECT u FROM UserGroupMembership u WHERE u.users.userId = :userId AND u.groups.groupId = :groupId")
	Optional<UserGroupMembership> findByUserIdAndGroupId(@Param("userId") Integer userId,@Param("groupId") Integer groupId);

	 @Query("SELECT u FROM UserGroupMembership ugm JOIN ugm.users u WHERE ugm.groups.groupId = :groupId AND u.status = :status")
	    List<Users> findUsersByGroupIdAndStatus(@Param("groupId") Integer groupId, @Param("status") Status status);
	 

}
