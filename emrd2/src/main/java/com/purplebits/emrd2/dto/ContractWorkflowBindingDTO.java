package com.purplebits.emrd2.dto;

import java.util.Date;

import com.purplebits.emrd2.util.JsonUtils;

public class ContractWorkflowBindingDTO {

	private Integer contractWorkflowBindingId;
    private ContractDTO contract;
    private WorkflowDTO workflow;
    private boolean isActive;
    private Date startDate;
    private Date endDate;
	public Integer getContractWorkflowBindingId() {
		return contractWorkflowBindingId;
	}
	public void setContractWorkflowBindingId(Integer contractWorkflowBindingId) {
		this.contractWorkflowBindingId = contractWorkflowBindingId;
	}
	public ContractDTO getContract() {
		return contract;
	}
	public void setContract(ContractDTO contract) {
		this.contract = contract;
	}
	public WorkflowDTO getWorkflow() {
		return workflow;
	}
	public void setWorkflow(WorkflowDTO workflow) {
		this.workflow = workflow;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
    
	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}
}
