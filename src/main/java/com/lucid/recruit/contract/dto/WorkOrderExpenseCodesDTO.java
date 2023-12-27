package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class WorkOrderExpenseCodesDTO {

	@Nullable
	private String expenseCodeId;

	// expense code name
	@NotBlank
	private String expenseCodeName;

	// can bill to client
	@NotNull
	private boolean billable;

	// start date of the expense
	@NotNull
	private LocalDate startDate;

	// end date of the expense
	@Nullable
	private LocalDate endDate;


	public WorkOrderExpenseCodesDTO() {
		super();
	}

	public String getExpenseCodeId() {
		return expenseCodeId;
	}

	public void setExpenseCodeId(String expenseCodeId) {
		this.expenseCodeId = expenseCodeId;
	}

	public String getExpenseCodeName() {
		return expenseCodeName;
	}

	public void setExpenseCodeName(String expenseCodeName) {
		this.expenseCodeName = expenseCodeName;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
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


}
