package com.lucid.recruit.contract.dto;

import com.lucid.core.dto.BaseAddressDTO;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/*
 * Contract work location
 */
public class ContractWorkLocationDTO extends BaseAddressDTO {

	private String contractwlID;

	// Work Location name
	@NotBlank
	private String workLocationName;


	// List of charge codes associated
	@Nullable
	private List<WorkOrderChargeCodeDTO> chargeCodes;

	public String getContractwlID() {
		return contractwlID;
	}

	public void setContractwlID(String contractwlID) {
		this.contractwlID = contractwlID;
	}

	public String getWorkLocationName() {
		return workLocationName;
	}

	public void setWorkLocationName(String workLocationName) {
		this.workLocationName = workLocationName;
	}

	@Nullable
	public List<WorkOrderChargeCodeDTO> getChargeCodes() {
		return chargeCodes;
	}

	public void setChargeCodes(@Nullable List<WorkOrderChargeCodeDTO> chargeCodes) {
		this.chargeCodes = chargeCodes;
	}
}
