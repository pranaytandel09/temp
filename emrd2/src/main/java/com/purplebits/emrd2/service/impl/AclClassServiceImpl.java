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

import com.purplebits.emrd2.dto.AclClassDTO;
import com.purplebits.emrd2.entity.AclClass;
import com.purplebits.emrd2.repositories.AclClassRepository;
import com.purplebits.emrd2.service.AclClassService;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class AclClassServiceImpl implements AclClassService {
	private final String className = AclClassServiceImpl.class.getSimpleName();
	private static final Logger logger = LogManager.getLogger(AclClassServiceImpl.class);

	@Autowired
	private AclClassRepository aclClassRepository;
	@Autowired
	Environment environment;

	@Override
	public AclClassDTO findByClassName(String name) {
		logger.info("method for finding acl class by name invoked.");
		logger.debug(className + " findByClassName() ");
		Optional<AclClass> aclOp = aclClassRepository.findByClassName(name);
		if (aclOp.isPresent())
			return ObjectMapperUtils.map(aclOp.get(), AclClassDTO.class);
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public AclClassDTO addNewAclClass(AclClassDTO aclClassDTO) {
		logger.info("method for adding acl class invoked.");
		logger.debug(className + " addNewAclClass()");
		AclClass aclClass = ObjectMapperUtils.map(aclClassDTO, AclClass.class);
		aclClass = aclClassRepository.saveAndFlush(aclClass);
		return ObjectMapperUtils.map(aclClass, AclClassDTO.class);
	}

}
