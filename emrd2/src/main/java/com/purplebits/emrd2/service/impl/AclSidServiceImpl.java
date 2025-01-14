package com.purplebits.emrd2.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.purplebits.emrd2.dto.AclSidDTO;
import com.purplebits.emrd2.entity.AclSid;
import com.purplebits.emrd2.repositories.AclSidRepository;
import com.purplebits.emrd2.service.AclSidService;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class AclSidServiceImpl implements AclSidService {
	private final String className = AclSidServiceImpl.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(AclSidServiceImpl.class);

	@Autowired
	Environment environment;

	@Autowired
	private AclSidRepository aclSidRepository;

	@Override
	public AclSidDTO findBySid(String roleName) {
		logger.info("method to find acl sid by name invoked.");
		logger.debug(className + " findBySid() ");

		Optional<AclSid> aclSidOp = aclSidRepository.findBySid(roleName);
		if (aclSidOp.isPresent())
			return ObjectMapperUtils.map(aclSidOp.get(), AclSidDTO.class);
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public AclSidDTO addNewPrincipleObject(AclSidDTO aclSidDTO) {
		logger.info("method to add new acl sid invoked.");
		logger.debug(className + " addNewPrincipleObject()");
		AclSid aclSid = ObjectMapperUtils.map(aclSidDTO, AclSid.class);
		aclSidRepository.saveAndFlush(aclSid);
		return ObjectMapperUtils.map(aclSid, AclSidDTO.class);
	}

}
