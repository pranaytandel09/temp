package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.AclEntryDTO;

public interface AclEntryService {

	AclEntryDTO addNewAclEntry(AclEntryDTO aclEntry);

	List<Integer> findDistinctMaskByObjectIdIdentityAndClassNameAndPrincipalAndSid(Integer objectIdIdentity,
			String className, Boolean principal, List<String> sids);

	Integer deleteByAclObjectIdentity(Long id);

	Integer deleteByAclObjectIdentityOwnerSidAndMask(Long aclObjectIdentityId, Long ownerSidId, Integer mask);

	Integer deleteByObjectIdentityIds(List<Long> aclObjectIdentityIds);

	Integer findByAclObjectIdentity(Long id);

	Integer deleteByAclObjectIdentityOwnerSid(Long aclObjectIdentityId, Long ownerSidId);

	void deleteALLByAclSid(Long ownerSidId);
}
