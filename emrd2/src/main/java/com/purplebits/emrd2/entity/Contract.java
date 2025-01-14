package com.purplebits.emrd2.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.purplebits.emrd2.types.Status;
import com.purplebits.emrd2.util.JsonUtils;

@Entity
@Table(name = "Contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContractID")
    private Integer contractId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EntityID", nullable = false)
    private Entities entity; 

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ProjectID", nullable = false)
    private Project project; 

    @Column(name = "ContractName")
    private String contractName;

    @Lob
    @Column(name = "Description")
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "StartDate")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "EndDate")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status;

    @Lob
    @Column(name = "BillingDetails")
    private String billingDetails;

    @Lob
    @Column(name = "Terms")
    private String terms;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Rate", precision = 10, scale = 2)
    private BigDecimal rate;

    @Column(name = "BillingUnit")
    private String billingUnit;

    @Column(name = "MaxFreeUnits")
    private Integer maxFreeUnits;

    @Column(name = "AdditionalCostPerUnit", precision = 10, scale = 2)
    private BigDecimal additionalCostPerUnit;

    @CreationTimestamp
    @Column(name = "CreatedAt", updatable = false)
    private Timestamp createdAt;

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Entities getEntity() {
		return entity;
	}

	public void setEntity(Entities entity) {
		this.entity = entity;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
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
		return "Contract [contractId=" + contractId + ", entity=" + entity + ", project=" + project + ", contractName="
				+ contractName + ", description=" + description + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", status=" + status + ", billingDetails=" + billingDetails + ", terms=" + terms + ", quantity="
				+ quantity + ", rate=" + rate + ", billingUnit=" + billingUnit + ", maxFreeUnits=" + maxFreeUnits
				+ ", additionalCostPerUnit=" + additionalCostPerUnit + ", createdAt=" + createdAt + "]";
	}
    
	
    
}
