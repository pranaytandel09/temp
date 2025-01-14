package com.purplebits.emrd2.dto.request_response;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purplebits.emrd2.dto.ContractDTO;
import com.purplebits.emrd2.dto.EntitiesDTO;
import com.purplebits.emrd2.dto.ProjectDTO;
import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

public class ContractRequestResponseDTO {

	private Integer contractId;
	private Integer entityId;
	private Integer projectId;
	private String contractName;
	private String description;
	private Date startDate;
	private Date endDate;
	private String billingDetails;
	private String terms;
	private Integer quantity;
	private BigDecimal rate;
	private String billingUnit;
	private Integer maxFreeUnits;
	private BigDecimal additionalCostPerUnit;
	private Timestamp createdAt;

	@Enumerated(EnumType.STRING)
	private Status status;

	public ContractRequestResponseDTO() {
		super();
	}

	public ContractRequestResponseDTO(ContractDTO contractDTO) {
		super();
		this.contractId = contractDTO.getContractId();
		this.entityId = contractDTO.getEntity().getEntityId();
		this.projectId = contractDTO.getProject().getProjectId();
		this.contractName = contractDTO.getContractName();
		this.description = contractDTO.getDescription();
		this.startDate = contractDTO.getStartDate();
		this.endDate = contractDTO.getEndDate();
		this.billingDetails = contractDTO.getBillingDetails();
		this.terms = contractDTO.getTerms();
		this.quantity = contractDTO.getQuantity();
		this.rate = contractDTO.getRate();
		this.billingUnit = contractDTO.getBillingUnit();
		this.maxFreeUnits = contractDTO.getMaxFreeUnits();
		this.additionalCostPerUnit = contractDTO.getAdditionalCostPerUnit();
		this.createdAt = contractDTO.getCreatedAt();
		this.status = contractDTO.getStatus();
	}

	@JsonIgnore
	public ContractDTO getContractDTO() {
		ContractDTO contractDTO = new ContractDTO();
		contractDTO.setContractId(contractId);
		contractDTO.setContractName(contractName);
		contractDTO.setDescription(description);
		contractDTO.setStartDate(startDate);
		contractDTO.setEndDate(endDate);
		contractDTO.setBillingDetails(billingDetails);
		contractDTO.setTerms(terms);
		contractDTO.setQuantity(quantity);
		contractDTO.setRate(rate);
		contractDTO.setBillingUnit(billingUnit);
		contractDTO.setMaxFreeUnits(maxFreeUnits);
		contractDTO.setAdditionalCostPerUnit(additionalCostPerUnit);
		contractDTO.setCreatedAt(createdAt);
		contractDTO.setStatus(status);

		EntitiesDTO entitiesDTO = new EntitiesDTO();
		entitiesDTO.setEntityId(entityId);
		contractDTO.setEntity(entitiesDTO);

		ProjectDTO projectsDTO = new ProjectDTO();
		projectsDTO.setProjectId(projectId);
		contractDTO.setProject(projectsDTO);
		return contractDTO;
	}

	public static List<ContractRequestResponseDTO> getContractRequestResponseDTO(List<ContractDTO> contractDTOs) {
		List<ContractRequestResponseDTO> res = new ArrayList<>();
		for (ContractDTO contract : contractDTOs) {
			res.add(new ContractRequestResponseDTO(contract));
		}
		return res;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return JsonUtils.createGson().toJson(this);
	}

}
