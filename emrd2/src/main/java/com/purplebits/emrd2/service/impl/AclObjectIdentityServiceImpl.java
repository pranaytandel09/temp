package com.purplebits.emrd2.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.AclObjectIdentityDTO;
import com.purplebits.emrd2.entity.AclObjectIdentity;
import com.purplebits.emrd2.repositories.AclObjectIdentityRepository;
import com.purplebits.emrd2.service.AclEntryService;
import com.purplebits.emrd2.service.AclObjectIdentityService;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class AclObjectIdentityServiceImpl implements AclObjectIdentityService {

	private final Logger logger = LogManager.getLogger(AclObjectIdentityServiceImpl.class);
	private final String className = AclObjectIdentityServiceImpl.class.getSimpleName();

	@Autowired
	private AclObjectIdentityRepository acIdentityRepository;
	@Autowired
	private AclEntryService aclEntryService;
	@Autowired
	Environment environment;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public AclObjectIdentityDTO addNewAclObjectIdentity(AclObjectIdentityDTO aclObjectIdentityDTO) {
		logger.info("method to add new acl objectIdentity invoked.");
		logger.debug(className + " addNewAclObjectIdentity() ");
		AclObjectIdentity aclObjectIdentity = ObjectMapperUtils.map(aclObjectIdentityDTO, AclObjectIdentity.class);
		aclObjectIdentity = acIdentityRepository.saveAndFlush(aclObjectIdentity);
		return ObjectMapperUtils.map(aclObjectIdentity, AclObjectIdentityDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public Integer deleteByObjectIdIdentity(Integer objectIdentity) {
		logger.info("method to delete acl objectIdentity by Id invoked.");
		logger.debug(className + " deleteByFileId()  ");
		Optional<AclObjectIdentity> aclOptional = acIdentityRepository.getByFileId(objectIdentity);
		if (aclOptional.isPresent()) {
			aclEntryService.deleteByAclObjectIdentity(aclOptional.get().getId());
		}
		return acIdentityRepository.deleteByFileId(objectIdentity);
	}

	@Override
	public AclObjectIdentityDTO getObjectIdIdentityAclClassAndOwnerSide(Integer objectIdIdentity, Long aclClassId) {
		logger.info("method to get acl objectIdentity by objectIdIdentity, acl class and ownerSid invoked.");
		logger.debug(className + " getObjectIdIdentityAclClassAndOwnerSide()");
		Optional<AclObjectIdentity> objectIdIdentityOp = acIdentityRepository
				.getObjectIdIdentityAclClassAndOwnerSide(objectIdIdentity, aclClassId);
		if (objectIdIdentityOp.isPresent())
			return ObjectMapperUtils.map(objectIdIdentityOp.get(), AclObjectIdentityDTO.class);
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public void deleteByAclObjectIdentityId(Long id) {
		logger.info("method to delete acl objectIdentity by Id invoked.");
		logger.debug(className + " deleteByAclObjectIdentityId() ");

		acIdentityRepository.deleteById(id);
	}

	@Override
	public List<Long> getAllByObjectIdIdentityAndAclClass(Integer objectIdIdentity, Long aclClassId) {
		logger.info("method to get all acl objectIdentity by Id invoked.");
		logger.debug(className + " getAllByObjectIdIdentityAndAclClass() ");
		return acIdentityRepository.getAllByObjectIdIdentityAndAclClass(objectIdIdentity, aclClassId);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public Integer deleteByObjectIdentityIds(List<Long> aclObjectIdentityIds) {
		logger.info("method to delete acl objectIdentity by objectIdentityIds invoked.");
		logger.debug(className + " deleteByObjectIdentityIds() ");

		return acIdentityRepository.deleteByObjectIdentityIds(aclObjectIdentityIds);
	}

	@Override
	public AclObjectIdentityDTO getAclObjectIdentityByObjectIdIdentityAndAclClass(Integer objectIdIdentity,
			Long aclClassId) {
		logger.info("method to get acl objectIdentity by objectIdIdentity and acl class invoked.");
		logger.debug(className + " getAclObjectIdentityByObjectIdIdentityAndAclClass() ");
		Optional<AclObjectIdentity> objectIdIdentityOp = acIdentityRepository
				.getAclObjectIdentityByObjectIdIdentityAndAclClass(objectIdIdentity, aclClassId);
		if (objectIdIdentityOp.isPresent())
			return ObjectMapperUtils.map(objectIdIdentityOp.get(), AclObjectIdentityDTO.class);
		else
			return null;
	}

}
