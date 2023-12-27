/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.dto;

import com.lucid.core.embeddable.Amount;
import com.lucid.recruit.contract.constants.EnumRateCategory;
import com.lucid.recruit.contract.constants.EnumRateUnits;

import java.time.LocalDate;

/**
 * 
 * Table holds the details of W/O rates for T&M contracts.
 */

public class ContractWorkOrderRateDTO{

	private String woRateID;


	// start date of the work order rate
	private LocalDate startDate;

	// end date of the work order rate
	// null represent active still work order end
	private LocalDate endDate;

	private EnumRateCategory rateCategory;

	private Amount amount;

	private EnumRateUnits rateUnits;

	public ContractWorkOrderRateDTO() {
		super();
	}

	public String getWoRateID() {
		return woRateID;
	}

	public void setWoRateID(String woRateID) {
		this.woRateID = woRateID;
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

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public EnumRateUnits getRateUnits() {
		return rateUnits;
	}

	public void setRateUnits(EnumRateUnits rateUnits) {
		this.rateUnits = rateUnits;
	}

}
