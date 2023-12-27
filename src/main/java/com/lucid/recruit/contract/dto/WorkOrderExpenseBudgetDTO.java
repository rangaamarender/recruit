
package com.lucid.recruit.contract.dto;

import com.lucid.recruit.contract.constants.EnumRateUnits;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class WorkOrderExpenseBudgetDTO {
	@Nullable
	private String expenseBudgetId;

	// start date of this record
	@NotNull
	private LocalDate startDate;

	// end date of this record
	@Nullable
	private LocalDate endDate;

	// rate
	@NotNull
	private double rate;

	@NotNull
	private EnumRateUnits rateFrequency;

	// rate
	@NotBlank
	private String rateDesc;

	public WorkOrderExpenseBudgetDTO() {
		super();
	}

	public String getExpenseBudgetId() {
		return expenseBudgetId;
	}

	public void setExpenseBudgetId(String expenseBudgetId) {
		this.expenseBudgetId = expenseBudgetId;
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

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public EnumRateUnits getRateFrequency() {
		return rateFrequency;
	}

	public void setRateFrequency(EnumRateUnits rateFrequency) {
		this.rateFrequency = rateFrequency;
	}

	public String getRateDesc() {
		return rateDesc;
	}

	public void setRateDesc(String rateDesc) {
		this.rateDesc = rateDesc;
	}

}
