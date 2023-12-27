package com.lucid.recruit.worker.dto;

import com.lucid.recruit.person.dto.PersonLegalSummaryDTO;
import com.lucid.recruit.referencedata.dto.RelatedWorkerTypeDTO;
import com.lucid.recruit.referencedata.entity.WorkerType;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class WorkerSummaryDTO {
	@Nullable
	private String workerID;
	@Nullable
	private String workerCode;
	@Nullable
	private LocalDate joiningDate;

	@NotNull
	private RelatedWorkerTypeDTO workerType;

	private PersonLegalSummaryDTO personLegal;

	private WorkerCurrentStatusDTO workerStatus;

	public WorkerSummaryDTO() {
		super();
	}

	public WorkerSummaryDTO(String workerID, String workerCode, @NotNull RelatedWorkerTypeDTO workerType,
			PersonLegalSummaryDTO personLegal, WorkerCurrentStatusDTO workerStatus) {
		super();
		this.workerID = workerID;
		this.workerCode = workerCode;
		this.workerType = workerType;
		this.personLegal = personLegal;
		this.workerStatus = workerStatus;
	}

	public String getWorkerID() {
		return workerID;
	}

	public void setWorkerID(String workerID) {
		this.workerID = workerID;
	}

	public String getWorkerCode() {
		return workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	public RelatedWorkerTypeDTO getWorkerType() {
		return workerType;
	}

	public void setWorkerType(RelatedWorkerTypeDTO workerType) {
		this.workerType = workerType;
	}

	public PersonLegalSummaryDTO getPersonLegal() {
		return personLegal;
	}

	public void setPersonLegal(PersonLegalSummaryDTO personLegal) {
		this.personLegal = personLegal;
	}

	public WorkerCurrentStatusDTO getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(WorkerCurrentStatusDTO workerStatus) {
		this.workerStatus = workerStatus;
	}

	@Nullable
	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(@Nullable LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}
}
