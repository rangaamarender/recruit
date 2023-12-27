package com.lucid.recruit.contract.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class WorkOrderChargeCodeDTO{

   @Nullable
	private String chargeCodeId;

	// charge code name
	@NotBlank
	private String chargeCodeName;

	// start date of the charge code
	@NotNull
	private LocalDate startDate;

	// end date of the charge code
	// null represent active still work order end
	@Nullable
	private LocalDate endDate;

	// List of tasks
	@Nullable
	private List<ChargeCodeTasksDTO> chargeCodeTasks;

	// List of work order rates
	@Nullable
	private List<ChargeCodeRateDTO> workOrderRates;

	// List of allowed expenses
	@Nullable
	private List<WorkOrderExpenseCodesDTO> workOrderExpenses;

	@Nullable
	private List<ChargeCodeResourceDTO> contractResources;

	@Nullable
	public String getChargeCodeId() {
		return chargeCodeId;
	}

	public void setChargeCodeId(@Nullable String chargeCodeId) {
		this.chargeCodeId = chargeCodeId;
	}

	public String getChargeCodeName() {
		return chargeCodeName;
	}

	public void setChargeCodeName(String chargeCodeName) {
		this.chargeCodeName = chargeCodeName;
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

	@Nullable
	public List<ChargeCodeTasksDTO> getChargeCodeTasks() {
		return chargeCodeTasks;
	}

	public void setChargeCodeTasks(@Nullable List<ChargeCodeTasksDTO> chargeCodeTasks) {
		this.chargeCodeTasks = chargeCodeTasks;
	}

	@Nullable
	public List<ChargeCodeRateDTO> getWorkOrderRates() {
		return workOrderRates;
	}

	public void setWorkOrderRates(@Nullable List<ChargeCodeRateDTO> workOrderRates) {
		this.workOrderRates = workOrderRates;
	}

	@Nullable
	public List<WorkOrderExpenseCodesDTO> getWorkOrderExpenses() {
		return workOrderExpenses;
	}

	public void setWorkOrderExpenses(@Nullable List<WorkOrderExpenseCodesDTO> workOrderExpenses) {
		this.workOrderExpenses = workOrderExpenses;
	}

	@Nullable
	public List<ChargeCodeResourceDTO> getContractResources() {
		return contractResources;
	}

	public void setContractResources(@Nullable List<ChargeCodeResourceDTO> contractResources) {
		this.contractResources = contractResources;
	}

}
