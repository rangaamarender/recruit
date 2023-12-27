package com.lucid.recruit.referencedata.entity;

import com.lucid.recruit.worker.entity.WorkerTypeCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = WorkerType.TABLE_NAME)
public class WorkerType {
	public static final String TABLE_NAME = "ref_workerType";

	@Id
	@Enumerated(EnumType.STRING)
	@Column(name = "worker_type_code", nullable = false, updatable = false)
	private WorkerTypeCode workerTypeCode;

	@Column(name = "worker_type_name", nullable = false)
	private String workerTypeName;

	@Column(name = "worker_type_desc", nullable = true)
	private String workerTypeDesc;

	public WorkerType() {
		super();

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

	public String getWorkerTypeDesc() {
		return workerTypeDesc;
	}

	public void setWorkerTypeDesc(String workerTypeDesc) {
		this.workerTypeDesc = workerTypeDesc;
	}

}
