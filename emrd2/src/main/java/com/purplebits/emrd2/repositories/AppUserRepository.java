package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Users;
import com.purplebits.emrd2.types.Status;

public interface AppUserRepository extends JpaRepository<Users, Integer> {

	@Query("SELECT u FROM Users u WHERE u.email = :email AND u.status IN (:statuses)")
	public Users findByEmailAndStatus(String email, List<Status> statuses);
	
	@Query("SELECT u FROM Users u WHERE u.email = :email AND u.status = :status") 
	public Users findByEmailAndStatus(@Param("email")String email,@Param("status") Status status);

	@Query("SELECT u FROM Users u WHERE u.email = :email")
	public Users findByEmail(String email);
	
	@Query("SELECT u FROM Users u WHERE u.userId = :userId AND u.username = :username AND u.status = :status")
    Optional<Users> findByUserIdAndUsernameAndStatus(@Param("userId") Integer userId,
                                                     @Param("username") String username,
                                                     @Param("status") Status status);

	@Query("SELECT u FROM Users u WHERE u.userId IN :userIds AND u.status = :status")
    public List<Users> findAllByUserIdAndStatus(@Param("userIds") List<Integer> userIds, @Param("status") Status status);
	
	@Query("SELECT u FROM Users u WHERE u.userId = :userIds AND u.status = :status")
    public Optional<Users> findByUserIdAndStatus(@Param("userIds") Integer userIds, @Param("status") Status status);
	
	
	@Query("SELECT u FROM Users u WHERE u.status = :status")
	 List<Users> findAllByStatus(@Param("status") Status status);
	
	@Query("SELECT u FROM Users u WHERE u.userId = :userId AND u.status = :status")
	public Optional<Users> findByIdAndStatus(@Param("userId") Integer userId, @Param("status") Status status);
	
	@Query("SELECT u FROM Users u WHERE (u.username = :username OR u.email = :email) AND u.status = :status")
	public Users findByUserNameAndEmailAndStatus(@Param("username") String username, @Param("email") String email, @Param("status") Status status);

	@Query("SELECT u FROM Users u WHERE u.username = :username  AND u.status = :status")
	public Optional<Users> findByUserNameAndStatus(@Param("username") String username, @Param("status") Status status);

	@Query("SELECT u FROM Users u WHERE  u.email = :email  AND u.username = :username AND u.status = :status")
	public Optional<Users> findByEmailAndUserNameAndStatus(@Param("email") String email,@Param("username") String username, @Param("status") Status status);
	@Query("SELECT u FROM Users u WHERE u.username = :username")
	public Optional<Users> findByUserName(@Param("username")String username);
	@Query("SELECT u FROM Users u WHERE u.email = :email  AND u.status = :status")
	 Users findByEmailsAndStatus(@Param("email") String username, @Param("status") Status status);
	@Modifying
	@Query("DELETE FROM Users u WHERE u.userId = :userId")
	public Integer deleteByUserId(@Param("userId") Integer userId);

	@Query("SELECT u FROM Users u WHERE u.status = :status")
	public Page<Users> findAllUsersByStatus(@Param("status") Status status, Pageable pageable);
	
	@Query("""
		    SELECT u
		    FROM Users u
		    JOIN UserGroupMembership ugm ON u.userId = ugm.users.userId
		    WHERE ugm.groups.groupId IN (
		        SELECT ugm2.groups.groupId
		        FROM UserGroupMembership ugm2
		        WHERE ugm2.users.userId = :userId
		    )
		    AND u.userId != :userId
		    AND u.status = :status
		""")
	Page<Users> findUsersInSameGroup(@Param("userId") Integer userId, @Param("status") Status status,Pageable pageable);
	
}
