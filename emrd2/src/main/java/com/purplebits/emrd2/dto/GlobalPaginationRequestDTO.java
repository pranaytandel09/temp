package com.purplebits.emrd2.dto;

public class GlobalPaginationRequestDTO {

	private PaginationRequestDTO paginationRequest;
	private Integer entityId;
	private Integer projectId;
	private int contractId;
	public PaginationRequestDTO getPaginationRequest() {
		return paginationRequest;
	}
	public void setPaginationRequest(PaginationRequestDTO paginationRequest) {
		this.paginationRequest = paginationRequest;
	}
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public int getContractId() {
		return contractId;
	}
	public void setContractId(int contractId) {
		this.contractId = contractId;
	}
	
	
}
