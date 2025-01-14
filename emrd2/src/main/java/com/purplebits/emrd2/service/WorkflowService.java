package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.WorkflowDTO;

public interface WorkflowService {

	WorkflowDTO createNewWorkFlow(WorkflowDTO workflowDTO);
	WorkflowDTO updateWorkFlow(WorkflowDTO workflowDTO);
	WorkflowDTO deleteWorkFlow(WorkflowDTO workflowDTO);
	WorkflowDTO getWorkFlowById(Integer workflowId);
}
