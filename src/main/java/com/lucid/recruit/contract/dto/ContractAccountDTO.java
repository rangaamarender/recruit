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
public class ContractAccountDTO {

	@Nullable
	private String contractAccountId;

	// Name (title) of the MSA
	@NotBlank
	private String contractAccountName;

	// MSA notes
	@Nullable
	private String notes;

	@Nullable
	private LocalDate startDate;

	// Date when the next bill shall be generated
	@Nullable
	private LocalDate nextBillDate;

	// Date when the last bill was generated
	@Nullable
	private LocalDate lastBillDate;

	@Nullable
	private List<ContractAccountStatusDTO> accountStatuses;

	@NotNull
	@Size(min = 1,message = "Billing details mandatory to create contractAccount")
	@Valid
	private List<ContractBillingDetailsDTO> contractBillingDetails;

	@Nullable
	@Valid
	private List<ContractDiscountDTO> contractDiscounts;

	@Nullable
	@Valid
	private List<ContractExpenseDTO> contractExpenses;

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
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(@Nullable LocalDate startDate) {
		this.startDate = startDate;
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
	public List<ContractAccountStatusDTO> getAccountStatuses() {
		return accountStatuses;
	}

	public void setAccountStatuses(@Nullable List<ContractAccountStatusDTO> accountStatuses) {
		this.accountStatuses = accountStatuses;
	}

	@Nullable
	public List<ContractBillingDetailsDTO> getContractBillingDetails() {
		return contractBillingDetails;
	}

	public void setContractBillingDetails(@Nullable List<ContractBillingDetailsDTO> contractBillingDetails) {
		this.contractBillingDetails = contractBillingDetails;
	}

	@Nullable
	public List<ContractDiscountDTO> getContractDiscounts() {
		return contractDiscounts;
	}

	public void setContractDiscounts(@Nullable List<ContractDiscountDTO> contractDiscounts) {
		this.contractDiscounts = contractDiscounts;
	}

	@Nullable
	public List<ContractExpenseDTO> getContractExpenses() {
		return contractExpenses;
	}

	public void setContractExpenses(@Nullable List<ContractExpenseDTO> contractExpenses) {
		this.contractExpenses = contractExpenses;
	}
}
