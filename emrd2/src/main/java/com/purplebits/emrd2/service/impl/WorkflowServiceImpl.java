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

import com.purplebits.emrd2.dto.WorkflowDTO;
import com.purplebits.emrd2.entity.Workflow;
import com.purplebits.emrd2.exceptions.WorkflowNotFoundException;
import com.purplebits.emrd2.repositories.WorkflowRepository;
import com.purplebits.emrd2.service.WorkflowService;
import com.purplebits.emrd2.util.ObjectMapperUtils;
import com.purplebits.emrd2.util.ResponseMessages;

@Service
public class WorkflowServiceImpl implements WorkflowService {
	
	private final Logger logger = LogManager.getLogger(WorkflowServiceImpl.class);
	private final String className = WorkflowServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private WorkflowRepository workflowRepository;

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public WorkflowDTO createNewWorkFlow(WorkflowDTO workflowDTO) {
		logger.info("method to add new workflow invoked." + workflowDTO);
		logger.debug(className + " createNewWorkFlow()");
		
		Workflow workflow = ObjectMapperUtils.map(workflowDTO, Workflow.class);
		workflow=workflowRepository.saveAndFlush(workflow);
		return ObjectMapperUtils.map(workflow, WorkflowDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public WorkflowDTO updateWorkFlow(WorkflowDTO workflowDTO) {
		logger.info("method to update workflow invoked." + workflowDTO);
		logger.debug(className + " updateWorkFlow()");
		
		Optional<Workflow> workflowOp = workflowRepository.findById(workflowDTO.getWorkflowId());
		if(!workflowOp.isPresent())throw new WorkflowNotFoundException(environment.getProperty(ResponseMessages.WORKFLOW_NOT_EXIST));
		
		Workflow workflow = ObjectMapperUtils.map(workflowDTO, Workflow.class);
		workflow=workflowRepository.saveAndFlush(workflow);
		return ObjectMapperUtils.map(workflow, WorkflowDTO.class);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
	@Override
	public WorkflowDTO deleteWorkFlow(WorkflowDTO workflowDTO) {

		return null;
	}

	@Override
	public WorkflowDTO getWorkFlowById(Integer workflowId) {
		logger.info("method to get workflow by id invoked. " + workflowId);
		logger.debug(className + " getWorkFlowById()");
		
		Optional<Workflow> workflowOp = workflowRepository.findById(workflowId);
		if(!workflowOp.isPresent())throw new WorkflowNotFoundException(environment.getProperty(ResponseMessages.WORKFLOW_NOT_EXIST));
		Workflow workflow = workflowOp.get();
		return ObjectMapperUtils.map(workflow, WorkflowDTO.class);
	}

}
