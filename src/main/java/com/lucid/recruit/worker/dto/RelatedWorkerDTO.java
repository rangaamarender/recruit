package com.lucid.recruit.worker.dto;

import com.lucid.recruit.person.dto.RelatedPersonLegalDTO;
import com.lucid.recruit.referencedata.dto.WorkerTypeDTO;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedWorkerDTO {
	@NotNull
	private String workerID;
	@Nullable
	private WorkerTypeDTO workerType;
	@Nullable	
	private RelatedPersonLegalDTO personLegal;

	public String getWorkerID() {
		return workerID;
	}

	public void setWorkerID(String workerID) {
		this.workerID = workerID;
	}

	public WorkerTypeDTO getWorkerType() {
		return workerType;
	}

	public void setWorkerType(WorkerTypeDTO workerType) {
		this.workerType = workerType;
	}

	@Nullable
	public RelatedPersonLegalDTO getPersonLegal() {return personLegal;}

	public void setPersonLegal(@Nullable RelatedPersonLegalDTO personLegal) {this.personLegal = personLegal;}
}
