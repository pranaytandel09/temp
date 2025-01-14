package com.purplebits.emrd2.service;

import java.util.List;

import com.purplebits.emrd2.dto.StepDTO;
import com.purplebits.emrd2.types.DependencyType;

public interface WorkflowDefinitionService {

	StepDTO getStepByWorkFlowId(Integer workFlowId,DependencyType dependencyType);

	List<StepDTO> getNextStepByWorkflowIdAndDependentOn(Integer workflowId, Integer stepId);
}
