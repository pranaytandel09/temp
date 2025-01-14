package com.purplebits.emrd2.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class ContractDTO {
	
    private Integer contractId;
    private EntitiesDTO entity; 
    private ProjectDTO project;
    private String contractName;
    private String description;
    private Date startDate;
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private Status status;
    
    private String billingDetails;
    private String terms;
    private Integer quantity;
    private BigDecimal rate;
    private String billingUnit;
    private Integer maxFreeUnits;
    private BigDecimal additionalCostPerUnit;
    private Timestamp createdAt;

	// No-argument constructor
	public ContractDTO() {}

	// All-arguments constructor
	public ContractDTO(Integer contractId, String contractName) {
		this.contractId = contractId;
		this.contractName = contractName;
	}
    public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public EntitiesDTO getEntity() {
		return entity;
	}
	public void setEntity(EntitiesDTO entity) {
		this.entity = entity;
	}
	public ProjectDTO getProject() {
		return project;
	}
	public void setProject(ProjectDTO project) {
		this.project = project;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getBillingDetails() {
		return billingDetails;
	}
	public void setBillingDetails(String billingDetails) {
		this.billingDetails = billingDetails;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getBillingUnit() {
		return billingUnit;
	}
	public void setBillingUnit(String billingUnit) {
		this.billingUnit = billingUnit;
	}
	public Integer getMaxFreeUnits() {
		return maxFreeUnits;
	}
	public void setMaxFreeUnits(Integer maxFreeUnits) {
		this.maxFreeUnits = maxFreeUnits;
	}
	public BigDecimal getAdditionalCostPerUnit() {
		return additionalCostPerUnit;
	}
	public void setAdditionalCostPerUnit(BigDecimal additionalCostPerUnit) {
		this.additionalCostPerUnit = additionalCostPerUnit;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "ContractDTO [contractId=" + contractId + ", entity=" + entity + ", project=" + project
				+ ", contractName=" + contractName + ", description=" + description + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", status=" + status + ", billingDetails=" + billingDetails + ", terms="
				+ terms + ", quantity=" + quantity + ", rate=" + rate + ", billingUnit=" + billingUnit
				+ ", maxFreeUnits=" + maxFreeUnits + ", additionalCostPerUnit=" + additionalCostPerUnit + ", createdAt="
				+ createdAt + "]";
	}
    
}
