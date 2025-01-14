package com.purplebits.emrd2.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.purplebits.emrd2.types.DependencyType;
import com.purplebits.emrd2.util.JsonUtils;

public class WorkflowDefinitionDTO {

	private Integer workflowDefinitionId;
	private WorkflowDTO workflow;
	private StepDTO step;
	private Integer sequence;
	private boolean isMandatory;
	private Integer dependentOn;

	@Enumerated(EnumType.STRING)
	private DependencyType dependencyType;

	public Integer getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}

	public void setWorkflowDefinitionId(Integer workflowDefinitionId) {
		this.workflowDefinitionId = workflowDefinitionId;
	}

	public WorkflowDTO getWorkflow() {
		return workflow;
	}

	public void setWorkflow(WorkflowDTO workflow) {
		this.workflow = workflow;
	}

	public StepDTO getStep() {
		return step;
	}

	public void setStep(StepDTO step) {
		this.step = step;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public DependencyType getDependencyType() {
		return dependencyType;
	}

	public void setDependencyType(DependencyType dependencyType) {
		this.dependencyType = dependencyType;
	}

	public Integer getDependentOn() {
		return dependentOn;
	}

	public void setDependentOn(Integer dependentOn) {
		this.dependentOn = dependentOn;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
