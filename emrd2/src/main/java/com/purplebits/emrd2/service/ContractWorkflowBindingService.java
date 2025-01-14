package com.purplebits.emrd2.service;

import com.purplebits.emrd2.dto.WorkflowDTO;

public interface ContractWorkflowBindingService {

	WorkflowDTO findWorkflowByContractId(Integer contractId);
}
