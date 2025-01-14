package com.purplebits.emrd2.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.AclEntryDTO;
import com.purplebits.emrd2.entity.AclEntry;
import com.purplebits.emrd2.repositories.AclEntryRepository;
import com.purplebits.emrd2.service.AclEntryService;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class AclEntryServiceImpl implements AclEntryService {

	private final Logger logger = LogManager.getLogger(AclEntryServiceImpl.class);
	private final String className = AclEntryServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private AclEntryRepository aclEntryRepository;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public AclEntryDTO addNewAclEntry(AclEntryDTO aclEntryDTO) {
		logger.info("method to add new acl entry invoked.");
		logger.debug(className + " addNewAclEntry()");
		AclEntry aclEntry = ObjectMapperUtils.map(aclEntryDTO, AclEntry.class);
		aclEntry = aclEntryRepository.saveAndFlush(aclEntry);

		return ObjectMapperUtils.map(aclEntry, AclEntryDTO.class);
	}

	public List<Integer> findDistinctMaskByObjectIdIdentityAndClassNameAndPrincipalAndSid(Integer objectIdIdentity,
			String className, Boolean principal, List<String> sids) {
		logger.info("method to find distinct mask invoked.");
		logger.debug(className + " findDistinctMaskByObjectIdIdentityAndClassNameAndPrincipalAndSid() ");
		return aclEntryRepository.findDistinctMaskByObjectIdIdentityAndClassNameAndPrincipalAndSid(objectIdIdentity,
				className, principal, sids);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public Integer deleteByAclObjectIdentity(Long id) {
		logger.info("method to delete acl entry by aclObjectIdentity invoked.");
		logger.debug(className + " deleteByAclObjectIdentity()  ");

		return aclEntryRepository.deleteByAclObjectIdentity(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public Integer deleteByAclObjectIdentityOwnerSidAndMask(Long aclObjectIdentity, Long ownerSid, Integer mask) {
		logger.info("method to delete acl entry by aclObjectIdentity, ownerSid and mask invoked.");
		logger.debug(className + " deleteByAclObjectIdentityOwnerSidAndMask()");

		return aclEntryRepository.deleteByAclObjectIdentityOwnerSidAndMask(aclObjectIdentity, ownerSid, mask);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public Integer deleteByObjectIdentityIds(List<Long> aclObjectIdentityIds) {
		logger.info("method to delete acl entry by aclObjectIdentities invoked.");
		logger.debug(className + " deleteByObjectIdentityIds()");

		return aclEntryRepository.deleteByObjectIdentityIds(aclObjectIdentityIds);
	}

	@Override
	public Integer findByAclObjectIdentity(Long id) {
		logger.info("method to find acl entry by aclObjectIdentity invoked.");
		logger.debug(className + " findByAclObjectIdentity() ");

		return aclEntryRepository.findByAclObjectIdentity(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public Integer deleteByAclObjectIdentityOwnerSid(Long aclObjectIdentityId, Long ownerSidId) {
		logger.info("method to delete acl entry by aclObjectIdentity, ownerSid invoked.");
		logger.debug(className + " deleteByAclObjectIdentityOwnerSid()");

		return aclEntryRepository.deleteByAclObjectIdentityOwnerSid(aclObjectIdentityId, ownerSidId);
	}

	public void deleteALLByAclSid(Long sidId) {
		logger.debug(className + " deleteALLByAclSid()");

		aclEntryRepository.deleteByOwnerSid(sidId);
	}

}
