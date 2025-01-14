package com.purplebits.emrd2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.AclEntry;

public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {
	// Additional query methods if needed

	@Query("SELECT DISTINCT ae.mask " + "FROM AclEntry ae " + "WHERE ae.aclObjectIdentity.id IN ("
			+ "   SELECT aoi.id FROM AclObjectIdentity aoi " + "   WHERE aoi.objectIdIdentity = :objectIdIdentity "
			+ "   AND aoi.aclClass.id = (" + "       SELECT ac.id FROM AclClass ac "
			+ "       WHERE ac.className = :className" + "   )" + ") " + "AND ae.sid.id IN ("
			+ "   SELECT s.id FROM AclSid s " + "   WHERE s.principal = :principal " + "   AND s.sid IN :sids" + ")")
	List<Integer> findDistinctMaskByObjectIdIdentityAndClassNameAndPrincipalAndSid(
			@Param("objectIdIdentity") Integer objectIdIdentity, @Param("className") String className,
			@Param("principal") Boolean principal, @Param("sids") List<String> sids);

	@Modifying
	@Query("DELETE FROM AclEntry ae WHERE ae.aclObjectIdentity.id = :id")
	Integer deleteByAclObjectIdentity(Long id);

	@Modifying
	@Query("DELETE FROM AclEntry ae WHERE ae.aclObjectIdentity.id = :aclObjectIdentity AND ae.sid.id = :ownerSid AND ae.mask = :mask")
	Integer deleteByAclObjectIdentityOwnerSidAndMask(@Param("aclObjectIdentity") Long aclObjectIdentity,
			@Param("ownerSid") Long ownerSid, @Param("mask") Integer mask);

	@Modifying
	@Query("DELETE FROM AclEntry ae WHERE ae.aclObjectIdentity.id IN :aclObjectIdentity")
	Integer deleteByObjectIdentityIds(@Param("aclObjectIdentity") List<Long> aclObjectIdentityIds);

	@Query("SELECT COUNT(ae) FROM AclEntry ae WHERE ae.aclObjectIdentity.id = :id")
	Integer findByAclObjectIdentity(Long id);

	@Modifying
	@Query("DELETE FROM AclEntry ae WHERE ae.aclObjectIdentity.id = :aclObjectIdentity AND ae.sid.id = :ownerSid")
	Integer deleteByAclObjectIdentityOwnerSid(@Param("aclObjectIdentity") Long aclObjectIdentityId,
			@Param("ownerSid") Long ownerSidId);

	@Modifying
	@Query("DELETE FROM AclEntry ae WHERE ae.sid.id = :ownerSid")
	Integer deleteByOwnerSid(@Param("ownerSid") Long sidId);

}
