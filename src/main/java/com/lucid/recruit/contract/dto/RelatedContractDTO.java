package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/***
 * 
 * DTO represent the related contract
 *
 */
public class RelatedContractDTO {
	@NotNull
	private String contractID;
	@Nullable
	private String contractName;

	@NotNull
	@Valid
	private RelatedWorkOrderDTO workOrder;

	public String getContractID() {
		return contractID;
	}

	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public RelatedWorkOrderDTO getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(RelatedWorkOrderDTO workOrder) {
		this.workOrder = workOrder;
	}
}
