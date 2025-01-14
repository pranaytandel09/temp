package com.purplebits.emrd2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.purplebits.emrd2.types.DependencyType;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "WorkflowDefinition")
public class WorkflowDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WorkflowDefinitionID")
    private Integer workflowDefinitionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "WorkflowID", nullable = false)
    private Workflow workflow;

    @ManyToOne
    @JoinColumn(name = "StepID")
    private Step step;

    @Column(name = "Sequence")
    private Integer sequence;

    @Column(name = "IsMandatory", columnDefinition = "bit default 0")
    private boolean isMandatory;
	@Column(name = "dependent_on")
	private Integer dependentOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "DependencyType")
    private DependencyType dependencyType;

	public Integer getWorkflowDefinitionId() {
		return workflowDefinitionId;
	}

	public void setWorkflowDefinitionId(Integer workflowDefinitionId) {
		this.workflowDefinitionId = workflowDefinitionId;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
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
