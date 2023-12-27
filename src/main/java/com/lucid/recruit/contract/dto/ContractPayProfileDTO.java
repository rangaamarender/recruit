/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.dto;

import com.lucid.core.embeddable.Amount;
import com.lucid.recruit.contract.constants.EnumRateCategory;
import com.lucid.recruit.contract.constants.EnumRateUnits;

import java.time.LocalDate;

public class ContractPayProfileDTO{


	private String contractPayProfileID;

	// start date of this record
	private LocalDate startDate;

	// end date of this record
	private LocalDate endDate;

	private EnumRateCategory rateCategory;

	private EnumRateUnits rateUnits;

	private Amount amount;

	public ContractPayProfileDTO() {
		super();
	}

	public String getContractPayProfileID() {
		return contractPayProfileID;
	}

	public void setContractPayProfileID(String contractPayProfileID) {
		this.contractPayProfileID = contractPayProfileID;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public EnumRateCategory getRateCategory() {
		return rateCategory;
	}

	public void setRateCategory(EnumRateCategory rateCategory) {
		this.rateCategory = rateCategory;
	}

	public EnumRateUnits getRateUnits() {
		return rateUnits;
	}

	public void setRateUnits(EnumRateUnits rateUnits) {
		this.rateUnits = rateUnits;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}
}
