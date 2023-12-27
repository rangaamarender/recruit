package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedWorkOrderExpenseCodesDTO {

	@NotNull
	private String expenseCodeId;
	@Nullable
	private String expenseCodeName;

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
}
