package com.purplebits.emrd2.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.ContractWorkflowBinding;
import com.purplebits.emrd2.entity.Workflow;

public interface ContractWorkflowBindingRepository extends JpaRepository<ContractWorkflowBinding, Integer> {

	 @Query("SELECT w FROM Workflow w " +
	           "JOIN ContractWorkflowBinding cwb ON w.workflowId = cwb.workflow.workflowId " +
	           "WHERE cwb.contract.contractId = :contractId AND cwb.isActive = :isActive")
	    Optional<Workflow> findActiveWorkflowByContractId(@Param("contractId") Integer contractId,@Param("isActive") boolean isActive);
}
