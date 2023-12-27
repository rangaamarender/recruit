package com.lucid.recruit.timecard.dto;

import com.lucid.recruit.contract.dto.RelatedContractDTO;
import com.lucid.recruit.timecard.constants.EnumTimeCardStatus;
import com.lucid.recruit.worker.dto.RelatedWorkerDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class TimeCardDTO {
	@Nullable
	private String timecardID;

	@NotNull(message = "Worker details are required")
	@Valid
	private RelatedWorkerDTO worker;

	@NotNull(message = "contract details are required")
	@Valid
	private RelatedContractDTO contract;

	@Nullable
	private EnumTimeCardStatus status;

	@Nullable
	public String getTimecardID() {
		return timecardID;
	}

	public void setTimecardID(@Nullable String timecardID) {
		this.timecardID = timecardID;
	}

	public RelatedWorkerDTO getWorker() {
		return worker;
	}

	public void setWorker(RelatedWorkerDTO worker) {
		this.worker = worker;
	}

	public RelatedContractDTO getContract() {
		return contract;
	}

	public void setContract(RelatedContractDTO contract) {
		this.contract = contract;
	}

	public EnumTimeCardStatus getStatus() {
		return status;
	}

	public void setStatus(EnumTimeCardStatus status) {
		this.status = status;
	}

}
