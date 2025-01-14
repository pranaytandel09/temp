package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.AclObjectIdentityDTO;

public interface AclObjectIdentityService {

	AclObjectIdentityDTO addNewAclObjectIdentity(AclObjectIdentityDTO aclObjectIdentity);

	Integer deleteByObjectIdIdentity(Integer objectIdIdentity);

	AclObjectIdentityDTO getObjectIdIdentityAclClassAndOwnerSide(Integer objectIdIdentity, Long aclClassId);

	void deleteByAclObjectIdentityId(Long id);

	List<Long> getAllByObjectIdIdentityAndAclClass(Integer objectIdIdentity, Long aclClassId);

	Integer deleteByObjectIdentityIds(List<Long> aclObjectIdentityIds);

	AclObjectIdentityDTO getAclObjectIdentityByObjectIdIdentityAndAclClass(Integer objectIdIdentity, Long aclClassId);

}
