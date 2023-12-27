package com.lucid.recruit.timecard.dto;

import java.time.LocalDate;

import com.lucid.core.embeddable.Amount;
import com.lucid.recruit.contract.dto.RelatedWorkOrderExpenseCodesDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class TimeCardExpenseDTO {
	@Nullable
	private String expenseID;
	@Nullable
	private String name;
	@NotNull
	private LocalDate expenseDate;
	@NotNull
	@Valid
	private RelatedWorkOrderExpenseCodesDTO expenseCode;

	@NotNull
	@Valid
	private Amount amount;

	public String getExpenseID() {
		return expenseID;
	}

	public void setExpenseID(String expenseID) {
		this.expenseID = expenseID;
	}

	@Nullable
	public String getName() {
		return name;
	}

	public void setName(@Nullable String name) {
		this.name = name;
	}

	public LocalDate getExpenseDate() {
		return expenseDate;
	}

	public void setExpenseDate(LocalDate expenseDate) {
		this.expenseDate = expenseDate;
	}

	public RelatedWorkOrderExpenseCodesDTO getExpenseCode() {
		return expenseCode;
	}

	public void setExpenseCode(RelatedWorkOrderExpenseCodesDTO expenseCode) {
		this.expenseCode = expenseCode;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

}
