package com.lucid.recruit.referencedata.dto;

import com.lucid.recruit.worker.entity.WorkerTypeCode;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WorkerTypeDTO {
	@Nullable
	private WorkerTypeCode workerTypeCode;
	@Nullable
	private String workerTypeName;
	@Nullable
	private String workerTypeDesc;

	public WorkerTypeDTO() {
		super();
	}

	public WorkerTypeDTO( WorkerTypeCode workerTypeCode, String workerTypeName, String workerTypeDesc) {
		super();
		this.workerTypeCode = workerTypeCode;
		this.workerTypeName = workerTypeName;
		this.workerTypeDesc = workerTypeDesc;
	}

	public WorkerTypeCode getWorkerTypeCode() {
		return workerTypeCode;
	}

	public void setWorkerTypeCode(WorkerTypeCode workerTypeCode) {
		this.workerTypeCode = workerTypeCode;
	}

	public String getWorkerTypeName() {
		return workerTypeName;
	}

	public void setWorkerTypeName(String workerTypeName) {
		this.workerTypeName = workerTypeName;
	}

	public String getWorkerTypeDesc() {return workerTypeDesc;}

	public void setWorkerTypeDesc(String workerTypeDesc) {this.workerTypeDesc = workerTypeDesc;}

}
