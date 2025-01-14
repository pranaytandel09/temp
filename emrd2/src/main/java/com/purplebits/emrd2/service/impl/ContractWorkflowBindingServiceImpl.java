package com.purplebits.emrd2.service.impl;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.purplebits.emrd2.dto.WorkflowDTO;
import com.purplebits.emrd2.entity.Workflow;
import com.purplebits.emrd2.repositories.ContractRepository;
import com.purplebits.emrd2.repositories.ContractWorkflowBindingRepository;
import com.purplebits.emrd2.service.ContractWorkflowBindingService;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class ContractWorkflowBindingServiceImpl implements ContractWorkflowBindingService{
	private final Logger logger = LogManager.getLogger(ContractWorkflowBindingServiceImpl.class);
	private final String className = ContractWorkflowBindingServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private ContractWorkflowBindingRepository contractWorkflowBindingRepository;
	
	@Override
	public WorkflowDTO findWorkflowByContractId(Integer contractId) {
		logger.info("method to get workflow by contract id invoked." + contractId);
		logger.debug(className + " findWorkflowByContractId()");
		Optional<Workflow> workflowOp = contractWorkflowBindingRepository.findActiveWorkflowByContractId(contractId, true);
		if(workflowOp.isPresent()) {
			return ObjectMapperUtils.map(workflowOp.get(), WorkflowDTO.class);
		}
		return null;
	}

}
