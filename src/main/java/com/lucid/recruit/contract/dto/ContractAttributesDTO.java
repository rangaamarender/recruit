package com.lucid.recruit.contract.dto;

import com.lucid.recruit.attr.dto.ContractAttributeDefDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ContractAttributesDTO {

	@Nullable
	private String contractAttrID;
	@NotNull
	private ContractAttributeDefDTO contractAttributeDef;
	@Nullable
	private String attrValue;

	@Nullable
	private LocalDate startDate;

	@Nullable
	private LocalDate endDate;

	@Nullable
	public String getContractAttrID() {
		return contractAttrID;
	}

	public void setContractAttrID(@Nullable String contractAttrID) {
		this.contractAttrID = contractAttrID;
	}

	public ContractAttributeDefDTO getContractAttributeDef() {
		return contractAttributeDef;
	}

	public void setContractAttributeDef(ContractAttributeDefDTO contractAttributeDef) {
		this.contractAttributeDef = contractAttributeDef;
	}

	@Nullable
	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(@Nullable String attrValue) {
		this.attrValue = attrValue;
	}

	@Nullable
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(@Nullable LocalDate startDate) {
		this.startDate = startDate;
	}

	@Nullable
	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(@Nullable LocalDate endDate) {
		this.endDate = endDate;
	}
}
