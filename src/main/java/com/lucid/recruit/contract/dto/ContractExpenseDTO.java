package com.lucid.recruit.contract.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ContractExpenseDTO {


	// UUID for each discount
	@Nullable
	private String expenseId;

	// Name of the discount, will be displayed on invoice if required
	@NotBlank
	private String expenseName;

	// Name of the discount description
	@Nullable
	private String expenseDesc;

	// total amount of the discounted amount, null if and only if discount type is
	// percentage
	@Nullable
	private double expenseAmount;

	// Date from which discount is active
	@NotNull
	private LocalDate startDate;

	// Last day on of the discount
	@Nullable
	private LocalDate endDate;

	@Nullable
	public String getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(@Nullable String expenseId) {
		this.expenseId = expenseId;
	}

	public String getExpenseName() {
		return expenseName;
	}

	public void setExpenseName(String expenseName) {
		this.expenseName = expenseName;
	}

	@Nullable
	public String getExpenseDesc() {
		return expenseDesc;
	}

	public void setExpenseDesc(@Nullable String expenseDesc) {
		this.expenseDesc = expenseDesc;
	}

	@Nullable
	public double getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(@Nullable double expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
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
