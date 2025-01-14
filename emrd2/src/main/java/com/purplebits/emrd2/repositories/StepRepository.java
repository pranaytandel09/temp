package com.purplebits.emrd2.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Step;
import com.purplebits.emrd2.types.DependencyType;

public interface StepRepository extends JpaRepository<Step, Integer> {
	
	@Query("SELECT s " +
		       "FROM Step s " +
		       "JOIN WorkflowDefinition wd ON wd.step.stepId = s.stepId " +
		       "JOIN ContractWorkflowBinding cwb ON cwb.workflow.workflowId = wd.workflow.workflowId " +
		       "WHERE cwb.contract.contractId = :contractId " +
		       "AND wd.dependencyType = :dependencyType")
		Page<Step> findStepsByContractIdAndDependencyType(@Param("contractId") int contractId, 
		                                                  @Param("dependencyType") DependencyType dependencyType,Pageable pageable);



}
