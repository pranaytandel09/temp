package com.purplebits.emrd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.purplebits.emrd2.entity.Step;
import com.purplebits.emrd2.entity.WorkflowDefinition;
import com.purplebits.emrd2.types.DependencyType;

public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, Integer> {

	@Query("SELECT s FROM Step s " +
	           "JOIN WorkflowDefinition wd ON s.stepId = wd.step.stepId " +
	           "WHERE wd.workflow.workflowId = :workflowId " +
	           "AND wd.dependencyType = :dependencyType ")
	    Optional<Step> findIndependentStepsByDependencyType(@Param("workflowId") Integer workflowId, @Param("dependencyType") DependencyType dependencyType);
	
	@Query("SELECT wd.step FROM WorkflowDefinition wd " +
	           "WHERE wd.workflow.workflowId = :workflowId " +
	           "AND wd.dependentOn = :dependentOn")
	    List<Step> findStepsByWorkflowIdAndDependentOn(@Param("workflowId") Integer workflowId,
	                                                   @Param("dependentOn") Integer dependentOn);
}
