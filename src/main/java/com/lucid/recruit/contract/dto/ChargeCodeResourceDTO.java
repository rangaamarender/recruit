/**
 * All Rights Reserved. Private and Confidential. May not be disclosed without permission.
 */
package com.lucid.recruit.contract.dto;

import java.time.LocalDate;
import java.util.List;

import com.lucid.recruit.worker.dto.RelatedWorkerDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class ChargeCodeResourceDTO {

	// UUID for each worker associated
	@Nullable
	private String contractResourceID;

	// UUID of worker
	@NotNull
	private RelatedWorkerDTO worker;

	// start date of this record
	@NotNull
	private LocalDate startDate;

	// end date of this record
	@Nullable
	private LocalDate endDate;

	@Nullable
	private List<WorkOrderExpenseBudgetDTO> payProfile;

	@Nullable
	public String getContractResourceID() {
		return contractResourceID;
	}

	public void setContractResourceID(@Nullable String contractResourceID) {
		this.contractResourceID = contractResourceID;
	}

	public RelatedWorkerDTO getWorker() {
		return worker;
	}

	public void setWorker(RelatedWorkerDTO worker) {
		this.worker = worker;
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
	public List<WorkOrderExpenseBudgetDTO> getPayProfile() {
		return payProfile;
	}

	public void setPayProfile(@Nullable List<WorkOrderExpenseBudgetDTO> payProfile) {
		this.payProfile = payProfile;
	}


}
