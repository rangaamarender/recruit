/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * 
 * Contract work order group, groups all the work orders having same discounts
 * Billing characteristics
 *
 */
public class RelatedContractAccountDTO {

	@Nullable
	private String contractAccountId;

	// Name (title) of the MSA
	@NotBlank
	private String contractAccountName;

	// MSA notes
	@Nullable
	private String notes;

	// Date when the next bill shall be generated
	@Nullable
	private LocalDate nextBillDate;

	// Date when the last bill was generated
	@Nullable
	private LocalDate lastBillDate;
	@Nullable
	private List<RelatedContractWorkOrderDTO> contractWorkOrders;

	@Nullable
	public String getContractAccountId() {
		return contractAccountId;
	}

	public void setContractAccountId(@Nullable String contractAccountId) {
		this.contractAccountId = contractAccountId;
	}

	public String getContractAccountName() {
		return contractAccountName;
	}

	public void setContractAccountName(String contractAccountName) {
		this.contractAccountName = contractAccountName;
	}

	@Nullable
	public String getNotes() {
		return notes;
	}

	public void setNotes(@Nullable String notes) {
		this.notes = notes;
	}

	@Nullable
	public LocalDate getNextBillDate() {
		return nextBillDate;
	}

	public void setNextBillDate(@Nullable LocalDate nextBillDate) {
		this.nextBillDate = nextBillDate;
	}

	@Nullable
	public LocalDate getLastBillDate() {
		return lastBillDate;
	}

	public void setLastBillDate(@Nullable LocalDate lastBillDate) {
		this.lastBillDate = lastBillDate;
	}

	@Nullable
	public List<RelatedContractWorkOrderDTO> getContractWorkOrders() {
		return contractWorkOrders;
	}

	public void setContractWorkOrders(@Nullable List<RelatedContractWorkOrderDTO> contractWorkOrders) {
		this.contractWorkOrders = contractWorkOrders;
	}
}
