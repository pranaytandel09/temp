package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.AclObjectIdentity;

public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {

	@Query("SELECT acl FROM AclObjectIdentity acl WHERE acl.aclClass.className = :className AND acl.ownerSid.sid = :sid")
	Optional<AclObjectIdentity> findByAclClassClassNameAndOwnerSidSid(@Param("className") String className,
			@Param("sid") String sid);

	@Query("SELECT acl FROM AclObjectIdentity acl WHERE acl.objectIdIdentity = :fileId")
	Optional<AclObjectIdentity> getByFileId(@Param("fileId") Integer fileId);

	@Modifying
	@Query("DELETE FROM AclObjectIdentity acl WHERE acl.objectIdIdentity = :fileId")
	Integer deleteByFileId(Integer fileId);

	@Query("SELECT acl FROM AclObjectIdentity acl WHERE acl.objectIdIdentity = :objectIdIdentity AND acl.aclClass.id = :aclClassId")
	Optional<AclObjectIdentity> getObjectIdIdentityAclClassAndOwnerSide(
			@Param("objectIdIdentity") Integer objectIdIdentity, @Param("aclClassId") Long aclClassId);

	@Query("SELECT acl.id FROM AclObjectIdentity acl WHERE acl.objectIdIdentity = :objectIdIdentity AND acl.aclClass.id = :aclClassId")
	List<Long> getAllByObjectIdIdentityAndAclClass(@Param("objectIdIdentity") Integer objectIdIdentity,
			@Param("aclClassId") Long aclClassId);

	@Modifying
	@Query("DELETE FROM AclObjectIdentity acl WHERE acl.id IN :aclObjectIdentityIds")
	Integer deleteByObjectIdentityIds(@Param("aclObjectIdentityIds") List<Long> aclObjectIdentityIds);

	@Query("SELECT acl FROM AclObjectIdentity acl WHERE acl.objectIdIdentity = :objectIdIdentity AND acl.aclClass.id = :aclClassId")
	Optional<AclObjectIdentity> getAclObjectIdentityByObjectIdIdentityAndAclClass(
			@Param("objectIdIdentity") Integer objectIdIdentity, @Param("aclClassId") Long aclClassId);

}
