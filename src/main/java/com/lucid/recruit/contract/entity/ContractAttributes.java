package com.lucid.recruit.contract.entity;

import com.lucid.recruit.attr.entity.ContractAttributeDef;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = ContractAttributes.TABLE_NAME)
public class ContractAttributes {

	public static final String TABLE_NAME = "c_contract_attributes";

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "cntrct_attr_id", nullable = false, length = 75)
	private String contractAttrID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cntrct_id", nullable = false, updatable = false)
	private Contract contract;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_attr_def_id", nullable = false, updatable = false)
	private ContractAttributeDef contractAttributeDef;

	@Column(name = "attr_value")
	private String attrValue;

	@Column(name = "start_dt",nullable = false)
	private LocalDate startDate;

	@Column(name = "end_dt")
	private LocalDate endDate;

	public String getContractAttrID() {
		return contractAttrID;
	}

	public void setContractAttrID(String contractAttrID) {
		this.contractAttrID = contractAttrID;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public ContractAttributeDef getContractAttributeDef() {
		return contractAttributeDef;
	}

	public void setContractAttributeDef(ContractAttributeDef contractAttributeDef) {
		this.contractAttributeDef = contractAttributeDef;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

}
