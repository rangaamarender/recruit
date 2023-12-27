package com.lucid.recruit.referencedata.dto;

import com.lucid.recruit.worker.entity.WorkerTypeCode;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public class RelatedWorkerTypeDTO {

	@NotNull
	private WorkerTypeCode workerTypeCode;
	@Nullable
	private String name;

	public RelatedWorkerTypeDTO() {
		super();
	}	

	public RelatedWorkerTypeDTO(@NotNull WorkerTypeCode workerTypeCode) {
		this.workerTypeCode = workerTypeCode;
	}

	public WorkerTypeCode getWorkerTypeCode() {
		return workerTypeCode;
	}

	public void setWorkerTypeCode(WorkerTypeCode workerTypeCode) {
		this.workerTypeCode = workerTypeCode;
	}

	@Nullable
	public String getName() {
		return name;
	}

	public void setName(@Nullable String name) {
		this.name = name;
	}
}
