package com.purplebits.emrd2.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.purplebits.emrd2.dto.StepDTO;
import com.purplebits.emrd2.dto.WorkflowDTO;
import com.purplebits.emrd2.entity.Step;
import com.purplebits.emrd2.entity.Workflow;
import com.purplebits.emrd2.repositories.ContractWorkflowBindingRepository;
import com.purplebits.emrd2.repositories.WorkflowDefinitionRepository;
import com.purplebits.emrd2.service.WorkflowDefinitionService;
import com.purplebits.emrd2.types.DependencyType;
import com.purplebits.emrd2.util.ObjectMapperUtils;

@Service
public class WorkflowDefinitionServiceImpl implements WorkflowDefinitionService {

	private final Logger logger = LogManager.getLogger(WorkflowDefinitionServiceImpl.class);
	private final String className = WorkflowDefinitionServiceImpl.class.getSimpleName();
	@Autowired
	Environment environment;

	@Autowired
	private WorkflowDefinitionRepository workflowDefinitionRepository;
	
	@Override
	public StepDTO getStepByWorkFlowId(Integer workFlowId, DependencyType dependencyType) {
		logger.info("method to get Independent Step by workflow id invoked. " + workFlowId);
		logger.debug(className + " getStepByWorkFlowId()");
		Optional<Step> stepOp = workflowDefinitionRepository.findIndependentStepsByDependencyType(workFlowId, dependencyType);
		if(stepOp.isPresent()) {
			return ObjectMapperUtils.map(stepOp.get(), StepDTO.class);
		}
		return null;
	}

	@Override
	public List<StepDTO> getNextStepByWorkflowIdAndDependentOn(Integer workflowId, Integer stepId) {
		logger.info("method to get next step by workflowId and dependentOn. ");
		logger.debug(className + " getNextStepByWorkflowIdAndDependentOn()");
		List<Step> steps = workflowDefinitionRepository.
				findStepsByWorkflowIdAndDependentOn(workflowId, stepId);
		return ObjectMapperUtils.mapAll(steps, StepDTO.class);
	}

}
